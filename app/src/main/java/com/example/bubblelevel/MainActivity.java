package com.example.bubblelevel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;




public class MainActivity extends AppCompatActivity implements SensorEventListener{
    int height = 0;
    int width = 0;
    int deg = 0;
    int sensitivity = 45;
    float offsetX = 0;
    float offsetY = 0;
    int type = 1;
    float lHead = 0;
    float lPitch = 0;
    float lRoll = 0;
    float newX = 0;
    float newY = 0;
    private SensorManager lSensorManager;
    private Sensor lOrientation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        super.onCreate(savedInstanceState);
        myView v;
        v = new myView(this);
        setContentView(v);
        lSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lOrientation = lSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        lSensorManager.registerListener( this, lOrientation, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            lHead = event.values[0];
            lPitch = event.values[1];
            lRoll = event.values[2] - deg;
        }
        //Log.d("angles", String.valueOf(lRoll));
    }
    public void onResume() {
        super.onResume();
        lSensorManager.registerListener( this, lOrientation, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void onPause() {
        super.onPause();
        lSensorManager.unregisterListener( this);
    }

    class myView extends View {
        private Paint paint;

        public myView(Context context) {
            super(context);
            init();
        }

        private void init() {
            paint = new Paint();
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            invalidate(); //causes a canvas draw
        }

        protected void onDraw(Canvas canvas) {
            if (type == 0) {
                degText(canvas);

                canvas.translate(0, -350);
                canvas.rotate(deg + 0, width / 2, height / 2);
                straightLevel(canvas);
                canvas.rotate(-deg + 0, width / 2, height / 2);
                canvas.translate(0, 350);
            } else if(type == 1) {
                canvas.translate(0, -350);
                surfaceLevel(canvas);
                canvas.translate(0, 350);
            }
            buttons(canvas);
            invalidate();
        }
        protected void surfaceLevel(Canvas canvas) {
            //Red border
            paint.setColor(Color.rgb(255, 0, 0));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(width/2, height/2, 500, paint);
            //Green background
            paint.setColor(Color.rgb(0, 255, 0));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(width/2, height/2, 490, paint);

            //Bubble
            float rS = (float) (Math.pow((lRoll - offsetX) * sensitivity, 2) + Math.pow((lPitch - offsetY) * sensitivity, 2));
            rS = (float) Math.sqrt(rS);
            //Log.d("rS", String.valueOf(rS));

            if (rS >= 330) {
                float adjust = 330/rS;
                newX = ((lRoll - offsetX) * sensitivity) * adjust;
                newY = ((lPitch - offsetY) * sensitivity) * adjust;
                //Log.d("rS", "X: " + String.valueOf(newX));
                //Log.d("rS", "Y: " + String.valueOf(newY));
            } else {
                newX = (lRoll - offsetX) * sensitivity;
                newY = (lPitch - offsetY) * sensitivity;
            }
            canvas.translate(newX, newY);
            fullBubble(canvas);
            if (rS >= 330) {
                float adjust = 330/rS;
                newX = ((lRoll - offsetX) * sensitivity) * adjust;
                newY = ((lPitch - offsetY) * sensitivity) * adjust;
            } else {
                newX = (lRoll - offsetX) * sensitivity;
                newY = (lPitch - offsetY) * sensitivity;
            }
            canvas.translate(-newX, -newY);

            //Outer marker
            paint.setColor(Color.rgb(0, 0, 0));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10);
            canvas.drawCircle(width/2, height/2, 250, paint);
            //Inner marker
            paint.setColor(Color.rgb(0, 0, 0));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10);
            canvas.drawCircle(width/2, height/2, 100, paint);
        }

        protected void degText(Canvas canvas) {
            paint.setColor(Color.rgb(255, 255, 255));
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(300);
            if (deg < 10)
                canvas.drawText(String.valueOf(deg) + "°", width - 280, 250, paint);
            else
                canvas.drawText(String.valueOf(deg) + "°", width - 450, 250, paint);
        }

        protected void straightLevel(Canvas canvas) {
            //Red border
            paint.setColor(Color.rgb(255, 0, 0));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(width/2 - 670, height/2 - 125, width/2 + 670, height/2 + 125, paint);

            //Green level
            paint.setColor(Color.rgb(0, 255, 0));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(width/2 - 660, height/2 - 115, width/2 + 660, height/2 + 115, paint);

            //Bubble call
            if ((lRoll - offsetX) * sensitivity <= -445)
                canvas.translate(-445, 0);
            else if ((lRoll - offsetX) * sensitivity >= 445)
                canvas.translate(445, 0);
            else
                canvas.translate((lRoll - offsetX) * sensitivity, 0);
            halfBubble(canvas);
            if ((lRoll - offsetX) * sensitivity <= -445)
                canvas.translate(445, 0);
            else if ((lRoll - offsetX)  * sensitivity >= 445)
                canvas.translate(-445, 0);
            else
                canvas.translate(-(lRoll - offsetX) * sensitivity, 0);

            //Left marker
            paint.setColor(Color.rgb(0, 0, 0));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(width/2 - 660/3 - 5, height/2 - 115, width/2 - 660/3 + 5, height/2 + 115, paint);

            //Right marker
            paint.setColor(Color.rgb(0, 0, 0));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(width/2 + 660/3 - 5, height/2 - 115, width/2 + 660/3 + 5, height/2 + 115, paint);
            //Log.d("pixel", String.valueOf((width/2 - 660/3) - (width/2 - 660)));
        }

        protected  void fullBubble(Canvas canvas) {
            //Bubble outside
            paint.setColor(Color.rgb(0, 100, 0));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(width/2, height/2, 160, paint);
            //Bubble inside
            paint.setColor(Color.rgb(150, 255, 150));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(width/2, height/2, 150, paint);
        }
        protected void halfBubble(Canvas canvas) {
            //Outer arc
            paint.setColor(Color.rgb(0, 100, 0));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawArc(width/2 - 660/3 + 5, height/2 - 240, width/2 + 660/3 - 5, height/2 + 10, 0, 180, true, paint);

            //Inner arc
            paint.setColor(Color.rgb(150, 255, 150));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawArc(width/2 - 660/3 + 15, height/2 - 230, width/2 + 660/3 - 15, height/2, 0, 180, true, paint);
        }

        protected void buttons(Canvas canvas) {
            if (type == 0) {
                //Straight level
                //Tare button
                paint.setColor(Color.rgb(0, 200, 255));
                paint.setStyle(Paint.Style.FILL);
                paint.setTextSize(150);
                canvas.drawRoundRect(20, height - 525, width / 3 - 20, height - 300, 75, 75, paint);
                paint.setColor(Color.rgb(0, 0, 0));
                canvas.drawText("Tare", 80, height - 360, paint);

                //Type button
                paint.setColor(Color.rgb(0, 200, 255));
                paint.setStyle(Paint.Style.FILL);
                canvas.drawRoundRect(width / 3 + 20, height - 525, width - (width / 3) - 20, height - 300, 75, 75, paint);
                paint.setColor(Color.rgb(0, 0, 0));
                canvas.drawText("Type", width / 2 - 160, height - 360, paint);

                //Rotate button
                paint.setColor(Color.rgb(0, 200, 255));
                paint.setStyle(Paint.Style.FILL);
                paint.setTextSize(125);
                canvas.drawRoundRect(width - (width / 3) + 20, height - 525, width - 20, height - 300, 75, 75, paint);
                paint.setColor(Color.rgb(0, 0, 0));
                canvas.drawText("Rotate", width / 2 + 300, height - 360, paint);
            } else if (type == 1) {
                //Surface level
                //Tare button
                paint.setColor(Color.rgb(0, 200, 255));
                paint.setStyle(Paint.Style.FILL);
                paint.setTextSize(150);
                canvas.drawRoundRect(width/4 - 220, height - 525, width/4 + 220, height - 300, 75, 75, paint);
                paint.setColor(Color.rgb(0, 0, 0));
                canvas.drawText("Tare", width/4 - 150, height - 360, paint);

                //Type button
                paint.setColor(Color.rgb(0, 200, 255));
                paint.setStyle(Paint.Style.FILL);
                canvas.drawRoundRect(width/4*3 - 220, height - 525, (width/4)*3 + 220, height - 300, 75, 75, paint);
                paint.setColor(Color.rgb(0, 0, 0));
                canvas.drawText("Type", width/4*3 - 150, height - 360, paint);
            }
        }

        public boolean onTouchEvent(MotionEvent event) {
            // get pointer index from the event object
            int pointerIndex = event.getActionIndex();
            float x = event.getX();
            float y = event.getY();

            // get masked (not specific to a pointer) action
            int maskedAction = event.getActionMasked();

            switch (maskedAction) {
                case MotionEvent.ACTION_DOWN: {
                    if (type == 0) {
                        //Tare button
                        if (x > 20 && x < width / 3 - 20 && y > height - 525 && y < height - 300) {
                            offsetX = lRoll;
                            //Log.d("offset", String.valueOf(offsetX));
                        }
                        //Type button
                        if (x > width / 3 + 20 && x < width - (width / 3) - 20 && y > height - 525 && y < height - 300) {
                            switch (type) {
                                case 0: {
                                    type = 1;
                                    deg = 0;
                                    offsetX = 0;
                                    offsetY = 0;
                                    break;
                                }
                                case 1: {
                                    type = 0;
                                    deg = 0;
                                    offsetX = 0;
                                    offsetY = 0;
                                    break;
                                }
                            }
                        }
                        //Rotate button
                        if (x > width - (width / 3) + 20 && x < width - 20 && y > height - 525 && y < height - 300) {
                            switch (deg) {
                                case 0: {
                                    deg = 45;
                                    offsetX = 0;
                                    offsetY = 0;
                                    break;
                                }
                                case 45: {
                                    deg = 0;
                                    offsetX = 0;
                                    offsetY = 0;
                                    break;
                                }
                            }
                        }
                    }
                    if (type == 1) {
                        //Tare button
                        if (x > width/4 - 220 && x < width/4 + 220 && y > height - 525 && y < height - 300) {
                            offsetX = lRoll;
                            offsetY = lPitch;
                            //Log.d("offset", String.valueOf(offsetX));
                        }
                        //Type button
                        if (x > width/4*3 - 220 && x < (width/4)*3 + 220 && y > height - 525 && y < height - 300) {
                            switch (type) {
                                case 0: {
                                    type = 1;
                                    deg = 0;
                                    offsetX = 0;
                                    offsetY = 0;
                                    break;
                                }
                                case 1: {
                                    type = 0;
                                    deg = 0;
                                    offsetX = 0;
                                    offsetY = 0;
                                    break;
                                }
                            }
                        }
                    }
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    break;
                }
            }
            invalidate();
            return true;
        }
    }
}