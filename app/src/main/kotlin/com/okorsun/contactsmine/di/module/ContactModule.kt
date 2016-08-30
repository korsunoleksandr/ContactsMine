package com.okorsun.contactsmine.di.module

import dagger.Module
import dagger.Provides

/**
 * Created by okorsun on 30.08.16.
 */

@Module
class ContactModule(private val contactId: Int) {

    @Provides
    fun provideContactId(): Int = contactId
}