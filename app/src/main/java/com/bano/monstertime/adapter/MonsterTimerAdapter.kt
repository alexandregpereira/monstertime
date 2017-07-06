package com.bano.monstertime.adapter

import com.bano.goblin.adapter.BaseAdapter
import com.bano.monstertime.R
import com.bano.monstertime.databinding.ItemTimerBinding
import com.bano.monstertime.model.MonsterTimer

/**

 * Created by Alexandre on 10/06/2017.
 */

class MonsterTimerAdapter(items: List<MonsterTimer>, listener: BaseAdapter.OnClickListener<MonsterTimer>) : BaseAdapter<MonsterTimer, ItemTimerBinding>(items, R.layout.item_timer, listener) {

    override fun onBindViewHolder(binding: ItemTimerBinding, item: MonsterTimer) {
        binding.txtTimer.text = item.name
    }
}
