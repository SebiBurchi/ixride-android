package keysight.ixia.hackathon.ixride;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import keysight.ixia.hackathon.ixride.auth.AuthenticationHolder;
import keysight.ixia.hackathon.ixride.model.RetroUser;
import keysight.ixia.hackathon.ixride.retrofit.RetrofitAPIService;
import keysight.ixia.hackathon.ixride.service.UserService;
import keysight.ixia.hackathon.ixride.service.implementation.UserServiceImpl;

public class LoginActivity extends AppCompatActivity {

    private EditText userName;
    private EditText userPassword;
    private TextView registerLinkView;
    private Button signInBtn;

    private UserService userService = new UserServiceImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            String actionMade = (String) b.get("action");
            switch (actionMade) {
                case "deleteAccount":
                    Toast.makeText(getApplicationContext(), "The account was deleted!", Toast.LENGTH_SHORT).show();
                    break;
                case "createAccount":
                    Toast.makeText(getApplicationContext(), "The account was created!", Toast.LENGTH_SHORT).show();
                    break;
                case "Logout":
                    Toast.makeText(getApplicationContext(), "Session lost!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        userName = (EditText) findViewById(R.id.loginUserName);
        userPassword = (EditText) findViewById(R.id.loginUserPassword);
        registerLinkView = (TextView) findViewById(R.id.loginRegisterLink);
        signInBtn = (Button) findViewById(R.id.loginSignInButton);

        registerLinkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegisterForm();
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        userName.setError(null);
        userPassword.setError(null);

        String username = userName.getText().toString();
        String password = userPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(username)) {
            userName.setError("This field is required!");
            focusView = userName;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            userPassword.setError("This field is required!");
            focusView = userPassword;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            RetrofitAPIService retrofitAPIService = RetrofitAPIService.aRetrofitApiService();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    RetroUser retroUser = new RetroUser(username, password);
                    RetroUser authUser = retrofitAPIService.authUser(retroUser);
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (authUser.getId() != null) {
                                SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
                                AuthenticationHolder.getInstance().setAuthUser(authUser, sharedPreferences);
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "Credentials are incorrect", Toast.LENGTH_SHORT).show();
                            }
                        }
                    };
                    LoginActivity.this.runOnUiThread(runnable);
                }
            }).start();


        }
    }

    private void goToRegisterForm() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }


}
