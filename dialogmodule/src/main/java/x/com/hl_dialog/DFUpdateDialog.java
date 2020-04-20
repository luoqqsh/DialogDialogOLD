package x.com.hl_dialog;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.Map;

import x.com.hl_base.dialog.BaseDialogFragment;
import x.com.hl_base.dialog.NDialog;
import x.com.hl_rx.download.DownloadListener;
import x.com.hl_rx.download.FileDownloadManager;
import x.com.hl_tools.hl_app;
import x.com.hl_tools.hl_file;
import x.com.hl_tools.hl_permissions;
import x.com.hl_tools.hl_permissionsOther;

public class DFUpdateDialog extends BaseDialogFragment {
    private final String[] mPermissionList = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    private String url;
    private TextView nDialogProgressText;
    private ProgressBar nDialogProgress;
    private LinearLayout nDialogBtnLayout, nDialogProgressLayout;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final String content = getArguments() != null ? getArguments().getString("content") : "";
        url = getArguments() != null ? getArguments().getString("url") : null;
        int isforce = getArguments() != null ? getArguments().getInt("isForce") : 0;
        NDialog nDialog = new NDialog(getActivity(), NDialog.nDialog_Update, 1.0f)
                .setTouchOutSideCancelable(false, false)
                .setMessage(content, NDialog.MSG_LAYOUT_CENTER);
        nDialog.setBtnClickListener(false, isforce == 1 ? null : "下次再说", "立即更新", new NDialog.onDialogbtnClickListener() {
            @Override
            public void onDialogbtnClick(Context context, Dialog dialog, int whichBtn) {
                switch (whichBtn) {
                    case 1://下次再说
                        dismiss();
                        break;
                    case 2:
                        if (hasPermission(getActivity()) == PackageManager.PERMISSION_GRANTED) {
                            startDownload(getActivity());
                        } else {
                            applyPermission();
                        }
                        break;
                }
            }
        });
        Dialog xdialog = nDialog.create();
        nDialogProgressText = xdialog.findViewById(R.id.nDialogProgressText);
        View view = xdialog.findViewById(R.id.view);
        nDialogProgress = xdialog.findViewById(R.id.nDialogProgress);
        nDialogBtnLayout = xdialog.findViewById(R.id.nDialogBtnLayout);
        nDialogProgressLayout = xdialog.findViewById(R.id.nDialogProgressLayout);

        if (isforce == 1) {
            view.setVisibility(View.GONE);
            nDialog.setTouchOutSideCancelable(false, false);
            xdialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
            });

        } else {
            view.setVisibility(View.VISIBLE);
            nDialog.setTouchOutSideCancelable(false, true);
        }
        return xdialog;
    }

    public static DFUpdateDialog newInstance(String content, int isForce, String url) {
        DFUpdateDialog fragment = new DFUpdateDialog();
        Bundle bundle = new Bundle();
        bundle.putString("content", content);
        bundle.putString("url", url);
        bundle.putInt("isForce", isForce);
        fragment.setArguments(bundle);
        return fragment;
    }

    private int hasPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void applyPermission() {
        hl_permissions.applyWriteStoragePermission(getContext(), new hl_permissions.RequestPermissionCallBack() {
            @Override
            public void requestPermissionSuccess() {
                startDownload(getActivity());
            }

            @Override
            public void requestPermissionFail(Map<String, Boolean> failPermission) {
                for (boolean value : failPermission.values()) {
                    if (value) {
                        hl_permissionsOther.gotoAppSetPage(getActivity());
                        Toast.makeText(getActivity(), "您已拒绝文件读写权限，无法下载更新包！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Toast.makeText(getActivity(), "您已拒绝文件读写权限，无法下载更新包！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startDownload(Context context) {
        if (url == null) {
            Toast.makeText(getActivity(), "下载地址错误！", Toast.LENGTH_SHORT).show();
            return;
        }
        nDialogBtnLayout.setVisibility(View.GONE);
        nDialogProgressLayout.setVisibility(View.VISIBLE);
        final File parentFile = new File(hl_file.getAppCachePath(context));
        String downloadfile = parentFile.getPath() + "/" + 3500 + hl_app.getVersionName(context, context.getPackageName()) + ".apk";
        FileDownloadManager.getInstance(context).setDownloadListener(new DownloadListener() {
            @Override
            public void onDownLoadStart(int startProgress) {
                nDialogProgress.setProgress(startProgress);
                nDialogProgressText.setText(startProgress + "%");
            }

            @Override
            public void onDownLoadProgress(int progress) {
                nDialogProgressText.setText(progress + "%");
                nDialogProgress.setProgress(progress);
            }

            @Override
            public void onDownLoadError(String error) {
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                nDialogProgressText.setText(error);
            }

            @Override
            public void onDownloadFinish(String filePath) {
                hl_app.installAPK(getActivity(), filePath);
                nDialogBtnLayout.setVisibility(View.VISIBLE);
                nDialogProgressLayout.setVisibility(View.GONE);
            }
        }).startDownload(url, downloadfile);
    }
}
