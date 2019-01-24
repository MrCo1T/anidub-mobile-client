package ru.mrcolt.anidubmobile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.mrcolt.anidubmobile.R;


public class DemoFragment extends Fragment {
    private String demoParam;

    public DemoFragment() {
    }

    public static DemoFragment newInstance(String param) {
        DemoFragment fragment = new DemoFragment();
        Bundle args = new Bundle();
        args.putString("demoParam", param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        demoParam = getArguments().getString("demoParam");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_demo, container, false);
        TextView tvLabel = view.findViewById(R.id.demo_fragment_text);
        tvLabel.setText(demoParam);
        return view;
    }
}
