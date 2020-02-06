package x.com.hl_base;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import x.com.hl_dialog.R;
import x.com.hl_model.MediaFileBean;

public class HLChooseMediaActivity extends HLBaseActivity {
    protected List<MediaFileBean> mDataList = new ArrayList<>();//数据回调
    protected List<MediaFileBean> mMainDataList = new ArrayList<>();//数据源
    private RecyclerView chooseFileList;
    private FrameLayout chooseFileBottom;

    @Override
    public void initUI(Bundle savedInstanceState) {
        chooseFileList = findViewById(R.id.chooseFileList);
        chooseFileBottom = findViewById(R.id.chooseFileBottom);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_media;
    }
}
