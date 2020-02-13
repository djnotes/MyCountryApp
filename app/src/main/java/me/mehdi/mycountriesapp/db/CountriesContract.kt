package me.mehdi.mycountriesapp.db

import android.provider.BaseColumns

object CountriesContract {
    object Country : BaseColumns {
        const val TABLE_NAME = "countries"
        const val COLUMN_NAME = "name"
        const val COLUMN_POPULATION = "population"
        const val COLUMN_IMAGE = "image"
    }

}