package com.bpe.fo;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bpe.fo.databinding.ActivitySaveNewBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Save_New extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler(Looper.myLooper());

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {

        }
    };
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }

        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (AUTO_HIDE) {
                        delayedHide(AUTO_HIDE_DELAY_MILLIS);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    break;
                default:
                    break;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySaveNewBinding binding = ActivitySaveNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EditText t1 = findViewById(R.id.spinnerA);
        EditText specialtext = findViewById(R.id.editTextTextPersonName6);
        EditText t2 = findViewById(R.id.cypher);
        binding.button111234567890.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(specialtext.getText().toString().length() == "\uD83C\uDDEE\uD83C\uDDF3".toString().length()){
                    if(!t1.getText().toString().equals("")){
                        if(!t2.getText().toString().equals("")){
                            String Text = t2.getText().toString();
                            if(Text.length()!=26){
                                Snackbar.make(view, "Enter Exact 26 Keys for 26 Alphabets", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            }
                            else{
                                String distinctStr = Text.chars().distinct().mapToObj(c -> String.valueOf((char) c)).collect(Collectors.joining()).toString();
                                if(distinctStr.length()!=26){
                                    Snackbar.make(view, "Remove Duplicates, One char Cant be Assigned to Two", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }
                                else{
                                    String DATA = "";
                                    try {
                                        FileInputStream fin = openFileInput("SecretData.txt");
                                        int a;
                                        StringBuilder temp = new StringBuilder();
                                        while ((a = fin.read()) != -1) {
                                            temp.append((char)a);
                                        }
                                        DATA = temp.toString();
                                        fin.close();
                                    }
                                    catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    //-----
                                    Vector<WorldMap> Countries = new Vector<WorldMap>(0);
                                    String Factions= "";
                                    for(int i=0;i<DATA.length();i++){
                                        if(DATA.charAt(i)=='\n'){
                                            WorldMap Map1 = new WorldMap();
                                            Map1.NameKeySeparator(Factions);
                                            Countries.add(Map1);
                                            Factions = "";
                                            continue;
                                        }
                                        else{
                                            Factions+=DATA.charAt(i);
                                        }
                                    }
                                    if(Countries.get(Countries.size()-1).getName()==""){
                                        Countries.remove(Countries.size()-1);
                                    }

                                    //-----
                                    int stopper = -1; String SecretData="";
                                    for(int i=0;i<Countries.size();i++){
                                        if(Countries.get(i).getName().equals(t1.getText().toString())){
                                            Countries.get(i).setKey(t2.getText().toString());
                                            Snackbar.make(view, "Country Already Present, Updating the Key!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                            stopper = 1;
                                        }
                                        SecretData+=Countries.get(i).getName()+" - "+Countries.get(i).getKey()+'\n';
                                    }
                                    if(stopper==1){
                                        FileOutputStream fos = null;
                                        try {
                                            fos = openFileOutput("SecretData.txt", Context.MODE_PRIVATE);
                                            fos.write(SecretData.getBytes());
                                            fos.flush();
                                            fos.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if(stopper ==-1){
                                        String TO_WRITE = t1.getText().toString() +" - "+t2.getText().toString();
                                        DATA = DATA + '\n' + TO_WRITE;

                                        FileOutputStream fos = null;
                                        try {
                                            fos = openFileOutput("SecretData.txt", Context.MODE_PRIVATE);
                                            fos.write(DATA.getBytes());
                                            fos.flush();
                                            fos.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        Snackbar.make(view, "Successfully Saved", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    }
                                    Intent i = new Intent(Save_New.this, MainMenuuu.class);
                                    startActivity(i);
                                }
                            }
                        }
                        else{
                            Snackbar.make(view, "Demographic Key Text Cannot Be Empty", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                    }
                    else{
                        Snackbar.make(view, "Name Cannot be Empty (1st Box) :)", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }
                else{
                    Snackbar.make(view, "Enter Only 1 Flag in Designated Box", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }


    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        // Show the system bar

        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}