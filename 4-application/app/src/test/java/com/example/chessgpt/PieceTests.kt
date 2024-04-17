package com.example.chessgpt

import com.example.chessgpt.piece.King
import com.example.chessgpt.piece.PieceColor
import com.example.chessgpt.piece.Queen
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class PieceTests {

    companion object {
        @JvmStatic
        fun kingMovementTestSource(): Stream<Arguments> {
            return Stream.of(
                // Middle should return 8
                Arguments.of(arrayOf(5, 5), 8),
                // Corners should return 3
                Arguments.of(arrayOf(0, 0), 3),
                Arguments.of(arrayOf(7,7), 3),
                Arguments.of(arrayOf(0,7), 3),
                Arguments.of(arrayOf(7,0), 3),
                // Edges should return 5
                Arguments.of(arrayOf(7, 5), 5),
                Arguments.of(arrayOf(5, 0), 5),
                Arguments.of(arrayOf(0, 5), 5),
                Arguments.of(arrayOf(5, 7), 5)
            )
        }

        @JvmStatic
        fun queenMovementTestSource(): Stream<Arguments> {
            return Stream.of(
                // Corners should return 21
                Arguments.of(arrayOf(0, 0), 21)
            )
        }
    }

    @ParameterizedTest
    @MethodSource("kingMovementTestSource")
    fun kingMovementTests(
        position: Array<Int>,
        expectedMoves: Int
    ) {
        val piece = King(position, PieceColor.WHITE)
        val moves = piece.getMoves()
        assertEquals(expectedMoves, moves.size)
    }

    @ParameterizedTest
    @MethodSource("queenMovementTestSource")
    fun queenMovementTests(
        position: Array<Int>,
        expectedMoves: Int
    ) {
        val piece = Queen(position, PieceColor.WHITE)
        val moves = piece.getMoves()
        assertEquals(expectedMoves, moves.size)
    }
}
