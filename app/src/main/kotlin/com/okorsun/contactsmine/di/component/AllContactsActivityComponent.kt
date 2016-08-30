package com.okorsun.contactsmine.di.component

import dagger.Component
import com.okorsun.contactsmine.di.UiScope
import com.okorsun.contactsmine.presentation.main.allcontacts.AllContactsPresenter
import com.okorsun.contactsmine.presentation.main.allcontacts.AllContactsView

/**
 * Created by okorsun on 30.08.16.
 */

@Component(dependencies = arrayOf(ApplicationComponent::class))
@UiScope
interface AllContactsActivityComponent : UiComponent<AllContactsPresenter, AllContactsView> {
}