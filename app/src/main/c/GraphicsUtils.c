#include "GraphicsUtils.h"

#include <android/bitmap.h>
#include <android/log.h>

#include <stdlib.h>

#define ABS(a) ((a)<(0)?(-a):(a))
#define MAX(a, b) ((a)>(b)?(a):(b))
#define MIN(a, b) ((a)<(b)?(a):(b))

JNIEXPORT jobject JNICALL Java_com_pramit_rmh_ui_GraphicsUtils_blur(JNIEnv *env, jclass thisClass, jobject backgroundBitmap, jint radius)
{
    jclass java_bitmap_class = (jclass)(*env)->FindClass(env, "android/graphics/Bitmap");
    int *pix = getPixels(env, backgroundBitmap);
    int w = getBitmapWidth(env, java_bitmap_class, backgroundBitmap);
    int h = getBitmapHeight(env, java_bitmap_class, backgroundBitmap);

    int wm = w - 1;
    int hm = h - 1;
    int wh = w * h;
    int div = radius + radius + 1;

    short *r = (short *) malloc(wh * sizeof(short));
    short *g = (short *) malloc(wh * sizeof(short));
    short *b = (short *) malloc(wh * sizeof(short));
    int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;

    int *vmin = (int *) malloc(MAX(w, h) * sizeof(int));

    int divsum = (div + 1) >> 1;
    divsum *= divsum;
    short *dv = (short *) malloc(256 * divsum * sizeof(short));
    for (i = 0; i < 256 * divsum; i++) {
        dv[i] = (short) (i / divsum);
    }

    yw = yi = 0;

    int(*stack)[3] = (int (*)[3]) malloc(div * 3 * sizeof(int));
    int stackpointer;
    int stackstart;
    int *sir;
    int rbs;
    int r1 = radius + 1;
    int routsum, goutsum, boutsum;
    int rinsum, ginsum, binsum;

    for (y = 0; y < h; y++) {
        rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
        for (i = -radius; i <= radius; i++) {
            p = pix[yi + (int)(MIN(wm, MAX(i, 0)))];
            sir = stack[i + radius];
            sir[0] = (p & 0xff0000) >> 16;
            sir[1] = (p & 0x00ff00) >> 8;
            sir[2] = (p & 0x0000ff);

            rbs = r1 - ABS(i);
            rsum += sir[0] * rbs;
            gsum += sir[1] * rbs;
            bsum += sir[2] * rbs;
            if (i > 0) {
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
            }
            else {
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
            }
        }
        stackpointer = radius;

        for (x = 0; x < w; x++) {

            r[yi] = dv[rsum];
            g[yi] = dv[gsum];
            b[yi] = dv[bsum];

            rsum -= routsum;
            gsum -= goutsum;
            bsum -= boutsum;

            stackstart = stackpointer - radius + div;
            sir = stack[stackstart % div];

            routsum -= sir[0];
            goutsum -= sir[1];
            boutsum -= sir[2];

            if (y == 0) {
                vmin[x] = MIN(x + radius + 1, wm);
            }
            p = pix[yw + vmin[x]];

            sir[0] = (p & 0xff0000) >> 16;
            sir[1] = (p & 0x00ff00) >> 8;
            sir[2] = (p & 0x0000ff);

            rinsum += sir[0];
            ginsum += sir[1];
            binsum += sir[2];

            rsum += rinsum;
            gsum += ginsum;
            bsum += binsum;

            stackpointer = (stackpointer + 1) % div;
            sir = stack[(stackpointer) % div];

            routsum += sir[0];
            goutsum += sir[1];
            boutsum += sir[2];

            rinsum -= sir[0];
            ginsum -= sir[1];
            binsum -= sir[2];

            yi++;
        }
        yw += w;
    }
    for (x = 0; x < w; x++) {
        rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
        yp = -radius * w;
        for (i = -radius; i <= radius; i++) {
            yi = MAX(0, yp) + x;

            sir = stack[i + radius];

            sir[0] = r[yi];
            sir[1] = g[yi];
            sir[2] = b[yi];

            rbs = r1 - ABS(i);

            rsum += r[yi] * rbs;
            gsum += g[yi] * rbs;
            bsum += b[yi] * rbs;

            if (i > 0) {
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
            }
            else {
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
            }

            if (i < hm) {
                yp += w;
            }
        }
        yi = x;
        stackpointer = radius;
        for (y = 0; y < h; y++) {
            pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

            rsum -= routsum;
            gsum -= goutsum;
            bsum -= boutsum;

            stackstart = stackpointer - radius + div;
            sir = stack[stackstart % div];

            routsum -= sir[0];
            goutsum -= sir[1];
            boutsum -= sir[2];

            if (x == 0) {
                vmin[y] = MIN(y + r1, hm) * w;
            }
            p = x + vmin[y];

            sir[0] = r[p];
            sir[1] = g[p];
            sir[2] = b[p];

            rinsum += sir[0];
            ginsum += sir[1];
            binsum += sir[2];

            rsum += rinsum;
            gsum += ginsum;
            bsum += binsum;

            stackpointer = (stackpointer + 1) % div;
            sir = stack[stackpointer];

            routsum += sir[0];
            goutsum += sir[1];
            boutsum += sir[2];

            rinsum -= sir[0];
            ginsum -= sir[1];
            binsum -= sir[2];

            yi += w;
        }
    }

    free(r);
    free(g);
    free(b);
    free(vmin);
    free(dv);
    free(stack);

    AndroidBitmap_unlockPixels(env, backgroundBitmap);
    return backgroundBitmap;
}

JNIEXPORT jobject JNICALL Java_com_pramit_rmh_ui_GraphicsUtils_merge(JNIEnv *env, jclass thisClass, jobject backgroundBitmap, jobject componentBitmap, jint xOffset, jint yOffset)
{
    jclass java_bitmap_class = (jclass)(*env)->FindClass(env, "android/graphics/Bitmap");
    jmethodID createBitmapMethod = (*env)->GetStaticMethodID(env, java_bitmap_class, "createBitmap", "(Landroid/graphics/Bitmap;IIII)Landroid/graphics/Bitmap;");

    int uiWidth = getBitmapWidth(env, java_bitmap_class, componentBitmap);
    int uiHeight = getBitmapHeight(env, java_bitmap_class, componentBitmap);

    jobject mergedBitmap = (*env)->CallStaticObjectMethod(env, java_bitmap_class, createBitmapMethod, backgroundBitmap, xOffset, yOffset, uiWidth, uiHeight);

    int *mergedBitmapPix = getPixels(env, mergedBitmap);
    int *componentPix = getPixels(env, componentBitmap);

    for(int y = 0; y < uiHeight; y++)
    {
        for(int x = 0; x < uiWidth; x++)
        {
            int curPixel = (y * uiWidth) + x;
            int backgroundPixel = mergedBitmapPix[curPixel];
            int componentPixel = componentPix[curPixel];
            int newPixelColor = blendPixels(backgroundPixel, componentPixel);
            mergedBitmapPix[curPixel] = newPixelColor;
        }
    }

    AndroidBitmap_unlockPixels(env, componentBitmap);
    AndroidBitmap_unlockPixels(env, mergedBitmap);
    return mergedBitmap;
}

int getBitmapWidth(JNIEnv *env, jclass bitmapClass, jobject bitmap)
{
    jmethodID getBitmapWidthMethod = (*env)->GetMethodID(env, bitmapClass, "getWidth", "()I");
    return (*env)->CallIntMethod(env, bitmap, getBitmapWidthMethod);
}

int getBitmapHeight(JNIEnv *env, jclass bitmapClass, jobject bitmap)
{
    jmethodID getBitmapHeightMethod = (*env)->GetMethodID(env, bitmapClass, "getHeight", "()I");
    return (*env)->CallIntMethod(env, bitmap, getBitmapHeightMethod);
}

int* getPixels(JNIEnv* env, jobject bitmap)
{
    AndroidBitmapInfo bitmapInfo;
    void *pixels;

    if (AndroidBitmap_getInfo(env, bitmap, &bitmapInfo) != ANDROID_BITMAP_RESULT_SUCCESS)
    {
        __android_log_write(ANDROID_LOG_ERROR, "GraphicsUtils", "AndroidBitmap_getInfo failed!");
        return NULL;
    }

    if (bitmapInfo.format != ANDROID_BITMAP_FORMAT_RGBA_8888)
    {
        __android_log_write(ANDROID_LOG_ERROR, "GraphicsUtils", "Only supports ANDROID_BITMAP_FORMAT_RGBA_8888");
        return NULL;
    }

    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) != ANDROID_BITMAP_RESULT_SUCCESS)
    {
        __android_log_write(ANDROID_LOG_ERROR, "GraphicsUtils", "AndroidBitmap_lockPixels failed!");
        return NULL;
    }
    return (int*)pixels;
}

int blendPixels(int firstColor, int secondColor)
{
    int firstRed = firstColor & 0xFF >> 16;
    int firstGreen = firstColor & 0xFF >> 8;
    int firstBlue = firstColor & 0xFF;
    int firstAlpha = firstColor & 0xFF >> 24;

    int secondRed = secondColor & 0xFF >> 16;
    int secondGreen = secondColor & 0xFF >> 8;
    int secondBlue = secondColor & 0xFF;
    int secondAlpha = secondColor & 0xFF >> 24;

    int aOut = (int)(firstAlpha + (secondAlpha * (255 - firstAlpha) / 255));
    int rOut = (int)((firstRed * firstAlpha + secondRed * secondAlpha * (255 - firstAlpha) / 255) / aOut);
    int gOut = (int)((firstGreen * firstAlpha + secondGreen * secondAlpha * (255 - firstAlpha) / 255) / aOut);
    int bOut = (int)((firstBlue * firstAlpha + secondBlue * secondAlpha * (255 - firstAlpha) / 255) / aOut);

    return (int)(((rOut & 0xFF) << 16)|((gOut & 0xFF) << 8)|(bOut & 0xFF)|((aOut & 0xFF) << 24));
}
