package com.example.urvish.canvasdemo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Canvas mCanvas;
    private Paint paint = new Paint();
    private Bitmap mBitmap;
    private ImageView mImageView;
    private Rect rect = new Rect();
    private Rect rect1 = new Rect();
    private final int OFFSET = 50;
    private int mOffset = OFFSET;
    private final int MULTIPLIER = 100;
    private int mBackgroundColor;
    private int mRectagleColor;
    private int mColorAccent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBackgroundColor = getResources().getColor(R.color.colorBackground, null);
        mColorAccent = getColor(R.color.colorAccent);
        mRectagleColor = getColor(R.color.colorRectangle);
        paint.setColor(mRectagleColor);
        mImageView = findViewById(R.id.canvasImageView);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int width = v.getWidth();
                int height = v.getHeight();
                int halfwidth = width / 2;
                int halfheight = height / 2;
                if (mOffset == OFFSET) {
                    mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    Log.d(TAG, "onClick: width " + width + " height" + height);
                    Log.d(TAG, "onClick: halfwidth " + halfwidth + " halfheight" + halfheight);
                    mImageView.setImageBitmap(mBitmap);
                    mCanvas = new Canvas(mBitmap);
                    mCanvas.drawColor(mBackgroundColor);
                    mOffset += OFFSET;
                    Log.d(TAG, "onClick: " + mOffset);

                } else {
                    if (mOffset < halfheight && mOffset < halfwidth) {
                        paint.setColor(mRectagleColor - MULTIPLIER * mOffset);
                        rect.set(mOffset, mOffset, width - mOffset, height - mOffset);
                        mCanvas.drawRect(rect, paint);
                        mOffset += OFFSET;
                        Log.d(TAG, "onClick: " + mOffset);
                    } else {
                        paint.setColor(mColorAccent);
                        mCanvas.drawCircle(halfwidth, halfheight, halfwidth / 4, paint);
                        Log.d(TAG, "onClick: " + mOffset);
                    }
                }
                v.invalidate();
            }
        });

    }
}
