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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
                                String departure = jsonObject.getString("departure");
                                String arrival = jsonObject.getString("arrival");
                                String datetime = jsonObject.getString("datetime");
                                String infos = jsonObject.getString("infos");
                                String status = jsonObject.getString("status");
                                String user_id = jsonObject.getString("user");

                                if (status.equals("SENT")){
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
                                                    bundle.putString("departure", departure);
                                                    bundle.putString("arrival", arrival);
                                                    bundle.putString("datetime", datetime);
                                                    bundle.putString("infos", infos);
                                                    bundle.putString("user", user_id);

                                                    courseFragment.setArguments(bundle);

                                                    getActivity().getSupportFragmentManager().beginTransaction()
                                                            .replace(R.id.fragment_container,courseFragment)
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