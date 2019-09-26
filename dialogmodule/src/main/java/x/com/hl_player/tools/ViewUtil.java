package x.com.hl_player.tools;

import android.util.Log;
import android.view.ViewGroup;

public class ViewUtil {
    //修改全屏下的显示比例问题
    public static void modifyFullProportion(ViewGroup viewGroup, int videoWidth, int videoHeight) {
        if (viewGroup != null) {
            Log.e("modifyFullProportion", videoWidth + " " + videoHeight + " " + viewGroup.getWidth() + " " + viewGroup.getHeight());
            float scaleWidth = videoWidth * 1.0f / viewGroup.getWidth();
            float scaleHeight = videoHeight * 1.0f / viewGroup.getHeight();
            float scale = Math.min(scaleWidth, scaleHeight);
            int resultVideoWidth = (int) (videoWidth / scale);
            int resultVideoHeight = (int) (videoHeight / scale);
            ViewGroup.LayoutParams lp = viewGroup.getLayoutParams();
            lp.width = resultVideoWidth;
            lp.height = resultVideoHeight;
            viewGroup.setLayoutParams(lp);
        }
    }
}
