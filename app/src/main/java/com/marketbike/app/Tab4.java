package com.marketbike.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class Tab4 extends Fragment {

    /**
     * Called when the activity is first created.
     */


    private boolean IS_LOGIN;
    private String get_id, get_name, get_gender, get_email, get_birthday;
    private String imageURL;
    private ImageView image_profile;
    private TextView txtFullName;
    private ImageView imageBgProfile;
    private Session session;
    private RelativeLayout profileConsole;
    private LinearLayout tab4;
    private View rootView;
    GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "416625437190";
    private Button btn_editprofile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(false);
        this.rootView = inflater.inflate(R.layout.tab4, container, false);
        image_profile = (ImageView) rootView.findViewById(R.id.image_profile);
        imageBgProfile = (ImageView) rootView.findViewById(R.id.imageBgProfile);
        profileConsole = (RelativeLayout) rootView.findViewById(R.id.profileConsole);
        btn_editprofile = (Button) rootView.findViewById(R.id.btn_editprofile);
        txtFullName = (TextView) rootView.findViewById(R.id.txtFullName);
        tab4 = (LinearLayout) rootView.findViewById(R.id.tab4);
        session = Session.getActiveSession();
        if (session != null && (session.isOpened() || session.isClosed())) {
            onSessionStateChange(session, session.getState(), null);
        } else if (session == null || session.isClosed()) {
            profileConsole.setVisibility(View.GONE);
            tab4.setPadding(0, 20, 0, 0);
        }


        btn_editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //session.close();
                //session.closeAndClearTokenInformation();
                Intent resultIntent = new Intent(getActivity(), SettingActivity.class);
                startActivity(resultIntent);
            }
        });

        return rootView;
    }


    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            //Log.i("fb", "Logged in...");
            Request.executeMeRequestAsync(session,
                    new Request.GraphUserCallback() {
                        @Override
                        public void onCompleted(GraphUser user,
                                                Response response) {
                            if (response != null) {
                                try {
                                    get_id = user.getId();
                                    get_name = user.getName();
                                    get_gender = (String) user.getProperty("gender");
                                    get_email = (String) user.getProperty("email");
                                    get_birthday = user.getBirthday();
                                    imageURL = "https://graph.facebook.com/" + get_id + "/picture?type=large";
                                    Transformation tm = new Transformation() {
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
                                    };

                                    Picasso.with(Tab4.this.getActivity()).load(imageURL).into(image_profile);
                                    String imgBg = "http://marketbike.zoaish.com/public/images/default_bg.jpg";
                                    Picasso.with(Tab4.this.getActivity()).load(imgBg).transform(tm).into(imageBgProfile);
                                    txtFullName.setText(get_name);


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        } else if (state.isClosed()) {
            Log.i("fb", "Logged out...");
            profileConsole.setVisibility(View.GONE);
            tab4.setPadding(0, 20, 0, 0);
        }

    }


}