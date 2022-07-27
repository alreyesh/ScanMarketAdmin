package alreyesh.android.scanmarketadmin.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import alreyesh.android.scanmarketadmin.Adapters.ProductAdapter;
import alreyesh.android.scanmarketadmin.Model.Order;
import alreyesh.android.scanmarketadmin.Model.Product;
import alreyesh.android.scanmarketadmin.R;
import alreyesh.android.scanmarketadmin.Utils.Util;


public class CartFragment extends Fragment {
    TextView txtPagar;
    TextView textCodOrder;
    TextView txtDate;
    Button btnPagado;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView mRecycler;
    SharedPreferences prefs;
    private ProductAdapter adapter;
    private FirebaseFirestore mfirestore;
    public CartFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.fragment_cart, container, false);
        prefs = Util.getSP(getContext());
        txtPagar = view.findViewById(R.id.txtPagar);
        textCodOrder= view.findViewById(R.id.textCodOrder);
        txtDate = view.findViewById(R.id.txtDate);
        btnPagado = view.findViewById(R.id.btnPagado);
        mRecycler = view.findViewById(R.id.recyclerView);
        mfirestore = FirebaseFirestore.getInstance();
        mRecycler.setHasFixedSize(true);
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(mLayoutManager);
        String json = Util.getJson(prefs);
        convertJsonString(json);
    //    txtResultado.setText(order.getTotal());
       return view;
    }

    private void convertJsonString(String json) {
        Gson gson = new Gson();
        Order order = gson.fromJson(json, Order.class);
        textCodOrder.setText("COD: "+order.getCodorder());
        txtPagar.setText("Total a Pagar: S/."+order.getTotal());
        txtDate.setText("Fecha: "+order.getDate());
        ArrayList<Product> productos = new ArrayList<>();
        productos.addAll(order.getProductos());
        adapter = new ProductAdapter(productos, new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product, int position) {

            }
        });
        mRecycler.setAdapter(adapter);
Toast.makeText(getContext(),"r:  "+productos.get(0).getCod() ,Toast.LENGTH_SHORT).show();
        btnPagado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrder(order);
            }
        });

    }

    private void addOrder(Order o){
        Map<String, Object> map = new HashMap<>();
        map.put("codorder", o.getCodorder());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(o.getDate());
            Long  longtime = System.currentTimeMillis();
            map.put("user", o.getUser());
            map.put("total",o.getTotal());
            map.put("longtime",longtime);
            map.put("fecha",date);
            map.put("productos",o.getProductos());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        mfirestore.collection("order").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getActivity(), "Pagado Exitosamente", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error al Ingresar", Toast.LENGTH_SHORT).show();
            }
        });
    }


}