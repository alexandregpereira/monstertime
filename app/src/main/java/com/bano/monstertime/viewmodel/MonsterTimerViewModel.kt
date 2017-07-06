package com.bano.monstertime.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.bano.monstertime.model.MonsterTimer

/**
 * Created by Alexandre on 05/07/2017.
 *
 */
class MonsterTimerViewModel: ViewModel() {
//    private val realmQuery: RealmQuery<MonsterTimer>
    val monsterTimersObservable = MutableLiveData<List<MonsterTimer>>()
    val timerObservable = MutableLiveData<String>()

    init {
//        realmQuery = realm.where(MonsterTimer::class.java)
    }

    fun loadTimers(monsterId: String): LiveData<List<MonsterTimer>>{
        if(monsterTimersObservable.value == null){
            monsterTimersObservable.value = listOf(MonsterTimer(), MonsterTimer())
        }

        return monsterTimersObservable
    }
}