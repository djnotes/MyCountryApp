package me.mehdi.mycountriesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
}
