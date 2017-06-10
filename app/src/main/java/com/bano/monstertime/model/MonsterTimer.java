package com.bano.monstertime.model;

/**
 *
 * Created by Alexandre on 10/06/2017.
 */

public class MonsterTimer {

    private String idKey;
    private String name;
    private long time;
    private int position;

    public MonsterTimer(){

    }

    public MonsterTimer(String name, long time, int position) {
        this.name = name;
        this.time = time;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MonsterTimer{" +
                "name='" + name + '\'' +
                ", time=" + time +
                ", position=" + position +
                '}';
    }

    public String getIdKey() {
        return idKey;
    }

    public void setIdKey(String idKey) {
        this.idKey = idKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MonsterTimer that = (MonsterTimer) o;

        return idKey != null ? idKey.equals(that.idKey) : that.idKey == null;

    }

    @Override
    public int hashCode() {
        return idKey != null ? idKey.hashCode() : 0;
    }
}
