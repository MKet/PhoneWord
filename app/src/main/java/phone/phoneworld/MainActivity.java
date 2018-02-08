package phone.phoneworld;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
    }
}
