package x.com.hl_tools.DialogTest;

public class myObjectPool extends ObjectPool {
    public myObjectPool(int initialCapacity, int maxCapacity) {
        super(initialCapacity, maxCapacity);
    }

    public myObjectPool(int maxCapacity) {
        super(maxCapacity);
    }

    @Override
    protected Object create() {
        return new Object();
    }
}
