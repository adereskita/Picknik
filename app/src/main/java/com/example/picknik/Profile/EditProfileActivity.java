package com.example.picknik.Profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.picknik.Login.MainActivity_Login;
import com.example.picknik.R;
import com.example.picknik.Utils.User;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProfileActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;

    //var
    private String avaId, fileName, userId;
    private Button editAvatar,saveData;
    private ImageView avatar;
    private EditText edtName,edtPhone,edtLocation,edtDescription;


    private Uri mFilePath;

    //firebase
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    private FirebaseDatabase mDatabase;
    private StorageReference mStorageRef, mFileToUpload;
    private DatabaseReference dbUser;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        //findId
        avatar = findViewById(R.id.iv_photo_profil);
        editAvatar = findViewById(R.id.btn_edit_photo);
        edtName = findViewById(R.id.et_name);
        edtPhone = findViewById(R.id.et_phone_number);
        edtLocation = findViewById(R.id.et_location);
        edtDescription = findViewById(R.id.et_description);

        //fireBase
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        dbUser = mDatabase.getReference();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();

        userId = user.getUid();


        //User firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        mUser = mFirebaseAuth.getCurrentUser();

        //get current user
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        mAuthStateListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser cUser = firebaseAuth.getCurrentUser();

                if (cUser == null){
                    startActivity(new Intent(EditProfileActivity.this, MainActivity_Login.class));
                    finish();
                }
            }
        };


        dbUser = FirebaseDatabase.getInstance().getReference("user").child(mUser.getUid());

        //storage init
        mStorageRef = FirebaseStorage.getInstance().getReference();


        ImageButton btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
            }
        });

        saveData = findViewById(R.id.btn_save);
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
                addData();
            }
        });


        editAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Launching the Intent
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);
            }
        });

            //update daata edit text
    }// End of OnCreate




    private void addData(){
    final String username = edtName.getText().toString().trim();
    final String phone = edtPhone.getText().toString().trim();
    final String location = edtLocation.getText().toString().trim();
    final String description = edtDescription.getText().toString().trim();

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(phone)
                && !TextUtils.isEmpty(location)){

            mUser = FirebaseAuth.getInstance().getCurrentUser();


            User user = new User(userId,username,phone,location,description,avaId);
            dbUser.setValue(user);


            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }else {
            Toast.makeText(getApplicationContext(), "Fill all the data.",Toast.LENGTH_SHORT)
                    .show();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null){



            mFilePath = data.getData();
            mUser = mFirebaseAuth.getCurrentUser();

                try{


                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mFilePath);
                    avatar.setImageBitmap(bitmap);

                    avaId = dbUser.push().getKey();

                    fileName = getFileName(mFilePath);
                    fileName = avaId;


                    User user = new User(avaId);
                    dbUser.setValue(user);

                }catch (Exception e){
                    e.getMessage();
                }

        }
    }


    //this method will upload the file
    private void uploadFile() {
        //if there is a file to upload
        if (mFilePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            fileName = getFileName(mFilePath);
            fileName = avaId;

            User user = new User(avaId);
            dbUser.setValue(user);

            //Uploading the photos to Firebase Storage HERE
            mFileToUpload = mStorageRef.child("Images").child(fileName);
            mFileToUpload.putFile(mFilePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }


    private String getFileName(Uri uri){
        String result =  null;
        if (uri.getScheme().equals("content")){
            Cursor cursor = getContentResolver().query(uri, null,null,null,null);
            try {
                if (cursor != null && cursor.moveToFirst()){
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }finally {
                assert cursor != null;
                cursor.close();
            }
        }
        if (result == null){
            result = uri.getPath();
            assert result != null;
            int cut = result.lastIndexOf('/');
            if (cut != -1){
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
