package com.pramit.rmh.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

public class GraphicsUtils {

    private static final float BITMAP_SCALE = 0.5f;

    static {
        System.loadLibrary("GraphicsUtils");
    }

    //FIX SCALING
    public static void blurUIComponent(View background, View uiComponent, int blurRadius, int cornerRadius) {
        Bitmap backgroundBitmap = getBackgroundBitmapFromView(background);
        Bitmap blurredBackgroundBitmap = blur(backgroundBitmap, blurRadius);
        Bitmap uiComponentBitmap = getBackgroundBitmapFromView(uiComponent);
        Bitmap mergedBitmap = merge(blurredBackgroundBitmap, uiComponentBitmap, (int) (uiComponent.getX() * BITMAP_SCALE), (int) (uiComponent.getY() * BITMAP_SCALE));
        RoundedBitmapDrawable roundedBitmap = RoundedBitmapDrawableFactory.create(uiComponent.getResources(), mergedBitmap);
        roundedBitmap.setCornerRadius(cornerRadius);
        setComponentBackground(uiComponent, roundedBitmap);
    }

    private static void setComponentBackground(View uiComponent, RoundedBitmapDrawable roundedBitmap) {
        Canvas canvas = new Canvas(Bitmap.createBitmap(uiComponent.getWidth(), uiComponent.getHeight(), Bitmap.Config.ARGB_8888));
        float scale = 1 / BITMAP_SCALE;
        canvas.scale(scale, scale);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(roundedBitmap.getBitmap(), 0, 0, paint);
    }

    public static Bitmap getBackgroundBitmapFromView(View view) {
        int width = (int) (view.getWidth() * BITMAP_SCALE);
        int height = (int) (view.getHeight() * BITMAP_SCALE);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable drawable = view.getBackground();
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    public static int pxToDp(Context context, float px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / displayMetrics.density);
    }

    public static int dpToPx(Context context, float dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * displayMetrics.density);
    }

    public static native Bitmap blur(Bitmap backgroundBitmap, int blurRadius);

    public static native Bitmap merge(Bitmap background, Bitmap componentUI, int xOffset, int yOffset);
}
