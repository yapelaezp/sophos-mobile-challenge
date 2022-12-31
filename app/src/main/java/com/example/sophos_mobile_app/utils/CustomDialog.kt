package com.example.sophos_mobile_app.utils

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class CustomDialog(
    private val title: String,
    private val message: String,
    private val onPositiveButtonClick: () -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(android.R.string.ok)) { _, _ ->
                onPositiveButtonClick()
            }
            .create()


    companion object {
        const val TAG = "PurchaseConfirmationDialog"
    }
}

