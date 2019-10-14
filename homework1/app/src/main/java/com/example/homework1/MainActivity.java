package com.example.homework1;

import androidx.appcompat.app.AppCompatActivity;



import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button n0, n1, n2, n3, n4, n5, n6, n7, n8, n9;
    Button plus, minus, multi, division, equal, clear;
    TextView LED;

    Switch aSwitch;
    LinearLayout cal ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LED = (TextView) findViewById(R.id.LED);
        cal = (LinearLayout) findViewById(R.id.calculator);

        clear = (Button) findViewById(R.id.clearButton);
        clear.setOnClickListener(this);

        plus = (Button) findViewById(R.id.plusButton);
        plus.setOnClickListener(this);
        minus = (Button) findViewById(R.id.minusButton);
        minus.setOnClickListener(this);
        multi = (Button) findViewById(R.id.multiplyButton);
        multi.setOnClickListener(this);
        division = (Button) findViewById(R.id.divisionButton);
        division.setOnClickListener(this);
        equal = (Button) findViewById(R.id.equalButton);
        equal.setOnClickListener(this);

        aSwitch = (Switch) findViewById(R.id.aswitch);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    LED.setVisibility(View.VISIBLE);
                    cal.setVisibility(View.VISIBLE);
                }
                else{
                    LED.setVisibility(View.INVISIBLE);
                    cal.setVisibility(View.INVISIBLE);
                }
            }
        });


        n0 = (Button) findViewById(R.id.d0);
        n0.setOnClickListener(this);

        n1 = (Button) findViewById(R.id.d1);
        n1.setOnClickListener(this);

        n2 = (Button) findViewById(R.id.d2);
        n2.setOnClickListener(this);

        n3 = (Button) findViewById(R.id.d3);
        n3.setOnClickListener(this);

        n4 = (Button) findViewById(R.id.d4);
        n4.setOnClickListener(this);

        n5 = (Button) findViewById(R.id.d5);
        n5.setOnClickListener(this);

        n6 = (Button) findViewById(R.id.d6);
        n6.setOnClickListener(this);

        n7 = (Button) findViewById(R.id.d7);
        n7.setOnClickListener(this);

        n8 = (Button) findViewById(R.id.d8);
        n8.setOnClickListener(this);

        n9 = (Button) findViewById(R.id.d9);
        n9.setOnClickListener(this);

    }






    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.clearButton:
                LED.setText("");
                break;
            case R.id.equalButton:
                String str = LED.getText().toString().trim();
                double result =eval(str);
                LED.setText(String.valueOf(result));
                break;
            case R.id.plusButton:
                LED.append("+");
                break;
            case R.id.minusButton:
                LED.append("-");
                break;
            case R.id.multiplyButton:
                LED.append("*");
                break;
            case R.id.divisionButton:
                LED.append("/");
                break;
            case R.id.d0:
                LED.append("0");
                break;
            case R.id.d1:
                LED.append("1");
                break;
            case R.id.d2:
                LED.append("2");
                break;
            case R.id.d3:
                LED.append("3");
                break;
            case R.id.d4:
                LED.append("4");
                break;
            case R.id.d5:
                LED.append("5");
                break;
            case R.id.d6:
                LED.append("6");
                break;
            case R.id.d7:
                LED.append("7");
                break;
            case R.id.d8:
                LED.append("8");
                break;
            case R.id.d9:
                LED.append("9");
                break;
        }
    }




    // 인용한 코드입니다.
    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }


}
