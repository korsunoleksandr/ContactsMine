package com.okorsun.contactsmine.presentation.main.allcontacts

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.okorsun.contactsmine.R
import com.okorsun.contactsmine.common.bindView
import com.okorsun.contactsmine.common.createRendererAdapter
import com.okorsun.contactsmine.db.Contact
import com.okorsun.contactsmine.di.component.ApplicationComponent
import com.okorsun.contactsmine.di.component.DaggerAllContactsActivityComponent
import com.okorsun.contactsmine.di.component.UiComponent
import com.okorsun.contactsmine.presentation.base.BaseActivity
import com.okorsun.contactsmine.widget.DividerItemDecoration
import com.pedrogomez.renderers.RVRendererAdapter

/**
 * Created by okorsun on 30.08.16.
 */
class AllContactsActivity : BaseActivity<AllContactsPresenter, AllContactsView>(), AllContactsView {

    override val view: AllContactsView = this

    private val fab by bindView<FloatingActionButton>(R.id.fab)
    private val contactsRV by bindView<RecyclerView>(R.id.contacts_rv)
    private val emptyView by bindView<TextView>(R.id.empty_view)
    private val toolbar by bindView<Toolbar>(R.id.toolbar)

    private lateinit var adapter: RVRendererAdapter<Contact>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_contacts)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        fab.setOnClickListener { presenter.newContact() }

        adapter = createRendererAdapter(ContactItemRenderer({ presenter.openContacDetails(it) }))
        contactsRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        contactsRV.adapter = adapter
        contactsRV.addItemDecoration(DividerItemDecoration(this, R.drawable.list_divider))
    }

    override fun showContacts(data: List<Contact>) {
        adapter.clear()
        adapter.addAll(data)
        adapter.notifyDataSetChanged()

        isEmptyCheck(data)
    }

    private fun isEmptyCheck(data: List<Contact>) {
        if (data.isEmpty()) {
            contactsRV.visibility = View.GONE;
            emptyView.visibility = View.VISIBLE;
        } else {
            contactsRV.visibility = View.VISIBLE;
            emptyView.visibility = View.GONE;
        }
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun createComponent(applicationComponent: ApplicationComponent): UiComponent<AllContactsPresenter, AllContactsView> =
            DaggerAllContactsActivityComponent.builder()
                    .applicationComponent(applicationComponent)
                    .build()

}