package x.com.hl_fileselect.media;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import x.com.hl_base.interfaces.HOnListener;
import x.com.hl_bean.CSelectType;
import x.com.hl_dialog.R;
import x.com.hl_log.ViseLog;
import x.com.hl_tools.hl_medialist;
import x.com.hl_tools.hl_permissions;
import x.com.hl_tools.hl_screen;

public class CMediaSelectDialog extends BaseSelectDialog {
    private RecyclerView nSelectItemList;
    private FileItemAdapter fileItemAdapter;
    private boolean isOpenFolder;
    private View nSelectMasking;
    private TextView nSelectFolderName;
    private FolderPopWindow folderPopWindow;

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
            mediaConfig.setMinSelectCount(minSelectCount);
            mediaConfig.setMaxSelectCount(maxSelectCount);
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

        public MediaSelectBuilder setOnSingleListener(MediaSelectListener<MediaFileBean> onSingleListener) {
            mediaConfig.setOnSingleListener(onSingleListener);
            return this;
        }

        public MediaSelectBuilder setShowCamera(boolean showCamera) {
            mediaConfig.setShowCamera(showCamera);
            return this;
        }

        public MediaSelectBuilder setNeedPreview(boolean needPreview) {
            mediaConfig.setNeedPreview(needPreview);
            return this;
        }

        public MediaSelectBuilder setOnMoreSelectListener(MediaSelectListener<List<MediaFileBean>> onMoreSelectListener) {
            mediaConfig.setOnMoreSelectListener(onMoreSelectListener);
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
            TextView nSelectTip = mView.findViewById(R.id.nSelectTip);
            nSelectTip.setText(mediaConfig.getTip());
            nSelectTip.setVisibility(View.VISIBLE);
        }
        mView.findViewById(R.id.nSelectBack).setOnClickListener(v -> dismiss());
        nSelectMasking = mView.findViewById(R.id.nSelectMasking);
        nSelectFolderName = mView.findViewById(R.id.nSelectFolderName);
        nSelectFolderName.setOnClickListener(v -> openFolder());
        mView.findViewById(R.id.nSelectConfirm).setOnClickListener(v -> {
            if (mediaConfig.callback()) dismiss();
        });
        nSelectItemList = mView.findViewById(R.id.nSelectItemList);
        fileItemAdapter = new FileItemAdapter(mActivitys.get(), mediaConfig, mFileList);
        fileItemAdapter.setBaseFragment(baseNDialogFragment);
        nSelectItemList.setAdapter(fileItemAdapter);
        if (hl_screen.isPortrait()) {
            GridLayoutManager mLayoutManager = new GridLayoutManager(mActivitys.get(), 3);
            nSelectItemList.setLayoutManager(mLayoutManager);
        } else {
            GridLayoutManager mLayoutManager = new GridLayoutManager(mActivitys.get(), 5);
            nSelectItemList.setLayoutManager(mLayoutManager);
        }
        initData();
    }

    private void initData() {
        hl_permissions.applyWriteStoragePermission(mActivitys.get(), new hl_permissions.RequestPermissionCallBack() {
            @Override
            public void requestPermissionSuccess() {
                switch (chooseType){
                    case CSelectType.images:
                        hl_medialist.getLocalImgFromLoader(mActivitys.get(), new HOnListener<Map<String, MediaFolderBean>>() {
                            @Override
                            public void onListen(Map<String, MediaFolderBean> mediaFolderMap) {
                                List<MediaFolderBean> mediaFolderBeanList = new ArrayList<>();
                                for (String key : mediaFolderMap.keySet()) {
                                    mediaFolderBeanList.add(mediaFolderMap.get(key));
                                }
                                initFolderList(mediaFolderBeanList);
                                mFileList = mediaFolderBeanList.get(0).getMediaFileBeanList();
                                fileItemAdapter.notifyDataSetChanged(mFileList);
                            }
                        });
                        break;
                    case CSelectType.videos:
                        hl_medialist.getLocalVideoFromLoader(mActivitys.get(), new HOnListener<Map<String, MediaFolderBean>>() {
                            @Override
                            public void onListen(Map<String, MediaFolderBean> mediaFolderMap) {
                                List<MediaFolderBean> mediaFolderBeanList = new ArrayList<>();
                                for (String key : mediaFolderMap.keySet()) {
                                    mediaFolderBeanList.add(mediaFolderMap.get(key));
                                }
                                initFolderList(mediaFolderBeanList);
                                mFileList = mediaFolderBeanList.get(0).getMediaFileBeanList();
                                fileItemAdapter.notifyDataSetChanged(mFileList);
                            }
                        });
                }
            }

            @Override
            public void requestPermissionFail(Map<String, Boolean> failPermission) {
                ViseLog.e("没有读写权限");
            }
        });
    }

    private void initFolderList(List<MediaFolderBean> mediaFolderBeanList) {
        if (mediaFolderBeanList != null && !mediaFolderBeanList.isEmpty()) {
            folderPopWindow = new FolderPopWindow(mActivitys.get(), mediaFolderBeanList, new HOnListener<MediaFolderBean>() {
                @Override
                public void onListen(MediaFolderBean mediaFolderBean) {
                    setFolder(mediaFolderBean);
                    closeFolder();
                }
            });
            folderPopWindow.setOnDismissListener(this::closeFolder);
        }
    }

    private void setFolder(MediaFolderBean folder) {
        mFileList = folder.getMediaFileBeanList();
        nSelectFolderName.setText(folder.getFolderName());
        nSelectItemList.scrollToPosition(0);
        fileItemAdapter.notifyDataSetChanged(folder.getMediaFileBeanList());
    }

    /**
     * 弹出文件夹列表
     */
    private void openFolder() {
        if (!isOpenFolder) {
            nSelectMasking.setVisibility(View.VISIBLE);
            isOpenFolder = true;
            folderPopWindow.showAsDropDown(nSelectFolderName, 0, 20);
        }
    }

    /**
     * 收起文件夹列表
     */
    private void closeFolder() {
        if (isOpenFolder) {
            nSelectMasking.setVisibility(View.GONE);
            isOpenFolder = false;
            folderPopWindow.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (fileItemAdapter != null) {
            fileItemAdapter.getCallbackPhoto();
        }
        dismiss();
    }
}
