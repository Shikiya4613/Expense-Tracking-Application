package com.example.expensetrackingapplication;

public class Expense{
    private String name;
    private String category;
    private double cost;
    private String reason;
    private String notes;
    private String date;

    public Expense() {
        name = "";
        reason = "";
        category = "";
        notes = "";
        cost = 0.0;
        date = "";
    }
    public Expense(String name, String reason, String category, String notes, double cost, String date) {
        name = this.name;
        reason = this.reason;
        category = this.category;
        notes = this.notes;
        cost = this.cost;
        date = this.date;
    }
}
