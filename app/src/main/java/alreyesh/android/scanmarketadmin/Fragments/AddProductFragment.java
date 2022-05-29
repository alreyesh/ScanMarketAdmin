package alreyesh.android.scanmarketadmin.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import alreyesh.android.scanmarketadmin.Model.Category;
import alreyesh.android.scanmarketadmin.R;


public class AddProductFragment extends Fragment {
    private Spinner mSpinnerCategories;
    private FirebaseFirestore mfirestore;
    public AddProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);
        // Inflate the layout for this fragment
        mfirestore = FirebaseFirestore.getInstance();
        CollectionReference ref =  mfirestore.collection("categoria");
        mSpinnerCategories = view.findViewById(R.id.spinnerCategories);
        List<String> categorias = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCategories.setAdapter(adapter);
        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document:task.getResult()){
                        String categoria = document.getString("nombre");
                        categorias.add(categoria);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        return view;
    }


}