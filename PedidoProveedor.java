package ERICK-S-CONVENIENCE-STORE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase que representa un pedido a un proveedor
 * Issue #28: Gestión de Pedidos a Proveedores
 * Issue #18: Historial de pedidos pasados
 */
public class PedidoProveedor {
    private int id;
    private Proveedor proveedor;
    private Date fechaPedido;
    private Date fechaEntregaEstimada;
    private Date fechaEntregaReal;
    private List<ItemPedido> items;
    private double totalPedido;
    private EstadoPedido estado;
    private String observaciones;

    public enum EstadoPedido {
        PENDIENTE,
        CONFIRMADO,
        EN_CAMINO,
        ENTREGADO,
        CANCELADO
    }

    public PedidoProveedor(int id, Proveedor proveedor, Date fechaEntregaEstimada) {
        if (proveedor == null) {
            throw new IllegalArgumentException("El proveedor no puede ser nulo");
        }
        this.id = id;
        this.proveedor = proveedor;
        this.fechaPedido = new Date();
        this.fechaEntregaEstimada = fechaEntregaEstimada;
        this.items = new ArrayList<>();
        this.totalPedido = 0.0;
        this.estado = EstadoPedido.PENDIENTE;
        this.observaciones = "";
    }

    /**
     * Agrega un item al pedido
     */
    public void agregarItem(String producto, int cantidad, double precioUnitario) {
        if (producto == null || producto.trim().isEmpty()) {
            throw new IllegalArgumentException("El producto no puede estar vacío");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        if (precioUnitario <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor a cero");
        }

        ItemPedido item = new ItemPedido(producto, cantidad, precioUnitario);
        items.add(item);
        calcularTotal();
    }

    /**
     * Calcula el total del pedido
     */
    private void calcularTotal() {
        totalPedido = items.stream()
                .mapToDouble(ItemPedido::getSubtotal)
                .sum();
    }

    /**
     * Marca el pedido como entregado
     */
    public void marcarComoEntregado() {
        this.estado = EstadoPedido.ENTREGADO;
        this.fechaEntregaReal = new Date();
    }

    /**
     * Cancela el pedido
     */
    public void cancelar(String motivo) {
        if (this.estado == EstadoPedido.ENTREGADO) {
            throw new IllegalStateException("No se puede cancelar un pedido ya entregado");
        }
        this.estado = EstadoPedido.CANCELADO;
        this.observaciones = motivo;
    }

    /**
     * Verifica si el pedido está pendiente
     */
    public boolean estaPendiente() {
        return this.estado == EstadoPedido.PENDIENTE || this.estado == EstadoPedido.CONFIRMADO;
    }

    /**
     * Verifica si el pedido fue completado
     */
    public boolean estaCompletado() {
        return this.estado == EstadoPedido.ENTREGADO;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public Date getFechaPedido() {
        return fechaPedido;
    }

    public Date getFechaEntregaEstimada() {
        return fechaEntregaEstimada;
    }

    public void setFechaEntregaEstimada(Date fechaEntregaEstimada) {
        this.fechaEntregaEstimada = fechaEntregaEstimada;
    }

    public Date getFechaEntregaReal() {
        return fechaEntregaReal;
    }

    public List<ItemPedido> getItems() {
        return new ArrayList<>(items);
    }

    public double getTotalPedido() {
        return totalPedido;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return "PedidoProveedor{" +
                "id=" + id +
                ", proveedor=" + proveedor.getNombre() +
                ", fechaPedido=" + fechaPedido +
                ", total=" + totalPedido +
                ", estado=" + estado +
                ", items=" + items.size() +
                '}';
    }

    /**
     * Clase interna para representar un item del pedido
     */
    public static class ItemPedido {
        private String producto;
        private int cantidad;
        private double precioUnitario;
        private double subtotal;

        public ItemPedido(String producto, int cantidad, double precioUnitario) {
            this.producto = producto;
            this.cantidad = cantidad;
            this.precioUnitario = precioUnitario;
            this.subtotal = cantidad * precioUnitario;
        }

        public String getProducto() {
            return producto;
        }

        public int getCantidad() {
            return cantidad;
        }

        public double getPrecioUnitario() {
            return precioUnitario;
        }

        public double getSubtotal() {
            return subtotal;
        }

        @Override
        public String toString() {
            return "ItemPedido{" +
                    "producto='" + producto + '\'' +
                    ", cantidad=" + cantidad +
                    ", precioUnitario=" + precioUnitario +
                    ", subtotal=" + subtotal +
                    '}';
        }
    }
}