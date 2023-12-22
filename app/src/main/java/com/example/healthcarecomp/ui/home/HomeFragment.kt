package com.example.healthcarecomp.ui.home

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseFragment
import com.example.healthcarecomp.common.Adapter.ItemActitivyHomeAdapter
import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.data.model.User
import com.example.healthcarecomp.databinding.FragmentHomeBinding
import com.example.healthcarecomp.ui.activity.main.MainActivity
import com.example.healthcarecomp.util.extension.isPatient
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private var parent: MainActivity? = null
    private lateinit var homeViewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        parent = requireActivity() as? MainActivity
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUser(parent?.currentUser)
        setUpAnimationMenu(binding)
        setUpRecycleView(binding)
        parent?.let {
            it.mainViewModel?.currentUser?.observe(viewLifecycleOwner, Observer { user ->
                setupUser(user)
            })
        }
        //setup search bar
        val menuList = Constant.getItemListForRecycleView_UserHome()
        val arr = menuList.map { it.nameIcon }.toTypedArray()
        binding.customSearchBar.addSuggestionArr(arr)
        binding.customSearchBar.binding.etSearch.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                menuList[position].actionId?.let {
                    moveToPage(it)
                }
            }
    }

    private fun setupUser(user: User?) {
        user?.let {
            binding.tvUsernameUserHome.text = user.firstName
            Glide.with(this).load(user.avatar).placeholder(R.drawable.default_user_avt)
                .into(binding.ivUseravtUserHome)
        }
    }

    private fun setUpAnimationMenu(binding: FragmentHomeBinding) {
        // Inflate animation
        val rotateAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate_button)
        // Gán animation cho button
        binding.btnMenuOptionUserHome.animation = rotateAnimation
        binding.btnMenuOptionUserHome.setOnClickListener {
            binding.btnMenuOptionUserHome.startAnimation(rotateAnimation)
        }
    }

    private fun setUpRecycleView(binding: FragmentHomeBinding) {
        val adapter = ItemActitivyHomeAdapter(Constant.getItemListForRecycleView_UserHome())
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)

        val gridSpacingItemDecoration = GridSpacingItemDecoration(20)
        binding.rvItemUserHome.addItemDecoration(gridSpacingItemDecoration)
        binding.rvItemUserHome.layoutManager = layoutManager
        binding.rvItemUserHome.adapter = adapter
        adapter.onItemClick = {
            it.actionId?.let { action ->
                moveToPage(action)
            }
        }
//        var progress = 0F
//
//        var tmp = 0F
//        binding.rvItemUserHome.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                tmp += dx
//                progress = (tmp / 900 * 100)
//                binding.pbItemUserhome.progress = progress.toInt()
//            }
//        })
    }

    private fun moveToPage(action: Int) {
        val currentUser = parent?.currentUser!!
        when(action) {
            R.id.action_navigation_home_to_medicalHistoryFragment -> {
                if (currentUser.isPatient()) {
                    homeViewModel.setMedicalHistoryPatientId(currentUser.id)
                } else {
                    homeViewModel.setMedicalHistoryPatientId("1a04ee07-5909-4471-b767-a62f8c1e99d1")
                }
                navigateToPage(action)
            }
        }
    }

    class GridSpacingItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.left = spacing * 2
            outRect.top = spacing
            outRect.right = spacing * 2
            outRect.bottom = spacing
        }
    }


}