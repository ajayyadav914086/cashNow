package com.taxerts.scratch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.Random;

import in.myinnos.androidscratchcard.ScratchCard;

public class Scratch extends AppCompatActivity {

    String Currency;
    Button button;
    TextView cardCost;
    TextView cardPrice;
    String cardType;
    ImageView cardTypeimage;
    TextView cardTypetext;
    CardView cardView;
    String[] carnival_price = {"10", "10", "10", "10", "10", "10", "20", "25", "25", "25", "30", "35", "40", "45", "50"};
    int cashInt;
    String[] cheers_price = {"10", "10", "10", "10", "20", "22", "28", "30"};
    String databCash;
    SharedPreferences.Editor editor;
    public FirebaseAnalytics firebaseAnalytics;
    int length;
    public FirebaseAuth mAuth;
    public DatabaseReference mDatabase;
    ScratchCard mScratchCard;
    String[] party_price = {"7", "7", "7", "9", "12", "15", "16", "18", "20"};
    int randcash;
    String randomCash;
    TextView scratchinvite;
    SharedPreferences sharedPreferences;
    String[] welcome_price = {"3", "3", "3", "7", "8", "9", "10"};
    TextView wintext;
    int woncash;
    int welcomeCardCost = 5;
    int partyCardCost = 10;
    int cheersCardCost = 15;
    int carnivalCardCost = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch);
        getSupportActionBar().hide();
        SharedPreferences sharedPreferences2 = getSharedPreferences("USER", 0);
        sharedPreferences = sharedPreferences2;
        editor = sharedPreferences2.edit();
        mDatabase = FirebaseDatabase.getInstance().getReference("user");
        mAuth = FirebaseAuth.getInstance();
        cardView = findViewById(R.id.main_scratch_cardview);
        cardPrice = findViewById(R.id.main_card_price);
        cardView.setBackgroundResource(getIntent().getIntExtra("color", 0));
        cardType = getIntent().getStringExtra("card_type");
        cardTypetext = findViewById(R.id.card_type_text);
        cardTypetext.setText(cardType + " Card");
        cardTypeimage = findViewById(R.id.card_type_image);
        Picasso.get().load(getIntent().getIntExtra("card_img", 0)).into(cardTypeimage);
        wintext = findViewById(R.id.win_text);
        mScratchCard = findViewById(R.id.SCard);
        mScratchCard.setOnScratchListener((scratchCard, visiblePercent) -> {
            if (visiblePercent > 0.1) {
                mScratchCard.setVisibility(View.GONE);
                Winalert();
                saveToDatabase();
            }
        });
        checkCards(cardType);

    }

    private void saveToDatabase() {
        Log.d("CASH DATABASE", String.valueOf(woncash));
        long cash = sharedPreferences.getLong("cash", 0);
        long finalCash = cash + woncash;
        mDatabase.child(mAuth.getUid()).child("Cash").setValue(finalCash);
        editor.putLong("cash", finalCash);
        editor.apply();
    }

    private void checkCards(String cardType) {
        if (Objects.equals(cardType, "welcome")) {
            welcomeCard();
        } else if (Objects.equals(cardType, "party")) {
            partyCard();
        } else if (Objects.equals(cardType, "cheers")) {
            cheersCard();
        } else if (Objects.equals(cardType, "carnival")) {
            carnivalCard();
        }
    }

    private void carnivalCard() {
        cardPrice.setText("Win price upto $0-10");
        randomCash = random(carnival_price);
        woncash = Integer.parseInt(randomCash) - carnivalCardCost;
        if (woncash < 0) {
            wintext.setText("You lost $" + Math.abs(woncash));
        } else {
            wintext.setText("You won $" + woncash);
        }
    }

    private void cheersCard() {
        cardPrice.setText("Win price upto $0-20");
        randomCash = random(cheers_price);
        woncash = Integer.parseInt(randomCash) - cheersCardCost;
        if (woncash < 0) {
            wintext.setText("You lost $" + Math.abs(woncash));
        } else {
            wintext.setText("You won $" + woncash);
        }
    }

    private void partyCard() {
        cardPrice.setText("Win price upto $0-30");
        randomCash = random(party_price);
        woncash = Integer.parseInt(randomCash) - partyCardCost;
        if (woncash < 0) {
            wintext.setText("You lost $" + Math.abs(woncash));
        } else {
            wintext.setText("You won $" + woncash);
        }
    }

    private void welcomeCard() {
        cardPrice.setText("Win price upto $0-50");
        randomCash = random(welcome_price);
        woncash = Integer.parseInt(randomCash) - welcomeCardCost;
        if (woncash < 0) {
            wintext.setText("You lost $" + Math.abs(woncash));
        } else {
            wintext.setText("You won $" + woncash);
        }
    }

    public void Winalert() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.mainwon);
        TextView textView = (TextView) dialog.findViewById(R.id.wintext);
        if (woncash > 0) {
            textView.setText("You won $" + woncash);
        } else {
            textView.setText("You lost $" + Math.abs(woncash));
        }
        dialog.findViewById(R.id.more_card_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public String random(String[] prices) {
        int rand = new Random().nextInt(prices.length);
        return prices[rand];
    }
}