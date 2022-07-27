package alreyesh.android.scanmarketadmin.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import alreyesh.android.scanmarketadmin.Adapters.ListProductAdapter;
import alreyesh.android.scanmarketadmin.Model.ListProduct;
import alreyesh.android.scanmarketadmin.Model.Product;
import alreyesh.android.scanmarketadmin.R;
import alreyesh.android.scanmarketadmin.dialog.EditProductDialog;

public class ListProductFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<ListProduct> productsList;
    RecyclerView.LayoutManager mLayoutManager;
    private ListProductAdapter adapter;
    private FirebaseFirestore db;
    ProgressDialog pd = null;

    public ListProductFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd= new ProgressDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_list_product, container, false);
        recyclerView = v.findViewById(R.id.recyclerViewProducts);
        productsList = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new LinearLayoutManager(getActivity());
        db = FirebaseFirestore.getInstance();
        recyclerView.setLayoutManager(mLayoutManager);

        loadProducts();


        return v;
    }

    private void loadProducts() {
        pd.setTitle("Cargando Lista");
        pd.show();
        db.collection("productos").get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()){
                if(productsList !=null)
                    productsList.clear();
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d : list) {
                     // after getting this list we are passing
                    // that list to our object class.
                    ListProduct products = d.toObject(ListProduct.class);
                    products.setId(d.getReference().getId());
                    // after getting data from Firebase
                    // we are storing that data in our array list
                    productsList.add(products);

                }
                adapter = new ListProductAdapter(productsList, new ListProductAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(ListProduct product, int position) {
                        EditProductDialog editProductDialog = new EditProductDialog();
                        editProductDialog.setTargetFragment( ListProductFragment.this, 100);
                        editProductDialog.show(getActivity().getSupportFragmentManager(), "editproductdialog");

                    }
                });
                recyclerView.setAdapter( adapter);
                adapter.notifyDataSetChanged();

            }else{
                productsList.clear();
                Toast.makeText(getActivity(),"No hay productos Añadidos",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"Error en los datos",Toast.LENGTH_SHORT).show();

            }
        });
        pd.dismiss();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            loadProducts();
        }


    }
}