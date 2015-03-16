package net.avenwu.support.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by chaobin on 3/16/15.
 */
public class BitmapUtil {
    /**
     * @param bitmap image src
     * @param radius ignore for circle, only used in rounded rectangle
     * @param circle whether return circle or not
     * @return decoded bitmap
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float radius, boolean circle) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int width = Math.min(bitmap.getWidth(), bitmap.getHeight());

        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.RED);//this color is not useful actually, the shape we drew is kind of mask

        final float dx = (bitmap.getWidth() - width) / 2.0f;
        final float dy = (bitmap.getHeight() - width) / 2.0f;
        final RectF srcRect = new RectF(dx, dy, dx + width, dy + width);

        final float r = circle ? width / 2.0f : radius;
        canvas.drawRoundRect(srcRect, r, r, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
