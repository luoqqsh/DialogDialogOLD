package x.com.dialogdialog;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;

import x.com.dialogdialog.Base.BaseActivity;
import x.com.hl_base.HOnListener;
import x.com.hl_log.ViseLog;
import x.com.hl_player.HHPlayer3;
import x.com.hl_player.base.HHMediaPlayer;
import x.com.hl_tools.hl_time;


public class HHMediaViewActivity extends BaseActivity {
    private HHPlayer3 hhPlayer3;
    private LinearLayout hhview;
    //  /storage/emulated/0/Music/虫儿飞 - 郑伊健.mp3
//  /storage/emulated/0/Music/炽热的青春 - 贾乃亮.mp3
//  /storage/emulated/0/Music/齿轮 - 赵薇.mp3
//  /storage/emulated/0/Music/痴心绝对 - 李圣杰.mp3
//  /storage/emulated/0/Music/城门 - 陈浩民.mp3
//  /storage/emulated/0/Music/车站 - 张秀卿.mp3
//  /storage/emulated/0/Music/常回家看看 - 陈红.mp3
//  /storage/emulated/0/Music/沧桑的浪漫 - 张睿、李晟.mp3
//  /storage/emulated/0/Music/沧海一声笑 - 任贤齐.mp3
//  /storage/emulated/0/Music/不要在我寂寞的时候说爱我 - 郑源.mp3
//  /storage/emulated/0/Music/不是因为寂寞才想你 - 陶钰玉.mp3
//  /storage/emulated/0/Music/不念 - 张靓颖.mp3
//  /storage/emulated/0/Music/不可说 - 霍建华、赵丽颖.mp3
    //----------------------------------------------------------
//  /storage/emulated/0/DCIM/wallpaperFairy/videoWallpaper/69a2ff59895cdf8a7ccaf2d73046f638.mp4
//  /storage/emulated/0/DCIM/wallpaperFairy/videoWallpaper/98bd9003e6e4bfddbfd02f45fc7f11dd.mp4
//  /storage/emulated/0/DCIM/wallpaperFairy/videoWallpaper/c4e3963e691763968b16b20fcf607067.mp4
//  /storage/emulated/0/DCIM/wallpaperFairy/videoWallpaper/c0197187ca9868c646f734310cd23eee.mp4
//  /storage/emulated/0/ClearWatermark/CWDownLoadTool/我的下载/bb327cd9d2c4d45cde8b168cb48f0e26.mp4
    private FrameLayout surface;
    private String musicpath = "/storage/emulated/0/Music/不可说 - 霍建华、赵丽颖.mp3";
    private String videopath = "/storage/emulated/0/DCIM/Camera/19619104518845.mp4";
    //    private String videopath = "http://s.bizhijingling.com/uploadfile/userload/vedio/2019/0516/20190516065450574.mp4";
    private boolean ismute = false;
    private TextView currenttime, totaltime;
    private AppCompatSeekBar seekBar, seekbar_volume;
    private TextView tvback5;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediaview);
        initUI();
        hhview = findViewById(R.id.hhPlayerView3);
        hhPlayer3 = new HHPlayer3(this, new HHMediaPlayer(), HHPlayer3.PlayerView.NEWTEXTURE, hhview);
        hhPlayer3.setLooping(false);
        hhPlayer3.setCompletionListener(new HOnListener<Object>() {
            @Override
            public void onListen(Object o) {
                ViseLog.d("finishaaa");
            }
        });
        hhPlayer3.setPreparedListener(new HOnListener<Object>() {
            @Override
            public void onListen(Object o) {
                seekbar_volume.setProgress((int) (hhPlayer3.getVolume() * 10));
                totaltime.setText(" / " + hl_time.formatTime(hhPlayer3.getMediaDuration()));
                seekBar.setMax((int) (hhPlayer3.getMediaDuration() / 1000));
            }
        });
        hhPlayer3.setProgessListener(new HOnListener<Long>() {
            @Override
            public void onListen(Long o) {
                currenttime.setText(hl_time.formatTime(o));
                seekBar.setProgress((int) (o / 1000));
            }
        });
        hhPlayer3.setOnBufferingUpdateListener(new HOnListener<Integer>() {
            @Override
            public void onListen(Integer integer) {
                seekBar.setSecondaryProgress(integer);
            }
        });


    }

    private void initUI() {
        currenttime = findViewById(R.id.currenttime);
        totaltime = findViewById(R.id.totaltime);
        seekbar_volume = findViewById(R.id.seekbar_volume);
        seekBar = findViewById(R.id.seekbar);
        seekBar.setMax(100);
        seekbar_volume.setMax(10);

        findViewById(R.id.select).setOnClickListener(v -> hhPlayer3.prepare(videopath));
        findViewById(R.id.play).setOnClickListener(v -> hhPlayer3.start());
        findViewById(R.id.pause).setOnClickListener(v -> hhPlayer3.pause());
        findViewById(R.id.stop).setOnClickListener(v -> hhPlayer3.stop());
        findViewById(R.id.reset).setOnClickListener(v -> hhPlayer3.reset());
        findViewById(R.id.full).setOnClickListener(v -> hhPlayer3.fullScreen());
        findViewById(R.id.mute).setOnClickListener(v -> hhPlayer3.setMute(!ismute));
        findViewById(R.id.fast).setOnClickListener(v -> hhPlayer3.setSpeed(2));
        findViewById(R.id.slow).setOnClickListener(v -> hhPlayer3.setSpeed(0.5f));
        findViewById(R.id.release).setOnClickListener(v -> hhPlayer3.release());
        findViewById(R.id.go5).setOnClickListener(v -> {
            if (hhPlayer3.getCurrentPosition() + 5000 < hhPlayer3.getMediaDuration()) {
                hhPlayer3.seekTo(hhPlayer3.getCurrentPosition() + 5000);
            }
        });
        tvback5 = findViewById(R.id.back5);
        tvback5.setOnClickListener(v -> {
            if (hhPlayer3.getCurrentPosition() > 5000) {
                hhPlayer3.seekTo(hhPlayer3.getCurrentPosition() - 5000);
            }
        });
        findViewById(R.id.slow).setOnClickListener(v -> hhPlayer3.setSpeed(0.5f));
        findViewById(R.id.get).setOnClickListener(v -> {
            ViseLog.d(hhPlayer3.getCurrentPosition());
            ViseLog.d(hhPlayer3.getMediaDuration());
            ViseLog.d(hhPlayer3.getMediaPath());
            ViseLog.d(hhPlayer3.getPlayerState());
            ViseLog.d(hhPlayer3.isPlaying());
            ViseLog.d(hhPlayer3.getScreenBrightness());
            hhPlayer3.setScreenBrigtness(80);
            ViseLog.d(hhPlayer3.getScreenBrightness());
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (hhPlayer3 != null) {
            hhPlayer3.release();
        }
    }
}
