package com.example.chessgpt.piece

import com.example.chessgpt.R

class Rook(pos : Array<Int>, color: PieceColor) : Piece(8, 0, pos, color) {
    init {
        if (color == PieceColor.WHITE) {
            image = R.drawable.rook_white
        } else {
            image = R.drawable.rook_black
        }

    }
}