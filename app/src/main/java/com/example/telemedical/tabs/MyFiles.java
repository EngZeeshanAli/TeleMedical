package com.example.telemedical.tabs;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedical.ConstantsUsage.Constants;
import com.example.telemedical.Formaters.ReportFormatter;
import com.example.telemedical.R;
import com.example.telemedical.adapter.MyFileAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;


public class MyFiles extends Fragment implements View.OnClickListener {
    RecyclerView myFile;
    private Button upload;
    private static int REQUEST_CODE = 100;
    private static int PDF_CODE = 86;
    private static final int PICK_IMAGE_GALERY = 100;
    private static final int PICK_IMAGE_CAMERA = 200;
    Uri pdfUri;
    private StorageReference mStorageRef;
    StorageReference mStorageReference;
    FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    ArrayList list;
    ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_myfiles, container, false);
        initUi(view);
        list = new ArrayList<>();
        getStarted();
        myFile = view.findViewById(R.id.myfiles);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        myFile.setLayoutManager(horizontalLayoutManagaer);
        MyFileAdapter adapter = new MyFileAdapter(list, getActivity(), currentUser.getUid());
        adapter.setHasStableIds(true);
        myFile.setAdapter(adapter);


        return view;
    }

    void initUi(View v) {
        upload = v.findViewById(R.id.uploadFile);
        upload.setOnClickListener(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        checkPermision();
    }

    void getStarted() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.REPORTS);
        myRef.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    ReportFormatter formatter = datas.getValue(ReportFormatter.class);
                    list.add(formatter);
                }
                myFile.setAdapter(new MyFileAdapter(list, getActivity(), currentUser.getUid()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    void checkPermision() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }

    }

    private void selectPdf() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PDF_CODE);
    }


    void setUpload(Uri file, String filenamer) {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Uploading File....");
        dialog.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String filename = filenamer + "|" + System.currentTimeMillis();
        StorageReference imagesRef = storageRef.child(Constants.UPLOAD_FILE + currentUser.getUid() + "/" + filename);
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
                String downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                savingFile(currentUser.getUid(), filename, "Dr. Ahmad", filename);
            }
        });
    }


    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getContext());
        pictureDialog.setTitle("Select Action");
        pictureDialog.setCancelable(true);
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera",
                "Select PDF file"};
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
                            case 2:
                                selectPdf();
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
        intent.putExtra("android.intent.extras.CAMERA_FACING", 0);
        startActivityForResult(intent, PICK_IMAGE_CAMERA);
    }


    Uri createPdf(Bitmap bitmap) {


        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pi = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pi);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        canvas.drawPaint(paint);
        paint.setColor(Color.parseColor("#FFFFFF"));
        canvas.drawPaint(paint);
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);
        File dir = new File(Environment.getExternalStorageDirectory(), "TeleMedical" + File.separator + "temp");
        if (!dir.exists()) {
            dir.mkdir();
        }
        File directory = new File(dir, "temp.pdf");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(directory);
            document.writeTo(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        document.close();
        return Uri.fromFile(directory);
    }

    private void savingFile(String uid, String name, String shared, String download) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child(Constants.REPORTS);
        ReportFormatter formatter = new ReportFormatter(uid, name, shared, download);
        myRef.child(uid).child(String.valueOf(System.currentTimeMillis())).setValue(formatter, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PDF_CODE && resultCode == Activity.RESULT_OK && data != null) {
            pdfUri = Uri.fromFile(new File(data.getData().getPath()));
            Toast.makeText(getActivity(), pdfUri.toString(), Toast.LENGTH_SHORT).show();
            setUpload(data.getData(), getFileName(pdfUri));
        }

        if (requestCode == PICK_IMAGE_CAMERA && resultCode == Activity.RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            setUpload(createPdf(bitmap), getFileName(createPdf(bitmap)));
        }
        if (requestCode == PICK_IMAGE_GALERY && resultCode == Activity.RESULT_OK && data != null) {
            Toast.makeText(getContext(), "galary", Toast.LENGTH_SHORT).show();
            Uri imageUri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            setUpload(createPdf(bitmap), getFileName(createPdf(bitmap)));

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectPdf();
        }

    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uploadFile:
                showPictureDialog();
                break;
        }
    }


}
