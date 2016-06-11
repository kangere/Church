package material.kangere.com.tandaza;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import material.kangere.com.tandaza.LocalDB.SQLiteHandler;
import material.kangere.com.tandaza.NavActivities.MainActivity;

public class Register extends AppCompatActivity {

    private static final String TAG = Register.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFName, inputEmail,inputPassword,initpassword,inputLName;

    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputFName = (EditText) findViewById(R.id.fname);
        inputLName = (EditText) findViewById(R.id.lname);
        inputEmail = (EditText) findViewById(R.id.email);
        initpassword = (EditText) findViewById(R.id.password);
        inputPassword = (EditText) findViewById(R.id.Rpassword);
        btnRegister = (Button) findViewById(R.id.bRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(Register.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFName.getText().toString().trim();
                String lname = inputLName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String initPass = initpassword.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if(initPass.equals(password)) {
                    //not yet implemented last name registration
                    if (!name.isEmpty() && !lname.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                        registerUser(name, lname, email, password);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please enter your details!", Toast.LENGTH_LONG)
                                .show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Passwords do not match",Toast.LENGTH_LONG).show();
                }
            }
        });
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Register.this,Login.class);
                startActivity(i);
            }
        });
    }
    private void registerUser(final String name,final String l_name,final String email,
                              final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        /*StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                Log.i("tagconvertstr", "["+response+"]");
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String l_name = user.getString("l_name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(name,l_name, email, uid, created_at);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                       /* Intent intent = new Intent(
                                Register.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("l_name", l_name);
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        }; */

        // Adding request to request queue

        //RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        //queue.add(strReq);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
