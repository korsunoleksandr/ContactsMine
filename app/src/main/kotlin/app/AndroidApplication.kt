package app

import android.app.Application
import di.ComponentProvider
import di.component.ApplicationComponent
import di.component.DaggerApplicationComponent
import di.module.ApplicationModule

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