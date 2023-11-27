package com.example.healthcarecomp.ui.info

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.nfc.FormatException
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseFragment
import com.example.healthcarecomp.base.dialog.ConfirmDialog
import com.example.healthcarecomp.base.dialog.NFCBottomSheetNNotify
import com.example.healthcarecomp.data.model.Doctor
import com.example.healthcarecomp.data.model.Patient
import com.example.healthcarecomp.databinding.FragmentInfoBinding
import com.example.healthcarecomp.helper.NFCHelper
import com.example.healthcarecomp.ui.activity.auth.AuthActivity
import com.example.healthcarecomp.ui.activity.auth.AuthViewModel
import com.example.healthcarecomp.ui.activity.main.MainActivity
import com.example.healthcarecomp.ui.activity.main.MainViewModel
import com.example.healthcarecomp.ui.auth.login.LoginViewModel
import com.example.healthcarecomp.util.ConvertUtils
import com.example.healthcarecomp.util.Resource
import com.example.healthcarecomp.util.ValidationUtils
import com.example.healthcarecomp.util.extension.afterTextChanged
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.io.UnsupportedEncodingException

@AndroidEntryPoint
class InfoFragment : BaseFragment(R.layout.fragment_info) {

    private lateinit var _binding: FragmentInfoBinding
    private lateinit var _viewModel: InfoViewModel
    private lateinit var _loginViewModel: LoginViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var datePickerDialog: DatePickerDialog
    lateinit var authViewModel: AuthViewModel
    private var nfcAdapter: NfcAdapter? = null
    private var myTag: Tag? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        _viewModel = ViewModelProvider(this)[InfoViewModel::class.java]
        _loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        return _binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = (requireActivity() as MainActivity).mainViewModel
        setUI()
        setEvents()
        setObservers()
        setUp_NFC_UI()
    }

    private fun setUp_NFC_UI() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(requireContext())
        if (nfcAdapter == null) {
            _binding.btnsaveNFCInfo.visibility = View.GONE
        }
        _binding.btnsaveNFCInfo.setOnClickListener {
            if (nfcAdapter!!.isEnabled) {

                    //myTag = requireActivity().intent.getParcelableExtra<Parcelable>(NfcAdapter.EXTRA_TAG) as Tag?
                val mainActivity = activity as MainActivity
                    try {
                        if (mainActivity.myTag == null) {
                            Toast.makeText(context, NFCHelper.ERROR_DETECTED, Toast.LENGTH_LONG).show()
                        } else {
                            NFCHelper(authViewModel, myTag, requireContext()).writeMultipleRecords("com.easa", "012414sbahusfbasjhfabfhas14", "asdafasf", "1214", mainActivity.myTag)
                            Toast.makeText(context, NFCHelper.WRITE_SUCCESS, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: IOException) {
                        Toast.makeText(context, NFCHelper.WRITE_ERROR, Toast.LENGTH_LONG).show()
                        e.printStackTrace()
                    } catch (e: FormatException) {
                        Toast.makeText(context, NFCHelper.WRITE_ERROR, Toast.LENGTH_LONG).show()
                        e.printStackTrace()
                    }


            } else showNFCDisabledDialog()
        }
    }

    private fun showNFCDisabledDialog() {
        // Tạo một đối tượng dialog
        val dialog = AlertDialog.Builder(requireContext())

        // Thiết lập tiêu đề của dialog
        dialog.setTitle("NFC bị vô hiệu hóa")

        // Thiết lập nội dung của dialog
        dialog.setMessage("NFC bị vô hiệu hóa. Vui lòng bật NFC trong cài đặt của bạn để sử dụng tính năng này.")

        // Thêm nút "OK" vào dialog
        dialog.setPositiveButton("OK") { _, _ ->
            // Mở cài đặt NFC
            val intent = Intent(Settings.ACTION_NFC_SETTINGS)
            startActivity(intent)
        }
        // Hiển thị dialog
        dialog.show()
    }

    private fun setUI() {
        _viewModel.isEditing.value = false

        val currentDate: Calendar = Calendar.getInstance()
        datePickerDialog = DatePickerDialog(
            this.requireContext(),
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                _binding.etDob.setText("$dayOfMonth/${month + 1}/$year")
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
                    val firstName = _binding.etInfoFirstName.text.toString()
                    val lastName = _binding.etInfoLastName.text.toString()

                    val password = _binding.etInfoPassword.text.toString()
                    val confirmPassword = _binding.etInfoPasswordConfirm.text.toString()
                    val gender = _binding.rbMale.isChecked

                    val avatar = mainViewModel.currentUser.value?.avatar
                    var dob: Long? = null
                    val dobCharSequence = _binding.etDob.text.toString()

                    if (!(ValidationUtils.isValidLastName(lastName)
                                && ValidationUtils.isValidFirstName(firstName)
                                && ValidationUtils.isValidConfirmPassword(password, confirmPassword)
                                && ValidationUtils.validatePassword(password)
                                && ValidationUtils.isValidDob(dobCharSequence))
                    ) {
                        _viewModel.userEditState.value =
                            Resource.Error("Please fill in correctly all field")
                        _viewModel.isEditing.value = !_viewModel.isEditing.value!!
                        mainViewModel.currentUser = mainViewModel.currentUser
                    } else {
                        try {
                            dob = ConvertUtils.convertDateStringToLong(dobCharSequence)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            _viewModel.userEditState.value = Resource.Error("Error input date")
                            _viewModel.isEditing.value = !_viewModel.isEditing.value!!
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
                    try {
                        Glide.with(requireActivity()).load(it.avatar).into(ivUserAvatar)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        ivUserAvatar.setImageURI(Uri.parse(it.avatar))
                    }
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
            _binding.etDob.apply {
                afterTextChanged {
                    error = if (!ValidationUtils.isValidDob(it)) {
                        "Date of birth format is dd/MM/yyyy (ex: 01/01/2023)"
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
                    Log.d("Image", "loading")
                    _viewModel.userEditState.value = Resource.Loading()
                }, onSuccess = {
                    Log.d("Image", "success")
                    hideLoading()
                    _binding.ivUserAvatar.setImageURI(it)
                    _viewModel.upsertUser(avatar = it.toString())
                }, onFailure = {
                    Log.d("Image", "fail")
                    _viewModel.userEditState.value = Resource.Error("Upload image failed")
                    Toast.makeText(this.requireContext(), it, Toast.LENGTH_SHORT).show()
                })
            }
        }
    }

}