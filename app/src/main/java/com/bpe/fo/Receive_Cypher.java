package com.bpe.fo;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

import com.bpe.fo.databinding.ActivityReceiveCypherBinding;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Receive_Cypher extends AppCompatActivity {

    private static final boolean AUTO_HIDE = true;
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
    private ActivityReceiveCypherBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReceiveCypherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mVisible = true;
        String AllCyphers = "";
        try {
            FileInputStream fin = openFileInput("Cyphers.txt");
            int a;
            StringBuilder temp = new StringBuilder();
            while ((a = fin.read()) != -1) {
                temp.append((char)a);
            }
            AllCyphers = temp.toString();
            fin.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Vector<CypherMap> All_Cyphers = new Vector<CypherMap>(0);
        String Factions= "";
        for(int i=0;i<AllCyphers.length();i++){
            if(AllCyphers.charAt(i)=='\n'){
                CypherMap cypher1 = new CypherMap();
                cypher1.NameTimeCypherSeparator(Factions);
                All_Cyphers.add(cypher1);
                Factions = "";
                continue;
            }
            else{
                Factions+=AllCyphers.charAt(i);
            }
        }
        CypherMap cypher1 = new CypherMap();
        cypher1.NameTimeCypherSeparator(Factions);
        All_Cyphers.add(cypher1);
        Factions = "";
        if(All_Cyphers.get(All_Cyphers.size()-1).getName()==""){
            All_Cyphers.remove(All_Cyphers.size()-1);
        }
        //-------------x
        String[] A_Ad = new String[All_Cyphers.size()];
        for(int i=0;i<A_Ad.length;i++){
            A_Ad[i]="";
        }
        for(int i=0;i<All_Cyphers.size();i++){
            A_Ad[i] = All_Cyphers.get(i).getName()+" "+All_Cyphers.get(i).getTime_Stamp();
        }
        //-----------0

        //-----------o


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
        String Factionsx= "";
        for(int i=0;i<DATA.length();i++){
            if(DATA.charAt(i)=='\n'){
                WorldMap Map1 = new WorldMap();
                Map1.NameKeySeparator(Factionsx);
                Countries.add(Map1);
                Factionsx = "";
                continue;
            }
            else{
                Factionsx+=DATA.charAt(i);
            }
        }
        WorldMap Map1 = new WorldMap();
        Map1.NameKeySeparator(Factionsx);
        Countries.add(Map1);
        Factionsx = "";
        if(Countries.get(Countries.size()-1).getName()==""){
            Countries.remove(Countries.size()-1);
        }
        //-----------0

        final int[] pp = {0};
        final int[] pp2 = {0};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, A_Ad);
        binding.spinnerA.setAdapter(adapter);
        binding.spinnerA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                binding.cypher.setText(All_Cyphers.get(position).getCypher_Text().trim());
                pp[0] = position;

                for(int i=0;i<Countries.size();i++){
                    if(Countries.get(i).getName().equals(All_Cyphers.get(pp[0]).getName())){
                        binding.keyHolder.setText(Countries.get(i).getKey().trim());
                        pp2[0] =i;
                        break;
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.button111234567890.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Receive_Cypher.this, Countries.get(pp2[0]).getKey().trim(), Toast.LENGTH_SHORT).show();
                binding.cypher.setText(decrypt(binding.cypher.getText().toString(), Countries.get(pp2[0]).getKey().trim()));
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

    public static String decrypt(String text, String key)  {
        String decrypted = "";
        text = text.toUpperCase();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isLetter(c)) {
                int index = key.indexOf(Character.toUpperCase(c));
                char decryptedChar = Character.isLowerCase(c) ? Character.toLowerCase((char) (index + 97)) : (char) (index + 65);
                decrypted += decryptedChar;
            } else {
                decrypted += c;
            }
        }
        return decrypted;
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