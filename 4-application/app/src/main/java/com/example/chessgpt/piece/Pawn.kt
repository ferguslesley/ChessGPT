package com.example.chessgpt.piece
import com.example.chessgpt.R
import com.example.chessgpt.board.*

class Pawn(pos: Array<Int>, color: PieceColor) : Piece(1, 0, pos, color) {
    init {
        if (color == PieceColor.WHITE) {
            image = R.drawable.pawn_white
        } else {
            image = R.drawable.pawn_black
        }

    }
    override fun calculateMoves() {
        // Get forward orthogonal movement
        super.calculateMoves()
        // TODO: Handle first move extra movement
        // TODO: Handle en passant
    }

    override fun getOrthogonals() {
        if(color == PieceColor.WHITE) {
            if(pos[1] + 1 < boardSize) {
                moves.add(intArrayOf(pos[0], pos[1] + 1))
            }
        }
        if(color == PieceColor.BLACK) {
            if(pos[1] - 1 >= 0) {
                moves.add(intArrayOf(pos[0], pos[1] - 1))
            }
        }
    }

    override fun getDiagonals() {
        // Check if any enemy piece in diagonal
        // Query board if enemy in relative pos -1, +1 or +1, +1
        // If so, add to moves
        val upLeft: Array<Int>
        val upRight: Array<Int>
        if (pos[0] - 1 >= 0 && pos[1] + 1 < boardSize) {
            upLeft = arrayOf(pos[0] - 1, pos[1] + 1)
            if(getPiece(upLeft)?.color == this.color.oppositeColor()) {
                moves.add(upLeft.toIntArray())
            }
        }

        if (pos[0] + 1 < boardSize && pos[1] + 1 < boardSize) {
            upRight = arrayOf(pos[0] + 1, pos[1] + 1)
            if(getPiece(upRight)?.color == this.color.oppositeColor()) {
                moves.add(upRight.toIntArray())
            }
        }
    }
}