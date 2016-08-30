package com.okorsun.contactsmine.presentation.main

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.okorsun.contactsmine.R
import com.pedrogomez.renderers.RVRendererAdapter
import com.okorsun.contactsmine.common.bindView
import com.okorsun.contactsmine.common.createRendererAdapter
import com.okorsun.contactsmine.db.Contact
import com.okorsun.contactsmine.di.component.ApplicationComponent
import com.okorsun.contactsmine.di.component.DaggerAllContactsActivityComponent
import com.okorsun.contactsmine.di.component.UiComponent
import com.okorsun.contactsmine.presentation.base.BaseActivity

/**
 * Created by okorsun on 30.08.16.
 */
class AllContactsActivity : BaseActivity<AllContactsPresenter, AllContactsView>(), AllContactsView {

    override val view: AllContactsView = this

    private val fab by bindView<FloatingActionButton>(R.id.fab)
    private val contactsRV by bindView<RecyclerView>(R.id.contacts_rv)
    private val toolbar by bindView<Toolbar>(R.id.toolbar)

    private lateinit var adapter: RVRendererAdapter<Contact>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_contacts)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        adapter = createRendererAdapter(ContactItemRenderer({ it }))
        contactsRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        contactsRV.adapter = adapter

    }

    override fun createComponent(applicationComponent: ApplicationComponent): UiComponent<AllContactsPresenter, AllContactsView> =
            DaggerAllContactsActivityComponent.builder()
                    .applicationComponent(applicationComponent)
                    .build()

}