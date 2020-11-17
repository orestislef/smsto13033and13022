package com.revinad.smsto13033;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.ToolbarWidgetWrapper;

import umairayub.madialog.MaDialog;
import umairayub.madialog.MaDialogListener;


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
        Button button1, button2, button3, button4, button5, button6 ,infoButton;
        Toolbar toolbar;

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
            case R.id.info_button:
                infoDialog();
                break;
            default:
                break;
        }
    }

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

    public void infoDialog() {
        new MaDialog.Builder(MainActivity.this)
                .setTitle(getString(R.string.info_dialog_title))
                .setMessage(getString(R.string.info_dialog_message))
                .setPositiveButtonText(getString(R.string.info_dialog_ok))
                .setButtonOrientation(LinearLayout.VERTICAL)
                .AddNewButton(R.style.Animation_Design_BottomSheetDialog, getString(R.string.info_dialog_ok), new MaDialogListener() {
                    @Override
                    public void onClick() {

                    }
                })
                .build();
    }

    public void shareDialog(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_button_text));
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.info_button:
                infoDialog();
                break;
            case R.id.share_button:
                shareDialog();
                break;
            default:
                break;
        }
        return true;
    }
}