package com.example.nishu.coffeeorderingappp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    
    
    
    
    
    private Button bDecrement;
    private Button bIncrement;
    private String sName=null;
    private String sPriceMessage=null;
    private String sPhone_number=null;
    private String sText=null;

    private CheckBox cbChocolateCheckBox;
    private CheckBox cbWhippedCreamCheckBox;
    private EditText etNameField;
    private EditText etPhoneNumber;
    private Intent iIntent1;
    private Intent iIntent2;
    private TextView tvPriceTextView;
    private TextView tvPriceSummaryTextView;
    private TextView tvQuantityTextView;

    private Toast gToast=null;
    private Toast mToast=null;
    private Toast cToast=null;

    private boolean bHasChecked;
    private boolean bIsChecked;

    private int iQuantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bDecrement = (Button) findViewById(R.id.bSubtract);
        bIncrement = (Button) findViewById(R.id.bAdd);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        Log.d(TAG, "onRequestPermissionsResult: " + requestCode);

        switch (requestCode) {

            case 0: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
                    //send sms here call your method
                    sendSms();
                } else {
                    Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }


    /**
     * This method decrements the quantity
     */
    public void decrement(View view) {
        iQuantity = iQuantity - 1;

        if (iQuantity <= 0) {
            iQuantity = 0;
            bDecrement.setEnabled(false);
        }

        displayQuantity(iQuantity);
        displayPrice(iQuantity * 5);
    }


    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        tvPriceSummaryTextView = (TextView) findViewById(R.id.tvPriceSummaryText);
        tvPriceSummaryTextView.setText(message);
        sText = "Please Wait...";
        mToast = Toast.makeText(this, sText, Toast.LENGTH_LONG);
        mToast.show();
    }


    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        tvQuantityTextView = (TextView) findViewById(R.id.tvQuantity);
        tvQuantityTextView.setText("" + number);
    }


    /**
     * This method displays the given price on the screen.
     */
    private void displayPrice(int number) {
        tvPriceTextView = (TextView) findViewById(R.id.tvPrice);
        tvPriceTextView.setText(NumberFormat.getCurrencyInstance().format(number));
    }


    /**
     * This method increments the quantity
     */
    public void increment(View view) {
        iQuantity = iQuantity + 1;

        if (iQuantity > 0) {
            bDecrement.setEnabled(true);
        }

        displayQuantity(iQuantity);
        displayPrice(iQuantity * 5);
    }


    public boolean isSMSPermissionGranted() {

        Log.d(TAG, "isSMSPermissionGranted: ");

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE}, 0);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }


    /**
     * This method is called when the order button is clicked.
     */
    private void order_summary(int total, String hadChecked, String isChecked) {
        sPriceMessage = "Name: " + sName;
        sPriceMessage += "\nAdd Whipped Cream: " + hadChecked;
        sPriceMessage += "\nAdd Chocolate: " + isChecked;
        sPriceMessage += "\nQuantity: " + iQuantity;
        sPriceMessage += "\nTotal: $" + total;
        sPriceMessage += "\nThank You!";
        displayMessage(sPriceMessage);
    }


    /**
     * This method executes when Email is clicked
     */
    public void sendEmail(View view) {
        iIntent1 = new Intent(Intent.ACTION_SENDTO);
        iIntent1.setData(Uri.parse("mailto:")); // only email apps should handle this
        iIntent1.putExtra(Intent.EXTRA_SUBJECT, "Coffee Order for" + sName);
        iIntent1.putExtra(Intent.EXTRA_TEXT, sPriceMessage);

        if (iIntent1.resolveActivity(getPackageManager()) != null) {
            startActivity(iIntent1);
        }
    }


    /**
     * This method executes when SMS is clicked
     */
    public void sendSMS(View view) {

        Log.d("Nishtha", "sendSMS: ");

        if (isSMSPermissionGranted()) {
            sendSms();
        }


    }


    private void sendSms() {
        try {

            Log.d(TAG, "sendSms: ");


            iIntent2 = new Intent(Intent.ACTION_SENDTO);
            iIntent2.setData(Uri.parse("smsto:" + sPhone_number));  // This ensures only SMS apps respond
            iIntent2.putExtra("sms_body", sPriceMessage);
            if (iIntent2.resolveActivity(getPackageManager()) != null) {
                startActivity(iIntent2);
            }

            //SmsManager smsManager = SmsManager.getDefault();
            //smsManager.sendTextMessage(phoneNo, null, msg, null, null);

            gToast=Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG);
            gToast.show();

        } catch (Exception ex) {

            cToast=Toast.makeText(getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_LONG);
            cToast.show();
            ex.printStackTrace();
        }
    }


    public void submitOrder(View view) {
        int price = iQuantity * 5;

        cbWhippedCreamCheckBox = (CheckBox) findViewById(R.id.checkWhippedCream);
        bHasChecked = cbWhippedCreamCheckBox.isChecked();

        String result = Boolean.toString(bHasChecked);
        String truee = "true";
        String finall;

        if (truee.equals(result)) {
            finall = "Yes";
        } else
            finall = "No";

        cbChocolateCheckBox = (CheckBox) findViewById(R.id.checkChocolate);
        bIsChecked = cbChocolateCheckBox.isChecked();

        String resultt = Boolean.toString(bIsChecked);
        String true1 = "true";
        String final1;


        if (true1.equals(resultt)) {
            final1 = "Yes";
        } else
            final1 = "No";


        if (bHasChecked) {
            price += 1;
        }


        if (bIsChecked) {
            price += 1;
        }

        etNameField = findViewById(R.id.etNameField);
        sName = etNameField.getText().toString();

        if (iQuantity == 0)
            price = 0;

        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        sPhone_number = etPhoneNumber.getText().toString();

        order_summary(price, finall, final1);

        //New lines added


    }
}
