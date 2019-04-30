package com.example.wong.fourm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class AddArticleActivity extends AppCompatActivity {
    private EditText et_title;
    private EditText et_body;
    private Button btn_submit;
    private SharedPreferences sp;
    private JSONObject obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);
        et_title = (EditText) findViewById(R.id.et_title);
        et_body = (EditText) findViewById(R.id.et_body);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        sp = getSharedPreferences("token.txt", 0);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = et_title.getText().toString();
                String body = et_body.getText().toString();

                OkGo.<String>post("http://192.168.1.2:3000/articles")
                        .headers("Authorization", "Bearer " + sp.getString("token", ""))
                        .params("body", body)
                        .params("title", title)
                        .execute(new StringCallback() {


                            @Override
                            public void onSuccess(Response<String> response) {
                                Log.d("hello", response.body());
                                try {

                                    obj = new JSONObject(response.body());
                                    if (!obj.isNull("createdArticle")) {

                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                        refresh();
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

    public void refresh() {

        finish();


        Intent intent = new Intent(AddArticleActivity.this, MainActivity.class);

        startActivity(intent);

    }
}


