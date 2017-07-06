package com.bano.monstertime.fragment

import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.IBinder
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bano.goblin.adapter.BaseAdapter
import com.bano.monstertime.R
import com.bano.monstertime.adapter.MonsterTimerAdapter
import com.bano.monstertime.constant.KeysContract
import com.bano.monstertime.databinding.FragmentMonsterBinding
import com.bano.monstertime.helper.MonsterHelper
import com.bano.monstertime.model.MonsterTimer
import com.bano.monstertime.service.MonsterService
import com.bano.monstertime.viewmodel.MonsterTimerViewModel
import java.util.*

/**
 *
 * Created by Alexandre on 10/06/2017.
 */

class MonsterFragment : LifecycleFragment(), BaseAdapter.OnClickListener<MonsterTimer>, ServiceConnection {

    private val MONSTER_ID_KEY: String = "MONSTER_ID_KEY"

    private var mBinding : FragmentMonsterBinding? = null
    private var mAdapter: MonsterTimerAdapter = MonsterTimerAdapter(ArrayList<MonsterTimer>(), this)
    private var mMonsterId: String = UUID.randomUUID().toString()
    private var mService: MonsterService? = null
    private var mBound: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments != null){
            mMonsterId = arguments.getString(MONSTER_ID_KEY)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_monster, container, false)
        return mBinding?.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val model = ViewModelProviders.of(this).get(MonsterTimerViewModel::class.java)

        mBinding?.recyclerTimer?.setHasFixedSize(true)
        mBinding?.recyclerTimer?.layoutManager = LinearLayoutManager(context)
        mBinding?.recyclerTimer?.adapter = mAdapter
        mBinding?.btnStart?.setOnClickListener { mService?.startMonster(model) }

        observeMonsterTimers(model, mMonsterId)
        observeTimer(model)
    }

    private fun observeMonsterTimers(model: MonsterTimerViewModel, monsterId: String){
        model.loadTimers(monsterId).observe(this, Observer {
            if(it == null) return@Observer
            Log.d(KeysContract.TAG, "MonsterFragment observeMonsterTimers")
            mBinding?.txtTimer?.text = MonsterHelper.countTotalTimerInSeconds(it).toString()
            mAdapter.items = it
        })
    }

    override fun onClicked(item: MonsterTimer) {
        //TODO implement
    }

    private fun changeTime(time: String){
        mBinding?.txtTimer?.text = time
    }

    private fun observeTimer(model: MonsterTimerViewModel) {
        model.timerObservable.observe(this, Observer {
            if(it == null) return@Observer
            changeTime(it)
        })
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        mBound = false
    }

    override fun onServiceConnected(p0: ComponentName?, iBinder: IBinder?) {
        val binder = iBinder as MonsterService.LocalBinder
        mService = binder.service
        mBound = true
    }

    override fun onStart() {
        super.onStart()
        // Bind to LocalService
        val intent = Intent(context, MonsterService::class.java)
        activity.bindService(intent, this, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        // Unbind from the service
        if (mBound) {
            activity.unbindService(this)
            mBound = false
        }
    }
}
