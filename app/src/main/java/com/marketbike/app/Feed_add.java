package com.marketbike.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Feed_add extends Activity implements Transformation {

    private Menu menu;
    private ArrayList tCate;
    private AsyncTask<ArrayList, Void, ArrayList> task;
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
    private SharedPreferences.Editor editor;
    private HashMap<String, String> imagespath = new HashMap<String, String>();
    private int cntImgs = 0;
    private ImageView user_img;
    private TextView txt_username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_add);
        setTitle("Back");
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        getActionBar().setDisplayHomeAsUpEnabled(true);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.buttonLoadPicture = (Button) findViewById(R.id.buttonLoadPicture);
        this.ad = new AlertDialog.Builder(this);
        this.myGallery = (LinearLayout) findViewById(R.id.mygallery);
        this.txt_description = (EditText) findViewById(R.id.txt_description);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        this.fbid = settings.getString("fbid", "");
        String strimg = "https://graph.facebook.com/" + fbid + "/picture?type=large";
       /* this.txt_description.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (txt_description.getText().toString() != "") {
                    setEnableMenu(true);
                } else {
                    setEnableMenu(false);
                }
                return false;
            }
        });*/

        user_img = (ImageView) findViewById(R.id.user_img);
        Picasso.with(this).load(strimg).transform(this).into(user_img);
        buttonLoadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated stub

                Intent intent = new Intent();
                intent.setType("image/jpeg");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICKER_SELECT);
            }
        });
    }

    View insertPhoto(String path) {

        int _layid = 75;
        int _imgid = 976;

        File imgFile = new File(path);

        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        //Log.d("fb", "myBitmap = " + myBitmap);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 200);
        params.setMargins(10, 0, 0, 0);
        final RelativeLayout layout = new RelativeLayout(getApplicationContext());
        layout.setLayoutParams(params);
        layout.setGravity(Gravity.CENTER);
        layout.setId(_layid + cntImgs);

        this.imagespath.put(String.valueOf(_layid + cntImgs), path);

        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(220, 220));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(myBitmap);
        layout.addView(imageView, params);


        //Image btn
        final ImageButton btnRemove = new ImageButton(getApplicationContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(50, 50);
        btnRemove.setLayoutParams(lp);
        Drawable removeIcon = getResources().getDrawable(R.drawable.ic_remove_img);
        btnRemove.setBackground(removeIcon);
        btnRemove.setId(_imgid + cntImgs);
        layout.addView(btnRemove);


        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout root = (LinearLayout) findViewById(R.id.mygallery);
                final RelativeLayout child = (RelativeLayout) findViewById(layout.getId());
                int index = layout.getId();

                imagespath.remove(String.valueOf(index));

                root.removeView(child);
                cntImgs--;
                Log.d("clicked", "imagespath = " + imagespath);
            }
        });

        cntImgs++;


        //Picasso.with(getBaseContext()).load(path).into(imageView);


        return layout;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == IMAGE_PICKER_SELECT && resultCode == Activity.RESULT_OK) {

            String strSDPath = getPictuerPath(data, getBaseContext());
            //Log.d("fb", "strSDPath = " + strSDPath);

            myGallery.addView(insertPhoto(strSDPath));

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save, menu);
        this.menu = menu;
        this.setEnableMenu(true);
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
                if (this.txt_description.getText().length() > 0) {
                    new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected void onPreExecute() {
                            dialog = ProgressDialog.show(Feed_add.this, "", "Saving ...", true);
                            super.onPreExecute();
                        }


                        @Override
                        protected Void doInBackground(Void... params) {


                            Addimage();

                            HttpClient httpclient = new DefaultHttpClient();
                            HttpPost httppost = new HttpPost("http://marketbike.zoaish.com/api/set_product");
                            try {
                                // Add your data

                                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                                nameValuePairs.add(new BasicNameValuePair("fbid", fbid));
                                nameValuePairs.add(new BasicNameValuePair("inputFiles", inputFiles));
                                nameValuePairs.add(new BasicNameValuePair("txt_description", txt_description.getText().toString()));


                                UrlEncodedFormEntity formEntity = null;
                                try {
                                    formEntity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
                                } catch (UnsupportedEncodingException e2) {
                                    e2.printStackTrace();
                                }
                                if (formEntity != null)
                                    httppost.setEntity(formEntity);
                                HttpResponse response = httpclient.execute(httppost);

                            } catch (ClientProtocolException e) {
                                // TODO Auto-generated catch block
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                            }


                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            dialog.dismiss();
                            Intent resultData = new Intent();
                            resultData.putExtra("Additem", "OK");
                            setResult(Activity.RESULT_OK, resultData);


                            finish();
                        }
                    }.execute();
                    return true;
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(this).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Alert Dialog");

                    // Setting Dialog Message
                    alertDialog.setMessage("Please post something...");

                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed
                            Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();

                    return false;
                }
            case android.R.id.home:
                finish();
                break;

        }


        return true;
    }


    private void Addimage() {


        for (Map.Entry<String, String> entry : imagespath.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            // ...


            Log.d("fb", "value = " + value);


            String strUrlServer = getString(R.string.WebServiceURL) + "/upload_images";

            String resServer = uploadFiletoServer(value, strUrlServer);

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

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (strStatusID.equals("0")) {
                ad.setTitle("Error!");
                ad.setIcon(android.R.drawable.btn_star_big_on);
                ad.setMessage(strError);
                ad.setPositiveButton("Close", null);
                ad.show();

            } else {
                // myGallery.addView(insertPhoto(strFileName));
            }

        }

        // Log.d("fb", "resServer = " + inputFiles);

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

    @Override
    public Bitmap transform(Bitmap bitmap) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);


        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());


        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);


        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));


        if (output != bitmap) {
            bitmap.recycle();
        }


        return output;


    }

    @Override
    public String key() {
        return "circle()";
    }

}
