package com.franz.indovinailnumero.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;

import com.franz.indovinailnumero.app.util.SoundPoolHelper;

import java.util.Random;


public class Game extends ActionBarActivity {

    public int fine, inizio, guess;
    public int i=5;
    public int attempts=0;
    public boolean finito=false;
    public SoundPoolHelper mp;
    int fail, error, applausi;



    public void checkWin(View view) {
        if (!finito) {
            float punteggio;
            Button inserisci = (Button) findViewById(R.id.button);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(inserisci.getWindowToken(), 0);

            TextView inputText = (TextView) findViewById(R.id.editText3);
            String guess1 = inputText.getText() + "";
            try {
                int guess = Integer.parseInt(guess1);


                TextView tentativi = (TextView) findViewById(R.id.textView2);
                TextView alto_basso = (TextView) findViewById(R.id.textView3);
                TextView vinto = (TextView) findViewById(R.id.textView4);
                TextView numero = (TextView) findViewById(R.id.textView5);
                EditText edit = (EditText) findViewById(R.id.editText3);
                TextView indovina=(TextView) findViewById(R.id.textView);
                Button hai_vinto=(Button)findViewById(R.id.button);
                ViewGroup layout_edit = (ViewGroup) edit.getParent();
                ViewGroup layout_bottone=(ViewGroup) hai_vinto.getParent();

                if (i > 0) {
                    if (guess == this.guess) {
                        tentativi.setText("");
                        alto_basso.setText("");
                        indovina.setText("");
                        layout_edit.removeView(edit);
                        layout_bottone.removeView(hai_vinto);

                        vinto.setVisibility(1);
                        vinto.setText("Hai\nVinto!!");
                        mp.play(applausi);
                        attempts++;
                        punteggio = (6 - attempts) * (fine - inizio) / 2;
                        numero.setText("Il tuo punteggio è: " + punteggio);

                        finito = true;

                    } else {
                        edit.setText("");
                        tentativi.setText("Ti sono rimasti ancora " + (i - 1) + " tentativi");
                        if (guess < this.guess) {
                            alto_basso.setText("Troppo basso!");
                            i--;
                            attempts++;
                            if (i > 0)
                                mp.play(error);
                        } else {
                            alto_basso.setText("Troppo alto!");
                            i--;
                            attempts++;
                            if (i > 0)
                                mp.play(error);
                        }
                    }
                    if (i == 0) {
                        tentativi.setText("");
                        alto_basso.setText("");
                        indovina.setText("");
                        layout_edit.removeView(edit);
                        layout_bottone.removeView(hai_vinto);
                        numero.setText("Il numero da indovinare era\n" + this.guess);
                        vinto.setVisibility(1);
                        vinto.setText("Hai\nPerso!!");

                        mp.play(fail);
                        finito = true;
                    }
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
        } //Fine if finito
    } //fine funzione
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent intent=getIntent();

        String f = intent.getStringExtra(MainActivity.FINE);
        fine =Integer.parseInt(f);
        String i = intent.getStringExtra(MainActivity.INIZIO);
        inizio =Integer.parseInt(i);
        Random random = new Random();
        guess =random.nextInt(fine - inizio +1)+ inizio;
        Log.d("guess", "Numero generato: " + guess);
        //Inizializzo i suoni
        mp = new SoundPoolHelper(1,this);
        fail = mp.load(this,R.raw.fail,1);
        error = mp.load(this,R.raw.error,1);
        applausi = mp.load(this,R.raw.applausi,1);
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
            Log.d("settings","click");
            return true;
        }
        if (id==R.id.action_ricomincia){
            // 1. Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(Game.this);

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage("Vuoi ricominciare?")
                    .setTitle("");

            builder.setPositiveButton("Si",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //aggiungo tutti sti stop perchè capita che io inizio una nuova partita ma ancora sento il suono del fail di prima, in questo modo invece interrompo tutto
                            mp.unload(error);
                            mp.unload(fail);
                            mp.unload(applausi);
                            setResult(1);
                            finish();

                        }
                    }
            );
            builder.setNegativeButton("No",
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
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void ricomincia(View view){
        if(!finito) {/*
            // 1. Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(Game.this);

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage("Vuoi ricominciare?")
                    .setTitle("");

            builder.setPositiveButton("Si",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //aggiungo tutti sti stop perchè capita che io inizio una nuova partita ma ancora sento il suono del fail di prima, in questo modo invece interrompo tutto
                            error.stop();
                            fail.stop();
                            applausi.stop();
                            setResult(1);
                            finish();
                        }
                    }
            );
            builder.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }
            );
            // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();

            dialog.show();*/

        }else{

            // 1. Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(Game.this);

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage("Vuoi rigiocare?")
                    .setTitle("Partita Conclusa");

            builder.setPositiveButton("Si",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mp.unload(error);
                            mp.unload(fail);
                            mp.unload(applausi);
                            setResult(1);
                            finish();
                        }
                    }
            );
            builder.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mp.unload(error);
                            mp.unload(fail);
                            mp.unload(applausi);
                            setResult(0);
                            finish();
                            dialogInterface.cancel();
                        }
                    }
            );
            // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();

            dialog.show();
        }

    }

}

