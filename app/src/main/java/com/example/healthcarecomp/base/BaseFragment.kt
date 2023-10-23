package com.example.healthcarecomp.base

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.healthcarecomp.R


open class BaseFragment(view: Int) : Fragment(view) {

    protected fun navigateToPage(actionId: Int) {
        findNavController().navigate(actionId)
    }

    protected fun showLoading(isShow: Boolean) {
        val activity = requireActivity()
        if (activity is BaseActivity) {
            activity.showLoading(isShow)
        }
    }

    protected fun showErrorMessage(messageId: Int) {
        val message = requireContext().getString(messageId)
        showErrorMessage(message)
    }

    protected fun showErrorMessage(message: String) {
        val activity = requireActivity()
        if (activity is BaseActivity) {
            activity.showErrorDialog(message)
        }
    }

    protected fun showNotify(title: String?, message: String) {
        val activity = requireActivity()
        if (activity is BaseActivity) {
            activity.showNotifyDialog(title ?: getDefaultNotifyTitle(), message)
        }
    }

    private fun getDefaultNotifyTitle(): String {
        return getString(R.string.default_notify_title)
    }

    protected fun showNotify(titleId: Int = R.string.default_notify_title, messageId: Int) {
        val activity = requireActivity()
        if (activity is BaseActivity) {
            activity.showNotifyDialog(titleId, messageId)
        }
    }

    protected fun showLoadingMore(isShow: Boolean) {

    }


}