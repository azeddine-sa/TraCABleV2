package be.azsa.tracable.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import be.azsa.tracable.R;

public class FirstFragement extends Fragment {
    Button btn_consult, btn_station, btn_enchere, btn_libocc, btn_activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        btn_consult = (Button) view.findViewById(R.id.btn_consult);
        btn_consult.setOnClickListener(btn_consult_listener);

        btn_station = (Button) view.findViewById(R.id.btn_station);
        btn_station.setOnClickListener(btn_station_listener);

        btn_activity = (Button) view.findViewById(R.id.btn_activity);
        btn_activity.setOnClickListener(btn_activity_listener);

        btn_enchere = (Button) view.findViewById(R.id.btn_encheres);
        btn_enchere.setOnClickListener(btn_enchere_listener);

        btn_libocc = (Button) view.findViewById(R.id.btn_libocc);
        btn_libocc.setOnClickListener(btn_libocc_listener);

        return view;
    }

    private View.OnClickListener btn_consult_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener btn_station_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener btn_activity_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener btn_enchere_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener btn_libocc_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
