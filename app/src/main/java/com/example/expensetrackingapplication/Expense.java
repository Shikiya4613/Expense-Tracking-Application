package com.example.expensetrackingapplication;

public class Expense{
    private String name;
    private double cost;
    private String category;
    private String date;
    private String reason;
    private String note;

    public Expense() {
        name = "";
        cost = 0.0;
        date = "";
        category = "";
        reason = "";
        note = "";
    }
    public Expense(String name, double cost, String date, String category, String reason, String note) {
        this.name = name;
        this.cost = cost;
        this.date = date;
        this.category = category;
        this.reason = reason;
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public String getReason() {
        return reason;
    }

    public String getNote() {
        return note;
    }
}
