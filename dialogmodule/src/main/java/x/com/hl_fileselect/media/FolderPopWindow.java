package x.com.hl_fileselect.media;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import x.com.hl_base.interfaces.HOnListener;
import x.com.hl_base.pop.BaseNPop;
import x.com.hl_dialog.R;
import x.com.hl_tools.hl_screen;

public class FolderPopWindow extends BaseNPop {
    private RecyclerView popFolderList;

    public FolderPopWindow(FragmentActivity mActivity, List<MediaFolderBean> mediaFolderBeanList, HOnListener<MediaFolderBean> mediaFolderBeanHOnListener) {
        super(mActivity, ViewGroup.LayoutParams.MATCH_PARENT, (int) (hl_screen.getScreenHeight(mActivity) * 0.6));
        FolderItemAdapter folderItemAdapter = new FolderItemAdapter(getActivity(), mediaFolderBeanList);
        folderItemAdapter.setOnFolderSelectListener(mediaFolderBean -> {
            if (mediaFolderBeanHOnListener != null) {
                mediaFolderBeanHOnListener.onListen(mediaFolderBean);
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
