package com.example.chessgpt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment

class BoardFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_board, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val startButton: Button = view.findViewById(R.id.startGame)
        val endButton: Button = view.findViewById(R.id.endGame)

        startButton.setOnClickListener {
            onStartButtonClick()
        }

        endButton.setOnClickListener {
            onEndButtonClick()
        }

    }

    private fun onStartButtonClick() {
        Toast.makeText(requireContext(), "Starting Game!", Toast.LENGTH_SHORT).show()
    }

    private fun onEndButtonClick() {
        Toast.makeText(requireContext(), "Ending Game!", Toast.LENGTH_SHORT).show()
    }

}