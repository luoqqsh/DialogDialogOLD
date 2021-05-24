package x.com.dialogdialog.FragmentTest;

import android.os.Bundle;
import android.view.View;

import x.com.base.fragment.HLBaseFragment;
import x.com.dialogdialog.R;
import x.com.dialogdialog.databinding.FragmentOginOntentBinding;
import x.com.util.U_statusbar;

public class Wallet1Login extends HLBaseFragment {
    FragmentOginOntentBinding binding;
    Wallet1Login f1;

    @Override
    public View getLayoutView() {
        binding = FragmentOginOntentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        U_statusbar.setStatusBarColor(mActivity, getResources().getColor(R.color.blue));
        String title = getArguments().getString("title");
        binding.text1.setText(title);
            binding.getRoot().setBackgroundColor(getResources().getColor(R.color.green));
    }
}
