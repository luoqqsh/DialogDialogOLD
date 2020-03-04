package x.com.hl_base.hl_fragment;


public interface DialogLifeCycleListener {

    void onCreate(BaseDialog dialog);

    void onShow(BaseDialog dialog);

    void onDismiss(BaseDialog dialog);

}
