package alreyesh.android.scanmarketadmin.Model;
public class Product {
    private String cod;
    private String link;
    private String name;
    private String descripcion;
    private String subtotal;

    private String cantidad;

    public Product(String codigo, String imagen, String nombre, String descripcion, String precio,String cantidad) {
        this.cod = codigo;
        this.link = imagen;
        this.name = nombre;
        this.descripcion = descripcion;
        this.subtotal = precio;

        this.cantidad = cantidad;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }


    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
}
