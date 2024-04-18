package com.example.chessgpt.piece

import com.example.chessgpt.R
import com.example.chessgpt.board.boardList
import com.example.chessgpt.board.boardSize
import com.example.chessgpt.board.getPiece
import com.example.chessgpt.board.isValid

open class Piece (
    var orthogonalMovement: Int,
    val diagonalMovement: Int,
    var pos: Array<Int>,
    val color: PieceColor,

    var moves: MutableList<IntArray> = mutableListOf(),
    open var image: Int = R.drawable.king_white
){

    open fun calculateMoves() {
        moves = mutableListOf()
        getOrthogonals()
        getDiagonals()
        // TODO: Handle Discovered Checks
    }

    open fun getOrthogonals() {
        getOrthPositY()
        getOrthNegatY()
        getOrthPositX()
        getOrthNegatX()
    }

    private fun getOrthPositY() {
        var i = 1
        var foundBlackPiece = false
        while (i <= orthogonalMovement && isValid(pos[0], pos[1] + i) && !foundBlackPiece) {
            moves.add(intArrayOf(pos[0], pos[1] + i))

            if (boardList[pos[0]][pos[1] + i]?.color == PieceColor.BLACK) {
                // Should be valid when it hits a black piece, but go no further
                foundBlackPiece = true
            }
            i++
        }
    }

    private fun getOrthNegatY() {
        var i = 1
        var foundBlackPiece = false
        while (i <= orthogonalMovement && isValid(pos[0], pos[1] - i) && !foundBlackPiece) {
            moves.add(intArrayOf(pos[0], pos[1] - i))

            if (boardList[pos[0]][pos[1] - i]?.color == PieceColor.BLACK) {
                // Should be valid when it hits a black piece, but go no further
                foundBlackPiece = true
            }
            i++
        }
    }

    private fun getOrthPositX() {
        var i = 1
        var foundBlackPiece = false
        while (i <= orthogonalMovement && isValid(pos[0] + i, pos[1]) && !foundBlackPiece) {
            moves.add(intArrayOf(pos[0] + i, pos[1]))

            if (boardList[pos[0] + i][pos[1]]?.color == PieceColor.BLACK) {
                // Should be valid when it hits a black piece, but go no further
                foundBlackPiece = true
            }
            i++
        }
    }

    private fun getOrthNegatX() {
        var i = 1
        var foundBlackPiece = false
        while (i <= orthogonalMovement && isValid(pos[0] - i, pos[1]) && !foundBlackPiece) {
            moves.add(intArrayOf(pos[0] - i, pos[1]))

            if (boardList[pos[0] - i][pos[1]]?.color == PieceColor.BLACK) {
                // Should be valid when it hits a black piece, but go no further
                foundBlackPiece = true
            }
            i++
        }
    }

    open fun getDiagonals() {
        getDiagUpLeft()
        getDiagUpRight()
        getDiagDownRight()
        getDiagDownLeft()
    }

    private fun getDiagUpLeft() {
        var i = 1
        var foundBlackPiece = false
        while (i <= diagonalMovement && isValid(pos[0] - i, pos[1] + i) && !foundBlackPiece) {
            moves.add(intArrayOf(pos[0] - i, pos[1] + i))

            if (boardList[pos[0] - i][pos[1] + i]?.color == PieceColor.BLACK) {
                // Should be valid when it hits a black piece, but go no further
                foundBlackPiece = true
            }
            i++
        }
    }

    private fun getDiagUpRight() {
        var i = 1
        var foundBlackPiece = false
        while (i <= diagonalMovement && isValid(pos[0] + i, pos[1] + i) && !foundBlackPiece) {
            moves.add(intArrayOf(pos[0] + i, pos[1] + i))

            if (boardList[pos[0] + i][pos[1] + i]?.color == PieceColor.BLACK) {
                // Should be valid when it hits a black piece, but go no further
                foundBlackPiece = true
            }
            i++
        }
    }

    private fun getDiagDownRight() {
        var i = 1
        var foundBlackPiece = false
        while (i <= diagonalMovement && isValid(pos[0] + i, pos[1] - i) && !foundBlackPiece) {
            moves.add(intArrayOf(pos[0] + i, pos[1] - i))

            if (boardList[pos[0] + i][pos[1] - i]?.color == PieceColor.BLACK) {
                // Should be valid when it hits a black piece, but go no further
                foundBlackPiece = true
            }
            i++
        }
    }

    private fun getDiagDownLeft() {
        var i = 1
        var foundBlackPiece = false
        while (i <= diagonalMovement && isValid(pos[0] - i, pos[1] - i) && !foundBlackPiece) {
            moves.add(intArrayOf(pos[0] - i, pos[1] - i))

            if (boardList[pos[0] - i][pos[1] - i]?.color == PieceColor.BLACK) {
                // Should be valid when it hits a black piece, but go no further
                foundBlackPiece = true
            }
            i++
        }
    }

    fun getMoves(): Array<IntArray> {
        calculateMoves()
        return moves.toTypedArray()
    }


}