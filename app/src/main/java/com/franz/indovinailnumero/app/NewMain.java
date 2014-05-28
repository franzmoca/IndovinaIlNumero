package com.franz.indovinailnumero.app;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
//import android.os.Bundle;


public class NewMain extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //Forza la portrait mode

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 0) {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_main, menu);
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

    public void startFreeGame(View view) {
        Intent intent = new Intent(NewMain.this, MainActivity.class);
        NewMain.this.startActivity(intent);
    }

    public void GoToLivelli(View view){

        Intent intent = new Intent(NewMain.this, SceltaLivelli.class);
        NewMain.this.startActivityForResult(intent, 0);
    }

    public void Esci(View view){
        setResult(0);
        finish();
    }
    @Override
    protected void onResume(){
        super.onResume();
       // mpAudio.start();
    }
    @Override
    protected void onPause(){
        super.onPause();
        //mpAudio.pause();
    }

}
