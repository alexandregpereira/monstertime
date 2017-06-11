package com.bano.monstertime.model

/**

 * Created by Alexandre on 10/06/2017.
 */

class MonsterTimer {

    var idKey: String? = null
    var name: String? = null
    var time: Long = 0
    var position: Int = 0

    override fun toString(): String {
        return "MonsterTimer{" +
                "name='" + name + '\'' +
                ", time=" + time +
                ", position=" + position +
                '}'
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val that = other as MonsterTimer?

        return if (idKey != null) idKey == that!!.idKey else that!!.idKey == null

    }

    override fun hashCode(): Int {
        return if (idKey != null) idKey!!.hashCode() else 0
    }
}
