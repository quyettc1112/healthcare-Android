package com.example.healthcarecomp.ui.auth.register

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseFragment
import com.example.healthcarecomp.base.dialog.ConfirmDialog
import com.example.healthcarecomp.data.model.User
import com.example.healthcarecomp.databinding.FragmentRegisterBinding
import com.example.healthcarecomp.ui.activity.AuthActivity
import com.example.healthcarecomp.util.Resource
import com.example.healthcarecomp.util.extension.afterTextChanged
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.example.healthcarecomp.util.ValidationUtils as ValidU

@AndroidEntryPoint
class RegisterFragment : BaseFragment(R.layout.fragment_register), View.OnClickListener {
    private lateinit var _binding: FragmentRegisterBinding
    lateinit var viewModel: RegisterViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        // handle user back to login page
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    navigateToPage(R.id.action_registerFragment_to_loginFragment)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeRegisterState()
        setupGGLogin()
        showDialog()

    }

    private fun setupGGLogin(){
        val userdata = arguments?.getString("userGG")
        userdata?.let {
            val user = Gson().fromJson(userdata, User::class.java)
            _binding.etSignUpFirstName.setText(user?.firstName)
            _binding.etSignUpLastName.setText(user?.lastName)
            _binding.etSignUpEmail.setText(user?.email)
            _binding.etSignUpEmail.isEnabled = false
            user?.phone.let {
                if(!user?.phone.isNullOrEmpty()){
                    _binding.etSignUpPhoneNumber.setText(user.phone)
                }
            }


        }

    }

    private fun showDialog() {

        if (requireActivity() is AuthActivity) {
            val confirmCallback = object : ConfirmDialog.ConfirmCallback {
                override fun positiveAction() {
                    Snackbar.make(requireView(), "Thank you", Snackbar.LENGTH_SHORT).show()
                }
                override fun negativeAction() {
                }

            }
            val authActivity = requireActivity() as AuthActivity
            authActivity.showConfirmDialog("Welcome to Health Care app",
                "We need some of your information, we do not share user info",
                "OK",
                "Cancle",
                "Yes",
                confirmCallback
                )
        }
    }

    private fun setupUI() {
        //set up display out line when user select role login
        _binding.ivSignUpDoctor.setOnClickListener(this)
        _binding.ivSignUpPatient.setOnClickListener(this)

        _binding.btnSignUp.setOnClickListener(this)
        _binding.tvSignUpLoginBtn.setOnClickListener(this)

        // input validate
        _binding.etSignUpFirstName.apply {
            this.afterTextChanged {
                error = when {
                    !ValidU.isValidFirstName(it) -> "First name must not be empty"
                    else -> null
                }
            }
        }
        _binding.etSignUpLastName.apply {
            this.afterTextChanged {
                error = when {
                    !ValidU.isValidLastName(it) -> "Last name must be >= 2 characters"
                    else -> null
                }
            }
        }
        _binding.etSignUpPasswordConfirm.apply {
            this.afterTextChanged {
                error = when {
                    !ValidU.isValidConfirmPassword(
                        it,
                        _binding.etSignUpPassword.text.toString()
                    ) -> "Confirm password is not match"

                    else -> null
                }
            }
        }
        _binding.etSignUpPhoneNumber.apply {
            this.afterTextChanged {
                try {
                    error = when {
                        !ValidU.isValidPhoneNumber(it) -> "Phone number is not valid"
                        else -> null
                    }
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                    error = "Phone must be number"
                }
            }
            _binding.etSignUpDoctorCode.apply {
                this.afterTextChanged {
                    error = when {
                        !ValidU.isValidDoctorSecurityCode(it) -> "Doctor code is not valid"
                        else -> null
                    }
                }
            }
            _binding.etSignUpEmail.apply {
                afterTextChanged {
                    error = if (!ValidU.validateEmail(it)) "Email format not valid"
                    else null
                }
            }
            _binding.etSignUpPassword.apply {
                afterTextChanged {
                    error = if (!ValidU.validatePassword(it)) {
                        "Password length must >= 8 characters," +
                                " has at least 1 uppercase, 1 digit, 1 special character"
                    } else null
                }
            }
        }
    }

    private fun isDoctorSignUp(): Boolean {
        return _binding.etSignUpDoctorCode.visibility == View.VISIBLE
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivSignUpDoctor -> {
                _binding.llSignUpDoctor.setBackgroundResource(R.drawable.img_high_light)
                _binding.llSignUpPatient.setBackgroundColor(Color.TRANSPARENT)
                _binding.etSignUpDoctorCode.visibility = View.VISIBLE
                _binding.tilSignUpDoctorCode.visibility = View.VISIBLE
            }

            R.id.ivSignUpPatient -> {
                _binding.llSignUpPatient.setBackgroundResource(R.drawable.img_high_light)
                _binding.llSignUpDoctor.setBackgroundColor(Color.TRANSPARENT)
                _binding.etSignUpDoctorCode.visibility = View.GONE
                _binding.tilSignUpDoctorCode.visibility = View.GONE
                _binding.etSignUpDoctorCode.text = null
            }

            R.id.btnSignUp -> {
                try {

                    if (!(ValidU.isValidLastName(_binding.etSignUpLastName.text.toString())
                                && ValidU.isValidFirstName(_binding.etSignUpFirstName.text.toString())
                                && ValidU.isValidConfirmPassword(
                            _binding.etSignUpPasswordConfirm.text.toString(),
                            _binding.etSignUpPassword.text.toString()
                        )
                                && ValidU.isValidPhoneNumber(
                            _binding.etSignUpPhoneNumber.text.toString()
                        )
                                && ValidU.validateEmail(_binding.etSignUpEmail.text.toString())
                                && ValidU.validatePassword(_binding.etSignUpPassword.text.toString())
                                && (ValidU.isValidDoctorSecurityCode(_binding.etSignUpDoctorCode.text.toString()) || !isDoctorSignUp())
                                )
                    ) {
                        Snackbar.make(
                            requireView(),
                            "Please fill in correctly all field",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else {
                        var doctorCode: String? = _binding.etSignUpDoctorCode.text.toString()
                        if (!isDoctorSignUp()) doctorCode = null

                        viewModel.register(
                            email = _binding.etSignUpEmail.text.toString(),
                            password = _binding.etSignUpPassword.text.toString(),
                            confirmPassword = _binding.etSignUpPasswordConfirm.text.toString(),
                            phone = _binding.etSignUpPhoneNumber.text.toString(),
                            firstName = _binding.etSignUpFirstName.text.toString(),
                            lastName = _binding.etSignUpPassword.text.toString(),
                            doctorCode = doctorCode
                        )
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            R.id.tvSignUpLoginBtn -> {
                navigateToPage(R.id.action_registerFragment_to_loginFragment)
            }

        }
    }

    private fun observeRegisterState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerFlow?.collectLatest {
                    _binding.pgRegister.visibility = View.GONE
                    when (it) {
                        is Resource.Error -> {
                            Toast.makeText(
                                requireContext(),
                                it.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is Resource.Loading -> {
                            _binding.pgRegister.visibility = View.VISIBLE
                        }

                        is Resource.Success -> {
                            Toast.makeText(
                                requireContext(),
                                "Sign up successfully please sign in",
                                Toast.LENGTH_SHORT
                            ).show()
                            val phone = _binding.etSignUpPhoneNumber.text.toString()
                            var bundle: Bundle = Bundle()
                            bundle.putString("phone", phone)
                            findNavController().navigate(R.id.action_registerFragment_to_loginFragment,bundle)
                        }

                        else -> null
                    }
                }
            }
        }
    }



}