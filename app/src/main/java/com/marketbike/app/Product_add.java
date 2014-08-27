package com.marketbike.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.marketbike.app.helper.JsonHelper;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Product_add extends Activity {

    private Menu menu;
    private ArrayList tCate;
    private AsyncTask<ArrayList, Void, ArrayList> task;
    private Spinner sp;
    private Bitmap bm;
    final String uploadFilePath = "/mnt/sdcard/";
    final String uploadFileName = "service_lifecycle.png";
    final Integer IMAGE_PICKER_SELECT = 900;
    private Button buttonLoadPicture;
    private AlertDialog.Builder ad;
    private ProgressDialog dialog = null;
    private LinearLayout myGallery;
    private String inputFiles = "";
    private EditText txt_title;
    private EditText txt_price;
    private EditText txt_description;
    private String fbid;
    public static final String PREFS_NAME = "MyData_Settings";
    private  SharedPreferences.Editor editor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_add);
        setTitle("ย้อนกลับ");
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        getActionBar().setDisplayHomeAsUpEnabled(true);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.sp = (Spinner) findViewById(R.id.cate_spinner);
        this.buttonLoadPicture = (Button) findViewById(R.id.buttonLoadPicture);
        this.get_category();
        this.ad = new AlertDialog.Builder(this);
        this.myGallery = (LinearLayout) findViewById(R.id.mygallery);
        this.txt_title = (EditText) findViewById(R.id.txt_title);
        this.txt_price = (EditText) findViewById(R.id.txt_price);
        this.txt_description = (EditText) findViewById(R.id.txt_description);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        this.fbid = settings.getString("fbid", "");
        this.txt_title.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (txt_title.getText().toString() != "" && txt_price.getText().toString() != "" && inputFiles != "") {
                    setEnableMenu(true);
                } else {
                    setEnableMenu(false);
                }
                return false;
            }
        });

        this.txt_price.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (txt_title.getText().toString() != "" && txt_price.getText().toString() != "" && inputFiles != "") {
                    setEnableMenu(true);
                } else {
                    setEnableMenu(false);
                }
                return false;
            }
        });


        buttonLoadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated stub

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICKER_SELECT);
            }
        });
    }

    View insertPhoto(String path) {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 200);
        params.setMargins(10, 0, 0, 0);
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setLayoutParams(params);
        layout.setGravity(Gravity.CENTER);

        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(220, 220));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(bm);

        Picasso.with(getBaseContext()).load(path).into(imageView);
        layout.addView(imageView, params);


        return layout;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == IMAGE_PICKER_SELECT && resultCode == Activity.RESULT_OK) {
            //dialog = ProgressDialog.show(Product_add.this, "", "Uploading file...", true);
            String strSDPath = getPictuerPath(data, getBaseContext());
            String strUrlServer = getString(R.string.WebServiceURL) + "/upload_images";

            String resServer = uploadFiletoServer(strSDPath, strUrlServer);

            /*** Default Value ***/
            String strStatusID = "0";
            String strError = "Unknow Status!";
            String strFileName = "";

            Log.d("fb", "resServer = " + resServer);
            try {
                JSONObject c = new JSONObject(resServer);
                strStatusID = c.getString("StatusID");
                strError = c.getString("Error");
                strFileName = c.getString("FileName");
                inputFiles = inputFiles + strFileName + ";";

                if (txt_title.getText().toString() != "" && txt_price.getText().toString() != "" && inputFiles != "") {
                    setEnableMenu(true);
                } else {
                    setEnableMenu(false);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (strStatusID.equals("0")) {
                ad.setTitle("Error!");
                ad.setIcon(android.R.drawable.btn_star_big_on);
                ad.setMessage(strError);
                ad.setPositiveButton("Close", null);
                ad.show();
                //dialog.dismiss();
            } else {
                myGallery.addView(insertPhoto(strFileName));
                //dialog.dismiss();
            }

        }
    }


    public String getPictuerPath(Intent data, Context context) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }


    private void get_category() {
        task = new AsyncTask<ArrayList, Void, ArrayList>() {
            @Override
            protected void onPreExecute() {
                tCate = new ArrayList();
                super.onPreExecute();
            }

            @Override
            protected ArrayList doInBackground(ArrayList... params) {

                try {
                    String url = "http://marketbike.zoaish.com/api/get_category";
                    JSONArray data = JsonHelper.getJson(url).getJSONArray("result");
                    //Log.v("fb", "data " + data);
                    for (int i = 0; i < data.length(); i++) {
                        String title = data.getJSONObject(i).getString("Headline");
                        tCate.add(title);
                    }


                } catch (Throwable e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return params[0];
            }

            @Override
            protected void onPostExecute(ArrayList result) {
                // Log.v("fb", "tCate " + tCate);

                ArrayAdapter<String> adp = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, tCate);
                adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp.setAdapter(adp);
            }

        };
        this.task.execute((ArrayList) null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save, menu);
        this.menu = menu;
        this.setEnableMenu(false);
        return super.onCreateOptionsMenu(menu);
    }

    private void setEnableMenu(boolean isenable) {
        if (isenable) {
            MenuItem menuItem = menu.getItem(0);
            menuItem.setEnabled(isenable);
            CharSequence menuTitle = menuItem.getTitle();
            SpannableString styledMenuTitle = new SpannableString(menuTitle);
            styledMenuTitle.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFFFF")), 0, menuTitle.length(), 0);
            menuItem.setTitle(styledMenuTitle);
        } else {
            MenuItem menuItem = menu.getItem(0);
            menuItem.setEnabled(isenable);
            CharSequence menuTitle = menuItem.getTitle();
            SpannableString styledMenuTitle = new SpannableString(menuTitle);
            styledMenuTitle.setSpan(new ForegroundColorSpan(Color.parseColor("#c0392b")), 0, menuTitle.length(), 0);
            menuItem.setTitle(styledMenuTitle);
        }

        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#e74c3c")));
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        int itemId = item.getItemId();



        switch (itemId) {
            case R.id.action_save:
                String category = (String) this.sp.getSelectedItem();
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://marketbike.zoaish.com/api/set_product");
                try {
                    // Add your data

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("fbid", fbid));
                    nameValuePairs.add(new BasicNameValuePair("txt_title", txt_title.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("inputFiles", inputFiles));
                    nameValuePairs.add(new BasicNameValuePair("category", category));
                    nameValuePairs.add(new BasicNameValuePair("txt_price", txt_price.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("txt_description", txt_description.getText().toString()));


                    UrlEncodedFormEntity formEntity = null;
                    try {
                        formEntity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
                    } catch (UnsupportedEncodingException e2) {
                        e2.printStackTrace();
                    }
                    if (formEntity != null)
                        httppost.setEntity(formEntity);

                   // httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);
                   // Log.d("fb", "response = " + response);


                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                }

                Intent resultData = new Intent();
                resultData.putExtra("Additem", "OK");
                setResult(Activity.RESULT_OK, resultData);
                finish();
                return true;
            case android.R.id.home:
                finish();
                break;

        }

        return true;
    }

    public String uploadFiletoServer(String strSDPath, String strUrlServer) {
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        int resCode = 0;
        String resMessage = "";

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        try {
            File file = new File(strSDPath);
            if (!file.exists()) {
                return "{\"StatusID\":\"0\",\"Error\":\"Please check path on SD Card\"}";
            }

            FileInputStream fileInputStream = new FileInputStream(new File(strSDPath));

            URL url = new URL(strUrlServer);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream outputStream = new DataOutputStream(conn
                    .getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"fileUpload\";" + " filename=\"" + strSDPath + "\"" + lineEnd);
            outputStream.writeBytes("Content-Type: image/JPEG" + lineEnd);
            outputStream.writeBytes(lineEnd);

            // create a buffer of maximum size
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Response Code and  Message
            resCode = conn.getResponseCode();
            if (resCode == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                int read = 0;
                while ((read = is.read()) != -1) {
                    bos.write(read);
                }
                byte[] result = bos.toByteArray();
                bos.close();

                resMessage = new String(result);

            }

            Log.d("fb", Integer.toString(resCode));
            Log.d("fb", resMessage.toString());

            fileInputStream.close();
            outputStream.flush();
            outputStream.close();

            return resMessage.toString();

        } catch (Exception ex) {
            Log.d("fb", "ex = " + ex.toString());
            dialog.dismiss();
            return null;
        }
    }


}
