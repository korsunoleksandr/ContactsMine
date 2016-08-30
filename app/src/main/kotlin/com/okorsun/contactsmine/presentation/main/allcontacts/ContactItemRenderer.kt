package com.okorsun.contactsmine.presentation.main.allcontacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.okorsun.contactsmine.R
import com.okorsun.contactsmine.db.Contact
import com.okorsun.contactsmine.presentation.base.KRenderer
import com.squareup.picasso.Picasso

/**
 * Created by okorsun on 30.08.16.
 */
class ContactItemRenderer(private val openDetails: (Int) -> Unit): KRenderer<Contact>() {

    val contactImage by bindView<ImageView>(R.id.contact_image)
    val contactName by bindView<TextView>(R.id.contact_name)

    override fun inflate(inflater: LayoutInflater, parent: ViewGroup): View? =
            inflater.inflate(R.layout.all_contacts_list_item, parent, false)

    override fun hookListeners(rootView: View) {
        rootView.setOnClickListener { openDetails(content.id) }
    }

    override fun render() {
        Picasso.with(context).load(content.imageUrl).error(R.drawable.default_image).centerCrop().into(contactImage)
        contactName.text = content.name
    }

    override fun setUpView(rootView: View?) {
    }
}