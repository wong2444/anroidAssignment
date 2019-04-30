package com.example.wong.fourm;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
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

public class EditActicleActivity extends Activity {
    private EditText et_title;
    private EditText et_body;
    private Button btn_edit;
    private Button btn_delete;
    private SharedPreferences sp;
    private JSONObject obj;
    private String articleId;
    private String title;
    private String body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_acticle);
        et_title = (EditText) findViewById(R.id.et_title);
        et_body = (EditText) findViewById(R.id.et_body);
        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        sp = getSharedPreferences("token.txt", 0);
        Bundle b = getIntent().getExtras();
        //获取Bundle的信息
        articleId = b.getString("articleId");
        title = b.getString("title");
        body = b.getString("body");
        et_title.setText(title);
        et_body.setText(body);

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = et_title.getText().toString();
                body = et_body.getText().toString();

                OkGo.<String>put("http://192.168.1.2:3000/articles/" + articleId)
                        .headers("Authorization", "Bearer " + sp.getString("token", ""))
                        .params("body", body)
                        .params("title", title)
                        .execute(new StringCallback() {

                            @Override
                            public void onSuccess(Response<String> response) {
                                Log.d("hello", response.body());
                                try {

                                    obj = new JSONObject(response.body());
                                    if (!obj.isNull("request")) {

                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                        Bundle bundle = new Bundle();

                                        bundle.putString("articleId", articleId);
                                        finish();


                                        Intent intent = new Intent(EditActicleActivity.this, AticleDetailActivity.class);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
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

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditActicleActivity.this);
                alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        OkGo.<String>delete("http://192.168.1.2:3000/articles/" + articleId)
                                .headers("Authorization", "Bearer " + sp.getString("token", ""))

                                .execute(new StringCallback() {

                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        Log.d("hello", response.body());
                                        try {

                                            obj = new JSONObject(response.body());
                                            if (!obj.isNull("request")) {

                                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                                finish();
                                                Intent intent = new Intent(EditActicleActivity.this, MainActivity.class);

                                                startActivity(intent);
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
                alertDialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.setMessage("delete article?");
                alertDialog.setTitle("Alert");
                alertDialog.show();
            }
        });
    }


}
