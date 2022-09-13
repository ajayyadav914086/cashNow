package com.taxerts.scratch.ui.dashboard;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.taxerts.scratch.FAQ;
import com.taxerts.scratch.InviteFriend;
import com.taxerts.scratch.Login;
import com.taxerts.scratch.R;
import com.taxerts.scratch.ScratchCard;

public class DashboardFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String Currency;
    TextView cash;
    SharedPreferences.Editor editor;
    FirebaseAnalytics firebaseAnalytics;
    GridView gridView;
    int[] grid_img = {R.drawable.ic_baseline_card_giftcard_24, R.drawable.ic_baseline_share_24, R.drawable.facebook, R.drawable.whatsapp, R.drawable.ic_baseline_rate_review_24, R.drawable.ic_baseline_chat_24, R.drawable.ic_baseline_question_answer_24, R.drawable.ic_baseline_send_24};
    String[] grid_text = {"Scratch card", "Invite $ Earn", "Facebook Share", "Whatsapp Share", "Rating", "Help", "FAQ", "Logout"};
    /* access modifiers changed from: private */
    public FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    /* access modifiers changed from: private */
    public GoogleSignInClient mGoogleSignInClient;
    TextView name;
    DatabaseReference rDatabase;
    String mParam1;
    String mParam2;
    SharedPreferences sharedPreferences;
    TextView totalinvites;
    ImageView userimage;
    private OnFragmentInteractionListener mListener;
    private InterstitialAd mInterstitialAd;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public static DashboardFragment newInstance(String str, String str2) {
        DashboardFragment dashboardFragment = new DashboardFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM1, str);
        bundle.putString(ARG_PARAM2, str2);
        dashboardFragment.setArguments(bundle);
        return dashboardFragment;
    }
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.mParam1 = getArguments().getString(ARG_PARAM1);
            this.mParam2 = getArguments().getString(ARG_PARAM2);
        }
        this.mAuth = FirebaseAuth.getInstance();
        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("USER", 0);
        this.sharedPreferences = sharedPreferences2;
        this.editor = sharedPreferences2.edit();
        this.Currency = this.sharedPreferences.getString(FirebaseAnalytics.Param.CURRENCY, "$");
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        name = (TextView) root.findViewById(R.id.user_name);
        mAuth = FirebaseAuth.getInstance();
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mGoogleSignInClient = GoogleSignIn.getClient((Activity) getActivity(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build());
        mDatabase = FirebaseDatabase.getInstance().getReference("user");
        rDatabase = FirebaseDatabase.getInstance().getReference("refer");
        gridView = (GridView) root.findViewById(R.id.main_grid);
        totalinvites = (TextView) root.findViewById(R.id.total_invites);
        cash = (TextView) root.findViewById(R.id.user_earnings);
        userimage = (ImageView) root.findViewById(R.id.user_profile);
        String valueOf = String.valueOf(this.sharedPreferences.getLong("cash", 0));
        TextView textView = cash;
        textView.setText(Currency + "" + valueOf);
        this.totalinvites.setText("");
        this.name.setText(this.mAuth.getCurrentUser().getDisplayName());
        Picasso.get().load(this.sharedPreferences.getString("userimg", (String) null)).placeholder((int) R.drawable.ic_baseline_person_24).into(this.userimage);
        updateCash();
        gridView.setAdapter(new GridAdapter());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                switch (i) {
                    case 0:
                        startActivity(new Intent(getContext(), ScratchCard.class));
                        return;
                    case 1:
                        if (mInterstitialAd != null) {
                            mInterstitialAd.show(getActivity());
                        }
                        startActivity(new Intent(getContext(), InviteFriend.class));
                        return;
                    case 2:
                        if (mInterstitialAd != null) {
                            mInterstitialAd.show(getActivity());
                        }
                        Intent intent = new Intent("android.intent.action.SEND");
                        intent.setType("text/plain");
                        intent.putExtra("android.intent.extra.TEXT", "Hey Check this amazing app, you can make " + Currency + " 500, you will get " + Currency + " 60 Signup Bonus, Just use my code below" + sharedPreferences.getString("refer", "null") + " https://play.google.com/store/apps/details?id=");
                        startActivity(intent);
                        return;
                    case 3:
                        if (mInterstitialAd != null) {
                            mInterstitialAd.show(getActivity());
                        }
                        Intent intent2 = new Intent("android.intent.action.SEND");
                        intent2.setType("text/plain");
                        intent2.setPackage("com.whatsapp");
                        intent2.putExtra("android.intent.extra.TEXT", "Hey Check this amazing app, you can make " + Currency + " 500, you will get " + Currency + " 60 Signup Bonus, Just use my code below" + sharedPreferences.getString("refer", "null") + " https://play.google.com/store/apps/details?id=");
                        try {
                            startActivity(intent2);
                            return;
                        } catch (ActivityNotFoundException unused) {
                            Toast.makeText(getContext(), "Whatsapp is not installed on your device", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    case 4:
                        if (mInterstitialAd != null) {
                            mInterstitialAd.show(getActivity());
                        }
                        String packageName = getActivity().getPackageName();
                        try {
                            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + packageName)));
                            return;
                        } catch (ActivityNotFoundException unused2) {
                            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
                            return;
                        }
                    case 5:
                        if (mInterstitialAd != null) {
                            mInterstitialAd.show(getActivity());
                        }
                        Intent intent3 = new Intent("android.intent.action.SENDTO", Uri.parse("mailto:rohitrai680@gmail.com"));
                        intent3.putExtra("android.intent.extra.SUBJECT", "ScratchNow Help");
                        startActivity(Intent.createChooser(intent3, "help"));
                        return;
                    case 6:
                        if (mInterstitialAd != null) {
                            mInterstitialAd.show(getActivity());
                        }
                        startActivity(new Intent(getActivity(), FAQ.class));
                        return;
                    case 7:
                        mAuth.signOut();
                        mGoogleSignInClient.signOut().addOnCompleteListener((Activity) getActivity(), new OnCompleteListener<Void>() {
                            public void onComplete(Task<Void> task) {
                                startActivity(new Intent(getContext(), Login.class));
                                getActivity().finish();
                            }
                        });
                        return;
                    default:
                }
            }
        });
        InterstitialAd.load(getContext(),"ca-app-pub-3940256099942544/1033173712", adRequest,
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
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    private void updateCash() {
        this.mDatabase.child(mAuth.getUid()).child("Cash").addValueEventListener(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                editor.putLong("cash", (Long) dataSnapshot.getValue());
                editor.apply();
                String valueOf = String.valueOf(sharedPreferences.getLong("cash", 0));
                TextView textView = cash;
                textView.setText(Currency + "" + valueOf);
            }
        });
        this.mDatabase.child(this.mAuth.getUid()).child("Invites").addValueEventListener(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                long longValue = ((Long) dataSnapshot.getValue()).longValue();
                editor.putLong("invites", longValue);
                editor.apply();
                totalinvites.setText(String.valueOf(longValue));
            }
        });
    }

    private class GridAdapter extends BaseAdapter {
        public Object getItem(int i) {
            return null;
        }

        public long getItemId(int i) {
            return 0;
        }

        private GridAdapter() {
        }

        public int getCount() {
            return grid_img.length;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            View inflate = getLayoutInflater().inflate(R.layout.grid_data, (ViewGroup) null);
            ((TextView) inflate.findViewById(R.id.grid_text)).setText(grid_text[i]);
            ((ImageView) inflate.findViewById(R.id.grid_img)).setImageResource(grid_img[i]);
            return inflate;
        }
    }
    public void onButtonPressed(Uri uri) {
        OnFragmentInteractionListener onFragmentInteractionListener = this.mListener;
        if (onFragmentInteractionListener != null) {
            onFragmentInteractionListener.onFragmentInteraction(uri);
        }
    }
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            this.mListener = (OnFragmentInteractionListener) context;
            return;
        }
        throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }
}