package ERICK-S-CONVENIENCE-STORE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase que gestiona los pedidos a proveedores
 * Issue #28: Gestión de Pedidos a Proveedores
 * Issue #18: Historial de pedidos pasados
 */
public class GestorPedidos {
    private List<PedidoProveedor> pedidos;
    private List<Proveedor> proveedores;
    private int siguienteIdPedido;
    private int siguienteIdProveedor;

    public GestorPedidos() {
        this.pedidos = new ArrayList<>();
        this.proveedores = new ArrayList<>();
        this.siguienteIdPedido = 1;
        this.siguienteIdProveedor = 1;
    }

    // ============ GESTIÓN DE PROVEEDORES ============

    /**
     * Agrega un nuevo proveedor al sistema
     */
    public Proveedor agregarProveedor(String nombre, String empresa, String telefono, String email) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (empresa == null || empresa.trim().isEmpty()) {
            throw new IllegalArgumentException("La empresa no puede estar vacía");
        }

        Proveedor nuevoProveedor = new Proveedor(siguienteIdProveedor++, nombre, empresa, telefono, email);
        proveedores.add(nuevoProveedor);
        return nuevoProveedor;
    }

    /**
     * Busca un proveedor por ID
     */
    public Proveedor buscarProveedorPorId(int id) {
        return proveedores.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Busca un proveedor por nombre
     */
    public Proveedor buscarProveedorPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return null;
        }
        return proveedores.stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre.trim()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Obtiene todos los proveedores activos
     */
    public List<Proveedor> obtenerProveedoresActivos() {
        return proveedores.stream()
                .filter(Proveedor::isActivo)
                .collect(Collectors.toList());
    }

    // ============ ISSUE #28: GESTIÓN DE PEDIDOS A PROVEEDORES ============

    /**
     * Crea un nuevo pedido a un proveedor
     */
    public PedidoProveedor crearPedido(int idProveedor, Date fechaEntregaEstimada) {
        Proveedor proveedor = buscarProveedorPorId(idProveedor);
        if (proveedor == null) {
            throw new IllegalArgumentException("No existe un proveedor con ese ID");
        }
        if (!proveedor.isActivo()) {
            throw new IllegalStateException("El proveedor no está activo");
        }

        PedidoProveedor nuevoPedido = new PedidoProveedor(siguienteIdPedido++, proveedor, fechaEntregaEstimada);
        pedidos.add(nuevoPedido);
        return nuevoPedido;
    }

    /**
     * Agrega items a un pedido existente
     */
    public boolean agregarItemAPedido(int idPedido, String producto, int cantidad, double precioUnitario) {
        PedidoProveedor pedido = buscarPedidoPorId(idPedido);
        if (pedido == null) {
            return false;
        }
        if (!pedido.estaPendiente()) {
            throw new IllegalStateException("Solo se pueden agregar items a pedidos pendientes");
        }

        pedido.agregarItem(producto, cantidad, precioUnitario);
        return true;
    }

    /**
     * Confirma un pedido
     */
    public boolean confirmarPedido(int idPedido) {
        PedidoProveedor pedido = buscarPedidoPorId(idPedido);
        if (pedido == null) {
            return false;
        }
        if (pedido.getItems().isEmpty()) {
            throw new IllegalStateException("No se puede confirmar un pedido sin items");
        }

        pedido.setEstado(PedidoProveedor.EstadoPedido.CONFIRMADO);
        return true;
    }

    /**
     * Marca un pedido como entregado
     */
    public boolean marcarPedidoComoEntregado(int idPedido) {
        PedidoProveedor pedido = buscarPedidoPorId(idPedido);
        if (pedido == null) {
            return false;
        }

        pedido.marcarComoEntregado();
        return true;
    }

    /**
     * Cancela un pedido
     */
    public boolean cancelarPedido(int idPedido, String motivo) {
        PedidoProveedor pedido = buscarPedidoPorId(idPedido);
        if (pedido == null) {
            return false;
        }

        try {
            pedido.cancelar(motivo);
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    /**
     * Busca un pedido por ID
     */
    public PedidoProveedor buscarPedidoPorId(int id) {
        return pedidos.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // ============ ISSUE #18: HISTORIAL DE PEDIDOS PASADOS ============

    /**
     * Obtiene el historial completo de pedidos
     */
    public List<PedidoProveedor> obtenerHistorialPedidos() {
        return new ArrayList<>(pedidos);
    }

    /**
     * Obtiene el historial de pedidos de un proveedor específico
     */
    public List<PedidoProveedor> obtenerHistorialPorProveedor(int idProveedor) {
        return pedidos.stream()
                .filter(p -> p.getProveedor().getId() == idProveedor)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene pedidos completados (entregados)
     */
    public List<PedidoProveedor> obtenerPedidosCompletados() {
        return pedidos.stream()
                .filter(PedidoProveedor::estaCompletado)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene pedidos pendientes
     */
    public List<PedidoProveedor> obtenerPedidosPendientes() {
        return pedidos.stream()
                .filter(PedidoProveedor::estaPendiente)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene pedidos por estado
     */
    public List<PedidoProveedor> obtenerPedidosPorEstado(PedidoProveedor.EstadoPedido estado) {
        return pedidos.stream()
                .filter(p -> p.getEstado() == estado)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene pedidos realizados en un rango de fechas
     */
    public List<PedidoProveedor> obtenerPedidosPorRangoFechas(Date fechaInicio, Date fechaFin) {
        return pedidos.stream()
                .filter(p -> !p.getFechaPedido().before(fechaInicio) && !p.getFechaPedido().after(fechaFin))
                .collect(Collectors.toList());
    }

    /**
     * Calcula el total gastado con un proveedor
     */
    public double calcularTotalGastadoProveedor(int idProveedor) {
        return pedidos.stream()
                .filter(p -> p.getProveedor().getId() == idProveedor)
                .filter(PedidoProveedor::estaCompletado)
                .mapToDouble(PedidoProveedor::getTotalPedido)
                .sum();
    }

    /**
     * Obtiene estadísticas de pedidos
     */
    public EstadisticasPedidos obtenerEstadisticas() {
        int totalPedidos = pedidos.size();
        int pendientes = (int) pedidos.stream().filter(PedidoProveedor::estaPendiente).count();
        int completados = (int) pedidos.stream().filter(PedidoProveedor::estaCompletado).count();
        int cancelados = (int) pedidos.stream()
                .filter(p -> p.getEstado() == PedidoProveedor.EstadoPedido.CANCELADO)
                .count();
        double totalGastado = pedidos.stream()
                .filter(PedidoProveedor::estaCompletado)
                .mapToDouble(PedidoProveedor::getTotalPedido)
                .sum();

        return new EstadisticasPedidos(totalPedidos, pendientes, completados, cancelados, totalGastado);
    }

    // ============ MÉTODOS AUXILIARES ============

    /**
     * Obtiene la cantidad total de pedidos
     */
    public int cantidadPedidos() {
        return pedidos.size();
    }

    /**
     * Obtiene la cantidad total de proveedores
     */
    public int cantidadProveedores() {
        return proveedores.size();
    }

    /**
     * Limpia todos los datos (útil para testing)
     */
    public void limpiarDatos() {
        pedidos.clear();
        proveedores.clear();
        siguienteIdPedido = 1;
        siguienteIdProveedor = 1;
    }

    /**
     * Clase para encapsular estadísticas de pedidos
     */
    public static class EstadisticasPedidos {
        private int totalPedidos;
        private int pedidosPendientes;
        private int pedidosCompletados;
        private int pedidosCancelados;
        private double totalGastado;

        public EstadisticasPedidos(int totalPedidos, int pedidosPendientes, int pedidosCompletados,
                                   int pedidosCancelados, double totalGastado) {
            this.totalPedidos = totalPedidos;
            this.pedidosPendientes = pedidosPendientes;
            this.pedidosCompletados = pedidosCompletados;
            this.pedidosCancelados = pedidosCancelados;
            this.totalGastado = totalGastado;
        }

        public int getTotalPedidos() {
            return totalPedidos;
        }

        public int getPedidosPendientes() {
            return pedidosPendientes;
        }

        public int getPedidosCompletados() {
            return pedidosCompletados;
        }

        public int getPedidosCancelados() {
            return pedidosCancelados;
        }

        public double getTotalGastado() {
            return totalGastado;
        }

        @Override
        public String toString() {
            return "EstadisticasPedidos{" +
                    "total=" + totalPedidos +
                    ", pendientes=" + pedidosPendientes +
                    ", completados=" + pedidosCompletados +
                    ", cancelados=" + pedidosCancelados +
                    ", totalGastado=" + totalGastado +
                    '}';
        }
    }
}