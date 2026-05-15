package com.example.multiviewapp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavoritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoritesFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private var favoritesList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listView = view.findViewById(R.id.listFavorites)

        loadFavorites()

        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, favoritesList)
        listView.adapter = adapter

        // 📋 Tap to copy
        listView.setOnItemClickListener { _, _, position, _ ->
            val text = favoritesList[position]
            val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Favorite Text", text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(requireContext(), "Copied!", Toast.LENGTH_SHORT).show()
        }

        // ❌ Long press to delete
        listView.setOnItemLongClickListener { _, _, position, _ ->
            val removed = favoritesList[position]
            favoritesList.removeAt(position)
            adapter.notifyDataSetChanged()
            saveFavorites()
            Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
            true
        }

        // ⬅️ Back Button Logic
        val btnBack = view.findViewById<Button>(R.id.btnGoBack)
        btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment())
                .commit()
        }
    }

    private fun loadFavorites() {
        val prefs = requireActivity()
            .getSharedPreferences("TransformerApp", AppCompatActivity.MODE_PRIVATE)

        val set = prefs.getStringSet("favorites", setOf()) ?: setOf()
        favoritesList = set.toMutableList()
    }

    private fun saveFavorites() {
        val prefs = requireActivity()
            .getSharedPreferences("TransformerApp", AppCompatActivity.MODE_PRIVATE)

        prefs.edit().putStringSet("favorites", favoritesList.toSet()).apply()
    }
}