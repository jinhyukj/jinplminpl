package com.example.myapplication;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class Tab1 extends Fragment {
    private RecyclerView recyclerView;
    ArrayList<ContactModel> arrayList = new ArrayList<ContactModel>();
    MainAdapter adapter;
    public static final int sub = 1001; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    private void checkPermisson() {
        //check condition
        if(ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
            //when permisson is not granted
            //request permisson
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 100);
        }
        else{
            //when permission granted
            //Create method
            getContactList();
        }
    }

    private void getContactList() {
        //Initialize uri
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        //sort by ascending
        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC";
        ContentResolver contentResolver = getActivity().getContentResolver();
        //Initialize cursor
        Cursor cursor = contentResolver.query(uri, null, null, null, sort);

        //Check condition
        if(cursor.getCount() > 0 ){
            //when count is greater than 0
            //Use while loop
            while(cursor.moveToNext()){
                //Cursor move to nest
                //Get contact id
                String id = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.Contacts._ID
                ));

                //Get contact name
                String name = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME
                ));

                //Initialize phone uri
                Uri uriphone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

                //Initialize selection
                String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?";

                //Initialize phone cursor
                Cursor phoneCursor = contentResolver.query(uriphone, null, selection, new String[]{id}, null);

                //check condition
                if(phoneCursor.moveToNext()){
                    //When phone cursor move to next
                    String number = phoneCursor.getString(phoneCursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                    ));

                    //Initialize contact model
                    ContactModel model = new ContactModel();
                    //Set name
                    model.setName(name);
                    //Set number
                    model.setNumber(number);
                    //Add model in array list
                    arrayList.add(model);

                    //close phone cursor
                    phoneCursor.close();
                }
            }

            //Close cursor
            cursor.close();
        }
        //Set Layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        //Initialize adapter
        adapter = new MainAdapter(this.getActivity(), arrayList);

        //Set adapter
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Check condition
        if(requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //When permission is granted
            //call method
            getContactList();
        }
        else{
            //When permission is denied
            //Display toast
            checkPermisson();

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view =  inflater.inflate(R.layout.fragment_tab1, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        Button button_add = view.findViewById(R.id.button_add);
        Button button_delete = view.findViewById(R.id.button_delete); /*페이지 전환버튼*/
        View view1 =  inflater.inflate(R.layout.fragment_add, container, false);

        //String name = view1.findViewById(R.id.name);
        //phone = view1.findViewById(R.id.phone);
        //add = view1.findViewById(R.id.btnAdd);

        checkPermisson();

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(),addContact.class);
                startActivityForResult(intent,sub);//액티비티 띄우기
            }
        });

        return view;
    }


}
