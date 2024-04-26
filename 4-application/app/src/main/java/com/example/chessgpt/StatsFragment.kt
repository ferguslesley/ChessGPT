package com.example.chessgpt

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.chessgpt.data.db
import com.example.chessgpt.openai.TokenManager
import com.example.chessgpt.user.User
import com.example.chessgpt.user.UserDao

class StatsFragment : Fragment() {
    private lateinit var viewModel: UserViewModel
    private lateinit var tokenManager: TokenManager

    private lateinit var winsText: TextView
    private lateinit var lossesText: TextView
    private lateinit var tokensText: TextView
    private lateinit var refreshButton: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        tokenManager = TokenManager(this.requireContext().getSharedPreferences("TokenPrefs", Context.MODE_PRIVATE))
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        winsText = view.findViewById(R.id.wins_text)
        lossesText = view.findViewById(R.id.losses_text)
        tokensText = view.findViewById(R.id.tokens_left)
        refreshButton = view.findViewById(R.id.refresh_button)
        refreshStats()

        refreshButton.setOnClickListener {
            refreshStats()
        }
    }

    private fun refreshStats() {
        viewModel.currentUser.observe(viewLifecycleOwner, Observer { currentUser ->
            winsText.text = requireContext().resources.getString(R.string.wins_placeholder, currentUser.wins)
            lossesText.text = requireContext().resources.getString(R.string.losses_placeholder, currentUser.losses)
        })
        tokensText.text = requireContext().resources.getString(
            R.string.tokens_left, tokenManager.getTokensLeft()
        )
    }
}