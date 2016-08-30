package com.okorsun.contactsmine.di.component

import com.okorsun.contactsmine.di.UiScope
import com.okorsun.contactsmine.di.module.ContactModule
import com.okorsun.contactsmine.presentation.main.details.ContactDetailsPresenter
import com.okorsun.contactsmine.presentation.main.details.ContactDetailsView
import dagger.Component

/**
 * Created by okorsun on 30.08.16.
 */

@Component(dependencies = arrayOf(ApplicationComponent::class),
        modules = arrayOf(ContactModule::class))
@UiScope
interface ContactDetailsActivityComponent : UiComponent<ContactDetailsPresenter, ContactDetailsView> {
}