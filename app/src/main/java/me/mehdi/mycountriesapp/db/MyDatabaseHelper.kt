package me.mehdi.mycountriesapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class MyDatabaseHelper(context: Context) : SQLiteOpenHelper(context,
    DB_NAME, null,
    DB_VERSION
) {

    companion object {
        const val DB_NAME = "mycountries.db"
        const val DB_VERSION = 1
        const val SQL_CREATE_COUNTRIES = "CREATE TABLE ${CountriesContract.Country.TABLE_NAME} (" +
                "${BaseColumns._ID} INT PRIMARY KEY, " +
                "${CountriesContract.Country.COLUMN_NAME} TEXT, " +
                "${CountriesContract.Country.COLUMN_POPULATION} INT, " +
                "${CountriesContract.Country.COLUMN_IMAGE} TEXT )"
        const val SQL_INSERT_DEFAULT = "INSERT INTO ${CountriesContract.Country.TABLE_NAME} (" +
                "${CountriesContract.Country.COLUMN_NAME}, ${CountriesContract.Country.COLUMN_POPULATION}) " +
                "VALUES ('IRAN', 80000000)"

        const val SQL_DELETE_COUNTRIES = "DROP TABLE IF EXISTS ${CountriesContract.Country.TABLE_NAME}"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_COUNTRIES)
        db?.execSQL(SQL_INSERT_DEFAULT)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_COUNTRIES)
        onCreate(db)
    }

}