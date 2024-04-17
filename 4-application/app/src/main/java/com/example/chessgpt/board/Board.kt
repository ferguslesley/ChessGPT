package com.example.chessgpt.board

import com.example.chessgpt.piece.Bishop
import com.example.chessgpt.piece.King
import com.example.chessgpt.piece.Knight
import com.example.chessgpt.piece.Pawn
import com.example.chessgpt.piece.Piece
import com.example.chessgpt.piece.PieceColor
import com.example.chessgpt.piece.Queen
import com.example.chessgpt.piece.Rook

var boardList:MutableList<MutableList<Piece?>> = MutableList(8) {MutableList(8) {null} }

fun setupBoard() {
    placePiece(Rook(arrayOf(0,0), PieceColor.WHITE), arrayOf(0,0))
    placePiece(Knight(arrayOf(1,0), PieceColor.WHITE), arrayOf(1,0))
    placePiece(Bishop(arrayOf(2, 0), PieceColor.WHITE), arrayOf(2,0))
    placePiece(Queen(arrayOf(3,0), PieceColor.WHITE), arrayOf(3,0))
    placePiece(King(arrayOf(4, 0), PieceColor.WHITE), arrayOf(4,0))
    placePiece(Bishop(arrayOf(5, 0), PieceColor.WHITE), arrayOf(5,0))
    placePiece(Knight(arrayOf(6, 0), PieceColor.WHITE), arrayOf(6,0))
    placePiece(Rook(arrayOf(7,0), PieceColor.WHITE), arrayOf(7,0))

    placePiece(Pawn(arrayOf(0, 1), PieceColor.WHITE), arrayOf(0,1))
    placePiece(Pawn(arrayOf(1, 1), PieceColor.WHITE), arrayOf(1,1))
    placePiece(Pawn(arrayOf(2, 1), PieceColor.WHITE), arrayOf(2,1))
    placePiece(Pawn(arrayOf(3, 1), PieceColor.WHITE), arrayOf(3,1))
    placePiece(Pawn(arrayOf(4, 1), PieceColor.WHITE), arrayOf(4,1))
    placePiece(Pawn(arrayOf(5, 1), PieceColor.WHITE), arrayOf(5,1))
    placePiece(Pawn(arrayOf(6, 1), PieceColor.WHITE), arrayOf(6,1))
    placePiece(Pawn(arrayOf(7, 1), PieceColor.WHITE), arrayOf(7,1))

    placePiece(Pawn(arrayOf(0, 6), PieceColor.BLACK), arrayOf(0, 6))
    placePiece(Pawn(arrayOf(1, 6), PieceColor.BLACK), arrayOf(1, 6))
    placePiece(Pawn(arrayOf(2, 6), PieceColor.BLACK), arrayOf(2, 6))
    placePiece(Pawn(arrayOf(3, 6), PieceColor.BLACK), arrayOf(3, 6))
    placePiece(Pawn(arrayOf(4, 6), PieceColor.BLACK), arrayOf(4, 6))
    placePiece(Pawn(arrayOf(5, 6), PieceColor.BLACK), arrayOf(5, 6))
    placePiece(Pawn(arrayOf(6, 6), PieceColor.BLACK), arrayOf(6, 6))
    placePiece(Pawn(arrayOf(7, 6), PieceColor.BLACK), arrayOf(7, 6))

    placePiece(Rook(arrayOf(0, 7), PieceColor.BLACK), arrayOf(0, 7))
    placePiece(Knight(arrayOf(1, 7), PieceColor.BLACK), arrayOf(1, 7))
    placePiece(Bishop(arrayOf(2, 7), PieceColor.BLACK), arrayOf(2, 7))
    placePiece(Queen(arrayOf(3, 7), PieceColor.BLACK), arrayOf(3, 7))
    placePiece(King(arrayOf(4, 7), PieceColor.BLACK), arrayOf(4, 7))
    placePiece(Bishop(arrayOf(5, 7), PieceColor.BLACK), arrayOf(5, 7))
    placePiece(Knight(arrayOf(6, 7), PieceColor.BLACK), arrayOf(6, 7))
    placePiece(Rook(arrayOf(7,7), PieceColor.BLACK), arrayOf(7, 7))

}

fun placePiece(piece: Piece, pos: Array<Int>) {
    val(row, col) = pos
    boardList[row][col] = piece
}

fun getPiece(pos: Array<Int>) : Piece? {
    val(row, col) = pos
    return boardList[row][col]
}