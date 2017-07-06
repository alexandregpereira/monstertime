package com.bano.monstertime.model

import java.util.*

/**

 * Created by Alexandre on 10/06/2017.
 */

class MonsterTimer {
    var id: String = UUID.randomUUID().toString()
    var name: String = "MonsterTimer"
    var time: Long = 10000
    var position: Int = 0

    override fun toString(): String {
        return "MonsterTimer{" +
                "name='" + name + '\'' +
                ", time=" + time +
                ", position=" + position +
                '}'
    }
}
