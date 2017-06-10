package com.bano.monstertime.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bano.monstertime.R;
import com.bano.monstertime.adapter.MonsterTimerAdapter;
import com.bano.monstertime.databinding.ActivityMainBinding;
import com.bano.monstertime.helper.MosterTimerHelper;
import com.bano.monstertime.model.MonsterTimer;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Comment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDataBaseReference;
    private ActivityMainBinding mBinding;
    private String TAG = "MainActivity";
    private MonsterTimerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MonsterTimer monsterTimer = new MonsterTimer("teste2", 20000, 1);
                String key = mDataBaseReference.push().getKey();
                mDataBaseReference.child(MosterTimerHelper.getUserId(MainActivity.this))
                        .child(key).setValue(monsterTimer);

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDataBaseReference = database.getReference("timer");
        mDataBaseReference.child(MosterTimerHelper.getUserId(this)).addChildEventListener(childEventListener);

        mBinding.contentMain.recyclerTimer.setHasFixedSize(true);
        mBinding.contentMain.recyclerTimer.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MonsterTimerAdapter(new ArrayList<MonsterTimer>(), null);
        mBinding.contentMain.recyclerTimer.setAdapter(mAdapter);
    }

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
            Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

            // A new comment has been added, add it to the displayed list
            MonsterTimer monsterTimer = dataSnapshot.getValue(MonsterTimer.class);
            monsterTimer.setIdKey(dataSnapshot.getKey());
            mAdapter.setItem(monsterTimer);

            Log.d(TAG, "onChildAdded:" + monsterTimer.toString());
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

            // A comment has changed, use the key to determine if we are displaying this
            // comment and if so displayed the changed comment.
            MonsterTimer monsterTimer = dataSnapshot.getValue(MonsterTimer.class);
            monsterTimer.setIdKey(dataSnapshot.getKey());
            mAdapter.setItem(monsterTimer);
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

            // A comment has changed, use the key to determine if we are displaying this
            // comment and if so remove it.
            MonsterTimer monsterTimer = dataSnapshot.getValue(MonsterTimer.class);
            monsterTimer.setIdKey(dataSnapshot.getKey());
            mAdapter.remove(monsterTimer);
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

            // A comment has changed position, use the key to determine if we are
            // displaying this comment and if so move it.
            Comment movedComment = dataSnapshot.getValue(Comment.class);
            String commentKey = dataSnapshot.getKey();

            // ...
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w(TAG, "postComments:onCancelled", databaseError.toException());
            Toast.makeText(MainActivity.this, "Failed to load comments.",
                    Toast.LENGTH_SHORT).show();
        }
    };

}
