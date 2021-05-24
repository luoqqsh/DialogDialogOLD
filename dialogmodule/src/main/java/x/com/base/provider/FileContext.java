package x.com.base.provider;

import androidx.core.content.FileProvider;

import x.com.base.app.AppInit;
import x.com.base.toast.U_Toast;

public class FileContext extends FileProvider {
    @Override
    public boolean onCreate() {
        if (getContext() != null) {
            AppInit.setContext(getContext());
        }
        return super.onCreate();
    }
}
