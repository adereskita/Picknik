package com.example.picknik.Login;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.picknik.MainActivity;
import com.example.picknik.R;
import com.example.picknik.Utils.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    //vars
        private Button btnSingUp;
        private EditText mName, mEmail,mPassword, mPassword2;
        private ProgressBar mProgressBar;

        //db
        private DatabaseReference dbUser;
        private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        dbUser = FirebaseDatabase.getInstance().getReference("user");
        mFirebaseAuth = FirebaseAuth.getInstance();

        mProgressBar = findViewById(R.id.progress_bar);
        mName = findViewById(R.id.edtNama);
        mEmail = findViewById(R.id.edtEmail);
        mPassword = findViewById(R.id.edtPassword);
        mPassword2 = findViewById(R.id.edtPassword2);

        btnSingUp = findViewById(R.id.btnSingUp);


        btnSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();

            }
        });
    }

    private void addUser(){
        final String name = mName.getText().toString().trim();
        final String email = mEmail.getText().toString().trim();
        final String passwd = mPassword.getText().toString().trim();
        final String passwd2 = mPassword2.getText().toString().trim();


        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(passwd) && !TextUtils.isEmpty(passwd2)){

            if (passwd.equals(passwd2)){

            }else{
                Toast.makeText(this,"Password is Wrong.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (passwd.length() < 6){
                Toast.makeText(getApplicationContext(),"Password is too short.", Toast.LENGTH_SHORT).show();
            }

            mProgressBar.setVisibility(View.VISIBLE);

            mFirebaseAuth.createUserWithEmailAndPassword(email,passwd)
                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mProgressBar.setVisibility(View.GONE);
                            if (!task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Authentication Failed." +task.getException(), Toast.LENGTH_SHORT).show();
                            }else {

                                FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

                                //create user tabel Real-Time

                                // before id = dbUser.push().getKey();

                                String id = mUser.getUid();

                                User user = new User(id, name, email, passwd);

                                dbUser.child(id).setValue(user);

                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }
                        }
                    });

        }else {
            Toast.makeText(this, "Please, input your data first.",Toast.LENGTH_SHORT).show();
            return;
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        mProgressBar.setVisibility(View.GONE);
    }
}
