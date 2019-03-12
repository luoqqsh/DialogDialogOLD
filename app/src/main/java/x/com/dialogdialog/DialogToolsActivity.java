package x.com.dialogdialog;


import android.os.Bundle;
import android.os.Environment;

import java.io.File;

import x.com.hl_tools.DialogTools.DownloadHelper;

public class DialogToolsActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogtools);
    }

    private void downloads() {
        //下载工具,不支持断点续传
        new DownloadHelper(this,
                "http://imtt.dd.qq.com/16891/337A49BBE7A8A0B42E2312893903BBB3.apk?fsname=com.coolsnow.screenshot_5.6.0_56000.apk",
                new DownloadHelper.DownloadCallBack() {
                    @Override
                    public void installCancel() {
                        //取消安装
                    }

                    @Override
                    public void downloadSuccess(File file) {
                        //下载成功
                    }

                    @Override
                    public void downloadFail() {
                        //下载失败
                    }
                })
                //设置是否强制升级
                .setIsForce(false)
                //配置通知参数
                .setNotificationShow(true, "下载通知标题", R.mipmap.ic_launcher)
                //配置弹框
                .setDialogShow(true, "取消安装", "立即安装")
                //配置下载路径
                .setSavePath(Environment.getExternalStorageDirectory() + "/" + "checkUp")
                //配置是否为版本升级，true则自动执行安装apk
                .setCheckUp(true)
                //配置存储地址 默认Environment.getExternalStorageDirectory() + "/" + "deanDownload";
                .setSavePath(Environment.getExternalStorageDirectory() + "/" + "deanDownload");
        //开启下载
        //.start();
        //停止下载
        DownloadHelper.cancelAll();
    }
}
