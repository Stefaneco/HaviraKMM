package com.piotrkalin.havira.android.auth.data

import android.content.Context
import com.piotrkalin.havira.auth.domain.ITokenDataSource

class TokenDataSource(private val context: Context) : ITokenDataSource {

    override fun getAuthToken(): String? {
        val sharedPref = context.getSharedPreferences(
            "com.piotrkalin.havira.android.PREFERENCE_FILE_KEY",
            Context.MODE_PRIVATE
        )

        return sharedPref.getString("authToken", null)
            //?: throw Exception("Session not found on the device!")
    }

    override fun updateAuthToken(token: String) {
        val sharedPref = context.getSharedPreferences("com.piotrkalin.havira.android.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        with(sharedPref.edit()){
            putString("authToken", token)
            commit()
        }
        println("Auth TokenDataSource: Saved token - $token")
    }

    override fun getProfileId(): String? {
        val sharedPref = context.getSharedPreferences("com.piotrkalin.havira.android.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        println("Auth TokenDataSource userId: ${sharedPref.getString("userId", null)}")
        return sharedPref.getString("userId", null)
    }

    override fun updateProfileId(userId: String) {
        val sharedPref = context.getSharedPreferences("com.piotrkalin.havira.android.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        with(sharedPref.edit()){
            putString("userId", userId)
            commit()
        }
        println("Auth TokenDataSource updated userId: ${sharedPref.getString("userId", null)}")
    }

    override fun removeAllTokens() {
        val sharedPref = context.getSharedPreferences("com.piotrkalin.havira.android.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        with(sharedPref.edit()){
            remove("authToken")
            commit()
        }
    }
}