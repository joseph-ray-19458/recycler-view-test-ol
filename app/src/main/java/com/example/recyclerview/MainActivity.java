package com.example.recyclerview;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MyCustomAdapter.DeleteButtonClickListener {

    private MyDatabase database;
    private EditText nameInput;
    private EditText phoneInput;
    private EditText emailInput;
    private RecyclerView recyclerView;
    private Button submitButton;
    private TextView noData;

    private List<Contact> contacts;
    private MyCustomAdapter adapter;
    private ProgressBar progressBar;
    private ImageButton deleteBtn , editBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameInput = findViewById(R.id.nameInput);
        phoneInput = findViewById(R.id.phoneInput);
        emailInput = findViewById(R.id.emailInput);
        recyclerView = findViewById(R.id.recycler_view);
        submitButton = findViewById(R.id.submit);
        noData = findViewById(R.id.noData);
        deleteBtn = findViewById(R.id.deleteButton);
        editBtn = findViewById(R.id.editButton);


        progressBar = findViewById(R.id.progressLoading);
        recyclerView.setVisibility(View.INVISIBLE);
        noData.setVisibility(View.INVISIBLE);

        database = MyDatabaseProvider.getDatabase(this);

        contacts = new ArrayList<>();
//        contacts.add(new Contact("joeph","7598575755","abc@abc.com"));
        adapter = new MyCustomAdapter(contacts,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);



        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<Contact> resultContacts = database.contactDao().getAllContacts();

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            contacts.clear();
                            contacts.addAll(resultContacts);
                            adapter.notifyDataSetChanged();

                            if (resultContacts.isEmpty()) {
                                progressBar.setVisibility(View.INVISIBLE);
                                noData.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.INVISIBLE);
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                noData.setVisibility(View.INVISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                } catch (Exception e) {
                    // Handle the exception here
                }
            }
        }).start();



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameInput.getText().toString();
                String phone = phoneInput.getText().toString();
                String email = emailInput.getText().toString();

                Contact contact = new Contact(name, phone, email);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            database.contactDao().insertContact(contact);
                            contacts.add(contact);

                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_SHORT).show();
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        } catch (Exception e) {
                            // Handle the exception here
                        }
                    }
                }).start();

                nameInput.setText("");
                phoneInput.setText("");
                emailInput.setText("");
            }
        });


        }

    @Override
    public void onDeleteButtonClick(int position) {
        // Handle the delete button click for the item at the specified position
        // You can prompt the user for confirmation before deleting the item
        // and then remove the item from the list and update the RecyclerView
        Toast.makeText(getApplicationContext(), "Position " + position + " deleted", Toast.LENGTH_SHORT).show();

        // Assuming 'contacts' is your list of Contact objects, remove the item at the specified position:
        if (position >= 0 && position < contacts.size()) {
            Contact deletedContact = contacts.remove(position);

            // Remove the item from the database as well (you may need to implement the logic to do this)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        database.contactDao().deleteContact(deletedContact);
                    } catch (Exception e) {
                        // Handle the exception here
                    }
                }
            }).start();

            // Notify the adapter that the item has been removed
            adapter.notifyItemRemoved(position);

            if (contacts.isEmpty()) {
                recyclerView.setVisibility(View.INVISIBLE);
                noData.setVisibility(View.VISIBLE);
            }
        }
    }

}
