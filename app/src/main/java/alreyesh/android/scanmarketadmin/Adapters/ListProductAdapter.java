package alreyesh.android.scanmarketadmin.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import alreyesh.android.scanmarketadmin.Model.ListProduct;
import alreyesh.android.scanmarketadmin.Model.Product;
import alreyesh.android.scanmarketadmin.R;
import alreyesh.android.scanmarketadmin.Utils.Util;

public class ListProductAdapter extends RecyclerView.Adapter<ListProductAdapter.ViewHolder> {
    private List<ListProduct> productList;
    private  OnItemClickListener itemListener;
    private Context context;
    private SharedPreferences prefs;
    public ListProductAdapter(List<ListProduct> productList,OnItemClickListener itemListener){
        this.productList = productList;
        this.itemListener = itemListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_products,parent,false);
       ViewHolder vh = new ViewHolder(v);
        context = parent.getContext();
        prefs = Util.getSP(context);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(productList.get(position),itemListener);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgProductView;
        TextView textViewName;
        TextView textViewCodigo;
        TextView textViewPrecio;
        TextView textViewDescuento;

        public ViewHolder(@NonNull View iv) {
            super(iv);
            imgProductView = iv.findViewById(R.id.imgProductView);
            textViewName =iv.findViewById(R.id.textViewName);
            textViewCodigo = iv.findViewById(R.id.textViewCodigo);
            textViewPrecio = iv.findViewById(R.id.textViewPrecio);
            textViewDescuento = iv.findViewById(R.id.textViewDescuento);


        }
        public void bind(final ListProduct products,final  OnItemClickListener itemListener){
            Picasso.get().load(products.getImagen()).fit().into(imgProductView);
            textViewName.setText(products.getNombre());
            textViewPrecio.setText("S/. "+products.getPrecio());
            textViewCodigo.setText("sku: "+products.getCodigo());
            textViewDescuento.setText("S/. "+products.getDescuento());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("idproduct",products.getId());
                    editor.putString("imagenproduct",products.getImagen());
                    editor.putString("descripcionproduct",products.getDescripcion());
                    editor.putString("nameproduct",products.getNombre());
                    editor.putString("codigoproduct",products.getCodigo());
                    editor.putString("precioproduct",products.getPrecio());
                    editor.putString("descuentoproduct",products.getDescuento());
                    editor.putString("categoriaproduct",products.getCategoria());
                    editor.commit();
                    itemListener.onItemClick(products,getBindingAdapterPosition());
                }
            });




        }





    }
    public interface OnItemClickListener {
        void onItemClick(ListProduct product, int position);
    }
}
