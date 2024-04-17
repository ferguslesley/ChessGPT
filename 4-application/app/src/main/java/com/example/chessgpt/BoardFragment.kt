package com.example.chessgpt

import android.media.Image
import android.os.Bundle
import android.text.Layout
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.chessgpt.board.boardList
import com.example.chessgpt.board.setupBoard
import com.example.chessgpt.piece.Piece

class BoardFragment : Fragment() {

    private lateinit var chessboardGrid: GridLayout
    private lateinit var boardImage: ImageView

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
        chessboardGrid = view.findViewById(R.id.chessboard_grid)
        boardImage = view.findViewById(R.id.board_image)

        startButton.setOnClickListener {
            onStartButtonClick()
        }

        endButton.setOnClickListener {
            onEndButtonClick()
        }

    }

    private fun onStartButtonClick() {
        Toast.makeText(requireContext(), "Starting Game!", Toast.LENGTH_SHORT).show()
        setupGrid()
        setupBoard()
        createWhitePieces()
        createBlackPieces()
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
                    rowSpec = GridLayout.spec(i)
                    columnSpec = GridLayout.spec(j)
                }
                cellView.layoutParams = layoutParams
                chessboardGrid.addView(cellView)
            }
        }
    }

    private fun createWhitePieces() {
        val cellWidth = boardImage.width / 8
        val cellHeight = boardImage.height / 8
        for (i in 0 .. 7) {
            for (j in 0 .. 1) {
                val pieceImage = ImageView(requireContext())
                val pieceObj: Piece? = boardList[i][j]
                val layoutParams = GridLayout.LayoutParams().apply {
                    width = cellWidth
                    height = cellHeight
                    rowSpec = GridLayout.spec(j)
                    columnSpec = GridLayout.spec(i)
                }
                pieceImage.layoutParams = layoutParams
                pieceImage.visibility = View.INVISIBLE
                pieceImage.setImageResource(pieceObj!!.image)
                chessboardGrid.addView(pieceImage)
                movePiece(pieceImage, j, i)
                pieceImage.setOnClickListener{
                    onPieceClick(pieceObj)
                }
            }
        }
    }

    private fun createBlackPieces() {
        val cellWidth = boardImage.width / 8
        val cellHeight = boardImage.height / 8
        for (i in 0 .. 7) {
            for (j in 6 .. 7) {
                val pieceImage = ImageView(requireContext())
                val pieceObj: Piece? = boardList[i][j]
                val layoutParams = GridLayout.LayoutParams().apply {
                    width = cellWidth
                    height = cellHeight
                    rowSpec = GridLayout.spec(j)
                    columnSpec = GridLayout.spec(i)
                }
                pieceImage.layoutParams = layoutParams
                pieceImage.visibility = View.INVISIBLE
                pieceImage.setImageResource(pieceObj!!.image)
                chessboardGrid.addView(pieceImage)
                movePiece(pieceImage, j, i)
                pieceImage.setOnClickListener{
                    onPieceClick(pieceObj)
                }
            }
        }
    }

    private fun movePiece(pieceImage: ImageView, row: Int, col: Int) {
        // Calculate the position of the cell
        val layoutParams = pieceImage.layoutParams as GridLayout.LayoutParams
        layoutParams.rowSpec = GridLayout.spec(7 - row)
        layoutParams.columnSpec = GridLayout.spec(col)
        pieceImage.layoutParams = layoutParams

        // Make the image visible
        pieceImage.visibility = View.VISIBLE
    }

    private fun onPieceClick(piece: Piece) {
        val validMoves : Array<IntArray> = piece.getMoves()
        for (pos in validMoves) {
            showDot(pos)
        }
    }

    private fun showDot(pos: IntArray) {
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
        chessboardGrid.addView(pieceImage)
    }

    private fun onEndButtonClick() {
        Toast.makeText(requireContext(), "Ending Game!", Toast.LENGTH_SHORT).show()
        chessboardGrid.removeAllViews()
    }

}