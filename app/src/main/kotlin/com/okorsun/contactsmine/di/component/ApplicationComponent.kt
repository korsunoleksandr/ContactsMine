package com.okorsun.contactsmine.di.component

import dagger.Component
import com.okorsun.contactsmine.di.AppScope
import com.okorsun.contactsmine.di.module.ApplicationModule

/**
 * Created by okorsun on 29.08.16.
 */

@Component(modules = arrayOf(ApplicationModule::class))
@AppScope
interface ApplicationComponent {
}