package x.com.hl_fileselect.media;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import x.com.hl_bean.CSelectType;
import x.com.hl_dialog.R;
import x.com.hl_log.ViseLog;
import x.com.hl_model.MediaFileBean;
import x.com.hl_rx.task.TaskManager;
import x.com.hl_tools.hl_medialist;
import x.com.hl_tools.hl_screen;

public class CMediaSelectDialog extends BaseSelectDialog {

    private RecyclerView nListView;
    private ImageAdapter imageAdapter;

    public static MediaSelectBuilder builder() {
        return new MediaSelectBuilder();
    }

    public static class MediaSelectBuilder {
        private MediaConfig mediaConfig;

        private MediaSelectBuilder() {
            mediaConfig = new MediaConfig();
        }

        //单选两个值都传1，
        public MediaSelectBuilder setSelectCount(int minSelectCount, int maxSelectCount) {
            if (minSelectCount <= maxSelectCount) {
                mediaConfig.setMinSelectCount(minSelectCount);
                mediaConfig.setMaxSelectCount(maxSelectCount);
            } else {
                ViseLog.e("传值错误，默认单选");
            }
            return this;
        }

        public MediaSelectBuilder setTip(String tip) {
            mediaConfig.setTip(tip);
            return this;
        }

        public MediaSelectBuilder setSelectList(List<MediaFileBean> mDataList) {
            mediaConfig.setSelectList(mDataList);
            return this;
        }

        public void startSelectImg(FragmentActivity mActivity) {
            CMediaSelectDialog cMediaSelectDialog = new CMediaSelectDialog();
            cMediaSelectDialog.setMediaConfig(mediaConfig);
            cMediaSelectDialog.setChooseType(CSelectType.images);
            cMediaSelectDialog.showDialog(mActivity);
        }

        public void startSelectVideo(FragmentActivity mActivity) {
            CMediaSelectDialog cMediaSelectDialog = new CMediaSelectDialog();
            cMediaSelectDialog.setMediaConfig(mediaConfig);
            cMediaSelectDialog.setChooseType(CSelectType.videos);
            cMediaSelectDialog.showDialog(mActivity);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ndialog_select;
    }

    @Override
    public float getWidthScale() {
        return 1;
    }

    @Override
    protected void initUI(View mView, Bundle bundle) {
        if (!TextUtils.isEmpty(mediaConfig.getTip())) {
            TextView nDialogTip = mView.findViewById(R.id.nDialogTip);
            nDialogTip.setText(mediaConfig.getTip());
        }
        mView.findViewById(R.id.btn_back).setOnClickListener(v -> dismiss());
        mView.findViewById(R.id.tv_confirm).setOnClickListener(v -> {
            if (mediaConfig.callback()) dismiss();
        });
        nListView = mView.findViewById(R.id.nDialogList);
        if (hl_screen.isPortrait()) {
            GridLayoutManager mLayoutManager = new GridLayoutManager(mActivitys.get(), 3);
            nListView.setLayoutManager(mLayoutManager);
        } else {
            GridLayoutManager mLayoutManager = new GridLayoutManager(mActivitys.get(), 5);
            nListView.setLayoutManager(mLayoutManager);
        }
        imageAdapter = new ImageAdapter(mActivitys.get(), mediaConfig, mDataList);
        nListView.setAdapter(imageAdapter);
        initData();
    }

    private void initData() {
        new TaskManager<List<MediaFileBean>>() {
            @Override
            public List<MediaFileBean> runOnBackgroundThread() {
                if (mDataList == null) {
                    mDataList = new ArrayList<>();
                } else {
                    mDataList.clear();
                }
                mDataList = hl_medialist.getImgList(mActivitys.get());
                return mDataList;
            }

            @Override
            public void runOnUIThread(@Nullable List<MediaFileBean> mediaFileBeanList) {
                imageAdapter.notifyDataSetChanged();
            }
        }.start();
    }
}
