package com.example.healthcarecomp.ui.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseFragment
import com.example.healthcarecomp.data.model.ChatRoom
import com.example.healthcarecomp.databinding.FragmentChatBinding
import com.example.healthcarecomp.ui.activity.main.MainActivity
import com.example.healthcarecomp.util.Resource
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatFragment : BaseFragment(R.layout.fragment_chat) {
    private lateinit var _binding: FragmentChatBinding
    private lateinit var _recyclerViewAdapter: ChatRecyclerViewAdapter
    private var _parent: MainActivity? = null
    private lateinit var _chatViewModel: ChatViewModel
    private lateinit var _suggestionAdapter: UserSuggestionRecyclerViewAdapter

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
        //set up search bar
        var searchJob: Job? = null
        requireActivity().findViewById<EditText>(com.google.android.material.R.id.open_search_view_edit_text)
            .doAfterTextChanged { text ->
                searchJob?.cancel()
                if (text.toString().length > 2) {
                    searchJob = MainScope().launch {
                        delay(500)
                        _chatViewModel.searchUsersByName(text.toString()) { result ->
                            result.observe(viewLifecycleOwner, Observer {
                                when (it) {
                                    is Resource.Success -> {
                                        _suggestionAdapter.typedText = text.toString()
                                        _suggestionAdapter.submitList(it.data!!)
                                    }

                                    else -> Log.i("test", "have error")
                                }
                            })
                        }
                    }
                } else {
                    _suggestionAdapter.submitList(mutableListOf())
                }
            }
        //set up search suggestion:
        _suggestionAdapter = UserSuggestionRecyclerViewAdapter()

        //set up onclick suggest find then move to chat message, if not found then create new chat room then move
        _suggestionAdapter.setItemOnclickListener { user ->
            _chatViewModel.findChatRoom(
                _parent?.currentUser!!.id,
                user.id,
                onChatRoomFound = { chatRoom ->
                    val direction =
                        ChatFragmentDirections.actionNavigationChatToChatMessageFragment(
                            user,
                            chatRoom
                        )
                    navigateToPage(direction)
                },
                onChatRoomNotFound = {
                    val newChatRoom = ChatRoom(
                        firstUserId = _parent?.currentUser?.id,
                        secondUserId = user.id,
                        lastActiveTime = System.currentTimeMillis()
                    )
                    val direction =
                        ChatFragmentDirections.actionNavigationChatToChatMessageFragment(
                            user,
                            newChatRoom
                        )
                    navigateToPage(direction)
                })
        }
        _binding.rvSearchSuggest.apply {
            adapter = _suggestionAdapter
            layoutManager = LinearLayoutManager(context)
        }

        _recyclerViewAdapter = ChatRecyclerViewAdapter()
        _parent?.let {
            _chatViewModel.invoke(_parent?.currentUser?.id!!) {
                _recyclerViewAdapter.submitData(it)
            }
        }
        _recyclerViewAdapter.currentUserId = _parent?.currentUser?.id!!
        _recyclerViewAdapter.setOnItemClickListener { user, chatRoom ->
            val direction =
                ChatFragmentDirections.actionNavigationChatToChatMessageFragment(user, chatRoom)
            navigateToPage(direction)
        }
        _recyclerViewAdapter.setOnAvatarClickListener { user ->
            val direction = ChatFragmentDirections.actionNavigationChatToViewProfileFragment(
                user,
                R.id.action_viewProfileFragment_to_navigation_chat
            )
            navigateToPage(direction)
        }
        _binding.rvChatUsers.apply {
            adapter = _recyclerViewAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        _chatViewModel.chatRooms.observe(viewLifecycleOwner, Observer { resources ->
            when (resources) {
                is Resource.Success -> {
                    hindLoading()
                    _recyclerViewAdapter.submitData(resources.data!!)
                }

                is Resource.Error -> {
                    hindLoading()
                }

                is Resource.Loading -> {
                    showLoading()
                }

                else -> {
                    hindLoading()
                }
            }
        })

    }

    fun showLoading() {
        _binding.pbChatLoading.visibility = View.VISIBLE
    }

    fun hindLoading() {
        _binding.pbChatLoading.visibility = View.INVISIBLE
    }

}