package com.techmania.calculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.techmania.calculator.databinding.ActivityChangeThemeBinding;

public class ChangeThemeActivity extends AppCompatActivity {

    ActivityChangeThemeBinding switchBinding;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchBinding = ActivityChangeThemeBinding.inflate(getLayoutInflater());
        setContentView(switchBinding.getRoot());

        switchBinding.toolbar2.setNavigationOnClickListener(v -> {
            finish();
        });

        switchBinding.mySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            sharedPreferences = this.getSharedPreferences("com.techmania.calculator", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            if (isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putBoolean("switch",true);
            }else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putBoolean("switch",false);
            }
            editor.apply();

        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        sharedPreferences = this.getSharedPreferences("com.techmania.calculator", Context.MODE_PRIVATE);
        switchBinding.mySwitch.setChecked(sharedPreferences.getBoolean("switch",false));

    }
}