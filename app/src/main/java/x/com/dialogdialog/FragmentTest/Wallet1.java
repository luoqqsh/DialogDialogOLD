package x.com.dialogdialog.FragmentTest;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.Observer;

import x.com.base.fragment.HLBaseFragment;
import x.com.dialogdialog.R;
import x.com.dialogdialog.databinding.FragmentOginBinding;
import x.com.liveEventBus.LiveEventBus;

public class Wallet1 extends HLBaseFragment {
    FragmentOginBinding binding;
    Wallet1Login f1;

    @Override
    public View getLayoutView() {
        binding = FragmentOginBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void initUI(Bundle savedInstanceState) {

        LiveEventBus.get("wallet1", Boolean.class).observe(mActivity, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                login(aBoolean);
            }
        });
        login(false);
    }

    private void login(boolean isLogin) {
        if (isLogin) {
            f1 = new Wallet1Login();
            Bundle bundle1 = new Bundle();
            bundle1.putString("title", "wallet1已登录");
            f1.setArguments(bundle1);
        } else {
            f1 = new Wallet1Login();
            Bundle bundle = new Bundle();
            bundle.putString("title", "wallet1未登录");
            f1.setArguments(bundle);
        }
        getChildFragmentManager().beginTransaction().replace(R.id.fragmentlog, f1).commit();
    }
}
