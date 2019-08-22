package x.com.hl_player.TvListBean

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import x.com.hl_dialog.R

class TvListAdapter(private val tv: MutableList<TvDataList>, private val context: Context) : BaseAdapter() {

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
            holder.textview = convertView1!!.findViewById(R.id.tv_tv)
            convertView1.tag = holder
        } else {
            holder = convertView1.tag as ViewHolder
        }

        holder.textview!!.text = (position + 1).toString() + ". " + tv[position].channelName
        return convertView1
    }

    internal inner class ViewHolder {
        var textview: TextView? = null
    }

}
