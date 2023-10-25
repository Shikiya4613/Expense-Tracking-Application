package com.example.expensetrackingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class SecondActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private EditText expenseDate;
    private EditText expenseCost;
    private int priceTracker = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        setDate();
        expenseDate = findViewById(R.id.expense_detail1_input);
        expenseDate.setText(getToday());

        expenseCost = findViewById(R.id.expense_detail4_input);
        expenseCost.setText("0.00");
        expenseCost.setCursorVisible(false);
        expenseCost.setLongClickable(false);
        expenseCost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                expenseCost.removeTextChangedListener(this);
                String input = editable.toString();
                if (!input.isEmpty()) {
                    if (input.length() > priceTracker) {
                        System.out.println("+" + Double.parseDouble(input));
                        String cost = String.format("%.2f", Double.parseDouble(input)*10);
                        expenseCost.setText(cost);
                        priceTracker = cost.length();
                    }
                    else {
                        System.out.printf("-" + Double.parseDouble(input));
                        String cost = String.format("%.2f", Double.parseDouble(input)/10);
                        expenseCost.setText(cost);
                        priceTracker = input.length();
                        if (priceTracker < 4) {
                            priceTracker = 4;
                        }
                    }
                }
                else {
                    System.out.printf("0" + Double.parseDouble(input));
                    expenseCost.setText("0.00");
                    priceTracker = 4;
                }
                expenseCost.addTextChangedListener(this);
                expenseCost.setSelection(priceTracker);
            }
        });
    }

    public void returnToMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
    }

    public void setDate() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String date = year + "/" + (month+1) + "/" + day;
                expenseDate.setText(date);
            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
    }
    public void selectDate(View view) {
        datePickerDialog.show();
    }

    public String getToday() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "/" + month + "/" + day;
    }
}
