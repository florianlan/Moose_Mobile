package com.florianlanz.touch_mouse.views

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.widget.SwitchCompat
import com.florianlanz.touch_mouse.R

class SettingsActivity : AppCompatActivity() {
    private lateinit var sp: SharedPreferences
    private lateinit var etPadTop: EditText
    private lateinit var etPadBot: EditText
    private lateinit var etPadLeft: EditText
    private lateinit var etPadRight: EditText
    private lateinit var etCols: EditText
    private lateinit var etRows: EditText
    private lateinit var swtShowSymbols: SwitchCompat
    private lateinit var swtShowLines: SwitchCompat
    private lateinit var swtShowFails: SwitchCompat


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        sp = this.applicationContext.getSharedPreferences("grid", Context.MODE_PRIVATE)
        etPadTop = findViewById(R.id.edit_top)
        etPadBot = findViewById(R.id.edit_bottom)
        etPadLeft = findViewById(R.id.edit_left)
        etPadRight = findViewById(R.id.edit_right)
        etCols = findViewById(R.id.edit_cols)
        etRows = findViewById(R.id.edit_rows)
        swtShowSymbols = findViewById(R.id.swt_showsym)
        swtShowLines = findViewById(R.id.swt_showlines)
        swtShowFails = findViewById(R.id.swt_showfails)

        //get valid settings from config
        etPadTop.setText(sp.getInt("pad_top", 50).toString())
        etPadBot.setText(sp.getInt("pad_bot", 1500).toString())
        etPadLeft.setText(sp.getInt("pad_left", 50).toString())
        etPadRight.setText(sp.getInt("pad_right", 50).toString())
        etCols.setText(sp.getInt("cols", 3).toString())
        etRows.setText(sp.getInt("rows", 2).toString())
        swtShowSymbols.isChecked = sp.getBoolean("show_sym", false)
        swtShowLines.isChecked = sp.getBoolean("show_lines", false)
        swtShowFails.isChecked = sp.getBoolean("show_fails", false)

    }

    fun saveSettings(view: View) {
        //save settings into config file
        val top = etPadTop.text.toString().toInt()
        val bot = etPadBot.text.toString().toInt()
        val left = etPadLeft.text.toString().toInt()
        val right = etPadRight.text.toString().toInt()
        val cols = etCols.text.toString().toInt()
        val rows = etRows.text.toString().toInt()
        val showSym = swtShowSymbols.isChecked
        val showLines = swtShowLines.isChecked
        val showFails = swtShowFails.isChecked

        val editor = sp.edit()
        editor.apply {
            putInt("pad_top", top)
            putInt("pad_bot", bot)
            putInt("pad_left", left)
            putInt("pad_right", right)
            putInt("cols", cols)
            putInt("rows", rows)
            putBoolean("show_sym", showSym)
            putBoolean("show_lines", showLines)
            putBoolean("show_fails", showFails)
            apply()
        }

        finish()

    }


}