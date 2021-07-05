package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.paint.PaintView;

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
import android.widget.ImageView;
import java.io.OutputStream;
import android.content.ContentValues;
import android.graphics.Bitmap.CompressFormat;
import android.provider.MediaStore.Images.Media;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;


public class Tab3 extends Fragment implements OnClickListener, OnTouchListener {
    PaintView paintView;
    ImageView choosenImageView;
    Button choosePicture;
    Button savePicture;

    Bitmap bmp;
    Bitmap alteredBitmap;
    Canvas canvas;
    Paint paint;
    Matrix matrix;

    float downx = 0;
    float downy = 0;
    float upx = 0;
    float upy = 0;

    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_tab3, container, false);
        context = container.getContext();
        paintView = new PaintView(this.getContext());
        choosenImageView = (ImageView) view.findViewById(R.id.ChoosenImageView);
        choosePicture = (Button) view.findViewById(R.id.ChoosePictureButton);
        savePicture = (Button) view.findViewById(R.id.SavePictureButton);

        savePicture.setOnClickListener(this);
        choosePicture.setOnClickListener(this);
        choosenImageView.setOnTouchListener(this);
        return view;
    }

    public void onClick(View v) {
        if (v == choosePicture) {
            Intent choosePictureIntent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(choosePictureIntent, 0);
        }
        else if (v == savePicture) {
            if (alteredBitmap != null) {
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
        }
    }

    public void onActivityResult(int requestCode, int resultCode,
                                 Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK) {
            Uri imageFileUri = intent.getData();
            try {
                BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
                bmpFactoryOptions.inJustDecodeBounds = true;
                bmp = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(
                        imageFileUri), null, bmpFactoryOptions);

                bmpFactoryOptions.inJustDecodeBounds = false;
                bmp = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(
                        imageFileUri), null, bmpFactoryOptions);

                alteredBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp
                        .getHeight(), bmp.getConfig());
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
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downx = event.getX();
                downy = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                upx = event.getX();
                upy = event.getY();
                canvas.drawLine(downx, downy, upx, upy, paint);
                choosenImageView.invalidate();
                downx = upx;
                downy = upy;
                break;
            case MotionEvent.ACTION_UP:
                upx = event.getX();
                upy = event.getY();
                canvas.drawLine(downx, downy, upx, upy, paint);
                choosenImageView.invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return true;
    }


}