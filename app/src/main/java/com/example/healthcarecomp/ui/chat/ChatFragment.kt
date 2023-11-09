package com.example.healthcarecomp.ui.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseFragment
import com.example.healthcarecomp.data.model.ChatRoom
import com.example.healthcarecomp.data.model.User
import com.example.healthcarecomp.databinding.FragmentChatBinding
import com.example.healthcarecomp.ui.activity.main.MainActivity
import com.example.healthcarecomp.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : BaseFragment(R.layout.fragment_chat) {
    private lateinit var _binding: FragmentChatBinding
    private lateinit var _recyclerViewAdapter: ChatRecyclerViewAdapter
    private var _parent: MainActivity? = null
    private lateinit var _chatViewModel: ChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(layoutInflater, container, false)
        _parent = requireActivity() as? MainActivity
        _chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()

    }

    private fun setupUI() {
        _recyclerViewAdapter = ChatRecyclerViewAdapter()
        _parent?.let {
            _chatViewModel.invoke(_parent?.currentUser?.id!!){
                _recyclerViewAdapter.submitData(it)
            }
        }
        _recyclerViewAdapter.currentUserId = _parent?.currentUser?.id!!
        _recyclerViewAdapter.setOnItemClickListener { user,id ->
            val direction = ChatFragmentDirections.actionNavigationChatToChatMessageFragment(user, id)
            navigateToPage(direction)
        }
        _binding.rvChatUsers.apply {
            adapter = _recyclerViewAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        _chatViewModel.chatRooms.observe(viewLifecycleOwner, Observer { resources ->
            when(resources) {
                is Resource.Success -> {
                    hindLoading()
                    _recyclerViewAdapter.submitData(resources.data!!)
                }
                is Resource.Error -> {
                }

                is Resource.Loading -> {
                    showLoading()
                }

                else -> {
                }
            }
        })
        _binding.fabChatAdd.setOnClickListener {
            val chatRoom = ChatRoom(
                firstUserId = "1a04ee07-5909-4471-b767-a62f8c1e99d1",
                secondUserId = "cbf1d2ea-0249-452e-bd4e-7db757ad6f4c",
                lastActiveTime = System.currentTimeMillis(),
                chatSeen = false
            )
            _chatViewModel.upsert(chatRoom)
        }

        _chatViewModel.chatRoomUpsert.observe(viewLifecycleOwner, Observer{
            when(it) {
                is Resource.Success -> Log.i("okok", "upsert success")
                is Resource.Error -> Log.i("okok", "upsert error: ${it.message}")
                is Resource.Unknown -> Log.i("okok", "unknown err")
                else -> Log.i("okok", "humm err")
            }
        })
    }

    fun showLoading(){
        _binding.pbChatLoading.visibility = View.VISIBLE
    }

    fun hindLoading() {
        _binding.pbChatLoading.visibility = View.INVISIBLE
    }

}