package com.example.motorcycleapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityThird21 extends AppCompatActivity {

    private Button submitButton;
    private EditText password;
    private EditText confirmPassword;
    private String passwordInput;
    private String confirmPasswordInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_screenv21);

        submitButton = findViewById(R.id.createAccount);
        password = (EditText) findViewById(R.id.pin);
        confirmPassword = (EditText) findViewById(R.id.confirmPin);

        password.addTextChangedListener(loginTextWatcher);
        confirmPassword.addTextChangedListener(loginTextWatcher);

        final SharedPreferences database = getSharedPreferences("UserInfo", MODE_PRIVATE);
        final SharedPreferences.Editor editor = database.edit();

        final String Alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvxyz" + "0123456789";
        final Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");

        submitButton.setEnabled(false);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String passwordValue=password.getText().toString();
                String confirmpassValue=confirmPassword.getText().toString();

                Matcher matcher1 = pattern.matcher(passwordValue);
                Matcher matcher2 = pattern.matcher(confirmpassValue);

                if (!matcher1.matches() || !matcher2.matches()) {
                    Toast.makeText(ActivityThird21.this, "Invalid! Password must contain with letters and numbers only", Toast.LENGTH_SHORT).show();
                } else if(!passwordValue.equals(confirmpassValue) ) {
                    Toast.makeText(ActivityThird21.this, "Invalid! Password Does Not Match", Toast.LENGTH_SHORT).show();
                } else if (passwordValue.length()<8 || passwordValue.length()>16 ||
                        confirmpassValue.length()<8 || confirmpassValue.length()>16 ) {
                    Toast.makeText(ActivityThird21.this, "Invalid! Password must be 8-16 characters", Toast.LENGTH_SHORT).show();
                } else {

                    editor.putString("password",passwordValue);
                    editor.putString("confirmPass",confirmpassValue);
                    editor.apply();

                    final AlertDialog.Builder builder = new AlertDialog.Builder(ActivityThird21.this);
                    builder.setCancelable(true);
                    builder.setTitle("Recovery Code");

                    StringBuilder recoverycode = new StringBuilder(8);

                    for (int i = 0; i <8; i++) {
                        int index = (int)(Alphanumeric.length() * Math.random());
                        recoverycode.append(Alphanumeric.charAt(index));
                    }

                    editor.putString("recoveryCode",recoverycode.toString());
                    editor.apply();

                    builder.setMessage("In case you forgot your password and pin number, use this code" + " '"+ recoverycode + "'"+ " .Take note of this code as this will only be given once." );

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            password.setText("");
                            confirmPassword.setText("");
                            openSuccessfulSignUp();
                        }
                    });
                    builder.show();

                }
            }
        });

    }

    private TextWatcher loginTextWatcher = new TextWatcher() {


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            passwordInput = password.getText().toString().trim();
            confirmPasswordInput = confirmPassword.getText().toString().trim();

            submitButton.setEnabled(!passwordInput.isEmpty() && !confirmPasswordInput.isEmpty());
        }
        @Override
        public void afterTextChanged(Editable s) {
            passwordInput = password.getText().toString().trim();
            confirmPasswordInput = confirmPassword.getText().toString().trim();

            final SharedPreferences database = getSharedPreferences("UserInfo", MODE_PRIVATE);
            final SharedPreferences.Editor editor = database.edit();
            editor.putString("passInput",passwordInput);
            editor.putString("confirmPassInput",confirmPasswordInput);
            editor.apply();
        }
    };

    public void openSuccessfulSignUp() {
        Intent intent = new Intent(this, successfulSignUp.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final SharedPreferences database = getSharedPreferences("UserInfo", MODE_PRIVATE);
        final SharedPreferences.Editor editor = database.edit();
        String inputtedPass = database.getString("passInput","");
        String inputtedConfirmPass = database.getString("confirmPassInput","");

        password.setText(inputtedPass);
        confirmPassword.setText(inputtedConfirmPass);
    }
}
