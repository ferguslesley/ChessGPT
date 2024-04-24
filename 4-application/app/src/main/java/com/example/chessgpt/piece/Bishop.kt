package com.example.chessgpt.piece

import com.example.chessgpt.R

class Bishop(pos: Array<Int>, color: PieceColor) : Piece(0, 8, pos, color) {
    init {
        image = if (color == PieceColor.WHITE) {
            R.drawable.bishop_white
        } else {
            R.drawable.bishop_black
        }

    }
}