package com.example.singleviewapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class Secondpage : AppCompatActivity() {
    // Selection data
    private var selectedBase: String = ""
    private var selectedNoodle: String = ""
    val selectedToppings = mutableListOf<String>()

    // Navigation State
    enum class RamenStep { SOUP, NOODLES, TOPPINGS }
    private var currentStep = RamenStep.SOUP

    // UI Components
    private lateinit var ramenBowl: ImageView
    private lateinit var soupLayer: ImageView
    private lateinit var noodleLayer: ImageView
    private lateinit var chasuLayer: ImageView
    private lateinit var eggLayer: ImageView
    private lateinit var fishcakeLayer: ImageView
    private lateinit var noriLayer: ImageView
    private lateinit var mushroomLayer: ImageView
    private lateinit var optionButton1: ImageButton
    private lateinit var optionButton2: ImageButton
    private lateinit var optionButton3: ImageButton
    private lateinit var optionButton4: ImageButton
    private lateinit var optionButton5: ImageButton
    private lateinit var optionButton6: ImageButton
    private lateinit var optionButton7: ImageButton
    private lateinit var instructionText: TextView
    private lateinit var calculateButton: Button
    private lateinit var resetButton: ImageButton

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secondpage)

        // 1. Initialize Food Layers
        ramenBowl = findViewById(R.id.ramenBowl)
        soupLayer = findViewById(R.id.soupLayer)
        noodleLayer = findViewById(R.id.noodleLayer)
        chasuLayer = findViewById(R.id.chasuLayer)
        eggLayer = findViewById(R.id.eggLayer)
        fishcakeLayer = findViewById(R.id.fishcakeLayer)
        noriLayer = findViewById(R.id.noriLayer)
        mushroomLayer = findViewById(R.id.mushroomLayer)

        // 2. Hide layers initially
        soupLayer.visibility = View.INVISIBLE
        noodleLayer.visibility = View.INVISIBLE

        optionButton1 = findViewById(R.id.btnlight)
        optionButton2 = findViewById(R.id.btnheavy)
        optionButton3 = findViewById(R.id.btnchasu)
        optionButton4 = findViewById(R.id.btnegg)
        optionButton5 = findViewById(R.id.btnfishcake)
        optionButton6 = findViewById(R.id.btnnori)
        optionButton7 = findViewById(R.id.btnmushroom)

        instructionText = findViewById(R.id.instructionText)
        calculateButton = findViewById(R.id.calculateButton)

        // 4. Set Initial UI State
        updateSelectionUI()

        // 5. Initial Animation for the Bowl
        val slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down)
        ramenBowl.startAnimation(slideDown)

        // 6. Button Listeners
        optionButton1.setOnClickListener { handleSelection(1) }
        optionButton2.setOnClickListener { handleSelection(2) }
        optionButton3.setOnClickListener { handleSelection(3) }
        optionButton4.setOnClickListener { handleSelection(4) }
        optionButton5.setOnClickListener { handleSelection(5) }
        optionButton6.setOnClickListener { handleSelection(6) }
        optionButton7.setOnClickListener { handleSelection(7) }

        calculateButton = findViewById(R.id.calculateButton)

        // 7. Instruction Popup Logic
        findViewById<ImageButton>(R.id.instructButton).setOnClickListener {
            showPopup(it)
        }

        resetButton = findViewById(R.id.resetBtn)
        resetButton.setOnClickListener {
            resetApp()
        }
    }

    private fun resetApp() {
        selectedBase = ""
        selectedNoodle = ""
        selectedToppings.clear()

        val layers = listOf(
            soupLayer, noodleLayer, chasuLayer, eggLayer,
            fishcakeLayer, noriLayer, mushroomLayer
        )
        layers.forEach { it.visibility = View.INVISIBLE }

        currentStep = RamenStep.SOUP

        updateSelectionUI()

        Toast.makeText(this, "Bowl Cleared", Toast.LENGTH_SHORT).show()
    }

    private fun handleSelection(choice: Int) {
        when (currentStep) {
            RamenStep.SOUP -> {
                selectedBase = if (choice == 1) "Light" else "Heavy"
                val resId = if (choice == 1) R.drawable.lightsoup else R.drawable.heavysoup
                animateFadeIn(soupLayer, resId)
                currentStep = RamenStep.NOODLES
                updateSelectionUI()
            }
            RamenStep.NOODLES -> {
                selectedNoodle = if (choice == 1) "Thin" else "Thick"
                val resId = if (choice == 1) R.drawable.thinnoodles else R.drawable.thicknoodles
                animateFadeIn(noodleLayer, resId)
                currentStep = RamenStep.TOPPINGS
                updateSelectionUI()
            }

            RamenStep.TOPPINGS -> {
                val toppingName = when(choice) {
                    3 -> "Chashu"
                    4 -> "Egg"
                    5 -> "Fishcake"
                    6 -> "Nori"
                    7 -> "Mushroom"
                    else -> ""
                }
                if (toppingName.isNotEmpty() && !selectedToppings.contains(toppingName)) {
                    selectedToppings.add(toppingName)
                }
                // Determine which topping was clicked
                val toppingLayer = when(choice) {
                    3 -> chasuLayer
                    4 -> eggLayer
                    5 -> fishcakeLayer
                    6 -> noriLayer
                    7 -> mushroomLayer
                    else -> null
                }
                val toppingDrawable = when(choice) {
                    3 -> R.drawable.chasu
                    4 -> R.drawable.egg
                    5 -> R.drawable.fishcake
                    6 -> R.drawable.nori
                    7 -> R.drawable.mushroom
                    else -> null
                }
                if (toppingLayer != null && toppingDrawable != null) {
                    animateFadeIn(toppingLayer, toppingDrawable)
                }

                calculateButton.setOnClickListener {
                    if (selectedBase.isEmpty() || selectedNoodle.isEmpty()) {
                        Toast.makeText(this, "Finish your bowl first!", Toast.LENGTH_SHORT).show()
                    } else {
                        performCalculation()
                    }
                }
            }
        }
    }

    private fun animateFadeIn(imageView: ImageView, drawableRes: Int) {
        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        imageView.setImageResource(drawableRes)
        imageView.visibility = View.VISIBLE
        imageView.startAnimation(fadeIn)
    }

    private fun updateSelectionUI() {
        // Hide all buttons first, then show only what we need
        val buttons = listOf(optionButton1, optionButton2, optionButton3, optionButton4, optionButton5, optionButton6, optionButton7)
        buttons.forEach { it.visibility = View.GONE }
        when (currentStep) {
            RamenStep.SOUP -> {
                instructionText.text = "Select Your Soup Base"
                optionButton1.apply { visibility = View.VISIBLE; setImageResource(R.drawable.lightsoup) }
                optionButton2.apply { visibility = View.VISIBLE; setImageResource(R.drawable.heavysoup) }
                calculateButton.visibility = View.GONE
            }
            RamenStep.NOODLES -> {
                instructionText.text = "Pick Your Noodle Type"
                optionButton1.apply { visibility = View.VISIBLE; setImageResource(R.drawable.thinnoodles) }
                optionButton2.apply { visibility = View.VISIBLE; setImageResource(R.drawable.thicknoodles) }
            }
            RamenStep.TOPPINGS -> {
                instructionText.text = "Tap to Add Toppings"
                calculateButton.visibility = View.VISIBLE
                optionButton3.apply {
                    visibility = View.VISIBLE
                    setImageResource(R.drawable.chasu)
                }
                optionButton4.apply {
                    visibility = View.VISIBLE
                    setImageResource(R.drawable.egg)
                }
                optionButton5.apply {
                    visibility = View.VISIBLE
                    setImageResource(R.drawable.fishcake)
                }
                optionButton6.apply {
                    visibility = View.VISIBLE
                    setImageResource(R.drawable.nori)
                }
                optionButton7.apply {
                    visibility = View.VISIBLE
                    setImageResource(R.drawable.mushroom)
                }
            }
        }
    }

    private fun performCalculation() {
        val toppingCount = selectedToppings.size

        val (title, description) = when {
            // 1. The Sumo Shakedown: Heavy + Thick + Many Toppings
            selectedBase == "Heavy" && selectedNoodle == "Thick" && toppingCount >= 3 ->
                "The Sumo Shakedown" to "A massive bowl of pure power. Warning: Immediate nap required after consumption."

            // 2. The Zen Minimalist: Light + Thin + No/Low Toppings
            selectedBase == "Light" && selectedNoodle == "Thin" && toppingCount <= 1 ->
                "The Zen Minimalist" to "Clarity in a bowl. Simple, elegant, and focused. You are a person of refined taste."

            // 3. The Salaryman's Standard: Light + Thin + Some Toppings
            selectedBase == "Light" && selectedNoodle == "Thin" ->
                "The Salaryman's Standard" to "Reliable, efficient, and classic. The perfect fuel for a busy afternoon."

            // 4. The Texture Architect: Thick Noodles + Specific Toppings
            selectedNoodle == "Thick" && (selectedToppings.contains("Mushroom") || selectedToppings.contains("Nori")) ->
                "The Texture Architect" to "You value the chew. This bowl is a structural masterpiece of flavor and bite."

            // 5. The Gourmet Ghost: Heavy Base + Thin Noodles
            selectedBase == "Heavy" && selectedNoodle == "Thin" ->
                "The Gourmet Ghost" to "Complex and rich, yet the thin noodles keep it mysterious. A sophisticated choice."

            // 6. The Weekend Warrior: Everything else
            else ->
                "The Weekend Warrior" to "A bold, experimental mix! You don't follow the rules—you make your own culinary path."
        }
        showResultModal(title, description)
        println("Current Toppings: $selectedToppings")
    }

    private fun showResultModal(title: String, desc: String) {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.activity_resultpage, null)

        // 1. Set Soup and Noodles
        val modalSoup: ImageView = popupView.findViewById(R.id.resultSoupLayer)
        val modalNoodle: ImageView = popupView.findViewById(R.id.resultNoodleLayer)

        modalSoup.setImageResource(if (selectedBase == "Light") R.drawable.lightsoup else R.drawable.heavysoup)
        modalNoodle.setImageResource(if (selectedNoodle == "Thin") R.drawable.thinnoodles else R.drawable.thicknoodles)

        // 2. Loop through selected toppings and show them
        for (topping in selectedToppings) {
            val toppingViewId = when (topping) {
                "Chashu" -> R.id.resChasu
                "Nori" -> R.id.resNori
                "Egg" -> R.id.resEgg
                "Mushroom" -> R.id.resMushroom
                "Fishcake" -> R.id.resFishcake
                else -> null
            }

            toppingViewId?.let {
                popupView.findViewById<ImageView>(it).visibility = View.VISIBLE
            }
        }
        popupView.findViewById<TextView>(R.id.resultTitle).text = title
        popupView.findViewById<TextView>(R.id.resultDescription).text = desc

        val popupWindow = PopupWindow(
            popupView,
            (resources.displayMetrics.widthPixels * 0.9).toInt(), // 90% width
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popupWindow.showAtLocation(ramenBowl, Gravity.CENTER, 0, 0)

        // Handles the close button
        popupView.findViewById<Button>(R.id.closeResultBtn).setOnClickListener {
            popupWindow.dismiss()
            // Clears data for new bowl
            selectedBase = ""
            selectedNoodle = ""
            selectedToppings.clear()
            // Hide all layers in the main view
            val layers = listOf(soupLayer, noodleLayer, chasuLayer, eggLayer, fishcakeLayer, noriLayer, mushroomLayer)
            layers.forEach { it.visibility = View.INVISIBLE }
            // Reset selection UI
            currentStep = RamenStep.SOUP
            updateSelectionUI()
        }
    }


        private fun showPopup(anchor: View) {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.activity_instructionpage, null)
        val popupWindow = PopupWindow(
            popupView,
            (resources.displayMetrics.widthPixels * 0.85).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popupWindow.showAtLocation(anchor, Gravity.CENTER, 0, 0)

        popupView.findViewById<Button>(R.id.closeButton).setOnClickListener {
            popupWindow.dismiss()
        }
    }
}