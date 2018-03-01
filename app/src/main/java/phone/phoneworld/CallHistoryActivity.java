package phone.phoneworld;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import logic.JSONStorage;
import logic.Storage;

public class CallHistoryActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            ArrayList<String> phoneNumbers = bundle.getStringArrayList(getString(R.string.phoneNumberListExtra));
            if (phoneNumbers != null)
                this.setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, phoneNumbers));
        }
    }

}
