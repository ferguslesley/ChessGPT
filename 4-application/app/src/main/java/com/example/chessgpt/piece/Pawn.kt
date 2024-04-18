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

    override fun getOrthogonals(givenBoardState: MutableList<MutableList<Piece?>>) {

        if (this.color == PieceColor.WHITE) {
            if (pos[1] + 1 < boardSize && getPiece(givenBoardState, arrayOf(pos[0], pos[1] + 1)) == null) {
                moves.add(intArrayOf(pos[0], pos[1] + 1))
            }
        } else {
            if (pos[1] - 1 >= 0 && getPiece(givenBoardState, arrayOf(pos[0], pos[1] - 1)) == null) {
                moves.add(intArrayOf(pos[0], pos[1] + 1))
            }
        }

    }

    override fun getDiagonals(givenBoardState: MutableList<MutableList<Piece?>>) {
        // Check if any enemy piece in diagonal
        // Query board if enemy in relative pos -1, +1 or +1, +1
        // If so, add to moves
        val upLeft: Array<Int>
        val upRight: Array<Int>
        if (this.color == PieceColor.WHITE) {
            if (pos[0] - 1 >= 0 && pos[1] + 1 < boardSize) {
                upLeft = arrayOf(pos[0] - 1, pos[1] + 1)
                if(getPiece(givenBoardState, upLeft)?.color == this.color.oppositeColor()) {
                    moves.add(upLeft.toIntArray())
                }
            }

            if (pos[0] + 1 < boardSize && pos[1] + 1 < boardSize) {
                upRight = arrayOf(pos[0] + 1, pos[1] + 1)
                if(getPiece(givenBoardState, upRight)?.color == this.color.oppositeColor()) {
                    moves.add(upRight.toIntArray())
                }
            }
        } else {
            if (pos[0] - 1 >= 0 && pos[1] - 1 >= 0) {
                upLeft = arrayOf(pos[0] - 1, pos[1] - 1)
                if(getPiece(givenBoardState, upLeft)?.color == this.color.oppositeColor()) {
                    moves.add(upLeft.toIntArray())
                }
            }

            if (pos[0] + 1 < boardSize && pos[1] - 1 >= 0) {
                upRight = arrayOf(pos[0] + 1, pos[1] - 1)
                if(getPiece(givenBoardState, upRight)?.color == this.color.oppositeColor()) {
                    moves.add(upRight.toIntArray())
                }
            }
        }
    }
}