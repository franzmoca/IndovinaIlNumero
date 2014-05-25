package com.franz.indovinailnumero.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;



public class SceltaLivelli extends ActionBarActivity {
    public final static String FINE = "com.franz.guessthenumber.app.inizio.livello";
    public String fine;

    public boolean livello2 =false;
    public boolean livello3 =false;
    public boolean livello4 =false;
    public boolean livello5 =false;
    public boolean livello6 =false;
    public boolean livello7 =false;
    public boolean livello8 =false;
    public boolean livello9 =false;
    public boolean livello10 =false;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scelta_livelli);
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
                intent.putExtra(FINE, fine);
                SceltaLivelli.this.startActivityForResult(intent, 0);
                break;
            case R.id.livello2:
                fine = "100";
                intent.putExtra(FINE, fine);
                SceltaLivelli.this.startActivityForResult(intent, 0);
                break;
            case R.id.livello3:
                fine = "200";
                intent.putExtra(FINE, fine);
                SceltaLivelli.this.startActivityForResult(intent, 0);
                break;
            case R.id.livello4:
                fine = "500";
                intent.putExtra(FINE, fine);
                SceltaLivelli.this.startActivityForResult(intent, 0);
                break;
            case R.id.livello5:
                fine = "1000";
                intent.putExtra(FINE, fine);
                SceltaLivelli.this.startActivityForResult(intent, 0);
                break;
            case R.id.livello6:
                fine = "2000";
                intent.putExtra(FINE, fine);
                SceltaLivelli.this.startActivityForResult(intent, 0);
                break;
            case R.id.livello7:
                fine = "3000";
                intent.putExtra(FINE, fine);
                SceltaLivelli.this.startActivityForResult(intent, 0);
                break;

        }
    }
}
