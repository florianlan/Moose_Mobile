package com.florianlanz.touch_mouse.views

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Switch
import com.florianlanz.touch_mouse.R

class SettingsActivity : AppCompatActivity() {
    private lateinit var sp: SharedPreferences //shared preferences
    private lateinit var etPadTop: EditText
    private lateinit var etPadBot: EditText
    private lateinit var etPadLeft: EditText
    private lateinit var etPadRight: EditText
    private lateinit var etCols: EditText
    private lateinit var etRows: EditText
    private lateinit var swtShowSymbols: Switch

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

        //get valid settings from config

        etPadTop.setText(sp.getInt("pad_top", 0).toString())
        etPadBot.setText(sp.getInt("pad_bot", 100).toString())
        etPadLeft.setText(sp.getInt("pad_left", 0).toString())
        etPadRight.setText(sp.getInt("pad_right", 0).toString())
        etCols.setText(sp.getInt("cols", 3).toString())
        etRows.setText(sp.getInt("rows", 3).toString())
        swtShowSymbols.isChecked = sp.getBoolean("show_sym", false)

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

        val editor = sp.edit()
        editor.apply {
            putInt("pad_top", top)
            putInt("pad_bot", bot)
            putInt("pad_left", left)
            putInt("pad_right", right)
            putInt("cols", cols)
            putInt("rows", rows)
            putBoolean("show_sym", showSym)
            apply()
        }

        finish()

    }
}