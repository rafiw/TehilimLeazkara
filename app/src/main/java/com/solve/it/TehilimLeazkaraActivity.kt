package com.solve.it

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.media.AudioManager
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannedString
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.preference.PreferenceManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import androidx.core.text.buildSpannedString
import com.google.android.material.textfield.TextInputLayout


class TehilimLeazkaraActivity : AppCompatActivity() {
    private var isMan: Boolean = true

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
        this.isMan = true
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
            isMan = !isMan
            toggleGender.text = if (isMan) getString(R.string.son) else getString(R.string.girl)
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
                    append(chainText(resources.getString(item[0])))
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
                        bold {
                            append(resources.getString(item[2]))
                            append(" ")
                            append(resources.getString(item[1]))
                            append(" ")
                            append(resources.getString(item[2]))
                        }
                    }
                    append(" ")
                    append(chainText(resources.getString(item[3]))) } }
            .let {
                sendTextToView(SpannableString(it))
            }
    }

    private fun <T> Boolean.select(ifTrue: T, ifFalse: T): T = if (this) ifTrue else ifFalse

    private fun handleTfilaLeiluyButtonClick() {
        if (inputName.text.isBlank() || inputParentName.text.isBlank()) {
            Toast.makeText(this, R.string.error_missing_nams, Toast.LENGTH_LONG).show()
        }
        val wData = Triple(
            resources.getString(R.string.girl),
            resources.getString(R.string.ashkava_w_anon),
            resources.getString(R.string.tfilat_leiluy_w))
        val mData = Triple(
            resources.getString(R.string.son),
            resources.getString(R.string.ashkava_m_anon),
            resources.getString(R.string.tfilat_leiluy_m))

        toggleGender
            .isChecked
            .select(wData, mData).let {
                buildSpannedString {
                    append(chainText(resources.getString(R.string.tfila_generic_start)))
                    append(" ")
                    bold {
                        append(inputName.text.ifEmpty {
                            it.second
                        }.toString())
                        append(" ")
                        append(it.first)
                        append(" ")
                        append(inputParentName.text.ifEmpty {
                            it.second
                        }.toString())
                    }
                    append(" ")
                    append(chainText(it.third)) } }
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

        textInputLayout.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty()) {
                    textInputLayout.error = null
                    textInputLayout.boxStrokeErrorColor = ColorStateList.valueOf(
                        ContextCompat.getColor(textInputLayout.context, R.color.primary)
                    )
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
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
                sendTextToView(chainText(resources.getString(it)))
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
            sendTextToView(chainText(getString(R.string.elMaleGeneric)))
            return
        }

        val elMaleText = buildSpannedString {
            append(chainText(getString(R.string.elMaleBeginGeneric)))
            bold {
                append(chainText(" ${inputName.text} "))
                append(getString(toggleGender.isChecked.select(R.string.girl, R.string.son)))
                append(chainText(" ${inputParentName.text} "))
            }
            append(getString(toggleGender.isChecked.select(R.string.elMaleWoman, R.string.elMaleMan)));
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
    private fun chainText(text: String): String {
        return updateNikud(removeElohim(removeGodName(text)));
    }

    private fun updateNikud(text: String): String {
        val nikud = getString(R.string.nikud).toRegex()

        return settingsManager
            .isNikudEnabled
            .select(text, text.replace(nikud, ""))
    }

    private fun removeGodName(text: String): String {
        val replacements = mapOf(
            "אֲדֹנָי" to "אֲדֹנָ-י",
            "יְהוָה" to "יְהוָ-ה",
        )
        val replacedText = replacements.entries.fold(text) { acc, (old, new) ->
            acc.replace(old, new)
        }

        return settingsManager.showGodName.select(text, replacedText)
    }

    private fun removeElohim(text: String): String {
        val replacements = mapOf(
            "אֶלֹהִים" to "אֶ-לֹהִים",
            "אֱלֹהִים" to "אֱ-לֹהִים",
            "אֱֶלֹהִים" to "אֱֶ-לֹהִים",
            "אֱלֹהֵינוּ" to "אֱֶ-לֹהֵינוּ",
            "אֱלֹהַי" to "אֱ-לֹהַי",
            "אֱלֹהֵי" to "אֱ-לֹהֵי",
            "אלֹהַי" to "א-לֹהַי",
            "אֱלָהָא" to "אֱ-לָהָא",
            "אֱלֹהָיו" to "אֱ-לֹהָיו"

        )
        val replacedText = replacements.entries.fold(text) { acc, (old, new) ->
            acc.replace(old, new)
        }

        return settingsManager.isElohim.select(text, replacedText)
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
                    append(chainText(kyt[index!!]))
                    append("\n\n")
                }
            }
    }

    private fun getMishnayot(name: String): List<SpannedString> {
        val mishnayotSource = resources.getStringArray(R.array.mishnayot_source)
        val mishnayot = resources.getStringArray(R.array.mishnayot)

        fun buildAfterLimud(): SpannedString {
            // name cannot be blank
            val lastName = inputParentName.text.toString().ifBlank {
                toggleGender.isChecked.select(
                    resources.getString(R.string.ashkava_w_anon),
                    resources.getString(R.string.ashkava_m_anon)
                )
            }.trim()

            fun buildDesName() = buildSpannedString {
                bold { append("$name ${toggleGender.text} $lastName ") }
            }

            val (prey2, opt2Suffix) = toggleGender.isChecked.select(
                resources.getString(R.string.after_limud_prey2_woman) to resources.getString(R.string.after_limud_opt2_woman),
                resources.getString(R.string.after_limud_prey2_man) to resources.getString(R.string.after_limud_opt2_man)
            )

            return buildSpannedString {
                bold { append(resources.getString(R.string.after_limud_intro)) }
                append("\n")
                append(resources.getString(R.string.after_limud_prey1))
                append(" ")
                append(buildDesName())
                append(prey2)
                append("\n\n")
                bold { append(resources.getString(R.string.second_opt)) }
                append("\n")
                append(resources.getString(R.string.after_limud_opt2_prey1))
                append(buildDesName())
                append(opt2Suffix)
            }
        }

        val mishnayotToLearn: List<SpannedString> = name.map { it to alefBet[it] }
            .filter { it.second != null }
            .map { (letter, idx) ->
                val index = idx!!
                buildSpannedString {
                    bold {
                        append(letter.toString())
                        append("\n")
                        append(mishnayotSource[index])
                    }
                    append("\n")
                    append(chainText(mishnayot[index]))
                    append("\n\n")
                }
            }

        return mishnayotToLearn + buildAfterLimud()
    }

    private fun setTehilimArray(): List<SpannedString>  {
        val alefBet = resources.getStringArray(R.array.alef_bet)
        return listOf(
            Triple(11,  2, R.string.tehilimLg),
            Triple( 8,  6, R.string.tehilimTz),
            Triple( 9,  6, R.string.tehilimYz),
            Triple(15,  1, R.string.tehilimAb),
            Triple(17,  0, R.string.tehilimTza),
            Triple(18,  3, R.string.tehilimKd),
            Triple(18, 11, R.string.tehilimKl))
            .map { (frst, scnd, res) ->
                buildSpannedString {
                    bold {
                        append(getString(R.string.perek))
                        append(" ")
                        append(alefBet[frst][0])
                        append("\"")
                        append(alefBet[scnd][0])
                        append("\n")
                    }
                    append(chainText(getString(res)))
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
    val isElohim: Boolean
        get() {
            val key = context.getString(R.string.pref_elohim)
            return prefs.getBoolean(key, true).also {
                Log.d("SettingsManager", "Getting isElohim: $it")
            }
        }
    val showGodName: Boolean
        get() {
            val key = context.getString(R.string.pref_is_god_name)
            return prefs.getBoolean(key, true).also {
                Log.d("SettingsManager", "Getting showGodName: $it")
            }
        }

}
