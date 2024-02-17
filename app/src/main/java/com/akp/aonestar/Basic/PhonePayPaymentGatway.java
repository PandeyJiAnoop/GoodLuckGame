package com.akp.aonestar.Basic;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.akp.aonestar.R;
import androidx.appcompat.app.AppCompatActivity;

public class PhonePayPaymentGatway extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_pay_payment_gatway);
        // Initialize PhonePe SDK
//        PhonePeSdk.init(this, "YOUR_MERCHANT_ID", "YOUR_MERCHANT_KEY");
//        findViewById(R.id.payButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                initiatePayment();
//            } });
    }

/*    private void initiatePayment() {
        // Create Payment Request
        PaymentRequest paymentRequest = new PaymentRequest.PaymentIntentBuilder()
                .setVpa("receiver@upi") // Replace with the receiver's UPI ID
                .setAmount(100) // Replace with the amount in paisa (e.g., 100 paisa = ₹1)
                .build();
        // Initiate Payment
        PhonePeSdk.startTransaction(PhonePayPaymentGatway.this, paymentRequest, new PaymentCallback() {
            @Override
            public void onTransactionResponse(TransactionStatus transactionStatus) {
                // Handle transaction response
                if (transactionStatus.getStatusCode() == TransactionStatus.SUCCESS) {
                    // Payment successful
                    displayMessage("Payment successful");
                } else {
                    // Payment failed or canceled
                    displayMessage("Payment failed");
                }
            }
            @Override
            public void onTransactionCancelled() {
                // Handle transaction cancellation
                displayMessage("Transaction cancelled");
            }
        });
    }

    private void displayMessage(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }*/
}