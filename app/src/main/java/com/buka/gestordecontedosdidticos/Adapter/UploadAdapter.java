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

    public UploadAdapter(Context context, List<Upload> uploads) {

        pContext = context;
        pUploads = uploads;
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

        uploadViewHolder.textViewSubject.setText(upload.getSubject());
        uploadViewHolder.textViewTheme.setText(upload.getTheme());
        uploadViewHolder.textViewCourse.setText(upload.getCourse());
        uploadViewHolder.textViewYear.setText(upload.getYear());

        Picasso.get().load(upload.getImageUrl()).fit().into(uploadViewHolder.imageViewFile);

    }

    @Override
    public int getItemCount() {
        return pUploads.size();
    }

    public class UploadViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewSubject;
        public TextView textViewTheme;
        public TextView textViewCourse;
        public TextView textViewYear;
        public TextView textViewUsername;
        public ImageView imageViewFile;

        public UploadViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewSubject = itemView.findViewById(R.id.textView_subject);
            textViewTheme = itemView.findViewById(R.id.textView_theme);
            textViewCourse = itemView.findViewById(R.id.textView_course);
            textViewYear = itemView.findViewById(R.id.textView_year);
            textViewUsername = itemView.findViewById(R.id.textView_username);
            imageViewFile = itemView.findViewById(R.id.imageView_item_post);

        }
    }
}
