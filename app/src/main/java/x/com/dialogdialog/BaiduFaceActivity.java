package x.com.dialogdialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.baidu.idl.main.facesdk.FaceAuth;
import com.baidu.idl.main.facesdk.FaceDetect;
import com.baidu.idl.main.facesdk.FaceInfo;
import com.baidu.idl.main.facesdk.callback.Callback;
import com.baidu.idl.main.facesdk.model.BDFaceImageInstance;
import com.baidu.idl.main.facesdk.model.BDFaceOcclusion;
import com.baidu.idl.main.facesdk.model.BDFaceSDKCommon;
import com.yanzhenjie.kalle.Kalle;
import com.yanzhenjie.kalle.KalleConfig;
import com.yanzhenjie.kalle.connect.http.LoggerInterceptor;
import com.yanzhenjie.kalle.simple.SimpleCallback;
import com.yanzhenjie.kalle.simple.SimpleResponse;

import java.io.FileNotFoundException;

import x.com.dialogdialog.Base.BaseActivity;
import x.com.dialogdialog.face.listener.SdkInitListener;
import x.com.dialogdialog.face.manager.FaceSDKManager;
import x.com.dialogdialog.face.model.SingleBaseConfig;
import x.com.dialogdialog.face.model.User;
import x.com.dialogdialog.face.utils.BitmapUtils;
import x.com.dialogdialog.face.utils.ImageUtils;
import x.com.dialogdialog.face.utils.ToastUtils;
import x.com.hl_log.ViseLog;
import x.com.hl_tools.hl_media;

public class BaiduFaceActivity extends BaseActivity {
    private FaceDetect faceDetect;
    private ImageView img1, img2;
    private FaceAuth faceAuth;
    private String token;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vchart);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);

        faceAuth = new FaceAuth();
        faceDetect = new FaceDetect();
        FaceSDKManager.getInstance().init(this, new SdkInitListener() {
            @Override
            public void initStart() {
                ViseLog.d("initStart");
            }

            @Override
            public void initLicenseSuccess() {
                ViseLog.d("initLicenseSuccess");
            }

            @Override
            public void initLicenseFail(int errorCode, String msg) {
                ViseLog.d(msg);
            }

            @Override
            public void initModelSuccess() {
                ViseLog.d("initModelSuccess");

            }

            @Override
            public void initModelFail(int errorCode, String msg) {
                ViseLog.d(msg);
            }
        });


    }


    public void select(View view) {
        hl_media.intentToGallery(this,1);
    }

    private byte[] firstFeature = new byte[512];

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && (data != null && data.getData() != null)) {
            Uri uri1 = ImageUtils.geturi(data, this);
            try {
                final Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri1));
                if (bitmap != null) {
                    img1.setImageBitmap(bitmap);
                    // 提取特征值
                    syncFeature(bitmap, firstFeature, 1, true);

                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 2 && (data != null && data.getData() != null)) {
            getToken();
        }
    }

    /**
     * bitmap -提取特征值
     *
     * @param bitmap
     * @param feature
     * @param index
     */

    private void syncFeature(final Bitmap bitmap, final byte[] feature, final int index, boolean isFromPhotoLibrary) {
        float ret = -1;
        BDFaceImageInstance rgbInstance = new BDFaceImageInstance(bitmap);
        FaceInfo[] faceInfos = FaceSDKManager.getInstance().getFaceDetect()
                .detect(BDFaceSDKCommon.DetectType.DETECT_VIS, rgbInstance);
        // 检测结果判断
        if (faceInfos != null && faceInfos.length > 0) {

            // 判断质量检测，针对模糊度、遮挡、角度
            if (qualityCheck(faceInfos[0], isFromPhotoLibrary)) {
                ret = FaceSDKManager.getInstance().getFaceFeature().feature(BDFaceSDKCommon.FeatureType.
                        BDFACE_FEATURE_TYPE_ID_PHOTO, rgbInstance, faceInfos[0].landmarks, feature);
                Log.i("qing", "ret:" + ret);

                if (ret == 128) {
                    ViseLog.e("图片" + index + "特征抽取成功");
                    BDFaceImageInstance myimgs = faceDetect.cropFace(rgbInstance, faceInfos[0].landmarks);
                    ViseLog.d(myimgs);
                    Bitmap testimg = BitmapUtils.getInstaceBmp(myimgs);
                    img2.setImageBitmap(testimg);
                } else if (ret == -100) {
                    ViseLog.e("未完成人脸比对，可能原因，图片1为空");
                } else if (ret == -101) {
                    ViseLog.e("未完成人脸比对，可能原因，图片2为空");
                } else if (ret == -102) {
                    ViseLog.e("未完成人脸比对，可能原因，图片1未检测到人脸");
                } else if (ret == -103) {
                    ViseLog.e("未完成人脸比对，可能原因，图片2未检测到人脸");
                } else {
                    ViseLog.e("未完成人脸比对，可能原因，"
                            + "人脸太小（小于sdk初始化设置的最小检测人脸）"
                            + "人脸不是朝上，sdk不能检测出人脸");
                }
            }

        } else {
            ViseLog.e("未检测到人脸,可能原因人脸太小");
        }

    }

    /**
     * 质量检测
     * FaceInfo faceInfo
     *
     * @return
     */
    public boolean qualityCheck(final FaceInfo faceInfo, boolean isFromPhotoLibrary) {

        // 不是相册选的图片，不必再次进行质量检测，因为采集图片的时候已经做过了
        if (!isFromPhotoLibrary) {
            return true;
        }

        if (!SingleBaseConfig.getBaseConfig().isQualityControl()) {
            return true;
        }

        if (faceInfo != null) {

            // 模糊结果过滤
            float blur = faceInfo.bluriness;
            if (blur > SingleBaseConfig.getBaseConfig().getBlur()) {
                ViseLog.e("图片模糊");
                return false;
            }

            // 光照结果过滤
            float illum = faceInfo.illum;
            if (illum < SingleBaseConfig.getBaseConfig().getIllumination()) {
                ViseLog.e("图片光照不通过");
                return false;
            }

            // 遮挡结果过滤
            if (faceInfo.occlusion != null) {
                BDFaceOcclusion occlusion = faceInfo.occlusion;

                if (occlusion.leftEye > SingleBaseConfig.getBaseConfig().getLeftEye()) {
                    // 左眼遮挡置信度
                    ViseLog.e("左眼遮挡");
                } else if (occlusion.rightEye > SingleBaseConfig.getBaseConfig().getRightEye()) {
                    // 右眼遮挡置信度
                    ViseLog.e("右眼遮挡");
                } else if (occlusion.nose > SingleBaseConfig.getBaseConfig().getNose()) {
                    // 鼻子遮挡置信度
                    ViseLog.e("鼻子遮挡");
                } else if (occlusion.mouth > SingleBaseConfig.getBaseConfig().getMouth()) {
                    // 嘴巴遮挡置信度
                    ViseLog.e("嘴巴遮挡");
                } else if (occlusion.leftCheek > SingleBaseConfig.getBaseConfig().getLeftCheek()) {
                    // 左脸遮挡置信度
                    ViseLog.e("左脸遮挡");
                } else if (occlusion.rightCheek > SingleBaseConfig.getBaseConfig().getRightCheek()) {
                    // 右脸遮挡置信度
                    ViseLog.e("右脸遮挡");
                } else if (occlusion.chin > SingleBaseConfig.getBaseConfig().getChinContour()) {
                    // 下巴遮挡置信度
                    ViseLog.e("下巴遮挡");
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public void auth(View view) {
        faceAuth.initLicenseOnLine(this, "P9IC-AUHF-YFPY-QLQV", new Callback() {
            @Override
            public void onResponse(final int code, final String response) {
                ToastUtils.toast(mContext, code + response);
                if (code == 0) {
                    FaceSDKManager.getInstance().initModel(mContext, new SdkInitListener() {
                        @Override
                        public void initStart() {
                            ViseLog.d("initstart");
                        }

                        @Override
                        public void initLicenseSuccess() {
                            ViseLog.d("initLicenseSuccess");
                        }

                        @Override
                        public void initLicenseFail(int errorCode, String msg) {
                            ToastUtils.toast(mContext, errorCode + msg);

                        }

                        @Override
                        public void initModelSuccess() {
                            ViseLog.d("initModelSuccess");
                        }

                        @Override
                        public void initModelFail(int errorCode, String msg) {

                        }
                    });
                }
            }
        });
    }


    /*********/////////////
    private void onLineTest(String token) {
        Kalle.post("https://aip.baidubce.com/rest/2.0/image-classify/v1/body_seg?access_token=" + token)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .param("image", "")
                .perform(new SimpleCallback<User>() {
                    @Override
                    public void onResponse(SimpleResponse<User, String> response) {
                        if (response.isSucceed()) {
                            ViseLog.d(response.succeed());
                        } else {
                            ViseLog.d(response.failed());
                        }
                    }
                });
    }

    private void getToken() {
        initKalle();
        token = "25.3b6bc331cf7fad1e46d389a68892d9b2.315360000.1882777054.282335-17162119";
        onLineTest(token);
//        Kalle.get("https://aip.baidubce.com/oauth/2.0/token")
//                .param("grant_type", "client_credentials")
//                .param("client_id", "aUEXdqAPTdUe8QFxv84hE5iM")
//                .param("client_secret", "ogMGTZtYbdL084TPpvQS3xUZtIDlhwZx")
//                .perform(new SimpleCallback<String>() {
//                    @Override
//                    public void onResponse(SimpleResponse<String, String> response) {
//                        onLineTest();
//                        if (response.isSucceed()) {
//                            ViseLog.d(response.succeed());
//                        } else {
//                            ViseLog.d(response.failed());
//                        }
//                    }
//                });
    }

    public void token(View view) {
        hl_media.intentToGallery(this,2);
    }

    private void initKalle() {
        Kalle.setConfig(KalleConfig.newBuilder()
                .addInterceptor(new LoggerInterceptor("KalleSample", BuildConfig.DEBUG))
                .build());
    }
}
