package com.example.healthcarecomp.base

import android.content.DialogInterface
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.healthcarecomp.R
import com.example.healthcarecomp.data.model.User


open class BaseFragment(view: Int) : Fragment(view) {

    protected fun navigateToPage(actionId: Int) {
        findNavController().navigate(actionId)
    }

    protected fun navigateToPage(actionId: Int, bundle: Bundle) {
        findNavController().navigate(actionId, bundle)
    }

    protected fun navigateToPage(direction: NavDirections) {
        findNavController().navigate(direction)
    }

    protected fun showLoading(
        title: String,
        message: String,
        cancelable: Boolean = false,
        cancelListener: ((DialogInterface) -> Unit) = {}
    ) {
        val activity = requireActivity()
        if (activity is BaseActivity) {
            activity.showLoading( title, message, cancelable, cancelListener)
        }
    }

    protected fun hideLoading(){
        val activity = requireActivity()
        if (activity is BaseActivity) {
            activity.hideLoading()
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