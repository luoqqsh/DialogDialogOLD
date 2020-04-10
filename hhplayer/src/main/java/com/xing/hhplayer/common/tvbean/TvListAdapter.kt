package com.xing.hhplayer.common.tvbean

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.hhplayer.R

class TvListAdapter(private val tv: MutableList<TvDataList>, private val context: Context) : BaseAdapter() {
    private lateinit var holder: ViewHolder
    override fun getCount(): Int {
        return tv.size
    }

    override fun getItem(position: Int): Any {
        return tv[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView1 = convertView
        val holder: ViewHolder
        if (convertView1 == null) {
            convertView1 = View.inflate(context, R.layout.item_tv, null)
            holder = ViewHolder()
            holder.itemChannelText = convertView1!!.findViewById(R.id.itemChannelText)
            convertView1.tag = holder
        } else {
            holder = convertView1.tag as ViewHolder
        }
        this.holder = holder
        holder.itemChannelText!!.text = (position + 1).toString() + ". " + tv[position].channelName
        if (tv[position].isChannelSelect!!) {
            holder.itemChannelText!!.setBackgroundResource(R.drawable.bg_conner_kuang)
        } else {
            holder.itemChannelText!!.background = null
        }
        return convertView1
    }

    internal inner class ViewHolder {
        var itemChannelText: TextView? = null
    }
}
