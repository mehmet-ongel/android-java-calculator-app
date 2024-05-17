package com.techmania.calculator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.techmania.calculator.databinding.ActivityMainBinding;

import org.mariuszgromada.math.mxparser.Expression;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;
    String number = null;
    int countOpenPar = 0;
    int countClosePar = 0;
    boolean operator = false;
    boolean dotControl = false;
    String result = "";
    boolean buttonEqualsControl = false;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        mainBinding.textViewResult.setText("0");

        sharedPreferences = this.getSharedPreferences("com.techmania.calculator", Context.MODE_PRIVATE);

        mainBinding.btn0.setOnClickListener(v -> {
            onNumberClicked("0");
        });
        mainBinding.btn1.setOnClickListener(v -> {
            onNumberClicked("1");
        });
        mainBinding.btn2.setOnClickListener(v -> {
            onNumberClicked("2");
        });
        mainBinding.btn3.setOnClickListener(v -> {
            onNumberClicked("3");
        });
        mainBinding.btn4.setOnClickListener(v -> {
            onNumberClicked("4");
        });
        mainBinding.btn5.setOnClickListener(v -> {
            onNumberClicked("5");
        });
        mainBinding.btn6.setOnClickListener(v -> {
            onNumberClicked("6");
        });
        mainBinding.btn7.setOnClickListener(v -> {
            onNumberClicked("7");
        });
        mainBinding.btn8.setOnClickListener(v -> {
            onNumberClicked("8");
        });
        mainBinding.btn9.setOnClickListener(v -> {
            onNumberClicked("9");
        });
        mainBinding.btnOpenPar.setOnClickListener(v -> {
            onParClicked("(");
            countOpenPar++;
        });
        mainBinding.btnClosePar.setOnClickListener(v -> {
            if (countClosePar < countOpenPar){
                onParClicked(")");
                countClosePar++;
            }
        });

        mainBinding.btnPlus.setOnClickListener(v -> {

            if (!operator && !dotControl){
                if (number == null){
                    number = "0+";
                } else if (buttonEqualsControl) {

                    number = result + "+";

                } else {
                    number += "+";
                }
                mainBinding.textViewResult.setText(number);
                operator = true;
                dotControl = true;
                buttonEqualsControl = false;
            }

        });
        mainBinding.btnMinus.setOnClickListener(v -> {

            if (!operator && !dotControl){
                if (number == null){
                    number = "0-";
                }else if (buttonEqualsControl) {

                    number = result + "-";

                }
                else {
                    number += "-";
                }
                mainBinding.textViewResult.setText(number);
                operator = true;
                dotControl = true;
                buttonEqualsControl = false;
            }

        });
        mainBinding.btnDivide.setOnClickListener(v -> {

            if (!operator && !dotControl){
                if (number == null){
                    number = "0/";
                }else if (buttonEqualsControl) {

                    number = result + "/";

                }
                else {
                    number += "/";
                }
                mainBinding.textViewResult.setText(number);
                operator = true;
                dotControl = true;
                buttonEqualsControl = false;
            }

        });
        mainBinding.btnMulti.setOnClickListener(v -> {

            if (!operator && !dotControl){
                if (number == null){
                    number = "0*";
                }else if (buttonEqualsControl) {

                    number = result + "*";

                }
                else {
                    number += "*";
                }
                mainBinding.textViewResult.setText(number);
                operator = true;
                dotControl = true;
                buttonEqualsControl = false;
            }

        });
        mainBinding.btnDot.setOnClickListener(v -> {

            if (!dotControl && !operator){

                if (buttonEqualsControl){

                    if (!result.contains(".")){
                        number = result + ".";
                        mainBinding.textViewResult.setText(number);
                        dotControl = true;
                        buttonEqualsControl = false;
                    }

                }else {

                    if (number == null){
                        number = "0.";
                        mainBinding.textViewResult.setText(number);
                        dotControl = true;
                        operator = true;
                    }else {

                        String expressionAfterLastOperator = "";
                        String lastCharacter;
                        dotLoop:for (int i = number.length()-1; i >= 0; i--){

                            lastCharacter = String.valueOf(number.charAt(i));
                            switch (lastCharacter){
                                case "+": case "-": case "*": case "/":
                                    break dotLoop;
                                default:
                                    expressionAfterLastOperator = lastCharacter.concat(expressionAfterLastOperator);
                                    break;
                            }

                        }

                        if (!expressionAfterLastOperator.contains(".")){
                            number += ".";
                            mainBinding.textViewResult.setText(number);
                            dotControl = true;
                            operator = true;
                        }

                    }

                }
            }

        });
        mainBinding.btnAC.setOnClickListener(v -> {
            onButtonACClicked();
        });
        mainBinding.btnDel.setOnClickListener(v -> {

            if (number == null || number.length() == 1){
                onButtonACClicked();
            }else {

                String lastChar;
                lastChar = String.valueOf(number.charAt(number.length()-1));
                switch (lastChar){
                    case "+": case "-": case "*": case "/": case ".":
                        operator = false;
                        dotControl = false;
                        break;
                    case "(":
                        countOpenPar--;
                        break;
                    case ")":
                        countClosePar--;
                        break;
                }

                number = number.substring(0,number.length()-1);
                mainBinding.textViewResult.setText(number);

                lastChar = String.valueOf(number.charAt(number.length()-1));
                switch (lastChar){
                    case "+": case "-": case "*": case "/": case ".":
                        operator = true;
                        dotControl = true;
                        break;
                }

            }

        });

        mainBinding.btnEquals.setOnClickListener(v -> {

            String expressionForCalculate = mainBinding.textViewResult.getText().toString();

            int difference = countOpenPar - countClosePar;
            if (difference > 0){
                for (int i = 0; i < difference; i++){
                    expressionForCalculate = expressionForCalculate.concat(")");
                }
            }

            Expression expression = new Expression(expressionForCalculate);
            result = String.valueOf(expression.calculate());

            if (result.equals("NaN")){

                checkDivisor(expressionForCalculate);

            }else {
                int indexOfDot = result.indexOf(".");
                String expressionAfterDot = result.substring(indexOfDot + 1);
                if (expressionAfterDot.equals("0")){
                    result = result.substring(0,indexOfDot);
                }

                mainBinding.textViewResult.setText(result);
                mainBinding.textViewHistory.setText(expressionForCalculate.concat(" = ").concat(result));

                buttonEqualsControl = true;
                operator = false;
                dotControl = false;
                countOpenPar = 0;
                countClosePar = 0;
            }

        });

        mainBinding.toolbar.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.settingsItem){
                //intent
                Intent intent = new Intent(MainActivity.this, ChangeThemeActivity.class);
                startActivity(intent);
                return true;
            }else {
                return false;
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean isDarMode = sharedPreferences.getBoolean("switch",false);
        if (isDarMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        String resultTextToSave = mainBinding.textViewResult.getText().toString();
        String historyTextToSave = mainBinding.textViewHistory.getText().toString();
        String resultToSave = result;
        String numberToSave = number;
        boolean operatorToSave = operator;
        boolean dotToSave = dotControl;
        boolean equalToSave = buttonEqualsControl;
        int countOpenParToSave = countOpenPar;
        int countCloseParToSave = countClosePar;

        editor.putString("resultText",resultTextToSave);
        editor.putString("history",historyTextToSave);
        editor.putString("result",resultToSave);
        editor.putString("number",numberToSave);
        editor.putBoolean("operator",operatorToSave);
        editor.putBoolean("dot",dotToSave);
        editor.putBoolean("equal",equalToSave);
        editor.putInt("countOpenPar",countOpenParToSave);
        editor.putInt("countClosePar",countCloseParToSave);

        editor.apply();

    }

    @Override
    protected void onStart() {
        super.onStart();

        mainBinding.textViewResult.setText(sharedPreferences.getString("resultText","0"));
        mainBinding.textViewHistory.setText(sharedPreferences.getString("history",""));
        result = sharedPreferences.getString("result","");
        number = sharedPreferences.getString("number",null);
        operator = sharedPreferences.getBoolean("operator",false);
        dotControl = sharedPreferences.getBoolean("dot",false);
        buttonEqualsControl = sharedPreferences.getBoolean("equal",false);
        countOpenPar = sharedPreferences.getInt("countOpenPar",0);
        countClosePar = sharedPreferences.getInt("countClosePar",0);

    }

    public void onNumberClicked(String clickedNumber){
        if (number == null || buttonEqualsControl){
            number = clickedNumber;
        }else {
            number += clickedNumber;
        }
        mainBinding.textViewResult.setText(number);
        operator = false;
        dotControl = false;
        buttonEqualsControl = false;
    }

    public void onParClicked(String par){
        if (number == null || buttonEqualsControl){
            number = par;
        }else {
            number += par;
        }
        mainBinding.textViewResult.setText(number);
        buttonEqualsControl = false;
    }

    public void onButtonACClicked(){

        number = null;
        mainBinding.textViewResult.setText("0");
        mainBinding.textViewHistory.setText("");
        dotControl = false;
        operator = false;
        countOpenPar = 0;
        countClosePar = 0;
        buttonEqualsControl = false;
        result = "";

    }

    public void checkDivisor(String expressionForCalculate){

        if (expressionForCalculate.contains("/")){

            int indexOfSlash = expressionForCalculate.indexOf("/");
            String expressionAfterSlash = expressionForCalculate.substring(indexOfSlash+1);
            if (expressionAfterSlash.contains(")")){
                int closingPar = 0, openingPar = 0;
                for (int i = 0; i < expressionAfterSlash.length(); i++){
                    String isPar = String.valueOf(expressionAfterSlash.charAt(i));
                    if (isPar.equals("(")){
                        openingPar++;
                    }else if (isPar.equals(")")){
                        closingPar++;
                    }
                }
                int diff = closingPar - openingPar;
                if (diff > 0){
                    for (int i = 0; i < diff; i++){
                        expressionAfterSlash = "(".concat(expressionAfterSlash);
                    }
                }
            }


            Expression expression = new Expression(expressionAfterSlash);
            String newResult = String.valueOf(expression.calculate());
            if (newResult.equals("0.0")){
                mainBinding.textViewHistory.setText("The divisor cannot be zero");
            }else {
                checkDivisor(expressionAfterSlash);
            }

        }else {
            mainBinding.textViewHistory.setText("Syntax error");
        }

    }

}






