package com.example.expensetrackingapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity implements ExpenseAdapter.ExpenseListListener {

    private static final String EXPENSE_KEY = "expenseList";
    private static final String PREFS_NAME = "expenseAddress";

    /* private Expense[] example = {new Expense("Rent", 1300.00, "2023/09/30", "Rent and Utilities", "rent for October", "example item 1"),
            new Expense("Gas Refill", 20.00, "2023/09/21",  "Transportation", "out of gas", "example item 2"),
            new Expense("Grocery", 123.45, "2023/09/21", "Food", "out of food", "example item 3"),
            new Expense("Pizza Delivery",  36.00, "2023/09/27", "Food", "lunch at work","example item 4")};*/
    private String filter = " ";
    private ExpenseAdapter adapter;
    private List<Expense> expenses;
    private ActivityResultLauncher<Intent> launcher;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Spinner spinner = findViewById(R.id.spinner_main);
        String[] optList = {" ","date", "name", "category"};
        ArrayAdapter<String> adaptSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, optList);
        adaptSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adaptSpinner);

        ListView listView = findViewById(R.id.expenseList);
        adapter = new ExpenseAdapter(getApplicationContext());
        listView.setAdapter(adapter);

        //expenses = new ArrayList<>(Arrays.asList(example));
        expenses = loadExpense();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long tag) {
                filter = optList[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        for(Expense expense:expenses) {
            adapter.add(new Expense(expense.getName(), expense.getCost(), expense.getDate(), expense.getCategory(), expense.getReason(), expense.getNote()));
        }
        recalculate();

         launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            String[] components = data.getStringArrayExtra("New Expense");
                            Expense newExpense = new Expense(components[0], Double.parseDouble(components[1]), components[2], components[3], components[4], components[5]);
                            adapter.add(newExpense);
                            expenses.add(newExpense);
                            recalculate();
                            saveExpense();
                        }
                    }
                }
        );
         adapter.setListener(this);
    }

    public void saveExpense() {
        SharedPreferences.Editor editor = preferences.edit();
        StringBuilder builder = new StringBuilder();
        for (Expense expense : expenses) {
            builder.append(serialize(expense)).append(",");
        }
        editor.putString(EXPENSE_KEY, builder.toString());
        editor.apply();
    }

    public List<Expense> loadExpense() {
        List<Expense> loadedExpenseList = new ArrayList<>();
        String[] expenseItems = preferences.getString(EXPENSE_KEY, "").split(",");
        for (String expenseItem : expenseItems) {
            if (!expenseItem.isEmpty()) {
                Expense newExpense = deserialize(expenseItem);
                loadedExpenseList.add(newExpense);
            }
        }
        recalculate();
        return loadedExpenseList;
    }

    public void reset(View view) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        expenses.clear();
        adapter.clear();
        recalculate();
    }

    public String serialize(Expense expense) {
        return expense.getName() + "|" + expense.getCost() + "|" + expense.getDate() + "|" + expense.getCategory() + "|" + expense.getReason() + "|" + expense.getNote();
    }

    public Expense deserialize(String expenseItem) {
        String[] components = expenseItem.split("\\|");
        return new Expense(components[0], Double.parseDouble(components[1]), components[2], components[3], components[4], components[5]);
    }

    public void launchSecondActivity(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        launcher.launch(intent);
    }

    public void filterList(View view) {
        EditText search = findViewById(R.id.text_search_input);
        String key = search.getText().toString().toLowerCase();
        if (filter.equals(" ")) {
            adapter.clear();
            for(Expense expense:expenses) {
                adapter.add(new Expense(expense.getName(), expense.getCost(), expense.getDate(), expense.getCategory(), expense.getReason(), expense.getNote()));
            }
        }
        else if (filter.equals("date")) {
            adapter.clear();
            for(Expense expense:expenses) {
                if (expense.getDate().toLowerCase().contains(key)) {
                    adapter.add(new Expense(expense.getName(), expense.getCost(), expense.getDate(), expense.getCategory(), expense.getReason(), expense.getNote()));
                }
            }
        }
        else if (filter.equals("name")) {
            adapter.clear();
            for(Expense expense:expenses) {
                if (expense.getName().toLowerCase().contains(key)) {
                    adapter.add(new Expense(expense.getName(), expense.getCost(), expense.getDate(), expense.getCategory(), expense.getReason(), expense.getNote()));
                }
            }
        }
        else if (filter.equals("category")) {
            adapter.clear();
            for(Expense expense:expenses) {
                if (expense.getCategory().toLowerCase().contains(key)) {
                    adapter.add(new Expense(expense.getName(), expense.getCost(), expense.getDate(), expense.getCategory(), expense.getReason(), expense.getNote()));
                }
            }
        }
        else {
            System.out.println("Undefined string caught for spinner input\n");
            System.exit(-1);
        }

        recalculate();
    }

    public void recalculate() {
        EditText total = findViewById(R.id.textTotal);
        total.setText(adapter.total());
    }

    @Override
    public void editExpense(Expense expense) {
        String[] decodedExpense = {expense.getName(), String.format("%.2f", expense.getCost()), expense.getDate(),
                expense.getCategory(), expense.getReason(), expense.getNote()};
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("editExpense", decodedExpense);
        launcher.launch(intent);
        adapter.delete(expense);
    }

    @Override
    public void deleteExpense() {
        recalculate();
    }
}