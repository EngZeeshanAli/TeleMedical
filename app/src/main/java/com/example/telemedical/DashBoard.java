package com.example.telemedical;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.telemedical.ConstantsUsage.Constants;
import com.example.telemedical.Formaters.UserFormater;
import com.example.telemedical.controls.UiControls;
import com.example.telemedical.frag.AppointmentFrag;
import com.example.telemedical.frag.EhrFiles;
import com.example.telemedical.frag.FindDoctor;
import com.example.telemedical.frag.FragMedicalHistory;
import com.example.telemedical.frag.FragPaymentHistory;
import com.example.telemedical.frag.FragPrescripitions;
import com.example.telemedical.frag.HomeFrag;
import com.example.telemedical.frag.MessagingFrag;
import com.example.telemedical.frag.ProfileFrag;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    CircleImageView imageView;
    NavigationView navigationView;
    private static final int PICK_IMAGE_GALERY = 100;
    private static final int PICK_IMAGE_CAMERA = 200;
    ProgressDialog dialog;
    View headerView;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = findViewById(R.id.maintoolbar);
        setSupportActionBar(toolbar);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseMessaging.getInstance().subscribeToTopic("news");


        navigationView = findViewById(R.id.nav_view_draw);
        navigationView.setNavigationItemSelectedListener(this);
        BottomNavigationView view = findViewById(R.id.bottomNavigationView);
        view.setOnNavigationItemSelectedListener(this);
        drawer = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_draw, R.string.close_draw);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new HomeFrag()).commit();
        }
        initUi();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            }
        }

    }


    void initUi() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        headerView = navigationView.getHeaderView(0);
        imageView = headerView.findViewById(R.id.nav_profile_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child(Constants.USER_PROFILE_PIC).child(uid);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                getProfilePicture(imageView,value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        setProfile(uid);


    }

    void getProfilePicture(CircleImageView imageView, String value){

        Glide.with(this).load(value).into(imageView);

    }

    private void savingData(String uid,String downloader) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child(Constants.USER_PROFILE_PIC);
        myRef.child(uid).setValue(downloader, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                dialog.dismiss();
            }
        });
    }


    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        pictureDialog.setCancelable(true);
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_GALERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        startActivityForResult(intent, PICK_IMAGE_CAMERA);
    }


    void setUpload(Uri file, String filenamer) {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading File....");
        dialog.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String filename = filenamer;
        StorageReference imagesRef = storageRef.child(Constants.PROFILE_IMG + filename);
        UploadTask uploadTask = imagesRef.putFile(file);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                dialog.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the uri
                        savingData(uid,uri.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });





            }
        });

    }
    void setUpload(Bitmap bitmap,String fileName){
        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading File....");
        dialog.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        StorageReference imagesRef = storageRef.child(Constants.PROFILE_IMG + fileName);
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                dialog.dismiss();
                Toast.makeText(DashBoard.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the uri
                        savingData(uid,uri.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });

            }
        });


    }


    void setProfile(final String uid) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.SAVE_USER).child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot mainSnapshot) {
//                UserFormater userProfile = mainSnapshot.getValue(UserFormater.class);
//                String name=userProfile.getName();
//                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_draw);
//                View headerView = navigationView.getHeaderView(0);
//                TextView navUsername = (TextView) headerView.findViewById(R.id.user_drawer_name);
                //navUsername.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_IMAGE_GALERY:
                if (data != null) {
                    setUpload(data.getData(),uid);
                    Toast.makeText(this, data.getData().toString(), Toast.LENGTH_SHORT).show();
                }
                break;
            case PICK_IMAGE_CAMERA:
                if (data != null) {
                    Bitmap bitmap= (Bitmap) data.getExtras().get("data");
                    setUpload(bitmap,uid);
                    CircleImageView profile=headerView.findViewById(R.id.nav_profile_image);
                    profile.setImageBitmap(bitmap);
                }
                break;

        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressed();
                return;
            } else {
                Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show();
            }

            mBackPressed = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.findFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new FindDoctor()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.appointFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new AppointmentFrag()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.profileFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new ProfileFrag()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.ehrFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new EhrFiles()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.medicalFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new FragMedicalHistory()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.paymentFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new FragPaymentHistory()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.prescriptionFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new FragPrescripitions()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.signOut:
                drawer.closeDrawer(GravityCompat.START);
                FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                finish();
                startActivity(new Intent(DashBoard.this, SignIn.class));
                break;


            ////////////////////////////////////

            case R.id.homefrg:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new HomeFrag()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.docFrag:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new FindDoctor()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.appointfrag:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new AppointmentFrag()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;


            case R.id.profileFrag:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new ProfileFrag()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.messageFrag:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new MessagingFrag()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
        }


        return true;
    }

}
