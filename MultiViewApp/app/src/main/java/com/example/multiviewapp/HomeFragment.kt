package com.example.multiviewapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(R.layout.fragment_home) { // Pass layout to constructor

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val input = view.findViewById<EditText>(R.id.inputText)
        val buttons = mapOf(
            R.id.btnReverse to "reverse",
            R.id.btnClassy to "script",
            R.id.btnMorse to "morse",
            R.id.btnCyberpunk to "cyberpunk"
        )

        for ((id, type) in buttons) {
            view.findViewById<View>(id).setOnClickListener { btn ->
                val text = input.text.toString()

                // Call animation from Activity
                (activity as? MainActivity)?.animateButton(btn) {
                    val intent = Intent(requireContext(), ResultActivity::class.java)
                    intent.putExtra("TEXT", text)
                    intent.putExtra("TYPE", type)
                    startActivity(intent)
                }
            }
        }
        val btnInstructions = view.findViewById<ImageButton>(R.id.btnInstructions)
        btnInstructions.setOnClickListener {
            InstructionDialog().show(parentFragmentManager, "dialog")
        }
    }
}