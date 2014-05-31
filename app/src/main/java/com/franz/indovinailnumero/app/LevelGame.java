package com.franz.indovinailnumero.app;

import android.app.Activity;
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
import android.support.v7.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;

import com.franz.indovinailnumero.app.util.LabeledImageView;
import com.franz.indovinailnumero.app.util.SoundPoolHelper;
import android.os.Vibrator;

import java.util.Random;

import static java.lang.Integer.parseInt;


public class LevelGame extends Activity {
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
    int powerup_usati=0;
    boolean won = false;
    int r;
    int livello=0;

    //boolean monouso
    boolean monousoLanterna=true;
    boolean monousoGuardone=true;
    boolean monousoPergamena=true;
    boolean monousoYinYang=true;
    boolean monousoManiDiDIo=true;

   // MediaPlayer mpAudio;
    MediaPlayer mpAudio;
    boolean tastoswitch=true;

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
        //setPoint(200);
        Intent intent = getIntent();

        String d =intent.getStringExtra(SceltaLivelli.FINE);
        if(d!=null) {
            fine = parseInt(d);
        }
        String level =intent.getStringExtra(SceltaLivelli.LIVELLO);
        if(level!=null) {
            livello = parseInt(level);
        }
        Log.d("Livello:" , ""+livello);

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
        mpAudio = MediaPlayer.create(this,R.raw.healing);
        mpAudio.setLooping(true);
        mpAudio.start();
        //Inizializzo i suoni
       /* mp = new SoundPoolHelper(1, this);
        fail = mp.load(this, R.raw.fail, 1);
        error = mp.load(this, R.raw.error, 1);
        applausi = mp.load(this, R.raw.applausi, 1);
        tock = mp.load(this,R.raw.tock, 1);*/
        //blocco la tastiera
        edit = (EditText) findViewById(R.id.editText3);
        edit.setKeyListener(null);

        setCost();
        //mpAudio = MediaPlayer.create(this,R.raw.healing);




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

                        if(won){
                            setResult(livello);
                        }else {
                            setResult(10);
                        }
                        finish();

                        if(finito) {
                           /* mp.stop(applausi);
                            mp.stop(fail);

                            mp.unload(error);
                            mp.unload(fail);
                            mp.unload(applausi);

                            mp.release();*/
                            if(won) {
                                setResult(livello);
                            }else{
                                setResult(0);
                            }
                            finish();
                        }



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
      //  Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

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
                //Vibrate for 500 milliseconds
                //v.vibrate(500);


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
                if(aiuto_guardone==true){
                    Guardone();
                }
            } else {
                alto_basso.setText("Troppo alto!");
                // Vibrate for 500 milliseconds
               // v.vibrate(500);

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
                if(aiuto_guardone==true){
                    Guardone();
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

            try {
                r = parseInt(guess1);
                if(r>max||r<min){
                    AlertDialog.Builder builder = new AlertDialog.Builder(LevelGame.this);

                    // 2. Chain together various setter methods to set the dialog characteristics
                    builder.setMessage("Devi inserire un numero compreso tra "+min+" e "+max)
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
                    edit.setText("");
                }else {
                    powerup=true;
                    if (mani == false) {
                        checkWin(r);
                    } else {
                        i += 2;
                        checkWin(r);
                        checkWin((r - 1) % range);
                        checkWin((r + 1) % range);
                        mani = false;
                    }
                }

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



    } //fine funzione

    public void endGame(boolean win) { //True = vittoria,False = sconfitta
        finito = true;
        TextView tentativi = (TextView) findViewById(R.id.textView2);
        EditText edit = (EditText) findViewById(R.id.editText3);
        TextView indovina = (TextView) findViewById(R.id.textView);
        Button hai_vinto = (Button) findViewById(R.id.button);
        ViewGroup layout_edit = (ViewGroup) edit.getParent();
        ViewGroup layout_bottone = (ViewGroup) hai_vinto.getParent();
        TextView estremi=(TextView)findViewById(R.id.estremi);



        ImageView sfondofine=(ImageView)findViewById(R.id.sfondofine);
        TextView numerodaind=(TextView)findViewById(R.id.textView4);
        TextView punteggiotesto=(TextView)findViewById(R.id.punteggio);
        TextView testovintoperso=(TextView)findViewById(R.id.textView3);
        Button vaiindietro=(Button)findViewById(R.id.vaiindietro);
        Button riprova=(Button)findViewById(R.id.riprova);
        Button continua=(Button)findViewById(R.id.continua);


        tentativi.setText("");
        indovina.setText("");
        if (layout_edit != null) {
            layout_edit.removeView(edit);
        }
        if (layout_bottone != null) {
            layout_bottone.removeView(hai_vinto);
        }
        //vinto.setVisibility(View.VISIBLE);
        testovintoperso.setText("Hai\n" + (win ? "Vinto!!!" : "Perso!!!"));

        //mp.play(win ? applausi : fail);
        if (win) {
            vaiindietro.setVisibility(View.VISIBLE);
            riprova.setVisibility(View.VISIBLE);
            continua.setVisibility(View.VISIBLE);
            testovintoperso.setVisibility(View.VISIBLE);
            sfondofine.setVisibility(View.VISIBLE);
            numerodaind.setVisibility(View.VISIBLE);
            numerodaind.setText("Il numero da indovinare era " + this.guess);
            punteggiotesto.setVisibility(View.VISIBLE);
            punteggiotesto.setText("Hai ottenuto "+gain(true)+" $");
            estremi.setVisibility(View.INVISIBLE);
            setPoint(punteggio + gain(true));
            won=true;
        } else {
            vaiindietro.setVisibility(View.VISIBLE);
            riprova.setVisibility(View.VISIBLE);
            continua.setVisibility(View.VISIBLE);
            testovintoperso.setVisibility(View.VISIBLE);
            sfondofine.setVisibility(View.VISIBLE);
            numerodaind.setVisibility(View.VISIBLE);
            numerodaind.setText("Il numero da indovinare era " + this.guess );
            punteggiotesto.setVisibility(View.VISIBLE);
            punteggiotesto.setText("Hai ottenuto "+gain(false)+" $");
            estremi.setVisibility(View.INVISIBLE);
            setPoint(punteggio + gain(false));

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
            Toast toast = Toast.makeText(this, "Hai ricevuto un tentativo extra", Toast.LENGTH_LONG);
            toast.show();
            monousoLanterna=false;

        }else{
            Toast toast = Toast.makeText(this, "'Lanterna' è già stata usata", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void Pergamena(){
        if(monousoPergamena==true) {
            int newguess;
            if(guess>99) {
                newguess = Integer.parseInt(("" + guess).substring(1));
            }else{
                newguess=guess;
            }
            int numero_cifre = (int) (Math.floor(Math.log10(newguess)) + 1);
            Random random = new Random();
            int index = random.nextInt(numero_cifre);
            String hint = ("" + newguess).substring(index, index + 1);
            Toast toast = Toast.makeText(this, "Una cifra del numero è " + hint, Toast.LENGTH_LONG);
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
            Toast toast = Toast.makeText(this, "Inserisci il numero da 'triplicare'", Toast.LENGTH_LONG);
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
            Toast toast = Toast.makeText(this, "Inserisci un numero per usare il Terzo Occhio", Toast.LENGTH_LONG);
            toast.show();
         //   i++;
            monousoGuardone=false;
        }else {
            Toast toast = Toast.makeText(this, "'Terzo Occhio' è già stata usato", Toast.LENGTH_LONG);
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
//Da fare: Spostare il toast
    if (dist <= distguess){
        Toast toast = Toast.makeText(this, r+" è LONTANO", Toast.LENGTH_LONG);
        toast.show();
    } else if(dist>distguess){
            Toast toast = Toast.makeText(this, r+" è VICINO", Toast.LENGTH_LONG);
            toast.show();

    }
    aiuto_guardone=false;
   }

   //cambio immagini rispettando il padding precedente
    public static void setViewBackgroundWithoutResettingPadding(final View v, final int backgroundResId) {
        final int paddingBottom = v.getPaddingBottom(), paddingLeft = v.getPaddingLeft();
        final int paddingRight = v.getPaddingRight(), paddingTop = v.getPaddingTop();
        v.setBackgroundResource(backgroundResId);
        v.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }



    public void PowerUp(View v) {
        Button tastpower=(Button)findViewById(R.id.tastpowerbutton);
        android.support.v7.widget.GridLayout tastiera=(android.support.v7.widget.GridLayout)findViewById(R.id.layoutTastiera);
        ImageView pergamena=(ImageView)findViewById(R.id.pergamena);
        ImageView lanterna=(ImageView)findViewById(R.id.lanterna);
        ImageView terzoocchio=(ImageView)findViewById(R.id.terzoocchio);
        ImageView manididio=(ImageView)findViewById(R.id.manididio);

        if (!finito&&powerup) {
            powerup=false;
            switch (v.getId()) {
                case R.id.lanterna:
                    if(monousoLanterna==true) {
                        if (punteggio >= costoLanterna()) {
                            setPoint((punteggio - costoLanterna()));
                            Lanterna();
                            powerup_usati++;
                            setCost();
                            setViewBackgroundWithoutResettingPadding(v,R.drawable.lanternano);

                        } else {
                            Toast toast = Toast.makeText(this, "Non hai abbastanza soldi", Toast.LENGTH_SHORT);
                            powerup=true;
                            toast.show();
                        }
                    }else{
                        powerup=true;
                        Toast toast = Toast.makeText(this, "'Lanterna' è già stata usata", Toast.LENGTH_LONG);
                        toast.show();

                    }
                    break;

                case R.id.pergamena:
                    if(monousoPergamena==true) {
                        if (punteggio >= costoPergamena()) {
                            setPoint((punteggio - costoPergamena()));
                            Pergamena();
                            powerup_usati++;
                            setCost();
                            setViewBackgroundWithoutResettingPadding(v,R.drawable.pergamenano);
                        } else {
                            Toast toast = Toast.makeText(this, "Non hai abbastanza soldi", Toast.LENGTH_SHORT);
                            powerup=true;
                            toast.show();
                        }
                    }else{
                        powerup=true;
                        Toast toast = Toast.makeText(this, "'Pergamena' è già stata usata", Toast.LENGTH_LONG);
                        toast.show();

                    }
                    break;

                case R.id.manididio:
                    if(monousoManiDiDIo==true) {
                        if (punteggio >= costoManiDiDio()) {
                            setPoint((punteggio - costoManiDiDio()));
                            ManiDiDio();
                            powerup_usati++;
                            setCost();
                            setViewBackgroundWithoutResettingPadding(v,R.drawable.manino);
                        } else {
                            Toast toast = Toast.makeText(this, "Non hai abbastanza soldi", Toast.LENGTH_SHORT);
                            powerup=true;
                            toast.show();
                        }
                    }else{
                        powerup=true;
                        Toast toast = Toast.makeText(this, "'Mani di Dio' è già stato usato", Toast.LENGTH_LONG);
                        toast.show();

                    }
                    break;
                /*case R.id.yinyang:
                    powerup=true;
                    break;*/
                case R.id.terzoocchio:
                    if(monousoGuardone==true) {
                        if (punteggio >= costoGuardone()) {
                            setPoint((punteggio - costoGuardone()));
                            AiutoGuardone();
                            powerup_usati++;
                            setCost();
                            setViewBackgroundWithoutResettingPadding(v,R.drawable.occhiono);
                        } else {
                            Toast toast = Toast.makeText(this, "Non hai abbastanza soldi", Toast.LENGTH_SHORT);
                            powerup=true;
                            toast.show();
                        }
                    }else{
                        powerup=true;
                        Toast toast = Toast.makeText(this, "'Guardone' è già stata usato", Toast.LENGTH_LONG);
                        toast.show();

                    }
                    break;
            }
        }else{
            Toast toast = Toast.makeText(this, "Non puoi usare i poteri per questo turno", Toast.LENGTH_LONG);
            toast.show();
        }
        if(!powerup){ //Riprendo la tastiera se ho usaot / non posso usare un powerup in questo turno
            if(!tastoswitch){
                tastiera.setVisibility(View.VISIBLE);
                pergamena.setVisibility(View.INVISIBLE);
                lanterna.setVisibility(View.INVISIBLE);
                terzoocchio.setVisibility(View.INVISIBLE);
                manididio.setVisibility(View.INVISIBLE);
                tastpower.setBackgroundResource(R.drawable.power2);
                tastoswitch=true;
            }
        }
    }
    public void getPoint(){
        SharedPreferences sharedPref = getSharedPreferences("punteggio",MODE_PRIVATE);
        punteggio = sharedPref.getInt(getString(R.string.punteggio), 1000);
        points.setText(""+punteggio);
    }
    public void setPoint(int newpoint){
        SharedPreferences sharedPref = getSharedPreferences("punteggio",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.punteggio), newpoint);
        editor.commit();
        getPoint();
    }
    public int gain(boolean win){
        int gain;
        if(win){
            gain = (livello*100)+(100*(i+1));
            return  gain;
        }else{
            gain =  (livello*100);
            return  gain;

        }

    }

    public void tastiera_powerup(View view){
        Button tastpower=(Button)findViewById(R.id.tastpowerbutton);
        GridLayout tastiera= (GridLayout)findViewById(R.id.layoutTastiera);
        ImageView pergamena=(ImageView)findViewById(R.id.pergamena);
        ImageView lanterna=(ImageView)findViewById(R.id.lanterna);
        ImageView terzoocchio=(ImageView)findViewById(R.id.terzoocchio);
        ImageView manididio=(ImageView)findViewById(R.id.manididio);
        if(tastoswitch==true){
            tastiera.setVisibility(View.INVISIBLE);
            pergamena.setVisibility(View.VISIBLE);
            lanterna.setVisibility(View.VISIBLE);
            terzoocchio.setVisibility(View.VISIBLE);
            manididio.setVisibility(View.VISIBLE);
            tastpower.setBackgroundResource(R.drawable.power1);
            tastoswitch=false;
        }else{
            tastiera.setVisibility(View.VISIBLE);
            pergamena.setVisibility(View.INVISIBLE);
            lanterna.setVisibility(View.INVISIBLE);
            terzoocchio.setVisibility(View.INVISIBLE);
            manididio.setVisibility(View.INVISIBLE);
            tastpower.setBackgroundResource(R.drawable.power2);
            tastoswitch=true;
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        mpAudio.start();
    }
    @Override
    protected void onPause(){
        super.onPause();
        mpAudio.pause();
        mpAudio.seekTo(0);
    }
    @Override
    public void onBackPressed()
    {
        Button tastpower=(Button)findViewById(R.id.tastpowerbutton);
        GridLayout tastiera=(GridLayout)findViewById(R.id.layoutTastiera);
        ImageView pergamena=(ImageView)findViewById(R.id.pergamena);
        ImageView lanterna=(ImageView)findViewById(R.id.lanterna);
        ImageView terzoocchio=(ImageView)findViewById(R.id.terzoocchio);
        ImageView manididio=(ImageView)findViewById(R.id.manididio);
        if(!tastoswitch){
            tastiera.setVisibility(View.VISIBLE);
            pergamena.setVisibility(View.INVISIBLE);
            lanterna.setVisibility(View.INVISIBLE);
            terzoocchio.setVisibility(View.INVISIBLE);
            manididio.setVisibility(View.INVISIBLE);
            tastpower.setBackgroundResource(R.drawable.power2);
            tastoswitch=true;
        }
    }


    public void Indietro(View view){
        mpAudio.pause();
        mpAudio.seekTo(0);
        setResult(10);
        finish();
    }

    public int costoPergamena() {
        int costo=200;
        if(powerup_usati>0) {
            costo += (powerup_usati * 100);
        }
        return costo;
    }
    public int costoGuardone() {
        int costo=400;
        if(powerup_usati>0) {
            costo += (powerup_usati * 100);
        }
        return costo;
    }
    public int costoManiDiDio() {
        int costo=100;
        if(powerup_usati>0) {
            costo += (powerup_usati * 100);
        }

        return costo;
    }
    public int costoLanterna() {
        int costo=300;
        if(powerup_usati>0) {
            costo += (powerup_usati * 100);
        }
        return costo;
    }
    public void setCost(){
        LabeledImageView pergamena=(LabeledImageView)findViewById(R.id.pergamena);
        LabeledImageView lanterna=(LabeledImageView)findViewById(R.id.lanterna);
        LabeledImageView terzoocchio=(LabeledImageView)findViewById(R.id.terzoocchio);
        LabeledImageView manididio=(LabeledImageView)findViewById(R.id.manididio);
        manididio.setText(""+costoManiDiDio());
        lanterna.setText(""+costoLanterna());
        terzoocchio.setText(""+costoGuardone());
        pergamena.setText(""+costoPergamena());
    }


}


