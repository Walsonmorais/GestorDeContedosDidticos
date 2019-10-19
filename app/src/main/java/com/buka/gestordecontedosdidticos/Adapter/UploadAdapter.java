package com.buka.gestordecontedosdidticos.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buka.gestordecontedosdidticos.R;
import com.buka.gestordecontedosdidticos.models.Upload;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UploadAdapter extends RecyclerView.Adapter<UploadAdapter.UploadViewHolder> {

    public Context pContext;
    public List<Upload> pUploads;

    public UploadAdapter(Context pContext, List<Upload> pUploads) {
        this.pContext = pContext;
        this.pUploads = pUploads;
    }

    @NonNull
    @Override
    public UploadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(pContext).inflate(R.layout.item_home, parent, false);
        return new UploadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UploadViewHolder uploadViewHolder, int position) {

        Upload upload = pUploads.get(position);


        uploadViewHolder.textViewTheme.setText(upload.getTheme());
        uploadViewHolder.textViewCourse.setText(upload.getCourse());
        uploadViewHolder.imageViewFile.setBackgroundResource(R.drawable.pdf_file);
        uploadViewHolder.textViewNotification.setText(upload.getFilesUrl());


    }

    @Override
    public int getItemCount() {
        return pUploads.size();
    }

    public class UploadViewHolder extends RecyclerView.ViewHolder {


        public TextView textViewTheme;
        public TextView textViewCourse;
        public TextView textViewUsername;
        public ImageView imageViewFile;
        public TextView textViewNotification;


        public UploadViewHolder(@NonNull View itemView) {
            super(itemView);


            textViewTheme = itemView.findViewById(R.id.textView_theme);
            textViewCourse = itemView.findViewById(R.id.textView_course);
            textViewUsername = itemView.findViewById(R.id.textView_username);
            imageViewFile = itemView.findViewById(R.id.imageView_item_post);
            textViewNotification = itemView.findViewById(R.id.text_notification_menu);

        }
    }
}
