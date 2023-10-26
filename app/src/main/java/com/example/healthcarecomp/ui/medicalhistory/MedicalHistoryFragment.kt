package com.example.healthcarecomp.ui.medicalhistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseFragment
import com.example.healthcarecomp.data.model.MedicalRecord
import com.example.healthcarecomp.databinding.FragmentMedicalHistoryBinding
import com.example.healthcarecomp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class MedicalHistoryFragment : BaseFragment(R.layout.fragment_medical_history) {
    private lateinit var _binding: FragmentMedicalHistoryBinding
    private lateinit var _recyclerViewAdapter: MedicalHistoryRecyclerViewAdapter
    private lateinit var medicalHistoryViewModel: MedicalHistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentMedicalHistoryBinding.inflate(inflater, container, false)
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToPage(R.id.action_medicalHistoryFragment_to_navigation_home)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
        medicalHistoryViewModel = ViewModelProvider(this)[MedicalHistoryViewModel::class.java]
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    private fun setupUI() {

        _recyclerViewAdapter = MedicalHistoryRecyclerViewAdapter()
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

        _binding.rvMedicalHistory.apply {
            adapter = _recyclerViewAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
        _binding.ibMedicalHistoryBack.setOnClickListener {
            navigateToPage(R.id.action_medicalHistoryFragment_to_navigation_home)
        }

        medicalHistoryViewModel.upsertMedialRecord(
            MedicalRecord(
                bodyTemperature = 0.1f,
                date = Date(2023,10,20)

            )
        )
        medicalHistoryViewModel.medicalHistoryList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> _recyclerViewAdapter.differ.submitList(it.data)
                else -> {}
            }
        })

    }


}