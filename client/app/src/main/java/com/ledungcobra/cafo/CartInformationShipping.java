package com.ledungcobra.cafo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ledungcobra.cafo.models.common_new.CartShop;


import java.util.ArrayList;

public class CartInformationShipping extends Activity {
    //Views
    protected EditText
            edtFullname,
            edtAddress,
            edtPhoneNumber,
            edtNote,
            edtCode;
    protected TextView
            tvCostFood,
            tvShippingFee,
            tvTotalCost;
    protected Button btnOrderShip;

    //Data
    ArrayList<CartShop> listCartShop;
    int foodCost = 0;
    int shippingFeeCost = 0;
    int totalCost = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_infomation_shipping);

        bindViews();
        btnOrderShip.setEnabled(false);

        Intent intent = getIntent();
        listCartShop = (ArrayList<CartShop>) intent.getSerializableExtra("Info");

        if (listCartShop != null && listCartShop.size() > 0) {
            for (CartShop cartShop: listCartShop){
                foodCost += cartShop.getFood().getPrice().getValue() * cartShop.getNumber();
            }
        }
        //TODO: Move this to make formatter for currency
        tvCostFood.setText(
                String.format("%,d", foodCost) + getResources().getString(R.string.currency));

        calcShippingFee();
        tvShippingFee.setText(
                String.format("%,d", shippingFeeCost) + getResources().getString(R.string.currency));

        calcTotalCost();
        tvTotalCost.setText(
                String.format("%,d", totalCost) + getResources().getString(R.string.currency));

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                //validate input
                for (EditText edt  : new EditText[] {edtFullname, edtAddress, edtPhoneNumber}) {
                    if (edt.getText().toString().trim().isEmpty()) {
                        btnOrderShip.setEnabled(false);
                        return;
                    }
                }
                btnOrderShip.setEnabled(true);
            }
        };

        edtFullname.addTextChangedListener(textWatcher);
        edtAddress.addTextChangedListener(textWatcher);
        edtPhoneNumber.addTextChangedListener(textWatcher);

        btnOrderShip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = edtFullname.getText().toString();
                String address = edtAddress.getText().toString();
                String phone = edtPhoneNumber.getText().toString();
                String notes = edtNote.getText().toString();

                String message = new String(
                        "Your name: "+ fullname
                        +"\nAddress: "+ address
                        +"\nPhone: " + phone
                        +"\nNotes: " + notes
                        +"\n\nTotal Cost: " + tvTotalCost.getText().toString()
                        //Too lazy to do format number again :p
                );

                AlertDialog.Builder myBuilder =
                        new AlertDialog.Builder(CartInformationShipping.this);
                myBuilder.setTitle("Confirm Order")
                        .setMessage(message)
                        .setPositiveButton("Confirm Order", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichOne) {
                                //TODO: Call API to place order
                                Toast.makeText(
                                        CartInformationShipping.this,
                                        "Order placed. We'll phone you to confirm shortly",
                                        Toast.LENGTH_SHORT
                                ).show();
                                finish();
                                //Intent browser = new Intent(Intent.ACTION_VIEW, null);
                                //startActivity(browser);
                            }})
                        .setNegativeButton("Cancel", null) //setNegativeButton
                        .show();
            }
        });

    }

    private void calcShippingFee() {
        if (foodCost > 0) {
            shippingFeeCost = 20000;
        }
        //TODO: Special shipping codes
        if (edtCode.getText().toString().equals("FREE_SHIPPING_CODE")) {
            shippingFeeCost = 0;
        }
    }

    private void calcTotalCost() {
        totalCost = foodCost + shippingFeeCost;
    }

    private void bindViews() {
        edtFullname = findViewById(R.id.editFullNameShip);
        edtAddress = findViewById(R.id.editAddressShip);
        edtPhoneNumber = findViewById(R.id.editPhone);
        edtNote = findViewById(R.id.editNote);
        edtCode = findViewById(R.id.editFreeShipCode);

        tvCostFood = findViewById(R.id.tvCostFoodShip);
        tvShippingFee = findViewById(R.id.tvFeeShip);
        tvTotalCost = findViewById(R.id.tvTotalCost);

        btnOrderShip = findViewById(R.id.btnOrderShip);
    }

}