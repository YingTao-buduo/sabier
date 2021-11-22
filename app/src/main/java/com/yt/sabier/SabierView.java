package com.yt.sabier;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;


import java.util.ArrayList;

public class SabierView extends View {

    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint;

    private int width;
    private int height;

    private ArrayList<ArrayList<Point>> strokes = new ArrayList<>();

    private ArrayList<Point> currentStroke;
    private Float currentWidth = 5.0f;
    private int currentColor = Color.BLACK;

    public SabierView(Context c, AttributeSet attr) {
        super(c, attr);
        setFocusable(true);
    }

    /**
     * 每次放入最新的三个点
     *
     * @param p1 三新
     * @param p2 二新
     * @param p3 一新
     */
    public void next(Point p1, Point p2, Point p3) {


        currentStroke = new ArrayList<>();
        addPointAndDraw(new Point((p1.getX() + 1) * width / 2d, (p1.getY() + 1) * height / 2d));
        addPointAndDraw(new Point((p2.getX() + 1) * width / 2d, (p2.getY() + 1) * height / 2d));

        //如果Point.value == 1，改灰色
        if (p3.getValue() == 1) {
//            currentColor = ContextCompat.getColor(this.getContext(), R.color.灰色);
        }
        //如果Point.value == 0，改绿色
        if (p3.getValue() == 0) {
//            currentColor = ContextCompat.getColor(this.getContext(), R.color.绿色);
        }
        addPointAndDraw(new Point((p3.getX() + 1) * width / 2d, (p3.getY() + 1) * height / 2d));
        strokes.add(currentStroke);
        int maxSize = 50;
        if (strokes.size() > maxSize) {
            strokes.remove(strokes.size() - maxSize);
            clearAnnotation();
            for (int i = strokes.size() - maxSize + 1; i < strokes.size(); i++) {
                currentStroke = strokes.get(i);
                for (int j = 0; j < currentStroke.size(); j++)
                    this.drawSection(j);
            }
        }
        if (Math.abs(p3.getX()) > 1 || Math.abs(p3.getY()) > 1) {
            clearAnnotation();
        }
        invalidate();
    }

    protected void onSizeChanged(int w, int h, int oldWith, int oldHeight) {
        width = w;
        height = h;
//        bitmap = getTransparentBitmap();
        bitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);

        canvas = new Canvas();
        canvas.setBitmap(bitmap);
        if (bitmap != null)
            canvas.drawBitmap(bitmap, 0, 0, null);


        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeCap(Cap.ROUND);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        clearAnnotation();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }


    private void addPointAndDraw(Point p) {
        currentStroke.add(p);
        if (currentStroke.size() < 3)
            return;
        this.drawSection(currentStroke.size() - 3);
    }

    private void drawSection(int i) {
        if (i > currentStroke.size() - 3)
            return;

        paint.setColor(currentColor);
        paint.setStrokeWidth(currentWidth);
        paint.setStyle(Paint.Style.STROKE);

        Point mid1 = new Point((currentStroke.get(i).getIntX() + currentStroke.get(i + 1).getIntX()) / 2d,
                (currentStroke.get(i).getIntY() + currentStroke.get(i + 1).getIntY()) / 2d);
        Point mid = currentStroke.get(i + 1);
        Point mid2 = new Point((currentStroke.get(i + 1).getIntX() + currentStroke.get(i + 2).getIntX()) / 2d,
                (currentStroke.get(i + 1).getIntY() + currentStroke.get(i + 2).getIntY()) / 2d);

        Path path = new Path();
        path.moveTo(mid1.getIntX(), mid1.getIntY());
        path.quadTo(mid.getIntX(), mid.getIntY(), mid2.getIntX(), mid2.getIntY());
        canvas.drawPath(path, paint);
    }

    public void clearAnnotation() {
        if (canvas != null) {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            invalidate();
        }
    }

    private Bitmap getTransparentBitmap() {
        Bitmap bitmap1 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);;
        if (bitmap1 != null) {
            int mWidth = bitmap1.getWidth();
            int mHeight = bitmap1.getHeight();
            for (int i = 0; i < mHeight; i++) {
                for (int j = 0; j < mWidth; j++) {
                    int color = bitmap1.getPixel(j, i);
                    int g = Color.green(color);
                    int r = Color.red(color);
                    int b = Color.blue(color);
                    int a = Color.alpha(color);
                    if (g >= 250 && r >= 250 && b >= 250) {
                        a = 0;
                    }
                    color = Color.argb(a, r, g, b);
                    bitmap1.setPixel(j, i, color);
                }
            }
        }
        return bitmap1;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Float getCurrentWidth() {
        return currentWidth;
    }

    public void setCurrentWidth(Float currentWidth) {
        this.currentWidth = currentWidth;
    }

    public int getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(int currentColor) {
        this.currentColor = currentColor;
    }
}
