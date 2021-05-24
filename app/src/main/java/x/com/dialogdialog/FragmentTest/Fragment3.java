package x.com.dialogdialog.FragmentTest;

import android.os.Bundle;
import android.view.View;

import x.com.base.fragment.HLBaseFragment;
import x.com.dialogdialog.databinding.FragmentcBinding;

public class Fragment3 extends HLBaseFragment {
    FragmentcBinding binding;

    @Override
    public View getLayoutView() {
        binding = FragmentcBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void initUI(Bundle savedInstanceState) {

    }
}
