package com.example.chessgpt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.chessgpt.data.db
import com.example.chessgpt.user.User
import com.example.chessgpt.user.UserDao

class StatsFragment : Fragment() {
    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val winsText: TextView = view.findViewById(R.id.wins_text)
        val lossesText: TextView = view.findViewById(R.id.losses_text)
        viewModel.currentUser.observe(viewLifecycleOwner, Observer { currentUser ->
            winsText.text = requireContext().resources.getString(R.string.wins_placeholder, currentUser.wins)
            lossesText.text = requireContext().resources.getString(R.string.losses_placeholder, currentUser.losses)
        })
    }
}