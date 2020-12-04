package com.ledungcobra.cafo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.database.UserApiHandler;
import com.ledungcobra.cafo.models.user.DetailUserInfo;
import com.ledungcobra.cafo.ui_calllback.UIThreadCallBack;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileUserFragment extends Fragment {


    public ProfileUserFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ProfileUserFragment newInstance() {
        ProfileUserFragment fragment = new ProfileUserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    private TextView txtUsername,txtPassword,
    txtPhoneNumber, txtFullName,txtEmail;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_user, container, false);
        txtUsername = view.findViewById(R.id.txtUserNamePf);
        txtPassword = view.findViewById(R.id.txtPasswordPf);
        txtPhoneNumber = view.findViewById(R.id.txtPhonePf);
        txtFullName = view.findViewById(R.id.txtFullnamePf);
        txtEmail = view.findViewById(R.id.txtEmailPf);
        final DetailUserInfo userInfo = new DetailUserInfo();
        UserApiHandler.getInstance().getUser(new UIThreadCallBack<DetailUserInfo, Error>() {
            @Override
            public void stopProgressIndicator() {

            }

            @Override
            public void startProgressIndicator() {

            }

            @Override
            public void onResult(DetailUserInfo result) {
                txtUsername.setText(result.getUsername());
                txtFullName.setText(result.getUsername());
                txtPassword.setText("**********");
                txtPhoneNumber.setText(result.getPhoneNumber());
                txtEmail.setText(result.getEmail());
            }

            @Override
            public void onFailure(Error error) {

            }
        });



        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);




    }
}