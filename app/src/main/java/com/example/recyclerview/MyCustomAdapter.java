package com.example.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyCustomAdapter extends RecyclerView.Adapter<MyCustomAdapter.ViewHolder> {

    private List<Contact> contacts;
    private DeleteButtonClickListener deleteButtonClickListener; // Define the delete click listener

    private int selectedPosition = RecyclerView.NO_POSITION; // Initialize with no selection


    public int getSelectedPosition() {
        return selectedPosition;
    }

    public MyCustomAdapter(List<Contact> contacts,DeleteButtonClickListener deleteListener) {
        this.contacts = contacts;
        this.deleteButtonClickListener = deleteListener;

    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }


    public void clearSelection() {
        selectedPosition = RecyclerView.NO_POSITION;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_custom_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.nameTextView.setText(contact.getName());
        holder.phoneNumberTextView.setText(contact.getPhoneNumber());
        holder.emailAddressTextView.setText(contact.getEmailAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition(); // or getLayoutPosition()
                // Perform some action based on the position.
            }
        });

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public interface DeleteButtonClickListener {
        void onDeleteButtonClick(int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView phoneNumberTextView;
        private final TextView emailAddressTextView;
        private final ImageButton editButton;
        private final ImageButton deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.my_name_text_view);
            phoneNumberTextView = itemView.findViewById(R.id.my_phone_number_text_view);
            emailAddressTextView = itemView.findViewById(R.id.my_email_address_text_view);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            // Set a click listener for the "Delete" button
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (deleteButtonClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            deleteButtonClickListener.onDeleteButtonClick(position);
                        }
                    }
                }
            });
        }
    }
}
