package net.karp.javed.fancyclockapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;


public class FancyClockActivity extends Activity {

    final String TAG = "FancyClockActivity";
    int hours, minutes, seconds;

    // The Main view
    private LinearLayout mFrame;

    Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fancy_clock);

        // Set up user interface
        mFrame = (LinearLayout) findViewById(R.id.linearLayout);

        ClockView clockView = new ClockView(getApplicationContext(), 200, 200);
        mFrame.addView(clockView);

    }

    public class ClockView extends View {

        private final Paint mPainter = new Paint(), mPainterHour = new Paint(), mPainterMin = new Paint();
        Typeface tf = Typeface.create("Courier New",Typeface.NORMAL);



        ClockView(Context context, float x, float y) {
            super(context);

            mPainter.setStyle(Paint.Style.FILL_AND_STROKE);
            mPainter.setAntiAlias(true);
            mPainter.setColor(Color.BLUE);

            mPainterHour.setStyle(Paint.Style.FILL_AND_STROKE);
            mPainterHour.setAntiAlias(true);
            mPainterHour.setColor(Color.WHITE);
            mPainterHour.setTextSize(50);
            mPainterHour.setTextAlign(Paint.Align.CENTER);
            mPainterHour.setTypeface(tf);

            mPainterMin.setStyle(Paint.Style.FILL_AND_STROKE);
            mPainterMin.setAntiAlias(true);
            mPainterMin.setColor(Color.WHITE);
            mPainterMin.setTextSize(30);
            mPainterMin.setTextAlign(Paint.Align.CENTER);
            mPainterMin.setTypeface(tf);

            updateClock();
            startScale();
        }

        @Override
        public void onDraw(Canvas canvas) {
            canvas.drawPaint(mPainter);
            String hourString, minuteString, secondString;
            hourString = convert99(hours).toUpperCase();
            minuteString = convert99(minutes);

            canvas.drawText(hourString, 300, 100, mPainterHour);
            canvas.drawText(minuteString, 300, 200, mPainterMin);

        }

        public void startScale(){
            ScaleAnimation animation = new ScaleAnimation(1,4/3,1,4);
            animation.setDuration(61000);
            animation.setInterpolator(getApplicationContext(),android.R.interpolator.accelerate_decelerate);
            this.startAnimation(animation);

        }


        public void updateClock(){
            // Updates Simple Clock every second
            Thread t = new Thread() {

                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            Thread.sleep(1000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Calendar cal = Calendar.getInstance();
                                    Date time = cal.getTime();

                                    hours = cal.get(Calendar.HOUR_OF_DAY);
                                    minutes = cal.get(Calendar.MINUTE);
                                    seconds = cal.get(Calendar.SECOND);

                                    if(seconds == 59) {
                                        startScale();
                                    }

                                    invalidate();


                                }
                            });
                        }
                    } catch (InterruptedException e) {
                    }
                }
            };

            t.start();
        }


        // This snippet may be used freely, as long as the authorship note remains in the source code.

        private final String[] lowNames = {
                "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten",
                "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};

        private final String[] tensNames = {
                "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};

        /**
         * Converts an integer number into words (american english).
         * @author Christian d'Heureuse, Inventec Informatik AG, Switzerland, www.source-code.biz
         **/
/*
        public  String convertNumberToWords (int n) {
            if (n < 0) {
                return "minus " + convertNumberToWords(-n); }
            if (n <= 999) {
                return convert999(n); }
            String s = null;
            int t = 0;
            while (n > 0) {
                if (n % 1000 != 0) {
                    String s2 = convert999(n % 1000);
                    if (t > 0) {
                        s2 = s2 + " " + bigNames[t-1]; }
                    if (s == null) {
                        s = s2; }
                    else {
                        s = s2 + ", " + s; }}
                n /= 1000;
                t++; }
            return s; }

        // Range 0 to 999.
        private String convert999 (int n) {
            String s1 = lowNames[n / 100] + " hundred";
            String s2 = convert99(n % 100);
            if (n <= 99) {
                return s2; }
            else if (n % 100 == 0) {
                return s1; }
            else {
                return s1 + " " + s2; }}
*/

        // Range 0 to 99.
        private String convert99 (int n) {
            if (n < 20) {
                return lowNames[n]; }
            String s = tensNames[n / 10 - 2];
            if (n % 10 == 0) {
                return s; }
            return s + "-" + lowNames[n % 10]; }


        @Override
        public boolean onTouchEvent (MotionEvent motionEvent){
            float touchX = motionEvent.getX();
            float touchY = motionEvent.getY();

            int[] funColors = {Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.CYAN, Color.DKGRAY, Color.RED};

            switch (motionEvent.getAction()){

                case MotionEvent.ACTION_DOWN:
                    mPainter.setColor(Color.RED);
                    break;
                case MotionEvent.ACTION_MOVE:
                    //_path.lineTo(touchX, touchY);
                    mPainter.setColor(funColors[rand.nextInt(funColors.length-1)]);
                    break;
                case MotionEvent.ACTION_UP:
                    mPainter.setColor(Color.BLUE);
                    break;
            }

            invalidate();
            return true;

        }

    }


}
