package com.taxerts.scratch.ui.redeem;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.taxerts.scratch.R;
import com.taxerts.scratch.databinding.FragmentRedeemBinding;

public class RedeemFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String Currency;
    SharedPreferences.Editor editor;
    private OnFragmentInteractionListener mListener;
    private String mParam1;
    private String mParam2;
    Button paypal;
    TextView paypalminimum;
    Button paytm;
    TextView paytmminimum;
    SharedPreferences sharedPreferences;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public static RedeemFragment newInstance(String str, String str2) {
        RedeemFragment reddemFragment = new RedeemFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM1, str);
        bundle.putString(ARG_PARAM2, str2);
        reddemFragment.setArguments(bundle);
        return reddemFragment;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.mParam1 = getArguments().getString(ARG_PARAM1);
            this.mParam2 = getArguments().getString(ARG_PARAM2);
        }
        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("USER", 0);
        this.sharedPreferences = sharedPreferences2;
        this.editor = sharedPreferences2.edit();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_redeem, container, false);
        this.paypal = (Button) inflate.findViewById(R.id.paypal_button);
        this.Currency = this.sharedPreferences.getString(FirebaseAnalytics.Param.CURRENCY, "$");
        final long j = this.sharedPreferences.getLong("cash", 0);
        final long j2 = this.sharedPreferences.getLong("invites", 0);
        TextView textView = (TextView) inflate.findViewById(R.id.paypal_minimum);
        this.paypalminimum = textView;
        textView.setText("minimum withdrawal amaount is " + this.Currency + " 500");
        this.paypal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (j < 500) {
                    final AlertDialog create = new AlertDialog.Builder(getActivity()).create();
                    create.setTitle("Alert");
                    create.setMessage("You need to earn $" + (500 - j) + " more to reach $500");
                    create.setButton(-3, "OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            create.dismiss();
                        }
                    });
                    create.show();
                } else if (j2 < 20) {
                    final AlertDialog create2 = new AlertDialog.Builder(getActivity()).create();
                    create2.setTitle("Payment process");
                    create2.setMessage("Invite atleast " + (20 - j2) + " peoples to get your payment");
                    create2.setButton(-3, "OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            create2.dismiss();
                        }
                    });
                    create2.show();
                }
            }
        });
        return inflate;

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