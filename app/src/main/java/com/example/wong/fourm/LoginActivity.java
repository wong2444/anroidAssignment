package com.example.wong.fourm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    EditText et_email;
    EditText et_name;
    EditText et_password;
    Button btn_login;
    Button btn_register;
    private JSONObject obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_name = (EditText) findViewById(R.id.et_name);
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = et_email.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                OkGo.<String>post("http://192.168.1.2:3000/user/login")
                        .params("email", email)
                        .params("password", password)
                        .execute(new StringCallback() {


                            @Override
                            public void onSuccess(Response<String> response) {
                                Log.d("hello", response.body());
                                try {

                                    obj = new JSONObject(response.body());
                                    if (!obj.isNull("token")) {
                                        SharedPreferences sp = getSharedPreferences("token.txt", 0);
                                        SharedPreferences.Editor edit = sp.edit();
                                        edit.putString("token", obj.getString("token"));
                                        edit.putString("userId", obj.getString("userId"));
                                        edit.apply();
                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {

                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                        et_email.setText("");
                                        et_password.setText("");
                                    }


                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onError(Response<String> response) {
                                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                                Log.d("hello", "fuck");
                                super.onError(response);
                            }
                        });
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String name = et_name.getText().toString();
                OkGo.<String>post("http://192.168.1.2:3000/user/signup")
                        .params("email", email)
                        .params("password", password)
                        .params("name", name)
                        .execute(new StringCallback() {


                            @Override
                            public void onSuccess(Response<String> response) {
                                Log.d("hello", response.body());
                                try {

                                    obj = new JSONObject(response.body());
                                    if (obj.isNull("error")) {
                                        et_email.setText("");
                                        et_password.setText("");
                                        et_name.setText("");

                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();

                                    } else {

                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();

                                    }


                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onError(Response<String> response) {
                                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                                Log.d("hello", "fuck");
                                super.onError(response);
                            }
                        });
            }
        });

    }
}
