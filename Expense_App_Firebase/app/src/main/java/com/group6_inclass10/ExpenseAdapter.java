package com.group6_inclass10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by shashank on 10/17/2016.
 */
public class ExpenseAdapter extends ArrayAdapter<Expense> {
    List<Expense> expenseList;
    Context context;
    int resource;

    public ExpenseAdapter(Context context, int resource, List<Expense> expenseList) {
        super(context, resource, expenseList);
        this.expenseList = expenseList;
        this.resource = resource;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(resource, parent, false);
        Expense expense = expenseList.get(position);
        TextView name = (TextView) convertView.findViewById(R.id.listExpenseName);
        name.setText(expense.getExpenseName());
        TextView amount = (TextView) convertView.findViewById(R.id.listExpenseAmount);
        amount.setText("$ "+Double.toString(expense.getAmount()));
        convertView.setBackgroundColor(android.graphics.Color.parseColor("#F0F0F0"));
        return convertView;
    }
}
