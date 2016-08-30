package com.okorsun.contactsmine.db

import com.j256.ormlite.dao.Dao
import javax.inject.Inject

/**
 * Created by okorsun on 29.08.16.
 */
class ContactDao
@Inject
constructor(private val dao: Dao<Contact, Int>) : Dao<Contact, Int> by dao
