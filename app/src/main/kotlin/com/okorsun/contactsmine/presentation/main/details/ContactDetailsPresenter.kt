package com.okorsun.contactsmine.presentation.main.details

import com.okorsun.contactsmine.core.ContactsManager
import com.okorsun.contactsmine.core.RxSchedulers
import com.okorsun.contactsmine.db.Contact
import com.okorsun.contactsmine.presentation.base.Presenter
import com.okorsun.contactsmine.presentation.base.PresenterView

/**
 * Created by okorsun on 30.08.16.
 */

interface ContactDetailsView : PresenterView {
    fun showContact(data: Contact)
    fun showMessage(message: String)
}

class ContactDetailsPresenter(private val rxSchedulers: RxSchedulers,
                              private val contactsManager: ContactsManager,
                              private val contactId: Int) : Presenter<ContactDetailsView>() {

    init {
        contactsManager.getContact(contactId)
                .bindUntilEvent(LifecycleEvent.DESTROY)
                .latestCache()
                .subscribe(onNext = { v, d -> v.showContact(d) },
                        onError = { v, e -> v.showMessage(e.message ?: "ERROR") })
    }

    fun editContact(){

    }

    fun callNumber(number: String){

    }

    fun sendMessage(number: String){

    }

}