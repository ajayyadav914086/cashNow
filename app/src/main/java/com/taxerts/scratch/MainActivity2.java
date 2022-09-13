package com.taxerts.scratch;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.taxerts.scratch.ui.dashboard.DashboardFragment;
import com.taxerts.scratch.ui.redeem.RedeemFragment;
import com.taxerts.scratch.ui.transactions.TransactionsFragment;

public class MainActivity2 extends AppCompatActivity implements DashboardFragment.OnFragmentInteractionListener, RedeemFragment.OnFragmentInteractionListener, TransactionsFragment.OnFragmentInteractionListener {
    String Currency;
    public String ReferUser;
    SharedPreferences.Editor editor;
    FirebaseAnalytics firebaseAnalytics;
    Boolean isEmpty;
    public FirebaseAuth mAuth;
    public DatabaseReference mDatabase;
    private InterstitialAd mInterstitialAd;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.navigation_transaction:
                    switchToTransaction();
                    return true;
                case R.id.navigation_dashboard:
                    switchToMain();
                    return true;
                case R.id.navigation_redeem:
                    switchToRedeem();
                    return true;
                default:
                    return false;
            }
        }
    };
    SharedPreferences sharedPreferences;
    long totalcash;

    public void onFragmentInteraction(Uri uri) {
    }

    public void onPointerCaptureChanged(boolean z) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getSupportActionBar().hide();
        MobileAds.initialize(MainActivity2.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).commit();
        firebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        mDatabase = FirebaseDatabase.getInstance().getReference("user");
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        this.ReferUser = intent.getStringExtra("referuser");
        this.isEmpty = intent.getBooleanExtra("empty", false);
        SharedPreferences sharedPreferences2 = getSharedPreferences("USER", 0);
        this.sharedPreferences = sharedPreferences2;
        this.Currency = sharedPreferences2.getString(FirebaseAnalytics.Param.CURRENCY, "$");
        this.editor = this.sharedPreferences.edit();
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }

                });
        String str = this.ReferUser;
        if (str != null) {
            this.mDatabase.child(str).child("Cash").addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(DatabaseError databaseError) {
                }

                public void onDataChange(DataSnapshot dataSnapshot) {
                    long longValue = (Long) dataSnapshot.getValue();
                    if (longValue > 480) {
                        totalcash = longValue;
                    } else {
                        totalcash = longValue + 10;
                    }
                    mDatabase.child(ReferUser).child("Cash").setValue(totalcash);
                }
            });
            this.mDatabase.child(this.ReferUser).child("Invites").addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(DatabaseError databaseError) {
                }

                public void onDataChange(DataSnapshot dataSnapshot) {
                    mDatabase.child(ReferUser).child("Invites").setValue((Long) dataSnapshot.getValue() + 1);
                }
            });
            customDialog(true);
        }
        if (isEmpty) {
            customDialog(false);
        }
        ((BottomNavigationView) findViewById(R.id.nav_view)).setOnNavigationItemSelectedListener(this.mOnNavigationItemSelectedListener);
    }
    private void customDialog(boolean z) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.win_dialog);
        TextView textView = (TextView) dialog.findViewById(R.id.wontext);
        if (z) {
            textView.setText("You have won " + this.Currency + " 60");
        } else {
            textView.setText("You have won " + this.Currency + " 50");
        }
        ((Button) dialog.findViewById(R.id.bi_dialog)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("dialog_invite_click", true);
                firebaseAnalytics.logEvent("dialog_invite_click", bundle);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.putExtra("android.intent.extra.TEXT", "Hey Check this amazing app, you can make " + Currency + " 500, you will get " + Currency + " 60 Signup Bonus, Just use my code below" + sharedPreferences.getString("refer", (String) null) + " https://play.google.com/store/apps/details?id=" + getPackageName());
                intent.setType("text/plan");
                startActivity(intent);
            }
        });
        dialog.show();
    }
    public void switchToMain() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).commit();
        if (mInterstitialAd != null) {
            mInterstitialAd.show(MainActivity2.this);
        }
    }
    public void switchToRedeem() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RedeemFragment()).commit();
        if (mInterstitialAd != null) {
            mInterstitialAd.show(MainActivity2.this);
        }
    }

    public void switchToTransaction() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TransactionsFragment()).commit();
        if (mInterstitialAd != null) {
            mInterstitialAd.show(MainActivity2.this);
        }
    }
}
