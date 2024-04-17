package com.example.chessgpt.piece

import com.example.chessgpt.R

class Bishop(pos: Array<Int>, color: PieceColor) : Piece(0, 8, pos, color) {
    init {
        if (color == PieceColor.WHITE) {
            image = R.drawable.bishop_white
        } else {
            image = R.drawable.bishop_black
        }

    }
}