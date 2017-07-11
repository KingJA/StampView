package com.kingja.stampview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

/**
 * Description:TODO
 * Create Time:2017/3/31 14:20
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class StampView extends View {

    private Paint mOutStrokePaint;
    private int mOutStrokeWidth=20;
    private int mInStrokeWidth=10;
    private int mOffSet;
    private int mWidth;
    private int mHeight;
    private float mTextSize;
    private Paint mInStrokePaint;
    private Paint mTextPaint;
    private float mTextHeightOffset;
    private int mBorderColor;
    private String mStampText;
    private Paint mSpotPaint;

    public StampView(Context context) {
        this(context,null);
    }

    public StampView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public StampView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StampView);
        mBorderColor = typedArray.getColor(R.styleable.StampView_stampColor, 0xffbb2c28);
        mStampText = typedArray.getString(R.styleable.StampView_stampText);
        mStampText=mStampText==null?"":mStampText;
        typedArray.recycle();
    }

    private void initStampView() {
        mSpotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSpotPaint.setColor(0xffffffff);


        mOutStrokePaint = new Paint();
        mOutStrokePaint.setColor(mBorderColor);
        mOutStrokePaint.setAntiAlias(true);
        mOutStrokePaint.setStrokeWidth(mOutStrokeWidth);
        mOutStrokePaint.setStyle(Paint.Style.STROKE);
        mOffSet = (int) (mOutStrokeWidth*0.5f);

        mInStrokePaint = new Paint();
        mInStrokePaint.setAntiAlias(true);
        mInStrokePaint.setColor(mBorderColor);
        mInStrokePaint.setStrokeWidth(mInStrokeWidth);
        mInStrokePaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mBorderColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setFakeBoldText(true);
        mTextHeightOffset = -(mTextPaint.ascent() + mTextPaint.descent()) * 0.5f;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mOutStrokeWidth= (int) (mWidth*1.0f/18);
        mInStrokeWidth= (int) (mWidth*1.0f/36);
        mTextSize=mWidth*1.0f/5;
        initStampView();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mWidth*0.5f,mWidth*0.5f,mWidth*0.5f-mOffSet,mOutStrokePaint);
        canvas.drawCircle(mWidth*0.5f,mWidth*0.5f,mWidth*0.5f-mOutStrokeWidth-mOutStrokeWidth,mInStrokePaint);

        float tabTextWidth = mTextPaint.measureText(mStampText);
        canvas.save();

        canvas.rotate(-30,mWidth*0.5f,mWidth*0.5f);
        canvas.drawText(mStampText, mWidth*0.5f - 0.5f * tabTextWidth, mHeight * 0.5f + mTextHeightOffset, mTextPaint);

        canvas.restore();
        drawSpot(canvas);

    }

    private void drawSpot(Canvas canvas) {
        for (int i = 0; i < 200; i++) {
            Path mirrorPath = getRandomSpotPath(3, new Random().nextInt(mWidth/2)+new Random().nextInt(mWidth/2), new Random()
                    .nextInt(mHeight/2)+new Random().nextInt(mHeight/2), new Random().nextInt(3) + 3);
            canvas.drawPath(mirrorPath,mSpotPaint);
        }
    }

    public Path getRandomSpotPath(int sides, int centerX, int centerY, int radius) {
        Path path = new Path();
        float offsetAngle = 0;
        offsetAngle = (float) (Math.PI * offsetAngle / 180);
        for (int i = 0; i < sides; i++) {
            float x = (float) (centerX + radius * Math.cos(offsetAngle));
            float y = (float) (centerY + radius * Math.sin(offsetAngle));
            offsetAngle += 2 * Math.PI / sides;
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x+new Random().nextInt(8)+3, y+new Random().nextInt(8)+3);
            }
        }
        path.close();
        return path;
    }

    public void reDraw() {
        invalidate();
    }
}
