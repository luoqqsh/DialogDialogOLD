package x.com.hl_tools;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.util.Random;

public class hl_math {
    //保留n位小数
    public static String RoundingDouble(String str, String doubleType) {
        DecimalFormat df = new DecimalFormat(doubleType);
        return df.format(Double.parseDouble(str));
    }

    //获取随机数
    public static int getRandom(int first, int last) {
        int number;
        while (true) {
            number = (int) (Math.random() * 1000);
            if (number >= first && number < last) {
                break;
            }
        }
        return number;
    }

    public static float getRandom() {
        return new Random().nextFloat();
    }

    /**
     * 限制2位小数
     */
    public static void setEditText2D(final EditText etChangeAmt) {
        etChangeAmt.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    edt.delete(posDot + 3, posDot + 4);
                }

            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                if (s.toString().trim().equals(".")) {
                    s = "0" + s;
                    etChangeAmt.setText(s);
                    etChangeAmt.setSelection(2);
                }
            }
        });
    }

}
