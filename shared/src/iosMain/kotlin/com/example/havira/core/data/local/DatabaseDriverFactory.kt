package com.example.havira.core.data.local

import com.example.havira.database.HaviraDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun create(): SqlDriver {
        return NativeSqliteDriver(HaviraDatabase.Schema,  "havira.db")
    }
}