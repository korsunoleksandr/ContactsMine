package com.okorsun.contactsmine.di.component

import com.okorsun.contactsmine.core.ContactsManager
import com.okorsun.contactsmine.core.RxSchedulers
import com.okorsun.contactsmine.di.AppScope
import com.okorsun.contactsmine.di.module.ApplicationModule
import dagger.Component

/**
 * Created by okorsun on 29.08.16.
 */

@Component(modules = arrayOf(ApplicationModule::class))
@AppScope
interface ApplicationComponent {

    fun rxSchedulers(): RxSchedulers

    fun contactManager(): ContactsManager
}