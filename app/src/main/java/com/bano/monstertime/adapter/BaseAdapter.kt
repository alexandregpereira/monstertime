package com.bano.monstertime.adapter

import android.content.Context
import android.content.res.Resources
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import java.util.*

/**

 * Created by Alexandre on 13/05/2017.
 */

abstract class BaseAdapter<T, E : ViewDataBinding> : RecyclerView.Adapter<BaseAdapter.ViewHolder<T, E>> {

    internal val layoutRes: Int
    protected val listener: OnClickListener<T>
    val resources: Resources?
    private var mItems: ArrayList<T>

    abstract fun onBindViewHolder(binding: E, item: T)

    interface OnClickListener<in T> {
        fun onClicked(t: T)
    }

    constructor(items: ArrayList<T>, layoutRes: Int, listener: OnClickListener<T>) {
        this.layoutRes = layoutRes
        mItems = items
        this.listener = listener
        resources = null
    }

    constructor(context: Context, items: ArrayList<T>, layoutRes: Int, listener: OnClickListener<T>) {
        this.layoutRes = layoutRes
        mItems = items
        this.listener = listener
        resources = context.resources
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter.ViewHolder<T, E> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<E>(layoutInflater, layoutRes, parent, false)
        return BaseAdapter.ViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: BaseAdapter.ViewHolder<T, E>, position: Int) {
        val t = mItems[position]
        holder.binding.root.tag = t
        this.onBindViewHolder(holder.binding, t)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: ViewHolder<T, E>, position: Int, payloads: List<Any>?) {
        if (payloads != null && !payloads.isEmpty()) {
            // update the specific view
            val t = payloads[0] as T
            holder.binding.root.tag = t
            this.onBindViewHolder(holder.binding, t)
        } else {
            // I have already overridden  the other onBindViewHolder(ViewHolder, int)
            // The method with 3 arguments is being called before the method with 2 args.
            // so calling super will call that method with 2 arguments.
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    fun replace(t: T) {
        val i = mItems.indexOf(t)
        if (i >= 0) {
            mItems[i] = t
            notifyItemChanged(i, t)
        }
    }

    fun remove(t: T) {
        mItems.remove(t)
        notifyDataSetChanged()
    }

    fun setItem(t: T) {
        val i = mItems.indexOf(t)
        if (i >= 0) {
            mItems[i] = t
            notifyItemChanged(i, t)
        } else {
            this.mItems.add(t)
            notifyDataSetChanged()
        }
    }

    var items: ArrayList<T>
        get() = mItems
        set(items) {
            this.mItems = items
            notifyDataSetChanged()
        }

    class ViewHolder<T, out E : ViewDataBinding>(val binding: E, listener: OnClickListener<T>?) : RecyclerView.ViewHolder(binding.root) {
        init {
            if (listener != null) {
                binding.root.setOnClickListener { view ->
                    @Suppress("UNCHECKED_CAST")
                    val t = view.tag as T
                    listener.onClicked(t)
                }
            }
        }
    }
}
