package com.example.chessgpt.piece

import com.example.chessgpt.R
import com.example.chessgpt.board.isValid

class Knight(pos : Array<Int>, color: PieceColor) : Piece(0, 0, pos, color) {
    init {
        image = if (color == PieceColor.WHITE) {
            R.drawable.knight_white
        } else {
            R.drawable.knight_black
        }

    }
    override fun calculateMoves() {
        moves = mutableListOf()
        if (isValid(this, pos[0] - 2, pos[1] + 1)) {
            moves.add(intArrayOf(pos[0] - 2, pos[1] + 1))
        }
        if (isValid(this, pos[0] - 1, pos[1] + 2)) {
            moves.add(intArrayOf(pos[0] - 1, pos[1] + 2))
        }
        if (isValid(this, pos[0] + 1, pos[1] + 2)) {
            moves.add(intArrayOf(pos[0] + 1, pos[1] + 2))
        }
        if (isValid(this, pos[0] + 2, pos[1] + 1)) {
            moves.add(intArrayOf(pos[0] + 2, pos[1] + 1))
        }
        if (isValid(this, pos[0] + 2, pos[1] - 1)) {
            moves.add(intArrayOf(pos[0] + 2, pos[1] - 1))
        }
        if (isValid(this, pos[0] + 1, pos[1] - 2)) {
            moves.add(intArrayOf(pos[0] + 1, pos[1] - 2))
        }
        if (isValid(this, pos[0] - 1, pos[1] - 2)) {
            moves.add(intArrayOf(pos[0] - 1, pos[1] - 2))
        }
        if (isValid(this, pos[0] - 2, pos[1] - 1)) {
            moves.add(intArrayOf(pos[0] - 2, pos[1] - 1))
        }
    }
}