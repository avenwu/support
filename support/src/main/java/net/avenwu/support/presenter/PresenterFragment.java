package net.avenwu.support.presenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class PresenterFragment<P extends Presenter> extends Fragment {
    P mPresenter;

    protected abstract Class<? extends P> getPresenterClass();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mPresenter = getPresenterClass().newInstance();
            mPresenter.attach();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPresenter.detach();
    }

    public P getPresenter() {
        return mPresenter;
    }
}
