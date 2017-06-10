package com.bano.monstertime.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bano.monstertime.R
import com.bano.monstertime.adapter.BaseAdapter
import com.bano.monstertime.adapter.MonsterTimerAdapter
import com.bano.monstertime.databinding.ActivityMainBinding
import com.bano.monstertime.helper.MonsterHelper
import com.bano.monstertime.model.MonsterTimer
import com.bano.monstertime.service.MonsterService
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import org.w3c.dom.Comment
import java.util.*

class MainActivity : AppCompatActivity(), ServiceConnection, View.OnClickListener, BaseAdapter.OnClickListener<MonsterTimer> {

    private var mBinding: ActivityMainBinding? = null
    private val TAG = "MainActivity"
    private var mAdapter: MonsterTimerAdapter = MonsterTimerAdapter(ArrayList<MonsterTimer>(), this)
    private var mService: MonsterService? = null
    private var mBound: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        //        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //        fab.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View view) {
        //                MonsterTimer monsterTimer = new MonsterTimer("teste2", 20000, 1);
        //                String key = mDataBaseReference.push().getKey();
        //                mDataBaseReference.child(MonsterHelper.getUserId(MainActivity.this))
        //                        .child(key).setValue(monsterTimer);
        //
        //                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                        .setAction("Action", null).show();
        //            }
        //        });

        val database = FirebaseDatabase.getInstance()
        val mDataBaseReference = database.getReference("timer")
        mDataBaseReference.child(MonsterHelper.getUserId(this)).addChildEventListener(childEventListener)

        mBinding?.contentMain?.recyclerTimer?.setHasFixedSize(true)
        mBinding?.contentMain?.recyclerTimer?.layoutManager = LinearLayoutManager(this)
        mBinding?.contentMain?.recyclerTimer?.adapter = mAdapter
        mBinding?.contentMain?.btnStart?.setOnClickListener(this)
        mBinding?.contentMain?.btnStart?.visibility = View.INVISIBLE
    }

    override fun onStart() {
        super.onStart()
        // Bind to LocalService
        val intent = Intent(this, MonsterService::class.java)
        bindService(intent, this, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        // Unbind from the service
        if (mBound) {
            unbindService(this)
            mBound = false
        }
    }

    internal var childEventListener: ChildEventListener = object : ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
            Log.d(TAG, "onChildAdded:" + dataSnapshot.key)

            // A new comment has been added, add it to the displayed list
            val monsterTimer = dataSnapshot.getValue<MonsterTimer>(MonsterTimer::class.java)
            monsterTimer.idKey = dataSnapshot.key
            mAdapter.setItem(monsterTimer)

            Log.d(TAG, "onChildAdded:" + monsterTimer.toString())
        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String) {
            Log.d(TAG, "onChildChanged:" + dataSnapshot.key)

            // A comment has changed, use the key to determine if we are displaying this
            // comment and if so displayed the changed comment.
            val monsterTimer = dataSnapshot.getValue<MonsterTimer>(MonsterTimer::class.java)
            monsterTimer.idKey = dataSnapshot.key
            mAdapter.setItem(monsterTimer)
        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            Log.d(TAG, "onChildRemoved:" + dataSnapshot.key)

            // A comment has changed, use the key to determine if we are displaying this
            // comment and if so remove it.
            val monsterTimer = dataSnapshot.getValue<MonsterTimer>(MonsterTimer::class.java)
            monsterTimer.idKey = dataSnapshot.key
            mAdapter.remove(monsterTimer)
        }

        override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String) {
            Log.d(TAG, "onChildMoved:" + dataSnapshot.key)

            // A comment has changed position, use the key to determine if we are
            // displaying this comment and if so move it.
            val movedComment = dataSnapshot.getValue<Comment>(Comment::class.java)
            val commentKey = dataSnapshot.key

            // ...
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.w(TAG, "postComments:onCancelled", databaseError.toException())
            Toast.makeText(this@MainActivity, "Failed to load comments.",
                    Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(view: View) {
        mService?.startMonster(mAdapter.items)
    }

    override fun onClicked(monsterTimer: MonsterTimer) {

    }


    override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
        val binder = iBinder as MonsterService.LocalBinder
        mService = binder.service
        mService?.setMonsterTimerList(mAdapter?.items)
        mBound = true
        mBinding?.contentMain?.btnStart?.visibility = View.VISIBLE
    }

    override fun onServiceDisconnected(componentName: ComponentName) {
        mBound = false
    }
}
