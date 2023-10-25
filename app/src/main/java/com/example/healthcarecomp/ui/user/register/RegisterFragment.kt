package com.example.healthcarecomp.ui.user.register

import android.graphics.Color
import android.opengl.Visibility
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
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseFragment
import com.example.healthcarecomp.databinding.FragmentRegisterBinding
import com.example.healthcarecomp.util.Resource
import com.example.healthcarecomp.util.ValidationUtils
import com.example.healthcarecomp.util.extension.afterTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
                    it.length < 2 -> "First name is not valid"
                    else -> null
                }
            }
        }
        _binding.etSignUpLastName.apply {
            this.afterTextChanged {
                error = when {
                    it.length < 2 -> "Last name is not valid"
                    else -> null
                }
            }
        }
        _binding.etSignUpPassword.apply {
            this.afterTextChanged {
                error = when {
                    it.length <= 6 -> "Password too short"
                    else -> null
                }
            }
        }
        _binding.etSignUpPhoneNumber.apply {
            this.afterTextChanged {
                error = when {
                    it.length !in 9..11 -> "Phone number is not valid"
                    else -> null
                }
            }
        }
        _binding.etSignUpDoctorCode.apply {
            this.afterTextChanged {
                error = when {
                    it.length != 6 -> "Doctor code is not valid"
                    else -> null
                }
            }
        }
        _binding.etSignUpEmail.apply {
            afterTextChanged {
                error = if (!ValidationUtils.validateEmail(it)) "Email format not valid"
                else null
            }
        }
        _binding.etSignUpPassword.apply {
            afterTextChanged {
                error = if (!ValidationUtils.validatePassword(it)) {
                    "Password length must >= 8 characters," +
                            " has at least 1 uppercase, 1 digit, 1 special character"
                } else null
            }
        }
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
            }

            R.id.btnSignUp -> {
                viewModel.register(
                    email = _binding.etSignUpEmail.text.toString(),
                    password = _binding.etSignUpPassword.text.toString(),
                    confirmPassword = _binding.etSignUpPasswordConfirm.text.toString(),
                    phone = _binding.etSignUpPhoneNumber.text.toString().toInt(),
                    firstName = _binding.etSignUpFirstName.text.toString(),
                    lastName = _binding.etSignUpPassword.text.toString()
                )
                observeRegisterState()
            }

            R.id.tvSignUpLoginBtn -> {
                navigateToPage(R.id.action_registerFragment_to_loginFragment)
            }
        }
    }

    private fun observeRegisterState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerFlow?.collectLatest{
                    _binding.pgRegister.visibility = View.INVISIBLE
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
                                "Welcome ${viewModel.registerFlow.value.toString()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> Toast.makeText(
                            requireContext(),
                            "Something go wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }


}