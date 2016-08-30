package com.okorsun.contactsmine.core

import com.okorsun.contactsmine.common.createObservable
import com.okorsun.contactsmine.db.Contact
import com.okorsun.contactsmine.db.ContactDao
import com.okorsun.contactsmine.db.Number
import com.okorsun.contactsmine.db.NumberDao
import com.okorsun.contactsmine.di.AppScope
import rx.Observable
import javax.inject.Inject

/**
 * Created by okorsun on 29.08.16.
 */

@AppScope
class ContactsManager
@Inject
constructor(private val contactDao: ContactDao,
            private val numberDao: NumberDao,
            private val notifier: Notifier,
            private val rxSchedulers: RxSchedulers) {

    fun getContacts(): Observable<List<Contact>> {
        return withRefetch(notifier, Contact::class) {
            createObservable { contactDao.queryForAll() }
        }
                .subscribeOn(rxSchedulers.db)
                .observeOn(rxSchedulers.main)
    }

    fun getContact(id: Int): Observable<Contact> {
        return withRefetch(notifier, Contact::class) {
            createObservable { contactDao.queryForId(id) }
        }
    }

    fun deletContact(contact: Contact) {
        notify(notifier, Contact::class) {
            contactDao.delete(contact)
        }
    }

    fun createOrUpdateContact(contact: Contact) {
        notify(notifier, Contact::class) {
            contactDao.createOrUpdate(contact)
        }
    }

    fun addOrModifyNumber(number: Number) {
        notify(notifier, Contact::class) {
            numberDao.createOrUpdate(number)
        }
    }

    fun deleteNumber(number: Number) {
        notify(notifier, Contact::class) {
            numberDao.delete(number)
        }
    }

}