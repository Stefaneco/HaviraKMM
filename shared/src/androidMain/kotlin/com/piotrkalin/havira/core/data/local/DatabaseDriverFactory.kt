package com.piotrkalin.havira.core.data.local

import android.content.Context
import com.piotrkalin.havira.database.HaviraDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory(
    private val context: Context
) {
    actual fun create(): SqlDriver {
        return AndroidSqliteDriver(HaviraDatabase.Schema, context, "havira.db")
    }
}