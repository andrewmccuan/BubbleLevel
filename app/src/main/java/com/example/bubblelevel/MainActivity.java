package com.example.bubblelevel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;




public class MainActivity extends AppCompatActivity {
    int height = 0;
    int width = 0;
    int deg = 0;
    float x = 0;
    float lr = 0;
    float ud = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        super.onCreate(savedInstanceState);
        //(R.layout.activity_main);
        myView v;
        v = new myView(this);
        setContentView(v);
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
            degText(canvas);

            canvas.translate(0, -350);
            canvas.rotate(deg + 0, width/2, height/2);
            level(canvas);
            canvas.rotate(-deg + 0, width/2, height/2);
            canvas.translate(0, 350);

            buttons(canvas);
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

        protected void level(Canvas canvas) {
            //Red border
            paint.setColor(Color.rgb(255, 0, 0));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(width/2 - 670, height/2 - 125, width/2 + 670, height/2 + 125, paint);

            //Green level
            paint.setColor(Color.rgb(0, 255, 0));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(width/2 - 660, height/2 - 115, width/2 + 660, height/2 + 115, paint);

            //Bubble call
            canvas.translate(lr + 0, ud + 0);
            bubble(canvas);
            canvas.translate(-lr + 0, -ud + 0);

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

        protected void bubble(Canvas canvas) {
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
            //Left button
            paint.setColor(Color.rgb(0, 200, 255));
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(150);
            canvas.drawRoundRect(20, height - 525, width/3 - 20, height - 300, 75, 75, paint);
            paint.setColor(Color.rgb(0, 0, 0));
            canvas.drawText("Tare", 80, height - 360, paint);

            //Center button
            paint.setColor(Color.rgb(0, 200, 255));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRoundRect(width/3 + 20, height - 525, width - (width/3) - 20, height - 300, 75, 75, paint);
            paint.setColor(Color.rgb(0, 0, 0));
            canvas.drawText("Type", width/2 - 160, height - 360, paint);

            //Right button
            paint.setColor(Color.rgb(0, 200, 255));
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(125);
            canvas.drawRoundRect(width - (width/3) + 20, height - 525, width - 20, height - 300, 75, 75, paint);
            paint.setColor(Color.rgb(0, 0, 0));
            canvas.drawText("Rotate", width/2 + 300, height - 360, paint);
        }

        public boolean onTouchEvent(MotionEvent event) {
            // get pointer index from the event object
            int pointerIndex = event.getActionIndex();
            float x = event.getX();
            float y = event.getY();

            // get pointer ID
            int pointerId = event.getPointerId(pointerIndex);

            // get masked (not specific to a pointer) action
            int maskedAction = event.getActionMasked();

            switch (maskedAction) {
                case MotionEvent.ACTION_DOWN: {
                    if (x > width - (width/3) + 20 && x < width - 20 && y > height - 525 && y < height - 300) {
                        switch (deg) {
                            case 0: {
                                deg = 45;
                                break;
                            }
                            case 45: {
                                deg = 90;
                                break;
                            }
                            case 90: {
                                deg = 0;
                                break;
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