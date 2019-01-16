package x.com.hl_dialog;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import x.com.hl_tools.hl_file;
import x.com.hl_tools.hl_permissions;
import x.com.hl_tools.hl_app;
import x.com.hl_tools.hl_interface.DownListener;
import x.com.hl_tools.hl_net;

public class DFUpdateDialog extends DialogFragment {
    private final String[] mPermissionList = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    private String url;
    private TextView tv_status;
    private ProgressBar pb_prog;
    private LinearLayout dialog_btnlayout, ll_progress;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final String content = getArguments() != null ? getArguments().getString("content") : "";
        url = getArguments() != null ? getArguments().getString("url") : null;
        int isforce = getArguments() != null ? getArguments().getInt("isforce") : 0;
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
                        if (havepermission(getActivity()) == PackageManager.PERMISSION_GRANTED) {
                            startdownload(getActivity());
                        } else {
                            applypression();
                        }
                        break;
                }
            }
        });
        Dialog xdialog = nDialog.create();
        tv_status = xdialog.findViewById(R.id.tv_progress);
        View view = xdialog.findViewById(R.id.view);
        pb_prog = xdialog.findViewById(R.id.progress);
        dialog_btnlayout = xdialog.findViewById(R.id.dialog_btnlayout);
        ll_progress = xdialog.findViewById(R.id.ll_progress);

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

    public static DFUpdateDialog newInstance(String content, int isforce, String url) {
        DFUpdateDialog fragment = new DFUpdateDialog();
        Bundle bundle = new Bundle();
        bundle.putString("content", content);
        bundle.putString("url", url);
        bundle.putInt("isforce", isforce);
        fragment.setArguments(bundle);
        return fragment;
    }

    private int havepermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void applypression() {
        hl_permissions.getInstance().applyPermission(this, mPermissionList, new hl_permissions.RequestPermissionCallBack() {
            @Override
            public void requestPermissionSuccess() {
                startdownload(getActivity());
            }

            @Override
            public void requestPermissionFail(boolean isNoTip) {
                if (isNoTip) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, 2);
                } else {
                    Toast.makeText(getActivity(), "您已拒绝文件读写权限，无法下载更新包！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void startdownload(Context context) {
        if (url == null) {
            Toast.makeText(getActivity(), "下载地址错误！", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog_btnlayout.setVisibility(View.GONE);
        ll_progress.setVisibility(View.VISIBLE);
        final File parentFile = hl_file.getParentFile(context);
        String downloadfile = parentFile.getPath() + "/" + 3500 + hl_app.getVersionName(context, context.getPackageName()) + ".apk";
        hl_net.downloadfile(url, downloadfile, new DownListener() {
            @Override
            public void onsuccess(File file) {
                hl_app.installAPK(getActivity(), file);
                dialog_btnlayout.setVisibility(View.VISIBLE);
                ll_progress.setVisibility(View.GONE);
            }

            @Override
            public void faild(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                tv_status.setText(message);
            }

            @Override
            public void downloading(int progress) {
                tv_status.setText(progress + "%");
                pb_prog.setProgress(progress);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        hl_permissions.getInstance().requestPermissionsResult(requestCode, permissions, grantResults);
    }
}
