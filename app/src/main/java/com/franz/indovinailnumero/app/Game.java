package com.franz.indovinailnumero.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import java.lang.*;

import com.franz.indovinailnumero.app.util.SoundPoolHelper;

import java.util.Random;


public class Game extends ActionBarActivity {

    public int fine, inizio, guess;
    public int i = 5;
    public int attempts = 0;
    public boolean finito = false;
    public SoundPoolHelper mp;
    int fail, error, applausi,tock;




    public void checkWin(View view) {

        Button inserisci = (Button) findViewById(R.id.BottoneInserisci);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inserisci.getWindowToken(), 0);

        EditText edit = (EditText) findViewById(R.id.EditInserisciNumero);
        String guess1 = edit.getText() + "";
        try {
            int guess = Integer.parseInt(guess1);
            TextView tentativi = (TextView) findViewById(R.id.textView2);
            TextView alto_basso = (TextView) findViewById(R.id.AltoBasso);
            attempts++;
            if (guess == this.guess) {
                endGame(true);
            } else {
                i--;
                if (i > 0)
                    mp.play(error);
                edit.setText("");
                tentativi.setText("Ti sono rimasti ancora " + i + " tentativi");
                if (guess < this.guess)
                    alto_basso.setText("Troppo basso!");
                else
                    alto_basso.setText("Troppo alto!");
            }
            if (i == 0) {
                endGame(false);
            }

        }//fine try
        catch (Exception e) {
            // 1. Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(Game.this);

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
        TextView alto_basso = (TextView) findViewById(R.id.AltoBasso);
        TextView vinto = (TextView) findViewById(R.id.VintoPerso);
        TextView numero = (TextView) findViewById(R.id.tentativiRimasti);
        EditText edit = (EditText) findViewById(R.id.EditInserisciNumero);
        TextView indovina = (TextView) findViewById(R.id.textView);
        Button hai_vinto = (Button) findViewById(R.id.BottoneInserisci);
        ViewGroup layout_edit = (ViewGroup) edit.getParent();
        ViewGroup layout_bottone = (ViewGroup) hai_vinto.getParent();
        GridLayout layouttastiera=(GridLayout)findViewById(R.id.layoutTastiera);
        layouttastiera.setVisibility(View.INVISIBLE);
        tentativi.setText("");
        alto_basso.setText("");
        indovina.setText("");
        if (layout_edit != null) {
            layout_edit.removeView(edit);
        }
        if (layout_bottone != null) {
            layout_bottone.removeView(hai_vinto);
        }
        vinto.setVisibility(View.VISIBLE);
        vinto.setText("Hai\n" + (win ? "vinto" : "perso"));
        mp.play(win ? applausi : fail);
        if (win) {
            float punteggio;
            punteggio = (6 - attempts) * (fine - inizio) / 2;
            numero.setText("Il tuo punteggio è: " + punteggio);
        } else
            numero.setText("Il numero da indovinare era\n" + this.guess);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //Forza la portrait mode
        setContentView(R.layout.activity_game);
        Intent intent = getIntent();

        String i = intent.getStringExtra(MainActivity.INIZIO);
        inizio = Integer.parseInt(i);

        String f = intent.getStringExtra(MainActivity.FINE);
        fine = Integer.parseInt(f);

        Random random = new Random();
        guess = random.nextInt(fine - inizio + 1) + inizio;
        Log.d("guess", "Numero generato: " + guess);
        //Inizializzo i suoni
        mp = new SoundPoolHelper(1, this);
        fail = mp.load(this, R.raw.fail, 1);
        error = mp.load(this, R.raw.error, 1);
        applausi = mp.load(this, R.raw.applausi, 1);
        tock=mp.load(this, R.raw.tock, 1);

        //blocco la tastiera
        EditText editT = (EditText) findViewById(R.id.EditInserisciNumero);
        editT.setKeyListener(null);



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
        AlertDialog.Builder builder = new AlertDialog.Builder(Game.this);
        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Vuoi rigiocare?")
                .setTitle("Partita Conclusa");
        builder.setPositiveButton("Si",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mp.stop(applausi);
                        mp.stop(fail);

                        mp.unload(error);
                        mp.unload(fail);
                        mp.unload(applausi);

                        mp.release();
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
                            mp.stop(applausi);
                            mp.stop(fail);

                            mp.unload(error);
                            mp.unload(fail);
                            mp.unload(applausi);

                            mp.release();
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

    // Will be called for every Button that is clicked
    public void input(View v){
        mp.play(tock);
        EditText edit = (EditText) findViewById(R.id.EditInserisciNumero);
         Button tasto1 =(Button)findViewById(R.id.tasto1);
         Button tasto2 =(Button)findViewById(R.id.tasto2);
         Button tasto3 =(Button)findViewById(R.id.tasto3);
         Button tasto4 =(Button)findViewById(R.id.tasto4);
         Button tasto5 =(Button)findViewById(R.id.tasto5);
         Button tasto6 =(Button)findViewById(R.id.tasto6);
         Button tasto7 =(Button)findViewById(R.id.tasto7);
         Button tasto8 =(Button)findViewById(R.id.tasto8);
         Button tasto9 =(Button)findViewById(R.id.tasto9);
         Button tasto0 =(Button)findViewById(R.id.tasto0);
         Button tasto00 =(Button)findViewById(R.id.tasto00);
         Button tastoC =(Button)findViewById(R.id.tastoC);
        String edit2=edit.getText()+"";
        String T1 =(String) tasto1.getText();
        String T2 =(String) tasto2.getText();
        String T3 =(String) tasto3.getText();
        String T4 = (String)tasto4.getText();
        String T5 = (String)tasto5.getText();
        String T6 = (String)tasto6.getText();
        String T7 =(String) tasto7.getText();
        String T8 =(String) tasto8.getText();
        String T9 =(String) tasto9.getText();
        String T0 = (String)tasto0.getText();
        String T00 =(String) tasto00.getText();
        String TC=(String)tastoC.getText();

        switch ( v.getId()) {
            case R.id.tasto1: edit.append(T1);
                break;
            case R.id.tasto2:   edit.append(T2);
                break;
            case R.id.tasto3:  edit.append(T3);
                break;
            case R.id.tasto4:   edit.append(T4);
                break;
            case R.id.tasto5:  edit.append(T5);
                break;
            case R.id.tasto6:  edit.append(T6);
                break;
            case R.id.tasto7:   edit.append(T7);
                break;
            case R.id.tasto8:   edit.append(T8);
                break;
            case R.id.tasto9:  edit.append(T9);
                break;
            case R.id.tasto0:  edit.append(T0);
                break;
            case R.id.tasto00:  edit.append(T00);
                break;
            case R.id.tastoC:
                              edit.setText(removeLastChar(edit2));
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

}





