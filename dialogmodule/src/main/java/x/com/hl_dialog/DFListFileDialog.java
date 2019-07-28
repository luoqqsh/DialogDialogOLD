package x.com.hl_dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;

import x.com.hl_base.BaseDialogFragment;
import x.com.hl_base.HOnListener;
import x.com.hl_fileselect.FileSelectorView;
import x.com.hl_tools.hl_file;

public class DFListFileDialog extends BaseDialogFragment {
    private TextView hl_list_path;
    private HOnListener<File> onSelectFileListener;
    private FileSelectorView fileSelectorView;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new NDialog(getActivity(), NDialog.nDialog_ListFile, 0.9f, true)
                .setTouchOutSideCancelable(true, true)
                .create();
        hl_list_path = dialog.findViewById(R.id.hl_list_path);
        fileSelectorView = dialog.findViewById(R.id.hl_list_file);
        if (getArguments() != null) {
            String path = getArguments().getString("path");
            if (path != null && hl_file.isFolderExist(path)) {
                fileSelectorView.setCurrentDirectory(new File(path));
            }
        }
        hl_list_path.setText("当前路径: " + fileSelectorView.getCurrentDirectory().getAbsolutePath());

        //设置文件过滤
        //fileSelectorView.setFileFilter(new FileExtendFilter(Arrays.asList("shp", "kml"))); // 设置过滤规则
        //fileSelectorView.setFileFilter(new FileContainsFieldsFilter(Arrays.asList("shp")));

       /* //自定义文件图标
        fileSelectorView.setFileIconFactory(new FileSelectorView.FileIconCreator() {
            public Drawable getIcon(File file) {
                if (file == null) {
                    return getResources().getDrawable(R.drawable.rotating);
                } else {
                    return getResources().getDrawable(R.drawable.layers3);
                }
            }
        });*/

        //自定义部分文件图标
//        fileSelectorView.setFileIconCreator(new FileSelectorView.FileIconCreator() {
//            @Override
//            public Drawable getIcon(File file) {
//                if (file == null) {
//                    return getResources().getDrawable(R.mipmap.layers3);
//                } else {
//                    return new DefaultFileIconCreator(getActivity()).getIcon(file); //走默认逻辑
//                }
//            }
//        });

        //设置选择文件的监听
        fileSelectorView.setOnFileSelectedListener(new FileSelectorView.OnFileSelectedListener() {
            @Override
            public void onSelected(File selectedFile) {
                Log.d("FileListDialog", selectedFile.getAbsolutePath());
                if (onSelectFileListener != null) {
                    onSelectFileListener.onListen(selectedFile);
                }
            }

            @Override
            public void onFilePathChanged(File file) {
                hl_list_path.setText("当前路径: " + file.getAbsolutePath());
            }
        });
        return dialog;

    }

    public static DFListFileDialog newInstance(String path) {
        DFListFileDialog fragment = new DFListFileDialog();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setOnSelectFileListener(HOnListener<File> onSelectFileListener) {
        this.onSelectFileListener = onSelectFileListener;
    }
}
