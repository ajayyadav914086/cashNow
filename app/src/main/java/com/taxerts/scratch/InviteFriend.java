package com.taxerts.scratch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

public class InviteFriend extends AppCompatActivity {
    String Currency;
    SharedPreferences.Editor editor;
    FirebaseAnalytics firebaseAnalytics;
    Button invite;
    TextView inviteinfo;
    TextView refer;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);
        getSupportActionBar().setTitle("Invite Friends");
        SharedPreferences sharedPreferences2 = getSharedPreferences("USER", 0);
        sharedPreferences = sharedPreferences2;
        editor = sharedPreferences2.edit();
        Currency = this.sharedPreferences.getString(FirebaseAnalytics.Param.CURRENCY, "$");
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        refer= findViewById(R.id.invite_code_text);
        refer.setText(this.sharedPreferences.getString("refer", null));
        invite = findViewById(R.id.friend_invite_button);
        inviteinfo = findViewById(R.id.invite_info);
        inviteinfo.setText("invite your friend and get " + this.Currency + " 10 when your friend join with your refer code below ");
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("INVITE BUTTON CLICK","INVITE BUTTON CLICK");
                Bundle bundle = new Bundle();
                bundle.putBoolean("invite_button", true);
                InviteFriend.this.firebaseAnalytics.logEvent("invite_button", bundle);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.putExtra("android.intent.extra.TEXT", "Hey Check this amazing app, you can make " + InviteFriend.this.Currency + " 500, you will get " + InviteFriend.this.Currency + " 60 Signup Bonus, Just use my code below" + InviteFriend.this.sharedPreferences.getString("refer", (String) null) + " https://play.google.com/store/apps/details?id=" + InviteFriend.this.getPackageName());
                intent.setType("text/plain");
                InviteFriend.this.startActivity(intent);
            }
        });
    }
}