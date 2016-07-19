package pl.eduweb.podcastplayer.screens.login;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.eduweb.podcastplayer.App;
import pl.eduweb.podcastplayer.MainActivity;
import pl.eduweb.podcastplayer.R;
import pl.eduweb.podcastplayer.screens.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.emailEditText)
    EditText emailEditText;
    @BindView(R.id.passwordEditText)
    EditText passwordEditText;
    @BindView(R.id.loginButton)
    TextView loginButton;
    @BindView(R.id.registerButton)
    TextView registerButton;
    @BindView(R.id.emailTextInputLayout)
    TextInputLayout emailTextInputLayout;
    @BindView(R.id.passwordTextInputLayout)
    TextInputLayout passwordTextInputLayout;

    @Inject
    LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        App.component.inject(this);

        Log.d(TAG, "LoginManager:" + loginManager);


    }

    @Override
    protected void onStart() {
        super.onStart();
        loginManager.onAttach(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        loginManager.onStop();
    }

    @OnClick(R.id.registerButton)
    public void registerClicked() {

        startActivity(new Intent(this, RegisterActivity.class));
    }

    @OnClick(R.id.loginButton)
    public void loginClicked() {

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean hasErrors = false;

        if (email.isEmpty()) {
            emailTextInputLayout.setError(getString(R.string.this_field_cant_be_empty));
            hasErrors = true;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTextInputLayout.setError(getString(R.string.not_an_email));
            hasErrors = true;
        } else {
            emailTextInputLayout.setError(null);
        }
        if (password.isEmpty()) {
            passwordTextInputLayout.setError(getString(R.string.this_field_cant_be_empty));
            hasErrors = true;
        } else if (password.length() < 6) {
            passwordTextInputLayout.setError(getString(R.string.password_too_short));
            hasErrors = true;
        } else {
            passwordTextInputLayout.setError(null);
        }

        if (!hasErrors) {
            loginManager.login(email, password);

        }

    }

    public void loginSuccess() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    public void showError(String error) {
        Toast.makeText(LoginActivity.this, "Error:" + error, Toast.LENGTH_SHORT).show();
    }

    public void showProgress(boolean progress) {
        loginButton.setEnabled(!progress);
    }
}
