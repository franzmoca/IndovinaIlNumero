package com.franz.indovinailnumero.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.franz.indovinailnumero.app.R;

import java.util.Random;

/**
 * Created by franz on 19/05/14.
 */
public class CustomView extends View {

    Bitmap mBmp;
    Random mRnd;
    Paint mPaint;

    int w,h,bw,bh;
    int px=-1,py=-1;
    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mBmp= BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);      //carichiamo l'immagine in una bitmap
        bw=mBmp.getWidth(); //larghezza bitmap
        bh=mBmp.getHeight();//altezza
        mPaint=new Paint(); // pennello
        mPaint.setColor(Color.CYAN);
        mPaint.setAntiAlias(true);
        mRnd=new Random();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);


        if(px==-1&&py==-1){ // se non abbiamo ancora settato le coordinate, posizioniamo la bmp al centro
            px=w/2-bw/2; //metà della larghezza view, meno metà della figura
            py=h/2-bh/2; //metà dell'altezza view, meno metà della figura
        }


        canvas.drawCircle(px+(bw/2), py+(bh/2), 70, mPaint); //disegnamo un cerchio con centro al centro della bitmap
        canvas.drawBitmap( // disegnamo la bitmap
                mBmp,
                px,
                py,
                null);


    }


    public void updatePosition(){
        //posizione random x,y della bitmap all interno della view
        px=mRnd.nextInt(w-bw);
        py=mRnd.nextInt(h-bh);
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