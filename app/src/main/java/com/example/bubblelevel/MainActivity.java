package com.example.bubblelevel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;




public class MainActivity extends AppCompatActivity {
    int height = 0;
    int width = 0;
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
            paint.setColor(Color.rgb(255, 0, 0));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(0, 100, 100, 1000, paint);

            paint.setColor(Color.rgb(0, 255, 0));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(width - 50, 350, 50, 100, paint);
            paint.setColor(Color.rgb(0, 0, 0));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(width - 50, 350, 50, 100, paint);
            paint.setColor(Color.rgb(0, 255, 255));
            canvas.drawRoundRect(width - 50, 350, 50, 100, 75, 75, paint);
            //drawSquare(canvas);
            paint.setColor(Color.rgb(255, 255, 0));
            paint.setTextSize(100);
            canvas.drawText("4350 Lab-11", 0, 50, paint);
        }

        protected void drawSquare(Canvas canvas) {
            paint.setColor(Color.rgb(255, 0, 255));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(width - 50, 350, 50, 100, paint);
        }
    }
}