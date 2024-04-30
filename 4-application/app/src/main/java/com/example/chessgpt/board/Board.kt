package com.example.chessgpt.board

import com.example.chessgpt.piece.Bishop
import com.example.chessgpt.piece.King
import com.example.chessgpt.piece.Knight
import com.example.chessgpt.piece.Pawn
import com.example.chessgpt.piece.Piece
import com.example.chessgpt.piece.PieceColor
import com.example.chessgpt.piece.Queen
import com.example.chessgpt.piece.Rook

var boardList:MutableList<MutableList<Piece?>> = MutableList(8) {
    MutableList(8) { null }
}
const val boardSize: Int = 8
var whiteKing =  King(arrayOf(4, 0), PieceColor.WHITE)

fun setupBoard() {
    whiteKing = King(arrayOf(4, 0), PieceColor.WHITE)
    boardList = MutableList(8) { MutableList(8) {null} }
    placePiece(Rook(arrayOf(0,0), PieceColor.WHITE), arrayOf(0,0))
    placePiece(Knight(arrayOf(1,0), PieceColor.WHITE), arrayOf(1,0))
    placePiece(Bishop(arrayOf(2, 0), PieceColor.WHITE), arrayOf(2,0))
    placePiece(Queen(arrayOf(3,0), PieceColor.WHITE), arrayOf(3,0))
    placePiece(whiteKing, arrayOf(4,0))
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
    val(col, row) = pos
    boardList[col][row] = piece
    piece.pos = pos
}

/**
 * Moves a piece on the board list data structure
 * @param piece The piece being moved
 * @param col Column/X coordinate of new position
 * @param row Row/Y coordinate of new position
 * @return Returns the move expressed in the format Piece OldPos -> NewPos
 */
fun movePiece(piece: Piece, col: Int, row: Int): String {
    val oldX = piece.pos[0]
    val oldY = piece.pos[1]

    // Move the piece, replace old position with null
    boardList[col][row] = piece
    if (oldX >= 0 && oldY >= 0) {
        boardList[oldX][oldY] = null
    }
    piece.pos = arrayOf(col, row)

    // Set pawn movement back to 1
    if (piece is Pawn && !piece.moved) {
        piece.orthogonalMovement = 1
        piece.moved = true
    }

    return reverseParseMove(piece, oldX, oldY, col, row)
}

fun reverseParseMove(piece: Piece, oldCol: Int, oldRow: Int, newCol: Int, newRow: Int): String {
    val pieceString: String = when (piece) {
        is Pawn -> "pawn"
        is Bishop -> "bishop"
        is King -> "king"
        is Knight -> "knight"
        is Queen -> "queen"
        is Rook -> "rook"
        else -> "unknown"
    }

    val oldPos = "${('a' + oldCol)}${8 - oldRow}" // eg 4, 3 becomes e4
    val newPos = "${('a' + newCol)}${8 - newRow}"
    return "$pieceString $oldPos -> $newPos"
}

fun getPiece(givenBoardState: MutableList<MutableList<Piece?>>,pos: Array<Int>) : Piece? {
    val (col, row) = pos
    return givenBoardState[col][row]
}

fun isValid(piece: Piece, newCol: Int, newRow: Int): Boolean {
    return newRow in 0 until boardSize &&
            newCol in 0 until boardSize &&
            (boardList[newCol][newRow] == null ||
                    boardList[newCol][newRow]?.color?.oppositeColor() == piece.color)
}

fun wouldBeDangerous(oldPos: Array<Int>, newCol: Int, newRow: Int): Boolean {
    // Create a temporary board to simulate a move
    val boardCopy = createBoardCopy()

    // Simulate the move
    val piece = boardCopy[oldPos[0]][oldPos[1]]
    boardCopy[newCol][newRow] = piece
    boardCopy[oldPos[0]][oldPos[1]] = null

    // Check if this board state means king is in check
    return isDangerous(boardCopy)
}

fun isDangerous(boardState: MutableList<MutableList<Piece?>>) : Boolean {
    var isDangerous = false
    var whiteKingPos: IntArray
    for (i in 0 until boardSize) {
        for (j in 0 until boardSize) {
            if (boardState[i][j] != null && boardState[i][j]?.color == PieceColor.BLACK) {
                whiteKingPos = findWhiteKing(boardState)
                for (pos in boardState[i][j]!!.getLineOfSight(boardState)) {
                    if (pos.contentEquals(whiteKingPos)) {
                        isDangerous = true
                    }
                }
            }
        }
    }
    return isDangerous
}

private fun findWhiteKing(boardState: MutableList<MutableList<Piece?>>) : IntArray {
    for (col in boardState.indices) {
        for (row in boardState[col].indices) {
            val piece: Piece? = boardState[col][row]
            if (piece is King && piece.color == PieceColor.WHITE) {
                return intArrayOf(col, row)
            }
        }
    }
    return intArrayOf(-1, -1)
}

private fun findBlackKing(boardState: MutableList<MutableList<Piece?>>): IntArray {
    for (col in boardState.indices) {
        for (row in boardState[col].indices) {
            val piece: Piece? = boardState[col][row]
            if (piece is King && piece.color == PieceColor.BLACK) {
                return intArrayOf(col, row)
            }
        }
    }
    return intArrayOf(-1, -1)
}

fun whiteKingDead(): Boolean {
    return findWhiteKing(boardList).contentEquals(intArrayOf(-1, -1))
}

fun blackKingDead(): Boolean {
    return findBlackKing(boardList).contentEquals(intArrayOf(-1, -1))
}

fun createBoardCopy(): MutableList<MutableList<Piece?>> {
    // Create a new mutable list instead of referring to the original
    val boardCopy = mutableListOf<MutableList<Piece?>>()

    // Fill new list with copies of data from original
    for (innerList in boardList) {
        val innerBoardCopy = mutableListOf<Piece?>()
        for (piece in innerList) {
            val pieceCopy = piece?.let { piece.deepCopyPiece() }
            innerBoardCopy.add(pieceCopy)
        }
        boardCopy.add(innerBoardCopy)
    }
    return boardCopy
}

fun convertToIndex(input: String): IntArray {
    var posX = -1
    var posY = -1
    for (char in input.lowercase()) {
        if (char in 'a'..'z') {
            posX = char - 'a'
        } else {
            posY = char - '1'
        }
    }
    return intArrayOf(posX, posY)
}

fun initPiece(pieceString: String): Piece {
    return when (pieceString.lowercase()) {
        "pawn" -> {
            val newPawn = Pawn(arrayOf(-1, -1), PieceColor.BLACK)
            newPawn
        }

        "bishop" -> {
            val newBishop = Bishop(arrayOf(-1, -1), PieceColor.BLACK)
            newBishop
        }

        "king" -> {
            val newKing = King(arrayOf(-1, -1), PieceColor.BLACK)
            newKing
        }

        "knight" -> {
            val newKnight = Knight(arrayOf(-1, -1), PieceColor.BLACK)
            newKnight
        }

        "queen" -> {
            val newQueen = Queen(arrayOf(-1, -1), PieceColor.BLACK)
            newQueen
        }

        "rook" -> {
            val newRook = Queen(arrayOf(-1, -1), PieceColor.BLACK)
            newRook
        }

        else -> {
            throw IllegalArgumentException("parsed input was not a valid piece: $pieceString")
        }
    }
}