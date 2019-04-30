package com.example.wong.fourm;


import android.app.Activity;
import android.content.Intent;
import android.location.Location;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

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


public class MainActivity extends AppCompatActivity {
    private ListView lv;
    private JSONObject obj;
    private ArrayList<Article> articleList;
    private int count = 0;

    protected Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OkGo.getInstance().init(getApplication());
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.lv_item);

//        GPSUtils gpsUtils = new GPSUtils(this);
//        String address = gpsUtils.getAddressStr();
//        Toast.makeText(this, address, Toast.LENGTH_LONG).show();


        OkGo.<String>get("http://192.168.1.2:3000/articles").execute(new StringCallback() {


            @Override
            public void onSuccess(Response<String> response) {
                Log.d("hello", response.body());
                try {
                    articleList = new ArrayList<Article>();
                    obj = new JSONObject(response.body());
                    count = Integer.parseInt(obj.getString("count"));
                    JSONArray articles = obj.getJSONArray("articles");
                    for (int i = 0; i < articles.length(); i++) {
                        JSONObject article = (JSONObject) articles.get(i);
                        //取出name
                        Article article1 = new Article();
                        article1.setId(article.getString("_id"));
                        article1.setTitle(article.getString("title"));
                        article1.setBody(article.getString("body"));
                        article1.setCreate_date(article.getString("create_date"));
                        JSONObject author = article.getJSONObject("author");
                        JSONObject request_data = article.getJSONObject("request");
                        article1.setAuthor_name(author.getString("name"));
                        article1.setAuthor_id(author.getString("_id"));
                        article1.setUrl(request_data.getString("url"));
                        article1.setType(request_data.getString("type"));

                        articleList.add(article1);
                    }

                    lv.setAdapter(new MyListAdapter());
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Article article = articleList.get(i);
                            Bundle bundle = new Bundle();

                            bundle.putString("articleId", article.getId());


                            Intent intent = new Intent(MainActivity.this, AticleDetailActivity.class);
                            intent.putExtras(bundle);
//                            finish();
                            startActivity(intent);


                        }
                    });

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


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_login:
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_add_article:
                intent = new Intent(MainActivity.this, AddArticleActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

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
            tv_title.setText(articleList.get(position).getTitle());
            tv_body.setText(articleList.get(position).getBody());
            return view;
        }
    }


}