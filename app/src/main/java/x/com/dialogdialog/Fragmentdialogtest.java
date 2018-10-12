package x.com.dialogdialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import x.com.hl_dialog.DFLoadingDialog;

public class Fragmentdialogtest extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialogtest, container, false);

        TextView tv_frag = v.findViewById(R.id.tv_frag);
        tv_frag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CheckUpActivity.class);
                startActivity(intent);
            }
        });
        TextView tv_frag2 = v.findViewById(R.id.tv_frag2);
        tv_frag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DFLoadingDialog a = new DFLoadingDialog();
                if (getFragmentManager() != null) {
                    a.show(getFragmentManager(), "fff");
                }
            }
        });

        return v;
    }
}
