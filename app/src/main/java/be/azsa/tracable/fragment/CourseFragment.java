package be.azsa.tracable.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import be.azsa.tracable.R;

public class CourseFragment extends Fragment {

    String idOrder, departure, arrival, datetime, infos, status, user_id;

    TextView course_departure, course_user, course_arrival, course_infos;
    Button course_pab, course_ncp, course_call;

    public CourseFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //bundle enchere
        if (getArguments() != null) {
            idOrder = getArguments().getString("idOrder");
            departure = getArguments().getString("departure");
            arrival = getArguments().getString("arrival");
            datetime = getArguments().getString("datetime");
            infos = getArguments().getString("infos");
            status = getArguments().getString("status");
            user_id = getArguments().getString("user");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);

        //TextView
        course_departure = (TextView) view.findViewById(R.id.tv_course_departure);
        course_user = (TextView) view.findViewById(R.id.tv_course_user);
        course_arrival = (TextView) view.findViewById(R.id.tv_course_arrival);
        course_infos = (TextView) view.findViewById(R.id.tv_course_infos);

        //Button
        course_ncp = (Button) view.findViewById(R.id.btn_course_ncp);
        course_pab = (Button) view.findViewById(R.id.btn_course_pab);
        course_call = (Button) view.findViewById(R.id.btn_course_call);

        courseVue();

        //Button Listener
        course_ncp.setOnClickListener(course_ncp_listener);
        course_pab.setOnClickListener(course_pab_listener);
        course_call.setOnClickListener(course_call_listener);

        return view;
    }

    //OnClickListener NCP
    private View.OnClickListener course_ncp_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Ne charge pas");
            builder.setMessage("Confirmation 'ne charge pas' ?");

            builder.setPositiveButton("Oui", di_ncp);

            builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    };

    //OnClickListener PAB
    private View.OnClickListener course_pab_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(course_pab.getText().toString().equals("PàB")){
                //Pop up confirmation
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Passager à bord");
                builder.setMessage("Confirmation que le passager est à bord ?");

                builder.setPositiveButton("Oui", di_pab);

                builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            } else if(course_pab.getText().toString().equals("FdC")){

                //Pop up confirmation
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Fin de course");
                builder.setMessage("Confirmation de fin de course ?");

                builder.setPositiveButton("Oui", di_fdc);

                builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    };

    //OnClickListener CALL
    private View.OnClickListener course_call_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String[] user_parts = user_id.split(",");

            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tél:"+user_parts[5].substring(9).replaceAll("[\"}]","")));

            startActivity(intent);
        }
    };

    //Affichage vue course
    private void courseVue(){
        String[] departure_parts = departure.split(",");
        course_departure.setText(" "+departure_parts[0]+"\n"
                +departure_parts[1]);

        String[] user_parts = user_id.split(",");
        course_user.setText(user_parts[1].substring(13).replaceAll("[\"}]","").toUpperCase()
                +" "+user_parts[2].substring(12).replaceAll("[\"}]","").toUpperCase()
                +"\nNuméro de téléphone : "+user_parts[5].substring(9).replaceAll("[\"}]",""));

        String[] arrival_parts = arrival.split(",");
        course_arrival.setText(arrival_parts[0]+"\n"+arrival_parts[1]);

        if(!infos.isEmpty()){
            course_infos.setText(infos);
        } else{
            course_infos.setText("Pas d'informations");
        }
    };

    //DialogInterface confirmation "YES" pàb
    private DialogInterface.OnClickListener di_pab = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String putUrl = getString(R.string.localhost)+"/order/"+idOrder+"/edit";
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

            //update status de la course IN_PROGRESS->ON_BOARD
            JSONObject putData = new JSONObject();
            try {
                putData.put("idOrder", idOrder);
                putData.put("departure", departure);
                putData.put("arrival", arrival);
                putData.put("datetime", datetime);
                putData.put("infos", infos);
                putData.put("status", "ON_BOARD");
                putData.put("user_id", user_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //modification du status de la course ON_BOARD to DONE
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

            //Set PaB to FdC (fin de course)
            course_pab.setText("FdC");
            course_pab.setCompoundDrawables(null,
                    getResources().getDrawable(R.drawable.ic_course_end),
                    null,
                    null);

            //Disabled NCP & CALL
            course_call.setEnabled(false);
            course_ncp.setEnabled(false);
        }
    };

    //DialogInterface confirmation "YES" fdc
    private DialogInterface.OnClickListener di_fdc = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            course_call.setEnabled(true);
            course_ncp.setEnabled(true);

            //Set PaB to FdC (fin de course)
            course_pab.setText("Pàb");
            course_pab.setCompoundDrawables(null,
                    getResources().getDrawable(R.drawable.ic_onboard),
                    null,
                    null);

            String putUrl = getString(R.string.localhost)+"/order/"+idOrder+"/edit";
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

            //update status de la course ON_BOARD to DONE
            JSONObject putData = new JSONObject();
            try {
                putData.put("idOrder", idOrder);
                putData.put("departure", departure);
                putData.put("arrival", arrival);
                putData.put("datetime", datetime);
                putData.put("infos", infos);
                putData.put("status", "DONE");
                putData.put("user_id", user_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //modification du status de la course ON_BOARD to DONE
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
                    .replace(R.id.fragment_container,new FirstFragment())
                    .commit();
        }
    };

    //DialogInterface confirmation "YES" ncp
    private DialogInterface.OnClickListener di_ncp = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String putUrl = getString(R.string.localhost)+"/order/"+idOrder+"/edit";
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

            //update status de la course IN_PROGRESS->ON_BOARD
            JSONObject putData = new JSONObject();
            try {
                putData.put("idOrder", idOrder);
                putData.put("departure", departure);
                putData.put("arrival", arrival);
                putData.put("datetime", datetime);
                putData.put("infos", infos);
                putData.put("status", "CANCELLED");
                putData.put("user_id", user_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //modification du status de la course ON_BOARD to DONE
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
                    .replace(R.id.fragment_container,new FirstFragment())
                    .commit();
        }
    };
}