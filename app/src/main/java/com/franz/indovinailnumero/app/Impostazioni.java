package com.franz.indovinailnumero.app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class Impostazioni extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impostazioni);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.impostazioni, menu);
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
    }
    public void setPointFALSE(){
        SharedPreferences sharedPref = getSharedPreferences("punteggio",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.punteggio), 100);
        editor.commit();
    }

    public void Reset(View view){
        setLevelFALSE();
        setPointFALSE();

    }
}
