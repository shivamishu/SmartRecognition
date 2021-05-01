package com.sjsu.smartrecognition;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sjsu.smartrecognition.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.sjsu.smartrecognition.R.drawable.firebase_icon;

public class MainActivity extends AppCompatActivity {

    private static final int IMAGE_REQUEST_CODE = 0612;
    private static final int LOGIN_REQUEST_CODE = 0200;
    private NavController navController;
    private ActivityMainBinding binding;
    private DrawerLayout drawerLayout;
    List<AuthUI.IdpConfig> providers;
    public static String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        drawerLayout = binding.drawerLayout;
        navController = Navigation.findNavController(this, R.id.myNavHostFragment);
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupWithNavController(binding.navView, navController);
        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.selection) {
                    Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_LONG).show();
                } else if (id == R.id.logoutFragment) {
                    Toast.makeText(getApplicationContext(), "Logging out..", Toast.LENGTH_SHORT).show();
                    showLogoutOptions();
                }
                //maintain Navigation view standard behavior
                NavigationUI.onNavDestinationSelected(item, navController);
                //close drawer
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );
        if (savedInstanceState == null) {
            showSignInOptions();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.myNavHostFragment);
        return NavigationUI.navigateUp(navController, drawerLayout);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //startActivity(new Intent(this, MainActivity.class));
                //finish();
                setUserName(user.getDisplayName());
                Toast.makeText(this, "Welcome " + user.getDisplayName() + " " + ("\ud83d\ude0a"), Toast.LENGTH_LONG).show();
                setUpNavHeader(user.getPhotoUrl(), user.getDisplayName(), user.getEmail());
            } else {
                Toast.makeText(this, "Error:" + response.getError().getMessage(), Toast.LENGTH_LONG).show();
            }
        }
//        if (resultCode == RESULT_OK && requestCode == IMAGE_REQUEST_CODE) {
//            uploadImageView.setImageURI(data.getData());
////            if (data != null) {
////                Intent tempuri = data;
////                if ("com.google.android.apps.photos.contentprovider".equals(tempuri.getAu()) ) {
////                    val pathUri: String = tempuri.getPath()!!
////                            val startindex = pathUri.indexOf("content")
////                    val lastindex = pathUri.lastIndex
////                    val newUri: String = pathUri.substring(startindex, lastindex)
////                    imageuri=newUri //data?.data!!
////                    Log.i("PostActivity",newUri)
////                }
//            Toast.makeText(this, "Photo added successfully", Toast.LENGTH_SHORT).show();
//        }
//        if ((data != null) && requestCode == IMAGE_REQUEST_CODE) {
//            resetButton.setVisibility(View.VISIBLE);
//            Uri photoUri = data.getData();
//
//            // Load the image located at photoUri into selectedImage
//            Bitmap selectedImage = loadFromUri(photoUri);
//
//            // Load the selected image into a preview
//            uploadImageView.setImageBitmap(selectedImage);
//        }
    }
    private void setUserName(String user){
        this.userName = user;
    }
    public static String getUserName(){
        return userName;
    }

    public Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            // check version of Android on device
            if (Build.VERSION.SDK_INT > 27) {
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
            }
        } catch (IOException err) {
            err.printStackTrace();
        }
        return image;
    }

    private void openImageIntent() {

        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_REQUEST_CODE);
        }
        //        Intent imageOpener = new Intent(Intent.ACTION_SEND);
//        imageOpener.setType("image/*");
//        if (imageOpener != null) {
//            startActivityForResult(imageOpener, IMAGE_REQUEST_CODE);
//        } else {
//            Toast.makeText(this, "Please allow photos permission", Toast.LENGTH_SHORT).show();
//        }
    }

    private void computeRecognition() {
    }

    private void setUpNavHeader(Uri photoUrl, String displayName, String email) {
        View hView = binding.navView.inflateHeaderView(R.layout.header_nav_drawer);
        ImageView imgView = (ImageView) hView.findViewById(R.id.imageViewAvatar);
        Picasso.get().load(photoUrl).into(imgView);
        TextView titleView = (TextView) hView.findViewById(R.id.header_title);
        TextView emailView = (TextView) hView.findViewById(R.id.email);
//        imgView .setImageResource(photoUrl);
        titleView.setText(displayName);
        emailView.setText(email);
    }

    private void showSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.FirebaseTheme)
                        .setLogo(firebase_icon)
                        .build(), LOGIN_REQUEST_CODE
        );
    }

    private void showLogoutOptions() {
        AuthUI.getInstance()
                .signOut(MainActivity.this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
//                        startActivityForResult(
//                                AuthUI.getInstance().createSignInIntentBuilder()
//                                        .setAvailableProviders(providers)
//                                        .setTheme(R.style.FirebaseTheme)
//                                        .setLogo(firebase_icon)
//                                        .build(), LOGIN_REQUEST_CODE
//                        );
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}