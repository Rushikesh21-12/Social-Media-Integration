package com.ruhsyu.socialmediaintegration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class AfterFacebookLogin extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();

    private TextView tvName, tvEmail;
    private ProfilePictureView ivProfile;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_facebook_login);

        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);
        ivProfile = findViewById(R.id.iv_profile);
        btnLogout = findViewById(R.id.btn_logout);

        updateUI(user);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                LoginManager.getInstance().logOut();
                startActivity(new Intent(AfterFacebookLogin.this, MainActivity.class));
                finish();
            }
        });
    }

    private void updateUI(FirebaseUser user) {

        if (user != null){
            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    try {
                        String firstName = object.getString("first_name");
                        String lastName = object.getString("last_name");
                        String email = object.getString("email");
                        String id = object.getString("id");

                        tvName.setText("Name : " + firstName + " " + lastName);
                        tvEmail.setText("Email : " + email);
                        ivProfile.setVisibility(View.VISIBLE);
                        ivProfile.setProfileId(id);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            Bundle bundle = new Bundle();
            bundle.putString("fields","first_name,last_name,email,id");
            request.setParameters(bundle);
            request.executeAsync();

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (user == null){
            startActivity(new Intent(AfterFacebookLogin.this,MainActivity.class));
            finish();
        }
    }
}