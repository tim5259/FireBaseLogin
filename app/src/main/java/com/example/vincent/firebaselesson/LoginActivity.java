package com.example.vincent.firebaselesson;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String STATE = "onAuthStateChanged";
    private static final String AUTH = "auth";
    FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private String userUID;

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authListener);
    }

    public void login(View v) {
        final String email = ((EditText) findViewById(R.id.email)).getText().toString();
        final String password = ((EditText) findViewById(R.id.password)).getText().toString();
        Log.d(AUTH, email + "/" + password);

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.d(STATE, "登入失敗:");
                            register(email, password);

                        }

                    }
                });


    }


    //因register方法內，有匿名陣列用到email及password參數，因此需加上final
    private void register(final String email, final String password) {
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle("登入問題")
                .setMessage("無此帳號，是否要以此帳號與密碼註冊?")
                .setPositiveButton("註冊", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int witch) {
                        createUser(email, password);

                    }
                })
                .setNeutralButton("取消", null)
                .show();

    }

    private void createUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String message = task.isComplete() ? "註冊成功" : "註冊失敗";

                        new AlertDialog.Builder(LoginActivity.this)
                                .setMessage("Message")
                                .setPositiveButton("OK", null)
                                .show();

                    }
                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(STATE, "登入ID:" + user.getUid());
                    Log.d(STATE, "登入E-Mail:" + user.getEmail());
                    userUID = user.getUid();

                } else {
                    Log.d(STATE, "已登出");

                }

            }
        };


    }


}
