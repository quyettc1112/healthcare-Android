package com.example.healthcarecomp.ui.medicalhistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseFragment
import com.example.healthcarecomp.data.model.MedicalRecord
import com.example.healthcarecomp.databinding.FragmentMedicalHistoryBinding
import java.text.SimpleDateFormat
import java.util.Date

class MedicalHistoryFragment : BaseFragment(R.layout.fragment_medical_history) {
    private lateinit var _binding: FragmentMedicalHistoryBinding
    private lateinit var _recyclerViewAdapter: MedicalHistoryRecyclerViewAdapter

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
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,onBackPressedCallback)

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    private fun setupUI() {

        _recyclerViewAdapter = MedicalHistoryRecyclerViewAdapter()
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        val list = mutableListOf<MedicalRecord>(
            MedicalRecord(date = simpleDateFormat.parse("23/11/2023")),
            MedicalRecord(date = simpleDateFormat.parse("24/11/2023")),
            MedicalRecord(date = simpleDateFormat.parse("25/11/2023")),
        )
        _recyclerViewAdapter.differ.submitList(list)
        _binding.rvMedicalHistory.apply {
            adapter =_recyclerViewAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
        _binding.ibMedicalHistoryBack.setOnClickListener {
            navigateToPage(R.id.action_medicalHistoryFragment_to_navigation_home)
        }

    }



}