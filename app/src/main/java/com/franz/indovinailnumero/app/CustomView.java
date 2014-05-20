package com.franz.indovinailnumero.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.franz.indovinailnumero.app.R;

import java.util.Random;

/**
 * Created by franz on 19/05/14.
 */
public class CustomView extends View {


    RectF rect;
    int w,h,bw,bh;
    float iGreen;
    float eGreen;
    float iRed;
    float eRed;
    int px=-1,py=-1;
    float c= (float) 1.05;
    Paint paint1 ;
    Paint paint2 ;
    Paint paint3 ;
    Paint paint4;
    Paint paint5;
    float mRadius;
    float anello;
    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        iGreen = 0;
        eGreen= 360;
        iRed=0;
        eRed=0;
        paint1 = new Paint();
        paint2 = new Paint();
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

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Log.d("On Draw ","h= "+h+" w= "+w);
        //Roba magica, non toccare! //Modificare c per ogni evenienza
        mRadius = (float) (c * (w/2.7));
        anello= (float) (c * (mRadius/2 - w/10.8));
        //Log.d("On Draw","mRadius: "+mRadius+" anello: "+anello);
       // Log.d("On Draw","iGreen: "+iGreen+" eGreen: "+eGreen+ " iRed: "+iRed+" eRed: "+eRed);
        paint1.setStrokeWidth(anello); //Larghezza tratto
        paint2.setStrokeWidth(anello);


        rect.set(canvas.getWidth()/2- mRadius, canvas.getHeight()/2 - mRadius, canvas.getWidth()/2 + mRadius, canvas.getHeight()/2 + mRadius);
        canvas.drawArc(rect, iGreen, eGreen, false, paint1);
        canvas.drawArc(rect, iRed, eRed, false, paint2);
        /*
        canvas.drawArc(rect, 120, 60, false, paint1);
        canvas.drawArc(rect, 180, 60, false, paint2);
        canvas.drawArc(rect, 240, 60, false, paint1);
        canvas.drawArc(rect, 300, 60, false, paint2);*/

/* Disegnare DENTRO il cerchio
paint4.setColor(Color.BLACK);
canvas.drawCircle(canvas.getWidth()/2, canvas.getHeight()/2, mRadius/2, paint4);
paint5.setColor(Color.YELLOW);
paint5.setStrokeWidth(3);
paint5.setAntiAlias(true);
paint5.setStrokeCap(Paint.Cap.BUTT);
paint5.setStyle(Paint.Style.STROKE);
canvas.drawCircle(canvas.getWidth()/2, canvas.getHeight()/2, mRadius/2, paint5);
*/

    }


    public void updatePosition(boolean alto, int ins, int range) {
        float n = (float) (ins * (360.0 /(float) range));
        Log.d("update: ", "n= " + n);
        if(alto){
            iRed =n;
            eRed = n;
        }else{

        }
            //eGreen = iRed;
        Log.d("On Draw","iGreen: "+iGreen+" eGreen: "+eGreen+ " iRed: "+iRed+" eRed: "+eRed);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //registriamo le dimensioni della view
        w=MeasureSpec.getSize(widthMeasureSpec);
        h=MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(w,h);
    }


}