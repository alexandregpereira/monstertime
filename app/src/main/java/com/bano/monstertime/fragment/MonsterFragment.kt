package com.bano.monstertime.fragment

import android.app.Activity
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bano.monstertime.R
import com.bano.monstertime.adapter.BaseAdapter
import com.bano.monstertime.adapter.MonsterTimerAdapter
import com.bano.monstertime.databinding.FragmentMonsterBinding
import com.bano.monstertime.helper.MonsterHelper
import com.bano.monstertime.model.MonsterTimer
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

/**
 *
 * Created by Alexandre on 10/06/2017.
 */

class MonsterFragment : Fragment(), BaseAdapter.OnClickListener<MonsterTimer>, ChildEventListener {

    private val TAG: String = "MonsterFragment"

    private var mBinding : FragmentMonsterBinding? = null
    private var mAdapter: MonsterTimerAdapter = MonsterTimerAdapter(ArrayList<MonsterTimer>(), this)
    private var mListener : OnMonsterListener? = null
    private var mTotalTime: Long = 0

    interface OnMonsterListener{
        fun onMonsterStarted(monsterList: ArrayList<MonsterTimer>)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_monster, container, false)
        return mBinding?.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val database = FirebaseDatabase.getInstance()
        val mDataBaseReference = database.getReference("timer")
        mDataBaseReference.child(MonsterHelper.getUserId(context)).addChildEventListener(this)

        mBinding?.recyclerTimer?.setHasFixedSize(true)
        mBinding?.recyclerTimer?.layoutManager = LinearLayoutManager(context)
        mBinding?.recyclerTimer?.adapter = mAdapter
        mBinding?.btnStart?.setOnClickListener {mListener?.onMonsterStarted(mAdapter.items)}
    }

    fun getMonsterList() : ArrayList<MonsterTimer> {
        return mAdapter.items
    }

    override fun onClicked(item: MonsterTimer) {
        //TODO implement
    }

    override fun onCancelled(databaseError: DatabaseError?) {
        Log.w(TAG, "postComments:onCancelled", databaseError?.toException())
        Toast.makeText(context, "Failed to load comments.",
                Toast.LENGTH_SHORT).show()
    }

    override fun onChildMoved(dataSnapshot: DataSnapshot?, p1: String?) {
        //TODO implement
    }

    override fun onChildChanged(dataSnapshot: DataSnapshot?, p1: String?) {
        val monsterTimer = dataSnapshot?.getValue<MonsterTimer>(MonsterTimer::class.java) ?: return
        monsterTimer.idKey = dataSnapshot.key
        mAdapter.setItem(monsterTimer)
    }

    override fun onChildAdded(dataSnapshot: DataSnapshot?, p1: String?) {
        // A new comment has been added, add it to the displayed list
        val monsterTimer = dataSnapshot?.getValue<MonsterTimer>(MonsterTimer::class.java) ?: return
        monsterTimer.idKey = dataSnapshot.key
        mAdapter.setItem(monsterTimer)
        mTotalTime += monsterTimer.time
        changeTime(mTotalTime)
    }

    override fun onChildRemoved(dataSnapshot: DataSnapshot?) {
        val monsterTimer = dataSnapshot?.getValue<MonsterTimer>(MonsterTimer::class.java) ?: return
        monsterTimer.idKey = dataSnapshot.key
        mAdapter.remove(monsterTimer)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val activity = context as Activity?
        try {
            mListener = activity as OnMonsterListener
        } catch (e: ClassCastException) {
            throw ClassCastException("Activity must implement OnApiMonitorPlayerListener")
        }
    }

    private fun changeTime(timeInMillis: Long){
        val time: String = (timeInMillis / 1000).toString()
        mBinding?.txtTimer?.text = time
    }

    fun updateTimer(millisUntilFinished: Long) {
        mTotalTime -= 1000
        changeTime(mTotalTime)
    }

}
