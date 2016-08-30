package com.okorsun.contactsmine.presentation.main.allcontacts

import com.okorsun.contactsmine.core.ContactsManager
import com.okorsun.contactsmine.core.RxSchedulers
import com.okorsun.contactsmine.db.Contact
import com.okorsun.contactsmine.di.UiScope
import com.okorsun.contactsmine.presentation.base.Presenter
import com.okorsun.contactsmine.presentation.base.PresenterView
import javax.inject.Inject

/**
 * Created by okorsun on 30.08.16.
 */

interface AllContactsView : PresenterView {
    fun showContacts(data: List<Contact>)
    fun showMessage(message: String)
}

@UiScope
class AllContactsPresenter
@Inject
constructor(private val rxSchedulers: RxSchedulers,
                           private val contactsManager: ContactsManager) : Presenter<AllContactsView>() {

    init {
        contactsManager.getContacts()
                .bindUntilEvent(LifecycleEvent.DESTROY)
                .latestCache()
                .subscribe(onNext = { v, d -> v.showContacts(d) },
                        onError = { v, e -> v.showMessage(e.message ?: "ERROR") })
    }

    fun newContact(){

    }

    fun openContacDetails(contacId: Int){

    }

}