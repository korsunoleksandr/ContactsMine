package com.okorsun.contactsmine.app

import android.app.Application
import com.okorsun.contactsmine.di.ComponentProvider
import com.okorsun.contactsmine.di.component.ApplicationComponent
import com.okorsun.contactsmine.di.component.DaggerApplicationComponent
import com.okorsun.contactsmine.di.module.ApplicationModule

/**
 * Created by okorsun on 20.04.16.
 */
class AndroidApplication : Application() {

    val componentProvider = ComponentProvider()

    val applicationComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

}