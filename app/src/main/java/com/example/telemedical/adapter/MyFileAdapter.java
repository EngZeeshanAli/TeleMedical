package com.example.telemedical.adapter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedical.ConstantsUsage.Constants;
import com.example.telemedical.Formaters.ReportFormatter;
import com.example.telemedical.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MyFileAdapter extends RecyclerView.Adapter<MyFileAdapter.item>  {
    private StorageReference mStorageRef;
    ArrayList<ReportFormatter> list;
    Context c;
    String uid,openlocation;

    public MyFileAdapter(ArrayList<ReportFormatter> list, Context c,String uid) {
        this.list = list;
        this.c = c;
        this.uid=uid;
    }

    @NonNull
    @Override
    public item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.my_file, parent, false);
        return new item(view);
    }


    @Override
    public void onBindViewHolder(@NonNull item holder, int position) {
        ReportFormatter formatter = list.get(position);
        final String download = formatter.getDownloadUrl();


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(c, "Downaloading Started!", Toast.LENGTH_SHORT).show();
                StorageReference mStorageRef;
                mStorageRef = FirebaseStorage.getInstance().getReference(Constants.UPLOAD_FILE+uid+"/").child(download);
                try {
                    File dir=new File(Environment.getExternalStorageDirectory(),"TeleMedical"+File.separator +"reports");
                    if (!dir.exists()){
                        dir.mkdir();
                    }
                    File directory = new File(dir,formatter.getName()+".pdf");

                    File localFile = File.createTempFile(download, "pdf");
                    openlocation = formatter.getName()+".pdf";


                    mStorageRef.getFile(directory)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    // Successfully downloaded data to local file
                                    Toast.makeText(c, "Downloaded", Toast.LENGTH_SHORT).show();
                                    addNotification(directory);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle failed download
                            Toast.makeText(c, exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class item extends RecyclerView.ViewHolder {
        LinearLayout layout;

        public item(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.pdf_file);
        }
    }

    private void addNotification(File item) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(c)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Download Complete!")
                        .setContentText("Store to Telemedical folder in mobile storage.");
//        Intent intent = new Intent();
//        intent.setAction(android.content.Intent.ACTION_VIEW);
//        intent.setType("application/pdf");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Add as notification
        NotificationManager manager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(c.getApplicationContext(), notification);
        r.play();
    }

}

