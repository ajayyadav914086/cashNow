package com.taxerts.scratch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReferEnter extends AppCompatActivity {
    String Country = "none";
    String Currency = "$";
    String ReferUser;
    String UserId;
    String capture_refer;
    SharedPreferences.Editor editor;
    String email;
    long invites = 0;
    private FirebaseAuth mAuth;
    public DatabaseReference mDatabase;
    String name;
    Button next;
    String photoUrl;
    public DatabaseReference rDatabase;
    EditText refer_edit;
    SharedPreferences sharedPreferences;
    Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_enter);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("user");
        rDatabase = FirebaseDatabase.getInstance().getReference("refer");
        SharedPreferences sharedPreferences2 = getSharedPreferences("USER", 0);
        sharedPreferences = sharedPreferences2;
        editor = sharedPreferences2.edit();
        email = sharedPreferences.getString("useremail", null);
        name = sharedPreferences.getString("username", null);
        photoUrl = sharedPreferences.getString("userimg", null);
        UserId = mAuth.getUid();
        refer_edit = findViewById(R.id.refereditText);
        next = findViewById(R.id.next_button);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                capture_refer = refer_edit.getText().toString();
                if (capture_refer.isEmpty()) {
                    users = new Users(email, name, photoUrl, Country, Currency, 50, invites);
                    mDatabase.child(UserId).setValue(users);
                    editor.putString(FirebaseAnalytics.Param.CURRENCY, ReferEnter.this.Currency);
                    editor.putLong("cash", 50);
                    editor.apply();
                    Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                    intent.putExtra("empty", true);
                    startActivity(intent);
                    finish();
                    return;
                }
                rDatabase.child(capture_refer).addValueEventListener(new ValueEventListener() {
                    public void onCancelled(DatabaseError databaseError) {
                    }

                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            users = new Users(ReferEnter.this.email, ReferEnter.this.name, ReferEnter.this.photoUrl, ReferEnter.this.Country, ReferEnter.this.Currency, 60, ReferEnter.this.invites);
                            ReferUser = dataSnapshot.getValue().toString();
                            mDatabase.child(ReferEnter.this.UserId).setValue(ReferEnter.this.users);
                            editor.putString(FirebaseAnalytics.Param.CURRENCY, ReferEnter.this.Currency);
                            editor.putLong("cash", 60);
                            editor.apply();
                            Intent intent = new Intent(getApplication(), MainActivity.class);
                            intent.putExtra("referuser", ReferUser);
                            ReferEnter.this.startActivity(intent);
                            ReferEnter.this.finish();
                            return;
                        }
                        Toast.makeText(ReferEnter.this, "Wrong Refer code,try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}