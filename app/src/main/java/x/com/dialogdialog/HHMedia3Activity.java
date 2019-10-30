package x.com.dialogdialog;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;

import java.util.ArrayList;
import java.util.List;

import x.com.dialogdialog.BaseApp.BaseActivity;
import x.com.hl_base.HOnListener;
import x.com.hl_player.HHPlayer3;
import x.com.hl_player.TvListBean.TvData;
import x.com.hl_player.TvListBean.TvDataList;
import x.com.hl_player.TvListBean.TypeClarity;
import x.com.hl_player.TvListBean.TypeClass;
import x.com.hl_player.TvListBean.TypeState;
import x.com.hl_player.TvListBean.TypeView;
import x.com.hl_player.base.HHMediaPlayer;
import x.com.hl_tools.DialogTest.phone.bean.MediaFileBean;
import x.com.hl_tools.hl_medialist;
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
//  /storage/emulated/0/Music/沧桑的浪漫 - 张睿、李晟.mp3
//  /storage/emulated/0/Music/沧海一声笑 - 任贤齐.mp3
//  /storage/emulated/0/Music/不要在我寂寞的时候说爱我 - 郑源.mp3
//  /storage/emulated/0/Music/不是因为寂寞才想你 - 陶钰玉.mp3
//  /storage/emulated/0/Music/不念 - 张靓颖.mp3
//  /storage/emulated/0/Music/不可说 - 霍建华、赵丽颖.mp3
    //----------------------------------------------------------
//    private String musicpath = "/storage/emulated/0/Music/不可说 - 霍建华、赵丽颖.mp3";
//    private String videopath = "/storage/emulated/0/Download/mpaa.mp4";
//    private String videopath = "/storage/emulated/0/Android/data/x.com.dialogdialog/mpaa.mp4";
//    private String videopath = "/storage/emulated/0/DCIM/Camera/mpaa.mp4";
//    private String videopath = "/storage/emulated/0/Android/data/x.com.dialogdialog/files/Download/mpaa.mp4";
//    private String videopath = "/storage/emulated/0/DCIM/Camera/19101112858336.mp4";
//    private String videopath = "http://s.bizhijingling.com/uploadfile/userload/vedio/2019/0516/20190516065450574.mp4";
//    private String videopath = "rtsp://27.148.240.138/PLTV/88888888/224/3221226716/10000100000000060000000001664269_0.smil";
//    private String videopath = "http://cstv.live.wscdns.com/live/xiamen1/playlist.m3u8";
    private boolean ismute = false;
    private TextView currenttime, totaltime;
    private AppCompatSeekBar seekBar, seekbar_volume;
    private TextView tvback5;
    private TvData tvUrl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediaview);
        initList();
        initUI();
        hhview = findViewById(R.id.hhPlayerView);
        hhPlayer3 = new HHPlayer3(this, new HHMediaPlayer(), TypeView.NEWSURFACE, hhview);
        hhPlayer3.addLoadingView();
        hhPlayer3.setPreparedListener(new HOnListener<Object>() {
            @Override
            public void onListen(Object o) {
                hhPlayer3.start();
                seekbar_volume.setProgress((int) (hhPlayer3.getVolume() * 10));
                totaltime.setText(" / " + hl_time.formatTime(hhPlayer3.getMediaDuration()));
                seekBar.setMax((int) (hhPlayer3.getMediaDuration() / 1000));
            }
        });
        hhPlayer3.setProgessListener(new HOnListener<Long>() {
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
        hhPlayer3.setOnBufferingUpdateListener(new HOnListener<Integer>() {
            @Override
            public void onListen(Integer integer) {
                seekBar.setSecondaryProgress(integer);
            }
        });
    }

    private void initList() {
        TvDataList tvDataList = new TvDataList(TypeClass.中央台, "中央电视台rtsp-smil", "rtsp://27.148.245.116:554/PLTV/88888888/224/3221227004/10000100000000060000000001740930_0.smil", TypeClarity.H4k, TypeState.流畅);
        TvDataList tvDataList2 = new TvDataList(TypeClass.中央台, "mp4", "http://s.bizhijingling.com/uploadfile/userload/vedio/2019/0516/20190516065450574.mp4", TypeClarity.标清, TypeState.流畅);
        TvDataList tvDataList3 = new TvDataList(TypeClass.中央台, "福建综合rtsp", "rtsp://27.148.219.129:554/live/ch15102621311662932570.sdp?playtype=1&boid=001&backupagent=27.148.219.129:554&clienttype=1&time=20190312204601+08&life=172800&ifpricereqsnd=1&vcdnid=001&userid=5922751618830&mediaid=ch15102621311662932570&ctype=2&TSTVTimeLife=7200&contname=&authid=0&terminalflag=1&UserLiveType=0&stbid=AC10049900703340000090D8F3A8EC17&nodelevel=3&bitrate=2000", TypeClarity.标清, TypeState.流畅);
        TvDataList tvDataList4 = new TvDataList(TypeClass.中央台, "湖南卫视rtsp", "rtsp://27.148.219.129:554/live/ch15102717162145789452.sdp?playtype=1&boid=001&backupagent=27.148.219.129:554&clienttype=1&time=20190312204601+08&life=172800&ifpricereqsnd=1&vcdnid=001&userid=5922751618830&mediaid=ch15102717162145789452&ctype=2&TSTVTimeLife=7200&contname=&authid=0&terminalflag=1&UserLiveType=0&stbid=AC10049900703340000090D8F3A8EC17&nodelevel=3&bitrate=2000", TypeClarity.标清, TypeState.流畅);
        TvDataList tvDataList5 = new TvDataList(TypeClass.中央台, "江苏卫视m3u8", "http://27.148.224.161/PLTV/88888888/224/3221226752/index.m3u8", TypeClarity.标清, TypeState.流畅);
        TvDataList tvDataList6 = new TvDataList(TypeClass.中央台, "cctv-4k", "http://183.56.168.47/cdn-out.iqy.sc96655.com/tslive/c204_scc_20190107_yvwl5h02_original_t10/c204_scc_20190107_yvwl5h02_original_t10.m3u8", TypeClarity.标清, TypeState.流畅);
        ArrayList<TvDataList> tvDataLists = new ArrayList<>();
//        tvDataLists.add(tvDataList);
        tvDataLists.add(tvDataList2);
        tvDataLists.add(tvDataList3);
        tvDataLists.add(tvDataList4);
        tvDataLists.add(tvDataList5);
        tvDataLists.add(tvDataList6);
        tvUrl = new TvData(1, "20190719", "itv", tvDataLists);
    }

    private void initUI() {
        currenttime = findViewById(R.id.currenttime);
        totaltime = findViewById(R.id.totaltime);
        seekbar_volume = findViewById(R.id.seekbar_volume);
        seekBar = findViewById(R.id.seekbar);
        seekBar.setMax(100);
        seekbar_volume.setMax(10);

        findViewById(R.id.select).setOnClickListener(v -> {
//            Uri imageContentUri = hl_media.getUriStringById(hl_uri.getVideoUri());
//            ViseLog.d(imageContentUri);
            hhPlayer3.prepare(tvUrl.getLinks(), 0);
            List<MediaFileBean> imgList = hl_medialist.getVideoList(mContext);
//            hhPlayer3.prepare("content://media/external/video/media/27", "");
        });

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
