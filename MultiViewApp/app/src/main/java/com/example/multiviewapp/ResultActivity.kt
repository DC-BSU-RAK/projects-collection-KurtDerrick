package com.example.multiviewapp

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.animation.AnimationUtils // ADD THIS IMPORT
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.widget.ImageButton

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // 1. Get the data passed from MainActivity
        val text = intent.getStringExtra("TEXT") ?: ""
        val type = intent.getStringExtra("TYPE") ?: "upper"

        val resultView = findViewById<TextView>(R.id.resultText)
        val btnCopy = findViewById<Button>(R.id.btnCopy)

        // 2. Perform the transformation
        val result = transform(text, type)
        resultView.text = result

        // 3. Save to SharedPreferences
        val btnFav = findViewById<Button>(R.id.btnFavorite)

        btnFav.setOnClickListener {
            val prefs = getSharedPreferences("TransformerApp", MODE_PRIVATE)

            val set = prefs.getStringSet("favorites", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
            set.add(resultView.text.toString())

            prefs.edit().putStringSet("favorites", set).apply()

            Toast.makeText(this, "Added to Favorites!", Toast.LENGTH_SHORT).show()
        }
        // 4. Copy Text
        btnCopy.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Transformed Text", resultView.text)
            clipboard.setPrimaryClip(clip)

            Toast.makeText(this, "Copied to clipboard!", Toast.LENGTH_SHORT).show()
        }

        // 5. Apply Animation
        val animation = AnimationUtils.loadAnimation(this, R.anim.slide_in)
        resultView.startAnimation(animation)

        // 6. Back Button
        val btnBack = findViewById<ImageButton>(R.id.imageback)
        btnBack.setOnClickListener {
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    private fun transform(text: String, type: String): String {
        return when (type) {
            "reverse" -> text.reversed()
            "script" -> {
                buildString {
                    var i = 0
                    while (i < text.length) {
                        val cp = text.codePointAt(i)
                        if (Character.isLetter(cp)) {
                            // Script block exceptions
                            when (cp) {
                                'B'.code -> append("ℬ")
                                'E'.code -> append("ℰ")
                                'F'.code -> append("ℱ")
                                'H'.code -> append("ℋ")
                                'I'.code -> append("ℐ")
                                'L'.code -> append("ℒ")
                                'M'.code -> append("ℳ")
                                'R'.code -> append("ℛ")
                                'e'.code -> append("ℯ")
                                'g'.code -> append("ℊ")
                                'o'.code -> append("ℴ")
                                in 'A'.code..'Z'.code -> appendCodePoint(cp + 0x1D4D0 - 'A'.code)
                                in 'a'.code..'z'.code -> appendCodePoint(cp + 0x1D4EA - 'a'.code)
                                else -> appendCodePoint(cp)
                            }
                        } else {
                            appendCodePoint(cp)
                        }
                        i += Character.charCount(cp)
                    }
                }
            }
            "morse" -> {
                val morseAlphabet = mapOf(
                    'A' to ".-", 'B' to "-...", 'C' to "-.-.", 'D' to "-..", 'E' to ".",
                    'F' to "..-.", 'G' to "--.", 'H' to "....", 'I' to "..", 'J' to ".---",
                    'K' to "-.-", 'L' to ".-..", 'M' to "--", 'N' to "-.", 'O' to "---",
                    'P' to ".--.", 'Q' to "--.-", 'R' to ".-.", 'S' to "...", 'T' to "-",
                    'U' to "..-", 'V' to "...-", 'W' to ".--", 'X' to "-..-", 'Y' to "-.--", 'Z' to "--.."
                )
                text.uppercase().map { char ->
                    morseAlphabet[char] ?: if (char == ' ') "/" else char.toString()
                }.joinToString(" ")
            }
            "cyberpunk" -> {
                // A curated list of distinct Unicode combining overlay marks
                val glitchMarks = listOf(
                    "\u0332", // Combining Low Line (underline glitch)
                    "\u0333", // Combining Double Low Line
                    "\u0334", // Combining Tilde Overlay
                    "\u0335", // Combining Short Stroke Overlay
                    "\u0337", // Combining Short Solidus Overlay (slanted cuts)
                    "\u0338", // Combining Long Solidus Overlay
                    "\u0322", // Combining Retroflex Hook Below
                    "\u032a"  // Combining Bridge Below
                )
                buildString {
                    var i = 0
                    while (i < text.length) {
                        val cp = text.codePointAt(i)

                        if (Character.isLetter(cp)) {
                            // 1. Append the original base letter first
                            appendCodePoint(cp)
                            // 2. Intentionally inject exactly 2 random overlay marks directly onto it
                            // This creates a glitch/scratch effect built into the character cell itself
                            append(glitchMarks.random())
                            append(glitchMarks.random())
                        } else {
                            // Pass emojis, spaces, and numbers through completely clean
                            appendCodePoint(cp)
                        }
                        i += Character.charCount(cp)
                    }
                }
            }
            else -> text
        }
    }
}