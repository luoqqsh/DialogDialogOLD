package x.com.dialogdialog;

import android.os.Bundle;
import android.view.SurfaceView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import x.com.dialogdialog.Base.BaseActivity;
import x.com.hl_player.MediaPlayerController;
import x.com.hl_tools.hl_device;
import x.com.hl_tools.hl_leak;

/**
 * 旧版mediaController
 */
public class MediaActivity extends BaseActivity {
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
    private MediaPlayerController mediaPlayerController;
    private String musicpath = "/storage/emulated/0/Music/不可说 - 霍建华、赵丽颖.mp3";
    private String videopath = "/storage/emulated/0/DCIM/wallpaperFairy/videoWallpaper/69a2ff59895cdf8a7ccaf2d73046f638.mp4";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        surface = findViewById(R.id.surface);
//        TextureView textureview = findViewById(R.id.textureview);
        //不支持设置透明度
        mediaPlayerController = new MediaPlayerController(surface);
        mediaPlayerController.setLooping(false);
        //正常设置透明度
//        mediaPlayerController = new MediaPlayerController(textureview);

        findViewById(R.id.select).setOnClickListener(v -> {
            mediaPlayerController.doPrepare(videopath);
        });
        findViewById(R.id.play).setOnClickListener(v -> mediaPlayerController.doStart());
        findViewById(R.id.pause).setOnClickListener(v -> mediaPlayerController.doPause());
        findViewById(R.id.stop).setOnClickListener(v -> mediaPlayerController.doStop());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayerController != null) {
            mediaPlayerController.doRelease();
        }
        hl_leak.fixInput(this);
    }
}
