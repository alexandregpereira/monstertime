package com.bano.monstertime.adapter;

import com.bano.monstertime.R;
import com.bano.monstertime.databinding.ItemTimerBinding;
import com.bano.monstertime.model.MonsterTimer;

import java.util.ArrayList;

/**
 *
 * Created by Alexandre on 10/06/2017.
 */

public class MonsterTimerAdapter extends BaseAdapter<MonsterTimer, ItemTimerBinding>{

    public MonsterTimerAdapter(ArrayList<MonsterTimer> items, OnClickListener<MonsterTimer> listener) {
        super(items, R.layout.item_timer, listener);
    }

    @Override
    protected void onBindViewHolder(ItemTimerBinding itemTimerBinding, MonsterTimer monsterTimer) {
        itemTimerBinding.txtTimer.setText(monsterTimer.getName());
    }
}
