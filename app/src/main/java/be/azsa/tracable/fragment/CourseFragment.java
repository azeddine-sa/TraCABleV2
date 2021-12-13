package be.azsa.tracable.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import be.azsa.tracable.R;

public class CourseFragment extends Fragment {

    String departure, arrival, datetime, infos, user_id;

    TextView course_departure, course_user, course_arrival, course_infos;

    public CourseFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);

        course_departure = (TextView) view.findViewById(R.id.tv_course_departure);
        course_user = (TextView) view.findViewById(R.id.tv_course_user);
        course_arrival = (TextView) view.findViewById(R.id.tv_course_arrival);
        course_infos = (TextView) view.findViewById(R.id.tv_course_infos);

        if (getArguments() != null) {
            departure = getArguments().getString("departure");
            arrival = getArguments().getString("arrival");
            datetime = getArguments().getString("datetime");
            infos = getArguments().getString("infos");
            user_id = getArguments().getString("user");
        }

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

        return view;
    }
}