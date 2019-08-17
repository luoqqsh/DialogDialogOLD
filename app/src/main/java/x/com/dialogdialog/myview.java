package x.com.dialogdialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class myview extends FrameLayout {
    public myview(@NonNull Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        //View里加载其他view
        View inflate = View.inflate(context, R.layout.activity_mediaplay, this);
    }

    public myview(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public myview(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
}
