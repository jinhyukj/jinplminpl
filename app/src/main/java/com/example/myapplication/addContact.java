package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class addContact extends AppCompatActivity {
    EditText firstname, lastname, email, phone;
    Button addContact;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragement_addcontact);

        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        addContact = findViewById(R.id.btnAdd);

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!lastname.getText().toString().isEmpty() && !phone.getText().toString().isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_INSERT);
                    intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                    intent.putExtra(ContactsContract.Intents.Insert.NAME, firstname.getText().toString() + ' ' +lastname.getText().toString());
                    intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone.getText().toString());
                    intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email.getText().toString());
                    if(intent.resolveActivity(getPackageManager()) != null){
                        startActivity(intent);
                    }

                    else{
                        Toast.makeText(addContact.this, "There is no app that support this action", Toast.LENGTH_SHORT).show();
                    }

                }

                else{
                    Toast.makeText(addContact.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }

                finish();
            }


        });

    }
}