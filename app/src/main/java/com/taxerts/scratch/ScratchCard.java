package com.taxerts.scratch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.analytics.FirebaseAnalytics;

public class ScratchCard extends AppCompatActivity {
    int[] card_background = {R.color.welcomebg, R.color.partybg, R.color.cheersbg, R.color.carnivalbg};
    String[] card_cost = {"5", "10", "15", "20"};
    int[] card_img = {R.drawable.welcomeelem, R.drawable.partyelem, R.drawable.cheerselem, R.drawable.carnivalelem};
    String[] card_price = {"win upto $0-10", "win upto $0-20", "win upto $0-30", "win upto $0-50"};
    String[] card_type = {"Welcome Card", "Party Card", "Cheers Card", "Carnival Card"};
    TextView cards_total_earnings;
    String cas;
    int count = 0;
    String currency;
    SharedPreferences.Editor editor;
    GridView gridView;
    TextView invitetot;
    SharedPreferences sharedPreferences;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch_card);
        getSupportActionBar().hide();
        SharedPreferences sharedPreferences2 = getSharedPreferences("USER", 0);
        sharedPreferences = sharedPreferences2;
        editor = sharedPreferences2.edit();
        sharedPreferences.getLong("invites", 0);
        currency = sharedPreferences.getString(FirebaseAnalytics.Param.CURRENCY, "$");
        gridView = (GridView) findViewById(R.id.gridview);
        TextView textView = (TextView) findViewById(R.id.invites_tot);
        invitetot = textView;
        textView.setText(String.valueOf(sharedPreferences.getLong("invites", 0)));
        TextView textView2 = (TextView) findViewById(R.id.cards_earnings);
        cards_total_earnings = textView2;
        textView2.setText(currency + " " + sharedPreferences.getLong("cash", 0));
        gridView.setAdapter(new GridcardAdapter());
        loadAd();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (i == 0) {
                    count++;
                    Intent intent = new Intent(ScratchCard.this, Scratch.class);
                    intent.putExtra("card_type", "welcome");
                    intent.putExtra("card_img",R.drawable.welcomeelem);
                    intent.putExtra("color",R.color.welcomebg);
                    intent.putExtra("cost","5");
                    startActivity(intent);
                    if (mInterstitialAd != null && count%2 ==0) {
                        mInterstitialAd.show(ScratchCard.this);
                    }
                } else if (i == 1) {
                    count++;
                    Intent intent2 = new Intent(ScratchCard.this, Scratch.class);
                    intent2.putExtra("card_type", "party");
                    intent2.putExtra("card_img",R.drawable.partyelem);
                    intent2.putExtra("color",R.color.partybg);
                    intent2.putExtra("cost","10");
                    startActivity(intent2);
                    if (mInterstitialAd != null && count%2 ==0) {
                        mInterstitialAd.show(ScratchCard.this);
                    }
                } else if (i == 2) {
                    count++;
                    Intent intent3 = new Intent(ScratchCard.this, Scratch.class);
                    intent3.putExtra("card_type", "cheers");
                    intent3.putExtra("card_img",R.drawable.cheerselem);
                    intent3.putExtra("color",R.color.cheersbg);
                    intent3.putExtra("cost","15");
                    startActivity(intent3);
                    if (mInterstitialAd != null && count%2 ==0) {
                        mInterstitialAd.show(ScratchCard.this);
                    }
                } else if (i == 3) {
                    count++;
                    Intent intent4 = new Intent(ScratchCard.this, Scratch.class);
                    intent4.putExtra("card_type", "carnival");
                    intent4.putExtra("card_img",R.drawable.carnivalelem);
                    intent4.putExtra("color",R.color.carnivalbg);
                    intent4.putExtra("cost","30");
                    startActivity(intent4);
                    if (mInterstitialAd != null && count%2 ==0) {
                        mInterstitialAd.show(ScratchCard.this);
                    }
                }
            }
        });
    }

    private void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdClicked() {
                                // Called when a click is recorded for an ad.
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                mInterstitialAd = null;
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                mInterstitialAd = null;
                            }

                            @Override
                            public void onAdImpression() {
                                // Called when an impression is recorded for an ad.

                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                            }
                        });

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }


                });
    }

    private class GridcardAdapter extends BaseAdapter {
        public Object getItem(int i) {
            return null;
        }

        public long getItemId(int i) {
            return 0;
        }

        private GridcardAdapter() {
        }

        public int getCount() {
            return ScratchCard.this.card_cost.length;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            View inflate = getLayoutInflater().inflate(R.layout.scratch_list, null);
            ((TextView) inflate.findViewById(R.id.card_cost)).setText("Price " +card_price[i]);
            ((TextView) inflate.findViewById(R.id.card_winning_price)).setText("Card Cost $"+card_cost[i]);
            inflate.findViewById(R.id.cardviewlist).setBackgroundResource(ScratchCard.this.card_background[i]);
            ((ImageView) inflate.findViewById(R.id.scratch_list_img)).setImageResource(ScratchCard.this.card_img[i]);
            ((TextView) inflate.findViewById(R.id.card_type_text)).setText(ScratchCard.this.card_type[i]);
            return inflate;
        }
    }
    public void onResume() {
        this.cas = String.valueOf(this.sharedPreferences.getLong("cash", 0));
        TextView textView = this.cards_total_earnings;
        textView.setText(this.currency + " " + this.cas);
        super.onResume();
        loadAd();
    }
}