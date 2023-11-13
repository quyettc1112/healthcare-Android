package com.example.healthcarecomp.ui.info

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseFragment
import com.example.healthcarecomp.base.dialog.ConfirmDialog
import com.example.healthcarecomp.data.model.Doctor
import com.example.healthcarecomp.data.model.Patient
import com.example.healthcarecomp.databinding.FragmentInfoBinding
import com.example.healthcarecomp.ui.activity.auth.AuthActivity
import com.example.healthcarecomp.ui.activity.main.MainActivity
import com.example.healthcarecomp.ui.activity.main.MainViewModel
import com.example.healthcarecomp.util.ConvertUtils
import com.example.healthcarecomp.util.Resource
import com.example.healthcarecomp.util.ValidationUtils
import com.example.healthcarecomp.util.extension.afterTextChanged
import dagger.hilt.android.AndroidEntryPoint
import java.sql.Timestamp
import java.util.Date
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class InfoFragment : BaseFragment(R.layout.fragment_info) {

    private lateinit var _binding: FragmentInfoBinding
    private lateinit var _viewModel: InfoViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var datePickerDialog: DatePickerDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        _viewModel = ViewModelProvider(this)[InfoViewModel::class.java]


        return _binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = (requireActivity() as MainActivity).mainViewModel
        setUI()
        setEvents()
        setObservers()
    }

    private fun setUI() {
        val currentDate: Calendar = Calendar.getInstance()
        datePickerDialog = DatePickerDialog(
            this.requireContext(),
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                _binding.etDob.setText("$dayOfMonth/${month+1}/$year")
            },
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DAY_OF_MONTH)
        )

    }

    //    @RequiresApi(Build.VERSION_CODES.O)
    private fun setEvents() {
        _binding.etDob.setOnClickListener {
            datePickerDialog.show()
        }

        //upload image
        _binding.ibEditAvatar.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                startActivityForResult(it, 0)
            }
        }

        //logout
        _binding.ibLogout.setOnClickListener {
            _viewModel.logout()
            val confirmCallback = object : ConfirmDialog.ConfirmCallback {
                override fun positiveAction() {
                    startActivity(Intent(requireActivity(), AuthActivity::class.java))
                    requireActivity().finish()
                }

                override fun negativeAction() {
                }
            }
            (requireActivity() as MainActivity).showConfirmDialog(
                "Logging out",
                "Do you really want to log out?",
                "Yes",
                "Cancel",
                "?",
                confirmCallback
            )

        }

        //edit info fab
        _binding.fabEdit.setOnClickListener {
            _viewModel.isEditing.value?.let {
                _viewModel.isEditing.value = !_viewModel.isEditing.value!!
                if (it) {
                    var firstName = _binding.etInfoFirstName.text.toString()
                    var lastName = _binding.etInfoLastName.text.toString()

                    var password = _binding.etInfoPassword.text.toString()
                    var confirmPassword = _binding.etInfoPasswordConfirm.text.toString()
                    var gender = _binding.rbMale.isChecked

                    var avatar = mainViewModel.currentUser.value?.avatar
                    var dob: Long? = null
                    var dobCharSequence = _binding.etDob.text.toString()

                    if (!(ValidationUtils.isValidLastName(lastName)
                                && ValidationUtils.isValidFirstName(firstName)
                                && ValidationUtils.isValidConfirmPassword(password, confirmPassword)
                                && ValidationUtils.validatePassword(password)
                                && ValidationUtils.isValidDob(dobCharSequence))
                    ) {
                        _viewModel.userEditState.value =
                            Resource.Error("Please fill in correctly all field")
                        mainViewModel.currentUser = mainViewModel.currentUser
                    } else {
                        try {
                            dob = ConvertUtils.convertDateStringToLong(dobCharSequence)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            _viewModel.userEditState.value = Resource.Error("Error input date")
                        }
                        _viewModel.upsertUser(
                            firstName = firstName,
                            lastName = lastName,
                            password = password,
                            confirmPassword = confirmPassword,
                            gender = gender,
                            dob = dob,
                            avatar = avatar
                        )
                    }
                }
            }
        }
    }


    private fun setObservers() {

        _viewModel.userEditState.observe(viewLifecycleOwner) {
            hideLoading()
            when (it) {
                is Resource.Success -> mainViewModel.currentUser.value = it.data
                is Resource.Loading -> showLoading("Updating profile", "Please wait...")
                is Resource.Error -> Toast.makeText(
                    requireContext(),
                    it.message,
                    Toast.LENGTH_SHORT
                ).show()
                else -> mainViewModel.currentUser = mainViewModel.currentUser
            }
        }

        //observe user info fields
        mainViewModel.currentUser.observe(viewLifecycleOwner, Observer {
            it?.let {
                _binding.run {
                    etInfoFirstName.setText(it.firstName)
                    etInfoLastName.setText(it.lastName)
                    etInfoEmail.setText(it.email)
                    etInfoPhoneNumber.setText(it.phone)
                    etInfoPassword.setText(it.password)
                    etInfoPasswordConfirm.setText(it.password)
                    etDob.setText(it.dob?.let { dob -> ConvertUtils.convertLongToDateString(dob) })
                    it.gender?.let { gender ->
                        when (gender) {
                            true -> rbMale.isChecked = true
                            false -> rbFemale.isChecked = true
                        }
                    }
                    Glide.with(requireActivity()).load(it.avatar).into(ivUserAvatar)
                    var prefix = if (it is Doctor) "Dr"
                    else if (it is Patient) {
                        if (it.gender == false) "Mrs" else "Mr"
                    } else ""
                    tvUserName.text = "$prefix.${it.firstName}"
                }
            }
        })

        //observe edit state
        _viewModel.isEditing.observe(viewLifecycleOwner, Observer {
            it?.let {
                changeEditState(it)
            }
        })

        // input validate
        _binding.etInfoFirstName.apply {
            this.afterTextChanged {
                error = when {
                    !ValidationUtils.isValidFirstName(it) -> "First name must not be empty"
                    else -> null
                }
            }
        }
        _binding.etInfoLastName.apply {
            this.afterTextChanged {
                error = when {
                    !ValidationUtils.isValidLastName(it) -> "Last name must be >= 2 characters"
                    else -> null
                }
            }
        }
        _binding.etInfoPasswordConfirm.apply {
            this.afterTextChanged {
                error = when {
                    !ValidationUtils.isValidConfirmPassword(
                        it,
                        _binding.etInfoPassword.text.toString()
                    ) -> "Confirm password is not match"

                    else -> null
                }
            }
        }
        _binding.etInfoPhoneNumber.apply {
            this.afterTextChanged {
                try {
                    error = when {
                        !ValidationUtils.isValidPhoneNumber(it) -> "Phone number is not valid"
                        else -> null
                    }
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                    error = "Phone must be number"
                }
            }
            _binding.etInfoEmail.apply {
                afterTextChanged {
                    error = if (!ValidationUtils.validateEmail(it)) "Email format not valid"
                    else null
                }
            }
            _binding.etInfoPassword.apply {
                afterTextChanged {
                    error = if (!ValidationUtils.validatePassword(it)) {
                        "Password length must >= 8 characters," +
                                " has at least 1 uppercase, 1 digit, 1 special character"
                    } else null
                }
            }
        }
    }

    private fun changeEditState(isEditing: Boolean) {
        _binding.run {
            etInfoFirstName.isEnabled = isEditing
            etInfoLastName.isEnabled = isEditing
            rbMale.isEnabled = isEditing
            rbFemale.isEnabled = isEditing
            etInfoPassword.isEnabled = isEditing
            etInfoPassword.isEnabled = isEditing
            etInfoPasswordConfirm.isEnabled = isEditing
            etDob.isEnabled = isEditing
            if (isEditing) {
                fabEdit.setImageResource(R.drawable.ic_save_24)
            } else {
                fabEdit.setImageResource(R.drawable.ic_edit)
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 0) {
            val uri = data?.data
//            _binding.ivUserAvatar.setImageURI(uri)
            Log.d("image", uri.toString())

            if (uri != null) {
                _viewModel.uploadImage(uri, onProgress = {
                    showLoading(
                        "Upload Image",
                        "Image is uploading please wait",
                    )
                }, onSuccess = {
                    hideLoading()
                    _binding.ivUserAvatar.setImageURI(it)
//                    _viewModel.upsertUser()
                }, onFailure = {
                    hideLoading()
                    Toast.makeText(this.requireContext(), it, Toast.LENGTH_SHORT).show()
                })
            }
        }
    }
}