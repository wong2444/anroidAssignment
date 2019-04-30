package com.example.wong.fourm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AticleDetailActivity extends AppCompatActivity {
    private ListView lv;
    private JSONObject obj;
    private ArrayList<Article> articleList;
    private ArrayList<Comment> commentList;
    private int count = 0;
    private Article article1;
    private TextView tv_title;
    private TextView tv_author;
    private TextView tv_body;
    private EditText et_comment;
    private Button btn_submit;
    private String articleId;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aticle_detail);
        OkGo.getInstance().init(getApplication());
        lv = (ListView) findViewById(R.id.lv_comment);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_author = (TextView) findViewById(R.id.tv_author);
        tv_body = (TextView) findViewById(R.id.tv_body);
        et_comment = (EditText) findViewById(R.id.et_comment);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        sp = getSharedPreferences("token.txt", 0);
        Bundle b = getIntent().getExtras();
        //获取Bundle的信息
        articleId = b.getString("articleId");


        OkGo.<String>get("http://192.168.1.2:3000/articles/" + articleId).execute(new StringCallback() {


            @Override
            public void onSuccess(Response<String> response) {
                Log.d("hello", response.body());
                try {
//                    articleList = new ArrayList<Article>();
                    commentList = new ArrayList<Comment>();
                    obj = new JSONObject(response.body());
                    JSONObject article = obj.getJSONObject("article");
                    JSONArray comments = article.getJSONArray("comments");
                    count = comments.length();
                    for (int i = 0; i < comments.length(); i++) {
                        JSONObject comment = (JSONObject) comments.get(i);
                        //取出name
                        Comment c = new Comment();
                        c.setId(comment.getString("_id"));
                        c.setBody(comment.getString("body"));
                        c.setCreate_date(comment.getString("create_date"));
                        c.setUpdate_date(comment.getString("update_date"));
                        c.setIs_update(comment.getBoolean("is_update"));
                        c.setIs_delete(comment.getBoolean("is_delete"));
                        JSONObject author = comment.getJSONObject("author");
                        c.setAuthor_name(author.getString("name"));
                        c.setAuthor_id(author.getString("_id"));

                        commentList.add(c);
                    }
                    article1 = new Article();
                    article1.setId(article.getString("_id"));
                    article1.setTitle(article.getString("title"));
                    article1.setBody(article.getString("body"));
                    article1.setCreate_date(article.getString("create_date"));
                    article1.setUpdate_date(article.getString("update_date"));
                    article1.setIs_update(article.getBoolean("is_update"));
                    article1.setIs_delete(article.getBoolean("is_delete"));
                    JSONObject author = article.getJSONObject("author");
                    article1.setAuthor_id(author.getString("_id"));
                    article1.setAuthor_name(author.getString("name"));

                    tv_title.setText(article1.getTitle());
                    tv_author.setText(article1.getAuthor_name() + "        " + article1.getCreate_date());
                    if (sp.getString("userId", "").equals(article1.getAuthor_id())) {
                        tv_body.setText(article1.getBody() + "\n\n" + "[edit]" + "\n");
                        tv_body.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();

                                bundle.putString("articleId", article1.getId());
                                bundle.putString("title", article1.getTitle());
                                bundle.putString("body", article1.getBody());


                                Intent intent = new Intent(AticleDetailActivity.this, EditActicleActivity.class);
                                intent.putExtras(bundle);
                                finish();
                                startActivity(intent);
                            }
                        });
                    } else {
                        tv_body.setText(article1.getBody() + "\n\n");
                    }


                    lv.setAdapter(new MyListAdapter());


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Response<String> response) {
                Log.d("hello", "fuck");
                super.onError(response);
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = et_comment.getText().toString();

                OkGo.<String>post("http://192.168.1.2:3000/comments")
                        .headers("Authorization", "Bearer " + sp.getString("token", ""))
                        .params("body", comment)
                        .params("articleId", articleId)
                        .execute(new StringCallback() {


                            @Override
                            public void onSuccess(Response<String> response) {
                                Log.d("hello", response.body());
                                try {

                                    obj = new JSONObject(response.body());
                                    if (!obj.isNull("createdComment")) {

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
        Bundle bundle = new Bundle();
        bundle.putString("articleId", articleId);

        Intent intent = new Intent(AticleDetailActivity.this, AticleDetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    private class MyListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return count;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = View.inflate(getApplicationContext(), R.layout.item_layout, null);

            } else {
                view = convertView;
            }
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            TextView tv_body = (TextView) view.findViewById(R.id.tv_body);
            tv_title.setText(commentList.get(position).getBody());
            tv_body.setText(commentList.get(position).getAuthor_name());
            return view;
        }
    }
}
