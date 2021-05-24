package x.com.dialogdialog.FragmentTest;

import android.os.Bundle;
import android.view.View;

import x.com.base.fragment.HLBaseFragment;
import x.com.dialogdialog.databinding.FragmentbBinding;

public class Fragment2 extends HLBaseFragment {
    FragmentbBinding binding;

    @Override
    public View getLayoutView() {
        binding = FragmentbBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void initUI(Bundle savedInstanceState) {

    }
}
