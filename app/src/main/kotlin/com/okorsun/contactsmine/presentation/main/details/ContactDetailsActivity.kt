package com.okorsun.contactsmine.presentation.main.details

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.okorsun.contactsmine.R
import com.okorsun.contactsmine.common.bindView
import com.okorsun.contactsmine.common.createRendererAdapter
import com.okorsun.contactsmine.db.Contact
import com.okorsun.contactsmine.db.Number
import com.okorsun.contactsmine.di.component.ApplicationComponent
import com.okorsun.contactsmine.di.component.UiComponent
import com.okorsun.contactsmine.presentation.base.BaseActivity
import com.okorsun.contactsmine.widget.DividerItemDecoration
import com.pedrogomez.renderers.RVRendererAdapter
import com.squareup.picasso.Picasso

/**
 * Created by okorsun on 30.08.16.
 */
class ContactDetailsActivity : BaseActivity<ContactDetailsPresenter, ContactDetailsView>(), ContactDetailsView {
    override val view: ContactDetailsView = this

    private val toolbar by bindView<Toolbar>(R.id.toolbar)
    private val contactImage by bindView<ImageView>(R.id.contact_image)
    private val numbersRV by bindView<RecyclerView>(R.id.numbers_rv)
    private val emptyView by bindView<TextView>(R.id.empty_view)

    private lateinit var adapter: RVRendererAdapter<Number>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_details)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        adapter = createRendererAdapter(NumberItemRenderer(
                { presenter.callNumber(it) },
                { presenter.sendMessage(it) }
        ))
        numbersRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        numbersRV.adapter = adapter
        numbersRV.addItemDecoration(DividerItemDecoration(this, R.drawable.list_divider))
    }

    override fun showContact(data: Contact) {
        supportActionBar?.title = data.name
        Picasso.with(this).load(data.imageUrl).error(R.drawable.default_image).centerCrop().into(contactImage)
        showNumber(data.numbers?.toList() ?: emptyList<Number>())
    }

    private fun showNumber(numbers: List<Number>) {
        adapter.clear()
        adapter.addAll(numbers)
        adapter.notifyDataSetChanged()

        isEmptyCheck(numbers)
    }

    private fun isEmptyCheck(data: List<Number>) {
        if (data.isEmpty()) {
            numbersRV.visibility = View.GONE;
            emptyView.visibility = View.VISIBLE;
        } else {
            numbersRV.visibility = View.VISIBLE;
            emptyView.visibility = View.GONE;
        }
    }

    override fun showMessage(message: String) {
        throw UnsupportedOperationException()
    }

    override fun createComponent(applicationComponent: ApplicationComponent): UiComponent<ContactDetailsPresenter, ContactDetailsView> {
        throw UnsupportedOperationException()
    }


}