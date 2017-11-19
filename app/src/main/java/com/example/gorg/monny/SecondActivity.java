package com.example.gorg.monny;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;

public class SecondActivity extends AppCompatActivity {

    private static final String dataFileName = "monny_records_file.txt";
    private static String category = "null";
    public static SharedPreferences appSettings;
    public static SharedPreferences settingsPersistence;
    private static int currentCategoriesSet = 1;

    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        setPreferences();

        setInitialCategoriesButtonText();

        setCounterOnScreen();
        setOnClickListeners();

        VarStorage.secondActivity = this;
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

    public void setCategory(String str) {
        this.category = str;
    }

    public void switchCategoryButtons() {
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
        String string = createSyncString();

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

    private String createSyncString() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        String result = timeStamp + ";" + VarStorage.current_sign + ";" + VarStorage.current_sum + ";" + category + ";" + getDataFromEditText();

        return result;
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

        Button back_button = (Button)findViewById(R.id.go_back_btn);
        Button commit_button = (Button)findViewById(R.id.commit_btn);
        Button settings_button = (Button)findViewById(R.id.settings_btn);
        Button sync_button = (Button)findViewById(R.id.sync_btn);

        back_button.setOnClickListener(new ChangeViewButtonListener());
        commit_button.setOnClickListener(new CommitButtonListener());
        settings_button.setOnClickListener(new SettingsButtonListener());
        sync_button.setOnClickListener(new SyncButtonListener());
    }

    public void handleTransferCategory() {
        createTransferDialogPopupWindow();
     }

    private void createTransferDialogPopupWindow() {

        Context context = getApplicationContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

        View dialogTransferView = inflater.inflate(R.layout.dialog_transfer,null);
        mPopupWindow = new PopupWindow(dialogTransferView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);


        if(Build.VERSION.SDK_INT>=21){ mPopupWindow.setElevation(5.0f); } // Set an elevation value for popup window ( Call requires API level 21 )

        TableLayout secondActivityTableLayout = (TableLayout) findViewById(R.id.second_activity_table_layout);

        Button transfer_dialog_ok_button = (Button) dialogTransferView.findViewById(R.id.transfer_dialog_ok);
        //transfer_dialog_ok_button.setOnClickListener( new TransferDialogOkButton(mPopupWindow));

        transfer_dialog_ok_button.setOnClickListener( new TransferDialogOkButton(dialogTransferView, mPopupWindow));

        mPopupWindow.showAtLocation(secondActivityTableLayout, Gravity.CENTER,0,0);
    }

    class TransferDialogOkButton implements View.OnClickListener {
        private View dialogTransferView;
        private PopupWindow popup;

        public TransferDialogOkButton(View v, PopupWindow p) {
            super();
            dialogTransferView = v;
            popup = p;
        }

        @Override
        public void onClick(View view) {
            EditText fromText = (EditText) dialogTransferView.findViewById(R.id.transfer_dialog_from_author);
            EditText toText = (EditText) dialogTransferView.findViewById(R.id.transfer_dialog_to_author);

            String fromTextString = fromText.getText().toString();
            String toTextString = toText.getText().toString();

            String fullTransferCategory = "transfer" + "__" + fromTextString + "__" + toTextString;
            VarStorage.secondActivity.setCategory(fullTransferCategory);

            popup.dismiss();
        }
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

}
