package com.example.chessgpt.piece;

import com.example.chessgpt.R

class King(pos: Array<Int>, color: PieceColor) : Piece(1, 1, pos, color){

    init {
        if (color == PieceColor.WHITE) {
            image = R.drawable.king_white
        } else {
            image = R.drawable.king_black
        }

    }
}
