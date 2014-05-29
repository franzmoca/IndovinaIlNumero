package com.franz.indovinailnumero.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


public class SceltaLivelli extends Activity {
    public final static String FINE = "com.franz.guessthenumber.app.inizio.livello";
    public final static String LIVELLO = "com.franz.guessthenumber.app.inizio.livello.numero";

    public String fine;
    public String liv;


    public boolean livello1=true;
    public boolean livello2;
    public boolean livello3;
    public boolean livello4;
    public boolean livello5;
    public boolean livello6;
    public boolean livello7;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scelta_livelli);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //Forza la portrait mode
        getLevel();
        //setLevelFALSE();
        //setLevel();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("RESULT CODE",""+resultCode);
        if(resultCode==0){
           finish();
       }else{
           setLevel(resultCode);
       }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.scelta_livelli, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Indietro(View view){
        setResult(1);
        finish();
    }

    public void Livello(View v){
        Intent intent = new Intent(SceltaLivelli.this, LevelGame.class);

        switch ( v.getId()) {

            case R.id.livello1:
                fine = "50";
                liv="1";
                intent.putExtra(FINE, fine);
                intent.putExtra(LIVELLO, liv);
                SceltaLivelli.this.startActivityForResult(intent, 0);
                break;
            case R.id.livello2:
                if(livello2) {
                    fine = "100";
                    liv="2";
                    intent.putExtra(FINE, fine);
                    intent.putExtra(LIVELLO,liv);
                    SceltaLivelli.this.startActivityForResult(intent, 0);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(SceltaLivelli.this);
                    builder.setMessage("Devi prima superare il livello 1!")
                            .setTitle("");

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;
            case R.id.livello3:
                if(livello3) {
                    fine = "200";
                    liv="3";
                    intent.putExtra(FINE, fine);
                    intent.putExtra(LIVELLO, liv);
                    SceltaLivelli.this.startActivityForResult(intent, 0);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(SceltaLivelli.this);
                    builder.setMessage("Devi prima superare il livello 2!")
                            .setTitle("");

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;
            case R.id.livello4:
                if(livello4) {
                    fine = "500";
                    liv="4";
                    intent.putExtra(FINE, fine);
                    intent.putExtra(LIVELLO,liv);
                    SceltaLivelli.this.startActivityForResult(intent, 0);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(SceltaLivelli.this);
                    builder.setMessage("Devi prima superare il livello 3!")
                            .setTitle("");

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;
            case R.id.livello5:
                if(livello5) {
                    fine = "1000";
                    liv="5";
                    intent.putExtra(FINE, fine);
                    intent.putExtra(LIVELLO, liv);
                    SceltaLivelli.this.startActivityForResult(intent, 0);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(SceltaLivelli.this);
                    builder.setMessage("Devi prima superare il livello 4!")
                            .setTitle("");

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;
            case R.id.livello6:
                if(livello6) {
                    fine = "2000";
                    liv="6";
                    intent.putExtra(FINE, fine);
                    intent.putExtra(LIVELLO,liv);
                    SceltaLivelli.this.startActivityForResult(intent, 0);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(SceltaLivelli.this);
                    builder.setMessage("Devi prima superare il livello 5!")
                            .setTitle("");

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;
            case R.id.livello7:
                if(livello7) {
                    fine = "3000";
                    liv="7";
                    intent.putExtra(FINE, fine);
                    intent.putExtra(LIVELLO,liv);
                    SceltaLivelli.this.startActivityForResult(intent, 0);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(SceltaLivelli.this);
                    builder.setMessage("Devi prima superare il livello 6!")
                            .setTitle("");

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;

        }
    }
    public void getLevel(){
        ImageView livellibackground= (ImageView) findViewById(R.id.backgroundlivelli);
        SharedPreferences sharedPref = getSharedPreferences("livelli",MODE_PRIVATE);
        livello2 = sharedPref.getBoolean(getString(R.string.livello2), false);
        livello3 = sharedPref.getBoolean(getString(R.string.livello3), false);
        livello4 = sharedPref.getBoolean(getString(R.string.livello4), false);
        livello5 = sharedPref.getBoolean(getString(R.string.livello5), false);
        livello6 = sharedPref.getBoolean(getString(R.string.livello6), false);
        livello7 = sharedPref.getBoolean(getString(R.string.livello7), false);

        if(livello2)
            setViewBackgroundWithoutResettingPadding(livellibackground,R.drawable.livello2);
        if(livello3)
            setViewBackgroundWithoutResettingPadding(livellibackground,R.drawable.livello3);
        if(livello4)
            setViewBackgroundWithoutResettingPadding(livellibackground,R.drawable.livello4);
        if(livello5)
            setViewBackgroundWithoutResettingPadding(livellibackground,R.drawable.livello5);
        if(livello6)
            setViewBackgroundWithoutResettingPadding(livellibackground,R.drawable.livello6);
        if(livello7)
            setViewBackgroundWithoutResettingPadding(livellibackground,R.drawable.livello7);

    }
    public void setLevel(int level){
        SharedPreferences sharedPref = getSharedPreferences("livelli",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        switch (level) {
            case 1:
                editor.putBoolean(getString(R.string.livello2), true);
                editor.commit();
                break;
            case 2:
                editor.putBoolean(getString(R.string.livello3), true);
                editor.commit();
                break;
            case 3:
                editor.putBoolean(getString(R.string.livello4), true);
                editor.commit();
                break;
            case 4:
                editor.putBoolean(getString(R.string.livello5), true);
                editor.commit();
                break;
            case 5:
                editor.putBoolean(getString(R.string.livello6), true);
                editor.commit();
                break;
            case 6:
                editor.putBoolean(getString(R.string.livello7), true);
                editor.commit();
                break;
            case 7:
                editor.putBoolean(getString(R.string.livello8), true);

                editor.commit();
                break;

        }
        getLevel();
    }

        public void setLevelFALSE()    {
        SharedPreferences sharedPref = getSharedPreferences("livelli",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.livello2), false);
        editor.putBoolean(getString(R.string.livello3), false);
        editor.putBoolean(getString(R.string.livello4), false);
        editor.putBoolean(getString(R.string.livello5), false);
        editor.putBoolean(getString(R.string.livello6), false);
        editor.putBoolean(getString(R.string.livello7), false);
        editor.commit();
        getLevel();
    }

    public void onBackPressed()
    {
       setResult(1);
       finish();
    }


    //cambio immagini rispettando il padding precedente
    public static void setViewBackgroundWithoutResettingPadding(final View v, final int backgroundResId) {
        final int paddingBottom = v.getPaddingBottom(), paddingLeft = v.getPaddingLeft();
        final int paddingRight = v.getPaddingRight(), paddingTop = v.getPaddingTop();
        v.setBackgroundResource(backgroundResId);
        v.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

}
