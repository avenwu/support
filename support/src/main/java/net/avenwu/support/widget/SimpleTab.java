package net.avenwu.support.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentTabHost;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aven on 5/3/15.
 */
public class SimpleTab {
    SparseArray<Item> mTabItems = new SparseArray<>();
    ViewGroup mContentView;

    public void injectInto(ViewGroup parent) {
        parent.addView(mContentView);
    }

    public interface OnTabClickListener {
        void onItemClick(View view, Item item, int position);
    }
    public static class Item {
        private CharSequence mLabel;
        private Drawable mIcon;
        private int mLayoutId = -1;
        private int mLabelId = -1;
        private int mIconId = -1;
        private Decorate mDecorate;

        public Item setLabelWithIcon(@StringRes int label, @DrawableRes int
                drawable) {
            mLabelId = label;
            mIconId = drawable;
            return this;
        }

        public Item setCustomItemLayout(int layout, Decorate decorate) {
            mLayoutId = layout;
            mDecorate = decorate;
            return this;
        }
    }

    public interface Decorate {
        void onBind(View contentView);
    }

    private static abstract class SimpleListener implements View.OnClickListener {
        int mPosition;

        public SimpleListener(int position) {
            mPosition = position;
        }
    }

    public static class Builder {
        Context mContext;
        List<Item> mItems;
        OnTabClickListener mListener;

        public Builder(Context context) {
            mContext = context;
            mItems = new ArrayList<Item>();
        }

        protected SimpleTab bind(SimpleTabLayout layout) {
            final SimpleTab tab = new SimpleTab();
            tab.mContentView = layout;
            init(tab);
            return tab;
        }

        public SimpleTab create(@LayoutRes int... layout) {
            final SimpleTab tab = new SimpleTab();
            if (layout != null && layout.length > 0) {
                tab.mContentView = (ViewGroup) View.inflate(mContext, layout[0], null);
            } else {
                LinearLayout tabLayout = new LinearLayout(mContext);
                tabLayout.setOrientation(LinearLayout.HORIZONTAL);
                tab.mContentView = tabLayout;
            }
            init(tab);
            return tab;
        }

        private void init(final SimpleTab tab) {
            for (int i = 0; i < mItems.size(); i++) {
                Item item = mItems.get(i);
                tab.mTabItems.put(i, item);
                if (item.mLayoutId != -1) {
                    View itemView = View.inflate(mContext, item.mLayoutId, null);
                    tab.mContentView.addView(itemView);
                    if (item.mDecorate != null) {
                        item.mDecorate.onBind(itemView);
                    }
                } else {
                    TextView label = new TextView(mContext);
                    label.setText(item.mLabelId);
                    label.setGravity(Gravity.CENTER);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                            (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
                                    .WRAP_CONTENT);
                    layoutParams.weight = 1;
                    label.setCompoundDrawablesWithIntrinsicBounds(0, item.mIconId, 0, 0);
                    tab.mContentView.addView(label, layoutParams);
                }
            }

            int count = tab.mContentView.getChildCount();
            for (int i = 0; i < count; i++) {
                View view = tab.mContentView.getChildAt(i);
                view.setOnClickListener(new SimpleListener(i) {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onItemClick(v, tab.mTabItems.get(mPosition), mPosition);
                        }
                    }
                });
            }
        }

        public Builder newItem(Item item, int... position) {
            if (position != null && position.length > 0) {
                mItems.add(position[0], item);
            } else {
                mItems.add(item);
            }
            return this;
        }

        public Builder setOnTabClickListener(OnTabClickListener listener) {
            mListener = listener;
            return this;
        }
    }
}
