package com.franz.indovinailnumero.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import com.franz.indovinailnumero.app.util.SoundPoolHelper;

import java.util.Random;

import static java.lang.Integer.parseInt;


public class LevelGame extends ActionBarActivity {
    CustomView cv;
    public int fine,guess;
    public final  int inizio=1;
    public int range;
    public int i = 5;
    public int attempts = 0;
    public boolean finito = false;
    public SoundPoolHelper mp;
    int fail, error, applausi,tock;
    EditText edit;
    //max e min mi servono come estremi e la textview si chiama "estremi"
    int min=1;
    int max;
    //boolean per manididio (mi setta mani a true), su checkwin se mani==true allora fa check dei 3 numeri altrimenti normale
    boolean mani=false;
    //Variabile locale in cui è salvato il punteggio
    TextView points;
    int punteggio;
    //Powerup usato?
    boolean aiuto_guardone=false;
    boolean powerup = true;
    int r;

    //boolean monouso
    boolean monousoLanterna=true;
    boolean monousoGuardone=true;
    boolean monousoPergamena=true;
    boolean monousoYinYang=true;
    boolean monousoManiDiDIo=true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //Forza la portrait mode
        setContentView(R.layout.activity_level_game);
        cv=(CustomView)findViewById(R.id.customView);
        points= (TextView) findViewById(R.id.soldi);

        TextView estremi=(TextView)findViewById(R.id.estremi);

        //Prendo il punteggio salvato
        getPoint();
        //setPoint(100);
        Intent intent = getIntent();
        /*
        String i = intent.getStringExtra(MainActivity.INIZIO);
        inizio = Integer.parseInt(i);
        */
        String d =intent.getStringExtra(SceltaLivelli.FINE);
        if(d!=null) {
            fine = parseInt(d);
        }

        String f = intent.getStringExtra(MainActivity.FINE);
        if(f!=null) {
            fine = parseInt(f);
        }

        max=fine;
        estremi.setText(min+"-"+max);
        range=fine-inizio+1;
        cv.range=range;
        Random random = new Random();
        guess = random.nextInt(fine - inizio + 1) + inizio;



        Log.d("guess", "Numero generato: " + guess);
        //Inizializzo i suoni
       /* mp = new SoundPoolHelper(1, this);
        fail = mp.load(this, R.raw.fail, 1);
        error = mp.load(this, R.raw.error, 1);
        applausi = mp.load(this, R.raw.applausi, 1);
        tock = mp.load(this,R.raw.tock, 1);*/
        //blocco la tastiera
        edit = (EditText) findViewById(R.id.editText3);
        edit.setKeyListener(null);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify inizio parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Log.d("settings", "click");
            return true;
        }
        if (id == R.id.action_ricomincia) {
            ricomincia();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void ricomincia(){
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(LevelGame.this);
        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Vuoi rigiocare?")
                .setTitle("Partita Conclusa");
        builder.setPositiveButton("Si",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       /* mp.stop(applausi);
                        mp.stop(fail);

                        mp.unload(error);
                        mp.unload(fail);
                        mp.unload(applausi);

                        mp.release();*/
                        setResult(1);
                        finish();
                    }
                }
        );
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(finito) {
                           /* mp.stop(applausi);
                            mp.stop(fail);

                            mp.unload(error);
                            mp.unload(fail);
                            mp.unload(applausi);

                            mp.release();*/
                            setResult(0);
                            finish();
                        }
                        dialogInterface.cancel();
                    }
                }
        );
        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void ricomincia(View view) {
        if (finito) {
            ricomincia();
        }

    }
    private void checkWin(int guess) {

        TextView tentativi = (TextView) findViewById(R.id.textView2);
        TextView alto_basso = (TextView) findViewById(R.id.textView);
        TextView estremi = (TextView) findViewById(R.id.estremi);

        attempts++;
        if (guess == this.guess) {
            endGame(true);
        } else {
            i--;
            if (i > 0)
                //mp.play(error);
            edit.setText("");
            tentativi.setText(i + " tentativi");
            if (guess < this.guess) {
                alto_basso.setText("Troppo basso!");
                if(aiuto_guardone==true){
                    Guardone();
                }

                if (guess < min) {
                    estremi.setText(min + "-" + max);
                    if (guess <= fine)
                        cv.updatePosition(false, guess);
                } else {
                    min = guess + 1;
                    estremi.setText(min + "-" + max);
                    if (guess <= fine)
                        cv.updatePosition(false, guess);
                }
            } else {
                alto_basso.setText("Troppo alto!");
                if(aiuto_guardone==true){
                    Guardone();
                }

                if (guess > max) {
                    estremi.setText(min + "-" + max);
                    if (guess >= inizio)
                        cv.updatePosition(true, guess);

                } else {
                    max = guess - 1;
                    estremi.setText(min + "-" + max);
                    if (guess >= inizio)
                        cv.updatePosition(true, guess);
                }
            }

        if (i == 0) {
            endGame(false);
        }
    }




    }
    public void checkWin(View view) {

        Button inserisci = (Button) findViewById(R.id.button);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inserisci.getWindowToken(), 0);

        TextView edit = (TextView) findViewById(R.id.editText3);
        String guess1 = edit.getText() + "";
        powerup=true;

        if(mani==false) {
            try {
                r = parseInt(guess1);
                checkWin(r);

            }//fine try
            catch (Exception e) {
                e.printStackTrace();
                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(LevelGame.this);

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage("Devi inserire un numero!")
                        .setTitle("Errore!");

                builder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }
                );

                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();
            }//Fine Catch
        }else if(mani==true){
            try {
                r = parseInt(guess1);
                i+=2;
                checkWin(r);
                checkWin((r-1)%range);
                checkWin((r+1)%range);
                mani=false;

            }//fine try
            catch (Exception e) {
                e.printStackTrace();
                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(LevelGame.this);

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage("Devi inserire un numero!")
                        .setTitle("Errore!");

                builder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }
                );

                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();
            }//Fine Catch
        }

    } //fine funzione

    public void endGame(boolean win) { //True = vittoria,False = sconfitta
        finito = true;
        TextView tentativi = (TextView) findViewById(R.id.textView2);
        TextView vinto = (TextView) findViewById(R.id.textView4);
        TextView numero = (TextView) findViewById(R.id.textView5);
        EditText edit = (EditText) findViewById(R.id.editText3);
        TextView indovina = (TextView) findViewById(R.id.textView);
        Button hai_vinto = (Button) findViewById(R.id.button);
        ViewGroup layout_edit = (ViewGroup) edit.getParent();
        ViewGroup layout_bottone = (ViewGroup) hai_vinto.getParent();
        TextView estremi=(TextView)findViewById(R.id.estremi);

        tentativi.setText("");
        indovina.setText("");
        if (layout_edit != null) {
            layout_edit.removeView(edit);
        }
        if (layout_bottone != null) {
            layout_bottone.removeView(hai_vinto);
        }
        vinto.setVisibility(View.VISIBLE);
        vinto.setText("Hai\n" + (win ? "vinto" : "perso"));

        //mp.play(win ? applausi : fail);
        if (win) {
            estremi.setVisibility(View.INVISIBLE);
            numero.setText("Il tuo punteggio è: " + gain(true));
            setPoint(punteggio+gain(true));
        } else {
            estremi.setVisibility(View.INVISIBLE);
            numero.setText("Il numero da indovinare era\n" + this.guess + " Punteggio è "+gain(false));
            setPoint(punteggio+gain(true));

        }
    }
    // Will be called for every Button that is clicked
    public void input(View v){
        //mp.play(tock);
        switch ( v.getId()) {
            case R.id.tasto1: edit.append("1");
                break;
            case R.id.tasto2:   edit.append("2");
                break;
            case R.id.tasto3:  edit.append("3");
                break;
            case R.id.tasto4:   edit.append("4");
                break;
            case R.id.tasto5:  edit.append("5");
                break;
            case R.id.tasto6:  edit.append("6");
                break;
            case R.id.tasto7:   edit.append("7");
                break;
            case R.id.tasto8:   edit.append("8");
                break;
            case R.id.tasto9:  edit.append("9");
                break;
            case R.id.tasto0:  edit.append("0");
                break;
            case R.id.tasto00:  edit.append("00");
                break;
            case R.id.tastoC:
                edit.setText(removeLastChar(edit.getText()+""));
           /*
            default: monthString = "Invalid month";
                break;*/
        }


        Log.v("APP", "Pressed: "+v.getTag());
    }
    //tastoC
    public String removeLastChar(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        return s.substring(0, s.length() - 1);
    }

    private void Lanterna(){
        if(monousoLanterna==true) {
            TextView tentativi = (TextView) findViewById(R.id.textView2);
            i++;
            tentativi.setText(i + " tentativi");
            Toast toast = Toast.makeText(this, "Hai usato 'Lanterna'", Toast.LENGTH_LONG);
            toast.show();
            monousoLanterna=false;

        }else{
            Toast toast = Toast.makeText(this, "'Lanterna' è già stata usata", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void Pergamena(){
        if(monousoPergamena==true) {
            int numero_cifre = (int) (Math.floor(Math.log10(guess)) + 1);
            Random random = new Random();
            int index = random.nextInt(numero_cifre);
            String hint = ("" + guess).substring(index, index + 1);
            Toast toast = Toast.makeText(this, "Una cifra della parola è " + hint, Toast.LENGTH_LONG);
            toast.show();
            Log.d("Pergamena: ", hint);
            monousoPergamena=false;
        }else{
            Toast toast = Toast.makeText(this, "'Pergamena' è già stata usata", Toast.LENGTH_LONG);
            toast.show();
        }


    }



    private void ManiDiDio(){
        if(monousoManiDiDIo==true) {
            mani = true;
            Toast toast = Toast.makeText(this, "Stai usando 'Mani Di Dio'", Toast.LENGTH_LONG);
            toast.show();
            monousoManiDiDIo=false;
        }else {
            Toast toast = Toast.makeText(this, "'Mani di Dio' è già stata usato", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void AiutoGuardone(){
        if(monousoGuardone==true) {
            aiuto_guardone = true;
            Toast toast = Toast.makeText(this, "Inserisci un numero per il Guardone", Toast.LENGTH_LONG);
            toast.show();
            i++;
            monousoGuardone=false;
        }else {
            Toast toast = Toast.makeText(this, "'Guardone' è già stata usato", Toast.LENGTH_LONG);
            toast.show();
        }
    }
    private void Guardone() {
    float distguess=0;
    float dist= (max-min)/2;
    if (r > this.guess) {
        distguess = r - this.guess;
    } else if (this.guess > r) {
        distguess = (this.guess - r);
    }

    if (dist <= distguess){
        Toast toast = Toast.makeText(this, "Il numero inserito è LONTANO", Toast.LENGTH_LONG);
        toast.show();
    } else if(dist>distguess){
            Toast toast = Toast.makeText(this, "Il numero inserito è VICINO", Toast.LENGTH_LONG);
            toast.show();

    }
    aiuto_guardone=false;
   }

    public void PowerUp(View v) {
        if (!finito&&powerup) {
            powerup=false;
            switch (v.getId()) {
                case R.id.lanterna:

                    if (punteggio >= 400) {
                        setPoint((int) (punteggio - 400));
                        Lanterna();
                    }
                    break;
                case R.id.pergamena:
                    Pergamena();


                    break;
                case R.id.manididio:

                    ManiDiDio();
                    break;
                case R.id.yinyang:
                    break;
                case R.id.guardone:

                    if (punteggio >= 800) {
                        setPoint((int) (punteggio - 800));
                        AiutoGuardone();

                    }

                    break;
            }
        }else{
            Toast toast = Toast.makeText(this, "Non puoi usare i poteri per questo turno", Toast.LENGTH_LONG);
            toast.show();
        }
    }
    public void getPoint(){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        punteggio = sharedPref.getInt(getString(R.string.punteggio), 1000);
        points.setText(""+punteggio);
    }
    public void setPoint(int newpoint){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.punteggio), newpoint);
        editor.commit();
        getPoint();
    }
    public int gain(boolean win){
        int gain;
        if(win){
            gain = ((i+1)*range)/2;
            return  gain;
        }else{
            gain = (range - (max-min))/2 ;
            return  gain;

        }

    }
}


