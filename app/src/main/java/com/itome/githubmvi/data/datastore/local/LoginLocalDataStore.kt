package com.itome.githubmvi.data.datastore.local

import com.itome.githubmvi.data.model.LoginData
import com.itome.githubmvi.data.model.User
import io.reactivex.Completable
import io.reactivex.Single
import io.realm.Realm
import io.realm.kotlin.where
import javax.inject.Inject

class LoginLocalDataStore @Inject constructor() {

    fun readAccessToken(): Single<String> {
        val realm = Realm.getDefaultInstance()
        val accessToken = realm.where<LoginData>().findFirst()?.accessToken ?: ""
        realm.close()
        return Single.just(accessToken)
    }

    fun saveAccessToken(accessToken: String): Completable {
        val realm = Realm.getDefaultInstance()
        val loginData = LoginData(accessToken = accessToken, loginUser = null)
        realm.executeTransaction { it.copyToRealm(loginData) }
        realm.close()
        return Completable.complete()
    }

    fun readLoginUser(): Single<User> {
        return Single.create {
            val realm = Realm.getDefaultInstance()
            val user = realm.where<LoginData>().findFirst()?.loginUser
            if (user != null) {
                it.onSuccess(realm.copyFromRealm(user))
            } else {
                it.onError(NullPointerException())
            }
            realm.close()
        }
    }

    fun saveLoginUser(user: User): Completable {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            val loginData = it.where<LoginData>().findFirst()!!
            loginData.loginUser = it.copyToRealmOrUpdate(user)
        }
        realm.close()
        return Completable.complete()
    }

    fun readLoginData(): Single<LoginData> {
        return Single.create {
            val realm = Realm.getDefaultInstance()
            val loginData = realm.where<LoginData>().findFirst()
            if (loginData != null) {
                it.onSuccess(realm.copyFromRealm(loginData))
            } else {
                it.onError(NullPointerException())
            }
            realm.close()
        }
    }
}