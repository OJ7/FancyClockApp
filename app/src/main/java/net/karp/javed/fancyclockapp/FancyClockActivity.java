package net.karp.javed.fancyclockapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class FancyClockActivity extends Activity {

    final String TAG = "FancyClockActivity";
    int hours, minutes, seconds;

    // The Main view
    private LinearLayout mFrame;

    // Bubble image's bitmap
    private Bitmap mBitmap;

    // Display dimensions
    private int mDisplayWidth, mDisplayHeight;
    float x, y;

    Random rand = new Random();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fancy_clock);

        // Set up user interface
        mFrame = (LinearLayout) findViewById(R.id.linearLayout);

        // Load basic bubble Bitmap
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.b64);


        ClockView clockView = new ClockView(getApplicationContext(), 50, 50);
        BubbleView bView = new BubbleView(getApplicationContext(), x, y);
        mFrame.addView(clockView);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {

            // Get the size of the display so this View knows where borders are
            mDisplayWidth = mFrame.getWidth();
            mDisplayHeight = mFrame.getHeight();

        }
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
            mPainterHour.setColor(Color.BLACK);
            mPainterHour.setTextSize(100);
            mPainterHour.setTextAlign(Paint.Align.CENTER);
            mPainterHour.setTypeface(tf);

            mPainterMin.setStyle(Paint.Style.FILL_AND_STROKE);
            mPainterMin.setAntiAlias(true);
            mPainterMin.setColor(Color.BLACK);
            mPainterMin.setTextSize(70);
            mPainterMin.setTextAlign(Paint.Align.CENTER);
            mPainterMin.setTypeface(tf);

            updateClock();
        }


        @Override
        public void onDraw(Canvas canvas) {


            //canvas.drawPaint(mPainter);
            String hourString, minuteString, secondString;
            hourString = convert99(hours).toUpperCase();
            minuteString = convert99(minutes);

            canvas.drawText(hourString, 300, 200, mPainterHour);
            canvas.drawText(minuteString, 300, 300, mPainterMin);

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

                                    invalidate();

                                    BubbleView bView = new BubbleView(getApplicationContext(), 50, 50);
                                    Log.i(TAG, "Adding BubbleView to mFrame.");
                                    mFrame.addView(bView);
                                    Log.i(TAG, "Calling BubbleView.start()");
                                    bView.start();


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

            int[] funColors = {Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.CYAN, Color.DKGRAY, Color.WHITE};



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


    public class BubbleView extends View {

        private static final int BITMAP_SIZE = 64;
        private static final int REFRESH_RATE = 40;
        private final Paint mPainter = new Paint();
        private ScheduledFuture<?> mMoverFuture;
        private int mScaledBitmapWidth;
        private Bitmap mScaledBitmap;

        // location, speed and direction of the bubble
        private float mXPos, mYPos, mDx, mDy, mRadius, mRadiusSquared;
        private long mRotate, mDRotate;

        BubbleView(Context context, float x, float y) {
            super(context);
            Log.i(TAG, "Creating Bubble at: x:" + x + " y:" + y);

            // Create a new random number generator to
            // randomize size, rotation, speed and direction
            Random r = new Random();

            // Creates the bubble bitmap for this BubbleView
            createScaledBitmap(r);

            // Radius of the Bitmap
            mRadius = mScaledBitmapWidth / 2;
            mRadiusSquared = mRadius * mRadius;

            // Adjust position to center the bubble under user's finger
            mXPos = x - mRadius;
            mYPos = y - mRadius;

            // Set the BubbleView's speed and direction
            setSpeedAndDirection(r);

            // Set the BubbleView's rotation
            setRotation(r);

            mPainter.setAntiAlias(true);

        }

        private void setRotation(Random r) {

                mDRotate = 0;

        }

        private void setSpeedAndDirection(Random r) {

            mDx = r.nextInt(7) - 3;
            mDy = r.nextInt(7) - 3;

        }

        private void createScaledBitmap(Random r) {
            mScaledBitmapWidth = (r.nextInt(3)+1) * BITMAP_SIZE;
            mScaledBitmap = Bitmap.createScaledBitmap(mBitmap, mScaledBitmapWidth, mScaledBitmapWidth, false);
        }

        // Start moving the BubbleView & updating the display
        private void start() {

            // Creates a WorkerThread
            ScheduledExecutorService executor = Executors
                    .newScheduledThreadPool(1);

            // Execute the run() in Worker Thread every REFRESH_RATE
            // milliseconds
            // Save reference to this job in mMoverFuture
            mMoverFuture = executor.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {

                    // TODO - implement movement logic.
                    // Each time this method is run the BubbleView should
                    // move one step. If the BubbleView exits the display,
                    // stop the BubbleView's Worker Thread.
                    // Otherwise, request that the BubbleView be redrawn.
                    Log.i(TAG, "Entering BubbleView.start()");
                    //mXPos += mDx;
                    //mYPos += mDy;

                    if(moveWhileOnScreen())
                    {
                        //TODO - stop thread -- double check if this works
                        Log.i(TAG, "BubbleView isOutOfView...cancelling thread.");
                        //mMoverFuture.cancel(true);
                        stop(true);
                    }
                    else {
                        // TODO - redraw view -- currently throwing an error
                        Log.i(TAG, "BubbleView is in view.");
                    }
					/*	runOnUiThread(new Runnable() {
							public void run() {
								invalidate();
							    }
							});
					}*/
                }
            }, 0, REFRESH_RATE, TimeUnit.MILLISECONDS);
        }

        // Cancel the Bubble's movement
        // Remove Bubble from mFrame
        // Play pop sound if the BubbleView was popped

        private void stop(final boolean wasPopped) {
            Log.i(TAG, "Entering BubbleView.stop()");
            if (null != mMoverFuture) {

                if (!mMoverFuture.isDone()) {
                    mMoverFuture.cancel(true);
                }

                // This work will be performed on the UI Thread
                mFrame.post(new Runnable() {
                    @Override
                    public void run() {

                        // TO-DO - Remove the BubbleView from mFrame
                        Log.i(TAG, "Removing BubbleView from mFrame.");
                        mFrame.removeView(BubbleView.this);

                        // TO-DO - If the bubble was popped by user,
                        // play the popping sound
                        //mAudioManager.playSoundEffect(mSoundID);
                        Log.i(TAG, "Bubble removed from view!");
                    }
                });
            }
        }

        // Draw the Bubble at its current location
        @Override
        protected synchronized void onDraw(Canvas canvas) {

            // TO-DO - save the canvas
            Log.i(TAG, "Saving canvas.");
            canvas.save();

            // TO-DO - increase the rotation of the original image by mDRotate
            mRotate =+ mDRotate;

            // TODO Rotate the canvas by current rotation
            // Hint - Rotate around the bubble's center, not its position
            canvas.rotate(mRotate, mXPos + mRadius, mYPos + mRadius);


            // TO-DO - draw the bitmap at it's new location
            Log.i(TAG, "Drawing bitmap in new location.");
            canvas.drawBitmap(mScaledBitmap, mXPos, mYPos, mPainter);

            // TO-DO - restore the canvas
            Log.i(TAG, "Restoring canvas.");
            canvas.restore();

        }

        // Returns true if the BubbleView is still on the screen after the move
        // operation
        private synchronized boolean moveWhileOnScreen() {

            // TODO - Move the BubbleView -- modify this to make it move!!
            Log.i(TAG, "Moving BubbleView.");
            Log.i(TAG, "mXPos: " + mXPos + " -- mYPos:" + mYPos);
            Log.i(TAG, "mDx: " + mDx + " -- mDy:" + mDy);

            mXPos += mDx;
            mYPos += mDy;

            Log.i(TAG, "New Location for BubbleView");
            Log.i(TAG, "mXPos: " + mXPos + " -- mYPos:" + mYPos);

            postInvalidate();

            return isOutOfView();

        }

        // Return true if the BubbleView is still on the screen after the move
        // operation
        private boolean isOutOfView() {

            Log.i(TAG, "Checking if isOutOfView.");
            return mXPos + 2*mRadius< 0 || mXPos - mRadius > mDisplayWidth || mYPos + 2*mRadius < 0 || mYPos - mRadius > mDisplayHeight;

        }
    }

}
