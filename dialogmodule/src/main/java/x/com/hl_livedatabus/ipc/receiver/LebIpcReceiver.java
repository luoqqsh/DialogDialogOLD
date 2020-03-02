package x.com.hl_livedatabus.ipc.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import x.com.hl_livedatabus.LiveEventBus;
import x.com.hl_livedatabus.ipc.IpcConst;
import x.com.hl_livedatabus.ipc.decode.IDecoder;
import x.com.hl_livedatabus.ipc.decode.ValueDecoder;
import x.com.hl_livedatabus.ipc.json.JsonConverter;

/**
 * Created by liaohailiang on 2019/3/26.
 */
public class LebIpcReceiver extends BroadcastReceiver {

    private IDecoder decoder;

    public LebIpcReceiver(JsonConverter jsonConverter) {
        this.decoder = new ValueDecoder(jsonConverter);
    }

    public void setJsonConverter(JsonConverter jsonConverter) {
        this.decoder = new ValueDecoder(jsonConverter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (IpcConst.ACTION.equals(intent.getAction())) {
            try {
                String key = intent.getStringExtra(IpcConst.KEY);
                Object value = decoder.decode(intent);
                if (key != null) {
                    LiveEventBus
                            .get(key)
                            .post(value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
