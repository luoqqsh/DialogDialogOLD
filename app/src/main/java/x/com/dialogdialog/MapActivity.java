package x.com.dialogdialog;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.xxing.amap.AmapController;

import x.com.dialogdialog.Base.BaseActivity;
import x.com.hl_map.HHmap;

public class MapActivity extends BaseActivity {
    private LinearLayout ll_mapview;
    private HHmap hHmap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapview);
        ll_mapview = findViewById(R.id.ll_mapview);
        hHmap = new HHmap(this, HHmap.MapType.AMAP, new AmapController());
        hHmap.addMapView(ll_mapview, savedInstanceState);
        hHmap.moveToPosition(24, 118, 0);
        hHmap.addMarker(24, 118, R.mipmap.map_icon_p);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hHmap.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        hHmap.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        hHmap.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        hHmap.onSaveInstanceState(outState);
    }
}
