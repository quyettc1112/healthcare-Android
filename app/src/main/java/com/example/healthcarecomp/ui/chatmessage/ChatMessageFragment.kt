package com.example.healthcarecomp.ui.chatmessage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseFragment
import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.databinding.FragmentChatMessageBinding
import com.example.healthcarecomp.ui.activity.MainActivity


class ChatMessageFragment : BaseFragment(R.layout.fragment_chat_message) {
    private lateinit var _binding: FragmentChatMessageBinding
    private var _parent: MainActivity? = null
    private val args: ChatMessageFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChatMessageBinding.inflate(layoutInflater, container, false)
        _parent = requireActivity() as? MainActivity
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToPage(R.id.action_chatMessageFragment_to_navigation_chat)
            }
        }
        _parent?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    private fun setupUI() {
        _binding.customToolBar.onStartIconClick = {
            navigateToPage(R.id.action_chatMessageFragment_to_navigation_chat)
        }
        _binding.customToolBar.setTitle(args.user.firstName!!)
    }




}