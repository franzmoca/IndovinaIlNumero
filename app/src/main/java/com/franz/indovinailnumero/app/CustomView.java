package com.franz.indovinailnumero.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by franz on 19/05/14.
 */
public class CustomView extends View {


    RectF rect;
    int w, h, bw, bh;
    float iRedHight;
    float eRedHight;
    int lowNumber=0;
    int highNumber=0;
    float iRedLow;
    float eRedLow;
    float c = (float) 1.05;
    Paint paint1;//Verde
    Paint paint2;//Rosso
    Paint mPaintText;//Testo


    int range;
    float mRadius;
    float anello;


    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);

        iRedLow = 0;
        eRedLow = 0;
        iRedHight = 360;
        eRedHight = 0;
        paint1 = new Paint();
        paint2 = new Paint();
        mPaintText = new Paint();

        mPaintText.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintText.setColor(Color.WHITE);
        mPaintText.setTextSize(60f);
        rect = new RectF();


        //Example values
        paint1.setColor(Color.GREEN);
        paint1.setStrokeCap(Paint.Cap.BUTT);
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setAntiAlias(true);
        paint2.setColor(Color.RED);
        paint2.setAntiAlias(true);
        paint2.setStrokeCap(Paint.Cap.BUTT);
        paint2.setStyle(Paint.Style.STROKE);
        mPaintText.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintText.setColor(Color.WHITE);
        mPaintText.setTextSize(60f);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Log.d("On Draw ","h= "+h+" w= "+w);
        //Roba magica, non toccare! //Modificare c per ogni evenienza
        mRadius = (float) (c * (w / 2.7));
        anello = (float) (c * (mRadius / 2 - w / 10.8));
        //Log.d("On Draw","mRadius: "+mRadius+" anello: "+anello);
        // Log.d("On Draw","iGreen: "+iGreen+" eGreen: "+eGreen+ " iRedLow: "+iRedLow+" eRedLow: "+eRedLow);
        paint1.setStrokeWidth(anello); //Larghezza tratto
        paint2.setStrokeWidth(anello+15);


        rect.set(canvas.getWidth() / 2 - mRadius, canvas.getHeight() / 2 - mRadius, canvas.getWidth() / 2 + mRadius, canvas.getHeight() / 2 + mRadius);
        canvas.drawArc(rect, 0, 360, false, paint1);
        canvas.drawArc(rect, iRedLow-1, eRedLow, false, paint2);
        canvas.drawArc(rect, iRedHight, eRedHight, false, paint2);


    }


    public void updatePosition(boolean alto, int ins) {

        float n = (float) (ins * (360.0 / (float) range));
        Log.d("update: ", "n= " + n);

        if (alto) {
            if ((iRedHight+90) > n) {
                highNumber=ins;
                iRedHight = -90 + n;
                eRedHight = 360 - n;
                invalidate();
            }
        } else {
           if(eRedLow<n) {
               lowNumber=ins;
               iRedLow = -90;
               eRedLow = n;
               invalidate();
           }
        }

        //eGreen = iRedLow;
        Log.d("On Draw", "iRedHight: " + iRedHight + " eRedHight: " + eRedHight + " iRedLow: " + iRedLow + " eRedLow: " + eRedLow);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //registriamo le dimensioni della view
        w = MeasureSpec.getSize(widthMeasureSpec);
        h = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(w, h);
    }


}