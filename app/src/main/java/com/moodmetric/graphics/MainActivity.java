package com.moodmetric.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
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

import static android.R.attr.centerX;
import static android.R.attr.data;
import static android.R.attr.left;
import static android.R.attr.path;
import static android.R.attr.radius;
import static android.R.attr.x;
import static android.R.attr.y;
import static com.moodmetric.graphics.R.attr.maxSpeed;
import static com.moodmetric.graphics.R.attr.thickness;

public class MainActivity extends AppCompatActivity {

    private Paint backgroundPaint;
    private Paint backgroundInnerPaint;
    private Paint maskPaint;
    private Paint needlePaint;
    private Paint ticksPaint;
    private Paint txtPaint;
    private Paint colorLinePaint;
    private Paint colorl0paint;
    private Paint colorl1paint;
    private Paint colorl2paint;
    private Paint colorl3paint;
    private Paint colorl4paint;
    private Paint colorl5paint;

    private int colorLogoTurquoise=Color.parseColor("#008f67");
    private int colorFlowTurquoise=Color.parseColor("#4bb494");
    private int colorCalmTurquoise=Color.parseColor("#98c399");
    private int colorGroundedBeige=Color.parseColor("#e4d599");
    private int colorIntensePurple=Color.parseColor("#824d90");
    private int colorCautiousRed=Color.parseColor("#f1471d");


    private View myView;
    private boolean toggleDayNight = false;

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
            backgroundPaint.setColor(Color.parseColor("#AA000000"));

            colorLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            colorLinePaint.setStyle(Paint.Style.STROKE);
            colorLinePaint.setStrokeWidth(5);
            colorLinePaint.setColor(Color.BLACK);

            colorLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            colorLinePaint.setStyle(Paint.Style.STROKE);
            colorLinePaint.setStrokeWidth(5);
            colorLinePaint.setColor(defaultColor);

            colorl0paint = new Paint();
            colorl0paint.setStyle(Paint.Style.FILL);
            colorl0paint.setColor(colorCautiousRed);

            colorl1paint = new Paint();
            colorl1paint.setStyle(Paint.Style.FILL);
            colorl1paint.setColor(colorIntensePurple);

            colorl2paint = new Paint();
            colorl2paint.setStyle(Paint.Style.FILL);
            colorl2paint.setColor(colorLogoTurquoise);

            colorl3paint = new Paint();
            colorl3paint.setStyle(Paint.Style.FILL);
            colorl3paint.setColor(colorFlowTurquoise);

            colorl4paint = new Paint();
            colorl4paint.setStyle(Paint.Style.FILL);
            colorl4paint.setColor(colorCalmTurquoise);

            colorl5paint = new Paint();
            colorl5paint.setStyle(Paint.Style.FILL);
            colorl5paint.setColor(colorGroundedBeige);

            double w = getWidth();
            double h = getHeight();
            double ox = w/2;
            double oy = h/2;
            double r = Math.min(ox,oy)-20;
            double rmax = r;
            double rmin = 30.0;



            ticksPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            ticksPaint.setStrokeWidth(3.0f);
            ticksPaint.setStyle(Paint.Style.STROKE);
            ticksPaint.setColor(colorCautiousRed);
            ticksPaint.setTextSize(60);

            RectF circle = getCircle(canvas, 1);
            canvas.drawArc(circle, 90, 360, true, colorl0paint);

            RectF Outcircle = getCircle(canvas, 1);
            canvas.drawArc(Outcircle, 90, 360, true, colorLinePaint);

            RectF innerCircle1 = getCircle(canvas, 0.9f);
            canvas.drawArc(innerCircle1, 90, 360, true, colorl1paint);

            RectF innerCircle2 = getCircle(canvas, 0.8f);
            canvas.drawArc(innerCircle2, 90, 360, true, colorl2paint);

            RectF innerCircle3 = getCircle(canvas, 0.7f);
            canvas.drawArc(innerCircle3, 90, 360, true, colorl3paint);

            RectF innerCircle4 = getCircle(canvas, 0.6f);
            canvas.drawArc(innerCircle4, 90, 360, true, colorl4paint);

            RectF innerCircle5 = getCircle(canvas, 0.5f);
            canvas.drawArc(innerCircle5, 90, 360, true, colorl5paint);

            //fillCircle(canvas,(float)ox,(float)oy,(float)(1*rmax),colorl0paint);

            Path path=new Path();
            List<MMPointer> data = simulateData();
            Double tprev=null;
            double t,v;
            for(MMPointer p:data){
                t=p.t;
                v=p.v;
                double a= ( 3 * Math.PI/2) - t * 2* Math.PI / (12*3600);
                double rv = rmin + v/100*(rmax-rmin);
                double xv = ox + (float)(rv * Math.cos(a));
                double yv = oy - (float)(rv * Math.sin(a));
                if(tprev == null) {
                    path.moveTo((float) ox, (float) oy);
                } else if (t - tprev.doubleValue() > 120) {
                    path.lineTo((float) ox, (float) oy);
                }
                path.lineTo((float) xv, (float) yv);
                tprev = t;
            }
            path.close();
            canvas.drawPath(path,backgroundPaint);

            //drawTicks(canvas);

            /*

            Resources res = getResources();
            Bitmap bitmap = toggleDayNight ? BitmapFactory.decodeResource(res, R.drawable.moon) : BitmapFactory.decodeResource(res, R.drawable.sun);

            canvas.drawBitmap(bitmap, circle.centerX()-20,circle.centerY()-20, ticksPaint);
            toggleDayNight = !toggleDayNight;
            */

        }


        private void fillCircle(Canvas canvas,float x, float y, float radius, Paint paint) {
            Path p=new Path();
            p.addArc(new RectF(0, 0, getWidth(),getWidth()), 0.0f, 360.0f);
            p.close();
            canvas.drawPath(p,paint);
            //canvas.save();
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
            int tickCount = 0;
            while (currentAngle <= 450) {
                canvas.drawLine(
                        (float) (oval.centerX() + Math.cos((180 - currentAngle) / 180 * Math.PI) * (radius - majorTicksLength / 2)),
                        (float) (oval.centerY() - Math.sin(currentAngle / 180 * Math.PI) * (radius - majorTicksLength / 2)),
                        (float) (oval.centerX() + Math.cos((180 - currentAngle) / 180 * Math.PI) * (radius + majorTicksLength / 2)),
                        (float) (oval.centerY() - Math.sin(currentAngle / 180 * Math.PI) * (radius + majorTicksLength / 2)),
                        ticksPaint
                );
                if (tickCount == 12 || tickCount == 3 || tickCount == 6 || tickCount == 9)
                    canvas.drawText(String.valueOf(tickCount == 0 ? 12 : tickCount),
                            (float) (oval.centerX() + Math.cos((180 - currentAngle) / 180 * Math.PI) * (radius - majorTicksLength / 2)),
                            (float) (oval.centerY() - Math.sin(currentAngle / 180 * Math.PI) * (radius + majorTicksLength / 2)), ticksPaint);
                tickCount++;
                currentAngle += majorStep;

            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            //return super.onTouchEvent(event);

            if (event.getAction() == MotionEvent.ACTION_UP && event.getX() > getWidth() / 2 - 40 && event.getX() < getWidth() / 2 + 40 && event.getY() > getHeight() / 2 - 40 && event.getY() < getHeight() / 2 + 40)
                //Log.d("onTouchEvent",event.getX()+"," + event.getY());
                myView.invalidate();
            return true;
        }
    }

    private List<MMPointer> simulateData() {
        List<MMPointer> data = new ArrayList<>();
        double t = 0.0;
        double v = 0.0;
        while (t < 24 * 3600) {
            if (Math.random() < 0.01) {
                double jump = 60 + Math.random() * 1 * 3600;
                t += jump;
                continue;
            }
            if (data.size() == 0) {
                data.add(new MMPointer(t, 50.0));
            } else {
                double rv = v + 20 * Math.random() - 10;
                if (rv < 0)
                    rv = 0;
                else if (rv > 100)
                    rv = 100;
                data.add(new MMPointer(t, v));
                v = rv;

            }
            t += 60;
        }

        return data;
    }


    class MMPointer {
        private double t;
        private double v;

        public double getT() {
            return t;
        }

        public double getV() {
            return v;
        }

        MMPointer(double t, double v) {
            this.t = t;
            this.v = v;
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
