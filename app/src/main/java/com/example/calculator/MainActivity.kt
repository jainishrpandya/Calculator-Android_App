package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.Toast
import android.widget.Toolbar
import com.example.calculator.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var canAddOperator = false
    var canAddOperation = false
    var canAddDecimal = true


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var clearbutton = findViewById<Button>(R.id.clearButton) as Button

        clearbutton.setOnLongClickListener{
            ClearAll()
        true
        }
    }



//    Toast for testing
//    Toast.makeText(this@MainActivity, "You clicked me.", Toast.LENGTH_SHORT).show()



    fun backSpaceAction(view: View){
        try {
            val length = binding.workingTV.text.length
            if (length > 0){
                binding.workingTV.text = binding.workingTV.text.subSequence(0, length - 1)
            }
        } catch (e: Exception) {
            Toast.makeText(this@MainActivity, "Some Error Occured", Toast.LENGTH_SHORT).show()
        }
    }

    fun numberAction(view: View)
    {
        if(view is Button)
        {
            if (view.text == ".")
            {
                if (canAddDecimal)
                    binding.workingTV.append(view.text)

                canAddDecimal = false
            }
            else
                binding.workingTV.append(view.text)

            canAddOperator = true
        }
    }

    fun operationAction(view: View)
    {
        if (view is Button)
        {
            binding.workingTV.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }

    fun ClearAll(){
        binding.workingTV.text = ""
        binding.resultTV.text = "0"
    }

    fun equalAction(view: View)
    {
        try {
            binding.resultTV.visibility = View.VISIBLE
            binding.resultTV.text = ""
            binding.resultTV.text = calculateResult()
        } catch (e: Exception){
            Toast.makeText(this@MainActivity, "Some Error Occured", Toast.LENGTH_SHORT).show()
        }
    }

    private fun calculateResult(): String
    {
       val digitsOperators = digitsOperators()
       if(digitsOperators.isEmpty()) return ""

       val timesDivision = timesDivisionCalculate(digitsOperators)
       if (digitsOperators.isEmpty()) return ""

       val result = addSubtractCalculate(timesDivision)
        return result.toString()
    }


    private fun digitsOperators(): MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for(character in binding.workingTV.text)
        {
            if (character.isDigit() || character == '.') {
                currentDigit += character
            } else {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if (currentDigit != ""){
            list.add(currentDigit.toFloat())
        }

        return list
    }


    private fun addSubtractCalculate(passedList: MutableList<Any>): Float
    {
        var result = passedList[0] as Float

        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex)
            {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit
            }
        }
        return result
    }


    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any>
    {
        var list = passedList
        while(list.contains('*') || list.contains('/') || list.contains('%'))
        {
            list = calcTimesDiv(list)
        }
        return list
    }


    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any>
    {
        val newlist = mutableListOf<Any>()
        var restartIndex = passedList.size

        for(i in passedList.indices){
            if(passedList[i] is Char && i != passedList.lastIndex && i < restartIndex)
            {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when(operator) {
                    '*' ->
                    {
                        newlist.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' ->
                    {
                        newlist.add(prevDigit/nextDigit)
                        restartIndex = i + 1
                    }
                    '%' -> {
                        newlist.add(prevDigit * nextDigit / 100)
                        restartIndex = i + 1
                    }
                    else ->
                    {
                        newlist.add(prevDigit)
                        newlist.add(operator)
                    }
            }
        }
            if(i > restartIndex){
                newlist.add(passedList[i])
            }
        }
        return newlist
    }





    fun closeTheApp(view: View){
        finish();
        System.exit(0);
    }

}