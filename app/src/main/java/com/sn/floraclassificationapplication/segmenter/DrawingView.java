package com.sn.floraclassificationapplication.segmenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


/**
 * Drawing view to enable hand drawing on an image.
 * Used to erase image parts by coloring with transparent color.
 */
public class DrawingView extends View {

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    Context context;
    private Paint circlePaint;
    private Path circlePath;
    private Paint mPaint;
    private Bitmap image;
    private int w;
    private int h;
    private ImageController ic = ImageController.getInstance();


    public DrawingView(Context context) {
        this(context, null);
    }

    public DrawingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.context=context;
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        circlePaint = new Paint();
        circlePath = new Path();

        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.BLUE);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.MITER);
        circlePaint.setStrokeWidth(4f);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.TRANSPARENT);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(36);
    }


    /**
     * Save parameters if image size is changed
     * @param w - image width
     * @param h - image height
     * @param oldw - old width
     * @param oldh - old height
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;

        if (image!= null) {
            mBitmap = image.copy(Bitmap.Config.ARGB_8888, true);
            mBitmap = ic.getResizedBitmap(mBitmap, h, w);
            mCanvas = new Canvas(mBitmap);
        }
    }

    /**
     * Set image for drawing/cleaning
     * @param image - image to draw on
     */
    protected  void setImage(Bitmap image) {
        this.image = image;
        mBitmap = image.copy(Bitmap.Config.ARGB_8888, true);
        mCanvas = new Canvas(mBitmap);
        invalidate();
    }

    /**
     * On draw event
     * @param canvas - canvas to paint on
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath, mPaint);
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;

            circlePath.reset();
            //circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
        }
    }

    /**
     * Hand touch up event. commit changes to canvas.
     */
    private void touch_up() {
        mPath.lineTo(mX, mY);
        circlePath.reset();
        // commit the path to our offscreen
        mCanvas.drawPath(mPath,  mPaint);
        // kill this so we don't double draw
        mPath.reset();
    }

    /**
     * start to touch the screen. record movements
     * @param event
     * @return true (required)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }

    /**
     * clear changes.
     */
    public void clear(){
        mBitmap = image.copy(Bitmap.Config.ARGB_8888, true);
        mBitmap = ic.getResizedBitmap(mBitmap, h, w);
        mCanvas = new Canvas(mBitmap);
        invalidate();
    }

    /**
     * resize the image to it's original size
     * @return bitmap with changes
     */
    public Bitmap saveImage() {
        return ic.getResizedBitmap(mBitmap, image.getHeight(), image.getWidth());
    }
}