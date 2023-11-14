package com.example.healthcarecomp.ui.chatmessage

import android.graphics.Path.Direction
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseFragment
import com.example.healthcarecomp.data.model.Message
import com.example.healthcarecomp.databinding.FragmentChatMessageBinding
import com.example.healthcarecomp.ui.activity.main.MainActivity
import com.example.healthcarecomp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatMessageFragment : BaseFragment(R.layout.fragment_chat_message) {
    private lateinit var _binding: FragmentChatMessageBinding
    private var _parent: MainActivity? = null
    private val args: ChatMessageFragmentArgs by navArgs()
    private lateinit var _recyclerViewAdapter: ChatMessageRecyclerViewAdapter
    private lateinit var _viewModel: ChatMessageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChatMessageBinding.inflate(layoutInflater, container, false)
        _parent = requireActivity() as? MainActivity

        //set up action click back
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToPage(R.id.action_chatMessageFragment_to_navigation_chat)
            }
        }
        _parent?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
        //setup view model
        _viewModel = ViewModelProvider(this)[ChatMessageViewModel::class.java]
        _viewModel.invoke(args.chatRoom)

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupObservers()
    }

    private fun setupUI() {

        _binding.customToolBar.onStartIconClick = {
            navigateToPage(R.id.action_chatMessageFragment_to_navigation_chat)
        }
        _binding.customToolBar.setTitle(args.user.firstName!!)
        //setting for recycler view
        _recyclerViewAdapter = ChatMessageRecyclerViewAdapter(_parent?.currentUser!!, args.user)
        _recyclerViewAdapter.setItemOrderBy {
            it.sortedBy { message ->
                message.timeStamp
            }
        }
        _recyclerViewAdapter.setOnItemDisplayListener { message ->
            if (message.receiverId == _parent?.currentUser?.id && !message.seen) {
                _viewModel.upsert(
                    message.copy(
                        seen = true
                    )
                )
            }
        }

        // auto scroll down after submit list
        var job: Job? = null
        _recyclerViewAdapter.setOnDataSubmitListener {
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                _binding.rvChatMessageContent.smoothScrollToPosition(_recyclerViewAdapter.itemCount - 1)
            }
        }

        _binding.rvChatMessageContent.apply {
            adapter = _recyclerViewAdapter
            layoutManager = LinearLayoutManager(context)
        }

        // click send btn
        _binding.tilChatMessage.setEndIconOnClickListener {
            val message = Message(
                timeStamp = System.currentTimeMillis(),
                content = _binding.etChatMessageText.text.toString(),
                chatRoomId = args.chatRoom.id,
                senderId = _parent?.currentUser?.id,
                receiverId = args.user.id
            )
            _binding.etChatMessageText.setText("")
            //send notification
            _viewModel.upsert(message,requireContext(), _parent?.currentUser?.firstName, args.user.id)
            Log.i("Maindd","${args.user.id}" )
        }
    }

    private fun setupObservers() {
        _viewModel.messageList.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource?.data?.let {
                        _recyclerViewAdapter.submitList(it)
                    }
                }

                is Resource.Loading -> {
                    Log.i("test", "loading")
                }

                else -> {
                    Log.i("test", "err: ${resource.message}")
                }
            }
        })

        _viewModel.messageSend.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Success -> {
                    Log.i("test", "send success")
                }

                is Resource.Loading -> {
                    Log.i("test", "send loading")
                }

                else -> {
                    Log.i("test", "err: ${resource.message}")
                }
            }
        })
    }


}