package x.com.hl_livedatabus;


import x.com.hl_livedatabus.core.Config;
import x.com.hl_livedatabus.core.LiveEventBusCore;
import x.com.hl_livedatabus.core.Observable;

public final class LiveEventBus {

    /**
     * get observable by key with type
     *
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    public static <T> Observable<T> get(String key, Class<T> type) {
        return LiveEventBusCore.get().with(key, type);
    }

    /**
     * get observable by key
     *
     * @param key
     * @return
     */
    public static Observable<Object> get(String key) {
        return get(key, Object.class);
    }

    /**
     * use the inner class Config to set params
     * first of all, call config to get the Config instance
     * then, call the method of Config to config LiveEventBus
     * call this method in Application.onCreate
     */
    public static Config config() {
        return LiveEventBusCore.get().config();
    }
}
