package com.example.picknik.Profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.picknik.Adaptor.UploadListAdapter;
import com.example.picknik.R;
import com.example.picknik.Utils.PostModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
//import com.mapbox.mapboxandroiddemo.R;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddPlaceActivity extends AppCompatActivity {

    //Gallery select
    public static final int RESULT_LOAD_IMAGE = 1;
    //mapbox
    private static final int PLACE_SELECTION_REQUEST_CODE = 56789;
    //vars
    private RecyclerView mRecyclerviewFile;
    private UploadListAdapter adapter;
    private Button mChooseFile, btn_PictMenu, mSubmit;
    private List<String> fileNameList, fileDoneList;

    private String fileName;
    private TextView type_jpg;
    private RadioButton rbMenu, rbPlace;
    private RadioGroup rgType, rgOwner, rgImgType;
    private String imgId, txt_imgType, imgEvent,postId,txt_type,txt_owner;
    private double lat;
    private double lng;


    //Uri
    private Uri mFilePath;

    //firebase
    private StorageReference mStorageRef, mFileToUpload;
    private DatabaseReference dbUser, dbPhotos;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        // Mapbox Access token
        Mapbox.getInstance(getApplicationContext(),
                getString(R.string.access_token));

        //findView Form
        nPicknikName = findViewById(R.id.edt_pName);
        phone = findViewById(R.id.edt_pPhone);
        location = findViewById(R.id.edt_location);
        sCategory = findViewById(R.id.type_spinner);
        avgCost = findViewById(R.id.edt_cost);
        description = findViewById(R.id.edt_description);
        type_jpg = findViewById(R.id.type_jpg);
        rbMenu = findViewById(R.id.rb_menu);
        rbPlace = findViewById(R.id.rb_place);
        edt_open = findViewById(R.id.time_open);
        edt_close = findViewById(R.id.time_close);


        //here time picker
        edt_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);


                TimePickerDialog timePickerDialog = new TimePickerDialog(AddPlaceActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        String amPm;
                        if (hourOfDay >= 12) {
                            amPm = "pm";
                        } else {
                            amPm = "am";
                        }

                        edt_open.setText(String.format("%02d:%02d", hourOfDay, minutes)+ amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });

        edt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddPlaceActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        String amPm;
                        if (hourOfDay >= 12) {
                            amPm = "pm";
                        } else {
                            amPm = "am";
                        }
                        edt_close.setText(String.format("%02d:%02d", hourOfDay, minutes)+ amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();

            }
        });




        rgType = findViewById(R.id.rbGroup_type);
        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //radio type
                int rb_typeId =rgType.getCheckedRadioButtonId();
                type =  rgType.findViewById(rb_typeId);
                txt_type = type.getText().toString();
                if (txt_type.equals("Place")){
                    rgImgType.setVisibility(View.VISIBLE);
                    rbMenu.setVisibility(View.VISIBLE);
                    rbPlace.setVisibility(View.VISIBLE);
                    btn_PictMenu.setVisibility(View.VISIBLE);
                    type_jpg.setVisibility(View.VISIBLE);
                }else {
                    rgImgType.setVisibility(View.INVISIBLE);
                    rbMenu.setVisibility(View.INVISIBLE);
                    rbPlace.setVisibility(View.INVISIBLE);
                    btn_PictMenu.setVisibility(View.GONE);
                    type_jpg.setVisibility(View.INVISIBLE);

                }

            }
        });


        rgOwner = findViewById(R.id.rbGroup_Owner);
        rgOwner.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //radio owner
                int rb_ownerId = rgOwner.getCheckedRadioButtonId();
                owner = rgOwner.findViewById(rb_ownerId);
                txt_owner = owner.getText().toString();
            }
        });


        rgImgType = findViewById(R.id.rbGroup_imgType);
        rgImgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //radio image type whether for MENU or PLACE
                int rb_imgType = rgImgType.getCheckedRadioButtonId();
                imgType = rgImgType.findViewById(rb_imgType);
                txt_imgType = imgType.getText().toString();
            }
        });




        //User firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        mUser = mFirebaseAuth.getCurrentUser();


        postId = FirebaseDatabase.getInstance().getReference().push().getKey();
        dbUser = FirebaseDatabase.getInstance().getReference("user")
        .child("picknik-post");



        //storage init
        mStorageRef = FirebaseStorage.getInstance().getReference();


        //arraylist and RecyclerView
        recylerPhotosData();
        //Spinner
        setupSpinner();



        //button choose Photos intent to Gallery for PLACE
        mChooseFile = findViewById(R.id.btn_ChoosePict);
        mChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Launching the Intent
                startActivityForResult(intent,RESULT_LOAD_IMAGE);
            }
        });

        //button choose Photos intent to Gallery for MENU
        btn_PictMenu = findViewById(R.id.btn_chooseMenu);
        btn_PictMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Launching the Intent
                startActivityForResult(intent,RESULT_LOAD_IMAGE);
            }
        });


        //button Submit Data
        mSubmit = findViewById(R.id.btn_submitData);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhotos();
                addDataPicknik();
            }
        });



        //button back
        ImageButton backButton = findViewById(R.id.btn_back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddPlaceActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        Button btn_map = findViewById(R.id.btn_map);
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mapbox.setAccessToken(getString(R.string.access_token));

                Intent intent = new PlacePicker.IntentBuilder()
                        .accessToken(Mapbox.getAccessToken())
                        .placeOptions(PlacePickerOptions.builder()
                                .statingCameraPosition(new CameraPosition.Builder()
                                                        .target(new LatLng(-5.165252,106.6459459))
                                                        .zoom(6)
                                                        .build())
                                        .build())
                        .build(AddPlaceActivity.this);
                startActivityForResult(intent, PLACE_SELECTION_REQUEST_CODE);

            }
        });
    }


    // HERE END OF ONCREATE //



    //SPINNER
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
            ArrayAdapter PlaceSpinner = ArrayAdapter.createFromResource(this,
                    R.array.category_place, android.R.layout.simple_spinner_item);
            // Specify dropdown layout style - simple list view with 1 item per line
            PlaceSpinner.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

            // Apply the adapter to the spinner
            sCategory.setAdapter(PlaceSpinner);

//        if (txt_type.equals("Event")){
//
//            ArrayAdapter EventSpinner = ArrayAdapter.createFromResource(this,
//                    R.array.category_event, android.R.layout.simple_spinner_item);
//            // Specify dropdown layout style - simple list view with 1 item per line
//            EventSpinner.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
//
//            // Apply the adapter to the spinner
//            sCategory.setAdapter(EventSpinner);
//        }



        // Set the integer mSelected to the constant values
        sCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
//                    if (selection.equals(getString(R.string.gender_male))) {
//                        mGender = 1; // Male
//                    } else if (selection.equals(getString(R.string.gender_female))) {
//                        mGender = 2; // Female
//                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                 // Unknown
            }
        });
    }



    //upload data picture to firebase storage
    private void uploadPhotos(){

        if (mFilePath != null){

            final ProgressDialog mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setTitle("Submiting data..");
            mProgressDialog.show();


            fileName = getFileName(mFilePath);

            //Uploading the photos to Firebase Storage HERE
            mFileToUpload = mStorageRef.child("Images").child(fileName);

            mFileToUpload.putFile(mFilePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    finish();
                    Toast.makeText(getApplicationContext(),"Data is submitted",Toast.LENGTH_SHORT).show();
                }
            })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        mProgressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Failed upload file",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        //displaying percentage in progress dialog
                        mProgressDialog.setMessage("Uploading " + ((int) progress) + "%...");
                    }
                });
        }else {
            Toast.makeText(this,"an Error occure while uploading data.",Toast.LENGTH_SHORT).show();
        }
    }



    //Activity Intent for Gallery "for choose button" now Getting data
    @Override
    //before its protected
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {

            //MAPBOX PLACE PICKER
            if (requestCode == PLACE_SELECTION_REQUEST_CODE && resultCode == Activity.RESULT_OK){

                // Retrieve the information from the selected location's CarmenFeature

                CarmenFeature carmenFeature = PlacePicker.getPlace(data);
                //String json = carmenFeature.toJson();
                String address = carmenFeature.placeName();

                //List<Double> coordiante = carmenFeature.center().coordinates();
                lat = carmenFeature.center().latitude();
                lng = carmenFeature.center().longitude();

                location.setText(address);

            }


            //GALLERY PICKER
            else if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK) {

                postId = dbUser.push().getKey();
                //multiple upload
                if (data.getClipData() != null) {

                    if (rbMenu.isChecked()) {
                        //for menu photos
//                        dbPhotos = FirebaseDatabase.getInstance().getReference("user")
//                                .child(mUser.getUid()).child("picknik-post")
//                                .child(postId);

                        // fileName = "M-" + fileName;
                        //User user = new User(fileName);
                        //dbUser.setValue(user);

                        int totalItemSelected = data.getClipData().getItemCount();

                        for (int i = 0; i < totalItemSelected; i++) {

                            mFilePath = data.getClipData().getItemAt(i).getUri();

                            fileName = getFileName(mFilePath);

                            imgId = dbUser.push().getKey();

                            fileName = imgId;

                            fileName = "M" + imgId;
                            PostModel mPost = new PostModel(fileName);
                            dbUser.child("photos").child(postId).child(imgId).setValue(mPost);

                            fileNameList.add(fileName);
                            fileDoneList.add("uploading");
                            adapter.notifyDataSetChanged();


                            //Uploading the photos to Firebase Storage HERE
                            mFileToUpload = mStorageRef.child("Images").child(fileName);

                            final int finalI = i;
                            mFileToUpload.putFile(mFilePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileDoneList.remove(finalI);
                                    fileDoneList.add(finalI, "done");

                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                        //if
                    } else if (txt_type.equals("Place")) {
                        //for menu photos
                        // mUser = mFirebaseAuth.getCurrentUser();

                        // fileName = "M-" + fileName;
                        //User user = new User(fileName);
                        //dbUser.setValue(user);

                        int totalItemSelected = data.getClipData().getItemCount();

                        for (int i = 0; i < totalItemSelected; i++) {

                            mFilePath = data.getClipData().getItemAt(i).getUri();

                            fileName = getFileName(mFilePath);

                            imgId = dbUser.push().getKey();

                            fileName = imgId;

                            fileName = "P" + imgId;
                            PostModel mPost = new PostModel(fileName);
                            dbUser.child("photos").child(postId).child(imgId).setValue(mPost);

                            fileNameList.add(fileName);
                            fileDoneList.add("uploading");
                            adapter.notifyDataSetChanged();


                            //Uploading the photos to Firebase Storage HERE
                            mFileToUpload = mStorageRef.child("Images").child(fileName);

                            final int finalI = i;
                            mFileToUpload.putFile(mFilePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileDoneList.remove(finalI);
                                    fileDoneList.add(finalI, "done");

                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }

                    } else if (txt_type.equals("Event")) {

                        //mUser = mFirebaseAuth.getCurrentUser();

                        // fileName = "M-" + fileName;
                        //User user = new User(fileName);
                        //dbUser.setValue(user);

                        int totalItemSelected = data.getClipData().getItemCount();

                        for (int i = 0; i < totalItemSelected; i++) {

                            mFilePath = data.getClipData().getItemAt(i).getUri();

                            fileName = getFileName(mFilePath);

                            imgId = dbUser.push().getKey();

                            fileName = imgId;

                            fileName = "E" + imgId;
                            PostModel mPost = new PostModel(fileName);
                            dbUser.child(postId).child("photos").setValue(mPost);

                            fileNameList.add(fileName);
                            fileDoneList.add("uploading");
                            adapter.notifyDataSetChanged();


                            //Uploading the photos to Firebase Storage HERE
                            mFileToUpload = mStorageRef.child("Images").child(fileName);

                            final int finalI = i;
                            mFileToUpload.putFile(mFilePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileDoneList.remove(finalI);
                                    fileDoneList.add(finalI, "done");

                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }

                    }
                    //single upload
                } else if (data.getData() != null) {

                    mFilePath = data.getData();

                    try {

                        if (rbMenu.isChecked()){

                            imgId = dbUser.push().getKey();

                            fileName = getFileName(mFilePath);
                            fileName = imgId;

                            fileName = "M"+imgId;

                            PostModel mPost = new PostModel(fileName);
                            dbUser.child("photos").child(postId).child(imgId).setValue(mPost);

                            fileNameList.add(fileName);
                            fileDoneList.add("uploading");
                            adapter.notifyDataSetChanged();

                            //Uploading the photos to Firebase Storage HERE
                            mFileToUpload = mStorageRef.child("Images").child(fileName);

                            mFileToUpload.putFile(mFilePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileDoneList.remove(mFilePath);
                                    fileDoneList.add(1, "done");
                                    adapter.notifyDataSetChanged();
                                }
                            });

                        }else if (txt_type.equals("Place")){

                            imgId = dbUser.push().getKey();

                            fileName = getFileName(mFilePath);
                            fileName = imgId;

                            fileName = "P"+imgId;

                            PostModel mPost = new PostModel(fileName);
                            dbUser.child("photos").child(postId).child(imgId).setValue(mPost);

                            fileNameList.add(fileName);
                            fileDoneList.add("uploading");
                            adapter.notifyDataSetChanged();

                            //Uploading the photos to Firebase Storage HERE
                            mFileToUpload = mStorageRef.child("Images").child(fileName);

                            mFileToUpload.putFile(mFilePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileDoneList.remove(mFilePath);
                                    fileDoneList.add(1, "done");
                                    adapter.notifyDataSetChanged();
                                }
                            });

                        }else if (txt_type.equals("Event")){

                            imgId = dbUser.push().getKey();

                            fileName = getFileName(mFilePath);
                            fileName = imgId;

                            fileName = "E"+imgId;

                            PostModel mPost = new PostModel(fileName);
                            dbUser.child("photos").child(postId).child(imgId).setValue(mPost);

                            fileNameList.add(fileName);
                            fileDoneList.add("uploading");
                            adapter.notifyDataSetChanged();

                            //Uploading the photos to Firebase Storage HERE
                            mFileToUpload = mStorageRef.child("Images").child(fileName);

                            mFileToUpload.putFile(mFilePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileDoneList.remove(mFilePath);
                                    fileDoneList.add(1, "done");
                                    adapter.notifyDataSetChanged();
                                }
                            });

                        }

                    } catch (Exception e) {
                        e.getMessage();
                    }
                    //else if there is no chosen picture
                } else {
                    Toast.makeText(getApplicationContext(), "NO DATA", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    //getting photos name with Cursor
    private String getFileName(Uri uri){
        String result =  null;
        if (uri.getScheme().equals("content")){
            Cursor cursor = getContentResolver().query(uri, null,
                    null,
                    null,
                    null);
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


    private  void recylerPhotosData(){

        //arraylist and RecyclerView
        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();

        mRecyclerviewFile = findViewById(R.id.file_list);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL, false);
        mRecyclerviewFile.setLayoutManager(layoutManager);


        //make vertical adapter
        adapter = new UploadListAdapter(fileNameList,fileDoneList);
        mRecyclerviewFile.setAdapter(adapter);

        mRecyclerviewFile.setHasFixedSize(true);
        //Line divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerviewFile.getContext(),
                layoutManager.getOrientation());
        mRecyclerviewFile.addItemDecoration(dividerItemDecoration);
    }



    //vars for adding data to Firebase REAL-TIME

    private EditText nPicknikName,phone,location,
            avgCost,description,edt_open, edt_close;
    private RadioButton type,owner,imgType;
    private Spinner sCategory;


    private void addDataPicknik(){



        final String mName = nPicknikName.getText().toString().trim();
        final String mPhone = phone.getText().toString().trim();
        final String mLocation = location.getText().toString().trim();
        final String mType = txt_type;
        final String mCategory = sCategory.getSelectedItem().toString().trim();
        final String tOpen = edt_open.getText().toString().trim();
        final String tClose = edt_close.getText().toString().trim();
        final String mAvgCost = avgCost.getText().toString().trim();
        final String mOwner = txt_owner;
        String mfileName = imgId.trim();
        final String mDesc = description.getText().toString().trim();


        if (!TextUtils.isEmpty(mName) && !TextUtils.isEmpty(mPhone) && !TextUtils.isEmpty(mLocation)
                && !mType.isEmpty() && !TextUtils.isEmpty(mCategory)
                && !TextUtils.isEmpty(mAvgCost) && !TextUtils.isEmpty(mOwner)
                && !fileName.isEmpty() && !TextUtils.isEmpty(mType)){


            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

            //create user tabel Real-Time


            if (mUser != null) {
                //String id = mUser.getUid();
                //id-Posting
                //String id = dbUser.push().getKey();

                if (mType.equals("Place")) {


                    rbMenu = findViewById(R.id.rb_menu);

                   // if (mImgType.equals("Menu")) {
                        //for menu photos
                    //    mfileName = "M-" + mfileName;

                    PostModel mPost = new PostModel(mUser.getUid(), postId, mName, mPhone, mLocation
                            ,lat,lng, mType, mCategory,tOpen,tClose, mAvgCost, mOwner, imgId, mDesc);

                        dbUser.child(postId).setValue(mPost);


//                    }else {
                        //for Place photos
//                        mfileName = "P-"+mfileName;
//                        User user = new User(id, mName, mPhone, mLocation, mType, mCategory
//                                , mAvgCost, mfileName, mDesc);
//                        dbUser.child(id).setValue(user);
 //                   }


                // if type not place but Event
                }else {

                    //imgType.setVisibility(View.INVISIBLE);
                    //imgType.setText(null);

                    //fileName = "E-"+ fileName;

                    PostModel mPost = new PostModel(mUser.getUid(), postId, mName, mPhone, mLocation
                            ,lat,lng, mType, mCategory, tOpen, tClose, mAvgCost, mOwner,
                            imgId, mDesc);

                    dbUser.child(postId).setValue(mPost);

                }
                finish();
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }else
                Toast.makeText(this, "Please Login.", Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(this, "Please fill the form.",Toast.LENGTH_SHORT).show();
        }
    }

}
