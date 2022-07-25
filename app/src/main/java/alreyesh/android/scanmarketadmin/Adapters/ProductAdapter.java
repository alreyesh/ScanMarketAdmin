package alreyesh.android.scanmarketadmin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import alreyesh.android.scanmarketadmin.Model.Product;
import alreyesh.android.scanmarketadmin.R;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context context;

    private  List<Product> products;

    public ProductAdapter(List<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_product,parent,false);
        ViewHolder vh = new ViewHolder(v);


        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull   ViewHolder holder, int position) {
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProductView;
        TextView textCodigo;
        TextView textViewName;
        TextView textViewCant;
        TextView textViewPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             imgProductView =  itemView.findViewById(R.id.imgProductView);
              textCodigo = itemView.findViewById(R.id.textCodigo);
              textViewName = itemView.findViewById(R.id.textViewName);
            textViewCant = itemView.findViewById(R.id.textViewCant);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);

        }
        public void bind(final Product products){
            Picasso.get().load(products.getLink()).fit().into(imgProductView);
            textViewName.setText(products.getName());
            textViewPrice.setText("S/. "+products.getSubtotal());
            textCodigo.setText("und: "+products.getCod());
            textViewCant.setText(products.getCantidad());
        }
    }
}
