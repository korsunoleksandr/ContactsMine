package db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import di.AppScope
import javax.inject.Inject

/**
 * Created by okorsun on 29.08.16.
 */

@AppScope
class DbHelper
@Inject constructor(private val context: Context) : OrmLiteSqliteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "contacts.db"
        private const val DB_VERSION = 1
        private val DB_ITEMS = arrayListOf(Contact::class.java, Number::class.java)
    }

    override fun onCreate(database: SQLiteDatabase?, connectionSource: ConnectionSource?) {
        DB_ITEMS.forEach {
            TableUtils.clearTable(connectionSource, it)
        }
    }

    override fun onUpgrade(database: SQLiteDatabase?, connectionSource: ConnectionSource?, oldVersion: Int, newVersion: Int) {
    }

}