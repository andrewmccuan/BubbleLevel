package com.example.bubblelevel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            canvas.drawRect(0, 0, 100, 100, paint);
            paint.setTextSize(100);
            canvas.drawText("4350 Lab-11", 0, 0, paint);
        }
    }
}