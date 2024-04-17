package com.example.chessgpt.piece

import com.example.chessgpt.R

open class Piece (
    var orthogonalMovement: Int,
    val diagonalMovement: Int,
    var pos: Array<Int>,
    val color: PieceColor,

    var moves: MutableList<IntArray> = mutableListOf(),
    val boardSize: Int = 8,
    open var image: Int = R.drawable.king_white
){

    open fun calculateMoves() {
        moves = mutableListOf()
        getOrthogonals()
        getDiagonals()
        // TODO: Handle Discovered Checks
    }

    open fun getOrthogonals() {
        for (i in 1..orthogonalMovement) {
            // +y
            if(pos[1] + i < boardSize) {
                moves.add(intArrayOf(pos[0], pos[1] + i))
            }
            // +x
            if(pos[0] + i < boardSize) {
                moves.add(intArrayOf(pos[0] + i, pos[1]))
            }
            // -y
            if(pos[1] - i >= 0) {
                moves.add(intArrayOf(pos[0], pos[1] - i))
            }
            // -x
            if(pos[0] - i >= 0) {
                moves.add(intArrayOf(pos[0] - i, pos[1]))
            }
        }
    }

    open fun getDiagonals() {
        for (i in 1..diagonalMovement) {
            // -x +y
            if (pos[0] - i >= 0 && pos[1] + i < boardSize) {
                moves.add(intArrayOf(pos[0] - i, pos[1] + i))
            }
            // +x +y
            if (pos[0] + i < boardSize && pos[1] + i < boardSize) {
                moves.add(intArrayOf(pos[0] + i, pos[1] + i))
            }
            // -x -y
            if (pos[0] - i >= 0 && pos[1] - i >= 0) {
                moves.add(intArrayOf(pos[0] - i, pos[1] - i))
            }
            // +x -y
            if (pos[0] + i < boardSize && pos[1] - i >= 0) {
                moves.add(intArrayOf(pos[0] + i, pos[1] - i))
            }
        }
    }

    fun getMoves(): Array<IntArray> {
        calculateMoves()
        return moves.toTypedArray()
    }


}