package com.example.kayakquest.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.example.kayakquest.R;

public class FloatPlanFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_float_plan, container, false);

        EditText etName = view.findViewById(R.id.et_name);
        EditText etDestination = view.findViewById(R.id.et_destination);
        EditText etStartTime = view.findViewById(R.id.et_start_time);
        Button btnSubmit = view.findViewById(R.id.btn_submit);

        btnSubmit.setOnClickListener(v ->
        {
            String name = etName.getText().toString();
            String destination = etDestination.getText().toString();
            String startTime = etStartTime.getText().toString();
            if (!name.isEmpty() && !destination.isEmpty() && !startTime.isEmpty())
            {
                Toast.makeText(getContext(), "Float Plan Submitted", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}