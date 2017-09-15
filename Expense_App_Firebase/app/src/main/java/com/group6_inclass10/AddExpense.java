package com.group6_inclass10;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddExpense extends AppCompatActivity {

    boolean flagSelected;
    EditText expName;
    EditText dateS;
    EditText amount;
    Button addExpense;
    Expense expense = new Expense();
    ArrayList<Expense> expenseArray = new ArrayList<Expense>();
    private DatabaseReference mDatabase;
    private String uid;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        setTitle("Add Expense");
        flagSelected=false;
        String uid = getIntent().getExtras().getString("uid");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("expenses");
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.displayCategory);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categoryArray, android.R.layout.simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                if(pos==0){
                    flagSelected=false;

                }
                else {
                    flagSelected=true;
                    String item = (String) adapterView.getItemAtPosition(pos);
                    expense.setCategory(item);

                    Log.d("selected item : ", item);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        findViewById(R.id.addExpenseButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {

                    expName = (EditText) findViewById(R.id.displayName);
                    amount = (EditText) findViewById(R.id.displayAmount);


                    if (expName.getText().toString().trim().equals("") || amount.getText().toString().trim().equals("") ||flagSelected==false) {
                       /* AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("All fields are required.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();*/
                        Toast.makeText(AddExpense.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    } else {
                        expense.setExpenseName(expName.getText().toString());
                        expense.setAmount(Double.parseDouble(amount.getText().toString()));
                        Date date1 = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                        expense.setDate(dateFormat.format(date1));
                       /* expenseArray.add(expense);
                        mListener.addExpenseData(expense);*/
                        String key  = mDatabase.push().getKey();
                        expense.setKey(key);
                        mDatabase.child(key).setValue(expense);
                        finish();
                        count++;
                    }
                }
            }
        });
    }
}
