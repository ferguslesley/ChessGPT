package com.example.chessgpt.piece

import com.example.chessgpt.R

class Queen(pos: Array<Int>, color: PieceColor) : Piece(8, 8, pos, color){
    init {
        if (color == PieceColor.WHITE) {
            image = R.drawable.queen_white
        } else {
            image = R.drawable.queen_black
        }

    }
}