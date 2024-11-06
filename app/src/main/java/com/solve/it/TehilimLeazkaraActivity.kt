package com.solve.it

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class TehilimLeazkaraActivity : AppCompatActivity() {
    private var sb: StringBuilder? = null
    private lateinit var kyt: Array<String>
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

        sendTextToView(string)
    }

    private fun handleAshkavaButtonClick() {
            val str: String
            if (genderToggle.isChecked) { // female
                if (nameInput.text.isNotEmpty()) {
                    str = resources.getString(R.string.ashkava_w1) + " " + nameInput.text + " בת " +
                            parentNameInput.text + " " + resources.getString(R.string.ashkava_w2)
                } else {
                    Toast.makeText(this, R.string.error_missing_nams, Toast.LENGTH_LONG).show()
                    str = resources.getString(R.string.ashkava_w1) + " " +
                            resources.getString(R.string.ashkava_w_anon) + " " + resources.getString(
                        R.string.ashkava_w2
                    )
                }
            } else { // male
                if (nameInput.text.isNotEmpty()) {
                    str = resources.getString(R.string.ashkava_m1) + " " + nameInput.text + " בן " +
                            parentNameInput.text + resources.getString(R.string.ashkava_m2)
                } else {
                    Toast.makeText(this, R.string.error_missing_nams, Toast.LENGTH_LONG).show()
                    str = resources.getString(R.string.ashkava_m1) + " " +
                            resources.getString(R.string.ashkava_m_anon) + " " + resources.getString(
                        R.string.ashkava_m2
                    )
                }
            }
            sendTextToView(str)
    }

    private fun handleTfilaLeiluyButtonClick() {
            val sb = StringBuilder(2048)
            sb.append(resources.getString(R.string.tfila_generic_start))
            sb.append(nameInput.text.toString() + " ")
            if (genderToggle.isChecked) {
                sb.append(resources.getString(R.string.girl) + " ")
                sb.append(" " + parentNameInput.text.toString() + " ")
                sb.append(resources.getString(R.string.tfilat_leiluy_w))
            } else {
                sb.append(resources.getString(R.string.son) + " ")
                sb.append(" " + parentNameInput.text.toString() + " ")
                sb.append(resources.getString(R.string.tfilat_leiluy_m))
            }
            sendTextToView(sb.toString())
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
        setArray()
        val nameToProcess = "${nameInput.text.toString().trim()}${getString(R.string.neshama)}"
        getTehilim(nameToProcess)
        sendTextToView(sb.toString())
        sb?.clear()
    }

    private fun handleKadishYatomButtonClick() {
        var selection = settingsManager.nusach.toInt()
        val str = when (selection) {
                1 -> resources.getString(R.string.kadishYatomSfard)
                2 -> resources.getString(R.string.kadishYatomEdot)
                3 -> resources.getString(R.string.kadishyatomTeiman)
                0 -> resources.getString(R.string.kadishYatomAshkenaz)
                else -> resources.getString(R.string.kadishYatomAshkenaz)
            }
            sendTextToView(str)
    }

    private fun handleKadishDerabananButtonClick() {
        var selection = settingsManager.nusach.toInt()
        val str = when (selection) {
                1 -> resources.getString(R.string.kadishDSfard)
                2 -> resources.getString(R.string.kadishDEdot)
                3 -> resources.getString(R.string.kadishDTeiman)
                0 -> resources.getString(R.string.kadishDAshkenaz)
                else -> resources.getString(R.string.kadishDAshkenaz)
        }
        sendTextToView(str)
    }

    private fun handleElMaleButtonClick() {
        if (nameInput.text.isBlank() || parentNameInput.text.isBlank()) {
            Toast.makeText(this, R.string.error_missing_nams, Toast.LENGTH_LONG).show()
            sendTextToView(getString(R.string.elMaleGeneric))
            return
        }

        val elMaleText = buildString {
            append(getString(R.string.elMaleBeginGeneric))
            append(" ${nameInput.text} ")
            append(if (genderToggle.isChecked) getString(R.string.son) else getString(R.string.girl))
            append(" ${parentNameInput.text} ")
            append(if (genderToggle.isChecked) getString(R.string.elMaleMan) else getString(R.string.elMaleWoman))
        }

        sendTextToView(elMaleText)
    }

    private fun sendTextToView(text: String) {
        val intent = Intent(this, ViewTehilim::class.java).apply {
            putExtra(Constants.FONT_TYPE, settingsManager.fontFamily)
            putExtra(Constants.TEXT_BLACK, settingsManager.isBlackText)
            Log.i("RAFI", "settingsManager.isNikudEnabled" + settingsManager.isNikudEnabled)

            putExtra(Constants.TEXT_KEY, if (settingsManager.isNikudEnabled) text else removeNikud(text))
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
        return when (item.itemId) {
            R.id._settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            R.id._about -> {
                showAboutDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun removeNikud(text: String): String {
        return text.replace(getString(R.string.nikud).toRegex(), "")
    }

    private fun getTehilim(name: String) {
        kyt = resources.getStringArray(R.array.kytn)
        name.forEach { char ->
            sb?.append(getKyt(char))
        }
    }

    private fun getKyt(letter: Char): String {
        val alefBet = resources.getStringArray(R.array.alef_bet)
        for (i in alefBet.indices) {
            if (alefBet[i].contains(letter)) {
                return """
                    $letter
                    ${kyt[i]}
                    
                    
                """.trimIndent()
            }
        }
        return ""
    }

    private fun setArray() {
        sb = StringBuilder(300)
        val alefBet = resources.getStringArray(R.array.alef_bet)

        val perakim = listOf(
            Triple(alefBet[11], alefBet[2], R.string.tehilimLg),
            Triple(alefBet[8], alefBet[6], R.string.tehilimTz),
            Triple(alefBet[9], alefBet[6], R.string.tehilimYz),
            Triple(alefBet[15], alefBet[1], R.string.tehilimAb),
            Triple(alefBet[22], alefBet[0], R.string.tehilimTza),
            Triple(alefBet[18], alefBet[3], R.string.tehilimKd),
            Triple(alefBet[18], alefBet[11], R.string.tehilimKl)
        )

        perakim.forEach { (first, second, stringRes) ->
            sb?.append("""
                ${getString(R.string.perek)} $first"$second
                ${getString(stringRes)}

            """.trimIndent())
        }
    }
}

// Helper class for managing settings
class SettingsManager(private val context: Context) {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    val nusach: String
        get() {
            val key = context.getString(R.string.pref_list_key)
            val value = prefs.getString(key, "0") ?: "0"
            return value
        }

    val fontFamily: String
        get() {
            val value = prefs.getString("pref_font_list", "0") ?: "0"
            Log.d("SettingsManager", "Getting fontFamily: $value")
            return value
        }

    val isNikudEnabled: Boolean
        get() {
            val key = context.getString(R.string.pref_is_font_enabled)
            val value = prefs.getBoolean(key, true)
            Log.d("SettingsManager", "Getting isNikudEnabled with key $key: $value")
            return value
        }

    val isBlackText: Boolean
        get() {
            val value = prefs.getBoolean("pref_black_text", true)
            Log.d("SettingsManager", "Getting isBlackText: $value")
            return value
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