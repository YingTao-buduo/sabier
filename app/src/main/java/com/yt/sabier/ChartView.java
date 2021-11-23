package com.yt.sabier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChartView extends View {
    public static class Point {
        public static final Comparator<Point> X_COMPARATOR = (lhs, rhs) -> (int) (lhs.x * 1000 - rhs.x * 1000);

        public float x;
        public float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public Point() {
        }
    }

    private static final float CURVE_LINE_WIDTH = 4f;

    private Point[] adjustedPoints;
    private final Rect chartRect = new Rect();
    private Paint curvePaint = new Paint();
    private final Path curvePath = new Path();
    private Paint fillPaint = new Paint();
    private final Path fillPath = new Path();

    private List<Point> originalList;

    private int maxSize = 20;
    private int count = 0;

    {
        curvePaint.setStyle(Paint.Style.STROKE);
        curvePaint.setStrokeCap(Paint.Cap.ROUND);
        curvePaint.setStrokeWidth(CURVE_LINE_WIDTH);
        curvePaint.setColor(Color.rgb(0x00, 0x89, 0xd8));
        curvePaint.setAntiAlias(true);
        curvePaint.setAlpha(200);

        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(Color.rgb(0x00, 0xd2, 0xff));
        fillPaint.setAlpha(170);
        fillPaint.setAntiAlias(true);
    }

    public ChartView(Context context) {
        super(context);
    }

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void adjustPoints(int chartWidth, int chartHeight) {
        float maxY = 0;
        for (Point p : originalList) {
            if (p.y > maxY) {
                maxY = p.y;
            }
        }

        float scaleY = chartHeight / maxY;

        float axesSpan = originalList.get(originalList.size() - 1).x - originalList.get(0).x;
        float startX = originalList.get(0).x;

        for (int i = 0; i < originalList.size(); i++) {
            Point p = originalList.get(i);

            Point newPoint = new Point();
            newPoint.x = (p.x - startX) * chartWidth / axesSpan + chartRect.left;

            newPoint.y = p.y * scaleY;
            newPoint.y = chartHeight - newPoint.y;

            adjustedPoints[i] = newPoint;
        }
    }

    private void buildPath(Path path) {
        path.reset();

        path.moveTo(adjustedPoints[0].x, adjustedPoints[0].y);
        int pointSize = originalList.size();
        for (int i = 0; i < pointSize - 1; i++) {
            float pointX = (adjustedPoints[i].x + adjustedPoints[i + 1].x) / 2;
            float pointY = (adjustedPoints[i].y + adjustedPoints[i + 1].y) / 2;

            float controlX = adjustedPoints[i].x;
            float controlY = adjustedPoints[i].y;

            path.quadTo(controlX, controlY, pointX, pointY);
        }
        path.quadTo(adjustedPoints[pointSize - 1].x, adjustedPoints[pointSize - 1].y, adjustedPoints[pointSize - 1].x,
                adjustedPoints[pointSize - 1].y);
    }

    private void drawCurve(Canvas canvas) {
        buildPath(curvePath);
        buildPath(fillPath);

        fillPath.lineTo(chartRect.right, chartRect.bottom);
        fillPath.lineTo(chartRect.left, chartRect.bottom);
        fillPath.lineTo(chartRect.left, adjustedPoints[0].y);
        fillPath.close();
        canvas.drawPath(fillPath, fillPaint);
        canvas.drawPath(curvePath, curvePaint);
    }


    public void init(int maxSize) {
        this.maxSize = maxSize;
        originalList = new ArrayList<>();
        adjustedPoints = new Point[maxSize];
    }

    public void add(float value) {
        int size = originalList.size();
        originalList.add(new Point(count++, value));
        if (size >= maxSize) {
            originalList.remove(0);
        }
        originalList.sort(Point.X_COMPARATOR);
        if (originalList.size() > 3) {
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        getDrawingRect(chartRect);

        if (originalList != null && originalList.size() > 3) {

            int chartHeight = chartRect.bottom - chartRect.top;
            int chartWidth = chartRect.right - chartRect.left;

            adjustPoints(chartWidth, chartHeight);

            drawCurve(canvas);
        }
    }

    public Paint getCurvePaint() {
        return curvePaint;
    }

    public Paint getFillPaint() {
        return fillPaint;
    }

    public void setCurvePaint(Paint curvePaint) {
        this.curvePaint = curvePaint;
    }

    public void setFillPaint(Paint fillPaint) {
        this.fillPaint = fillPaint;
    }
}
