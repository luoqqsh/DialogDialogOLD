package x.com.dialogdialog;

import java.io.Serializable;

public class TestModel implements Serializable {
    private String a;
    private int b;
    private boolean c;
    private double d;

    public TestModel(String a, int b, boolean c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }
}
