package com.example.chessgpt.piece

import com.example.chessgpt.R

class Queen(pos: Array<Int>, color: PieceColor) : Piece(8, 8, pos, color){
    init {
        image = if (color == PieceColor.WHITE) {
            R.drawable.queen_white
        } else {
            R.drawable.queen_black
        }

    }
}