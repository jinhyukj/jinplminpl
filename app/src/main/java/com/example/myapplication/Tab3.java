package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.paint.PaintView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.kyanogen.signatureview.SignatureView;

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
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import java.io.OutputStream;
import java.util.List;

import android.content.ContentValues;
import android.graphics.Bitmap.CompressFormat;
import android.provider.MediaStore.Images.Media;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import yuku.ambilwarna.AmbilWarnaDialog;

import static android.app.Activity.RESULT_OK;


public class Tab3 extends Fragment {
    ImageView choosenImageView;

    Bitmap bmp;
    Bitmap alteredBitmap;
    Canvas canvas;
    Paint paint;
    Matrix matrix;

    int defaultColor;

    SignatureView signatureView;
    ImageButton imageEraser, imgColor, imgSave, choosePicture;
    SeekBar seekBar;
    TextView txtPenSize;

    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_tab3, container, false);
        context = container.getContext();

        //choosenImageView = view.findViewById(R.id.ChoosenImageView);

        choosePicture = view.findViewById(R.id.ChoosePictureButton);
        signatureView = view.findViewById(R.id.signature_view);
        imageEraser = view.findViewById(R.id.btnEraser);
        imgColor = view.findViewById(R.id.btnColor);
        imgSave = view.findViewById(R.id.btnSave);
        seekBar = view.findViewById(R.id.penSize);
        txtPenSize = view.findViewById(R.id.txtPenSize);

        askPermission();

        defaultColor = ContextCompat.getColor(getActivity(), R.color.black);

        imgColor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

        imageEraser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view1){
                signatureView.clearCanvas();
            }
        });

        choosePicture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view1){
                //choosenImageView.setOnTouchListener(this);
                Intent choosePictureIntent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(choosePictureIntent, 0);
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtPenSize.setText(progress + "dp");
                signatureView.setPenSize(progress);
                seekBar.setMax(50);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        imgSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!signatureView.isBitmapEmpty()){
                    SaveImage();
                }
            }
        });


        return view;
    }

    private void SaveImage() {
            ContentValues contentValues = new ContentValues(3);
            contentValues.put(Media.DISPLAY_NAME, "Draw On Me");

            Uri imageFileUri = getActivity().getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, contentValues);
            try {
                OutputStream imageFileOS = getActivity().getContentResolver().openOutputStream(imageFileUri);
                alteredBitmap.compress(CompressFormat.JPEG, 90, imageFileOS);
                Toast t = Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT);
                t.show();

            } catch (Exception e) {
                Log.v("EXCEPTION", e.getMessage());
            }
    }

    private void openColorPicker() {
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(getContext(), defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColor= color;
                signatureView.setPenColor(color);
            }
        });
        ambilWarnaDialog.show();
    }

    private void askPermission(){
        Dexter.withContext(getContext())
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener(){

            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                Toast.makeText(context, "Granted!", Toast.LENGTH_SHORT ).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();

            }
        }).check();
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

                alteredBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

                canvas = new Canvas(alteredBitmap);
                paint = new Paint();
                paint.setColor(Color.GREEN);
                paint.setStrokeWidth(5);
                matrix = new Matrix();
                canvas.drawBitmap(bmp, matrix, paint);

                choosenImageView.setImageBitmap(alteredBitmap);
                choosenImageView.setOnTouchListener((OnTouchListener) this);
            } catch (Exception e) {
                Log.v("ERROR", e.toString());
            }
        }
    }



}