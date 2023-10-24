package com.example.healthcarecomp.ui.user.register

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseFragment
import com.example.healthcarecomp.databinding.FragmentRegisterBinding


class RegisterFragment : BaseFragment(R.layout.fragment_register), View.OnClickListener {
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentRegisterBinding.inflate(inflater,container, false)

        // handle user back to login page
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    navigateToPage(R.id.action_registerFragment_to_loginFragment)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }
    private fun setupUI() {
        //set up display out line when user select role login
        binding.ivSignUpDoctor.setOnClickListener(this)
        binding.ivSignUpPatient.setOnClickListener(this)

        binding.btnSignUp.setOnClickListener(this)
        binding.tvSignUpLoginBtn.setOnClickListener(this)

        // input validate
        binding.etSignUpFirstName.apply {
            setOnEditorActionListener { _, actionId, _ ->

                when(actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        if(text.toString().length <= 2){
                            error = "First name is not valid"
                        }else{
                            error = null
                        }

                    }
                }
                false
            }
        }

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivSignUpDoctor -> {
                binding.llSignUpDoctor.setBackgroundResource(R.drawable.img_high_light)
                binding.llSignUpPatient.setBackgroundColor(Color.TRANSPARENT)
                binding.etSignUpDoctorCode.visibility = View.VISIBLE
                binding.tilSignUpDoctorCode.visibility = View.VISIBLE
            }

            R.id.ivSignUpPatient -> {
                binding.llSignUpPatient.setBackgroundResource(R.drawable.img_high_light)
                binding.llSignUpDoctor.setBackgroundColor(Color.TRANSPARENT)
                binding.etSignUpDoctorCode.visibility = View.GONE
                binding.tilSignUpDoctorCode.visibility = View.GONE
            }

            R.id.btnSignUp -> {
                when {
                    else -> {
                        navigateToPage(R.id.action_registerFragment_to_loginFragment)
                    }
                }
            }
            R.id.tvSignUpLoginBtn -> {
                navigateToPage(R.id.action_registerFragment_to_loginFragment)
            }
        }
    }




}