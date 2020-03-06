package x.com.hl_base.hl_interface;

public interface OnClickItemListener {
    //返回是点击后自动关闭对话框，返回否则不关闭
    boolean onItemClick(String text, int position);
}
