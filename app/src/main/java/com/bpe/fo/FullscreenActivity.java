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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bpe.fo.databinding.ActivityFullscreenBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
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
            if (Build.VERSION.SDK_INT >= 30) {

            } else {
                // Note that some of these constants are new as of API 16 (Jelly Bean)
                // and API 19 (KitKat). It is safe to use them, as they are inlined

            }
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
    private ActivityFullscreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityFullscreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mVisible = true;
        ConstraintLayout frameLayout = findViewById(R.id.cl);

        Glide.with(this)
                .load("https://images.unsplash.com/photo-1676545736963-98c64e6ff281?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=387&q=80")
                .placeholder(R.color.teal_200) // Placeholder image until the image is loaded
                .error(R.color.light_blue_600) // Error image if the image fails to load
                .into(new CustomViewTarget<ConstraintLayout, Drawable>(frameLayout) {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        Toast.makeText(FullscreenActivity.this, "Load Failed", Toast.LENGTH_SHORT).show();
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
        EditText NameEdit = (EditText) findViewById(R.id.editTextTextPersonName);
        EditText EmailEdit = (EditText) findViewById(R.id.editTextTextPersonName2);
        EditText PhoneNoEdit = (EditText) findViewById(R.id.editTextTextPersonName3);
        EditText PassEdit = (EditText) findViewById(R.id.editTextTextPersonName4);
        TextView B1 = (TextView) findViewById(R.id.button);
        TextView B2 = (TextView) findViewById(R.id.button2);
        NameEdit.setVisibility(View.INVISIBLE);
        EmailEdit.setVisibility(View.INVISIBLE);
        PhoneNoEdit.setVisibility(View.INVISIBLE);
        PassEdit.setVisibility(View.INVISIBLE);
        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                B1.setVisibility(View.GONE);
                B2.setVisibility(View.VISIBLE);
                B2.setText("LOG IN");
                NameEdit.setVisibility(View.VISIBLE);
                EmailEdit.setVisibility(View.VISIBLE);
                EmailEdit.setHint("Enter Password");
                PhoneNoEdit.setVisibility(View.GONE);
                PassEdit.setVisibility(View.GONE);

            }

        });
        B2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((PhoneNoEdit.getVisibility()== View.GONE) && (PassEdit.getVisibility()==View.GONE)){
                    if((!NameEdit.getText().toString().equals(""))&&(!EmailEdit.getText().toString().equals(""))){
                        String LoginInfo = NameEdit.getText().toString()+"|"+EmailEdit.getText().toString();
                        StringBuilder temp = new StringBuilder();
                        try {
                            FileInputStream fin = openFileInput("Login Info.txt");
                            int a;

                            while ((a = fin.read()) != -1) {
                                temp.append((char)a);
                            }
                            fin.close();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }

                        if(temp.toString().equals(LoginInfo)){
                            Intent Starter = new Intent(FullscreenActivity.this, MainMenuuu.class);
                            startActivity(Starter);
                        }
                        else{
                            Toast.makeText(FullscreenActivity.this, "Incorrect Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(FullscreenActivity.this, "LOGIN EMPTY", Toast.LENGTH_SHORT).show();
                    }
                }
                else if((PhoneNoEdit.getVisibility()== View.VISIBLE) && (PassEdit.getVisibility()==View.VISIBLE)&&(NameEdit.getVisibility()==View.VISIBLE)&&(EmailEdit.getVisibility()==View.VISIBLE)){
                    if((!NameEdit.getText().toString().equals(""))&&(!PassEdit.getText().toString().equals(""))){
                        if((EmailEdit.getText().toString().equals(""))&&(PhoneNoEdit.getText().toString().equals(""))){
                            Snackbar.make(view, "Enter Either Email Or Phone Number", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                        else{
                            FileOutputStream fos = null;
                            String PatchedInfo = NameEdit.getText().toString()+"|"+PassEdit.getText().toString();
                            try {
                                fos = openFileOutput("Login Info.txt", Context.MODE_PRIVATE);
                                fos.write(PatchedInfo.getBytes());
                                fos.flush();
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Intent Restarter = new Intent(FullscreenActivity.this, FullscreenActivity.class);
                            startActivity(Restarter);
                        }

                    }
                    else{
                        Toast.makeText(FullscreenActivity.this, "SIGN UP EMPTY", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    B1.setVisibility(View.GONE);
                    B2.setVisibility(View.VISIBLE);
                    NameEdit.setVisibility(View.VISIBLE);
                    EmailEdit.setVisibility(View.VISIBLE);
                    PhoneNoEdit.setVisibility(View.VISIBLE);
                    PassEdit.setVisibility(View.VISIBLE);
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


        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        // Show the system bar
        if (Build.VERSION.SDK_INT >= 30) {

        } else {

        }
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