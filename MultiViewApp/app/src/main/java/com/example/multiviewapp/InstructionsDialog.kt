package com.example.multiviewapp

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class InstructionDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("How to Use")
            .setMessage(
                "Enter text and choose a transformation:\n\n" +
                        "Reverse → flips text\n" +
                        "Classy → Classic Cursive letters\n" +
                        "Morse → Replaces words with dots and dash\n" +
                        "Cyber_punk → Glitch like text\n" +
                        "\n" +
                        "The tabs below navigates the Home, Favorites, and Styles\n" +
                        "\n" +
                        "In the Favorite tab, you will see all text " +
                        "transformation in a list.\n" +
                        "You can copy or delete the content of the list."
            )
            .setPositiveButton("OK", null)
            .create()
    }
}