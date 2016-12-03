package com.moodmetric.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.format;
import static android.R.attr.x;
import static android.R.attr.y;

public class MainActivity extends AppCompatActivity {

    private Paint backgroundPaint;
    private Paint ticksPaint;
    private Paint colorLinePaint;
    private Paint colorCautiousRedPaint;
    private Paint colorIntensePurplePaint;
    private Paint colorLogoTurquoisePaint;
    private Paint colorFlowTurquoisePaint;
    private Paint colorCalmTurquoisePaint;
    private Paint colorGroundedBeigePaint;
    private Paint outerCirclePaint;

    private int colorLogoTurquoise = Color.parseColor("#008f67");
    private int colorFlowTurquoise = Color.parseColor("#4bb494");
    private int colorCalmTurquoise = Color.parseColor("#98c399");
    private int colorGroundedBeige = Color.parseColor("#e4d599");
    private int colorIntensePurple = Color.parseColor("#824d90");
    private int colorCautiousRed = Color.parseColor("#f1471d");

    private View FlowerView;

    public static final double DEFAULT_MAX_SPEED = 100.0;
    public static final double DEFAULT_MAJOR_TICK_STEP = 20.0;
    public static final int DEFAULT_MINOR_TICKS = 1;
    private int defaultColor = Color.rgb(180, 180, 180);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FlowerView = new MyView(getApplicationContext());
        setContentView(FlowerView);
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

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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

            colorCautiousRedPaint = new Paint();
            colorCautiousRedPaint.setStyle(Paint.Style.FILL);
            colorCautiousRedPaint.setColor(colorCautiousRed);

            colorIntensePurplePaint = new Paint();
            colorIntensePurplePaint.setStyle(Paint.Style.FILL);
            colorIntensePurplePaint.setColor(colorIntensePurple);

            colorLogoTurquoisePaint = new Paint();
            colorLogoTurquoisePaint.setStyle(Paint.Style.FILL);
            colorLogoTurquoisePaint.setColor(colorLogoTurquoise);

            colorFlowTurquoisePaint = new Paint();
            colorFlowTurquoisePaint.setStyle(Paint.Style.FILL);
            colorFlowTurquoisePaint.setColor(colorFlowTurquoise);

            colorCalmTurquoisePaint = new Paint();
            colorCalmTurquoisePaint.setStyle(Paint.Style.FILL);
            colorCalmTurquoisePaint.setColor(colorCalmTurquoise);

            colorGroundedBeigePaint = new Paint();
            colorGroundedBeigePaint.setStyle(Paint.Style.FILL);
            colorGroundedBeigePaint.setColor(colorGroundedBeige);

            ticksPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            ticksPaint.setStrokeWidth(3.0f);
            ticksPaint.setStyle(Paint.Style.STROKE);
            ticksPaint.setColor(colorCautiousRed);
            ticksPaint.setTextSize(60);

            double w = getWidth();
            double h = getHeight();
            double ox = w / 2;
            double oy = h / 2;
            double r = Math.min(ox, oy) - 20;
            double rmax = r;
            double rmin = 30.0;

            canvas.save();
            Path path = new Path();
            List<MMPointer> data = simulateData();
            Double tprev = null;
            double t, v;
            for (MMPointer p : data) {
                t = p.t;
                v = p.v;
                double a = (3 * Math.PI / 2) - t * 2 * Math.PI / (12 * 3600);
                double rv = rmin + v / 100 * (rmax - rmin);
                double xv = ox + (float) (rv * Math.cos(a));
                double yv = oy - (float) (rv * Math.sin(a));
                if (tprev == null) {
                    path.moveTo((float) ox, (float) oy);
                } else if (t - tprev.doubleValue() > 120) {
                    path.lineTo((float) ox, (float) oy);
                }
                path.lineTo((float) xv, (float) yv);
                tprev = t;
            }
            path.close();
            canvas.drawPath(path, backgroundPaint);
            canvas.clipPath(path);

            RectF circle = getCircle(canvas, 1);
            canvas.drawArc(circle, 90, 360, true, colorCautiousRedPaint);


            RectF Outcircle = getCircle(canvas, 1);
            canvas.drawArc(Outcircle, 90, 360, true, colorLinePaint);

            RectF innerCircle1 = getCircle(canvas, 0.9f);
            canvas.drawArc(innerCircle1, 90, 360, true, colorIntensePurplePaint);

            RectF innerCircle2 = getCircle(canvas, 0.8f);
            canvas.drawArc(innerCircle2, 90, 360, true, colorLogoTurquoisePaint);

            RectF innerCircle3 = getCircle(canvas, 0.7f);
            canvas.drawArc(innerCircle3, 90, 360, true, colorFlowTurquoisePaint);

            RectF innerCircle4 = getCircle(canvas, 0.6f);
            canvas.drawArc(innerCircle4, 90, 360, true, colorCalmTurquoisePaint);

            RectF innerCircle5 = getCircle(canvas, 0.5f);
            canvas.drawArc(innerCircle5, 90, 360, true, colorGroundedBeigePaint);

            canvas.restore();

            //draw centre button place
            canvas.save();
            Paint centerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            centerCirclePaint.setShadowLayer(100, 0, 0, Color.BLACK);
            centerCirclePaint.setColor(Color.WHITE);
            RectF innerCircle = getCircle(canvas, 0.18f);
            canvas.drawArc(innerCircle, 90, 360, true, centerCirclePaint);
            canvas.restore();

            //draw clock table
            outerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            outerCirclePaint.setStyle(Paint.Style.STROKE);
            outerCirclePaint.setStrokeWidth(5);
            outerCirclePaint.setColor(colorFlowTurquoise);
            outerCirclePaint.setTextSize(60);
            Path outerCirclePath = new Path();
            RectF outerCircle = getCircle(canvas, 1);
            outerCirclePath.addArc(outerCircle, 90, 360);
            outerCirclePath.moveTo((float) ox, (float) (oy + rmin));
            outerCirclePath.lineTo((float) ox, (float) (oy + r));
            canvas.drawPath(outerCirclePath, outerCirclePaint);

            //drawTicks(canvas);

            // Draw ticks

            int tickr = (int)(r+10);
            for(int i=0;i<=3;i++) {
                double angle = (i+1)*Math.PI/2;
                String tick = String.format("%d", 12-i*3);
                double tx = ox + tickr*(float)(Math.cos(angle))-40;// - sz.width/2
                double ty = oy - tickr*(float)(Math.sin(angle))-40;// - sz.height/2
                canvas.drawText(tick,(float) tx,(float) ty,outerCirclePaint);
            }
            Path tickPath=new Path();
            for(int i=0;i<=11;i++) {
                double angle = i*Math.PI/6;
                double x1 = ox + (r-5)*(float)(Math.cos(angle));
                double y1 = oy - (r-5)*(float)(Math.sin(angle));
                double x2 = ox + r*(float)(Math.cos(angle));
                double y2 = oy - r*(float)(Math.sin(angle));
                tickPath.moveTo((float) x1, (float) y1);
                tickPath.lineTo((float) x2, (float) y2);
                canvas.drawPath(tickPath,outerCirclePaint);
            }


            /*

            Resources res = getResources();
            Bitmap bitmap = toggleDayNight ? BitmapFactory.decodeResource(res, R.drawable.moon) : BitmapFactory.decodeResource(res, R.drawable.sun);

            canvas.drawBitmap(bitmap, circle.centerX()-20,circle.centerY()-20, ticksPaint);
            toggleDayNight = !toggleDayNight;
            */

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
            RectF oval = getCircle(canvas, 1f);
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
                            (float) (oval.centerY() - Math.sin(currentAngle / 180 * Math.PI) * (radius + majorTicksLength / 2)), outerCirclePaint);
                tickCount++;
                currentAngle += majorStep;

            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            //return super.onTouchEvent(event);

            if (event.getAction() == MotionEvent.ACTION_UP && event.getX() > getWidth() / 2 - 40 && event.getX() < getWidth() / 2 + 40 && event.getY() > getHeight() / 2 - 40 && event.getY() < getHeight() / 2 + 40)
                //Log.d("onTouchEvent",event.getX()+"," + event.getY());
                FlowerView.invalidate();
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
