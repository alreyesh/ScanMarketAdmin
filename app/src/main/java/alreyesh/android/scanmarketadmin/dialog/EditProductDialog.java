package alreyesh.android.scanmarketadmin.dialog;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alreyesh.android.scanmarketadmin.Model.Category;
import alreyesh.android.scanmarketadmin.R;
import alreyesh.android.scanmarketadmin.Utils.Util;

public class EditProductDialog extends DialogFragment {
    Context context;
    EditText editCodProduct;
    EditText editNameProduct;
    EditText editDescProduct;
    EditText editPriceProduct;
    EditText editDescuento;
    Spinner spinnerCategories;
    Button img_select_button;
    Button btnRegisterProduct;
    ImageView img_product;
    SharedPreferences prefs;
    FirebaseFirestore db;
    private Uri filePath;
    private StorageReference mStorageRef;
    List<String> categorias;
    Map<String, Object> map;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.edit_product_layout  , null);
          UI( v);
        String id = Util.getidProduct(prefs);
        String nombre = Util.getnombreProduct(prefs);
        String imagen = Util.getimagenProduct(prefs);
        String descripcion = Util.getDescripcionProduct(prefs);
        String codigo = Util.getcodigoProduct(prefs);
        String precio = Util.getprecioProduct(prefs);
        String descuento = Util.getdescuentoProduct(prefs);
        setear(nombre,imagen,descripcion,codigo,precio,descuento);
        loadCategories();
        btnRegisterProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarproducto(id,nombre,imagen,descripcion,codigo,precio,descuento);

            }
        });





        builder.setView(v);

        return builder.create();


    }

    private void actualizarproducto(String id, String nombre, String imagen, String descripcion, String codigo, String precio, String descuento) {
        String name =   editNameProduct.getText().toString().trim();
        String des = editDescProduct.getText().toString().trim();
        String cod  = editCodProduct.getText().toString().trim();
        String pre = editPriceProduct.getText().toString().trim();
        String desc = editDescuento.getText().toString().trim();
        map = new HashMap<>();
        if(!cod.isEmpty())
            map.put("codigo",cod);
        if(!name.isEmpty())
            map.put("nombre",name);
       if(!des.isEmpty())
           map.put("descripcion",des);
       if(!pre.isEmpty()){
           float p = Float.parseFloat(pre);
           float d=0.0f;
           if(desc.isEmpty()){
               d =   Float.parseFloat(descuento);
           }else{
               d =  Float.parseFloat(desc);
           }


           if(p>d){
               map.put("precio",pre);
           }else{
               Toast.makeText(context, "El precio de producto no debe ser mayor al precio de descuento", Toast.LENGTH_SHORT).show();
           }
       }
       if(!desc.isEmpty()){
           float d = Float.parseFloat(desc);
           float p = 0.0f;
           if(pre.isEmpty()){
               p = Float.parseFloat(precio);
          }else{
               p = Float.parseFloat(pre);
           }

           if(p>d){
               map.put("descuento",desc);
           }else{
               Toast.makeText(context, "El precio de producto no debe ser mayor al precio de descuento", Toast.LENGTH_SHORT).show();
           }
       }
       if(filePath != null){
           StorageReference fotoRef = mStorageRef.child("FotosProducto").child(cod+"_"+filePath.getLastPathSegment());
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
                   if (task.isSuccessful()){
                       Uri downloadLink = task.getResult();
                       map.put("imagen",downloadLink.toString());
                   }
               }
           });
       }




        if(map.isEmpty()){
            Toast.makeText(context,"Ingresar Datos", Toast.LENGTH_SHORT).show();
        }else{
            db.collection("productos").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(context,"Se Actualizo el Producto", Toast.LENGTH_SHORT).show();
                }
            });
            Intent data = null;
            getTargetFragment().onActivityResult( 100, 100, data);
            dismiss();

        }







    }

    private void setear( String nombre, String imagen, String descripcion, String codigo, String precio, String descuento) {
        img_select_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/");
                startActivityForResult(i.createChooser(i,"Selecciona la aplicacion"),10);
            }
        });


        Picasso.get().load(imagen).fit().into(img_product);
        editCodProduct.setHint(codigo);
        editNameProduct.setHint(nombre);
        editDescProduct.setHint(descripcion);
        editPriceProduct.setHint(precio);
        editDescuento.setHint(descuento);









    }
    private void loadCategories(){

        CollectionReference ref =  db.collection("categoria");
          categorias = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(adapter);
        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document:task.getResult()){
                        String categoria = document.getString("nombre");
                        categorias.add(categoria);
                    }


                    String cate = Util.getCategoriaProduct(prefs);
                    for(int i = 0; i < categorias.size(); i++) {
                        String  c = categorias.get(i) ;

                        if( c.equals(cate)) {
                            spinnerCategories.setSelection(i);


                        }
                    }
                    adapter.notifyDataSetChanged();

                }
            }
        });






    }

    private void UI(View v) {
        db = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        editCodProduct =v.findViewById(R.id.editCodProduct);
        editNameProduct=v.findViewById(R.id.editNameProduct);
        editDescProduct=v.findViewById(R.id.editDescProduct);
        editPriceProduct=v.findViewById(R.id.editPriceProduct);
        editDescuento=v.findViewById(R.id.editDescuento);
        img_product = v.findViewById(R.id.img_product);
        img_select_button = v.findViewById(R.id.img_select_button);
        btnRegisterProduct = v.findViewById(R.id.btnRegisterProduct);
        spinnerCategories=v.findViewById(R.id.spinnerCategories);
        prefs = Util.getSP(getContext());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== RESULT_OK){
            Uri path = data.getData();
            filePath = data.getData();
            //  img_product.setImageURI(path);

            try {
                Bitmap bitmapImagen = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),path);
                img_product.setImageBitmap(bitmapImagen);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
}
