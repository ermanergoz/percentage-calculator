package com.erman.percentagecalculator

import android.content.Context
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged

class MainActivity : AppCompatActivity() {

    private var numberA: Double = 0.0
    private var numberB: Double = 0.0
    private var mode: Int = 0

    private fun hideKeyboard() {
        val inputManager: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            currentFocus?.windowToken,
            InputMethodManager.SHOW_FORCED
        )
    }

    private fun calculate(mode: Int): Double {
        when (mode) {
            1 -> return (numberA / 100) * numberB
            2 -> return (numberA / numberB) * 100
            3 -> return ((numberB - numberA) / numberA) * 100
            4 -> return numberA + ((numberA / 100) * numberB)
            5 -> return numberA - ((numberA / 100) * numberB)
        }
        return 0.0
    }

    private fun clearAll() {
        radioGroup.clearCheck()
        numberAEditText.text.clear()
        numberBEditText.text.clear()
        resultTextView.text = ""
        hideKeyboard()
    }

    private fun updateResult(result: Double) {
        if (mode == 2 || mode == 3)
            resultTextView.text = getString(R.string.percentage_result, String.format("%.1f", result))
        else
            resultTextView.text = String.format("%.1f", result)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        clearButton.setOnClickListener {
            clearAll()
        }

        numberAEditText.doOnTextChanged { text, start, count, after ->
            numberA = if (text!!.isNotEmpty()) {
                numberAEditText.text.toString().toDouble()
            } else {
                0.0
            }
            updateResult(calculate(mode))
        }

        numberBEditText.doOnTextChanged { text, start, count, after ->
            numberB = if (text!!.isNotEmpty()) {
                numberBEditText.text.toString().toDouble()
            } else {
                0.0
            }
            updateResult(calculate(mode))
        }

        radioGroup.setOnCheckedChangeListener { _, _ ->
            hideKeyboard()

            when {
                PercentageBofNumberARadioButton1.isChecked -> mode = 1
                percentageOfBRadioButton2.isChecked -> mode = 2
                changeAtoBRadioButton3.isChecked -> mode = 3
                increaseByPercentBRadioButton4.isChecked -> mode = 4
                decreaseAbyPercentBRadioButton5.isChecked -> mode = 5
            }
            updateResult(calculate(mode))
        }
    }
}