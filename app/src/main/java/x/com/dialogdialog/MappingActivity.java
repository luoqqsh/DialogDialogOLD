package x.com.dialogdialog;

import android.os.Bundle;
import android.view.View;

import java.util.Collection;

import io.github.lizhangqu.retrace.ClassMapping;
import io.github.lizhangqu.retrace.MethodMapping;
import io.github.lizhangqu.retrace.Retrace;
import x.com.dialogdialog.BaseApp.BaseActivity;

public class MappingActivity extends BaseActivity {
    private String mappingFile = "";

    @Override
    public View getLayoutView() {
        return new View(this);
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
//        TaskDelayBManager task = new TaskDelayBManager() {
//            @Override
//            public void onListen(Long index) {
////                a();
////                佛祖保佑();
//                c();
//            }
//        }.delay(1);
    }

    /**
     * 根据原始 class 名或方法名获取混淆后的 class 名或方法名
     */
    private void a() {
        Retrace retrace = Retrace.createRetrace(mappingFile, true);
        ClassMapping retraceClass = retrace.getClassMapping("com.google.common.base.CharMatcher$1");
        String originalName = retraceClass.getOriginalName();
        String obfuscatedName = retraceClass.getObfuscatedName();

        Collection<MethodMapping> methods = retraceClass.getMethods("join");
        for (MethodMapping methodMapping : methods) {
            String originalMethodName = methodMapping.getOriginalName();
            String obfuscatedMethodName = methodMapping.getObfuscatedName();
            String methodReturn = methodMapping.getReturn();
            String methodArgs = methodMapping.getArgs();
            String methodRange = String.valueOf(methodMapping.getRange());
        }
    }

    /**
     * 根据混淆后的class名或方法名获取原始class名或方法名
     */
    private void b() {
        Retrace retrace = Retrace.createRetrace(mappingFile, false);
        ClassMapping retraceClass = retrace.getClassMapping("a.a.a.佛祖保佑.n");
        String obfuscatedName = retraceClass.getObfuscatedName();
        String originalName = retraceClass.getOriginalName();

        Collection<MethodMapping> methods = retraceClass.getMethods("a");
        for (MethodMapping methodMapping : methods) {
            String obfuscatedMethodName = methodMapping.getObfuscatedName();
            String originalMethodName = methodMapping.getOriginalName();
            String methodReturn = methodMapping.getReturn();
            String methodArgs = methodMapping.getArgs();
            String methodRange = String.valueOf(methodMapping.getRange());
        }
    }

    /**
     * 堆栈还原
     */
    private void c() {
        Retrace retrace = Retrace.createRetrace(mappingFile, false);
        String originalStacktrace = retrace.stackTrace("");
    }
}
