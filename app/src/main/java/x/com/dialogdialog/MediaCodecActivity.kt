package x.com.dialogdialog

import android.os.Bundle
import android.view.SurfaceView
import kotlinx.android.synthetic.main.activity_mediacodec.*
import x.com.dialogdialog.BaseApp.BaseActivity
import x.com.hl_mediacodec.kotlin.decoder.AudioDecoder
import x.com.hl_mediacodec.kotlin.decoder.VideoDecoder
import x.com.hl_mediacodec.kotlin.muxer.MP4Repack
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MediaCodecActivity : BaseActivity() {
    private lateinit var audioDecoder: AudioDecoder
    private lateinit var videoDecoder: VideoDecoder
    private val musicPath = "/storage/emulated/0/Music/不可说 - 霍建华、赵丽颖.mp3"
    private val videoPath = "/storage/emulated/0/mvtest.mp4"
    override fun getLayoutId(): Int {
        return R.layout.activity_mediacodec
    }

    override fun initUI(savedInstanceState: Bundle?) {
        path.text = videoPath
        start.setOnClickListener { initPlayer() }
    }

    private fun initPlayer() {
        //创建线程池
        val threadPool = Executors.newFixedThreadPool(2)
        //创建视频解码器
        videoDecoder = VideoDecoder(videoPath, sfv, null)
        threadPool.execute(videoDecoder)
        //创建音频解码器
        audioDecoder = AudioDecoder(videoPath)
        threadPool.execute(audioDecoder)
        //开启播放
        videoDecoder.goOn()
        audioDecoder.goOn()
    }

    private fun dabao(path: String) {
        MP4Repack(path).start()
    }

    override fun onDestroy() {
        super.onDestroy()
        videoDecoder.doneDecode()
        audioDecoder.doneDecode()
    }
}