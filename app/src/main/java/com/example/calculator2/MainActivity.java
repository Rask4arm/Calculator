package com.example.calculator2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity {
    public String display="0";
    public String history="";
    public int reset = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void onDigitClicked(View view){
        Button button = (Button) view;
        String digit = button.getText().toString();
        if (reset==1){
            resetDisplay(digit);
            reset = 0;
        } else if (reset==0) {
            updateDisplay(digit);
        }
    }

    public void onOperationClicked(View view){
        Button button = (Button) view;
        String operator = button.getText().toString();
        reset = 0;
        updateDisplay(operator);
    }

    public void updateDisplay(String character){
        display += character;
        TextView textView = findViewById(R.id.textView);
        textView.setText(display);
    }

    public void resetDisplay(String character){
        display = character;
        TextView textView = findViewById(R.id.textView);
        textView.setText(display);
    }

    public void onEqualsClicked(View view){
        TextView textView = findViewById(R.id.textView);
        String expressionString = textView.getText().toString();

        try {
            Expression expression = new ExpressionBuilder(expressionString).build();
            String result = BigDecimal.valueOf(expression.evaluate())
                    .setScale(10, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();

            resetDisplay(result);
            reset = 1;
            history += expressionString + "=" + result + "\n";
        } catch (IllegalArgumentException e) {
            // Handles invalid math syntax entered by the user (e.g., "5++3")
            resetDisplay("Err");
            reset = 1;
        }
    }

    public void onCClicked(View view) {
        resetDisplay("0");
        reset = 1;
    }

    public void onACClicked(View view) {
        resetDisplay("0");
        history="";
        reset = 1;
    }
    public void onNegativeClicked(View view) {
        String result = display;
        for (int i = 1; display.length() - i >= 0 && (Character.isDigit(display.charAt(display.length() - i)) ||
                display.charAt(display.length() - i) == '.'); i++) {
            result = new StringBuilder(display).insert(display.length() - i, "-").toString();
        }
        display = result;
        TextView textView = findViewById(R.id.textView);
        textView.setText(display);
    }

    public void onHistoryClicked(View view) {
        setContentView(R.layout.history);
        TextView historyView = findViewById(R.id.historyView);
        historyView.setText(history);
    }

    public void onBackClicked(View view) {
        setContentView(R.layout.activity_main);
    }
}