package com.example.chessgpt.piece;

import com.example.chessgpt.R

class King(pos: Array<Int>, color: PieceColor) : Piece(1, 1, pos, color){

    init {
        image = if (color == PieceColor.WHITE) {
            R.drawable.king_white
        } else {
            R.drawable.king_black
        }

    }
}
