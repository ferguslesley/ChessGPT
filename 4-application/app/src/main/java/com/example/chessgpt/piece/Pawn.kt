package com.example.chessgpt.piece
import com.example.chessgpt.R
import com.example.chessgpt.board.*

class Pawn(pos: Array<Int>, color: PieceColor) : Piece(2, 0, pos, color) {
    var moved: Boolean = false
    var justMoved: Boolean = false
    init {
        image = if (color == PieceColor.WHITE) {
            R.drawable.pawn_white
        } else {
            R.drawable.pawn_black
        }

    }
    override fun calculateMoves() {
        super.calculateMoves()
        enPassant()
    }

    override fun getOrthogonals(givenBoardState: MutableList<MutableList<Piece?>>) {

        if (this.color == PieceColor.BLACK) {
            orthogonalMovement = -orthogonalMovement
        }

        for (i in 1..orthogonalMovement) {
            if (pos[1] + i in 0 until boardSize) {
                if (getPiece(givenBoardState, arrayOf(pos[0], pos[1] + i)) == null) {
                    moves.add(intArrayOf(pos[0], pos[1] + i))
                } else {
                    break  // found piece, so can't go any further
                }
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

    private fun enPassant() {
        var adjacentPieceR: Piece? = null
        var adjacentPieceL: Piece? = null
        if (pos[0] + 1 < boardSize) {
            adjacentPieceR = boardList[pos[0] + 1][pos[1]]
        }
        if (pos[0] - 1 >= 0) {
            adjacentPieceL = boardList[pos[0] - 1][pos[1]]
        }

        if (adjacentPieceR is Pawn && adjacentPieceR.justMoved) {
            moves.add(intArrayOf(pos[0] + 1, pos[1] + 1))
        }

        if (adjacentPieceL is Pawn && adjacentPieceL.justMoved) {
            moves.add(intArrayOf(pos[0] - 1, pos[1] + 1))
        }
    }
}