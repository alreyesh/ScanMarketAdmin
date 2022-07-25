package alreyesh.android.scanmarketadmin.Model;
public class Product {
    private String cod;
    private String link;
    private String name;
    private String cantidad;
    private String subtotal;

    public Product(String cod, String link, String name, String cantidad, String subtotal) {
        this.cod = cod;
        this.link = link;
        this.name = name;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
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

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }
}
