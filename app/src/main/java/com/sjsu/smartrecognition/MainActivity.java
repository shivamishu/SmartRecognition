package com.sjsu.smartrecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int IMAGE_REQUEST_CODE = 0612;
    private ImageView uploadImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        uploadImageView = (ImageView) findViewById(R.id.uploadImageView);

        uploadImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openImageIntent();
            }
        });

        Button recognizeButton = (Button) findViewById(R.id.recognize);

        recognizeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                computeRecognition();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_REQUEST_CODE) {
            uploadImageView.setImageURI(data.getData());
//            if (data != null) {
//                val tempuri = data
//                if ("com.google.android.apps.photos.contentprovider".equals(tempuri.getAuthority()) ) {
//                    val pathUri: String = tempuri.getPath()!!
//                            val startindex = pathUri.indexOf("content")
//                    val lastindex = pathUri.lastIndex
//                    val newUri: String = pathUri.substring(startindex, lastindex)
//                    imageuri=newUri //data?.data!!
//                    Log.i("PostActivity",newUri)
//                }
            Toast.makeText(this, "Photo added successfully", Toast.LENGTH_SHORT).show();
        }
    }
    private void openImageIntent(){
        Intent imageOpener = new Intent(Intent.ACTION_SEND);
        imageOpener.setType("image/*");
        if (imageOpener != null) {
            startActivityForResult(imageOpener, IMAGE_REQUEST_CODE);
        } else {
            Toast.makeText(this, "Please allow photos permission", Toast.LENGTH_SHORT).show();
        }
    }
    private void computeRecognition() {
    }
}