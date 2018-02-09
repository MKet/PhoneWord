package phone.phoneworld;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import logic.PhoneNumberTranslator;

public class MainActivity extends AppCompatActivity {
    private static final ArrayList<String> phoneNumbers = new ArrayList<>();
    private static final int REQUEST_PHONE_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        EditText numberText = findViewById(R.id.PhoneNumberText);
        Button callButton = findViewById(R.id.CallButton);
        Button translateButton = findViewById(R.id.TranslateButton);
        Button callHistoryButton = findViewById(R.id.HistoyButton);

        callButton.setEnabled(false);


        translateButton.setOnClickListener(v -> {
            PhoneNumberTranslator translator = new PhoneNumberTranslator();
            String translatedNumber = translator.ToNumber(numberText.getText().toString());

            if (!translatedNumber.matches("[-[0-9][A-Z]]+")) {
                callButton.setText(getString(R.string.CallButtonText));
                callButton.setEnabled(false);
            } else {
                callButton.setText(getString(R.string.CallButtonTextWithNumber, translatedNumber));
                callButton.setEnabled(true);
            }
        });

        callButton.setOnClickListener(v -> {
            // On "Call" button click, try to dial phone number.
            PhoneNumberTranslator translator = new PhoneNumberTranslator();
            String translatedNumber = translator.ToNumber(numberText.getText().toString());

            AlertDialog.Builder callDialog = new AlertDialog.Builder(this);
            callDialog.setMessage(getString(R.string.callDialog_message, translatedNumber));
            callDialog.setNeutralButton(getString(R.string.callDialog_NeutralButton), (e, i) -> {
                // add dialed number to list of called numbers.
                phoneNumbers.add(translatedNumber);
                // enable the Call History button
                callHistoryButton.setEnabled(true);

                // Create intent to dial phone
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(android.net.Uri.parse("tel:" + translatedNumber));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    String[] PERMISSIONS = {Manifest.permission.CALL_PHONE};
                    ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PHONE_CALL);
                    return;
                }
                startActivity(callIntent);
            });
            callDialog.setNegativeButton(getString(R.string.callDialog_NegativeButtonText), (n, i) -> {
            });

            // Show the alert dialog to the user and wait for response.
            callDialog.show();
        });

        callHistoryButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CallHistoryActivity.class);
            intent.putStringArrayListExtra("phone_numbers", phoneNumbers);
            startActivity(intent);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+918511812660"));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intent);
                }
            }
        }
    }
}
