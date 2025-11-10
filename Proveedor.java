package ERICK-S-CONVENIENCE-STORE;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un proveedor de la tienda
 * Issue #28: Gestión de Pedidos a Proveedores
 */
public class Proveedor {
    private int id;
    private String nombre;
    private String empresa;
    private String telefono;
    private String email;
    private List<String> productosQueSupministra;
    private boolean activo;

    public Proveedor(int id, String nombre, String empresa, String telefono, String email) {
        this.id = id;
        this.nombre = nombre;
        this.empresa = empresa;
        this.telefono = telefono;
        this.email = email;
        this.productosQueSupministra = new ArrayList<>();
        this.activo = true;
    }

    /**
     * Agrega un producto a la lista de productos que suministra
     */
    public void agregarProducto(String producto) {
        if (producto == null || producto.trim().isEmpty()) {
            throw new IllegalArgumentException("El producto no puede estar vacío");
        }
        if (!productosQueSupministra.contains(producto)) {
            productosQueSupministra.add(producto);
        }
    }

    /**
     * Verifica si el proveedor suministra un producto específico
     */
    public boolean suministraProducto(String producto) {
        return productosQueSupministra.contains(producto);
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getProductosQueSupministra() {
        return new ArrayList<>(productosQueSupministra);
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Proveedor{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", empresa='" + empresa + '\'' +
                ", telefono='" + telefono + '\'' +
                ", activo=" + activo +
                ", productos=" + productosQueSupministra.size() +
                '}';
    }
}