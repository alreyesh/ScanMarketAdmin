package alreyesh.android.scanmarketadmin.Model;

import com.google.gson.stream.JsonReader;
import com.google.type.Date;


import java.util.ArrayList;

public class Order {
    private String codorder;
    private String user;
    private String date;
    private String total;
    private ArrayList<Product> productos;

    public Order(String codorder,String user, String  date, String total, ArrayList<Product> productos) {
       this.codorder = codorder;
        this.user = user;
        this.date = date;
        this.total = total;
        this.productos = productos;
    }

    public String getCodorder() {
        return codorder;
    }

    public void setCodorder(String codorder) {
        this.codorder = codorder;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String  getDate() {
        return date;
    }

    public void setDate(String  date) {
        this.date = date;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public ArrayList<Product> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<Product> productos) {
        this.productos = productos;
    }
}
