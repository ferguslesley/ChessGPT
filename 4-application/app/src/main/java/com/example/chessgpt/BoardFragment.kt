package com.example.chessgpt

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chessgpt.board.blackKingDead
import com.example.chessgpt.board.boardList
import com.example.chessgpt.board.convertToIndex
import com.example.chessgpt.board.initPiece
import com.example.chessgpt.board.movePiece
import com.example.chessgpt.board.placePiece
import com.example.chessgpt.board.setupBoard
import com.example.chessgpt.board.whiteKingDead
import com.example.chessgpt.moves.MovesAdapter
import com.example.chessgpt.openai.ApiService
import com.example.chessgpt.openai.ApiServiceImpl
import com.example.chessgpt.openai.OpenAi
import com.example.chessgpt.piece.Piece
import com.example.chessgpt.piece.PieceColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IndexOutOfBoundsException
import java.util.concurrent.Executors

class BoardFragment : Fragment() {

    private lateinit var chessboardGrid: GridLayout
    private lateinit var boardImage: ImageView
    private lateinit var prompt: String
    private lateinit var playerMoves: MutableList<String>
    private lateinit var aiMoves: MutableList<String>
    private lateinit var instructionText: TextView
    private lateinit var viewModel: UserViewModel
    private lateinit var formattedMoves: MutableList<Pair<String, String>>
    private lateinit var pairBuilder: MutableList<String>
    private lateinit var movesRecyclerView: RecyclerView
    private lateinit var apiService: ApiService
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        return inflater.inflate(R.layout.fragment_board, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val startButton: Button = view.findViewById(R.id.start_game)
        val endButton: Button = view.findViewById(R.id.end_game)
        val settingsButton: Button = view.findViewById(R.id.settings_button)

        chessboardGrid = view.findViewById(R.id.chessboard_grid)
        boardImage = view.findViewById(R.id.board_image)
        instructionText = view.findViewById(R.id.instruction_text)
        instructionText.text = getString(R.string.initial_instruction_text)

        playerMoves = mutableListOf()
        aiMoves = mutableListOf()

        pairBuilder = mutableListOf()
        formattedMoves = mutableListOf()

        movesRecyclerView = view.findViewById(R.id.player_moves_recycler_view)
        movesRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            true
        )
        movesRecyclerView.adapter = MovesAdapter(formattedMoves)

        apiService = ApiServiceImpl()

        startButton.setOnClickListener {
            onStartButtonClick()
        }

        endButton.setOnClickListener {
            onEndButtonClick()
        }

        settingsButton.setOnClickListener {
            onSettingsButtonClick()
        }
    }

    private fun onStartButtonClick() {
        Toast.makeText(requireContext(), "Starting Game!", Toast.LENGTH_SHORT).show()
        setupBoard()
        drawPieces()
        setupAi()
        formattedMoves = mutableListOf()
        movesRecyclerView.adapter = MovesAdapter(formattedMoves)
        instructionText.text = getString(R.string.pre_first_move_text)
    }

    private fun setupAi() {
        playerMoves = mutableListOf()
        aiMoves = mutableListOf()
//        prompt = "You are playing chess as black. (Your pieces are on rows 8 and 7 to start)\n" +
//                "It is VITAL that you respond ONLY in this EXACT format:\n" +
//                "[chess piece][coordinate] -> [new coordinate]\n" +
//                "for example\n" +
//                "Knight d4 -> f5\n" +
//                "the arrow (->) is ESSENTIAL.\n" +
//                "do not include any other symbols or letters. it must be this exact format.\n" +
//                "\n" +
//                "My first move will come after you respond with 'Ok.'\n" +
//                "After that, you will ONLY respond in the EXACT format described.\n" +
//                "If you think a move was invalid, do not mention it, just continue."
        prompt = "You are playing as black in a chess game. Make your move.\n" +
                "Response should under NO circumstances violate this format:\n" +
                "[chess piece] [coordinate] -> [new coordinate]\n" +
                "You must only change what's in parentheses.\n" +
                "eg. Knight d4 -> f5"
        playerMoves.add(prompt)
        aiMoves.add("Ok.")
    }

    /**
     * Creates an invisible cell for each chess position.
     * Ensures that even if a piece disappears, there will still be a cell
     * and space on the image representing that position.
     */
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

    private fun drawPieces(): Boolean {
        setupGrid()
        return createPieces()
    }

    /**
     * Creates an Image View for each piece in the board list
     * Uses the empty cells as a guide for width and position.
     */
    private fun createPieces(): Boolean {
        val cellWidth = boardImage.width / 8
        val cellHeight = boardImage.height / 8
        if (blackKingDead()) {
            (activity as? MainActivity)?.win()
            onEndButtonClick()
            return true
        }
        if (whiteKingDead()) {
            (activity as? MainActivity)?.lose()
            onEndButtonClick()
            return true
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
        return false
    }

    /**
     * Draws a dot for each legal move.
     * Dot is set to move the piece when clicked.
     */
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
            makeMove(piece, pos[0], pos[1])
        }
        chessboardGrid.addView(pieceImage)
    }

    private fun makeMove(piece: Piece, col: Int, row: Int) {
        // Make move
        val move: String = movePiece(piece, col, row)

        // Update recorded moves
        playerMoves.add(move)
        pairBuilder.add(move)

        // Draw the pieces in their new positions
        if (drawPieces()) {
            return
        }

        makeAiMove()
        postMoveText()
    }

    /**
     * Converts a string in the format "piece location -> new location"
     * to a board position and a piece instance.
     * Creates a piece at that position, overwriting whatever is currently there,
     * then moves it to the new position.
     * This means if the AI moves a piece that doesn't exist on that
     * location, it will just spawn a new piece
     * (even if it overwrites one of white's!)
     */
    private fun parseMove(move: String) {
        val pieceToMove: String
        val chessPos: String
        val newChessPos: String
        val newPos: IntArray
        val pos: IntArray
        val piece: Piece

        // Check if response (without spaces) is an api error
        if (move.replace("\\s".toRegex(), "")
                .startsWith("Error:{\"error\":{\"message\":\"IncorrectAPIkeyprovided")) {
            // Wrong api key
            Toast.makeText(
                requireContext(),
                "The API key you provided is not correct.",
                Toast.LENGTH_SHORT
            ).show()
            (activity as? MainActivity)?.showApiKeyAlert()
            onEndButtonClick()
            return
        }

        // Check if run out of tokens for the day (app limit)
        if (move.replace("\\s".toRegex(), "")
                .startsWith("Error:Nomoretokensleftfortoday!")) {
            (activity as? MainActivity)?.showTokenLimitAlert()
            onEndButtonClick()
            return
        }

        // Check if run out of tokens for key (api key limit)
        if (move.replace("\\s".toRegex(), "")
                .startsWith("Error:{\"error\":{\"message\":\"Youexceededyourcurrentquota")) {
            Toast.makeText(
                requireContext(),
                "This API key doesn't have enough quota.",
                Toast.LENGTH_SHORT
            ).show()
            (activity as? MainActivity)?.showApiQuotaAlert()
            onEndButtonClick()
            return
        }

        try {
            pieceToMove = move.split(" ")[0]
            chessPos = move.split(" ")[1].slice(0 until 2)
            newChessPos = move.split("-> ")[1].slice(0 until 2)
            newPos = convertToIndex(newChessPos)
            pos = convertToIndex(chessPos)
            piece = initPiece(pieceToMove)
            placePiece(piece, arrayOf(pos[0], pos[1]))
            val moveNotation: String = movePiece(piece, newPos[0], newPos[1])
            aiMoves.add(moveNotation)
            pairBuilder.add(moveNotation)
            formattedMoves.add(Pair(pairBuilder[0], pairBuilder[1]))
            pairBuilder = mutableListOf()
            // Update recycler view
            movesRecyclerView.adapter!!.notifyItemInserted(formattedMoves.size)
            movesRecyclerView.postDelayed({
                movesRecyclerView.smoothScrollToPosition(formattedMoves.size - 1)
            }, 100)
            postAiMoveText()
            drawPieces()
        } catch (e: IndexOutOfBoundsException) {
            flipTable()
            return
        } catch (e: IllegalArgumentException) {
            flipTable()
            return
        } catch (e: ArrayIndexOutOfBoundsException) {
            flipTable()
            return
        }
    }

    /**
     * Launches a new thread which sends a request to the
     * OpenAI http API containing the past 5 messages
     * from the user and AI.
     * Parses the response to the function to move pieces.
     */
    private fun makeAiMove() {
        val openAiTask = OpenAi(playerMoves, aiMoves, this.requireContext(), this.apiService)

        var result: String

        lifecycleScope.launch(Dispatchers.IO) {
            result = Executors.newSingleThreadExecutor().submit(openAiTask).get()

            withContext(Dispatchers.Main) {
                parseMove(result)
            }
        }
    }

    private fun flipTable() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("ChatGPT Flipped the table!")
        builder.setMessage("Sorry, Chat GPT decided to flip the table...\n" +
                "(it responded in a way that couldn't be parsed as a move)\n" +
                "So, I guess you win! Congrats :)")
        builder.setPositiveButton("Yippee!") { _, _ ->
            onEndButtonClick()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun postMoveText() {
        instructionText.text = getString(R.string.post_move_text)
    }

    private fun postAiMoveText() {
        instructionText.text = getString(R.string.post_ai_move_text)
    }

    private fun onSettingsButtonClick() {
        val intent = Intent(requireContext(), SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun onEndButtonClick() {
        Toast.makeText(requireContext(), "Ending Game!", Toast.LENGTH_SHORT).show()
        chessboardGrid.removeAllViews()
        instructionText.text = getString(R.string.initial_instruction_text)
    }

}