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
import com.example.healthcarecomp.data.model.MedicalRecord
import com.example.healthcarecomp.databinding.FragmentMedicalHistoryBinding
import com.example.healthcarecomp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

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
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,onBackPressedCallback)
        medicalHistoryViewModel = ViewModelProvider(this)[MedicalHistoryViewModel::class.java]
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    private fun setupUI() {

        _binding.btnMedicalHistoryAdd.setOnClickListener {
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
            val medicalRecord = MedicalRecord(
                date = simpleDateFormat.parse("23/11/2023")
                , doctorId = "11111111",
                patientId = "2222222",
                bodyTemperature = 37.7F,
                bloodPressure = 70,
                hearthRate = 110,
                bloodSugar = 28,
                general = "it is good",
                height = 170.7F,
                weight = 71.0F)
            medicalHistoryViewModel.upsertMedialRecord(medicalRecord)
        }

        _recyclerViewAdapter = MedicalHistoryRecyclerViewAdapter()
        //set on item to see detail
        _recyclerViewAdapter.setOnItemDetailButtonClick {
            val directions = MedicalHistoryFragmentDirections.actionMedicalHistoryFragmentToMedicalRecordFragment(it.id)
            navigateToPage(directions)
        }

        _binding.rvMedicalHistory.apply {
            adapter =_recyclerViewAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
        _binding.ibMedicalHistoryBack.setOnClickListener {
            navigateToPage(R.id.action_medicalHistoryFragment_to_navigation_home)
        }

        medicalHistoryViewModel.medicalAdded.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Success -> {
                    Log.e("lol", "add done")
                }
                is Resource.Error -> {
                    Log.e("lol", "add err: ${it.message}")
                }

                is Resource.Loading -> {
                    Log.e("lol", "add loading")
                }

                else -> {
                    Log.e("lol", "add unkown")
                }
            }
        })

        medicalHistoryViewModel.medicalHistoryList.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Success -> {
                    hindLoadingBar()
                    _recyclerViewAdapter.differ.submitList(it.data?.toList())
                }

                is Resource.Loading -> {
                    showLoadingBar()
                    Log.e("lol", "loading")
                }

                is Resource.Error -> {
                    hindLoadingBar()
                    Log.e("Medical History Fragment", it.message!!)
                }

                else -> {
                    Log.e("lol", "unknow")
                }
            }
        })

    }

    private fun showLoadingBar(){
        _binding.pbMedicalHistory.visibility = View.VISIBLE
    }

    private fun hindLoadingBar(){
        _binding.pbMedicalHistory.visibility = View.INVISIBLE
    }



}