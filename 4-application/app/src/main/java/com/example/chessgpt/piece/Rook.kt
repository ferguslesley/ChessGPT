package com.example.chessgpt.piece

import com.example.chessgpt.R

class Rook(pos : Array<Int>, color: PieceColor) : Piece(8, 0, pos, color) {
    init {
        image = if (color == PieceColor.WHITE) {
            R.drawable.rook_white
        } else {
            R.drawable.rook_black
        }

    }
}