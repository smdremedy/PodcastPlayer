package pl.eduweb.podcastplayer.screens.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.eduweb.podcastplayer.App;
import pl.eduweb.podcastplayer.MainActivity;
import pl.eduweb.podcastplayer.R;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.firstNameEditText)
    EditText firstNameEditText;
    @BindView(R.id.lastNameEditText)
    EditText lastNameEditText;
    @BindView(R.id.emailEditText)
    EditText emailEditText;
    @BindView(R.id.passwordEditText)
    EditText passwordEditText;

    private RegisterManager registerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        registerManager = ((App) getApplication()).getRegisterManager();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerManager.onAttach(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        registerManager.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.register, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_register) {
            tryToRegister();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void tryToRegister() {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        
        boolean hasErrors = false;
        
        if(firstName.isEmpty()) {
            firstNameEditText.setError(getString(R.string.this_field_cant_be_empty));
            hasErrors = true;
        }
        if(lastName.isEmpty()) {
            lastNameEditText.setError(getString(R.string.this_field_cant_be_empty));
            hasErrors = true;
        } else if (email.isEmpty()) {
            emailEditText.setError(getString(R.string.this_field_cant_be_empty));
            hasErrors = true;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError(getString(R.string.not_an_email));
            hasErrors = true;
        }
        if (password.isEmpty()) {
            passwordEditText.setError(getString(R.string.this_field_cant_be_empty));
            hasErrors = true;
        } else if (password.length() < 6) {
            passwordEditText.setError(getString(R.string.password_too_short));
            hasErrors = true;
        }
        
        if(!hasErrors) {
            registerManager.register(firstName, lastName, email, password);
        }
    }

    public void registerSuccessful() {

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void showError(String error) {

        Toast.makeText(RegisterActivity.this, "Error:" + error, Toast.LENGTH_SHORT).show();

    }

    public void showProgress(boolean inProgress) {

        // TODO: 2016-07-06  

    }
}
