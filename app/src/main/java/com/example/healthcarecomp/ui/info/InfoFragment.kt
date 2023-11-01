package com.example.healthcarecomp.ui.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseFragment
import com.example.healthcarecomp.data.model.User
import com.example.healthcarecomp.databinding.FragmentInfoBinding
import com.example.healthcarecomp.ui.activity.main.MainActivity
import com.example.healthcarecomp.ui.activity.main.MainViewModel


class InfoFragment : BaseFragment(R.layout.fragment_info) {

    private lateinit var _binding: FragmentInfoBinding
    private lateinit var _viewModel: InfoViewModel
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentInfoBinding.inflate(inflater, container, false)

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = (requireActivity() as MainActivity).mainViewModel
        setUI()
        setEvents()
        setObservers()
    }

    private fun setUI(){

    }

    private fun setEvents(){
        _binding.fabEdit.setOnClickListener {
            while (_binding.)
        }
    }

    private fun setObservers(){
        mainViewModel.user.observe(viewLifecycleOwner, Observer {
            _binding.run{
                etInfoFirstName.setText(it?.firstName)
                etInfoLastName.setText(it?.lastName)
                etInfoEmail.setText(it?.email)
                etInfoPhoneNumber.setText(it?.phone)
                etInfoPassword.setText(it?.password)
                etInfoPasswordConfirm.setText(it?.password)
            }

        })
    }
}