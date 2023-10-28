package com.example.expensetrackingapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ExpenseAdapter extends ArrayAdapter<Expense> {

    public interface ExpenseListListener {
        void editExpense(Expense expense);
        void deleteExpense();
    }

    private ExpenseListListener listener;

    static class ItemViewHolder {
        TextView expense_name;
        TextView expense_cost;
        TextView expense_date;
        TextView expense_category;
        TextView expense_reason;
        TextView expense_note;
        Button edit_expense;
        Button delete_expense;
    }
    private static final String TAG = "ExpenseItemDefaultTag";
    private List<Expense> expenseList = new ArrayList<>();

    public ExpenseAdapter(Context context) {
        super(context, R.layout.item_expense);
    }

    public void setListener(ExpenseListListener listener) {
        this.listener = listener;
    }

    public String total() {
        double total = 0.00;
        for (Expense expense : expenseList) {
            total += expense.getCost();
        }
        return "$ " + String.format("%.2f", total);
    }

    public void delete(Expense expense) {
        expenseList.remove(expense);
        super.notifyDataSetChanged();
    }

    public void edit(int index) {

    }

    @Override
    public void add(Expense expense) {
        expenseList.add(expense);
        super.add(expense);
    }

    @Override
    public void clear() {
        expenseList = new ArrayList<>();
        super.clear();
    }

    @Override
    public int getCount() {
        return expenseList.size();
    }

    @Override
    public Expense getItem(int index) {
        return expenseList.get(index);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewHolder viewHolder;
        View item = convertView;
        if (item == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            item = inflater.inflate(R.layout.item_expense, parent, false);
            viewHolder = new ItemViewHolder();
            viewHolder.expense_name = (TextView)item.findViewById(R.id.expense_name);
            viewHolder.expense_cost = (TextView)item.findViewById(R.id.expense_cost);
            viewHolder.expense_date = (TextView)item.findViewById(R.id.expense_date);
            viewHolder.expense_category = (TextView)item.findViewById(R.id.expense_category);
            viewHolder.expense_reason = (TextView)item.findViewById(R.id.expense_reason);
            viewHolder.expense_note = (TextView)item.findViewById(R.id.expense_note);
            viewHolder.delete_expense = (Button)item.findViewById(R.id.delete_expense);
            viewHolder.edit_expense = (Button) item.findViewById(R.id.edit_expense);
            item.setTag(viewHolder);
        }
        else {
            viewHolder = (ItemViewHolder)item.getTag();
        }
        Expense expense = getItem(position);
        viewHolder.expense_name.setText(expense.getName());
        viewHolder.expense_cost.setText("$" + String.format("%.2f", expense.getCost()));
        viewHolder.expense_date.setText(expense.getDate());
        viewHolder.expense_category.setText(expense.getCategory());
        viewHolder.expense_reason.setText(expense.getReason());
        viewHolder.expense_note.setText(expense.getNote());

        Button editButton = viewHolder.edit_expense;
        Button deleteButton = viewHolder.delete_expense;

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.editExpense(getItem(position));
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    delete(getItem((position)));
                    listener.deleteExpense();
                }
            }
        });

        return item;
    }
}
