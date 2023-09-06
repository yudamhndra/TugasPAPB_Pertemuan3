package com.example.calculator

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.ArithmeticException
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    var lastNumeric = false
    var stateError = false
    var lastDec = false
    var previousResult: Double? = null

    private lateinit var expression: Expression
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    fun onEqualClick(view: View) {
        onEqual()
        binding.txtCalculate.visibility = View.GONE
        binding.txtResult.visibility = View.VISIBLE
    }


    fun onOperatorClick(view: View) {

        if (!stateError && lastNumeric){
            binding.txtCalculate.append((view as Button).text)
            lastDec = false
            lastNumeric = false
            onEqual()
        }
    }


    fun onDigitClick(view: View) {
        if (stateError){
            binding.txtCalculate.text = (view as Button).text
            stateError = false
        } else{

            binding.txtCalculate.append((view as Button).text)
        }

        lastNumeric = true
        onEqual()

    }


    fun onClearClick(view: View) {
        binding.txtResult.text = ""
        binding.txtCalculate.text = ""
        stateError = false
        lastNumeric = false
        binding.txtResult.visibility = View.GONE
        binding.txtCalculate.visibility = View.VISIBLE
    }

    fun onBackClick(view: View) {
        binding.txtCalculate.text = binding.txtCalculate.text.toString().dropLast(1)

        try {
            val lastchar = binding.txtCalculate.text.toString().last()

            if (lastchar.isDigit()){
                onEqual()
            }
        } catch (e : Exception){

            binding.txtResult.text = ""
            binding.txtResult.visibility = View.GONE
            Log.e("last char error", e.toString())

        }
        }

    fun onPercentageClick(view: View) {

        try {
            if (previousResult != null) {
                val percentage = previousResult!! / 100.0
                previousResult = percentage // Simpan hasil persentase ke lastResult

                binding.txtCalculate.text = percentage.toString()
            } else {
                // Tampilkan pesan kesalahan jika lastResult belum dihitung
                Toast.makeText(this, "No result to calculate percentage from", Toast.LENGTH_SHORT).show()
            }
        } catch (e: NumberFormatException) {
            // Tangani kesalahan jika teks tidak dapat diubah menjadi angka
            Toast.makeText(this, "Invalid Number", Toast.LENGTH_SHORT).show()
        }
    }

    fun onEqual() {
        if (lastNumeric && !stateError) {
            val txt = binding.txtCalculate.text.toString()

            expression = ExpressionBuilder(txt).build()

            try {
                val result = expression.evaluate()

                previousResult = result

                binding.txtResult.text = result.toString()

                binding.txtCalculate.visibility = View.VISIBLE

            } catch (ex : ArithmeticException){
                Log.e("evaluate error", ex.toString())
                binding.txtResult.text = "error"
                stateError = true
                lastNumeric = false
            }
        }
    }





}

