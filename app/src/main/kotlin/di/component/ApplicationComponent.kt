package di.component

import dagger.Component
import di.AppScope
import di.module.ApplicationModule

/**
 * Created by okorsun on 29.08.16.
 */

@Component(modules = arrayOf(ApplicationModule::class))
@AppScope
interface ApplicationComponent {
}