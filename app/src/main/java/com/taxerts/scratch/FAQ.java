package com.taxerts.scratch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FAQ extends AppCompatActivity {
    Button buttonHelp;
    TextView howearn;
    TextView howpaid;
    TextView whenpaid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        getSupportActionBar().hide();
        buttonHelp = findViewById(R.id.help);
        howearn = findViewById(R.id.how_earn);
        whenpaid = findViewById(R.id.when_paid);
        howpaid = findViewById(R.id.how_paid);
        TextView textView = howearn;
        textView.setText("There are two ways to earn money, 1) By inviting friends you will get $10 per invite. 2) By Scratching the Scratch cards.");
        TextView textView2 = whenpaid;
        textView2.setText("You will get paid when you will reached to the minimum withdrawal amount of $500");
        TextView textView3 = howpaid;
        textView3.setText("You will get paid through paypal when you reached $500");
        buttonHelp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.SENDTO", Uri.fromParts("mailto", "rohitrai680@gmail.com", (String) null));
                intent.putExtra("android.intent.extra.SUBJECT", "ScratchIT help");
                FAQ.this.startActivity(Intent.createChooser(intent, "Help"));
            }
        });
    }
}