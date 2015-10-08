package net.avenwu.support.util;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EdgeEffect;
import android.widget.TextView;
import android.widget.Toast;

import net.avenwu.support.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by chaobin on 2/25/15.
 */
public class ViewCompat {
    /**
     * set specific distance for AbsListView to enable the over scroll effect
     *
     * @param listView
     * @param distance
     */
    public static void enableOverScroll(AbsListView listView, int distance) {
        try {
            Field overscrollDistance = AbsListView.class.getDeclaredField("mOverscrollDistance");
            overscrollDistance.setAccessible(true);
            overscrollDistance.setInt(listView, distance);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * set custom edge color on over scroll
     * Copy from http://stackoverflow.com/questions/11603267/scrollview-change-the-edge-effect-color-with-holo
     *
     * @param listView
     * @param color
     */
    public static void customEdgeEffectColor(AbsListView listView, @ColorRes int color) {
        try {
            final Field fEdgeGlowTop = AbsListView.class.getDeclaredField("mEdgeGlowTop");
            final Field fEdgeGlowBottom = AbsListView.class.getDeclaredField("mEdgeGlowBottom");
            fEdgeGlowTop.setAccessible(true);
            fEdgeGlowBottom.setAccessible(true);
            setEdgeEffectColor((EdgeEffect) fEdgeGlowTop.get(listView), color);
            setEdgeEffectColor((EdgeEffect) fEdgeGlowBottom.get(listView), color);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public static void setEdgeEffectColor(final EdgeEffect edgeEffect, @ColorRes final int color) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                edgeEffect.setColor(color);
                return;
            }
            final Field edgeField = EdgeEffect.class.getDeclaredField("mEdge");
            final Field glowField = EdgeEffect.class.getDeclaredField("mGlow");
            edgeField.setAccessible(true);
            glowField.setAccessible(true);
            final Drawable edge = (Drawable) edgeField.get(edgeEffect);
            final Drawable glow = (Drawable) glowField.get(edgeEffect);
            edge.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            glow.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            edge.setCallback(null); // free up any references
            glow.setCallback(null); // free up any references
        } catch (final Exception ignored) {
            ignored.printStackTrace();
        }
    }

    /**
     * Remove the left space on ActionBar while using Toolbar as ActionBar
     * Usage:
     * {@code ViewCompat.cleanContentInset(getSupportActionBar().getCustomView());}
     * <p>
     * Toolbar toolbar = (Toolbar) customView.getParent();
     * toolbar.setContentInsetsAbsolute(0, 0);
     *
     * @param customView
     */
    public static boolean removeContentInsetOfToolbar(View customView) {
        if (customView != null && customView.getParent().getClass().getCanonicalName().equals("android.support.v7.widget.Toolbar")) {
            try {
                Method setContentInsetsAbsolute = customView.getParent().getClass().getDeclaredMethod("setContentInsetsAbsolute", int.class, int.class);
                setContentInsetsAbsolute.setAccessible(true);
                setContentInsetsAbsolute.invoke(customView.getParent(), 0, 0);
                return true;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Toast makeText(Context context, String text, int duration) {
        Toast result = new Toast(context);
        View v = View.inflate(context, R.layout.custom_toast, null);
        TextView tv = (TextView) v.findViewById(android.R.id.message);
        tv.setText(text);
        result.setView(v);
        result.setDuration(duration);
        return result;
    }

    public static Toast makeText(Context context, @StringRes int text, int duration) {
        Toast result = new Toast(context);
        View v = View.inflate(context, R.layout.custom_toast, null);
        TextView tv = (TextView) v.findViewById(android.R.id.message);
        tv.setText(text);
        result.setView(v);
        result.setDuration(duration);
        return result;
    }
}
