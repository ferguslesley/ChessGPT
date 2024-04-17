package com.example.chessgpt.piece

enum class PieceColor {
    WHITE,
    BLACK;

    fun oppositeColor(): PieceColor {
        return when (this) {
            WHITE -> BLACK
            BLACK -> WHITE
        }
    }
}