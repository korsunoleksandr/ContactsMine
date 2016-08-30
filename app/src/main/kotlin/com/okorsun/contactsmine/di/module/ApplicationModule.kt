package com.okorsun.contactsmine.di.module

import android.content.Context
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.okorsun.contactsmine.app.AndroidApplication
import com.okorsun.contactsmine.core.RxSchedulers
import com.okorsun.contactsmine.core.RxSchedulersImpl
import com.okorsun.contactsmine.db.Contact
import com.okorsun.contactsmine.db.DbHelper
import com.okorsun.contactsmine.db.Number
import com.okorsun.contactsmine.di.AppScope
import dagger.Module
import dagger.Provides

/**
 * Created by okorsun on 29.08.16.
 */

@Module
class ApplicationModule(private val androidApplication: AndroidApplication) {

    @Provides
    fun provideContext(): Context = androidApplication.applicationContext

    @Provides
    @AppScope
    fun provideRxSchedulers(rxSchedulers: RxSchedulersImpl): RxSchedulers = rxSchedulers

    @Provides
    fun provideContactDao(dbHelper: DbHelper): Dao<Contact, Int> =
            DaoManager.createDao<Dao<Contact, Int>, Contact>(dbHelper.connectionSource, Contact::class.java)

    @Provides
    fun provideNumberDao(dbHelper: DbHelper): Dao<Number, Int> =
            DaoManager.createDao<Dao<Number, Int>, Number>(dbHelper.connectionSource, Number::class.java)
}