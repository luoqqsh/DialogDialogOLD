package x.com.dialogdialog;

import android.os.Bundle;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import x.com.hl_base.HOnListener;
import x.com.hl_log.ViseLog;
import x.com.hl_player.HHMediaPlayer;
import x.com.hl_player.HHPlayer;
import x.com.hl_player.MediaPlayerController;
import x.com.hl_rx.TaskDelayBManager;
import x.com.hl_rx.TaskDelayManager;
import x.com.hl_tools.hl_device;
import x.com.hl_tools.hl_leak;
import x.com.hl_tools.hl_time;

public class HHMediaActivity extends AppCompatActivity {
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
    private SurfaceView surface;
    private String musicpath = "/storage/emulated/0/Music/不可说 - 霍建华、赵丽颖.mp3";
    private String videopath = "/storage/emulated/0/DCIM/wallpaperFairy/videoWallpaper/69a2ff59895cdf8a7ccaf2d73046f638.mp4";
    private HHPlayer hhPlayer;
    private boolean ismute = false;
    private TextView currenttime;
    private AppCompatSeekBar seekBar;
    private TextureView textureview;
    private TextView tvback5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediaplay);
        textureview = findViewById(R.id.textureview);
        surface = findViewById(R.id.surface);
        currenttime = findViewById(R.id.currenttime);
        seekBar = findViewById(R.id.seekbar);
        seekBar.setMax(100);
//        TextureView textureview = findViewById(R.id.textureview);
//        hhPlayer = new HHPlayer(new HHMediaPlayer(), surface);
        hhPlayer = new HHPlayer(new HHMediaPlayer(), textureview);
        hhPlayer.setStartWhenPrepared(false);
        hhPlayer.setLooping(false);

        findViewById(R.id.select).setOnClickListener(v -> hhPlayer.prepare(videopath));
        findViewById(R.id.play).setOnClickListener(v -> hhPlayer.start());
        findViewById(R.id.pause).setOnClickListener(v -> hhPlayer.pause());
        findViewById(R.id.stop).setOnClickListener(v -> hhPlayer.stop());
        findViewById(R.id.reset).setOnClickListener(v -> hhPlayer.reset());
        findViewById(R.id.full).setOnClickListener(v -> hhPlayer.fullScreen());
        findViewById(R.id.mute).setOnClickListener(v -> hhPlayer.setMute(!ismute));
        findViewById(R.id.fast).setOnClickListener(v -> hhPlayer.setSpeed(2));
        findViewById(R.id.slow).setOnClickListener(v -> hhPlayer.setSpeed(0.5f));
        findViewById(R.id.go5).setOnClickListener(v -> {
            if (hhPlayer.getCurrentPosition() + 5000 < hhPlayer.getMediaDuration()) {
                hhPlayer.seekTo(hhPlayer.getCurrentPosition() + 5000);
            }
        });
        tvback5 = findViewById(R.id.back5);
        tvback5.setOnClickListener(v -> {
            if (hhPlayer.getCurrentPosition() > 5000) {
                hhPlayer.seekTo(hhPlayer.getCurrentPosition() - 5000);
            }
        });
        findViewById(R.id.slow).setOnClickListener(v -> hhPlayer.setSpeed(0.5f));
        findViewById(R.id.get).setOnClickListener(v -> {
            ViseLog.d(hhPlayer.getCurrentPosition());
            ViseLog.d(hhPlayer.getMediaDuration());
            ViseLog.d(hhPlayer.getMediaPath());
            ViseLog.d(hhPlayer.getState());
        });
        hhPlayer.setPreparedListener(new HOnListener<Object>() {
            @Override
            public void onListen(Object o) {
                seekBar.setMax((int) (hhPlayer.getMediaDuration() / 1000));
            }
        });
        hhPlayer.setProgessListener(new HOnListener<Long>() {
            @Override
            public void onListen(Long o) {
                currenttime.setText(hl_time.formatTime(o));
                seekBar.setProgress((int) (o / 1000));
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progess, boolean fromUser) {
                if (fromUser) {
                    hhPlayer.seekTo(progess * 1000);
                }
//                ViseLog.d("?/");
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
        if (hhPlayer != null) {
            hhPlayer.release();
        }
        if (hl_device.isHuaWeiPhone()) {
            hl_leak.fixLeak(this);
        }
    }
}
