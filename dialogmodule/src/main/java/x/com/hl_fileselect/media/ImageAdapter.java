package x.com.hl_fileselect.media;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import x.com.hl_bean.CSelectType;
import x.com.hl_dialog.R;
import x.com.hl_model.MediaFileBean;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private FragmentActivity mActivity;
    private MediaConfig mediaConfig;
    private List<MediaFileBean> mediaFileBeanList;

    public ImageAdapter(FragmentActivity mActivity, MediaConfig mediaConfig, List<MediaFileBean> mediaFileBeanList) {
        this.mActivity = mActivity;
        this.mediaConfig = mediaConfig;
        this.mediaFileBeanList = mediaFileBeanList;
        if (mediaConfig.isShowCamera()) {
            mediaFileBeanList.add(new MediaFileBean());
        }
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
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == CSelectType.images) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.item_select_img, parent, false);
            return new ViewHolder(view);
        } else if (viewType == CSelectType.photo) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.item_select_camera, parent, false);
            return new ViewHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        ImageView ivSelectIcon;
        ImageView ivMasking;
        ImageView ivCamera;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
            ivSelectIcon = itemView.findViewById(R.id.iv_select);
            ivMasking = itemView.findViewById(R.id.iv_masking);
            ivCamera = itemView.findViewById(R.id.iv_camera);
        }
    }

    public void selectAll() {

    }

    public void clearSelect() {

    }
}
