package com.example.sharecare;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addChildFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addChildFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ChildFragmentListener listener;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public addChildFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addChildFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static addChildFragment newInstance(String param1, String param2) {
        addChildFragment fragment = new addChildFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public interface ChildFragmentListener {
        void onChildDataAdded(String name, int age, String gender, String schoolName);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_child, container, false);
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ChildFragmentListener) {
            listener = (ChildFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ChildFragmentListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void sendDataToParentActivity(String name, int age, String gender, String schoolName) {
        if (listener != null) {
            listener.onChildDataAdded(name, age, gender, schoolName);
        }
    }

    // Method to send data to the parent activity, you can call this when needed
    private void sendDataToParent() {
        String name = "Child's Name"; // Replace this with the actual child's name
        int age = 8; // Replace this with the actual child's age
        String gender = "Male"; // Replace this with the actual child's gender
        String schoolName = "ABC School"; // Replace this with the actual school name

        sendDataToParentActivity(name, age, gender, schoolName);
    }
}