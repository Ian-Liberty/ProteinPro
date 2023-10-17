package com.example.proteinpro.util.Popup

import android.content.Context
import androidx.appcompat.app.AlertDialog

class PopupInterface(private val context: Context) {

    fun showPopupMessage(title: String, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }


}