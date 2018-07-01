package com.codeislife.joun;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    Button Register;
    EditText Username, passwordreg, emailreg;
    ProgressBar progressBar;
    FirebaseAuth auth;
    TextView ToSignIn;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

    Register = (Button)findViewById(R.id.btnSignin);
    Username = (EditText)findViewById(R.id.UserName);
    passwordreg = (EditText)findViewById(R.id.SignupPass);
    emailreg = (EditText)findViewById(R.id.Email);
    ToSignIn = (TextView)findViewById(R.id.SigninTextView);

        mProgress = new ProgressDialog(this);

    ToSignIn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(Register.this, Login.class));
        }
    });

    Register.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String name = Username.getText().toString();
            String email = emailreg.getText().toString();
            String password = passwordreg.getText().toString();



            if(TextUtils.isEmpty(name)){

                Toast.makeText(getApplicationContext(),"Enter Your Name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(email)){
                Toast.makeText(getApplicationContext(), "Enter your Email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)){
                Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                return;
            }

            mProgress.setMessage("Signing In.....");
            mProgress.show();

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                Toast.makeText(Register.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                    if (!task.isSuccessful()){

                        Toast.makeText(Register.this, "Authentication Failed" + task.getException(),Toast.LENGTH_SHORT).show();

                        mProgress.setMessage("Signing In.....");
                        mProgress.show();
                    }else {
                        mProgress.dismiss();
                        startActivity(new Intent(new Intent(Register.this, MainActivity.class)));

                        finish();
                    }
                }
            });
        }
    });

    }


}
