package keysight.ixia.hackathon.ixride;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private EditText userName;
    private EditText userPassword;
    private TextView registerLinkView;
    private Button signInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void goToRegisterForm() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
