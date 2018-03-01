package logic;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Marco on 2/22/2018.
 */

public class JSONStorage implements Storage<ArrayList<String>> {

    private Context context;
    private String fileName;

    public JSONStorage(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    @Override
    public ArrayList<String> read() {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            Gson gson = new Gson();
            return gson.fromJson(sb.toString(),  new TypeToken<ArrayList<String>>(){}.getType());

        } catch (IOException ioException) {
            return null;
        }
    }

    @Override
    public boolean create( ArrayList<String> jsonString){
        try {
            FileOutputStream fos = context.openFileOutput(fileName,Context.MODE_PRIVATE);
            if (jsonString != null) {
                Gson gson = new Gson();
                String data = gson.toJson(jsonString);
                fos.write(data.getBytes());
            }
            fos.close();
            return true;
        } catch (IOException ioException) {
            return false;
        }
    }

    @Override
    public boolean isAvailable() {
        String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        return file.exists();
    }
}
