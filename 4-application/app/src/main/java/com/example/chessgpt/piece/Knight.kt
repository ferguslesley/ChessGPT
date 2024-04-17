package com.example.chessgpt.piece

class Knight(pos : Array<Int>, color: PieceColor) : Piece(0, 0, pos, color) {
    override fun calculateMoves() {
        if (pos[0] - 2 >= 0 && pos[1] + 1 <= boardSize) {
            moves.add(intArrayOf(pos[0] - 2, pos[1] + 1))
        }
        if (pos[0] - 1 >= 0 && pos[1] + 2 <= boardSize) {
            moves.add(intArrayOf(pos[0] - 1, pos[1] + 2))
        }
        if (pos[0] + 1 <= boardSize && pos[1] + 2 <= boardSize) {
            moves.add(intArrayOf(pos[0] + 1, pos[1] + 2))
        }
        if (pos[0] + 2 <= boardSize && pos[1] + 1 <= boardSize) {
            moves.add(intArrayOf(pos[0] + 2, pos[1] + 1))
        }
        if (pos[0] + 2 <= boardSize && pos[1] - 1 >= 0) {
            moves.add(intArrayOf(pos[0] + 2, pos[1] - 1))
        }
        if (pos[0] + 1 <= boardSize && pos[1] - 2 >= 0) {
            moves.add(intArrayOf(pos[0] + 1, pos[1] - 2))
        }
        if (pos[0] - 1 >= 0 && pos[1] - 2 >= 0) {
            moves.add(intArrayOf(pos[0] - 1, pos[1] - 2))
        }
        if (pos[0] - 2 >= 0 && pos[1] - 1 >= 0) {
            moves.add(intArrayOf(pos[0] - 2, pos[1] - 1))
        }
    }
}