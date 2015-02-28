package net.avenwu.support.util;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.widget.AbsListView;
import android.widget.EdgeEffect;

import java.lang.reflect.Field;

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
}
