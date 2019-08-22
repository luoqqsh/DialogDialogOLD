package x.com.hl_player.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import x.com.hl_dialog.R;
import x.com.hl_player.HHPlayer3;
import x.com.hl_player.TvListBean.TvDataList;
import x.com.hl_player.TvListBean.TvListAdapter;

public class HHListView extends FrameLayout {
    private HHPlayer3 hhPlayer3;
    private List<TvDataList> tvDataList;
    private TvListAdapter tvListAdapter;


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
        View view = View.inflate(context, R.layout.player_listview, this);
        ListView playlistView = view.findViewById(R.id.lv_playlist);
        tvListAdapter = new TvListAdapter(tvDataList, context);
        playlistView.setBackgroundColor(0x99000000);
        playlistView.setAdapter(tvListAdapter);
        playlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                hhPlayer3.prepare(tvDataList.get(i).getChannelURLs().get(0).getChannelURL(), tvDataList.get(i).getChannelName());
            }
        });
    }

    public void refreshList(List<TvDataList> tvDataList) {
        this.tvDataList = tvDataList;
        tvListAdapter.notifyDataSetChanged();
    }
}
