package di.module

import android.content.Context
import app.AndroidApplication
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import dagger.Module
import dagger.Provides
import db.Contact
import db.DbHelper
import db.Number

/**
 * Created by okorsun on 29.08.16.
 */

@Module
class ApplicationModule(private val androidApplication: AndroidApplication) {

    @Provides
    fun provideContext(): Context = androidApplication.applicationContext

    @Provides
    fun provideContactDao(dbHelper: DbHelper): Dao<Contact, Int> =
            DaoManager.createDao<Dao<Contact, Int>, Contact>(dbHelper.connectionSource, Contact::class.java)

    @Provides
    fun provideNumberDao(dbHelper: DbHelper): Dao<Number, Int> =
            DaoManager.createDao<Dao<Number, Int>, Number>(dbHelper.connectionSource, Number::class.java)
}