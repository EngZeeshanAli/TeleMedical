package com.example.telemedical.adapter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import java.util.Date;

public class MyFileAdapter extends RecyclerView.Adapter<MyFileAdapter.item> {
    private StorageReference mStorageRef;
    ArrayList<ReportFormatter> list;
    Context c;
    String uid, openlocation;

    public MyFileAdapter(ArrayList<ReportFormatter> list, Context c, String uid) {
        this.list = list;
        this.c = c;
        this.uid = uid;
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
        String[] separated = formatter.getName().split(",");
        long date = Long.parseLong(separated[1]);
        // or you already have long value of date, use this instead of milliseconds variable.
        String d = DateFormat.format("MM/dd/yyyy", new Date(date)).toString();
        holder.fileName.setText(separated[0]);
        holder.nameDate.setText(formatter.getSharedWith() + " (" + d.toString() + ")");
        final String download = formatter.getDownloadUrl();
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(c, "Downaloading Started!", Toast.LENGTH_SHORT).show();
                StorageReference mStorageRef;
                mStorageRef = FirebaseStorage.getInstance().getReference(Constants.UPLOAD_FILE + uid + "/").child(download);
                try {
                    File first = new File(Environment.getExternalStorageDirectory(), "TeleMedical");
                    if (!first.exists()) {
                        first.mkdir();
                    }
                    File dir = new File(first, "Repots");
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                    File directory = new File(dir, formatter.getName() + ".pdf");
                    File localFile = File.createTempFile(download, "pdf");
                    openlocation = formatter.getName() + ".pdf";


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
        TextView fileName, nameDate;

        public item(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.pdf_file);
            fileName = itemView.findViewById(R.id.fileName);
            nameDate = itemView.findViewById(R.id.nameDate);
        }
    }

    private void addNotification(File item) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(c)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Download Complete!")
                        .setContentText("Store to Telemedical folder in mobile storage.");
        // Add as notification
        NotificationManager manager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(c.getApplicationContext(), notification);
        r.play();
    }

}

