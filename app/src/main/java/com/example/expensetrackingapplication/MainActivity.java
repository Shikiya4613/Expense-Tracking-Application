package com.example.expensetrackingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = findViewById(R.id.spinner_main);
        String[] itemList = {"date", "name", "category"};
        ArrayAdapter<Expense> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ListView listView = findViewById(R.id.expense_list);
        List<Expense> expenseList = new ArrayList<>();
        Expense[] examples = {new Expense("Grocery", "for October", "Food","example 1", 78.53,"2023/09/24"),
                new Expense("Fast Food", "dinner", "Food", "example 2", 24.76, "2023/09/21"),
                new Expense("Rent", "for October", "Utilities", "example 3", 900.00, "2023/09/30"),
                new Expense("Internet", "for October", "Utilities", "example 4", 50, "2023/09/29")};
        expenseList.addAll(Arrays.asList(examples));
        List<Expense> filteredList = new ArrayList<>(expenseList);
        ArrayAdapter<Expense> listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filteredList);
        listView.setAdapter(listAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                String filter = itemList[pos];
                filteredList.clear();
                for (Expense expense : expenseList) {

                }
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void launchSecondActivity(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }
}