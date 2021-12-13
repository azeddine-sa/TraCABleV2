package be.azsa.tracable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import be.azsa.tracable.model.Driver;

public class LoginActivity extends AppCompatActivity {

    private EditText txtDriverNum;
    private EditText txtPassword;
    private Button btnConnexion;

    private String driverNum;
    private String driverPassword;

    Driver driver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    //initialisation
    private void init(){
        txtDriverNum = (EditText) findViewById( R.id.txtDriverNum);
        txtPassword = (EditText) findViewById( R.id.txtPassword);
        btnConnexion = (Button) findViewById( R.id.btnConnexion);

        //association button avec le listener
        btnConnexion.setOnClickListener(btnConnexionListener);
    }

    // Gestionnaire d'évènement (listener)
    private View.OnClickListener btnConnexionListener = new View.OnClickListener() {
        //valeur d'initialisation du listener btnConnexionListener
        @Override
        public void onClick(View v){
            //Donnée de connexion introduite par l'utilisateur
            driverNum = txtDriverNum.getText().toString();
            driverPassword = txtPassword.getText().toString();

            Log.i("DEBBUG", driverNum+" "+driverPassword);

            //Volley connexion API
            String getUrl = getString(R.string.localhost)+"/driver/byDriverNum/"+driverNum;
            Log.i("DEBBUG", getUrl);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, getUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("driver",response);
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                String idDriver = jsonObject.getString("idDriver");
                                String firstname = jsonObject.getString("firstname");
                                String lastname= jsonObject.getString("lastname");
                                String email = jsonObject.getString("email");
                                String password = jsonObject.getString("password");
                                String phone = jsonObject.getString("phone");

                                Log.i("DEBBUG", driverPassword+" "+password);

                                if(driverPassword.equals(password)){
                                    Toast.makeText(getApplicationContext(), "Connexion réussie !",
                                            Toast.LENGTH_SHORT).show();
                                    //redirection vers la page de connexion
                                    Intent main = new Intent(getApplicationContext(), MainActivity.class);
                                    main.putExtra("idDriver", idDriver);
                                    main.putExtra("firstname", firstname);
                                    main.putExtra("lastname", lastname);
                                    main.putExtra("email", email);
                                    main.putExtra("phone", phone);
                                    main.putExtra("driverNum", driverNum);
                                    startActivity(main);
                                }else {
                                    Toast.makeText(getApplicationContext(), "Mot de passe incorrect",
                                            Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.i("DEBBUG", "l'erreur est ici !!!");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"Numero de chauffeur inconnu",Toast.LENGTH_LONG).show();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
            requestQueue.add(stringRequest);
        }
    };
}