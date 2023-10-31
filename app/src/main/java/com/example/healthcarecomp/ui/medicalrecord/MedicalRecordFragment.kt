package com.example.healthcarecomp.ui.medicalrecord

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseFragment
import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.data.model.MedicalRecord
import com.example.healthcarecomp.databinding.FragmentMedicalRecordBinding
import com.example.healthcarecomp.util.extension.afterTextChanged
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import com.example.healthcarecomp.util.ValidationUtils as ValidU

@AndroidEntryPoint
class MedicalRecordFragment : BaseFragment(R.layout.fragment_medical_record) {
    private lateinit var _binding: FragmentMedicalRecordBinding
    private val args: MedicalRecordFragmentArgs by navArgs()
    private lateinit var _medicalRecordViewModel: MedicalRecordViewModel
    private var _currentMedical: MedicalRecord? = null


    companion object {
        val HEART_RATE = Constant.MEDICAL.INT.HEARTH_RATE.dimension
        val BODY_TEMPERATURE = Constant.MEDICAL.FLOAT.BODY_TEMPERATURE.dimension
        val HEIGHT = Constant.MEDICAL.FLOAT.HEIGHT.dimension
        val WEIGHT = Constant.MEDICAL.FLOAT.WEIGHT.dimension
        val BLOOD_SUGAR = Constant.MEDICAL.INT.BLOOD_SUGAR.dimension
        val BLOOD_PRESSURE = Constant.MEDICAL.STRING.BLOOD_PRESSURE.dimension
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
                var bundle = Bundle()
                bundle.putString(Constant.PATIENT_MEDICAL_HISTORY_KEY, args.patientId)
                navigateToPage(R.id.action_medicalRecordFragment_to_medicalHistoryFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
        _medicalRecordViewModel = ViewModelProvider(this)[MedicalRecordViewModel::class.java]
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()

    }


    private fun setupUI() {

        //////////////// setup when click back button ////////////

        _binding.ibMedicalRecordBack.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Constant.PATIENT_MEDICAL_HISTORY_KEY, args.patientId)
            navigateToPage(R.id.action_medicalRecordFragment_to_medicalHistoryFragment, bundle)
        }

        ////////////////// Handle event when click save & edit button ///////////////

        _binding.ibMedicalRecordEdit.setOnClickListener {
            _medicalRecordViewModel.isEditMode.value = true
            it.visibility = View.GONE
            _binding.ibMedicalRecordSave.visibility = View.VISIBLE
        }
        _binding.ibMedicalRecordSave.setOnClickListener {
            val medical = takeStatsData()
            medical?.let { medical ->
                _medicalRecordViewModel.upsertMedicalRecord(
                    medical
                )
                it.visibility = View.GONE
                _binding.ibMedicalRecordEdit.visibility = View.VISIBLE
                _medicalRecordViewModel.isEditMode.value = false
            }
        }
        /////////// validation for input field //////////
        _binding.etMedicalRecordHeight.apply {
            if (_medicalRecordViewModel.isEditMode.value!!) {
                this.afterTextChanged {
                    error = when {
                        ValidU.isValidHeight(it) -> null
                        else -> "Height must be in range ${Constant.MEDICAL.FLOAT.HEIGHT.range}"
                    }
                }
            }
        }

        _binding.etMedicalRecordWeight.apply {
            this.afterTextChanged {
                if (_medicalRecordViewModel.isEditMode.value!!) {
                    error = when {
                        ValidU.isValidWeight(it) -> null
                        else -> "Weight must be in range ${Constant.MEDICAL.FLOAT.WEIGHT.range}"
                    }
                }
            }
        }

        _binding.etMedicalRecordBloodPressure.apply {
            this.afterTextChanged {
                if (_medicalRecordViewModel.isEditMode.value!!) {
                    error = when {
                        ValidU.isValidBloodPressure(it) -> null
                        else -> "Blood pressure must be format xx/xx"
                    }
                }
            }
        }

        _binding.etMedicalRecordBloodSugar.apply {
            this.afterTextChanged {
                if (_medicalRecordViewModel.isEditMode.value!!) {
                    error = when {
                        ValidU.isValidBloodSugar(it) -> null
                        else -> "Blood sugar must be in range ${Constant.MEDICAL.INT.BLOOD_SUGAR.range}"
                    }
                }
            }
        }

        _binding.etMedicalRecordHearthRate.apply {
            this.afterTextChanged {
                if (_medicalRecordViewModel.isEditMode.value!!) {
                    error = when {
                        ValidU.isValidHearthRate(it) -> null
                        else -> "Hearth rate must be in range ${Constant.MEDICAL.INT.HEARTH_RATE.range}"
                    }
                }
            }
        }

        _binding.etMedicalRecordBodyTemperature.apply {

            this.afterTextChanged {
                if (_medicalRecordViewModel.isEditMode.value!!) {
                    error = when {
                        ValidU.isValidBodyTemperature(it) -> null
                        else -> "Temperature must be in range ${Constant.MEDICAL.FLOAT.BODY_TEMPERATURE.range}"
                    }
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

            _currentMedical?.let { _ ->
                var temperature = _currentMedical?.bodyTemperature.toString()
                var weight = _currentMedical?.weight.toString()
                var height = _currentMedical?.height.toString()
                var hearthRate = _currentMedical?.hearthRate.toString()
                var bloodSugar = _currentMedical?.bloodSugar.toString()
                var bloodPressure = _currentMedical?.bloodPressure.toString()

                when (it) {
                    true -> {

                    }

                    else -> {
                        temperature += " $BODY_TEMPERATURE"
                        weight += " $WEIGHT"
                        height += " $HEIGHT"
                        hearthRate += " $HEART_RATE"
                        bloodPressure += " $BLOOD_PRESSURE"
                        bloodSugar += " $BLOOD_SUGAR"
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
    //validation
    private fun takeStatsData(): MedicalRecord? {
        val hearthRate = _binding.etMedicalRecordHearthRate.text.toString()
        val weight = _binding.etMedicalRecordWeight.text.toString()
        val height = _binding.etMedicalRecordHeight.text.toString()
        val bloodPressure = _binding.etMedicalRecordBloodPressure.text.toString()
        val bloodSugar = _binding.etMedicalRecordBloodSugar.text.toString()
        val bodyTemperature = _binding.etMedicalRecordBodyTemperature.text.toString()
        val general = _binding.etMedicalRecordNote.text.toString()

        if (ValidU.isValidWeight(weight) && ValidU.isValidHeight(height)
            && ValidU.isValidHearthRate(hearthRate) && ValidU.isValidBloodSugar(bloodSugar)
            && ValidU.isValidBloodPressure(bloodPressure) && ValidU.isValidBodyTemperature(
                bodyTemperature
            )
        ) {
            return MedicalRecord(
                hearthRate = hearthRate.toIntOrNull(),
                weight = weight.toFloatOrNull(),
                height = height.toFloatOrNull(),
                bloodPressure = bloodPressure,
                bloodSugar = bloodSugar.toIntOrNull(),
                bodyTemperature = bodyTemperature.toFloatOrNull(),
                id = _currentMedical!!.id,
                doctorId = _currentMedical!!.doctorId,
                patientId = _currentMedical!!.patientId,
                general = general
            )
        } else {
            Snackbar.make(requireView(), "Please fill in all field!", Snackbar.LENGTH_SHORT).show()
        }

        return null
    }


}