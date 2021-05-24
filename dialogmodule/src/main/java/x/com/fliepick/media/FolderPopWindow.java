package x.com.fliepick.media;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import x.com.base.interfaces.HOnListener;
import x.com.base.pop.BaseNPop;
import x.com.dialog.R;
import x.com.fliepick.bean.FolderBean;
import x.com.util.U_screen;

public class FolderPopWindow extends BaseNPop {
    private RecyclerView popFolderList;

    public FolderPopWindow(FragmentActivity mActivity, List<FolderBean> folderBeanList, HOnListener<FolderBean> mediaFolderBeanHOnListener) {
        super(mActivity, ViewGroup.LayoutParams.MATCH_PARENT, (int) (U_screen.getScreenHeight(mActivity) * 0.6));
        FolderItemAdapter folderItemAdapter = new FolderItemAdapter(getActivity(), folderBeanList);
        folderItemAdapter.setOnFolderSelectListener(folderBean -> {
            if (mediaFolderBeanHOnListener != null) {
                mediaFolderBeanHOnListener.onListen(folderBean);
            }
        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        popFolderList.setLayoutManager(mLayoutManager);
        popFolderList.setAdapter(folderItemAdapter);
    }

    @Override
    protected void initUI(View layout) {
        popFolderList = layout.findViewById(R.id.popFolderList);
    }

    @Override
    public View getLayout(Context context) {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.pop_select_folder;
    }
}
