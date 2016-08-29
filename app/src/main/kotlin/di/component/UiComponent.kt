package di.component

import presentation.base.*

/**
 * Created by okorsun on 20.04.16.
 */
interface UiComponent<P : Presenter<V>, V : PresenterView> {

    fun inject(activity: BaseActivity<P, V>)

    fun inject(fragment: BaseFragment<P, V>)

    fun inject(dialog: BaseDialogFragment<P, V>)

}