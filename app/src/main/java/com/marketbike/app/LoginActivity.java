package com.marketbike.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class LoginActivity extends Activity {

    private List<String> permissions;
    private UiLifecycleHelper uiHelper;
    public Session.StatusCallback callback;
    private String get_id, get_name, get_gender, get_email, get_birthday, get_link, get_fname, get_lname, get_username;
    private SharedPreferences.Editor editor;
    GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "416625437190";
    public static final String PREFS_NAME = "MyData_Settings";
    Session session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        getActionBar().hide();
        //define facebook button
        LoginButton authButton = (LoginButton) findViewById(R.id.authButton);
        permissions = Arrays.asList("user_likes", "public_profile", "user_friends", "email", "user_birthday");
        //,"user_birthday","user_location" not approve
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        editor = settings.edit();

        if (checkLogin()) {
            Intent resultIntent = new Intent(this, MainActivity.class);
            startActivity(resultIntent);
        }

    }

    private boolean checkLogin() {
        if (session == null) {
            if (session == null) {
                session = Session.openActiveSessionFromCache(this);
                if (session == null) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
        Session session = Session.getActiveSession();
        if (session != null && (session.isOpened() || session.isClosed())) {
            if (session.getState().isOpened()) {

                Request.executeMeRequestAsync(session,
                        new Request.GraphUserCallback() {
                            @Override
                            public void onCompleted(GraphUser user,
                                                    Response response) {
                                if (response != null) {
                                    try {
                                        get_id = user.getId();
                                        get_name = user.getName();
                                        get_link = user.getLink();
                                        get_fname = (String) user.getProperty("first_name");
                                        get_lname = (String) user.getProperty("last_name");
                                        get_username = (String) user.getProperty("gender");
                                        get_gender = (String) user.getProperty("gender");
                                        get_email = (String) user.getProperty("email");
                                        get_birthday = user.getBirthday();

                                        editor.putString("fbid", get_id);
                                        editor.commit();

                                        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {


                                            @Override
                                            protected void onPreExecute() {
                                                super.onPreExecute();
                                            }

                                            @Override
                                            protected Void doInBackground(Void... arg0) {
                                                try {
                                                    if (gcm == null) {
                                                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                                                    }
                                                    regid = gcm.register(PROJECT_NUMBER);
                                                    HttpClient httpclient = new DefaultHttpClient();
                                                    HttpPost httppost = new HttpPost("http://marketbike.zoaish.com/api/user_register");
                                                    // Add your data
                                                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                                                    nameValuePairs.add(new BasicNameValuePair("regid", regid));
                                                    nameValuePairs.add(new BasicNameValuePair("fbid", get_id));
                                                    nameValuePairs.add(new BasicNameValuePair("name", get_name));
                                                    nameValuePairs.add(new BasicNameValuePair("gender", get_gender));
                                                    nameValuePairs.add(new BasicNameValuePair("email", get_email));
                                                    nameValuePairs.add(new BasicNameValuePair("birthday", get_birthday));
                                                    nameValuePairs.add(new BasicNameValuePair("link", get_link));
                                                    nameValuePairs.add(new BasicNameValuePair("fname", get_fname));
                                                    nameValuePairs.add(new BasicNameValuePair("lname", get_lname));
                                                    nameValuePairs.add(new BasicNameValuePair("username", get_username));

                                                   /* httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                                                    // execute HTTP post request
                                                    HttpResponse response = httpclient.execute(httppost);
                                                    HttpEntity resEntity = response.getEntity();*/


                                                    UrlEncodedFormEntity formEntity = null;
                                                    try {
                                                        formEntity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
                                                    } catch (UnsupportedEncodingException e2) {
                                                        e2.printStackTrace();
                                                    }
                                                    if (formEntity != null)
                                                        httppost.setEntity(formEntity);
                                                    HttpResponse response = httpclient.execute(httppost);
                                                    //Log.v("fb", "Response: " + response);


                                                  /*  if (resEntity != null) {

                                                        String responseStr = EntityUtils.toString(resEntity).trim();
                                                        Log.v("fb", "Response: " + responseStr);

                                                        // you can add an if statement here and do other actions based on the response
                                                    }*/

                                                } catch (ClientProtocolException e) {
                                                    e.printStackTrace();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                return null;
                                            }

                                            @Override
                                            protected void onPostExecute(Void result) {
                                            }

                                        };
                                        task.execute((Void[]) null);


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });

                this.finish();
                Intent newActivity = new Intent(this, MainActivity.class);
                startActivity(newActivity);
            }
        }
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }


}