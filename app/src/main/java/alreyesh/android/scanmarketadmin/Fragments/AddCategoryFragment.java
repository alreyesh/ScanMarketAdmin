package alreyesh.android.scanmarketadmin.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alreyesh.android.scanmarketadmin.Adapters.CategoryAdapter;
import alreyesh.android.scanmarketadmin.Model.Category;
import alreyesh.android.scanmarketadmin.R;

public class AddCategoryFragment extends Fragment {
    private Button btn_add;
    private EditText name_cat;
    private FirebaseFirestore mfirestore;
    private Task<QuerySnapshot> reff;
    private int counter;
    private RecyclerView mRecycler;
    private RecyclerView.LayoutManager mLayoutManager;
    private CategoryAdapter mAdapter;

    public AddCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_category, container, false);



        mLayoutManager = new LinearLayoutManager(getContext());


        mfirestore = FirebaseFirestore.getInstance();
        mRecycler = view.findViewById(R.id.recyclerview);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        Query query =  mfirestore.collection("categoria").orderBy("catid", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Category> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Category>().setQuery(query,Category.class).build();
        mAdapter = new CategoryAdapter(firestoreRecyclerOptions);

        mRecycler.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mAdapter.deleteItem(viewHolder.getLayoutPosition());
            }
        }).attachToRecyclerView(mRecycler);


        name_cat =(EditText)view.findViewById(R.id.editNameCategory);
        btn_add= (Button) view.findViewById(R.id.btnRegistrarCategory);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namecat = name_cat.getText().toString().trim();

                reff = FirebaseFirestore.getInstance().collection("categoria").orderBy("catid", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String count = null;
                        Category categorias;
                        if(task.isSuccessful()){

                            for(DocumentSnapshot document : task.getResult()){
                                count = document.getData().get("catid").toString();

                            }

                            counter=Integer.parseInt(count);
                            Toast.makeText(getActivity(),"count:"+ counter,Toast.LENGTH_SHORT).show();

                        }else{

                        }
                    }
                });


                if(namecat.isEmpty()){
                    Toast.makeText(getActivity(),"Ingrese Nombre de Categoria",Toast.LENGTH_SHORT).show();
                }else{
                    postCat(namecat,counter);
                }
            }
        });




        mAdapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
     mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    private void postCat(String namecat,int counter) {

        Map<String, Object> map = new HashMap<>();

        map.put("catid", counter+1);
        map.put("nombre",namecat);


        mfirestore.collection("categoria").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                name_cat.setText("");
                mRecycler.scrollToPosition(0);
                Toast.makeText(getActivity(),"Creado Exitosamente",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"Error al Ingresar",Toast.LENGTH_SHORT).show();

            }
        });
    }
}