package com.sarasi1.sumiyuru1.surveySS1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;


public class Login extends AppCompatActivity {
  LoginButton loginButton;

    TextView name,email1;
    CallbackManager callbackManager;
    ProfilePictureView profilePictureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton=findViewById(R.id.login_button);
        name=findViewById(R.id.textView);
        email1=findViewById(R.id.textView2);

        profilePictureView=findViewById(R.id.ProfilePicture);
        byte[] decodedString = Base64.decode(String.valueOf(R.drawable.com_facebook_button_icon), Base64.NO_WRAP);
        InputStream input=new ByteArrayInputStream(decodedString);
        Bitmap ext_pic = BitmapFactory.decodeStream(input);
        profilePictureView.setDefaultProfilePicture(ext_pic);
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));

        checkLoginStatus();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker accessTokenTracker= new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            if (currentAccessToken==null){
                name.setText("");
                email1.setText("");
                byte[] decodedString = Base64.decode(String.valueOf(R.drawable.com_facebook_button_icon), Base64.NO_WRAP);
                InputStream input=new ByteArrayInputStream(decodedString);
                Bitmap ext_pic = BitmapFactory.decodeStream(input);
                profilePictureView.setDefaultProfilePicture(ext_pic);
                Toast.makeText(Login.this, "User Logged out", Toast.LENGTH_SHORT).show();
            }else {
                loadUserProfile(currentAccessToken);
            }
        }
    };

    private  void loadUserProfile(AccessToken newAccessToken){

        GraphRequest request =GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {

                    String first_name=object.getString("first_name");
                    String last_name=object.getString("last_name");
                    String email=object.getString("email");
                    String id=object.getString("id");

                    URL imageURL = null;


                    name.setText(first_name+"   "+last_name);
                    email1.setText(email);
                    try {
                        profilePictureView.setProfileId(id);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }





                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields","first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void checkLoginStatus(){
        if (AccessToken.getCurrentAccessToken()!=null){
            loadUserProfile(AccessToken.getCurrentAccessToken());
        }
    }


}
