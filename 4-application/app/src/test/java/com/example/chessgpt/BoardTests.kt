package com.example.chessgpt

import com.example.chessgpt.board.boardList
import com.example.chessgpt.board.setupBoard
import com.example.chessgpt.piece.Bishop
import com.example.chessgpt.piece.King
import com.example.chessgpt.piece.Knight
import com.example.chessgpt.piece.Pawn
import com.example.chessgpt.piece.Piece
import com.example.chessgpt.piece.PieceColor
import com.example.chessgpt.piece.Queen
import com.example.chessgpt.piece.Rook
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

class BoardTests {

    @Test
    fun shouldSetupBoard() {
        // given
        val expectedBoard: MutableList<MutableList<Piece?>> = mutableListOf(
            mutableListOf(
                Rook(arrayOf(0,0), PieceColor.WHITE),
                Pawn(arrayOf(0,1), PieceColor.WHITE),
                null,
                null,
                null,
                null,
                Pawn(arrayOf(0,6), PieceColor.BLACK),
                Rook(arrayOf(0,7), PieceColor.BLACK)
            ),
            mutableListOf(
                Knight(arrayOf(1,0), PieceColor.WHITE),
                Pawn(arrayOf(1,1), PieceColor.WHITE),
                null,
                null,
                null,
                null,
                Pawn(arrayOf(1,6), PieceColor.BLACK),
                Knight(arrayOf(1,7), PieceColor.BLACK)
            ),
            mutableListOf(
                Bishop(arrayOf(2, 0), PieceColor.WHITE),
                Pawn(arrayOf(2, 1), PieceColor.WHITE),
                null,
                null,
                null,
                null,
                Pawn(arrayOf(2, 6), PieceColor.BLACK),
                Bishop(arrayOf(2, 7), PieceColor.BLACK)
            ),
            mutableListOf(
                Queen(arrayOf(3, 0), PieceColor.WHITE),
                Pawn(arrayOf(3, 1), PieceColor.WHITE),
                null,
                null,
                null,
                null,
                Pawn(arrayOf(3, 6), PieceColor.BLACK),
                Queen(arrayOf(3, 7), PieceColor.BLACK)
            ),
            mutableListOf(
                King(arrayOf(4, 0), PieceColor.WHITE),
                Pawn(arrayOf(4, 1), PieceColor.WHITE),
                null,
                null,
                null,
                null,
                Pawn(arrayOf(4, 6), PieceColor.BLACK),
                King(arrayOf(4, 7), PieceColor.BLACK)
            ),
            mutableListOf(
                Bishop(arrayOf(5, 0), PieceColor.WHITE),
                Pawn(arrayOf(5, 1), PieceColor.WHITE),
                null,
                null,
                null,
                null,
                Pawn(arrayOf(5, 6), PieceColor.BLACK),
                Bishop(arrayOf(5, 7), PieceColor.BLACK)
            ),
            mutableListOf(
                Knight(arrayOf(6, 0), PieceColor.WHITE),
                Pawn(arrayOf(6, 1), PieceColor.WHITE),
                null,
                null,
                null,
                null,
                Pawn(arrayOf(6, 6), PieceColor.BLACK),
                Knight(arrayOf(6, 7), PieceColor.BLACK)
            ),
            mutableListOf(
                Rook(arrayOf(7, 0), PieceColor.WHITE),
                Pawn(arrayOf(7, 1), PieceColor.WHITE),
                null,
                null,
                null,
                null,
                Pawn(arrayOf(7, 6), PieceColor.BLACK),
                Rook(arrayOf(7, 7), PieceColor.BLACK)
            )
        )
        // when
        setupBoard()

        // then
        assertTrue(compareBoards(expectedBoard, boardList))
    }

    private fun compareBoards(firstBoard: MutableList<MutableList<Piece?>>, secondBoard: MutableList<MutableList<Piece?>>): Boolean {
        if (firstBoard.size != secondBoard.size) {
            return false
        }

        for (i in firstBoard.indices) {
            if (firstBoard[i].size != secondBoard[i].size) {
                return false
            }

            for (j in firstBoard[i].indices) {
                val firstPiece = firstBoard[i][j]
                val secondPiece = secondBoard[i][j]

                if (firstPiece == null) {
                    if (secondPiece != null) {
                        return false
                    }
                } else {
                    if (secondPiece == null) {
                        return false
                    }

                    if (!firstPiece.pos.contentEquals(secondPiece.pos)) {
                        return false
                    }

                    if (firstPiece.color != secondPiece.color) {
                        return false
                    }
                }
            }
        }
        return true
    }
}