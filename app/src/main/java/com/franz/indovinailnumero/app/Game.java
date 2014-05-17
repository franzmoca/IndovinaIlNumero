package com.franz.indovinailnumero.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;


public class Game extends ActionBarActivity {

    public int fine, inizio, guess;
    public int i=5;
    public int attempts=0;
    public boolean finito=false;

    MediaPlayer error,applausi,fail;






    public void checkWin(View view){
        float punteggio;

        Button inserisci = (Button)findViewById(R.id.button);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inserisci.getWindowToken(), 0);

        error=MediaPlayer.create(this,R.raw.error);
        //error.setLooping(true);
        applausi=MediaPlayer.create(this,R.raw.applausi);
        //applausi.setLooping(true);
        fail=MediaPlayer.create(this,R.raw.fail);
        //fail.setLooping(true);

        TextView inputText = (TextView)findViewById(R.id.editText3);
        String guess1 =  inputText.getText()+"";
        try {
            int guess = Integer.parseInt(guess1);


            TextView tentativi = (TextView) findViewById(R.id.textView2);
            TextView alto_basso = (TextView) findViewById(R.id.textView3);
            TextView vinto = (TextView) findViewById(R.id.textView4);
            TextView numero = (TextView) findViewById(R.id.textView5);
            EditText edit = (EditText) findViewById(R.id.editText3);

            if (i > 0) {
                if (guess == this.guess) {
                    tentativi.setText("");
                    alto_basso.setText("");

                    vinto.setText("Hai\nVinto!!");
                    //applausi.start();
                    applausi.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            mediaPlayer.start();
                        }
                    });
                    attempts++;
                    punteggio=(5-attempts)*(fine-inizio)/2;
                    numero.setText("Il tuo punteggio è: "+punteggio);

                    finito=true;

                } else {
                    edit.setText("");
                    tentativi.setText("Ti sono rimasti ancora " + (i - 1) + " tentativi");
                    if (guess < this.guess) {
                        alto_basso.setText("Troppo basso!");
                        i--;
                        attempts++;
                        if(i>0)
                            error.start();
                    } else {
                        alto_basso.setText("Troppo alto!");
                        i--;
                        attempts++;
                        if(i>0)
                            error.start();
                    }
                }
                if (i == 0) {
                    tentativi.setText("");
                    alto_basso.setText("");
                    numero.setText("Il numero da indovinare era\n" + this.guess);
                    vinto.setText("Hai\nPerso!!");

                    fail.start();
                    finito=true;



                }
            }
        }catch(Exception e){
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


        }


    }
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
        return super.onOptionsItemSelected(item);
    }

    public void ricomincia(View view){
        final Intent intent= new Intent(this, MainActivity.class);
        if(!finito) {
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

            dialog.show();

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
                            error.stop();
                            fail.stop();
                            applausi.stop();
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

