package com.example.healthcarecomp.ui.viewProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseFragment
import com.example.healthcarecomp.data.model.ChatRoom
import com.example.healthcarecomp.databinding.FragmentInfoBinding
import com.example.healthcarecomp.databinding.FragmentViewProfileBinding
import com.example.healthcarecomp.ui.activity.main.MainActivity
import com.example.healthcarecomp.ui.activity.main.MainViewModel
import com.example.healthcarecomp.ui.info.InfoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewProfileFragment : BaseFragment(R.layout.fragment_view_profile) {

    private lateinit var _binding: FragmentViewProfileBinding
    private val args: ViewProfileFragmentArgs by navArgs()
    private lateinit var _viewModel: ViewProfileViewModel
    private var _parent: MainActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewProfileBinding.inflate(inflater, container, false)
        _viewModel = ViewModelProvider(this)[ViewProfileViewModel::class.java]
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToPage(args.prePage)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
        _parent = requireActivity() as? MainActivity
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        _binding.customToolBar.onStartIconClick = {
            navigateToPage(args.prePage)
        }
        Glide.with(this).load(args.user.avatar).into(_binding.ivUserAvatar)
        _binding.llBtnChat.setOnClickListener {
            _viewModel.findChatRoom(_parent?.currentUser?.id!!,args.user.id, onChatRoomFound =  {chatRoom ->
                val direction = ViewProfileFragmentDirections.actionViewProfileFragmentToChatMessageFragment(args.user, chatRoom)
                navigateToPage(direction)
            }, onChatRoomNotFound = {
                val newChatRoom = ChatRoom(
                    firstUserId = _parent?.currentUser?.id,
                    secondUserId = args.user.id,
                    lastActiveTime = System.currentTimeMillis()
                )
                _viewModel.upset(newChatRoom, onSuccess = {
                    val direction = ViewProfileFragmentDirections.actionViewProfileFragmentToChatMessageFragment(args.user, newChatRoom)
                    navigateToPage(direction)
                })
            })
        }
    }
}