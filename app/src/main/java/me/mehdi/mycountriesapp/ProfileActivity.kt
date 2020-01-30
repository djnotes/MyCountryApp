package me.mehdi.mycountriesapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.FileDescriptor
import java.io.FileNotFoundException

class ProfileActivity : AppCompatActivity() {

    companion object {
        const val STORAGE_REQUEST_CODE = 100
        const val REQUEST_FILE_CODE = 200
        const val MYAPP_DATA = "myapp_data"
        const val PROFILE_IMAGE_URI = "profile_image_uri"
    }

    lateinit var profile: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profile = findViewById(R.id.profileImage)

        val prefs = getSharedPreferences(MYAPP_DATA, Context.MODE_PRIVATE)
        if(prefs.contains(PROFILE_IMAGE_URI)){
            val uri = Uri.parse(prefs.getString(PROFILE_IMAGE_URI, ""))

            var pFD : ParcelFileDescriptor? = null
            try {
                uri?.also {
                    pFD = contentResolver.openFileDescriptor(it, "r")
//                    contentResolver.takePersistableUriPermission(
//                        it,
//                        Intent.FLAG_GRANT_READ_URI_PERMISSION
//                    )
                }
            } catch(ex: FileNotFoundException){
                Log.e("ProfileActivity", "File not found ${ex.message}")
                return
            }

            val bmp = BitmapFactory.decodeFileDescriptor(pFD?.fileDescriptor)

            profile.setImageBitmap(bmp)

        }


        profile.setOnClickListener {
            if(ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this@ProfileActivity, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_REQUEST_CODE)
            }
            else {
                changeProfilePicture()
            }
        }


    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == STORAGE_REQUEST_CODE &&
               grantResults[0] ==  PackageManager.PERMISSION_GRANTED)
        {
            changeProfilePicture()
        }
        else {
            Toast.makeText(applicationContext, R.string.permission_not_granted, Toast.LENGTH_LONG).show()
        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    private fun changeProfilePicture() {
        val fileIntent = Intent(
            Intent.ACTION_OPEN_DOCUMENT
        ).apply{
            type = "image/jpeg"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(fileIntent, REQUEST_FILE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, returnedIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, returnedIntent)
        var pFD : ParcelFileDescriptor? = null
        when(requestCode) {
            REQUEST_FILE_CODE -> {
                if(resultCode == Activity.RESULT_OK) {
                    try {
                        returnedIntent!!.data?.also {
                            pFD = contentResolver.openFileDescriptor(it, "r")
                            contentResolver.takePersistableUriPermission(
                                it,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                            )
                        }
                    } catch(ex: FileNotFoundException){
                        Log.e("ProfileActivity", "File not found ${ex.message}")
                        return
                    }

                    val bmp = BitmapFactory.decodeFileDescriptor(pFD?.fileDescriptor)

                    profile.setImageBitmap(bmp)

                    val prefs = getSharedPreferences(MYAPP_DATA, Context.MODE_PRIVATE)
                    prefs.edit().putString(PROFILE_IMAGE_URI, returnedIntent.data.toString())
                        .apply()
                }
            }
        }
    }
}
