package com.okorsun.contactsmine.presentation.main.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.okorsun.contactsmine.R
import com.okorsun.contactsmine.db.Number
import com.okorsun.contactsmine.presentation.base.KRenderer

/**
 * Created by okorsun on 30.08.16.
 */
class NumberItemRenderer(private val callNumber: (String) -> Unit,
                         private val sendMessage: (String) -> Unit): KRenderer<Number>() {

    private val callBtn by bindView<ImageButton>(R.id.btn_call)
    private val messageBtn by bindView<ImageButton>(R.id.btn_message)
    private val number by bindView<TextView>(R.id.contact_number)

    override fun inflate(inflater: LayoutInflater, parent: ViewGroup): View? =
         inflater.inflate(R.layout.numbers_list_item, parent, false)

    override fun hookListeners(rootView: View) {
        callBtn.setOnClickListener { callNumber(content.number) }
        messageBtn.setOnClickListener { sendMessage(content.number) }
    }

    override fun render() {
        number.text = content.number
    }

    override fun setUpView(rootView: View?) {
    }
}