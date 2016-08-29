package presentation.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import app.AndroidApplication
import di.ComponentProvider
import di.component.ApplicationComponent
import di.component.UiComponent
import java.util.*
import javax.inject.Inject

abstract class BaseActivity<P : Presenter<V>, V : PresenterView> : AppCompatActivity(), PresenterView {

    companion object {
        private val KEY_COMPONENT_NAME = "key_component_name"
    }

    private val routers = mutableListOf<Router<*>>()

    @Inject
    protected lateinit var presenter: P

    protected abstract val view: V
//    protected val toolbar: Toolbar? by bindOptionalView(R.id.toolbar)

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
//        setUpScreenOrientation()

        componentName = savedInstanceState?.getString(KEY_COMPONENT_NAME) ?: UUID.randomUUID().toString()

        component.inject(this)
        presenter.viewCreated(view)

        if (savedInstanceState != null) {
            presenter.restoreState(savedInstanceState)
        }
    }


    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setupToolbar()
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        setupToolbar()
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        super.setContentView(view, params)
        setupToolbar()
    }

    private fun setupToolbar() {
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        toolbar?.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_COMPONENT_NAME, componentName)
        presenter.saveState(outState)
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

        if (isFinishing) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        routers.forEach { router ->
            router.activityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun requestTkPermissions(permissions: Set<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(this, permissions.toTypedArray(), requestCode)
    }

    override val appContext: Context
        get() = applicationContext

    private val application: AndroidApplication
        get() = applicationContext as AndroidApplication

    protected abstract fun createComponent(applicationComponent: ApplicationComponent): UiComponent<P, V>

}