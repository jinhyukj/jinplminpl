package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.paint.PaintView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import java.io.OutputStream;
import java.util.List;

import android.content.ContentValues;
import android.graphics.Bitmap.CompressFormat;
import android.provider.MediaStore.Images.Media;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import yuku.ambilwarna.AmbilWarnaDialog;

import static android.app.Activity.RESULT_OK;


public class Tab3 extends Fragment {
    Switch lock;
    int defaultColor, penSize;
    ImageButton imageEraser, imgColor, choosePicture, redo, undo, save;
    SeekBar seekBar;
    TextView txtPenSize;
    PaintView paintView;
    Bitmap bmp, alteredBitmap;
    Canvas canvas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_tab3, container, false);

        paintView = view.findViewById(R.id.paintView);
        choosePicture = view.findViewById(R.id.ChoosePictureButton);
        imageEraser = view.findViewById(R.id.btnEraser);
        imgColor = view.findViewById(R.id.btnColor);
        seekBar = view.findViewById(R.id.penSize);
        txtPenSize = view.findViewById(R.id.txtPenSize);
        lock = view.findViewById(R.id.lock);
        undo = view.findViewById(R.id.undo);
        redo = view.findViewById(R.id.redo);
        save = view.findViewById(R.id.save);

        askPermission();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        paintView.initialise(displayMetrics);
        defaultColor = ContextCompat.getColor(getActivity(), R.color.black);

        imgColor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

        undo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view1){
                paintView.undo();
            }
        });

        redo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view1){
                paintView.redo();
            }
        });

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view1){
                paintView.saveImage();

            }
        });

        imageEraser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view1){
                paintView.clear();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = seekBar.getProgress();
                paintView.setStrokeWidth(progress);
                txtPenSize.setText(progress + "dp");
            }
        });

        lock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((MainActivity) getActivity()).lockchecked(isChecked);
            }
        });


        choosePicture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view1){
                Intent choosePictureIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(choosePictureIntent, 0);
            }
        });
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            Uri imageFileUri = intent.getData();
            try {
                BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
                bmpFactoryOptions.inJustDecodeBounds = true;
                bmp = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageFileUri), null, bmpFactoryOptions);

                bmpFactoryOptions.inJustDecodeBounds = false;
                bmp = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageFileUri), null, bmpFactoryOptions);

                paintView.Drawing(bmp);

                //choosenImageView.setImageBitmap(alteredBitmap);
                //choosenImageView.setOnTouchListener(this);

            } catch (Exception e) {
                Log.v("ERROR", e.toString());
            }
        }
    }

    private void openColorPicker() {
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(getContext(), defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                Toast.makeText(getContext(), "Unavailable", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColor = color;
                paintView.setColor(color);
            }
        });
        ambilWarnaDialog.show(); // add
    }

    private void askPermission(){
        Dexter.withContext(getContext())
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener(){

            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                Toast.makeText(getContext(), "Granted!", Toast.LENGTH_SHORT ).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }
}