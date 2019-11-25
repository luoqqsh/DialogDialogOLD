package com.xing.hhplayer.common.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hhplayer.R;
import com.xing.hhplayer.common.HHPlayer3;
import com.xing.hhplayer.common.TvListBean.TvDataList;
import com.xing.hhplayer.common.TvListBean.TvListAdapter;

import java.util.ArrayList;
import java.util.List;

public class HHListView extends FrameLayout {
    private HHPlayer3 hhPlayer3;
    private List<TvDataList> tvDataList;
    private TvListAdapter tvListAdapter;
    private int currentSelectPlay = 0;
    private ListView playlistView;

    public HHListView(@NonNull Context context, List<TvDataList> tvDataList, HHPlayer3 hhPlayer3) {
        super(context);
        this.tvDataList = tvDataList;
        this.hhPlayer3 = hhPlayer3;
        init(context);
    }

    public HHListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HHListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(Context context) {
        if (tvDataList == null) {
            tvDataList = new ArrayList<>();
        }
        View view = View.inflate(context, R.layout.hhplayer_listview, this);
        playlistView = view.findViewById(R.id.lv_playlist);
        tvListAdapter = new TvListAdapter(tvDataList, context);
        playlistView.setBackgroundColor(0x99000000);
        playlistView.setAdapter(tvListAdapter);
        playlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //点击事件，电视上的话不存在这个
                currentSelectPlay = i;
                hhPlayer3.prepare(tvDataList.get(i).getChannelURLs().get(0).getChannelURL(), tvDataList.get(i).getChannelName());
            }
        });
    }

    //切换、刷新列表
    public void refreshList(List<TvDataList> tvDataList) {
        this.tvDataList = tvDataList;
        tvDataList.get(currentSelectPlay).setChannelSelect(false);
        currentSelectPlay = 0;
        tvDataList.get(currentSelectPlay).setChannelSelect(true);
        tvListAdapter.notifyDataSetChanged();
    }

    public int getListSize() {
        return tvDataList.size();
    }

    //获取当前选择的是哪一个，
    public int getCurrentSelect() {
        return currentSelectPlay;
    }

    //上下切换当前选中
    public void setCurrentSelectPlay(int currentSelectPlay) {
        tvDataList.get(this.currentSelectPlay).setChannelSelect(false);
        this.currentSelectPlay = currentSelectPlay;
        tvDataList.get(currentSelectPlay).setChannelSelect(true);
        playlistView.smoothScrollToPosition(currentSelectPlay);
        tvListAdapter.notifyDataSetChanged();
    }
}
