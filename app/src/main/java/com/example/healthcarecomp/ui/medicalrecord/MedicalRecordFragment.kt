package com.example.healthcarecomp.ui.medicalrecord

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseFragment
import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.data.model.MedicalRecord
import com.example.healthcarecomp.databinding.FragmentMedicalRecordBinding
import com.example.healthcarecomp.util.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MedicalRecordFragment : BaseFragment(R.layout.fragment_medical_record) {
    private lateinit var _binding : FragmentMedicalRecordBinding
    private val args: MedicalRecordFragmentArgs by navArgs()
    private lateinit var _medicalRecordViewModel: MedicalRecordViewModel
    private var _currentMedical: MedicalRecord? = null

    companion object{
        val HEART_RATE = Constant.MEDICAL_RECORD_DIMENSION[Constant.MEDICAL_STATS.HEARTH_RATE.ordinal]
        val BODY_TEMPERATURE = Constant.MEDICAL_RECORD_DIMENSION[Constant.MEDICAL_STATS.BODY_TEMPERATURE.ordinal]
        val HEIGHT = Constant.MEDICAL_RECORD_DIMENSION[Constant.MEDICAL_STATS.HEIGHT.ordinal]
        val WEIGHT = Constant.MEDICAL_RECORD_DIMENSION[Constant.MEDICAL_STATS.WEIGHT.ordinal]
        val BLOOD_SUGAR = Constant.MEDICAL_RECORD_DIMENSION[Constant.MEDICAL_STATS.BLOOD_SUGAR.ordinal]
        val BLOOD_PRESSURE = Constant.MEDICAL_RECORD_DIMENSION[Constant.MEDICAL_STATS.BLOOD_PRESSURE.ordinal]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentMedicalRecordBinding.inflate(inflater, container, false)
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToPage(R.id.action_medicalRecordFragment_to_medicalHistoryFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,onBackPressedCallback)
        _medicalRecordViewModel = ViewModelProvider(this)[MedicalRecordViewModel::class.java]
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun CharSequence.showSackBarNotify(){
        Log.i("huhu", this.toString())
    }


    private fun setupUI(){

        //////////////// setup when click back button ////////////

        _binding.ibMedicalRecordBack.setOnClickListener {
            navigateToPage(R.id.action_medicalRecordFragment_to_medicalHistoryFragment)
        }

        ////////////////// Handle event when click save & edit button ///////////////

        _binding.ibMedicalRecordEdit.setOnClickListener {
            _medicalRecordViewModel.isEditMode.value = true
            it.visibility = View.GONE
            _binding.btnMedicalRecordSave.visibility = View.VISIBLE
        }

        _binding.btnMedicalRecordSave.setOnClickListener {
            val medical = takeStatsData()
            medical?.let {medical ->
                _medicalRecordViewModel.upsertMedicalRecord(
                    medical
                )
                it.visibility = View.GONE
                _binding.ibMedicalRecordEdit.visibility = View.VISIBLE
            }
        }

        _medicalRecordViewModel.upsertMedicalRecord.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success -> "Save successful!".showSackBarNotify()
                is Resource.Loading -> "Waiting...".showSackBarNotify()
                is Resource.Error -> "Ops error ${it.message}".showSackBarNotify()
                else -> {
                    "Unknown error".showSackBarNotify()
                }
            }
        })

        /////////// update data when data change ///////////

        _medicalRecordViewModel.onItemDataChange(args.medicalRecordId) { resources ->
            _medicalRecordViewModel.isEditMode.value = false
            when(resources) {
                is Resource.Success -> {
                    resources.data?.let {
                        _currentMedical = it
                    }
                    resources.data?.height?.let {
                        val text = "$it $HEIGHT"
                        _binding.etMedicalRecordHeight.setText(text)
                    }
                    resources.data?.weight?.let {
                        val text = "$it $WEIGHT"
                        _binding.etMedicalRecordWeight.setText(text)
                    }
                    resources.data?.general?.let {
                        _binding.etMedicalRecordNote.setText(it)
                    }
                    resources.data?.bloodPressure?.let {
                        val text = "$it $BLOOD_PRESSURE"
                        _binding.etMedicalRecordBloodPressure.setText(text)
                    }
                    resources.data?.bloodSugar?.let {
                        val text = "$it $BLOOD_SUGAR"
                        _binding.etMedicalRecordBloodSugar.setText(text)
                    }
                    resources.data?.hearthRate?.let {
                        val text = "$it $HEART_RATE"
                        _binding.etMedicalRecordHearthRate.setText(text)
                    }
                    resources.data?.bodyTemperature?.let {
                        val text = "$it $BODY_TEMPERATURE"
                        _binding.etMedicalRecordBodyTemperature.setText(text)
                    }


                }

                else -> {

                }
            }
        }

        ////////// observe edit or not /////////////
        _medicalRecordViewModel.isEditMode.observe(viewLifecycleOwner, Observer {
            _binding.etMedicalRecordNote.isEnabled = it
            _binding.etMedicalRecordHeight.isEnabled = it
            _binding.etMedicalRecordWeight.isEnabled = it
            _binding.etMedicalRecordBloodSugar.isEnabled = it
            _binding.etMedicalRecordBloodPressure.isEnabled = it
            _binding.etMedicalRecordBodyTemperature.isEnabled = it
            _binding.etMedicalRecordHearthRate.isEnabled = it

            _currentMedical?.let {medicalRecord ->
                var temperature = _currentMedical?.bodyTemperature.toString()
                var weight = _currentMedical?.weight.toString()
                var height = _currentMedical?.height.toString()
                var hearthRate = _currentMedical?.hearthRate.toString()
                var bloodSugar = _currentMedical?.bloodSugar.toString()
                var bloodPressure = _currentMedical?.bloodPressure.toString()

                when(it) {
                    true -> {

                    }
                    else -> {
                        temperature+= " $BODY_TEMPERATURE"
                        weight+=" $WEIGHT"
                        height+=" $HEIGHT"
                        hearthRate+=" $HEART_RATE"
                        bloodPressure+=" $BLOOD_PRESSURE"
                        bloodSugar+=" $BLOOD_SUGAR"
                    }
                }

                _binding.etMedicalRecordBodyTemperature.setText(temperature)
                _binding.etMedicalRecordHeight.setText(height)
                _binding.etMedicalRecordWeight.setText(weight)
                _binding.etMedicalRecordHearthRate.setText(hearthRate)
                _binding.etMedicalRecordBloodSugar.setText(bloodSugar)
                _binding.etMedicalRecordBloodPressure.setText(bloodPressure)
            }


        })




    }

    //////////// take data from view /////////////

    private fun takeStatsData(): MedicalRecord? {
        val hearthRate = _binding.etMedicalRecordHearthRate.text.toString().toIntOrNull()
        val weight = _binding.etMedicalRecordWeight.text.toString().toFloatOrNull()
        val height = _binding.etMedicalRecordHeight.text.toString().toFloatOrNull()
        val bloodPressure = _binding.etMedicalRecordBloodPressure.text.toString().toIntOrNull()
        val bloodSugar = _binding.etMedicalRecordBloodSugar.text.toString().toIntOrNull()
        val bodyTemperature = _binding.etMedicalRecordBodyTemperature.text.toString().toFloatOrNull()
        val general = _binding.etMedicalRecordNote.text.toString()

        when {
            bodyTemperature == null -> _binding.etMedicalRecordBodyTemperature.error = "Not valid value"
            weight == null -> _binding.etMedicalRecordWeight.error = "Not valid value"
            height == null -> _binding.etMedicalRecordHeight.error = "Not valid value"
            else -> {
                return MedicalRecord(
                    hearthRate = hearthRate,
                    weight = weight,
                    height = height,
                    bloodPressure = bloodPressure,
                    bloodSugar = bloodSugar,
                    bodyTemperature = bodyTemperature,
                    id = _currentMedical!!.id,
                    doctorId = _currentMedical!!.doctorId,
                    patientId = _currentMedical!!.patientId,
                    general = general
                )
            }
        }

        return null
    }


}