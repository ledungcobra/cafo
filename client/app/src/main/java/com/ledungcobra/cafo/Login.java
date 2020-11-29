package com.ledungcobra.cafo;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ledungcobra.cafo.database.UserApiHandler;
import com.ledungcobra.cafo.models.user.DetailUserInfo;
import com.ledungcobra.cafo.models.user.UserInfo;
import com.ledungcobra.cafo.ui_calllback.OnAnimationEnd;
import com.ledungcobra.cafo.ui_calllback.UIThreadCallBack;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class Login extends AppCompatActivity {

    MaterialButton btnSignIn;
    MaterialButton btnCreateAccount;
    TextInputEditText edtFullName;
    TextInputEditText edtUsername;
    TextInputEditText edtEmail;
    TextInputEditText edtPassword;
    TextInputEditText edtConfirmPassword;
    TextInputLayout lFullname;
    TextInputLayout lEmail;
    TextInputLayout lConfirmPassword;

    TextInputEditText edtPhoneNumber;
    TextInputLayout lPhoneNumber;

    TextView tvSignUp;
    RadioGroup radioGroupLoginAs;
    boolean signInClicked = false;
    private static final String SHARED_PREF_NAME = "USER_ACCESS_TOKEN";
    SharedPreferences pref;
    String TAG = "CALL_API";

    View lnSignInAs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pref = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        final String TOKEN = pref.getString(getString(R.string.token), "");
        final String ROLE= pref.getString(getString(R.string.role), "");
        //Initial
        initUI();


        if (!TOKEN.equals("") && !ROLE.equals("")) {
            UserApiHandler.getInstance().setUserAccessToken(TOKEN);
            UserApiHandler.getInstance().getUser(new UIThreadCallBack<DetailUserInfo, Error>() {
                @Override
                public void stopProgressIndicator() {

                }

                @Override
                public void startProgressIndicator() {

                }

                @Override
                public void onResult(DetailUserInfo result) {
                    if (ROLE.equals("shipper")) {
                        startActivity(new Intent(Login.this, DriverScreen.class));
                    } else if (ROLE.equals("customer")) {
                        startActivity(new Intent(Login.this, MainActivity.class));
                    }
                    Log.d(TAG, "onResult: " + result);
                }

                @Override
                public void onFailure(Error error) {

                    Toast.makeText(Login.this, "User session expired", Toast.LENGTH_SHORT).show();
                    //Clean all data stored Shared pref when session expired
                    Editor editor = pref.edit();
                    editor.clear();
                    editor.apply();


                }
            });

        }




    }

    private void initUI() {
        btnSignIn = findViewById(R.id.buttonSignIn);
        btnCreateAccount = findViewById(R.id.buttonCreateAccount);
        edtFullName = findViewById(R.id.textFieldFullName);
        edtConfirmPassword = findViewById(R.id.textFieldConfirmPassword);
        edtUsername = findViewById(R.id.textFieldUsername);
        edtEmail = findViewById(R.id.textFieldEmail);
        edtPassword = findViewById(R.id.textFieldPassword);
        radioGroupLoginAs = findViewById(R.id.radioGroupLoginAs);

        lFullname = findViewById(R.id.fullname_layout);
        lEmail = findViewById(R.id.email_layout);
        lConfirmPassword = findViewById(R.id.confirm_password_layout);

        edtPhoneNumber = findViewById(R.id.textFieldPhoneNumber);
        lPhoneNumber = findViewById(R.id.phone_number_layout);

        tvSignUp = findViewById(R.id.tvSignUp);

        lnSignInAs = findViewById(R.id.ln_sign_as);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInClicked = !signInClicked;
                if (signInClicked) {
                    changeLayoutForSignIn();
                } else {
                    changeLayoutForCreateAccount();
                }

            }
        });

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (signInClicked) {
                    //User want to login
                    boolean shouldRun = true;

                    if (edtUsername.getText().equals("") || edtPassword.getText().equals("")) {
                        shouldRun = false;
                    }

                    if (shouldRun) {
                        UserApiHandler.getInstance().signIn(edtUsername.getText().toString(),
                                edtPassword.getText().toString(), new UIThreadCallBack<UserInfo, Error>() {
                                    @Override
                                    public void stopProgressIndicator() {

                                    }

                                    @Override
                                    public void startProgressIndicator() {

                                    }

                                    @Override
                                    public void onResult(UserInfo result) {

                                        try {

                                            storeUserData(result.getAccessToken(), result.getRoles().get(0));
                                            if (result.getRoles().get(0).equals("customer")) {
                                                startActivity(new Intent(Login.this, MainActivity.class));
                                            } else if (result.getRoles().get(0).equals("shipper")) {
                                                startActivity(new Intent(Login.this, DriverScreen.class));
                                            }
                                        } catch (Exception e) {
                                            Log.d("CALL_API", "onResult: " + e);
                                            Toast.makeText(Login.this, "Server Error", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onFailure(Error error) {
                                        Toast.makeText(Login.this, "Login failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(Login.this, "You must complete all field", Toast.LENGTH_SHORT).show();
                    }

                } else { // User want to Create an account
                    //TODO: CHECK
                    ArrayList<String> roles = new ArrayList<>();
                    roles.add(radioGroupLoginAs.getCheckedRadioButtonId() == R.id.radioCustomer ? "customer" : "shipper");
                    UserApiHandler.getInstance().signUp( edtUsername.getText().toString(),
                            edtPassword.getText().toString(),
                            edtEmail.getText().toString(), roles, edtPhoneNumber.getText().toString(), new UIThreadCallBack<Void, Error>() {
                                @Override
                                public void stopProgressIndicator() {

                                }

                                @Override
                                public void startProgressIndicator() {

                                }

                                @Override
                                public void onResult(Void result) {

                                    UserApiHandler.getInstance().signIn(edtUsername.getText().toString(),
                                            edtPassword.getText().toString(), new UIThreadCallBack<UserInfo, Error>() {
                                                @Override
                                                public void stopProgressIndicator() {

                                                }

                                                @Override
                                                public void startProgressIndicator() {

                                                }

                                                @Override
                                                public void onResult(UserInfo result) {
                                                    Log.d(TAG, "onResult: "+result);
                                                    assert  result!=null;
                                                    storeUserData(result.getAccessToken(), result.getRoles().get(0));

                                                    if (result.getRoles().get(0).equals("shipper")) {
                                                        startActivity(new Intent(Login.this, DriverScreen.class));
                                                    } else if (result.getRoles().get(0).equals("customer")) {
                                                        startActivity(new Intent(Login.this, MainActivity.class));
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Error error) {
                                                    Log.d(TAG, "onFailure: "+error);
                                                }
                                            });
                                }

                                @Override
                                public void onFailure(Error error) {
                                    Log.d(TAG, "onFailure: "+error);
                                }
                            }
                    );

                }

            }
        });

        UserApiHandler.getInstance().getUserAccessToken().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d(TAG, "User token "+ s);
            }
        });

    }

    private void storeUserData(String accessToken, String role) {
        Editor editor = pref.edit();
        editor.putString(getString(R.string.token), accessToken);
        editor.putString(getString(R.string.role), role);
        editor.apply();
    }

    private void changeLayoutForSignIn() {
        btnCreateAccount.setText("Sign In");
        btnSignIn.setText("Create An Account");

        edtFullName.setVisibility(View.GONE);
        animateView(lFullname);

        edtEmail.setVisibility(View.GONE);
        animateView(lEmail);

        edtConfirmPassword.setVisibility(View.GONE);
        animateView(lConfirmPassword);

        edtPhoneNumber.setVisibility(View.GONE);
        animateView(lPhoneNumber);

        tvSignUp.setText(R.string.sign_in);

        lnSignInAs.setVisibility(View.GONE);
    }

    private void changeLayoutForCreateAccount() {
        btnCreateAccount.setText("Create an account");

        btnSignIn.setText("Sign In");
        edtFullName.setVisibility(View.VISIBLE);
        animateView(lFullname);
        edtEmail.setVisibility(View.VISIBLE);
        animateView(lEmail);
        edtConfirmPassword.setVisibility(View.VISIBLE);
        animateView(lConfirmPassword);
        edtPhoneNumber.setVisibility(View.VISIBLE);
        animateView(lPhoneNumber);

        tvSignUp.setText(R.string.sign_up);

        lnSignInAs.setVisibility(View.VISIBLE);


    }

    private int maxHeight = -100;


    private void animateView(final View view) {

        final LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams.height == WRAP_CONTENT) {
            if (maxHeight == -100) {
                maxHeight = view.getMeasuredHeight();
            }
            slideView(view, view.getMeasuredHeight(), 0, null);
        } else {
            slideView(view, 0, maxHeight, new OnAnimationEnd() {
                @Override
                public void onEnd() {
                    layoutParams.height = WRAP_CONTENT;
                    view.setLayoutParams(layoutParams);
                }
            });
        }


    }

    public void slideView(final View view,
                          int currentHeight,
                          final int newHeight,
                          final OnAnimationEnd callback
    ) {
        ValueAnimator slideAnimator = ValueAnimator
                .ofInt(currentHeight, newHeight)
                .setDuration(500);

        /* We use an update listener which listens to each tick
         * and manually updates the height of the view  */

        slideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();

                view.getLayoutParams().height = value;
                view.requestLayout();

                if (callback != null && newHeight == value) {
                    callback.onEnd();
                }

            }
        });

        AnimatorSet animationSet = new AnimatorSet();
        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animationSet.play(slideAnimator);
        animationSet.start();
    }

}