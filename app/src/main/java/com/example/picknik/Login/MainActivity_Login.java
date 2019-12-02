package com.example.picknik.Login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.picknik.MainActivity;
import com.example.picknik.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity_Login extends AppCompatActivity {

    private  Button btnSingUp,btnSingIn,btnAnonymous;
    private ProgressBar mProgressbar;

    private FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();

        btnSingIn = findViewById(R.id.btnSingIn);
        btnSingUp = findViewById(R.id.btnSingUp);
        btnAnonymous = findViewById(R.id.btn_anonymous);
        mProgressbar = findViewById(R.id.progress_bar);


        btnSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
            }
        });

        btnSingUp.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
              startActivity(intent);

          }
      });

        btnAnonymous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserAnonymous();
            }
        });
    }


    private void getUserAnonymous(){

        mProgressbar.setVisibility(View.VISIBLE);

        //authenticate user
        mFirebaseAuth.signInAnonymously()
                .addOnCompleteListener(MainActivity_Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mProgressbar.setVisibility(View.GONE);
                        if (!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Authentication Failed." +task.getException(), Toast.LENGTH_SHORT).show();
                        }else {

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    }
                });
    }
}
