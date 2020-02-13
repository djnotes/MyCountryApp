package me.mehdi.mycountriesapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class AddCountryDialogActivity : AppCompatActivity() {

    companion object {
        const val COUNTRY_NAME = "me.mehdi.mycountriesapp.country_name"
        const val POPULATION = "me.mehdi.mycountriesapp.population"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_country_dialog)

        title = "New Country"

        val name : EditText = findViewById(R.id.countryName)
        val population: EditText = findViewById(R.id.population)

        val save : Button = findViewById(R.id.save)
        val cancel: Button = findViewById(R.id.cancel)

        save.setOnClickListener {
            val nameText = name.text.toString()
            val populationText = population.text.toString()
            if(nameText.isEmpty() || populationText.isEmpty()){
                Toast.makeText(applicationContext, "لطفا موارد خواسته شده را تکمیل کنید", Toast.LENGTH_SHORT).show()
            }
            else {
                val resultIntent = Intent()
                resultIntent.putExtra(COUNTRY_NAME, nameText)
                resultIntent.putExtra(POPULATION, populationText.toInt())
                setResult(RESULT_OK, resultIntent)
                finish()
            }

        }

        cancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

    }
}