package ERICK-S-CONVENIENCE-STORE;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Tests para GestorPedidos
 * Issue #28: Gestión de Pedidos a Proveedores
 * Issue #18: Historial de pedidos pasados
 */
public class GestorPedidosTest {
    
    private GestorPedidos gestor;
    private Proveedor proveedor1;
    private Proveedor proveedor2;

    @BeforeEach
    public void setUp() {
        gestor = new GestorPedidos();
        proveedor1 = gestor.agregarProveedor("Juan Distribuidora", "Distribuidora del Norte", "3001234567", "juan@distribuidora.com");
        proveedor2 = gestor.agregarProveedor("María Alimentos", "Alimentos Frescos SA", "3109876543", "maria@alimentos.com");
    }

    // ============ TESTS GESTIÓN DE PROVEEDORES ============

    @Test
    @DisplayName("Debe agregar un proveedor correctamente")
    public void testAgregarProveedor() {
        Proveedor proveedor = gestor.agregarProveedor("Carlos Suministros", "Suministros XYZ", "3201234567", "carlos@xyz.com");
        
        assertNotNull(proveedor);
        assertEquals("Carlos Suministros", proveedor.getNombre());
        assertEquals(3, gestor.cantidadProveedores());
    }

    @Test
    @DisplayName("No debe permitir agregar proveedor con nombre vacío")
    public void testAgregarProveedorNombreVacio() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gestor.agregarProveedor("", "Empresa Test", "3001111111", "test@test.com");
        });
        assertEquals("El nombre no puede estar vacío", exception.getMessage());
    }

    @Test
    @DisplayName("Debe buscar proveedor por ID")
    public void testBuscarProveedorPorId() {
        Proveedor encontrado = gestor.buscarProveedorPorId(proveedor1.getId());
        assertNotNull(encontrado);
        assertEquals("Juan Distribuidora", encontrado.getNombre());
    }

    @Test
    @DisplayName("Debe buscar proveedor por nombre")
    public void testBuscarProveedorPorNombre() {
        Proveedor encontrado = gestor.buscarProveedorPorNombre("María Alimentos");
        assertNotNull(encontrado);
        assertEquals("Alimentos Frescos SA", encontrado.getEmpresa());
    }

    // ============ TESTS ISSUE #28: GESTIÓN DE PEDIDOS ============

    @Test
    @DisplayName("Issue #28: Debe crear un pedido correctamente")
    public void testCrearPedido() {
        Date fechaEntrega = obtenerFechaFutura(7);
        PedidoProveedor pedido = gestor.crearPedido(proveedor1.getId(), fechaEntrega);
        
        assertNotNull(pedido);
        assertEquals(proveedor1.getId(), pedido.getProveedor().getId());
        assertEquals(PedidoProveedor.EstadoPedido.PENDIENTE, pedido.getEstado());
        assertEquals(1, gestor.cantidadPedidos());
    }

    @Test
    @DisplayName("Issue #28: No debe crear pedido con proveedor inexistente")
    public void testCrearPedidoProveedorInexistente() {
        Date fechaEntrega = obtenerFechaFutura(7);
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gestor.crearPedido(999, fechaEntrega);
        });
        assertEquals("No existe un proveedor con ese ID", exception.getMessage());
    }

    @Test
    @DisplayName("Issue #28: Debe agregar items a un pedido")
    public void testAgregarItemAPedido() {
        Date fechaEntrega = obtenerFechaFutura(7);
        PedidoProveedor pedido = gestor.crearPedido(proveedor1.getId(), fechaEntrega);
        
        boolean resultado = gestor.agregarItemAPedido(pedido.getId(), "Arroz", 50, 3000.0);
        
        assertTrue(resultado);
        assertEquals(1, pedido.getItems().size());
        assertEquals(150000.0, pedido.getTotalPedido(), 0.001);
    }

    @Test
    @DisplayName("Issue #28: Debe agregar múltiples items a un pedido")
    public void testAgregarMultiplesItems() {
        Date fechaEntrega = obtenerFechaFutura(7);
        PedidoProveedor pedido = gestor.crearPedido(proveedor1.getId(), fechaEntrega);
        
        gestor.agregarItemAPedido(pedido.getId(), "Arroz", 50, 3000.0);
        gestor.agregarItemAPedido(pedido.getId(), "Frijol", 30, 4000.0);
        gestor.agregarItemAPedido(pedido.getId(), "Azúcar", 40, 2500.0);
        
        assertEquals(3, pedido.getItems().size());
        assertEquals(370000.0, pedido.getTotalPedido(), 0.001);
    }

    @Test
    @DisplayName("Issue #28: Debe confirmar un pedido")
    public void testConfirmarPedido() {
        Date fechaEntrega = obtenerFechaFutura(7);
        PedidoProveedor pedido = gestor.crearPedido(proveedor1.getId(), fechaEntrega);
        gestor.agregarItemAPedido(pedido.getId(), "Producto", 10, 5000.0);
        
        boolean resultado = gestor.confirmarPedido(pedido.getId());
        
        assertTrue(resultado);
        assertEquals(PedidoProveedor.EstadoPedido.CONFIRMADO, pedido.getEstado());
    }

    @Test
    @DisplayName("Issue #28: No debe confirmar pedido sin items")
    public void testConfirmarPedidoSinItems() {
        Date fechaEntrega = obtenerFechaFutura(7);
        PedidoProveedor pedido = gestor.crearPedido(proveedor1.getId(), fechaEntrega);
        
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            gestor.confirmarPedido(pedido.getId());
        });
        assertEquals("No se puede confirmar un pedido sin items", exception.getMessage());
    }

    @Test
    @DisplayName("Issue #28: Debe marcar pedido como entregado")
    public void testMarcarPedidoComoEntregado() {
        Date fechaEntrega = obtenerFechaFutura(7);
        PedidoProveedor pedido = gestor.crearPedido(proveedor1.getId(), fechaEntrega);
        gestor.agregarItemAPedido(pedido.getId(), "Producto", 10, 5000.0);
        gestor.confirmarPedido(pedido.getId());
        
        boolean resultado = gestor.marcarPedidoComoEntregado(pedido.getId());
        
        assertTrue(resultado);
        assertEquals(PedidoProveedor.EstadoPedido.ENTREGADO, pedido.getEstado());
        assertNotNull(pedido.getFechaEntregaReal());
        assertTrue(pedido.estaCompletado());
    }

    @Test
    @DisplayName("Issue #28: Debe cancelar un pedido")
    public void testCancelarPedido() {
        Date fechaEntrega = obtenerFechaFutura(7);
        PedidoProveedor pedido = gestor.crearPedido(proveedor1.getId(), fechaEntrega);
        gestor.agregarItemAPedido(pedido.getId(), "Producto", 10, 5000.0);
        
        boolean resultado = gestor.cancelarPedido(pedido.getId(), "Proveedor no disponible");
        
        assertTrue(resultado);
        assertEquals(PedidoProveedor.EstadoPedido.CANCELADO, pedido.getEstado());
    }

    @Test
    @DisplayName("Issue #28: No debe cancelar pedido ya entregado")
    public void testNoCancelarPedidoEntregado() {
        Date fechaEntrega = obtenerFechaFutura(7);
        PedidoProveedor pedido = gestor.crearPedido(proveedor1.getId(), fechaEntrega);
        gestor.agregarItemAPedido(pedido.getId(), "Producto", 10, 5000.0);
        gestor.marcarPedidoComoEntregado(pedido.getId());
        
        boolean resultado = gestor.cancelarPedido(pedido.getId(), "Motivo");
        
        assertFalse(resultado);
        assertEquals(PedidoProveedor.EstadoPedido.ENTREGADO, pedido.getEstado());
    }

    // ============ TESTS ISSUE #18: HISTORIAL DE PEDIDOS ============

    @Test
    @DisplayName("Issue #18: Debe obtener historial completo de pedidos")
    public void testObtenerHistorialPedidos() {
        Date fechaEntrega = obtenerFechaFutura(7);
        gestor.crearPedido(proveedor1.getId(), fechaEntrega);
        gestor.crearPedido(proveedor2.getId(), fechaEntrega);
        gestor.crearPedido(proveedor1.getId(), fechaEntrega);
        
        List<PedidoProveedor> historial = gestor.obtenerHistorialPedidos();
        
        assertEquals(3, historial.size());
    }

    @Test
    @DisplayName("Issue #18: Debe obtener historial por proveedor")
    public void testObtenerHistorialPorProveedor() {
        Date fechaEntrega = obtenerFechaFutura(7);
        gestor.crearPedido(proveedor1.getId(), fechaEntrega);
        gestor.crearPedido(proveedor2.getId(), fechaEntrega);
        gestor.crearPedido(proveedor1.getId(), fechaEntrega);
        gestor.crearPedido(proveedor1.getId(), fechaEntrega);
        
        List<PedidoProveedor> historialProveedor1 = gestor.obtenerHistorialPorProveedor(proveedor1.getId());
        List<PedidoProveedor> historialProveedor2 = gestor.obtenerHistorialPorProveedor(proveedor2.getId());
        
        assertEquals(3, historialProveedor1.size());
        assertEquals(1, historialProveedor2.size());
    }

    @Test
    @DisplayName("Issue #18: Debe obtener pedidos completados")
    public void testObtenerPedidosCompletados() {
        Date fechaEntrega = obtenerFechaFutura(7);
        PedidoProveedor pedido1 = gestor.crearPedido(proveedor1.getId(), fechaEntrega);
        PedidoProveedor pedido2 = gestor.crearPedido(proveedor2.getId(), fechaEntrega);
        PedidoProveedor pedido3 = gestor.crearPedido(proveedor1.getId(), fechaEntrega);
        
        gestor.agregarItemAPedido(pedido1.getId(), "Producto", 10, 1000.0);
        gestor.agregarItemAPedido(pedido2.getId(), "Producto", 10, 1000.0);
        gestor.agregarItemAPedido(pedido3.getId(), "Producto", 10, 1000.0);
        
        gestor.marcarPedidoComoEntregado(pedido1.getId());
        gestor.marcarPedidoComoEntregado(pedido3.getId());
        
        List<PedidoProveedor> completados = gestor.obtenerPedidosCompletados();
        
        assertEquals(2, completados.size());
    }

    @Test
    @DisplayName("Issue #18: Debe obtener pedidos pendientes")
    public void testObtenerPedidosPendientes() {
        Date fechaEntrega = obtenerFechaFutura(7);
        PedidoProveedor pedido1 = gestor.crearPedido(proveedor1.getId(), fechaEntrega);
        PedidoProveedor pedido2 = gestor.crearPedido(proveedor2.getId(), fechaEntrega);
        PedidoProveedor pedido3 = gestor.crearPedido(proveedor1.getId(), fechaEntrega);
        
        gestor.agregarItemAPedido(pedido1.getId(), "Producto", 10, 1000.0);
        gestor.agregarItemAPedido(pedido3.getId(), "Producto", 10, 1000.0);
        
        gestor.marcarPedidoComoEntregado(pedido1.getId());
        
        List<PedidoProveedor> pendientes = gestor.obtenerPedidosPendientes();
        
        assertEquals(2, pendientes.size());
    }

    @Test
    @DisplayName("Issue #18: Debe obtener pedidos por estado")
    public void testObtenerPedidosPorEstado() {
        Date fechaEntrega = obtenerFechaFutura(7);
        PedidoProveedor pedido1 = gestor.crearPedido(proveedor1.getId(), fechaEntrega);
        PedidoProveedor pedido2 = gestor.crearPedido(proveedor2.getId(), fechaEntrega);
        
        gestor.agregarItemAPedido(pedido1.getId(), "Producto", 10, 1000.0);
        gestor.agregarItemAPedido(pedido2.getId(), "Producto", 10, 1000.0);
        gestor.confirmarPedido(pedido1.getId());
        
        List<PedidoProveedor> confirmados = gestor.obtenerPedidosPorEstado(PedidoProveedor.EstadoPedido.CONFIRMADO);
        List<PedidoProveedor> pendientes = gestor.obtenerPedidosPorEstado(PedidoProveedor.EstadoPedido.PENDIENTE);
        
        assertEquals(1, confirmados.size());
        assertEquals(1, pendientes.size());
    }

    @Test
    @DisplayName("Issue #18: Debe calcular total gastado con un proveedor")
    public void testCalcularTotalGastadoProveedor() {
        Date fechaEntrega = obtenerFechaFutura(7);
        PedidoProveedor pedido1 = gestor.crearPedido(proveedor1.getId(), fechaEntrega);
        PedidoProveedor pedido2 = gestor.crearPedido(proveedor1.getId(), fechaEntrega);
        PedidoProveedor pedido3 = gestor.crearPedido(proveedor2.getId(), fechaEntrega);
        
        gestor.agregarItemAPedido(pedido1.getId(), "Producto", 10, 5000.0);
        gestor.agregarItemAPedido(pedido2.getId(), "Producto", 20, 3000.0);
        gestor.agregarItemAPedido(pedido3.getId(), "Producto", 15, 4000.0);
        
        gestor.marcarPedidoComoEntregado(pedido1.getId());
        gestor.marcarPedidoComoEntregado(pedido2.getId());
        gestor.marcarPedidoComoEntregado(pedido3.getId());
        
        double totalProveedor1 = gestor.calcularTotalGastadoProveedor(proveedor1.getId());
        
        assertEquals(110000.0, totalProveedor1, 0.001);
    }

    @Test
    @DisplayName("Issue #18: Debe generar estadísticas de pedidos")
    public void testObtenerEstadisticas() {
        Date fechaEntrega = obtenerFechaFutura(7);
        PedidoProveedor pedido1 = gestor.crearPedido(proveedor1.getId(), fechaEntrega);
        PedidoProveedor pedido2 = gestor.crearPedido(proveedor2.getId(), fechaEntrega);
        PedidoProveedor pedido3 = gestor.crearPedido(proveedor1.getId(), fechaEntrega);
        
        gestor.agregarItemAPedido(pedido1.getId(), "Producto", 10, 5000.0);
        gestor.agregarItemAPedido(pedido2.getId(), "Producto", 20, 3000.0);
        gestor.agregarItemAPedido(pedido3.getId(), "Producto", 15, 4000.0);
        
        gestor.marcarPedidoComoEntregado(pedido1.getId());
        gestor.cancelarPedido(pedido3.getId(), "Motivo");
        
        GestorPedidos.EstadisticasPedidos stats = gestor.obtenerEstadisticas();
        
        assertEquals(3, stats.getTotalPedidos());
        assertEquals(1, stats.getPedidosPendientes());
        assertEquals(1, stats.getPedidosCompletados());
        assertEquals(1, stats.getPedidosCancelados());
        assertEquals(50000.0, stats.getTotalGastado(), 0.001);
    }

    // ============ MÉTODOS AUXILIARES ============

    private Date obtenerFechaFutura(int dias) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, dias);
        return cal.getTime();
    }
}