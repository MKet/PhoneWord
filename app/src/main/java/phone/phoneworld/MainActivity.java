package phone.phoneworld;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import logic.JSONStorage;
import logic.PhoneNumberTranslator;
import logic.Storage;

public class MainActivity extends AppCompatActivity {
    private static final ArrayList<String> phoneNumbers = new ArrayList<>();
    private static final int REQUEST_PHONE_CALL = 1;

    private EditText numberText;
    private Button callButton;
    private Button translateButton;
    private Button callHistoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberText = findViewById(R.id.PhoneNumberText);
        callButton = findViewById(R.id.CallButton);
        translateButton = findViewById(R.id.TranslateButton);
        callHistoryButton = findViewById(R.id.HistoyButton);

        callButton.setEnabled(false);
        Storage<ArrayList<String>> storage = new JSONStorage(this,  getString(R.string.history_filename));

        Context context = this.getParent();

        boolean available = storage.isAvailable();

        System.out.print("storage available" + Boolean.toString(available));

        ArrayList<String> phoneNumbers;

        if (!available)
            phoneNumbers = new ArrayList<>();
        else {
            phoneNumbers = storage.read();
        }


        translateButton.setOnClickListener(v -> {
            PhoneNumberTranslator translator = new PhoneNumberTranslator();
            String translatedNumber = translator.ToNumber(numberText.getText().toString());

            if (!translatedNumber.matches("[-0-9A-Za-z]+")) {
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

                storage.create(phoneNumbers);

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
            callDialog.setNegativeButton(getString(R.string.callDialog_NegativeButtonText), (n, i) -> {});

            // Show the alert dialog to the user and wait for response.
            callDialog.show();
        });

        callHistoryButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CallHistoryActivity.class);
            intent.putStringArrayListExtra(getString(R.string.phoneNumberListExtra), phoneNumbers);
            startActivity(intent);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhoneNumberTranslator translator = new PhoneNumberTranslator();
                    String translatedNumber = translator.ToNumber(numberText.getText().toString());
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + translatedNumber));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intent);
                }
            }
        }
    }
}
