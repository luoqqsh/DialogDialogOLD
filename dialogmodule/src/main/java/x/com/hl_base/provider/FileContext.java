package x.com.hl_base.provider;

import androidx.core.content.FileProvider;

import x.com.hl_base.app.AppInit;

public class FileContext extends FileProvider {
    @Override
    public boolean onCreate() {
        if (getContext() != null) {
            AppInit.setContext(getContext());
        }
        return super.onCreate();
    }
}
