package com.example.chessgpt.piece

import com.example.chessgpt.R
import com.example.chessgpt.board.boardList
import com.example.chessgpt.board.isValid
import com.example.chessgpt.board.wouldBeDangerous

open class Piece (
    var orthogonalMovement: Int,
    private val diagonalMovement: Int,
    var pos: Array<Int>,
    val color: PieceColor,

    var moves: MutableList<IntArray> = mutableListOf(),
    open var image: Int = R.drawable.king_white
){

    open fun calculateMoves() {
        moves = mutableListOf()
        getOrthogonals(boardList)
        getDiagonals(boardList)
    }

    open fun calculateMoves(givenBoardState: MutableList<MutableList<Piece?>>) {
        moves = mutableListOf()
        getOrthogonals(givenBoardState)
        getDiagonals(givenBoardState)
    }

    open fun getOrthogonals(givenBoardState: MutableList<MutableList<Piece?>>) {
        getOrthPositY(givenBoardState)
        getOrthNegatY(givenBoardState)
        getOrthPositX(givenBoardState)
        getOrthNegatX(givenBoardState)
    }

    private fun getOrthPositY(givenBoardState: MutableList<MutableList<Piece?>>) {
        var i = 1
        var foundOppositePiece = false
        while (i <= orthogonalMovement && isValid(
                this,
                pos[0],
                pos[1] + i
            ) && !foundOppositePiece) {
            moves.add(intArrayOf(pos[0], pos[1] + i))

            if (givenBoardState[pos[0]][pos[1] + i] != null) {
                if (givenBoardState[pos[0]][pos[1] + i]!!.color.oppositeColor() == this.color) {
                    // Should be valid when it hits an opposite color piece, but go no further
                    foundOppositePiece = true
                }
            }
            i++
        }
    }

    private fun getOrthNegatY(givenBoardState: MutableList<MutableList<Piece?>>) {
        var i = 1
        var foundOppositePiece = false
        while (i <= orthogonalMovement && isValid(
                this,
                pos[0],
                pos[1] - i
            ) && !foundOppositePiece) {
            moves.add(intArrayOf(pos[0], pos[1] - i))

            if (givenBoardState[pos[0]][pos[1] - i] != null) {
                if (givenBoardState[pos[0]][pos[1] - i]!!.color.oppositeColor() == this.color) {
                    // Should be valid when it hits an opposite color piece, but go no further
                    foundOppositePiece = true
                }
            }
            i++
        }
    }

    private fun getOrthPositX(givenBoardState: MutableList<MutableList<Piece?>>) {
        var i = 1
        var foundOppositePiece = false
        while (i <= orthogonalMovement && isValid(
                this,
                pos[0] + i,
                pos[1]
            ) && !foundOppositePiece) {
            moves.add(intArrayOf(pos[0] + i, pos[1]))

            if (givenBoardState[pos[0] + i][pos[1]] != null) {
                if (givenBoardState[pos[0] + i][pos[1]]!!.color.oppositeColor() == this.color) {
                    // Should be valid when it hits an opposite color piece, but go no further
                    foundOppositePiece = true
                }
            }
            i++
        }
    }

    private fun getOrthNegatX(givenBoardState: MutableList<MutableList<Piece?>>) {
        var i = 1
        var foundOppositePiece = false
        while (i <= orthogonalMovement && isValid(
                this,
                pos[0] - i,
                pos[1]
            ) && !foundOppositePiece) {
            moves.add(intArrayOf(pos[0] - i, pos[1]))

            if (givenBoardState[pos[0] - i][pos[1]] != null) {
                if (givenBoardState[pos[0] - i][pos[1]]!!.color.oppositeColor() == this.color) {
                    // Should be valid when it hits an opposite color piece, but go no further
                    foundOppositePiece = true
                }
            }
            i++
        }
    }

    open fun getDiagonals(givenBoardState: MutableList<MutableList<Piece?>>) {
        getDiagUpLeft(givenBoardState)
        getDiagUpRight(givenBoardState)
        getDiagDownRight(givenBoardState)
        getDiagDownLeft(givenBoardState)
    }

    private fun getDiagUpLeft(givenBoardState: MutableList<MutableList<Piece?>>) {
        var i = 1
        var foundOppositePiece = false
        while (i <= diagonalMovement && isValid(
                this,
                pos[0] - i,
                pos[1] + i
            ) && !foundOppositePiece) {
            moves.add(intArrayOf(pos[0] - i, pos[1] + i))

            if (givenBoardState[pos[0] - i][pos[1] + i] != null) {
                if (givenBoardState[pos[0] - i][pos[1] + i]!!.color.oppositeColor() == this.color) {
                    // Should be valid when it hits an opposite color piece, but go no further
                    foundOppositePiece = true
                }
            }
            i++
        }
    }

    private fun getDiagUpRight(givenBoardState: MutableList<MutableList<Piece?>>) {
        var i = 1
        var foundOppositePiece = false
        while (i <= diagonalMovement && isValid(
                this,
                pos[0] + i,
                pos[1] + i
            ) && !foundOppositePiece) {
            moves.add(intArrayOf(pos[0] + i, pos[1] + i))

            if (givenBoardState[pos[0] + i][pos[1] + i] != null) {
                if (givenBoardState[pos[0] + i][pos[1] + i]!!.color.oppositeColor() == this.color) {
                    // Should be valid when it hits an opposite color piece, but go no further
                    foundOppositePiece = true
                }
            }
            i++
        }
    }

    private fun getDiagDownRight(givenBoardState: MutableList<MutableList<Piece?>>) {
        var i = 1
        var foundOppositePiece = false
        while (i <= diagonalMovement && isValid(
                this,
                pos[0] + i,
                pos[1] - i
            ) && !foundOppositePiece) {
            moves.add(intArrayOf(pos[0] + i, pos[1] - i))

            if (givenBoardState[pos[0] + i][pos[1] - i] != null) {
                if (givenBoardState[pos[0] + i][pos[1] - i]!!.color.oppositeColor() == this.color) {
                    // Should be valid when it hits an opposite color piece, but go no further
                    foundOppositePiece = true
                }
            }
            i++
        }
    }

    private fun getDiagDownLeft(givenBoardState: MutableList<MutableList<Piece?>>) {
        var i = 1
        var foundOppositePiece = false
        while (i <= diagonalMovement && isValid(
                this,
                pos[0] - i,
                pos[1] - i
            ) && !foundOppositePiece) {
            moves.add(intArrayOf(pos[0] - i, pos[1] - i))

            if (givenBoardState[pos[0] - i][pos[1] - i] != null) {
                if (givenBoardState[pos[0] - i][pos[1] - i]!!.color.oppositeColor() == this.color) {
                    // Should be valid when it hits an opposite color piece, but go no further
                    foundOppositePiece = true
                }
            }
            i++
        }
    }

    fun getMoves(): Array<IntArray> {
        calculateMoves()
        removeDangerousMoves()
        return moves.toTypedArray()
    }

    fun deepCopyPiece(): Piece {
        // Make a new piece, depending on what type it is
        // Copy all aspects of the piece
        return when (val original = this) {
            is Pawn -> {
                val pawnCopy = Pawn(
                    original.pos.copyOf(),
                    original.color
                )
                pawnCopy.moves.addAll(original.moves.map { it.copyOf() })
                pawnCopy
            }
            is Bishop -> {
                val bishopCopy = Bishop(
                    original.pos.copyOf(),
                    original.color
                )
                bishopCopy.moves.addAll(original.moves.map { it.copyOf() })
                bishopCopy
            }
            is King -> {
                val kingCopy = King(
                    original.pos.copyOf(),
                    original.color
                )
                kingCopy.moves.addAll(original.moves.map { it.copyOf() })
                kingCopy
            }
            is Knight -> {
                val knightCopy = Knight(
                    original.pos.copyOf(),
                    original.color
                )
                knightCopy.moves.addAll(original.moves.map { it.copyOf() })
                knightCopy
            }
            is Queen -> {
                val queenCopy = Queen(
                    original.pos.copyOf(),
                    original.color
                )
                queenCopy.moves.addAll(original.moves.map { it.copyOf() })
                queenCopy
            }
            is Rook -> {
                val rookCopy = Rook(
                    original.pos.copyOf(),
                    original.color
                )
                rookCopy.moves.addAll(original.moves.map { it.copyOf() })
                rookCopy
            }
            else -> {
                val pieceCopy = Piece(
                    original.orthogonalMovement,
                    original.diagonalMovement,
                    original.pos.copyOf(),
                    original.color
                )
                pieceCopy.moves.addAll(original.moves.map { it.copyOf() })
                pieceCopy
            }
        }
    }

    private fun removeDangerousMoves() {
        // Iterator so that move can be removed while iterating
        val iterator = moves.iterator()
        while (iterator.hasNext()) {
            val newPos = iterator.next()
            if (wouldBeDangerous(pos, newPos[0], newPos[1])) {
                iterator.remove()
            }
        }
    }

    fun getLineOfSight(givenBoardState: MutableList<MutableList<Piece?>>): Array<IntArray> {
        calculateMoves(givenBoardState)
        return moves.toTypedArray()
    }

}