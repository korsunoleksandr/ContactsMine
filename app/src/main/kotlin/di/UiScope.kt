package di

import javax.inject.Scope
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*

@Scope
@Retention(RUNTIME)
@Target(CLASS, FIELD, FUNCTION)
annotation class UiScope