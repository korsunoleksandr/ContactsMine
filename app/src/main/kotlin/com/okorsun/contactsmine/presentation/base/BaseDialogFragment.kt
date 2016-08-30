package com.okorsun.contactsmine.presentation.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.okorsun.contactsmine.app.AndroidApplication
import com.okorsun.contactsmine.di.ComponentProvider
import com.okorsun.contactsmine.di.component.ApplicationComponent
import com.okorsun.contactsmine.di.component.UiComponent
import java.util.*
import javax.inject.Inject

abstract class BaseDialogFragment<P : Presenter<V>, V : PresenterView> : DialogFragment(), PresenterView {

    companion object {
        private val KEY_COMPONENT_NAME = "key_component_name"
    }

    private val routers = mutableListOf<Router<*>>()

    @Inject
    protected lateinit var presenter: P

    protected abstract val view: V

    private lateinit var componentName: String

    private val component: UiComponent<P, V> by lazy {
        if (!componentProvider.hasComponent(componentName)) {
            componentProvider.putComponent(componentName, createComponent(application.applicationComponent))
        }

        componentProvider.getComponent<UiComponent<P, V>>(componentName)
    }

    private val componentProvider: ComponentProvider by lazy { application.componentProvider }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        componentName = savedInstanceState?.getString(KEY_COMPONENT_NAME) ?: UUID.randomUUID().toString()

        component.inject(this)
        presenter.viewCreated(view)

        if (savedInstanceState != null) {
            presenter.restoreState(savedInstanceState)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_COMPONENT_NAME, componentName)
        presenter.saveState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        presenter.attach(view)
    }

    override fun onStop() {
        super.onStop()
        presenter.detach()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.viewDestroyed(view)

        if (isRemoving || activity.isFinishing) {
            presenter.destroy()
            componentProvider.removeComponent(componentName)
        }
    }

    override fun startActivity(intent: Intent, requestCode: Int) {
        startActivityForResult(intent, requestCode)
    }

    override fun attachRouter(router: Router<*>) {
        routers.add(router)
    }

    override fun detachRouter(router: Router<*>) {
        routers.remove(router)
    }

    override val appContext: Context
        get() = activity.applicationContext

    override fun finish() {
        activity.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        routers.forEach {
            router ->
            router.activityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun requestTkPermissions(permissions: Set<String>, requestCode: Int) {
        requestPermissions(permissions.toTypedArray(), requestCode)
    }

    private val application: AndroidApplication
        get() = context.applicationContext as AndroidApplication

    protected abstract fun createComponent(applicationComponent: ApplicationComponent): UiComponent<P, V>

}