package x.com.dialogdialog;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSeekBar;

import com.xing.hhplayer.common.HHPlayer3;
import com.xing.hhplayer.common.tvbean.TvData;
import com.xing.hhplayer.common.tvbean.TvDataList;
import com.xing.hhplayer.common.tvbean.TvView;
import com.xing.hhplayer.common.tvbean.TypeClarity;
import com.xing.hhplayer.common.tvbean.TypeClass;
import com.xing.hhplayer.common.tvbean.TypeState;
import com.xing.hhplayer.common.base.OnPlayerListener;
import com.xing.hhplayer.ijk.HHijkPlayer;

import java.util.ArrayList;

import x.com.dialogdialog.BaseApp.BaseActivity;
import x.com.hl_gl.shader.VignetteEffect;
import x.com.hl_log.ViseLog;
import x.com.hl_tools.hl_screen;
import x.com.hl_tools.hl_time;

public class HHMedia3Activity extends BaseActivity {
    private HHPlayer3 hhPlayer3;
    private FrameLayout hhview;
    //  /storage/emulated/0/Music/虫儿飞 - 郑伊健.mp3
//  /storage/emulated/0/Music/炽热的青春 - 贾乃亮.mp3
//  /storage/emulated/0/Music/齿轮 - 赵薇.mp3
//  /storage/emulated/0/Music/痴心绝对 - 李圣杰.mp3
//  /storage/emulated/0/Music/城门 - 陈浩民.mp3
//  /storage/emulated/0/Music/车站 - 张秀卿.mp3
//  /storage/emulated/0/Music/常回家看看 - 陈红.mp3
    //----------------------------------------------------------
    private boolean ismute = false;
    private TextView currenttime, totaltime;
    private AppCompatSeekBar seekBar, seekbar_volume;
    private TvData tvUrl;
    private ImageView imgvTest;

    @Override
    public void doBeforeCreate() {
        super.doBeforeCreate();
        ViseLog.d("oncreate");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_mediaview;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        ViseLog.d("initUI");
        initList();
        initUI();
        hhview = findViewById(R.id.hhPlayerView);
        hl_screen.setViewSize(hhview, ViewGroup.LayoutParams.MATCH_PARENT, hl_screen.getHeightFromWidthAtPort(mActivity));
        hhPlayer3 = new HHPlayer3(this, new HHijkPlayer(), TvView.HHGLSURFACE, hhview);
        hhPlayer3.addControlView();
        hhPlayer3.setOnPreparedListener(new OnPlayerListener<Object>() {
            @Override
            public void onListen(Object o) {
                hhPlayer3.start();
                seekbar_volume.setProgress((int) (hhPlayer3.getVolume() * 10));
                totaltime.setText(" / " + hl_time.formatTime(hhPlayer3.getMediaDurationMS()));
                seekBar.setMax((int) (hhPlayer3.getMediaDurationMS() / 1000));
            }
        });
        hhPlayer3.setOnProgressListener(new OnPlayerListener<Long>() {
            @Override
            public void onListen(Long time) {//当前是timer线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currenttime.setText(hl_time.formatTime(time));
                        seekBar.setProgress((int) (time / 1000));
                    }
                });
            }
        });
        hhPlayer3.setOnBufferingUpdateListener(new OnPlayerListener<Integer>() {
            @Override
            public void onListen(Integer integer) {
                seekBar.setSecondaryProgress(integer);
            }
        });
    }

    private void initList() {
        TvDataList tvDataList = new TvDataList(TypeClass.厦门台, "厦门电视台1", "http://cstvpull.live.wscdns.com/live/xiamen1.flv", TypeClarity.H4k, TypeState.流畅);
        TvDataList tvDataList2 = new TvDataList(TypeClass.厦门台, "厦门电视台2", "http://cstvpull.live.wscdns.com/live/xiamen2.flv", TypeClarity.H4k, TypeState.流畅);
        TvDataList tvDataList3 = new TvDataList(TypeClass.厦门台, "厦门电视台3", "http://cstvpull.live.wscdns.com/live/xiamen3.flv", TypeClarity.H4k, TypeState.流畅);
        TvDataList tvDataList4 = new TvDataList(TypeClass.厦门台, "厦门电视台4", "http://cstvpull.live.wscdns.com/live/xiamen4.flv", TypeClarity.H4k, TypeState.流畅);
        TvDataList tvDataList5 = new TvDataList(TypeClass.厦门台, "厦门卫视", "http://cstvpull.live.wscdns.com/live/xiamen.flv", TypeClarity.H4k, TypeState.流畅);
        TvDataList tvDataList6 = new TvDataList(TypeClass.厦门台, "厦门移动电视", "http://cstvpull.live.wscdns.com/live/xiamenyidong.flv", TypeClarity.H4k, TypeState.流畅);
        ArrayList<TvDataList> tvDataLists = new ArrayList<>();
        tvDataLists.add(tvDataList);
        tvDataLists.add(tvDataList2);
        tvDataLists.add(tvDataList3);
        tvDataLists.add(tvDataList4);
        tvDataLists.add(tvDataList5);
        tvDataLists.add(tvDataList6);
        tvUrl = new TvData(1, "20190719", "itv", tvDataLists);
    }

    float i = 0f;

    private void initUI() {
        fixRotateBug26();
        startOriListener();
        currenttime = findViewById(R.id.testCurrentTime);
        totaltime = findViewById(R.id.testTotalTime);
        seekbar_volume = findViewById(R.id.testSeekVolume);
        seekBar = findViewById(R.id.testSeekProgress);
        seekBar.setMax(100);
        seekbar_volume.setMax(10);

        findViewById(R.id.testSelect).setOnClickListener(v -> {
//            hhPlayer3.prepare(tvUrl.getLinks(), 0);
            hhPlayer3.prepare("http://cstvpull.live.wscdns.com/live/xiamen1.flv");
//            hhPlayer3.prepare("http://s.bizhijingling.com/uploadfile/coustom/2019/309/20190613170125729.mp4");
//            List<MediaFileBean> imgList = hl_medialist.getVideoList(mContext);
//            hhPlayer3.prepare("content://media/external/video/media/27", "");
        });

        findViewById(R.id.testPlay).setOnClickListener(v -> hhPlayer3.start());
        findViewById(R.id.testPause).setOnClickListener(v -> hhPlayer3.pause());
        findViewById(R.id.testStop).setOnClickListener(v -> hhPlayer3.stop());
        findViewById(R.id.testReset).setOnClickListener(v -> {
            hhPlayer3.resetPlayer();
            hhPlayer3.prepare("http://s.bizhijingling.com/uploadfile/coustom/2019//309/20190613170125729.mp4");
        });
        findViewById(R.id.testFull).setOnClickListener(v -> {
            hhPlayer3.fullScreen();
//            hideBar();
        });
        findViewById(R.id.testExit).setOnClickListener(v -> hhPlayer3.exitFullScreen());
        findViewById(R.id.testMute).setOnClickListener(v -> hhPlayer3.setMute(!ismute));
        findViewById(R.id.testFast).setOnClickListener(v -> hhPlayer3.setSpeed(2));
        findViewById(R.id.testSlow).setOnClickListener(v -> hhPlayer3.setSpeed(0.5f));
        findViewById(R.id.testRelease).setOnClickListener(v -> hhPlayer3.release());
        findViewById(R.id.testGo5).setOnClickListener(v -> {
            if (hhPlayer3.getCurrentPositionMS() + 5000 < hhPlayer3.getMediaDurationMS()) {
                hhPlayer3.seekTo(hhPlayer3.getCurrentPositionMS() + 5000);
            }
        });
        TextView tvback5 = findViewById(R.id.testBack5);
        tvback5.setOnClickListener(v -> {
            if (hhPlayer3.getCurrentPositionMS() > 5000) {
                hhPlayer3.seekTo(hhPlayer3.getCurrentPositionMS() - 5000);
            }
        });
        imgvTest = findViewById(R.id.imgvTest);
        findViewById(R.id.testGet).setOnClickListener(v -> {
//            hhPlayer3.changeScale(i);
            i += 0.1;
            if (i > 2) {
                i = 0;
            }
            ViseLog.d(i);
//            hhPlayer3.takeBitmap(new HOnListener<Bitmap>() {
//                @Override
//                public void onListen(Bitmap bitmap) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            imgvTest.setImageBitmap(bitmap);
//                        }
//                    });
//                }
//            });
            hhPlayer3.setEffect(new VignetteEffect(i));
        });
        findViewById(R.id.testRotate).setOnClickListener(v -> {
            //判断当前是否为横屏,判断是否旋转
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                hl_screen.setViewSize(hhPlayer3.getPlayerView(), hl_screen.getDeviceWidth(mActivity), hl_screen.getDeviceHeight(mActivity));
                hideBar();
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
                hl_screen.setViewSize(hhPlayer3.getPlayerView(), ViewGroup.LayoutParams.MATCH_PARENT, hl_screen.getHeightFromWidthAtLand(mActivity));
                showBar();
            }
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progess, boolean fromUser) {
                if (fromUser) {
                    hhPlayer3.seekTo(progess * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar_volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progess, boolean fromUser) {
                if (fromUser) {
                    hhPlayer3.setVolume((float) progess / 10);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        findViewById(R.id.testKeyUp).setOnClickListener(v -> hhPlayer3.sendKeyDown(KeyEvent.KEYCODE_DPAD_UP));
        findViewById(R.id.testKeyDown).setOnClickListener(v -> hhPlayer3.sendKeyDown(KeyEvent.KEYCODE_DPAD_DOWN));
        findViewById(R.id.testKeyLeft).setOnClickListener(v -> hhPlayer3.sendKeyDown(KeyEvent.KEYCODE_DPAD_LEFT));
        findViewById(R.id.testKeyRight).setOnClickListener(v -> hhPlayer3.sendKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT));
        findViewById(R.id.testKeyMenu).setOnClickListener(v -> hhPlayer3.sendKeyDown(KeyEvent.KEYCODE_MENU));
        findViewById(R.id.testKeyBack).setOnClickListener(v -> hhPlayer3.sendKeyDown(KeyEvent.KEYCODE_BACK));
        findViewById(R.id.testKeyEnter).setOnClickListener(v -> hhPlayer3.sendKeyDown(KeyEvent.KEYCODE_DPAD_CENTER));
    }

    @Override
    protected boolean isOpenSlideBack() {
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        hhPlayer3.sendKeyDown(keyCode);
        return false;//return false 会调用系统的方法
    }


    @Override
    public void changeToLand() {
        if (hhPlayer3.getPlayerView() != null) {
            hl_screen.setViewSize(hhPlayer3.getPlayerView(), hl_screen.getDeviceWidth(mActivity), hl_screen.getDeviceHeight(mActivity));
            hideBar();
        }
    }

    @Override
    public void changeToPort() {
        if (hhPlayer3.getPlayerView() != null) {
            hl_screen.setViewSize(hhPlayer3.getPlayerView(), ViewGroup.LayoutParams.MATCH_PARENT, hl_screen.getHeightFromWidthAtLand(mActivity));
            showBar();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (hhPlayer3 != null) {
            hhPlayer3.release();
        }
    }
}
