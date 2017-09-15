package com.group6_inclass10;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ExpenseAppActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    ArrayList<Expense> expenseArray = new ArrayList<Expense>();
    Context context;
    private String uid;
    ListView listView;
    ExpenseAdapter expenseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_app);

        setTitle("Expense App");
        final TextView text = (TextView) findViewById(R.id.message);
        uid = getIntent().getExtras().getString("uid");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("expenses");
           mDatabase.addChildEventListener(new ChildEventListener() {
               @Override
               public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                   Log.d("test","inside child");
                   Expense expense = dataSnapshot.getValue(Expense.class);
                   expenseArray.add(expense);

                   if(expenseArray.size()==0) {
                       text.setText("There is no expense to show, Please add your expenses from the menu");
                   } else {
                       text.setText("");
                   }
                   listView = (ListView) findViewById(R.id.listView);
                   expenseAdapter = new ExpenseAdapter(ExpenseAppActivity.this, R.layout.row_layout, expenseArray);
                   listView.setAdapter(expenseAdapter);
                   listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                       @Override
                       public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                           Intent intent = new Intent(ExpenseAppActivity.this,ShowExpensesActivity.class);
                           intent.putExtra("expense", expenseArray.get(i));
                           startActivity(intent);
                       }
                   });
                   listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                       @Override
                       public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                           Expense expense = expenseArray.get(i);
                           mDatabase.child(expense.getKey()).removeValue();
                           expenseArray.remove(i);
                           expenseAdapter.notifyDataSetChanged();
                           Toast.makeText(ExpenseAppActivity.this,"Expense deleted", Toast.LENGTH_SHORT).show();
                           if(expenseArray.size()==0) {
                               text.setText("There is no expense to show, Please add your expenses from the menu");
                           } else {
                               text.setText("");
                           }
                           return true;
                       }
                   });

               }

               @Override
               public void onChildChanged(DataSnapshot dataSnapshot, String s) {

               }

               @Override
               public void onChildRemoved(DataSnapshot dataSnapshot) {

               }

               @Override
               public void onChildMoved(DataSnapshot dataSnapshot, String s) {

               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           });
        findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ExpenseAppActivity.this,AddExpense.class);
                i.putExtra("uid", uid);
                startActivity(i);

            }
        });



    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        final TextView text = (TextView) findViewById(R.id.message);

        if(expenseArray.size()==0) {
            text.setText("There is no expense to show, Please add your expenses from the menu");
        } else {
            text.setText("");
        }
        ListView listView = (ListView) findViewById(R.id.listView);
        final ExpenseAdapter expenseAdapter = new ExpenseAdapter(this, R.layout.row_layout, expenseArray);
        listView.setAdapter(expenseAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ExpenseAppActivity.this,ShowExpensesActivity.class);
                intent.putExtra("expense", expenseArray.get(i));
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Expense expense = expenseArray.get(i);
                mDatabase.child(expense.getKey()).removeValue();
                expenseArray.remove(i);
                expenseAdapter.notifyDataSetChanged();
                Toast.makeText(ExpenseAppActivity.this,"Expense deleted", Toast.LENGTH_SHORT).show();
                if(expenseArray.size()==0) {
                    text.setVisibility(View.VISIBLE);
                } else {
                    text.setVisibility(View.INVISIBLE);
                }
                return true;
            }
        });
    }
}
