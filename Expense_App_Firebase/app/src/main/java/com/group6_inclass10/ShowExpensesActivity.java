package com.group6_inclass10;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class ShowExpensesActivity extends AppCompatActivity {

    Expense expense;
    TextView name;
    TextView category;
    TextView amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_expenses);
        setTitle("Show Expenses");
        expense = (Expense) getIntent().getExtras().getSerializable("expense");
        name = (TextView) findViewById(R.id.showName);
        name.setText(expense.getExpenseName());
        category = (TextView)findViewById(R.id.showCategory);
        category.setText(expense.getCategory());
        amount = (TextView)findViewById(R.id.showAmount);
        amount.setText("$ "+Double.toString(expense.getAmount()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        TextView tv = (TextView) findViewById(R.id.showDate);
        tv.setText(expense.getDate());
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
