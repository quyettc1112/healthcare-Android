package com.example.healthcarecomp.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseFragment
import com.example.healthcarecomp.databinding.FragmentHomeBinding


class HomeFragment : BaseFragment(R.layout.fragment_home) {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Inflate animation
        val rotateAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate_button)

        // Gán animation cho button
        binding.btnMenuOptionUserHome.animation = rotateAnimation

        binding.btnMenuOptionUserHome.setOnClickListener {
            binding.btnMenuOptionUserHome.startAnimation(rotateAnimation)
        }


        return binding.root
    }




}