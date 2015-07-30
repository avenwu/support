package net.avenwu.support.presenter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by chaobin on 7/14/15.
 */
public abstract class PresenterActivity<P extends Presenter> extends AppCompatActivity {
    protected P mPresenter;

    protected abstract Class<? extends P> getPresenterClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detach();
    }

    public P getPresenter() {
        return mPresenter;
    }
}
