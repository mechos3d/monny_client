package com.example.gorg.monny;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SecondActivity extends AppCompatActivity {

    private static final String dataFileName = "monny_records_file.txt";
    private static String category = "null";
    public static SharedPreferences appSettings;
    public static SharedPreferences settingsPersistence;
    private static int currentCategoriesSet = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        setPreferences();

        setInitialCategoriesButtonText();

        setCounterOnScreen();
        setOnClickListeners();
    }

    // NOTE: I store current total_sum in preferences file, it's not a preference,
    // but it's the only persistence that i know how to use
    private void setPreferences(){
        settingsPersistence = getSharedPreferences(VarStorage.PREFS_FILE_NAME, 0);
        appSettings = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void setInitialCategoriesButtonText(){
        // TODO: refactor - this method is almost identical to switchCategoryButtons()
        Button b1,b2,b3,b4,b5;

        b1=(Button)findViewById(R.id.cat_button_1);
        b2=(Button)findViewById(R.id.cat_button_2);
        b3=(Button)findViewById(R.id.cat_button_3);
        b4=(Button)findViewById(R.id.cat_button_4);
        b5=(Button)findViewById(R.id.cat_button_5);
        Button[] arr = new Button[] {b1, b2, b3, b4, b5};

        Button changeCatsButton = (Button)findViewById(R.id.change_cats_button);
        changeCatsButton.setText("-- 1 --");

        String[] categories = getCategoriesFromSettings();

        for(int i=0; i < 5; i++){
            if (i < categories.length ){
                arr[i].setText(categories[i]);
                arr[i].setEnabled(true);
            } else {
                arr[i].setText("---");
                arr[i].setEnabled(false);
            }
        }
    }

    private String[] getCategoriesFromSettings(){
        String categoriesString = appSettings.getString("categories_list", "");
        return categoriesString.split(",");
    }

    private void switchCategoryButtons() {
        Button b1,b2,b3,b4,b5;
        b1=(Button)findViewById(R.id.cat_button_1);
        b2=(Button)findViewById(R.id.cat_button_2);
        b3=(Button)findViewById(R.id.cat_button_3);
        b4=(Button)findViewById(R.id.cat_button_4);
        b5=(Button)findViewById(R.id.cat_button_5);
        Button[] arr = new Button[] {b1, b2, b3, b4, b5};

        Button changeCatsButton = (Button)findViewById(R.id.change_cats_button);

        String[] categories = getCategoriesFromSettings();
        if ( categories.length > currentCategoriesSet * 5 ){
            currentCategoriesSet++ ;
            changeCatsButton.setText("-- "+ Integer.toString(currentCategoriesSet) + " --");

            for(int i =0; i < 5; i++){
                int k = 5 * (currentCategoriesSet - 1) + i;
                if (k < categories.length ){
                    arr[i].setText(categories[k]);
                    arr[i].setEnabled(true);
                } else {
                    arr[i].setText("---");
                    arr[i].setEnabled(false);
                }
            }
        } else {
            currentCategoriesSet = 1;
            setInitialCategoriesButtonText();
        }
    }

    private void setCounterOnScreen(){
        int sum = VarStorage.current_sum;
        Button counterButton = (Button) findViewById(R.id.go_back_btn);
        counterButton.setText("" + sum);
    }

    private void changeViewBack(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showSettingsView(){
        Intent intent = new Intent(this, PreferencesActivity.class);
        startActivity(intent);
    }

    private void commit(){
        try {
            writeToFile();
            syncWithServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        changeViewBack();
    }

    private void writeToFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        String string = timeStamp + ";" + VarStorage.current_sign + ";" + VarStorage.current_sum + ";" + category + ";" + getDataFromEditText();

        File file = new File(getExternalFilesDir(null), dataFileName);
        if(!file.exists()) {
            file.createNewFile();
        }
        PrintWriter pw = null;
        try {
            OutputStream os = new FileOutputStream(file, true);
            pw = new PrintWriter(os);
            pw.println(string);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pw.close();
        }
    }

    private void syncWithServer() {
        File dataFile = new File(getExternalFilesDir(null), dataFileName);
        SyncService service = new SyncService(dataFile, this);
        service.perform();
    }

    private String getDataFromEditText(){
        EditText categoryEditor = (EditText) findViewById(R.id.category_text);
        return categoryEditor.getText().toString();
    }

    private void updateGoBackButton(){
        int sum = VarStorage.current_sum;
        Button counterButton = (Button) findViewById(R.id.go_back_btn);
        String str = "" + sum + "\n" + category;
        counterButton.setText("" + str);
    }

    private void setOnClickListeners(){
        Button b1,b2,b3,b4,b5;

        b1=(Button)findViewById(R.id.cat_button_1);
        b2=(Button)findViewById(R.id.cat_button_2);
        b3=(Button)findViewById(R.id.cat_button_3);
        b4=(Button)findViewById(R.id.cat_button_4);
        b5=(Button)findViewById(R.id.cat_button_5);
        Button[] arr = new Button[] {b1, b2, b3, b4, b5};

        for(Button b : arr ){
            b.setOnClickListener(new ButtonListener(b));
        }

        Button changeCatsButton = (Button)findViewById(R.id.change_cats_button);
        Button back_button = (Button)findViewById(R.id.go_back_btn);
        Button commit_button = (Button)findViewById(R.id.commit_btn);
        Button settings_button = (Button)findViewById(R.id.settings_btn);
        Button sync_button = (Button)findViewById(R.id.sync_btn);

        back_button.setOnClickListener(new ChangeViewButtonListener());
        commit_button.setOnClickListener(new CommitButtonListener());
        settings_button.setOnClickListener(new SettingsButtonListener());
        sync_button.setOnClickListener(new SyncButtonListener());
        changeCatsButton.setOnClickListener(new ChangeCatsButtonListener());
    }

    class ButtonListener implements View.OnClickListener {
        private Button button;

        public ButtonListener(Button b) {
            super();
            button = b;
        }
        @Override
        public void onClick(View view) {
            SecondActivity.category = button.getText().toString();
            updateGoBackButton();
        }
    }

    class ChangeViewButtonListener implements View.OnClickListener {

        public ChangeViewButtonListener() {
        }
        @Override
        public void onClick(View view) {
            changeViewBack();
        }
    }

    class CommitButtonListener implements View.OnClickListener {

        public CommitButtonListener() {
        }
        @Override
        public void onClick(View view) {
            commit();
        }
    }

    class SettingsButtonListener implements View.OnClickListener {

        public SettingsButtonListener() {
        }
        @Override
        public void onClick(View view) {
            showSettingsView();
        }
    }

    class SyncButtonListener implements View.OnClickListener {

        public SyncButtonListener() {
        }
        @Override
        public void onClick(View view) {
            syncWithServer();
        }
    }

    class ChangeCatsButtonListener implements View.OnClickListener {
        public ChangeCatsButtonListener() {
        }
        @Override
        public void onClick(View view) {
            switchCategoryButtons();
        }
    }

}
