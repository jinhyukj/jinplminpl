package com.example.paint;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.Draw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class PaintView extends View {

    public static int BRUSH_SIZE = 5;
    public static final int DEFAULT_COLOR = Color.RED;
    public static final int DEFAULT_BG_COLOR = Color.WHITE;
    private static final float TOUCH_TOLERANCE = 4;

    private float mX, mY;
    private Path mPath;
    private Paint mPaint;
    private int currentColor;
    private int backgroundColor = DEFAULT_BG_COLOR;
    private int strokeWidth;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);

    private ArrayList<Draw> paths = new ArrayList<>();
    private ArrayList<Draw> undo = new ArrayList<>();

    public PaintView(Context context) {
        super(context, null);

    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);

    }

    public void initialise (DisplayMetrics displayMetrics) {
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        currentColor = DEFAULT_COLOR;
        strokeWidth = BRUSH_SIZE;
    }

    public void Drawing(Bitmap bmp){
        mBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        mCanvas = new Canvas(mBitmap);
    }
    public Bitmap getBitmap(){
        return mBitmap;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        mCanvas.drawColor(backgroundColor); // WRONG
        for(Draw draw : paths) {
            mPaint.setColor(draw.color); // WRONG
            mPaint.setStrokeWidth(draw.strokeWidth);
            mPaint.setMaskFilter(null);
            mCanvas.drawPath(draw.path, mPaint);
        }
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.restore();
    }

    private void touchStart (float x, float y) {
        mPath = new Path();
        Draw draw = new Draw(currentColor, strokeWidth, mPath);
        paths.add(draw);
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove (float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touchUp () {
        mPath.lineTo(mX, mY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;
        }
        return true;
    }

    public void clear () {
        backgroundColor = DEFAULT_BG_COLOR;
        paths.clear();
        invalidate();

    }

    public void undo () {
        if (paths.size() > 0) {
            undo.add(paths.remove(paths.size() - 1));
            invalidate(); // add

        } else {
            Toast.makeText(getContext(), "Nothing to undo", Toast.LENGTH_LONG).show();
        }

    }

    public void redo () {
        if (undo.size() > 0) {
            paths.add(undo.remove(undo.size() - 1));
            invalidate(); // add

        } else {
            Toast.makeText(getContext(), "Nothing to undo", Toast.LENGTH_LONG).show();
        }

    }

    public void setStrokeWidth (int size) {
        strokeWidth = size;
    }

    public void setColor (int color) {
        currentColor = color;
    }

    public void saveImage() {
        ContentValues contentValues = new ContentValues(3);
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "Draw On Me");

        Uri imageFileUri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        try {
            OutputStream imageFileOS = getContext().getContentResolver().openOutputStream(imageFileUri);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 90, imageFileOS);
            Toast t = Toast.makeText(getContext(), "Saved!", Toast.LENGTH_SHORT);
            t.show();

        } catch (Exception e) {
            Log.v("EXCEPTION", e.getMessage());
        }

    }
}