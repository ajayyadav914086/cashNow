package com.taxerts.scratch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class Login extends AppCompatActivity {
    SignInButton btSignIn;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth mAuth;
    private static final String TAG = "FIREBASE_AUTH";
    FirebaseUser cUser;
    SharedPreferences.Editor editor;
    long invites = 0;
    public DatabaseReference mDatabase;
    public DatabaseReference rDatabase;
    String refer_id;
    SharedPreferences sharedPreferences;
    String user_id;
    String user_image;
    Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btSignIn = findViewById(R.id.loginf);
        SharedPreferences sharedPreferences2 = getSharedPreferences("USER", 0);
        sharedPreferences = sharedPreferences2;
        this.editor = sharedPreferences2.edit();
        this.mDatabase = FirebaseDatabase.getInstance().getReference("user");
        this.rDatabase = FirebaseDatabase.getInstance().getReference("refer");
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("404354410372-2helvqa60sp422hoa89tmcegu9hulre1.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleSignInClient= GoogleSignIn.getClient(Login.this
                ,googleSignInOptions);
        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=googleSignInClient.getSignInIntent();
                // Start activity for result
                startActivityForResult(intent,100);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            try {
                firebaseAuth(GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException.class));
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failes", e);
            }
        }
    }

    private void firebaseAuth(GoogleSignInAccount result) {
        Log.d(TAG, "FirebaseAuthWithGoogle:" + result.getIdToken());
        AuthCredential authCredential= GoogleAuthProvider
                .getCredential(result.getIdToken()
                        ,null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "FirebaseAuthWithGoogle:" );
                if (task.isSuccessful()){
                    Log.d(Login.TAG, "signInWithCredential:success");
                    cUser = mAuth.getCurrentUser();
                    user_id = mAuth.getUid();
                    user_image = String.valueOf(cUser.getPhotoUrl());
                    refer_id = randomString();
                    users = new Users(cUser.getDisplayName(),user_image,cUser.getEmail(),invites);
                    if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                        editor.putString("useremail", cUser.getEmail());
                        editor.putString("username", cUser.getDisplayName());
                        editor.putString("userimg", user_image);
                        editor.putString("refer", refer_id);
                        editor.apply();
                        mDatabase.child(user_id).setValue(users);
                        rDatabase.child(refer_id).setValue(user_id);
                        startActivity(new Intent(Login.this, ReferEnter.class));
                        finish();
                        return;
                    }
                    Login.this.rDatabase.orderByValue().equalTo(Login.this.mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot key : dataSnapshot.getChildren()) {
                                String key2 = key.getKey();
                                Login.this.editor.putString("useremail", cUser.getEmail());
                                Login.this.editor.putString("username", cUser.getDisplayName());
                                Login.this.editor.putString("userimg", user_image);
                                Login.this.editor.putString("refer", key2);
                                Login.this.editor.apply();
                                Login.this.startActivity(new Intent(Login.this, MainActivity2.class));
                                Login.this.finish();
                            }
                        }

                        public void onCancelled(DatabaseError databaseError) {
                            Log.d(TAG,databaseError.getDetails());
                        }
                    });
                }
            }
        });
    }

    public String randomString() {
        char[] charArray = ("abcdefghijklmnopqrstuvwxyz".toUpperCase() + "0123456789").toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= 5; i++) {
            sb.append(charArray[new Random().nextInt(charArray.length)]);
        }
        return sb.toString();
    }
    public void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity2.class));
            finish();
        }
    }
}