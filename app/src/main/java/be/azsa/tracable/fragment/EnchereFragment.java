package be.azsa.tracable.fragment;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import be.azsa.tracable.R;

public class EnchereFragment extends Fragment {

    TextView enchere_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enchere, container, false);

        enchere_list = (TextView) view.findViewById(R.id.tv_enchere_list);

        //recupération layout Enchere
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.enchere_layout);

        //recupération de la l'historique de l'utilisateur sur la BD
        String getUrl = getString(R.string.localhost)+"/orders";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
            (Request.Method.GET, getUrl, null, new Response.Listener<JSONArray>() {
                @RequiresApi(api = Build.VERSION_CODES.Q)
                @Override
                public void onResponse(JSONArray jsonArray) {
                    Log.i("DEBBUG", jsonArray.toString());
                    try {
                        for(int i=0; i<jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id_order = jsonObject.getString("idOrder");
                            String departure = jsonObject.getString("departure");
                            String arrival = jsonObject.getString("arrival");
                            String datetime = jsonObject.getString("datetime");
                            String infos = jsonObject.getString("infos");
                            String status = jsonObject.getString("status");
                            String user_id = jsonObject.getString("user");

                            //parse String datetime on Datetime
                            String[] datetime_parts = new String[2];
                            datetime_parts[0] = datetime.substring(0,10);
                            datetime_parts[1] = datetime.substring(11,19);
                            String dtp = datetime_parts[0]+" "+datetime_parts[1];
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            LocalDateTime dt = LocalDateTime.parse(dtp, formatter);

                            //Si la status de la course est SENT
                            //& la date&heure de la course est < à l'heure actuelle + 15min
                            if (status.equals("SENT") && dt.isBefore(LocalDateTime.now().plusMinutes(15))){
                                //Création d'un bouton
                                Button btn = new Button(getContext());

                                btn.setText(departure);
                                btn.setPadding(16,16,16,16);
                                btn.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                                btn.setBackgroundColor(Color.rgb(33,66,101));
                                btn.setTextColor(Color.WHITE);
                                btn.layout(0,0,0,10);
                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //Pop up confirmation
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setTitle("Enchères");
                                        builder.setMessage("Etes-vous sûr de vouloir prendre la course ?");

                                        //confirmation, renvoie fragment course
                                        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                CourseFragment courseFragment = new CourseFragment();

                                                Bundle bundle = new Bundle();
                                                bundle.putString("idOrder", id_order);
                                                bundle.putString("departure", departure);
                                                bundle.putString("arrival", arrival);
                                                bundle.putString("datetime", datetime);
                                                bundle.putString("infos", infos);
                                                bundle.putString("status", status);
                                                bundle.putString("user", user_id);

                                                courseFragment.setArguments(bundle);

                                                //update status de la course SENT->IN_PROGRESS
                                                //POST REST API
                                                String putUrl = getString(R.string.localhost)+"/order/"+id_order+"/edit";
                                                JSONObject putData = new JSONObject();
                                                try {
                                                    putData.put("idOrder", id_order);
                                                    putData.put("departure", departure);
                                                    putData.put("arrival", arrival);
                                                    putData.put("datetime", datetime);
                                                    putData.put("infos", infos);
                                                    putData.put("status", "IN_PROGRESS");
                                                    putData.put("user_id", user_id);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                //modification du status de la course sent en in_progress
                                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, putUrl, putData, new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        System.out.println(response);
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        error.printStackTrace();
                                                    }
                                                });

                                                requestQueue.add(jsonObjectRequest);

                                                getActivity().getSupportFragmentManager().beginTransaction()
                                                        .replace(R.id.fragment_container,courseFragment)
                                                        .addToBackStack(null)
                                                        .commit();
                                            }
                                        });

                                        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });

                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                });

                                ll.addView(btn);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    enchere_list.setText("Il n'y a pas d'enchères!");
                    Log.i("DEBBUG", error.getMessage());
                }
            });

        requestQueue.add(jsonArrayRequest);

        return view;
    }

}