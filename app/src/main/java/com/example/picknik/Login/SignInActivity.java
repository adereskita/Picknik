package com.example.picknik.Login;

import android.content.Intent;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";

    //vars
    private Button btnSignIn;
    private EditText mEmail,mPassword;
    private ProgressBar mProgressBar;

    //db
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mFirebaseAuth = FirebaseAuth.getInstance();

//        if (mFirebaseAuth.getCurrentUser().isAnonymous() || mFirebaseAuth.getCurrentUser() == null){
//
//        }
//        if (mFirebaseAuth.getCurrentUser().isEmailVerified()){
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            finish();
//        }

        mEmail = findViewById(R.id.edtEmail);
        mPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnSingIn);

        mProgressBar = findViewById(R.id.progress_bar);



        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUser();
            }
        });
    }



    private void getUser() {
        String email = mEmail.getText().toString().trim();
        final String passwd = mPassword.getText().toString().trim();


        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(passwd)) {

            mProgressBar.setVisibility(View.VISIBLE);

            //authenticate user
            mFirebaseAuth.signInWithEmailAndPassword(email, passwd)
                    .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mProgressBar.setVisibility(View.GONE);
                            if (!task.isSuccessful()) {
                                if (passwd.length() < 6) {
                                    mPassword.setError("Password is too Short");
                                }
                                Toast.makeText(getApplicationContext(), "Authentication Failed." + task.getException(), Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }
                        }
                    });
        }else {
            Toast.makeText(this, "Make Sure you fill the data.", Toast.LENGTH_SHORT).show();
        }
    }
}

