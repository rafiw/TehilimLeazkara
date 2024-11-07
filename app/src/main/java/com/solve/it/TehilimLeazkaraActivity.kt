package com.solve.it

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
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
import androidx.navigation.Navigator


class TehilimLeazkaraActivity : AppCompatActivity() {
    private var isSon: Boolean = true

    // UI Components
    private lateinit var thilimButton: MaterialButton
    private lateinit var elMaleButton: MaterialButton
    private lateinit var kadishYatomButton: MaterialButton
    private lateinit var kadishDerabananButton: MaterialButton
    private lateinit var entranceButton: MaterialButton
    private lateinit var ashkavaButton: MaterialButton
    private lateinit var tfilaLeiluyButton: MaterialButton
    private lateinit var nameInput: EditText
    private lateinit var parentNameInput: EditText
    private lateinit var genderToggle: MaterialButton
    private lateinit var configuredNusachText: TextView
    private lateinit var toolbar: MaterialToolbar

    // Settings
    private val settingsManager by lazy {
        SettingsManager(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        initializeViews()
        setupClickListeners()
        updateNusachDisplay()
    }

    private fun initializeViews() {
        // Initialize all view references
        this.isSon = true
        thilimButton = findViewById(R.id.viewTehilim)
        nameInput = findViewById(R.id.name)
        parentNameInput = findViewById(R.id.par_name)
        ashkavaButton = findViewById(R.id.ashkava_button)
        elMaleButton = findViewById(R.id.elMaleRachamim)
        genderToggle = findViewById(R.id.son_daughter)
        kadishYatomButton = findViewById(R.id.kadishYatomButton)
        kadishDerabananButton = findViewById(R.id.kadishDerabananButton)
        tfilaLeiluyButton = findViewById(R.id.tfilaLeiluy)
        entranceButton = findViewById(R.id.entranceButton)
        configuredNusachText = findViewById(R.id.configed_nusach)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    private fun setupClickListeners() {
        thilimButton.setOnClickListener {
            handleTehilimButtonClick()
        }

        elMaleButton.setOnClickListener {
            handleElMaleButtonClick()
        }
        genderToggle.setOnClickListener {
            isSon = !isSon
            genderToggle.text = if (isSon) getString(R.string.son) else getString(R.string.girl)
        }
        entranceButton.setOnClickListener {
            handleEntranceButtonClick()
        }

        ashkavaButton.setOnClickListener {
            handleAshkavaButtonClick()
        }

        kadishYatomButton.setOnClickListener {
            handleKadishYatomButtonClick()
        }

        tfilaLeiluyButton.setOnClickListener {
            handleTfilaLeiluyButtonClick()
        }

        kadishDerabananButton.setOnClickListener {
            handleKadishDerabananButtonClick()
        }
    }

    private fun handleEntranceButtonClick() {
        val selection = 0; // RAFI fix get from settings
        val string: String = if (selection == 0) resources.getString(R.string.enterAshkenaz) else resources.getString(
                R.string.enterSfardi
            )

        sendTextToView(updateNikud(string))
    }

    private fun handleAshkavaButtonClick() {
        if (nameInput.text.isEmpty()) {
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
        genderToggle
            .isChecked
            .select(wData, mData)
            .let { item ->
                buildSpannedString {
                    append(updateNikud(resources.getString(item[0])))
                    append(" ")
                    if (nameInput.text.isNotEmpty()) {
                        bold {
                            append(nameInput.text)
                            append(" ")
                            append(resources.getString(item[1]))
                            append(" ")
                            append(parentNameInput.text)
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
        val wData = Pair(R.string.girl, R.string.tfilat_leiluy_w)
        val mData = Pair(R.string.son, R.string.tfilat_leiluy_m)

        genderToggle
            .isChecked
            .select(wData, mData).let {
                buildSpannedString {
                    append(updateNikud(resources.getString(R.string.tfila_generic_start)))
                    append(" ")
                    bold {
                        append(nameInput.text.toString())
                        append(" ")
                        append(resources.getString(it.first))
                        append(" ")
                        append(parentNameInput.text.toString())
                    }
                    append(" ")
                    append(updateNikud(resources.getString(it.second))) } }
            .let {
                sendTextToView(SpannableString(it))
            }
    }

    private fun showAboutDialog() {
        MaterialAlertDialogBuilder(this, R.style.MaterialDialog)
            .setTitle(R.string.about_title)
            .setMessage(R.string.credit)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    private fun handleTehilimButtonClick() {
        val nameToProcess = "${nameInput.text.toString().trim()}${getString(R.string.neshama)}"

        buildSpannedString {
            setArray()
                .forEach { append(it) }
            getTehilim(nameToProcess)
                .forEach { append(it) } }
            .let {
                sendTextToView(SpannableString(it))
            }
    }

    private fun handleKashishClick(kadish: Array<Int>) {
        kadish
            .getOrElse(settingsManager.nusach.toInt()) {
                kadish[0] }
            .let {
                sendTextToView(updateNikud(resources.getString(it)))
            }
    }

    private fun handleKadishYatomButtonClick() {
        handleKashishClick(
            arrayOf(
                R.string.kadishYatomAshkenaz,
                R.string.kadishYatomSfard,
                R.string.kadishYatomEdot,
                R.string.kadishyatomTeiman
            )
        )
    }

    private fun handleKadishDerabananButtonClick() {
        handleKashishClick(
            arrayOf(
                R.string.kadishDAshkenaz,
                R.string.kadishDSfard,
                R.string.kadishDEdot,
                R.string.kadishDTeiman
            )
        )
    }

    private fun handleElMaleButtonClick() {
        if (nameInput.text.isBlank() || parentNameInput.text.isBlank()) {
            Toast.makeText(this, R.string.error_missing_nams, Toast.LENGTH_LONG).show()
            sendTextToView(updateNikud(getString(R.string.elMaleGeneric)))
            return
        }

        val elMaleText = buildSpannedString {
            append(updateNikud(getString(R.string.elMaleBeginGeneric)))
            append(updateNikud(" ${nameInput.text} "))
            append(if (genderToggle.isChecked) getString(R.string.son) else getString(R.string.girl))
            append(updateNikud(" ${parentNameInput.text} "))
            append(if (genderToggle.isChecked) getString(R.string.elMaleMan) else getString(R.string.elMaleWoman))
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
            .select(text.replace(nikud, ""), text)
    }

    private fun getTehilim(name: String): List<SpannedString> {
        val kyt = resources.getStringArray(R.array.kytn)

        //  convert ABC into a Map<Char, Int>
        //  this can be done only once
        val alefBet = resources
            .getStringArray(R.array.alef_bet)
            .mapIndexed { index, letters ->
                letters.map { it to index } }
            .flatten()
            .toMap()

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
                }}
    }

    private fun setArray(): List<SpannedString>  {
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
            return (prefs.getString("pref_font_list", "0") ?: "0").also {
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
            return prefs.getBoolean("pref_black_text", true).also {
                Log.d("SettingsManager", "Getting isBlackText: $it")
            }
        }
}

//package com.solve.it
//
//import android.content.Intent
////import android.content.SharedPreferences
////import android.os.Bundle
////import android.view.Menu
////import android.view.MenuItem
//import android.view.View
//import android.widget.Button
//import android.widget.EditText
//import android.widget.TextView
//import android.widget.Toast
//import android.widget.ToggleButton
////import androidx.appcompat.app.AppCompatActivity
////import androidx.prefrence
//
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.WindowCompat
//import androidx.navigation.findNavController
//import androidx.navigation.ui.AppBarConfiguration
//import androidx.navigation.ui.navigateUp
//import androidx.navigation.ui.setupActionBarWithNavController
//import android.view.Menu
//import android.view.MenuItem
//import com.google.android.material.dialog.MaterialAlertDialogBuilder
//
//
//class TehilimLeazkaraActivity : AppCompatActivity() {
//    private var sb: StringBuilder? = null
//    private var kyt: Array<String> = arrayOf()
//    private var thilim: Button? = null
//    private var elMale: Button? = null
//    private var kadishY: Button? = null
//    private var kadishD: Button? = null
//    private var entButton: Button? = null
//    private var ashkava: Button? = null
//    private var tfilaLeiluy: Button? = null
////    private var prefs: SharedPreferences? = null
//    private var view: Intent? = null
//    private var name: EditText? = null
//    private var selection = 0
//    private var configuredNusach: TextView? = null
//    private var parName: EditText? = null
//    private var tg: ToggleButton? = null
////    private lateinit var binding: ActivityMainBinding
//
//    /**
//     * Called when the activity is first created.
//     */
//    public override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        supportFragmentManager
//            .beginTransaction()
//            .replace(R.id._settings, SettingsFragment())
//            .commit()
//
//        setContentView(R.layout.main)
//        setWidgets()
//    }
//
//    override fun onResume() {
//        super.onResume()
////        selection = prefs!!.getString(getString(R.string.pref_list_key), "0")!!.toInt()
//        configuredNusach!!.text = resources.getString(R.string.def_nusach) +
//                resources.getStringArray(R.array.listOptions)[selection]
//    }
//
//    private fun sendTextToView(text: String) {
//        //attach to bundle
//        //font
//        var str = text
//        val fontType = "Ariel"
////        val fontType = prefs!!.getString(resources.getString(R.string.pref_font_list), "0")
////        //fontSize
//        var fontSize = "26"
////        var fontSize = prefs!!.getString(
////            resources.getString(R.string.pref_font_size),
////            Constants.DEFAULT_FONT_SIZE
////        )
//        try {
//            if (fontSize!!.toInt() == 0 || fontSize.toInt() > 80 || fontSize.toInt() < 1) {
//                fontSize = Constants.DEFAULT_FONT_SIZE
//                Toast.makeText(
//                    this, R.string.error_small,
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//        } catch (e: NumberFormatException) {
//            Toast.makeText(
//                this, R.string.error_small,
//                Toast.LENGTH_LONG
//            ).show()
//        }
//        // colors
//        val textBlack = true//prefs!!.getBoolean(getString(R.string.pref_black_text), true)
//        // show nikud
//        val showNikud = true//prefs!!.getBoolean(getString(R.string.pref_is_font_enabled), true)
//        //fix right to left
//        val rightToLeft = true//prefs!!.getBoolean(getString(R.string.pref_right_to_left), true)
//        if (!showNikud) {
//            str = removeNikud(str)
//        }
//        view = Intent("com.solve.it.THILIM")
//        view!!.putExtra(Constants.FONT_TYPE, fontType)
//        view!!.putExtra(Constants.FONT_SIZE, fontSize)
//        view!!.putExtra(Constants.TEXT_BLACK, textBlack)
//        view!!.putExtra(Constants.RIGHT_TO_LEFT, rightToLeft)
//        //text to show
//        view!!.putExtra(Constants.TEXT_KEY, str)
//        //start view
//        startActivity(view)
//    }
//
//    private fun setArray() {
//        sb = StringBuilder(300)
//        val a_b = resources.getStringArray(R.array.alef_bet)
//        sb!!.append(
//            """${resources.getString(R.string.perek)} ${a_b[11]}"${a_b[2]}
//${resources.getString(R.string.tehilimLg)}
//
//"""
//        )
//        sb!!.append(
//            """${resources.getString(R.string.perek)} ${a_b[8]}"${a_b[6]}
//${resources.getString(R.string.tehilimTz)}
//
//"""
//        )
//        sb!!.append(
//            """${resources.getString(R.string.perek)} ${a_b[9]}"${a_b[6]}
//${resources.getString(R.string.tehilimYz)}
//
//"""
//        )
//        sb!!.append(
//            """${resources.getString(R.string.perek)} ${a_b[15]}"${a_b[1]}
//${resources.getString(R.string.tehilimAb)}
//
//"""
//        )
//        sb!!.append(
//            """${resources.getString(R.string.perek)} ${a_b[22]}"${a_b[0]}
//${resources.getString(R.string.tehilimTza)}
//
//"""
//        )
//        sb!!.append(
//            """${resources.getString(R.string.perek)} ${a_b[18]}"${a_b[3]}
//${resources.getString(R.string.tehilimKd)}
//
//"""
//        )
//        sb!!.append(
//            """${resources.getString(R.string.perek)} ${a_b[18]}"${a_b[11]}
//${resources.getString(R.string.tehilimKl)}
//
//"""
//        )
//    }
//
//    private fun removeNikud(sb2: String): String {
//        return sb2.replace(resources.getString(R.string.nikud).toRegex(), "")
//    }
//
//    private fun setWidgets() {
//        //for nusach
////        selection = prefs!!.getString(getString(R.string.pref_list_key), "0")!!.toInt()
//        thilim = findViewById<View>(R.id.okBbutton) as Button
//        name = findViewById<View>(R.id.name) as EditText
//        parName = findViewById<View>(R.id.par_name) as EditText
//        ashkava = findViewById<View>(R.id.ashkava_button) as Button
//        elMale = findViewById<View>(R.id.elMaleRachamim) as Button
//        tg = findViewById<View>(R.id.son_daughter) as ToggleButton
//        kadishY = findViewById<View>(R.id.kadishYatomButton) as Button
//        kadishD = findViewById<View>(R.id.kadishDerabananButton) as Button
//        tfilaLeiluy = findViewById<View>(R.id.tfilaLeiluy) as Button
//        thilim!!.setOnClickListener {
//            setArray()
//            val nameToProcess: String = name!!.text.toString().replace(" ", "") + resources.getString(R.string.neshama)
//
//            getTehilim(nameToProcess)
//            sendTextToView(sb.toString())
//            sb!!.delete(0, sb!!.length)
//        }
//        entButton = findViewById<View>(R.id.entranceButton) as Button
//        entButton!!.setOnClickListener {
//            val string: String = if (selection == 0) resources.getString(R.string.enterAshkenaz) else resources.getString(
//                    R.string.enterSfardi
//                )
//            sendTextToView(string)
//        }
//        elMale!!.setOnClickListener { v ->
//            if (name!!.text.isEmpty() || parName!!.text.isEmpty()) {
//                Toast.makeText(v.context, R.string.error_missing_nams, Toast.LENGTH_LONG).show()
//                sendTextToView(resources.getString(R.string.elMaleGeneric))
//            } else {
//                val el = StringBuilder()
//                el.append(resources.getString(R.string.elMaleBeginGeneric))
//                val str: String
//                if (tg!!.isChecked) {
//                    str = " " + name!!.text.toString() + " " +
//                            resources.getString(R.string.son) + " " +
//                            parName!!.text.toString() + " "
//                } else {
//                    str = " " + name!!.text.toString() + " " +
//                            resources.getString(R.string.girl) + " " +
//                            parName!!.text.toString() + " "
//                }
//                el.append(str)
//                if (tg!!.isChecked) el.append(resources.getString(R.string.elMaleMan)) else el.append(
//                    resources.getString(R.string.elMaleWoman)
//                )
//                sendTextToView(el.toString())
//            }
//        }
//        ashkava!!.setOnClickListener { v ->
//            val str: String
//            if (tg!!.isChecked) { // male
//                if (name!!.text.isNotEmpty()) {
//                    str = resources.getString(R.string.ashkava_m1) + " " + name!!.text + " בן " +
//                            parName!!.text + resources.getString(R.string.ashkava_m2)
//                } else {
//                    Toast.makeText(v.context, R.string.error_missing_nams, Toast.LENGTH_LONG).show()
//                    str = resources.getString(R.string.ashkava_m1) + " " +
//                            resources.getString(R.string.ashkava_m_anon) + " " + resources.getString(
//                        R.string.ashkava_m2
//                    )
//                }
//            } else { // female
//                if (name!!.text.isNotEmpty()) {
//                    str = resources.getString(R.string.ashkava_w1) + " " + name!!.text + " בת " +
//                            parName!!.text + " " + resources.getString(R.string.ashkava_w2)
//                } else {
//                    Toast.makeText(v.context, R.string.error_missing_nams, Toast.LENGTH_LONG).show()
//                    str = resources.getString(R.string.ashkava_w1) + " " +
//                            resources.getString(R.string.ashkava_w_anon) + " " + resources.getString(
//                        R.string.ashkava_w2
//                    )
//                }
//            }
//            sendTextToView(str)
//        }
//        kadishY!!.setOnClickListener {
//            val str: String
//            str = when (selection) {
//                1 -> resources.getString(R.string.kadishYatomSfard)
//                2 -> resources.getString(R.string.kadishYatomEdot)
//                3 -> resources.getString(R.string.kadishyatomTeiman)
//                0 -> resources.getString(R.string.kadishYatomAshkenaz)
//                else -> resources.getString(R.string.kadishYatomAshkenaz)
//            }
//            sendTextToView(str)
//        }
//        tfilaLeiluy!!.setOnClickListener {
//            val sb = StringBuilder(3050)
//            sb.append(resources.getString(R.string.tfila_generic_start))
//            sb.append(name!!.text.toString() + " ")
//            if (tg!!.isChecked) {
//                //son
//                sb.append(resources.getString(R.string.son) + " ")
//                sb.append(" " + parName!!.text.toString() + " ")
//                sb.append(resources.getString(R.string.tfilat_leiluy_m))
//            } else {
//                //girl
//                sb.append(resources.getString(R.string.girl) + " ")
//                sb.append(" " + parName!!.text.toString() + " ")
//                sb.append(resources.getString(R.string.tfilat_leiluy_w))
//            }
//            sendTextToView(sb.toString())
//        }
//        kadishD!!.setOnClickListener {
//            val str: String
//            str = when (selection) {
//                1 -> resources.getString(R.string.kadishDSfard)
//                2 -> resources.getString(R.string.kadishDEdot)
//                3 -> resources.getString(R.string.kadishDTeiman)
//                0 -> resources.getString(R.string.kadishDAshkenaz)
//                else -> resources.getString(R.string.kadishDAshkenaz)
//            }
//            sendTextToView(str)
//        }
//        configuredNusach = findViewById<View>(R.id.configed_nusach) as TextView
//        configuredNusach!!.text = resources.getString(R.string.def_nusach) +
//                resources.getStringArray(R.array.listOptions)[selection]
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.first_menu, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return super.onOptionsItemSelected(item)
////        return when (item.itemId) {
////            R.id.action_settings -> true
////            else -> super.onOptionsItemSelected(item)
////        }
//    }
//

////    override fun onOptionsItemSelected(item: MenuItem): Boolean {
////        val id = item.itemId
////        val intent: Intent = if (id == R.id._about) {
////            Intent("com.solve.it.ABOUT")
////        } else if (id == R.id._settings) {
////            Intent("com.solve.it.PREFS")
////        } else {
////            return super.onOptionsItemSelected(item)
////        }
////        startActivity(intent)
////        return true
////    }
//
//    private fun getTehilim(name: String) {
//        kyt = resources.getStringArray(R.array.kytn)
//        for (element in name) sb!!.append(getKyt(element))
//    }
//
//    private fun getKyt(letter: Char): String {
//    val a_b = resources.getStringArray(R.array.alef_bet)
//        for (i in a_b.indices) if (a_b[i].contains(letter)) return """
//     $letter
//     ${kyt[i]}
//
//
//     """.trimIndent()
//        return ""
//    }
//}