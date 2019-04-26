package x.com.dialogdialog;

import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import x.com.hl_base.HOnListener;
import x.com.hl_log.ViseLog;
import x.com.hl_player.HHMediaPlayer;
import x.com.hl_player.HHPlayer;
import x.com.hl_player.MediaPlayerController;
import x.com.hl_tools.hl_device;
import x.com.hl_tools.hl_leak;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediaplay);
        surface = findViewById(R.id.surface);
        currenttime = findViewById(R.id.currenttime);
        seekBar = findViewById(R.id.seekbar);
//        TextureView textureview = findViewById(R.id.textureview);


        hhPlayer = new HHPlayer(new HHMediaPlayer(), surface);
        hhPlayer.setStartWhenPrepared(false);
        //正常设置透明度
//        mediaPlayerController = new MediaPlayerController(textureview);

        findViewById(R.id.select).setOnClickListener(v -> hhPlayer.prepare(videopath));
        findViewById(R.id.play).setOnClickListener(v -> hhPlayer.start());
        findViewById(R.id.pause).setOnClickListener(v -> hhPlayer.pause());
        findViewById(R.id.stop).setOnClickListener(v -> hhPlayer.stop());
        findViewById(R.id.full).setOnClickListener(v -> hhPlayer.fullScreen());
        findViewById(R.id.mute).setOnClickListener(v -> hhPlayer.setMute(!ismute));
        hhPlayer.setPreparedListener(new HOnListener<Object>() {
            @Override
            public void onListen(Object o) {
                seekBar.setMax((int) (hhPlayer.getMediaDuration() / 1000));
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                hhPlayer.seekTo(i );
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
