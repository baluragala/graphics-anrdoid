package com.moodmetric.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.left;
import static com.moodmetric.graphics.R.attr.maxSpeed;

public class MainActivity extends AppCompatActivity {

    private Paint backgroundPaint;
    private Paint backgroundInnerPaint;
    private Paint maskPaint;
    private Paint needlePaint;
    private Paint ticksPaint;
    private Paint txtPaint;
    private Paint colorLinePaint;
    private View myView;
    private boolean toggleDayNight=false;

    public static final double DEFAULT_MAX_SPEED = 100.0;
    public static final double DEFAULT_MAJOR_TICK_STEP = 20.0;
    public static final int DEFAULT_MINOR_TICKS = 1;
    public static final int DEFAULT_LABEL_TEXT_SIZE_DP = 12;

    private double maxSpeed = DEFAULT_MAX_SPEED;
    private double speed = 0;
    private int defaultColor = Color.rgb(180, 180, 180);
    private double majorTickStep = DEFAULT_MAJOR_TICK_STEP;
    private int minorTicks = DEFAULT_MINOR_TICKS;

    private List<ColoredRange> ranges = new ArrayList<ColoredRange>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myView = new MyView(getApplicationContext());
        setContentView(myView);
    }

    class MyView extends View {

        public MyView(Context context) {
            super(context);
        }

        public MyView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public MyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void setOnTouchListener(OnTouchListener l) {
            super.setOnTouchListener(l);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            backgroundPaint.setStyle(Paint.Style.STROKE);
            backgroundPaint.setColor(Color.rgb(127, 127, 127));

            ticksPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            ticksPaint.setStrokeWidth(3.0f);
            ticksPaint.setStyle(Paint.Style.STROKE);
            ticksPaint.setColor(Color.RED);
            ticksPaint.setTextSize(60);

            colorLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            colorLinePaint.setStyle(Paint.Style.STROKE);
            colorLinePaint.setStrokeWidth(5);
            colorLinePaint.setColor(defaultColor);

            int w = getWidth();
            int h = getHeight();
            //canvas.drawArc(getCircle(canvas, 0.8f), 90.0f, 360.0f, true, backgroundPaint);

            RectF circle = getCircle(canvas, 0.85f);
            canvas.drawArc(circle, 90, 360, true, backgroundPaint);

            RectF innerCircle = getCircle(canvas, 0.2f);
            canvas.drawArc(innerCircle, 90, 360, true, backgroundPaint);

            drawTicks(canvas);

            Resources res = getResources();
            Bitmap bitmap = toggleDayNight ? BitmapFactory.decodeResource(res, R.drawable.moon) : BitmapFactory.decodeResource(res, R.drawable.sun);
            canvas.drawBitmap(bitmap, (canvas.getWidth()/2-40),(canvas.getHeight()/2-40), ticksPaint);
            toggleDayNight=!toggleDayNight;

        }

        private RectF getCircle(Canvas canvas, float factor) {
            RectF oval;
            final int canvasWidth = canvas.getWidth() - getPaddingLeft() - getPaddingRight();
            final int canvasHeight = canvas.getHeight() - getPaddingLeft() - getPaddingRight();
            if (canvasHeight * 2 >= canvasWidth) {
                oval = new RectF(0, 0, canvasWidth * factor, canvasWidth * factor);
            } else {
                oval = new RectF(0, 0, canvasHeight * 2 * factor, canvasHeight * 2 * factor);
            }

            oval.offset((canvasWidth - oval.width()) / 2 + getPaddingLeft(), (canvasHeight - oval.height()) / 2 + getPaddingTop());

            return oval;
        }

        private void drawTicks(Canvas canvas) {
            float majorStep = 30;
            float majorTicksLength = 30;
            RectF oval = getCircle(canvas, 0.85f);
            float radius = oval.width() * 0.5f;
            float currentAngle = 90;
            int tickCount=0;
            while (currentAngle <= 450) {
                canvas.drawLine(
                        (float) (oval.centerX() + Math.cos((180 - currentAngle) / 180 * Math.PI) * (radius - majorTicksLength / 2)),
                        (float) (oval.centerY() - Math.sin(currentAngle / 180 * Math.PI) * (radius - majorTicksLength / 2)),
                        (float) (oval.centerX() + Math.cos((180 - currentAngle) / 180 * Math.PI) * (radius + majorTicksLength / 2)),
                        (float) (oval.centerY() - Math.sin(currentAngle / 180 * Math.PI) * (radius + majorTicksLength / 2)),
                        ticksPaint
                );
                if(tickCount==12 || tickCount == 3 || tickCount == 6 || tickCount == 9)
                    canvas.drawText(String.valueOf(tickCount==0?12:tickCount),
                            (float) (oval.centerX() + Math.cos((180 - currentAngle) / 180 * Math.PI) * (radius - majorTicksLength / 2)),
                            (float) (oval.centerY() - Math.sin(currentAngle / 180 * Math.PI) * (radius + majorTicksLength / 2)),ticksPaint);
                tickCount++;
                currentAngle += majorStep;

            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            //return super.onTouchEvent(event);

            if( event.getAction()==MotionEvent.ACTION_UP && event.getX() > getWidth()/2 - 40 && event.getX() < getWidth()/2+40 && event.getY() > getHeight()/2 - 40 && event.getY() < getHeight()/2+40)
                //Log.d("onTouchEvent",event.getX()+"," + event.getY());
                myView.invalidate();
            return true;
        }
    }

    public static class ColoredRange {

        private int color;
        private double begin;
        private double end;

        public ColoredRange(int color, double begin, double end) {
            this.color = color;
            this.begin = begin;
            this.end = end;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public double getBegin() {
            return begin;
        }

        public void setBegin(double begin) {
            this.begin = begin;
        }

        public double getEnd() {
            return end;
        }

        public void setEnd(double end) {
            this.end = end;
        }
    }

}
