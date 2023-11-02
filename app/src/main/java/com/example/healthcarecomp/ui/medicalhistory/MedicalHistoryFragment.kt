package com.example.healthcarecomp.ui.medicalhistory

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseFragment
import com.example.healthcarecomp.common.Constant.Companion.PATIENT_MEDICAL_HISTORY_KEY
import com.example.healthcarecomp.data.model.MedicalRecord
import com.example.healthcarecomp.databinding.FragmentMedicalHistoryBinding
import com.example.healthcarecomp.ui.activity.MainActivity
import com.example.healthcarecomp.util.Resource
import com.example.healthcarecomp.util.extension.isDoctor
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

@AndroidEntryPoint
class MedicalHistoryFragment : BaseFragment(R.layout.fragment_medical_history) {
    private lateinit var _binding: FragmentMedicalHistoryBinding
    private lateinit var _recyclerViewAdapter: MedicalHistoryRecyclerViewAdapter
    private lateinit var _medicalHistoryViewModel: MedicalHistoryViewModel
    private var _parent: MainActivity? = null
    private lateinit var _patientId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // handle back press
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
        //set up view model
        _medicalHistoryViewModel = ViewModelProvider(this)[MedicalHistoryViewModel::class.java]
        _patientId = arguments?.getString(PATIENT_MEDICAL_HISTORY_KEY)!!
        _medicalHistoryViewModel.invoke(_patientId)
        //
        _parent = requireActivity() as? MainActivity
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        _parent?.let {
            if (it.currentUser.isDoctor()) {
                _binding.btnMedicalHistoryAdd.visibility = View.VISIBLE
                _binding.btnMedicalHistoryAdd.setOnClickListener {
                    val directions =
                        MedicalHistoryFragmentDirections.actionMedicalHistoryFragmentToMedicalRecordFragment(
                            null,
                            _patientId,
                            null
                        )
                    navigateToPage(directions)
                }
            }

        }


        _recyclerViewAdapter = MedicalHistoryRecyclerViewAdapter()
        //set on item to see detail
        _recyclerViewAdapter.setOnItemDetailButtonClick {
            val directions =
                MedicalHistoryFragmentDirections.actionMedicalHistoryFragmentToMedicalRecordFragment(
                    it.id,
                    it.patientId,
                    it.doctorId
                )
            navigateToPage(directions)
        }

        _binding.rvMedicalHistory.apply {
            adapter = _recyclerViewAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
        _binding.ibMedicalHistoryBack.setOnClickListener {
            navigateToPage(R.id.action_medicalHistoryFragment_to_navigation_home)
        }

        _medicalHistoryViewModel.medicalAdded.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                }

                is Resource.Error -> {
                }

                is Resource.Loading -> {
                }

                else -> {
                }
            }
        })

        _medicalHistoryViewModel.medicalHistoryList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    hindLoadingBar()
                    _recyclerViewAdapter.doctorList = _medicalHistoryViewModel.doctorList
                    _recyclerViewAdapter.differ.submitList(it.data?.toList())
                }

                is Resource.Loading -> {
                    showLoadingBar()
                }

                is Resource.Error -> {
                    hindLoadingBar()
                }

                else -> {
                }
            }
        })

    }

    private fun showLoadingBar() {
        _binding.pbMedicalHistory.visibility = View.VISIBLE
    }

    private fun hindLoadingBar() {
        _binding.pbMedicalHistory.visibility = View.INVISIBLE
    }


}