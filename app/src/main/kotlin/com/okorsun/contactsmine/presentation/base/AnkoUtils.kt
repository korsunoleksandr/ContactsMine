package com.okorsun.contactsmine.presentation.base

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

object AnkoUtils {


    // Some constants not present in Android SDK v.15
    private object InternalConfiguration {
        val SCREENLAYOUT_LAYOUTDIR_MASK = 0xC0
        val SCREENLAYOUT_LAYOUTDIR_SHIFT = 6
        val SCREENLAYOUT_LAYOUTDIR_RTL = 0x02 shl SCREENLAYOUT_LAYOUTDIR_SHIFT

        val UI_MODE_TYPE_APPLIANCE = 0x05
        val UI_MODE_TYPE_WATCH = 0x06

        val DENSITY_DPI_NONE = 0xffff
    }

    @JvmStatic
    fun <T> createIntent(ctx: Context, clazz: Class<out T>, params: Array<out Pair<String, Any>>): Intent {
        val intent = Intent(ctx, clazz)
        if (params.isNotEmpty()) fillIntentArguments(intent, params)
        return intent
    }

    @JvmStatic
    fun internalStartActivity(
            ctx: Context,
            activity: Class<out Activity>,
            params: Array<out Pair<String, Any>>
    ) {
        ctx.startActivity(createIntent(ctx, activity, params))
    }

    @JvmStatic
    fun internalStartActivityForResult(
            act: Activity,
            activity: Class<out Activity>,
            requestCode: Int,
            params: Array<out Pair<String, Any>>
    ) {
        act.startActivityForResult(createIntent(act, activity, params), requestCode)
    }

    @JvmStatic
    fun internalStartService(
            ctx: Context,
            activity: Class<out Service>,
            params: Array<out Pair<String, Any>>
    ) {
        ctx.startService(createIntent(ctx, activity, params))
    }

    @JvmStatic
    private fun fillIntentArguments(intent: Intent, params: Array<out Pair<String, Any>>) {
        params.forEach {
            val value = it.second
            when (value) {
                is Int -> intent.putExtra(it.first, value)
                is Long -> intent.putExtra(it.first, value)
                is CharSequence -> intent.putExtra(it.first, value)
                is String -> intent.putExtra(it.first, value)
                is Float -> intent.putExtra(it.first, value)
                is Double -> intent.putExtra(it.first, value)
                is Char -> intent.putExtra(it.first, value)
                is Short -> intent.putExtra(it.first, value)
                is Boolean -> intent.putExtra(it.first, value)
                is Serializable -> intent.putExtra(it.first, value)
                is Bundle -> intent.putExtra(it.first, value)
                is Parcelable -> intent.putExtra(it.first, value)
                is Array<*> -> when {
                    value.isArrayOf<CharSequence>() -> intent.putExtra(it.first, value)
                    value.isArrayOf<String>() -> intent.putExtra(it.first, value)
                    value.isArrayOf<Parcelable>() -> intent.putExtra(it.first, value)
                    else -> throw IllegalArgumentException("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
                }
                is IntArray -> intent.putExtra(it.first, value)
                is LongArray -> intent.putExtra(it.first, value)
                is FloatArray -> intent.putExtra(it.first, value)
                is DoubleArray -> intent.putExtra(it.first, value)
                is CharArray -> intent.putExtra(it.first, value)
                is ShortArray -> intent.putExtra(it.first, value)
                is BooleanArray -> intent.putExtra(it.first, value)
                else -> throw IllegalArgumentException("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
            }
        }
    }
}