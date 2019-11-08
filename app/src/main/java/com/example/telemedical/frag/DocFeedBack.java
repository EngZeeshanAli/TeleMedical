package com.example.telemedical.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedical.R;
import com.example.telemedical.adapter.DocFeedBackAdapter;

public class DocFeedBack extends Fragment {
    RecyclerView docFeedBack;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_doc_feedback,container,false);
        docFeedBack=view.findViewById(R.id.doc_feedback_view);
        docFeedBack.setLayoutManager(new LinearLayoutManager(getActivity()));
        docFeedBack.setAdapter(new DocFeedBackAdapter());
        return view;

    }
}
