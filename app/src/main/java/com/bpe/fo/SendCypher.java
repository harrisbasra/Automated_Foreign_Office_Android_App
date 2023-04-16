package com.bpe.fo;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bpe.fo.databinding.ActivitySendCypherBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SendCypher extends AppCompatActivity {
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
            // Delayed removal of status and navigation bar

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
    private ActivitySendCypherBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySendCypherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mVisible = true;
        ConstraintLayout frameLayout = findViewById(R.id.cl);

        Glide.with(this)
                .load("https://images.unsplash.com/photo-1576731753569-3e93a228048c?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=387&q=80")
                .placeholder(R.color.teal_200) // Placeholder image until the image is loaded
                .error(R.color.light_blue_600) // Error image if the image fails to load
                .into(new CustomViewTarget<ConstraintLayout, Drawable>(frameLayout) {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        Toast.makeText(SendCypher.this, "Load Failed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        getView().setBackground(resource);
                    }

                    @Override
                    protected void onResourceCleared(@Nullable Drawable placeholder) {
                        // handle resource cleared
                    }
                });

        //-----
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
        String[] A_Ad = new String[Countries.size()-1];
        for(int i=0;i<A_Ad.length;i++){
            A_Ad[i]="";
        }
        for(int i=0;i<Countries.size()-1;i++){
            A_Ad[i] = Countries.get(i).getName();
        }
        final int[] p = {-1};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, A_Ad);
        binding.spinnerA.setAdapter(adapter);
        binding.spinnerA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                binding.keyHolder.setText(Countries.get(position).getKey().trim());
                p[0] = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.button111234567890.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Run = "";
                try {
                    FileInputStream fin = openFileInput("Cyphers.txt");
                    int a;
                    StringBuilder temp = new StringBuilder();
                    while ((a = fin.read()) != -1) {
                        temp.append((char)a);
                    }
                    Run = temp.toString();
                    fin.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                //--x
                Date currentDate = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String currentDateTimeString = dateFormat.format(currentDate);
                //--x
                binding.cypher.setText(encrypt(binding.cypher.getText().toString(), Countries.get(p[0]).getKey()));
                if(Run.equals("")){
                    Run+=Countries.get(p[0]).getName()+"-"+currentDateTimeString+" "+binding.cypher.getText().toString();
                }
                else{
                    Run+='\n' + Countries.get(p[0]).getName()+"-"+currentDateTimeString+" "+binding.cypher.getText().toString();
                }
                //-->
                FileOutputStream fos = null;
                try {
                    fos = openFileOutput("Cyphers.txt", Context.MODE_PRIVATE);
                    fos.write(Run.getBytes());
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //-->
                binding.button111234567890.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(v, "Sending.....", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        Snackbar.make(v, "Sent !", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    }
                }, 2000);
                Intent i = new Intent(SendCypher.this, MainMenuuu.class);
                startActivity(i);
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
    public static String encrypt(String input, String key) {
        // Create a mapping from alphabets to their corresponding substitutions
        String alphabets = "abcdefghijklmnopqrstuvwxyz";
        String substitutions = key.toLowerCase();
        Map<Character, Character> substitutionMap = new HashMap<>();
        for (int i = 0; i < alphabets.length(); i++) {
            substitutionMap.put(alphabets.charAt(i), substitutions.charAt(i));
        }

        // Encrypt the input string using the substitution map
        StringBuilder encrypted = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            char substitution = substitutionMap.getOrDefault(Character.toLowerCase(c), c);
            if (Character.isUpperCase(c)) {
                substitution = Character.toUpperCase(substitution);
            }
            encrypted.append(substitution);
        }

        return encrypted.toString();
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