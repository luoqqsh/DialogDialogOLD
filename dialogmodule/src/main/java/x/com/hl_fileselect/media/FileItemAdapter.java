package x.com.hl_fileselect.media;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import x.com.hl_bean.CSelectType;
import x.com.hl_dialog.R;
import x.com.hl_log.ViseLog;
import x.com.hl_tools.hl_img;
import x.com.hl_tools.hl_permissions;

public class FileItemAdapter extends RecyclerView.Adapter<FileItemAdapter.ViewHolder> {
    private FragmentActivity mActivity;
    private MediaConfig mediaConfig;
    private List<MediaFileBean> mediaFileBeanList;
    private ViewHolder singleSelectHolder;
    private Fragment baseFragment;
    private String mCameraImagePath;
    private Uri mCameraUri;

    public FileItemAdapter(FragmentActivity mActivity, MediaConfig mediaConfig, List<MediaFileBean> mediaFileBeanList) {
        this.mActivity = mActivity;
        this.mediaConfig = mediaConfig;
        this.mediaFileBeanList = mediaFileBeanList;
        if (mediaConfig.isShowCamera()) {
            mediaFileBeanList.add(new MediaFileBean());
        }
    }

    public void setBaseFragment(Fragment baseFragment) {
        this.baseFragment = baseFragment;
    }

    @Override
    public int getItemViewType(int position) {
        if (mediaConfig.isShowCamera() && position == 0) {
            return CSelectType.photo;
        } else {
            return CSelectType.images;
        }
    }

    @NonNull
    @Override
    public FileItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == CSelectType.images) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.item_select_img, parent, false);
            return new ViewHolder(view);
        } else if (viewType == CSelectType.photo) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.item_select_camera, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.item_select_img, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FileItemAdapter.ViewHolder holder, int position) {
        if (getItemViewType(position) == CSelectType.images) {
            MediaFileBean mediaFileBean = mediaFileBeanList.get(position);
            Glide.with(mActivity).load(mediaFileBean.getFilePath())
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                    .into(holder.sFileThumb);

            setItemSelect(holder, mediaConfig.getSelectList().contains(mediaFileBean));

            //点击选中/取消选中图片
            holder.sFileState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkedImage(holder, mediaFileBean);
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaConfig.isNeedPreview()) {//跳转到预览页面
                    } else {
                        checkedImage(holder, mediaFileBean);
                    }
                }
            });
        } else if (getItemViewType(position) == CSelectType.photo) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    takePhoto();
                }
            });
        }
    }

    /**
     * 设置图片选中和未选中的效果
     */
    private void setItemSelect(ViewHolder holder, boolean isSelect) {
        if (mediaConfig.isSingleSelect()) {
            if (singleSelectHolder != null) {
                singleSelectHolder.sFileState.setImageResource(R.mipmap.icon_image_un_select);
                singleSelectHolder.sFileMasking.setAlpha(0.2f);
            }
            singleSelectHolder = holder;
        }
        if (isSelect) {
            holder.sFileState.setImageResource(R.mipmap.icon_image_select);
            holder.sFileMasking.setAlpha(0.5f);
        } else {
            holder.sFileState.setImageResource(R.mipmap.icon_image_un_select);
            holder.sFileMasking.setAlpha(0.2f);
        }
    }

    /**
     * 选中 未选中
     */
    private void checkedImage(ViewHolder holder, MediaFileBean mediaFileBean) {
        if (mediaConfig.getSelectList().contains(mediaFileBean)) {
            //如果图片已经选中，就取消选中
            mediaConfig.getSelectList().remove(mediaFileBean);
            setItemSelect(holder, false);
        } else if (mediaConfig.isSingleSelect()) {
            //如果是单选，就先清空已经选中的图片，再选中当前图片
            mediaConfig.getSelectList().clear();
            mediaConfig.getSelectList().add(mediaFileBean);
            setItemSelect(holder, true);
        } else if (mediaConfig.getSelectList().size() <= mediaConfig.getMaxSelectCount()) {
            mediaConfig.getSelectList().add(mediaFileBean);
            setItemSelect(holder, true);
        }
    }

    @Override
    public int getItemCount() {
        return mediaFileBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView sFileThumb;
        ImageView sFileState;
        ImageView sFileMasking;
        ImageView sFileCamera;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sFileThumb = itemView.findViewById(R.id.sFileThumb);
            sFileState = itemView.findViewById(R.id.sFileState);
            sFileMasking = itemView.findViewById(R.id.sFileMasking);
            sFileCamera = itemView.findViewById(R.id.sFileCamera);
        }
    }

    public void selectAll() {
        clearSelect();
        mediaConfig.getSelectList().addAll(mediaFileBeanList);
    }

    public void clearSelect() {
        mediaConfig.getSelectList().clear();
    }

    public void notifyDataSetChanged(List<MediaFileBean> mediaFileBeanList) {
        this.mediaFileBeanList = mediaFileBeanList;
        if (mediaConfig.isShowCamera()) {
            mediaFileBeanList.add(new MediaFileBean());
        }
    }

    private void takePhoto() {
        hl_permissions.applyCameraPermission(mActivity, new hl_permissions.RequestPermissionCallBack() {
            @Override
            public void requestPermissionSuccess() {
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (captureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
                    File photoFile = null;
                    Uri photoUri = null;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        photoUri = hl_img.createImagePathUri();
                    } else {
                        try {
                            photoFile = hl_img.createImageFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (photoFile != null) {
                            mCameraImagePath = photoFile.getAbsolutePath();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                //通过FileProvider创建一个content类型的Uri
                                photoUri = FileProvider.getUriForFile(mActivity, mActivity.getPackageName() + ".dialog.fileProvider", photoFile);
                            } else {
                                photoUri = Uri.fromFile(photoFile);
                            }
                        }
                    }

                    mCameraUri = photoUri;
                    if (photoUri != null) {
                        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        baseFragment.startActivityForResult(captureIntent, 1);
                    }
                }
            }

            @Override
            public void requestPermissionFail(Map<String, Boolean> failPermission) {

            }
        });
    }

    public void getCallbackPhoto() {
        MediaFileBean mediaFileBean = new MediaFileBean();
        mediaFileBean.setFilePath(mCameraImagePath);
        mediaFileBean.setFilePathUri(mCameraUri);
        mediaConfig.getSelectList().add(mediaFileBean);
        mediaConfig.callback();
    }
}
