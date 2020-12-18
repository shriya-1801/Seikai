package com.example.my_app;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class SignUpActivity extends AppCompatActivity {
  private EditText emailEt,passwordEt1,passwordEt2,nameEt;
  private Button SignUpButton1;
  private FirebaseAuth firebaseAuth;
  @SuppressLint("WrongViewCast")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.signup);
    firebaseAuth=FirebaseAuth.getInstance();

    nameEt=findViewById(R.id.fullname);
    emailEt=findViewById(R.id.EmailAddress1);
    passwordEt1=findViewById(R.id.Password1);
    passwordEt2=findViewById(R.id.Password2);
    SignUpButton1=findViewById(R.id.signup1);
    SignUpButton1.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        SignUp();
      }
    });
  }
  private void SignUp(){
    String email=emailEt.getText().toString();
    final String fullname=nameEt.getText().toString();
    String password1=passwordEt1.getText().toString();
    String password2=passwordEt2.getText().toString();
    if (TextUtils.isEmpty(fullname)){
      nameEt.setError("Enter your full name");
      nameEt.requestFocus();
      return;
    }
    else if (TextUtils.isEmpty(email)){
      emailEt.setError("Enter your email");
      emailEt.requestFocus();
      return;
    }
    else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
      emailEt.setError("Please provide valid email");
      emailEt.requestFocus();
    }
    else if (TextUtils.isEmpty(password1)){
      passwordEt1.setError("Enter your password");
      passwordEt1.requestFocus();
      return;
    }
    else if (TextUtils.isEmpty(password2)){
      passwordEt2.setError("Confirm your password");
      passwordEt2.requestFocus();
      return;
    }
    else if (!password1.equals(password2)){
      passwordEt2.setError("Different password");
      passwordEt2.requestFocus();
      return;
    }
    else if (password1.length()<6){
      passwordEt1.setError("Length should be greater than 6");
      return;
    }

    firebaseAuth.createUserWithEmailAndPassword(email,password1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
      @Override
      public void onComplete(@NonNull Task<AuthResult> task) {
        if(task.isSuccessful()){
          DashboardActivity user= new DashboardActivity(fullname);
          //Intent intent=new Intent(SignUpActivity.this,DashboardActivity.class);
          FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              if(task.isSuccessful()){
                Toast.makeText(SignUpActivity.this,"Successfully registered",Toast.LENGTH_SHORT).show();
              }
            }
          });
          //startActivity(intent);
          //finish();
        }
        else {
          Toast.makeText(SignUpActivity.this,"Sign up fail !",Toast.LENGTH_LONG).show();

        }
      }
    });
  }

  }

