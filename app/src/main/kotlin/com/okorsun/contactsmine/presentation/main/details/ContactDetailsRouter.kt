package com.okorsun.contactsmine.presentation.main.details

import com.okorsun.contactsmine.presentation.base.Router
import com.okorsun.contactsmine.presentation.base.RouterView

/**
 * Created by okorsun on 30.08.16.
 */
class ContactDetailsRouter : Router<RouterView>() {

    companion object{
        const val CONTACT_ID = "contact_id"
    }

    fun openContacDetails(contactId: Int){
        startActivity<ContactDetailsActivity>(CONTACT_ID to contactId)
    }

}