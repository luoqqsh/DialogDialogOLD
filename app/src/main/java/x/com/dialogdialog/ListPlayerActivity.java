package x.com.dialogdialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xing.hhplayer.common.HHPlayer3;
import com.xing.hhplayer.common.TvListBean.TvView;
import com.xing.hhplayer.common.base.HHMediaPlayer;

import java.util.ArrayList;
import java.util.List;

import x.com.hl_base.hl_activity.HLBaseActivity;

public class ListPlayerActivity extends HLBaseActivity {
    private RecyclerView lvVideo;
    private List<String> data;
    private MyAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    int playPosition = 0;

    @Override
    public void initUI(Bundle savedInstanceState) {
        lvVideo = findViewById(R.id.rcview);
        data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add("http://crk.momocdn.com/mv/DD/E6/DDE62798-C8F1-4526-ACC1-E2240328B12E20180406_h264.mp4");
        }
        mLayoutManager = new LinearLayoutManager(this);
        //2 为RecyclerView创建布局管理器，这里使用的是LinearLayoutManager，表示里面的Item排列是线性排列
        lvVideo.setLayoutManager(mLayoutManager);

        adapter = new MyAdapter();
        lvVideo.setAdapter(adapter);
        lvVideo.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                recyclerView.getBaseline();
                //direction为 -1 表示手指向下滑动（屏幕向上滑动）， 1 表示手指向上滑动（屏幕向下滑动）。
                if (mLayoutManager != null) {
                    int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();//得到显示屏内的第一个list的位置数position
                    View firstView = mLayoutManager.findViewByPosition(firstVisibleItem);
                    if (null != firstView) {
                        if (dy > 0) {


                            if (firstView.getHeight() + firstView.getTop() <= firstView.getHeight() / 3) {
                                //video stop or play second
                                if (mLayoutManager.getChildCount() < 2) {
                                    return;
                                }
                                if (playPosition == firstVisibleItem + 1) {
                                    return;
                                }
                                playPosition = firstVisibleItem + 1;
                                adapter.setPlay(playPosition);
                            } else {
                                if (playPosition == firstVisibleItem) {
                                    return;
                                }
                                playPosition = firstVisibleItem;
                                adapter.setPlay(playPosition);
                            }

                        }
                        if (dy < 0) {
                            if (firstView.getHeight() + firstView.getTop() >= firstView.getHeight() * 2 / 3) {
                                //video stop or play second
                                if (mLayoutManager.getChildCount() < 2) {
                                    return;
                                }
                                if (playPosition == firstVisibleItem) {
                                    return;
                                }
                                playPosition = firstVisibleItem;
                                adapter.setPlay(playPosition);
                            } else {
                                if (playPosition == firstVisibleItem + 1) {
                                    return;
                                }
                                playPosition = firstVisibleItem + 1;
                                adapter.setPlay(playPosition);
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_list;
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private int play = 0;

        public void setPlay(int play) {
            this.play = play;
            notifyDataSetChanged();
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //加载布局文件
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_videolist, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int position) {
            //将数据填充到具体的view中
            if (play == position) {
                viewHolder.ijkPlayer.prepare(data.get(position));
            } else {
                viewHolder.ijkPlayer.pause();
            }

        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            FrameLayout frameLayout;
            HHPlayer3 ijkPlayer;

            public ViewHolder(View itemView) {
                super(itemView);
                frameLayout = itemView.findViewById(R.id.videoLayout);
                ijkPlayer = new HHPlayer3(mActivity, new HHMediaPlayer(), TvView.NEWSURFACE, frameLayout);
                ijkPlayer.setPlayWhenPrepared(true);
            }
        }
    }

}
