package com.revinad.smsto13033;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    public String
            smsNum = "13033",
            mName, mStreet;
    public EditText name, street;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        mName = preferences.getString("autoSaveName", "");
        mStreet = preferences.getString("autoSaveStreet", "");

        CheckBox saveCB;
        boolean saveIns = preferences.getBoolean("autoSave", true);
        Button button1, button2, button3, button4, button5, button6;

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);

        button1.setOnClickListener(this::onClick);
        button2.setOnClickListener(this::onClick);
        button3.setOnClickListener(this::onClick);
        button4.setOnClickListener(this::onClick);
        button5.setOnClickListener(this::onClick);
        button6.setOnClickListener(this::onClick);

        name = findViewById(R.id.name_et);
        street = findViewById(R.id.street_et);
        saveCB = findViewById(R.id.save_cb);

        name.setText(mName);
        street.setText(mStreet);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                preferences.edit().putString("autoSaveName", s.toString()).apply();
            }

            @Override
            public void afterTextChanged(Editable s) {
                preferences.edit().putString("autoSaveName", s.toString()).apply();
            }
        });
        street.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                preferences.edit().putString("autoSaveStreet", s.toString()).apply();
            }

            @Override
            public void afterTextChanged(Editable s) {
                preferences.edit().putString("autoSaveStreet", s.toString()).apply();
            }
        });

        saveCB.setChecked(saveIns);

        saveCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    name.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            preferences.edit().putString("autoSaveName", name.toString()).apply();
                        }
                    });
                    street.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            preferences.edit().putString("autoSaveStreet", street.toString()).apply();
                        }
                    });
                    preferences.edit().putBoolean("autoSave", true).apply();
                } else {
                    preferences.edit().putString("autoSaveName", null).apply();
                    preferences.edit().putString("autoSaveStreet", null).apply();
                    preferences.edit().putBoolean("autoSave", false).apply();

                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                sendSms(1);
                break;
            case R.id.button2:
                sendSms(2);
                break;
            case R.id.button3:
                sendSms(3);
                break;
            case R.id.button4:
                sendSms(4);
                break;
            case R.id.button5:
                sendSms(5);
                break;
            case R.id.button6:
                sendSms(6);
                break;
            default:
                break;
        }
    }

//    private void sendSms(int i) {
//        String msgString = (i + " " + name.getText() + " " + street.getText()).toUpperCase();
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setType("vnd.android-dir/mms-sms");
//        intent.putExtra("address", smsNum);
//        intent.putExtra(Intent.EXTRA_TEXT, msgString);
//        if (isEditTextEmpty(name)) {
//            name.setError(getString(R.string.error_empty_tv));
//            return;
//        }
//        if (isEditTextEmpty(street)) {
//            street.setError(getString(R.string.error_empty_tv));
//            return;
//        } else{
//            Log.d(TAG, "sendSms: " + intent.toString());
//            startActivity(intent);
//        }
//    }

    private void sendSms(int i) {
        String msgBody = (i + " " + name.getText() + " " + street.getText()).toUpperCase();
        String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(this);
        Log.d(TAG, "sendSms: " + defaultSmsPackageName);
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra("address", " " + smsNum);
        sendIntent.putExtra(Intent.EXTRA_TEXT, msgBody);
        if (defaultSmsPackageName != null) {
            sendIntent.setPackage(defaultSmsPackageName);
            if (isEditTextEmpty(name)) {
                name.setError(getString(R.string.error_empty_tv));
                return;
            }
            if (isEditTextEmpty(street)) {
                street.setError(getString(R.string.error_empty_tv));
                return;
            } else {
                startActivity(sendIntent);
            }
        }
    }

    public boolean isEditTextEmpty(EditText mInput) {
        return mInput.length() == 0;
    }
}