package com.solve.it

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.media.AudioManager
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannedString
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import androidx.preference.PreferenceManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import androidx.core.text.buildSpannedString
import com.google.android.material.textfield.TextInputLayout


class TehilimLeazkaraActivity : AppCompatActivity() {
    private var isSon: Boolean = true

    // UI Components
    private lateinit var buttonThilim: MaterialButton
    private lateinit var buttonMishnayot: MaterialButton
    private lateinit var buttonElMale: MaterialButton
    private lateinit var buttonKadishY: MaterialButton
    private lateinit var buttonKadishD: MaterialButton
    private lateinit var buttonEntrance: MaterialButton
    private lateinit var buttonAshkava: MaterialButton
    private lateinit var buttonTfilaLeiluy: MaterialButton
    private lateinit var inputName: EditText
    private lateinit var nameLayout: TextInputLayout
    private lateinit var inputParentName: EditText
    private lateinit var parentNameLayout: TextInputLayout
    private lateinit var toggleGender: MaterialButton
    private lateinit var textViewNusach: TextView
    private lateinit var toolbar: MaterialToolbar

    // Settings
    private val settingsManager by lazy {
        SettingsManager(applicationContext)
    }

    private val alefBet by lazy {
        resources
            .getStringArray(R.array.alef_bet)
            .mapIndexed { index, letters ->
                letters.map { it to index } }
            .flatten()
            .toMap()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        initializeViews()
        setupClickListeners()
        updateNusachDisplay()
        checkIfPhoneIsSilent()
    }

    private fun checkIfPhoneIsSilent() {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        if (audioManager.ringerMode == AudioManager.RINGER_MODE_NORMAL) {
            Toast.makeText(this,
                R.string.quiet,
                Toast.LENGTH_LONG
            ).show()
        }
    }
    private fun initializeViews() {
        // Initialize all view references
        this.isSon = true
        buttonAshkava = findViewById(R.id.buttonAshkava)
        buttonElMale = findViewById(R.id.buttonElMale)
        buttonEntrance = findViewById(R.id.buttonEntrance)
        buttonKadishD = findViewById(R.id.buttonKadishD)
        buttonKadishY = findViewById(R.id.buttonKadishY)
        buttonTfilaLeiluy = findViewById(R.id.buttonTfilaLeiluy)
        buttonThilim = findViewById(R.id.buttonTehilim)
        buttonMishnayot = findViewById(R.id.buttonMishnayot)
        inputName = findViewById(R.id.name)
        nameLayout = findViewById(R.id.name_layout)
        inputParentName = findViewById(R.id.par_name)
        parentNameLayout = findViewById(R.id.par_name_layout)
        textViewNusach = findViewById(R.id.configured_nusach)
        toggleGender = findViewById(R.id.son_daughter)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    private fun setupClickListeners() {
        buttonThilim.setOnClickListener {
            handleButtonClickTehilim()
        }

        buttonMishnayot.setOnClickListener {
            handleButtonClickMishnayot()
        }
        buttonElMale.setOnClickListener {
            handleButtonClick_ElMale()
        }
        toggleGender.setOnClickListener {
            isSon = !isSon
            toggleGender.text = if (isSon) getString(R.string.son) else getString(R.string.girl)
        }
        buttonEntrance.setOnClickListener {
            handleEntranceButtonClick()
        }

        buttonAshkava.setOnClickListener {
            handleAshkavaButtonClick()
        }

        buttonKadishY.setOnClickListener {
            handleButtonClick_KadishY()
        }

        buttonTfilaLeiluy.setOnClickListener {
            handleTfilaLeiluyButtonClick()
        }

        buttonKadishD.setOnClickListener {
            handleButtonClick_KadishD()
        }
    }

    private fun handleEntranceButtonClick() {
        handleButtonClick_EnranceD()
    }

    private fun handleAshkavaButtonClick() {
        if (inputName.text.isBlank() || inputParentName.text.isBlank()) {
            Toast.makeText(this, R.string.error_missing_nams, Toast.LENGTH_LONG).show()
        }

        val wData = arrayOf(
            R.string.ashkava_w1,
            R.string.girl,
            R.string.ashkava_w_anon,
            R.string.ashkava_w2)
        val mData = arrayOf(
            R.string.ashkava_m1,
            R.string.son,
            R.string.ashkava_m_anon,
            R.string.ashkava_m2)
        toggleGender
            .isChecked
            .select(wData, mData)
            .let { item ->
                buildSpannedString {
                    append(updateNikud(resources.getString(item[0])))
                    append(" ")
                    if (inputName.text.isNotEmpty()) {
                        bold {
                            append(inputName.text)
                            append(" ")
                            append(resources.getString(item[1]))
                            append(" ")
                            append(inputParentName.text)
                        }
                    } else {
                        append(resources.getString(item[2]))
                    }
                    append(" ")
                    append(updateNikud(resources.getString(item[3]))) } }
            .let {
                sendTextToView(SpannableString(it))
            }
    }

    private fun <T> Boolean.select(ifTrue: T, ifFalse: T): T = if (this) ifTrue else ifFalse

    private fun handleTfilaLeiluyButtonClick() {
        if (inputName.text.isBlank() || inputParentName.text.isBlank()) {
            Toast.makeText(this, R.string.error_missing_nams, Toast.LENGTH_LONG).show()
        }
        val wData = Pair(R.string.girl, R.string.tfilat_leiluy_w)
        val mData = Pair(R.string.son, R.string.tfilat_leiluy_m)

        toggleGender
            .isChecked
            .select(wData, mData).let {
                buildSpannedString {
                    append(updateNikud(resources.getString(R.string.tfila_generic_start)))
                    append(" ")
                    bold {
                        append(inputName.text.toString())
                        append(" ")
                        append(resources.getString(it.first))
                        append(" ")
                        append(inputParentName.text.toString())
                    }
                    append(" ")
                    append(updateNikud(resources.getString(it.second))) } }
            .let {
                sendTextToView(SpannableString(it))
            }
    }

    private fun showAboutDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.about_title)
            .setMessage(R.string.credit)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showErrorWithBlink(textInputLayout: TextInputLayout) {
        textInputLayout.error = "חסר ערך"

        val errorAnimation = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 200 // Duration for each blink
            repeatCount = 2 // Number of blinks (2 means blink 3 times)
            repeatMode = ValueAnimator.REVERSE

            addUpdateListener { animator ->
                val alpha = animator.animatedValue as Float
                textInputLayout.boxStrokeErrorColor = ColorStateList.valueOf(
                    Color.argb(
                        (255 * alpha).toInt(),
                        255, // Red
                        0,   // Green
                        0    // Blue
                    )
                )
            }
        }

        errorAnimation.start()
    }

    private fun handleButtonClickMishnayot() {
        if (inputName.text.isBlank()) {
            Toast.makeText(this, R.string.error_missing_name, Toast.LENGTH_LONG).show()
            showErrorWithBlink(nameLayout)
            return
        }
        buildSpannedString {
            getMishnayot(inputName.text.toString().trim())
                .forEach { append(it) } }
            .let {
                sendTextToView(SpannableString(it))
            }
    }

    private fun handleButtonClickTehilim() {
        val suffix = arrayOf(
            getString(R.string.neshama),
            getString(R.string.kra_satan),
            getString(R.string.neshama),
            getString(R.string.neshama),
            getString(R.string.neshama),
        )
        val nameToProcess = "${inputName.text.toString().trim()}${suffix[settingsManager.nusach.toInt()]}"

        if (inputName.text.isBlank() || inputParentName.text.isBlank()) {
            Toast.makeText(this, R.string.error_missing_nams, Toast.LENGTH_LONG).show()
        }
        buildSpannedString {
            setTehilimArray()
                .forEach { append(it) }
            getTehilim(nameToProcess)
                .forEach { append(it) } }
            .let {
                sendTextToView(SpannableString(it))
            }
    }

    private fun handleButtonClick_Kadish(kadish: Array<Int>) {
        kadish
            .getOrElse(settingsManager.nusach.toInt()) {
                kadish[0] }
            .let {
                sendTextToView(updateNikud(resources.getString(it)))
            }
    }

    private fun handleButtonClick_KadishY() {
        handleButtonClick_Kadish(
            arrayOf(
                R.string.kadishY_Ashkenaz,
                R.string.kadishY_Edot,
                R.string.kadishY_Sfard,
                R.string.kadishY_Edot,
                R.string.kadishY_Teiman,
            )
        )
    }

    private fun handleButtonClick_EnranceD() {
        handleButtonClick_Kadish(
            arrayOf(
                R.string.enterAshkenaz,
                R.string.enterSfardi
            )
        )
    }

    private fun handleButtonClick_KadishD() {
        handleButtonClick_Kadish(
            arrayOf(
                R.string.kadishD_Ashkenaz,
                R.string.kadishD_Edot,
                R.string.kadishD_Sfard,
                R.string.kadishD_Edot,
                R.string.kadishD_Teiman,
            )
        )
    }

    private fun handleButtonClick_ElMale() {
        if (inputName.text.isBlank() || inputParentName.text.isBlank()) {
            Toast.makeText(this, R.string.error_missing_nams, Toast.LENGTH_LONG).show()
            sendTextToView(updateNikud(getString(R.string.elMaleGeneric)))
            return
        }

        val elMaleText = buildSpannedString {
            append(updateNikud(getString(R.string.elMaleBeginGeneric)))
            append(updateNikud(" ${inputName.text} "))
            append(if (toggleGender.isChecked) getString(R.string.girl) else getString(R.string.son))
            append(updateNikud(" ${inputParentName.text} "))
            append(if (toggleGender.isChecked) getString(R.string.elMaleWoman) else getString(R.string.elMaleMan))
        }

        sendTextToView(SpannableString(elMaleText))
    }

    private fun sendTextToView(text: String) {
        sendTextToView(SpannableString(text))
    }

    private fun sendTextToView(text: Spannable) {
        val intent = Intent(this, ViewTehilim::class.java).apply {
            putExtra(Constants.FONT_TYPE, settingsManager.fontFamily)
            putExtra(Constants.TEXT_BLACK, settingsManager.isBlackText)
            Log.i("RAFI", "settingsManager.isNikudEnabled" + settingsManager.isNikudEnabled)

            putExtra(Constants.TEXT_KEY, text)
        }
        startActivity(intent)
    }

    private fun updateNusachDisplay() {
        settingsManager.nusach.toInt().let {
            textViewNusach.text = resources.getString(R.string.def_nusach,
                resources.getStringArray(R.array.nusachlistOptions)[it])

        }
    }

    override fun onResume() {
        super.onResume()
        updateNusachDisplay()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.first_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val actions = mapOf(
            R.id._settings  to { startActivity(Intent(this, SettingsActivity::class.java)) },
            R.id._about     to { showAboutDialog() }
        )

        return actions[item.itemId]
            ?.let {
                it.invoke()
                true
            }
            ?: run {
                super.onOptionsItemSelected(item)
            }
    }

    private fun updateNikud(text: String): String {
        val nikud = getString(R.string.nikud).toRegex()

        return settingsManager
            .isNikudEnabled
            .select(text, text.replace(nikud, ""))
    }

    private fun getTehilim(name: String): List<SpannedString> {
        val kyt = resources.getStringArray(R.array.kytn)

        return name.map { it to alefBet[it] }
            .filter { it.second != null }
            .map { (letter, index) ->
                buildSpannedString {
                    bold {
                        append(letter.toString())
                    }
                    append("\n")
                    append(updateNikud(kyt[index!!]))
                    append("\n\n")
                }
            }
    }

    private fun getMishnayot(name: String): List<SpannedString> {
        val mishnayotSource = resources.getStringArray(R.array.mishnayot_source)
        val mishnayot = resources.getStringArray(R.array.mishnayot)

        return name.map { it to alefBet[it] }
            .filter { it.second != null }
            .map { (letter, index) ->
                buildSpannedString {
                    bold {
                        append(letter.toString())
                        append("\n")
                        append(mishnayotSource[index!!].toString())
                    }
                    append("\n")
                    append(updateNikud(mishnayot[index!!]))
                    append("\n\n")
                }
            }
    }

    private fun setTehilimArray(): List<SpannedString>  {
        val alefBet = resources.getStringArray(R.array.alef_bet)
        return listOf(
            Triple(11,  2, R.string.tehilimLg),
            Triple( 8,  6, R.string.tehilimTz),
            Triple( 9,  6, R.string.tehilimYz),
            Triple(15,  1, R.string.tehilimAb),
            Triple(22,  0, R.string.tehilimTza),
            Triple(18,  3, R.string.tehilimKd),
            Triple(18, 11, R.string.tehilimKl))
            .map { (frst, scnd, res) ->
                buildSpannedString {
                    bold {
                        append(getString(R.string.perek))
                        append(" ")
                        append(alefBet[frst])
                        append("\"")
                        append(alefBet[scnd])
                        append("\n")
                    }
                    append(updateNikud(getString(res)))
                    append("\n\n")
                }
            }
    }
}


// Helper class for managing settings
class SettingsManager(private val context: Context) {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    val nusach: String
        get() {
            val key = context.getString(R.string.pref_list_key)
            return prefs.getString(key, "0") ?: "0"
        }

    val fontFamily: String
        get() {
            return (prefs.getString(context.getString(R.string.pref_font_list), "0") ?: "0").also {
                Log.d("SettingsManager", "Getting fontFamily: $it")
            }
        }

    val isNikudEnabled: Boolean
        get() {
            val key = context.getString(R.string.pref_is_font_enabled)
            return prefs.getBoolean(key, true).also {
                Log.d("SettingsManager", "Getting isNikudEnabled with key $key: $it")
            }
        }

    val isBlackText: Boolean
        get() {
            val key = context.getString(R.string.pref_black_text)
            return prefs.getBoolean(key, true).also {
                Log.d("SettingsManager", "Getting isBlackText: $it")
            }
        }
}
