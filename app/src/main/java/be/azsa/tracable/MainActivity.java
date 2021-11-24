package be.azsa.tracable;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import be.azsa.tracable.model.Driver;

public class MainActivity extends AppCompatActivity {

    private EditText txtDriverNum;
    private EditText txtPassword;
    private Button btnConnexion;

    private int driverNum;
    private String driverPassword;

    Driver driver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            driverNum = Integer.parseInt(txtDriverNum.getText().toString());
            driverPassword = txtPassword.getText().toString();

            //Volley connexion API
            String getUrl = "http://192.168.141.234:8080/api/driver/byDriverNum/"+driverNum;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, getUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("driver",response);
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                String idDriver = jsonObject.getString("idDriver");
                                String firstname = jsonObject.getString("firstname");
                                String lastname= jsonObject.getString("lastname");
                                String driverNum = jsonObject.getString("driverNum");
                                String email = jsonObject.getString("email");
                                String password = jsonObject.getString("password");
                                String phone = jsonObject.getString("phone");

                                driver.setIdDriver(Long.parseLong(idDriver));
                                driver.setPhone(phone);
                                driver.setDriverNum(Integer.parseInt(driverNum));
                                driver.setPassword(Integer.parseInt(password));
                                driver.setEmail(email);
                                driver.setLastname(lastname);
                                driver.setFirstname(firstname);

                                if(driverPassword.equals(driver.getPassword())){
                                    Toast.makeText(MainActivity.this,
                                            "Bienvenue "+driver.getLastname().toUpperCase(),
                                            Toast.LENGTH_SHORT).show();
                                    //redirection vers la page de connexion
                                    Intent main = new Intent(getApplicationContext(), MainActivity.class);
                                    main.putExtra("idUser", idDriver);
                                    main.putExtra("firstname", firstname);
                                    main.putExtra("lastname", lastname);
                                    main.putExtra("email", email);
                                    main.putExtra("phone", phone);
                                    main.putExtra("password", password);
                                    startActivity(main);
                                } else{
                                    Toast.makeText(MainActivity.this,
                                            "Mot de Passe Incorrect",
                                            Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"Numero de chauffeur inconnu",Toast.LENGTH_LONG).show();

                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(stringRequest);
        }
    };
}