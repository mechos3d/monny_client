package com.example.gorg.monny;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

// TODO: make fonts on buttons bigger.

public class MainActivity extends AppCompatActivity {
    private static SharedPreferences settingsPersistence;
    private static Button button_next;
    public static SharedPreferences appSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appSettings = PreferenceManager.getDefaultSharedPreferences(this);
        settingsPersistence = getSharedPreferences(VarStorage.PREFS_FILE_NAME, 0);
        button_next=(Button)findViewById(R.id.next_screen);
        VarStorage.current_sum = 0;
        VarStorage.current_sign = "-";
        setOnClickListeners();
        updateCurrentSumonNextScreenButton();
        VarStorage.mainActivity = this;
    }

    public static void updateCurrentSumonNextScreenButton() {
        String result = "";
        String authors_str = appSettings.getString("authors_list", "");
        String[] authors = authors_str.split(",");

        for(String author : authors ){
            int sum = settingsPersistence.getInt(author, 0);
            result = result + author + ": " + sum + " \n";
        }
        button_next.setText(result);
    }

    public void addToCounter(int number) {
        VarStorage.current_sum += number;
        changeCounterOnScreen();
    }

    public void changeCounterOnScreen(){
        int sum = VarStorage.current_sum;
        String sign = VarStorage.current_sign;
        TextView current_sum = (TextView)findViewById(R.id.current_sum);
        current_sum.setText(sign + " " + sum);
    }

    private void changeView() {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

    private void setOnClickListeners() {
        Button b1,b2,b3,b4,b5,b6;

        b1=(Button)findViewById(R.id.button_1);
        b2=(Button)findViewById(R.id.button_2);
        b3=(Button)findViewById(R.id.button_3);
        b4=(Button)findViewById(R.id.button_4);
        b5=(Button)findViewById(R.id.button_5);
        b6=(Button)findViewById(R.id.button_6);

        Button[] arr = new Button[] {b1, b2, b3, b4, b5, b6};

        for(Button b : arr ){
            String str = b.getText().toString();
            b.setOnClickListener(new ButtonListener(str));
        }

        Button button_next=(Button)findViewById(R.id.next_screen);
        button_next.setOnClickListener(new ChangeViewButtonListener());
    }

    class ButtonListener implements View.OnClickListener {
        private String string = "";
        public ButtonListener(String str) {
            super();
            string = str;
        }
        @Override
        public void onClick(View view) {
            int num = Integer.parseInt(string);
            addToCounter(num);
        }
    }

    class ChangeViewButtonListener implements View.OnClickListener {
        public ChangeViewButtonListener() {
            super();
        }
        @Override
        public void onClick(View view) {
            changeView();
        }
    }
}