package ayyash.app.seamolec_udj;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Abdul Rizal Adompo on 9/18/2016.
 */
public class Login extends AppCompatActivity {


    EditText nis, password;
    String id_kelas;
    Button btnLogin,btnRegister;
    private String ambilIP;
    private boolean loggedIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nis = (EditText)findViewById(R.id.nis);
        password = (EditText)findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button)findViewById(R.id.btnRegister);

        //ngambil IP
        SharedPreferences sps = getSharedPreferences("", MODE_PRIVATE);
         ambilIP = sps.getString("IPnya", "");

        Toast.makeText(Login.this, "Connecting to Server....: " + ambilIP, Toast.LENGTH_LONG).show();



      btnLogin.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              if(nis.getText().toString().isEmpty()&& password.getText().toString().isEmpty()){
                  Toast.makeText(getApplicationContext(),"username dan password tidak boleh kosong", Toast.LENGTH_SHORT).show();
              }else {
                  // cek dari db
                    login();

              }

          }
      });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Register.class);
                startActivity(i);
                finish();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
         loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);


        if(loggedIn){
            Intent intent = new Intent(Login.this, ListPaketSoal.class);
            startActivity(intent);
            finish();
        }
    }

    int ambilIDKelas;

    private void login(){
        final String nisA = nis.getText().toString().trim();
        final String passwordA =password .getText().toString().trim();




        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ambilIP+"/new_udj/login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            if(response.equalsIgnoreCase(Config.LOGIN_SUCCESS)){

                            SharedPreferences sharedPreferences = Login.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();



                            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                            editor.putString(Config.NIS_SHARED_PREF, nisA);









                            editor.commit();


                            Intent intent = new Intent(Login.this, ListPaketSoal.class);
                            startActivity(intent);
                        }else{

                            Toast.makeText(Login.this, "username/password salah /masalah koneksi ke server", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                params.put(Config.KEY_NIS, nisA);
                params.put(Config.KEY_PASSWORD, passwordA);



                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }



}
