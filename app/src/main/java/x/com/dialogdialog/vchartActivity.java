package x.com.dialogdialog;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.vinctor.vchartviews.radar.RadarChart;

import x.com.dialogdialog.Base.BaseActivity;

public class vchartActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vchart);
        RadarChart radarview = findViewById(R.id.radarview);
        radarview.setCount(5)//多边形几条边
                .setDensity(5)//雷达图蜘蛛网密度
                .setTitleTextSize(30)//雷达边角标题文字大小(px)默认30
                .setTagTextSize(30)//雷达刻度文字大小
                .setTitles(new String[]{"人气值", "影响力", "生产力", "传播度", "魅力值"})//边角文字
                .setAlpha(0.5f);//雷达图数据遮盖透明度

        radarview.commit();//以上设置需要此方法才能生效
    }
}
