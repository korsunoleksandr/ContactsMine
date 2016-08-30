package com.okorsun.contactsmine.presentation.base

interface PresenterView : RouterView {
    fun attachRouter(router: Router<*>)
    fun detachRouter(router: Router<*>)

    fun requestTkPermissions(permissions: Set<String>, requestCode: Int)
}