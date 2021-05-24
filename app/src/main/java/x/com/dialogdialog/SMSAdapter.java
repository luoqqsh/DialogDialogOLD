package x.com.dialogdialog;

import android.widget.TextView;

import java.util.List;

import x.com.util.U_time;
import x.com.util.model.SMSBean;
import x.com.view.Recyclerview.LiAdapter.LiBaseRecyclerViewAdapter;

public class SMSAdapter extends LiBaseRecyclerViewAdapter<SMSBean> {
    public SMSAdapter(List<SMSBean> datas) {
        super(datas);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_sms_content;
    }

    @Override
    protected void bindData(BaseViewHolder holder, SMSBean smsBean, int position) {
        TextView titleText = holder.getView(R.id.titleText);
        TextView contentText = holder.getView(R.id.contentText);
        titleText.setText(smsBean.getTypeStr() + " " + smsBean.getPhoneNo() + "\n" + U_time.convertLongToTime(smsBean.getLongDate(), U_time.yyyy_MM_ddHH_mm_ss));
        contentText.setText(smsBean.getContent());
    }
}
