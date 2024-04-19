package com.example.chessgpt

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.chessgpt.board.blackKingDead
import com.example.chessgpt.board.boardList
import com.example.chessgpt.board.convertToIndex
import com.example.chessgpt.board.initPiece
import com.example.chessgpt.board.movePiece
import com.example.chessgpt.board.placePiece
import com.example.chessgpt.board.setupBoard
import com.example.chessgpt.board.whiteKingDead
import com.example.chessgpt.openai.OpenAi
import com.example.chessgpt.piece.Piece
import com.example.chessgpt.piece.PieceColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

class BoardFragment : Fragment() {

    private lateinit var chessboardGrid: GridLayout
    private lateinit var boardImage: ImageView
    private lateinit var prompt: String

    private lateinit var playerMoves: MutableList<String>
    private lateinit var aiMoves: MutableList<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_board, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val startButton: Button = view.findViewById(R.id.start_game)
        val endButton: Button = view.findViewById(R.id.end_game)
        val blackMoveButton: Button = view.findViewById(R.id.black_move_button)
        val blackMoveText: EditText = view.findViewById(R.id.black_move_text)


        chessboardGrid = view.findViewById(R.id.chessboard_grid)
        boardImage = view.findViewById(R.id.board_image)

        startButton.setOnClickListener {
            onStartButtonClick()
        }

        endButton.setOnClickListener {
            onEndButtonClick()
        }

        blackMoveButton.setOnClickListener {
            val userInput = blackMoveText.text.toString()
            parseMove(userInput)
        }
    }

    private fun onStartButtonClick() {
        Toast.makeText(requireContext(), "Starting Game!", Toast.LENGTH_SHORT).show()
        setupBoard()
        drawPieces()
        setupAi()
    }

    private fun setupAi() {
        playerMoves = mutableListOf()
        aiMoves = mutableListOf()
        prompt = "You are playing chess as black. (Your pieces are on rows 8 and 7 to start)\n" +
                "Respond in this exact format:\n" +
                "[chess piece][coordinate] -> [new coordinate]\n" +
                "for example\n" +
                "Knight d4 -> f5\n" +
                "do not include any other symbols or letters. it must be this exact format.\n" +
                "\n" +
                "My first move will come after you respond with 'Ok.'\n" +
                "After that, you will ONLY respond in the EXACT format described."
        playerMoves.add(prompt)
        aiMoves.add("Ok.")
    }

    private fun setupGrid() {
        val boardWidth = boardImage.width
        val boardHeight = boardImage.height

        chessboardGrid.removeAllViews()

        chessboardGrid.layoutParams.width = boardWidth
        chessboardGrid.layoutParams.height = boardHeight

        val cellWidth = boardImage.width / 8
        val cellHeight = boardImage.height / 8

        chessboardGrid.rowCount = 8
        chessboardGrid.columnCount = 8
        for (i in 0 .. 7) {
            for (j in 0 .. 7) {
                val cellView = layoutInflater.inflate(R.layout.cell_layout, chessboardGrid, false)
                val layoutParams = GridLayout.LayoutParams().apply {
                    width = cellWidth
                    height = cellHeight
                    columnSpec = GridLayout.spec(i)
                    rowSpec = GridLayout.spec(7 - j)
                }
                cellView.layoutParams = layoutParams
                cellView.setOnClickListener {
                    drawPieces() // clears dots
                }
                chessboardGrid.addView(cellView)
            }
        }
    }

    private fun drawPieces() {
        setupGrid()
        createPieces()
    }

    private fun createPieces() {
        val cellWidth = boardImage.width / 8
        val cellHeight = boardImage.height / 8
        if (blackKingDead()) {
            (activity as? MainActivity)?.win()
            chessboardGrid.removeAllViews()
            return
        }
        if (whiteKingDead()) {
            (activity as? MainActivity)?.lose()
            chessboardGrid.removeAllViews()
            return
        }
        for (i in 0 .. 7) {
            for (j in 0 .. 7) {
                val pieceObj: Piece? = boardList[i][j]
                if (pieceObj != null) {
                    val pieceImage = ImageView(requireContext())
                    val layoutParams = GridLayout.LayoutParams().apply {
                        width = cellWidth
                        height = cellHeight
                        columnSpec = GridLayout.spec(i)
                        rowSpec = GridLayout.spec(7 - j)
                    }
                    pieceImage.layoutParams = layoutParams
                    pieceImage.setImageResource(pieceObj.image)
                    chessboardGrid.addView(pieceImage)
                    pieceImage.visibility = View.VISIBLE
                    if (pieceObj.color == PieceColor.WHITE) {
                        pieceImage.setOnClickListener{
                            onPieceClick(pieceObj)
                        }
                    }
                }
            }
        }
    }

    private fun onPieceClick(piece: Piece) {
        drawPieces()
        val validMoves : Array<IntArray> = piece.getMoves()
        for (pos in validMoves) {
            showDot(piece, pos)
        }
    }

    private fun showDot(piece: Piece, pos: IntArray) {
        val cellWidth = boardImage.width / 16
        val cellHeight = boardImage.height / 16
        val pieceImage = ImageView(requireContext())
        val layoutParams = GridLayout.LayoutParams().apply {
            width = cellWidth
            height = cellHeight
            columnSpec = GridLayout.spec(pos[0])
            rowSpec = GridLayout.spec(7 - pos[1])
            setGravity(Gravity.CENTER)
        }
        pieceImage.layoutParams = layoutParams
        pieceImage.setImageResource(R.drawable.baseline_circle_24)
        pieceImage.visibility = View.VISIBLE
        pieceImage.setOnClickListener {
            // Add move to openai conversation
            playerMoves.add(movePiece(piece, pos[0], pos[1]))
            // Draw the pieces in their new positions
            drawPieces()
            makeAiMove()
        }
        chessboardGrid.addView(pieceImage)
    }

    private fun parseMove(move: String) {
        val pieceToMove = move.split(" ")[0]
        val chessPos = move.split(" ")[1]
        val newChessPos = move.split("-> ")[1]
        val newPos = convertToIndex(newChessPos)
        val pos = convertToIndex(chessPos)
        val piece = initPiece(pieceToMove)

        placePiece(piece, arrayOf(pos[0], pos[1]))
        aiMoves.add(movePiece(piece, newPos[0], newPos[1]))
        drawPieces()
    }

    private fun makeAiMove() {
        val openAiTask = OpenAi(playerMoves, aiMoves, this.requireContext())

        var result: String

        lifecycleScope.launch(Dispatchers.IO) {
            result = Executors.newSingleThreadExecutor().submit(openAiTask).get()

            withContext(Dispatchers.Main) {
                parseMove(result)
            }
        }
    }

    private fun onEndButtonClick() {
        Toast.makeText(requireContext(), "Ending Game!", Toast.LENGTH_SHORT).show()
        chessboardGrid.removeAllViews()
    }

}