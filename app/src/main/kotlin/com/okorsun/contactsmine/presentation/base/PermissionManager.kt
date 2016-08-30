package com.okorsun.contactsmine.presentation.base

import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import com.okorsun.contactsmine.app.BaseException
import rx.Observable
import rx.subjects.PublishSubject

class PermissionManager {
    companion object {
        private val permissionResults = PublishSubject.create<Int>()
        var freeRequestCode = 0
            get() = field++

        private fun checkPermissions(permissions: Set<String>, view: PresenterView): Set<String> =
                permissions.filter {
                    ContextCompat.checkSelfPermission(view.appContext, it) == PackageManager.PERMISSION_DENIED
                }.toSet()
    }

    fun requestPermissions(permissions: Set<String>, views: Observable<PresenterView?>): Observable<PermissionObtained> =
            views.filter { view -> view != null }
                    .map { view -> view!! }
                    .switchMap { view ->

                        val missingPermissions = checkPermissions(permissions, view)

                        if (missingPermissions.isEmpty()) {
                            Observable.just(PermissionObtained)
                        } else {
                            val requestCode = freeRequestCode
                            view.requestTkPermissions(missingPermissions, requestCode)

                            permissionResults.switchMap {
                                receivedRequestCode ->

                                val deniedPermissions = checkPermissions(permissions, view)
                                if (deniedPermissions.isEmpty()) {
                                    Observable.just(PermissionObtained)
                                } else if (receivedRequestCode == requestCode) {
                                    Observable.error(MissingPermissionException(deniedPermissions))
                                } else {
                                    Observable.empty<PermissionObtained>()
                                }

                            }
                        }
                    }.take(1)

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionResults.onNext(requestCode)
    }
}

object PermissionObtained

class MissingPermissionException(val permissions: Set<String>) : BaseException("missing permissions " + permissions.joinToString())
