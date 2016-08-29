package presentation.base

import android.content.Context
import android.content.Intent

interface RouterView {
    fun startActivity(intent: Intent, requestCode: Int = -1)

    fun finish()

    val appContext: Context
}