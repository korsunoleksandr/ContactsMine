package di.component

import dagger.Component
import di.UiScope
import presentation.main.AllContactsPresenter
import presentation.main.AllContactsView

/**
 * Created by okorsun on 30.08.16.
 */

@Component(dependencies = arrayOf(ApplicationComponent::class))
@UiScope
interface AllContactsActivityComponent : UiComponent<AllContactsPresenter, AllContactsView> {
}