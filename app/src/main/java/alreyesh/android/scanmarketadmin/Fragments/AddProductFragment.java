package alreyesh.android.scanmarketadmin.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alreyesh.android.scanmarketadmin.Model.Category;
import alreyesh.android.scanmarketadmin.R;
import android.provider.MediaStore.Images.Media;

public class AddProductFragment extends Fragment {
    private Spinner mSpinnerCategories;
    private EditText editCodProduct,editNameProduct,editDescProduct,editPriceProduct,editDescuentoProduct;
    private Button btnRegisterProduct,img_select_button;
    private ImageView img_product;
    private Uri filePath = null;
    private FirebaseFirestore mfirestore;
    private StorageReference mStorageRef;

    private ProgressDialog pd;
    public AddProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd= new ProgressDialog(getContext());
        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();

                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== RESULT_OK){
            Uri path = data.getData();
            filePath = data.getData();
          //  img_product.setImageURI(path);
            Toast.makeText(getContext(),"foto: "+filePath,Toast.LENGTH_SHORT).show();
            try {
                Bitmap bitmapImagen = Media.getBitmap(getActivity().getContentResolver(),path);
                img_product.setImageBitmap(bitmapImagen);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);
        // Inflate the layout for this fragment
        mfirestore = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        //[Producto]
        editCodProduct = (EditText) view.findViewById(R.id.editCodProduct);
        editNameProduct= (EditText)view.findViewById(R.id.editNameProduct);
        editDescProduct = (EditText)view.findViewById(R.id.editDescProduct);
        editPriceProduct = (EditText)view.findViewById(R.id.editPriceProduct);
        editDescuentoProduct = (EditText)view.findViewById(R.id.editDescuento);
        btnRegisterProduct = (Button) view.findViewById(R.id.btnRegisterProduct);
        img_select_button = (Button)view.findViewById(R.id.img_select_button);
        img_product =(ImageView) view.findViewById(R.id.img_product);


        //[End/Producto]
        //[Categoria]
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

        //[End/Categoria]
        //[Image]
        img_select_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/");
                startActivityForResult(i.createChooser(i,"Selecciona la aplicacion"),10);
            }
        });

        //[End/Image]
        btnRegisterProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setTitle("Añadiendo Producto..");
                pd.show();
                String  textSpinner = mSpinnerCategories.getSelectedItem().toString();
                String  textCod = editCodProduct.getText().toString().trim();
                String textName = editNameProduct.getText().toString().trim();
                String textDesc = editDescProduct.getText().toString().trim();
                String  textPrice = editPriceProduct.getText().toString().trim();
                String textDescuento = editDescuentoProduct.getText().toString().trim();


                    if(textCod ==""|| textCod==null||textName ==""||textName==null||textDesc==""||textDesc==null||textPrice==""||textPrice==null|| textDescuento=="" || textDescuento==null){
                        Toast.makeText(getContext(), "Rellenar todos los campos", Toast.LENGTH_SHORT).show();

                    }else{
                        Float precio = Float.parseFloat(textPrice);
                        Float descuento = Float.parseFloat(textDescuento);
                        if(precio > descuento){
                            if (filePath != null) {

                                        StorageReference fotoRef = mStorageRef.child("FotosProducto").child(textCod+"_"+filePath.getLastPathSegment());
                                        fotoRef.putFile(filePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                            @Override
                                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                if (!task.isSuccessful()) {
                                                    throw new Exception();
                                                }
                                                return fotoRef.getDownloadUrl();
                                            }
                                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                           public void onComplete(@NonNull Task<Uri> task) {
                                                if (task.isSuccessful()) {
                                                    Map<String, Object> map = new HashMap<>();
                                                    Uri downloadLink = task.getResult();
                                                    map.put("codigo", textCod.toLowerCase());
                                                    map.put("nombre", textName.toLowerCase());
                                                    map.put("descripcion", textDesc);
                                                    map.put("precio", textPrice);
                                                    map.put("categoria", textSpinner);
                                                    map.put("descuento",textDescuento);
                                                    map.put("imagen", downloadLink.toString());
                                                    mfirestore.collection("productos").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            editCodProduct.setText("");
                                                            editNameProduct.setText("");
                                                            editDescProduct.setText("");
                                                            editPriceProduct.setText("");
                                                            editDescuentoProduct.setText("");
                                                            String uri = "@drawable/ic_camera";
                                                            int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
                                                            Drawable res = getResources().getDrawable(imageResource);
                                                            img_product.setImageDrawable(res);
                                                            pd.dismiss();
                                                            Toast.makeText(getActivity(), "Creado Exitosamente", Toast.LENGTH_SHORT).show();

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            pd.dismiss();
                                                            Toast.makeText(getActivity(), "Error al Ingresar", Toast.LENGTH_SHORT).show();

                                                        }
                                                    });


                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                pd.dismiss();
                                            }
                                        });
                            }
                            else{
                                        Toast.makeText(getContext(), "Seleccionar una Foto para el Producto", Toast.LENGTH_SHORT).show();

                            }
                        }else{
                            Toast.makeText(getContext(), "El precio del producto tiene que ser mayor al precio de descuento", Toast.LENGTH_SHORT).show();
                        }
                    }





            }
        });

        return view;
    }


}