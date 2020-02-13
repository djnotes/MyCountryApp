package me.mehdi.mycountriesapp

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import me.mehdi.mycountriesapp.db.CountriesContract
import me.mehdi.mycountriesapp.db.MyDatabaseHelper

class MainActivity : AppCompatActivity() {


    companion object {
        const val ADD_CODE = 100
    }

    lateinit var mDbHelper : MyDatabaseHelper
    lateinit var mCursor : Cursor
    lateinit var mLeft : ImageView
    lateinit var mRight : ImageView
    lateinit var mCountry : TextView
    lateinit var mPopulation : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fab : FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener{
            val dialogIntent = Intent(this@MainActivity, AddCountryDialogActivity::class.java)
            startActivityForResult(dialogIntent, ADD_CODE)
        }

        mDbHelper = MyDatabaseHelper(applicationContext)

        mLeft = findViewById(R.id.left)
        mRight = findViewById(R.id.right)

        mCountry = findViewById(R.id.countryName)
        mPopulation = findViewById(R.id.population)


        mRight.setOnClickListener {
            mCursor.moveToNext()
            if(!mCursor.isLast) {
                mCountry.text =
                    mCursor.getString(mCursor.getColumnIndex(CountriesContract.Country.COLUMN_NAME))
                mPopulation.text =
                    "${mCursor.getInt(mCursor.getColumnIndex(CountriesContract.Country.COLUMN_POPULATION))}"
            }
    }

        refreshUI()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.home -> {
                Toast.makeText(applicationContext, R.string.home, Toast.LENGTH_SHORT).show()
            }

            R.id.about -> {
                Toast.makeText(applicationContext, R.string.about, Toast.LENGTH_SHORT).show()
            }

            R.id.profile -> {
                startActivity(
                    Intent(this, ProfileActivity::class.java)
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ADD_CODE && resultCode == RESULT_OK) {
            val countryName = data?.getStringExtra(AddCountryDialogActivity.COUNTRY_NAME)
            val population = data?.getIntExtra(AddCountryDialogActivity.POPULATION, 0)

            val db = mDbHelper.writableDatabase

            val values = ContentValues()
            values.put(CountriesContract.Country.COLUMN_NAME, countryName)
            values.put(CountriesContract.Country.COLUMN_POPULATION, population)

            val rowId = db?.insert(CountriesContract.Country.TABLE_NAME, null, values)

            if(rowId != -1L) {
                Toast.makeText(applicationContext, "آیتم جدید با موفقیت اضافه شد", Toast.LENGTH_LONG)
                    .show()
            }

        }
    }


    fun refreshUI() {
        val database = mDbHelper.readableDatabase


//        val columns = arrayOf(CountriesContract.Country.COLUMN_NAME, CountriesContract.Country.COLUMN_POPULATION, CountriesContract.Country.COLUMN_IMAGE)
        val columns = arrayOf(CountriesContract.Country.COLUMN_NAME, CountriesContract.Country.COLUMN_POPULATION)

        mCursor = database.query(CountriesContract.Country.TABLE_NAME,
            columns,
            null,
            null,
            null,
            null,
            null)


        val numOfCountries : TextView = findViewById(R.id.numOfCountries)
        numOfCountries.text = "${mCursor.count}"

        mCountry= findViewById(R.id.countryName)
        mPopulation = findViewById(R.id.population)

        mCursor.moveToFirst()
        if(mCursor.count > 0) {
            mCountry.text = mCursor.getString(mCursor.getColumnIndex(CountriesContract.Country.COLUMN_NAME))
            mPopulation.text = "${mCursor.getInt(mCursor.getColumnIndex(CountriesContract.Country.COLUMN_POPULATION))}"
        }




    }
}
