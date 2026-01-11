package com.cyberfeedforward.gibblygoatgames.viewmodels

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "MainViewModel"

class MainViewModel : ViewModel() {
    init {
        Log.i(TAG, "MainViewModel created")
    }

    var quantity: Int = 2

    fun shareActivity(context: Context, shareMessage: Int) {
        Log.i(TAG, "shareActivity")

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"

            putExtra(
                Intent.EXTRA_TEXT,
                shareMessage
            )
        }

        context.startActivity(intent)
    }
}