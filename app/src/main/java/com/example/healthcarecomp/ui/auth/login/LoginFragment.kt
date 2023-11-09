package com.example.healthcarecomp.ui.auth.login

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseFragment
import com.example.healthcarecomp.data.model.User
import com.example.healthcarecomp.databinding.FragmentLoginBinding
import com.example.healthcarecomp.helper.BiometricHelper
import com.example.healthcarecomp.ui.activity.auth.AuthActivity
import com.example.healthcarecomp.ui.activity.main.MainActivity
import com.example.healthcarecomp.util.Resource
import com.example.healthcarecomp.util.extension.isDoctor
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment(R.layout.fragment_login), View.OnClickListener {
    private lateinit var _binding: FragmentLoginBinding
    private lateinit var _viewModel: LoginViewModel
    private var _userGG: User? = null
    private lateinit var biometricHelper: BiometricHelper


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        _viewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)

        autoLogin()

        return _binding.root
    }

    private fun autoLogin() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && _viewModel.getLoggedInUser() != null) {
            biometricHelper = BiometricHelper(this, { errorCode, errString ->
                Toast.makeText(requireContext(), errString, Toast.LENGTH_SHORT).show()
            }, {
                _viewModel.isBiometricSuccess.value = true
            }) {
                null
            }
            biometricHelper.authenticateWithBiometric()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
        setEvents()
        setObservers()
    }

    private fun setUI() {
        //auto input phone
        arguments?.getString("phone")?.let {
            _binding.etLoginPhoneNumber.setText(it)
        }
    }

    private fun setEvents() {

        //set up display out line when user select role login
        _binding.ivLoginPatient.setOnClickListener(this)
        _binding.ivLoginDoctor.setOnClickListener(this)

        //setup login btn
        _binding.ibLoginWithGoogle.setOnClickListener(this)
        _binding.btnLogin.setOnClickListener(this)

        _binding.tvLoginSignUpBtn.setOnClickListener(this)
        _binding.tvLoginForgotBnt.setOnClickListener(this)

    }


    override fun onClick(v: View?) {
        when (v?.id) {

            //set up display out line when user select role login
            R.id.ivLoginPatient -> {
                _binding.llLoginPatientBorder.setBackgroundResource(R.drawable.img_high_light)
                _binding.llLoginDoctorBorder.setBackgroundColor(Color.TRANSPARENT)
            }

            R.id.ivLoginDoctor -> {
                _binding.llLoginDoctorBorder.setBackgroundResource(R.drawable.img_high_light)
                _binding.llLoginPatientBorder.setBackgroundColor(Color.TRANSPARENT)
            }

            //
            R.id.btnLogin -> {

                _viewModel.loginByPhone(
                    _binding.etLoginPhoneNumber.text.toString(),
                    _binding.etLoginPassword.text.toString()
                )
            }

            R.id.ibLoginWithGoogle -> {
                loginWithGG()
            }

            R.id.tvLoginSignUpBtn -> {
                navigateToPage(R.id.action_loginFragment_to_registerFragment)
            }


        }
    }

    private fun loginWithGG() {
        (requireActivity() as AuthActivity).loginWithGoogle() {
            if (it == null) {
                Toast.makeText(
                    requireActivity(),
                    "Have problem to login with google",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                _userGG = it
                _viewModel.loginByMail(it.email!!)
            }
        }

    }

    private fun setObservers() {
        //biometric login state
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                _viewModel.isBiometricSuccess.collectLatest {
                    if (_viewModel.isBiometricSuccess.value) {
                        _viewModel.autoLogin()
                    }
                }
            }
        }

        //Observe login state
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                _viewModel.loginFLow?.collectLatest {
                    _binding.pgLogin.visibility = View.GONE
                    when (it) {
                        is Resource.Error -> {
                            Toast.makeText(
                                requireContext(),
                                it.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is Resource.Loading -> {
                            _binding.pgLogin.visibility = View.VISIBLE
                        }

                        is Resource.Success -> {
                            Log.d("Auth", _viewModel.getLoggedInUser().toString())
                            val prefix =
                                if (_viewModel.getLoggedInUser()!!.isDoctor()) " Dr." else ""
                            Toast.makeText(
                                requireContext(),
                                "Welcome$prefix ${_viewModel.getLoggedInUser()?.firstName}",
                                Toast.LENGTH_SHORT
                            ).show()
                            openMainActivity()
                        }

                        else -> null
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                _viewModel.loginGGFlow?.collectLatest {
                    _binding.pgLogin.visibility = View.GONE
                    when (it) {
                        is Resource.Success -> {
                            val prefix =
                                if (_viewModel.getLoggedInUser()!!.isDoctor()) " Dr." else ""
                            Toast.makeText(
                                requireContext(),
                                "Welcome$prefix ${_viewModel.getLoggedInUser()?.firstName}",
                                Toast.LENGTH_SHORT
                            ).show()
                            openMainActivity()
                        }

                        is Resource.Loading -> {
                            _binding.pgLogin.visibility = View.VISIBLE
                        }

                        is Resource.Error -> {
                            val data = Gson().toJson(_userGG)
                            val bundle = Bundle()
                            bundle.putString("userGG", data)
                            navigateToPage(R.id.action_loginFragment_to_registerFragment, bundle)
                            _viewModel.loginGGFlow.value = null
                        }

                        else -> null
                    }
                }
            }
        }
    }

    private fun openMainActivity() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

}