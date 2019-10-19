package com.buka.gestordecontedosdidticos;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.buka.gestordecontedosdidticos.models.Upload;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Activity_Add_Files extends AppCompatActivity {


    private static final int PICK_WORD_FILE = 1;
    private static final int PICK_PDF_FILE = 2;


    private StorageReference storageReference;
    private DatabaseReference databaseReference;



    private ImageView imageAdd;
    private EditText edit_subject, edit_theme, edit_course, edit_year;
    private TextView text_notification_pdf;
    private Button btn_upload;
    private ProgressBar progressBar_Add;
    private ProgressDialog pd;

    private Uri filesUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_files);



        imageAdd = findViewById(R.id.add_image);

        edit_subject = findViewById(R.id.edit_add_subject);
        edit_theme = findViewById(R.id.edit_add_theme);
        edit_course = findViewById(R.id.edit_add_course);
        edit_year = findViewById(R.id.edit_add_year);
        text_notification_pdf = findViewById(R.id.text_notification_pdf);

        btn_upload = findViewById(R.id.btn_upload);


        // progressBar_Add = findViewById(R.id.progress_add_image);//

        storageReference = FirebaseStorage.getInstance().getReference("Uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("Uploads");


        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String subjects = edit_subject.getText().toString().trim();
                final String themes = edit_theme.getText().toString().trim();
                final String courses = edit_course.getText().toString().trim();
                final String years = edit_year.getText().toString().trim();

                if (TextUtils.isEmpty(subjects)) {
                    Toast.makeText(Activity_Add_Files.this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(themes)) {
                    Toast.makeText(Activity_Add_Files.this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(courses)) {
                    Toast.makeText(Activity_Add_Files.this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(years)) {
                    Toast.makeText(Activity_Add_Files.this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
                } else {

                    uploadFile(subjects, themes, courses, years);

                }
            }
        });

    }

    private void showFilePickDialog() {

        String options[] = {"Ms Word Files", "Pdf Files"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select File");


        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {


                    selectWordFile();

                }
                if (which == 1) {

                    selectPdfFile();

                }

            }
        });
        builder.create().show();
    }



    private String getFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private void uploadFile(final String subjects, final String themes, final String courses, final String years) {


        pd = new ProgressDialog(Activity_Add_Files.this);
        pd.setMessage("Por favor Aguarde!");
        pd.show();

        if (filesUri != null) {

            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(filesUri));
            fileReference.putFile(filesUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    Upload upload = new Upload(subjects, courses, themes, years, taskSnapshot.getStorage().getDownloadUrl().toString());

                    String uploadId = databaseReference.push().getKey();
                    databaseReference.child(uploadId).setValue(upload);

                    pd.dismiss();
                    Toast.makeText(Activity_Add_Files.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Activity_Add_Files.this, Activity_Teacher_Menu.class));
                    finish();


                }
            }).addOnFailureListener(new OnFailureListener() {

                @Override
                public void onFailure(@NonNull Exception e) {

                    pd.dismiss();
                    Toast.makeText(Activity_Add_Files.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        } else {
            pd.dismiss();
            Toast.makeText(Activity_Add_Files.this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }


    private void selectWordFile() {


        Intent intentWord = new Intent();
        intentWord.setType("application/docx");
        intentWord.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentWord.createChooser(intentWord, "Select Ms Word File"), PICK_WORD_FILE);

    }

    private void selectPdfFile() {

        Intent intentPdf = new Intent();
        intentPdf.setType("application/pdf");
        intentPdf.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentPdf, PICK_PDF_FILE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (resultCode == RESULT_OK) {

            if (requestCode == PICK_WORD_FILE) {

                filesUri = data.getData();


                imageAdd.setBackgroundResource(R.drawable.ms_word);
                text_notification_pdf.setText("  ficheiro: " + filesUri.getLastPathSegment() + "  ");


            } else if (requestCode == PICK_PDF_FILE) {

                filesUri = data.getData();
                imageAdd.setBackgroundResource(R.drawable.pdf_file);
                text_notification_pdf.setText("  ficheiro: " + filesUri.getLastPathSegment() + "  ");


            }
        } else {

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main3, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_close) {

            Intent intent = new Intent(Activity_Add_Files.this, Activity_Teacher_Menu.class);
            startActivity(intent);
        } else if (id == R.id.action_files) {

            showFilePickDialog();

        }
        return super.onOptionsItemSelected(item);
    }


}