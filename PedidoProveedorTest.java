package ERICK-S-CONVENIENCE-STORE;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Calendar;
import java.util.Date;

/**
 * Tests para la clase PedidoProveedor
 */
public class PedidoProveedorTest {
    
    private Proveedor proveedor;
    private PedidoProveedor pedido;
    private Date fechaEntrega;

    @BeforeEach
    public void setUp() {
        proveedor = new Proveedor(1, "Distribuidora Norte", "Norte SA", "3001234567", "norte@test.com");
        fechaEntrega = obtenerFechaFutura(7);
        pedido = new PedidoProveedor(1, proveedor, fechaEntrega);
    }

    // ============ TESTS DE CREACIÓN ============

    @Test
    @DisplayName("Debe crear un pedido correctamente")
    public void testCrearPedido() {
        assertNotNull(pedido);
        assertEquals(1, pedido.getId());
        assertEquals(proveedor, pedido.getProveedor());
        assertEquals(fechaEntrega, pedido.getFechaEntregaEstimada());
        assertEquals(PedidoProveedor.EstadoPedido.PENDIENTE, pedido.getEstado());
        assertEquals(0.0, pedido.getTotalPedido(), 0.001);
        assertTrue(pedido.getItems().isEmpty());
    }

    @Test
    @DisplayName("No debe crear pedido con proveedor nulo")
    public void testNoCrearPedidoProveedorNulo() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new PedidoProveedor(1, null, fechaEntrega);
        });
        assertEquals("El proveedor no puede ser nulo", exception.getMessage());
    }

    @Test
    @DisplayName("Debe iniciar con estado PENDIENTE")
    public void testEstadoInicialPendiente() {
        assertEquals(PedidoProveedor.EstadoPedido.PENDIENTE, pedido.getEstado());
        assertTrue(pedido.estaPendiente());
        assertFalse(pedido.estaCompletado());
    }

    @Test
    @DisplayName("Debe registrar la fecha de creación del pedido")
    public void testFechaPedidoRegistrada() {
        assertNotNull(pedido.getFechaPedido());
    }

    // ============ TESTS DE AGREGAR ITEMS ============

    @Test
    @DisplayName("Debe agregar un item correctamente")
    public void testAgregarItem() {
        pedido.agregarItem("Arroz", 50, 3000.0);
        
        assertEquals(1, pedido.getItems().size());
        assertEquals(150000.0, pedido.getTotalPedido(), 0.001);
    }

    @Test
    @DisplayName("Debe agregar múltiples items")
    public void testAgregarMultiplesItems() {
        pedido.agregarItem("Arroz", 50, 3000.0);
        pedido.agregarItem("Frijol", 30, 4000.0);
        pedido.agregarItem("Azúcar", 40, 2500.0);
        
        assertEquals(3, pedido.getItems().size());
        assertEquals(370000.0, pedido.getTotalPedido(), 0.001);
    }

    @Test
    @DisplayName("No debe agregar item con producto vacío")
    public void testNoAgregarItemProductoVacio() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pedido.agregarItem("", 10, 1000.0);
        });
        assertEquals("El producto no puede estar vacío", exception.getMessage());
    }

    @Test
    @DisplayName("No debe agregar item con producto nulo")
    public void testNoAgregarItemProductoNulo() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pedido.agregarItem(null, 10, 1000.0);
        });
        assertEquals("El producto no puede estar vacío", exception.getMessage());
    }

    @Test
    @DisplayName("No debe agregar item con cantidad cero")
    public void testNoAgregarItemCantidadCero() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pedido.agregarItem("Arroz", 0, 1000.0);
        });
        assertEquals("La cantidad debe ser mayor a cero", exception.getMessage());
    }

    @Test
    @DisplayName("No debe agregar item con cantidad negativa")
    public void testNoAgregarItemCantidadNegativa() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pedido.agregarItem("Arroz", -5, 1000.0);
        });
        assertEquals("La cantidad debe ser mayor a cero", exception.getMessage());
    }

    @Test
    @DisplayName("No debe agregar item con precio cero")
    public void testNoAgregarItemPrecioCero() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pedido.agregarItem("Arroz", 10, 0.0);
        });
        assertEquals("El precio unitario debe ser mayor a cero", exception.getMessage());
    }

    @Test
    @DisplayName("No debe agregar item con precio negativo")
    public void testNoAgregarItemPrecioNegativo() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pedido.agregarItem("Arroz", 10, -1000.0);
        });
        assertEquals("El precio unitario debe ser mayor a cero", exception.getMessage());
    }

    // ============ TESTS DE CÁLCULO DE TOTALES ============

    @Test
    @DisplayName("Debe calcular el total correctamente con un item")
    public void testCalcularTotalUnItem() {
        pedido.agregarItem("Arroz", 50, 3000.0);
        assertEquals(150000.0, pedido.getTotalPedido(), 0.001);
    }

    @Test
    @DisplayName("Debe calcular el total correctamente con múltiples items")
    public void testCalcularTotalMultiplesItems() {
        pedido.agregarItem("Arroz", 50, 3000.0);    // 150000
        pedido.agregarItem("Frijol", 30, 4000.0);   // 120000
        pedido.agregarItem("Azúcar", 40, 2500.0);   // 100000
        pedido.agregarItem("Aceite", 20, 5000.0);   // 100000
        
        assertEquals(470000.0, pedido.getTotalPedido(), 0.001);
    }

    @Test
    @DisplayName("El total debe actualizarse al agregar items")
    public void testTotalSeActualizaAlAgregar() {
        assertEquals(0.0, pedido.getTotalPedido(), 0.001);
        
        pedido.agregarItem("Producto1", 10, 1000.0);
        assertEquals(10000.0, pedido.getTotalPedido(), 0.001);
        
        pedido.agregarItem("Producto2", 5, 2000.0);
        assertEquals(20000.0, pedido.getTotalPedido(), 0.001);
    }

    // ============ TESTS DE ESTADOS ============

    @Test
    @DisplayName("Debe cambiar el estado correctamente")
    public void testCambiarEstado() {
        pedido.setEstado(PedidoProveedor.EstadoPedido.CONFIRMADO);
        assertEquals(PedidoProveedor.EstadoPedido.CONFIRMADO, pedido.getEstado());
        
        pedido.setEstado(PedidoProveedor.EstadoPedido.EN_CAMINO);
        assertEquals(PedidoProveedor.EstadoPedido.EN_CAMINO, pedido.getEstado());
    }

    @Test
    @DisplayName("Debe marcar como entregado correctamente")
    public void testMarcarComoEntregado() {
        assertNull(pedido.getFechaEntregaReal());
        
        pedido.marcarComoEntregado();
        
        assertEquals(PedidoProveedor.EstadoPedido.ENTREGADO, pedido.getEstado());
        assertNotNull(pedido.getFechaEntregaReal());
        assertTrue(pedido.estaCompletado());
        assertFalse(pedido.estaPendiente());
    }

    @Test
    @DisplayName("Debe cancelar pedido correctamente")
    public void testCancelarPedido() {
        String motivo = "Proveedor no disponible";
        pedido.cancelar(motivo);
        
        assertEquals(PedidoProveedor.EstadoPedido.CANCELADO, pedido.getEstado());
        assertEquals(motivo, pedido.getObservaciones());
    }

    @Test
    @DisplayName("No debe cancelar pedido ya entregado")
    public void testNoCancelarPedidoEntregado() {
        pedido.marcarComoEntregado();
        
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            pedido.cancelar("Motivo");
        });
        assertEquals("No se puede cancelar un pedido ya entregado", exception.getMessage());
    }

    @Test
    @DisplayName("Debe identificar correctamente pedidos pendientes")
    public void testEstaPendiente() {
        assertTrue(pedido.estaPendiente());
        
        pedido.setEstado(PedidoProveedor.EstadoPedido.CONFIRMADO);
        assertTrue(pedido.estaPendiente());
        
        pedido.setEstado(PedidoProveedor.EstadoPedido.EN_CAMINO);
        assertFalse(pedido.estaPendiente());
        
        pedido.setEstado(PedidoProveedor.EstadoPedido.ENTREGADO);
        assertFalse(pedido.estaPendiente());
    }

    @Test
    @DisplayName("Debe identificar correctamente pedidos completados")
    public void testEstaCompletado() {
        assertFalse(pedido.estaCompletado());
        
        pedido.setEstado(PedidoProveedor.EstadoPedido.CONFIRMADO);
        assertFalse(pedido.estaCompletado());
        
        pedido.setEstado(PedidoProveedor.EstadoPedido.ENTREGADO);
        assertTrue(pedido.estaCompletado());
    }

    // ============ TESTS DE ITEMS ============

    @Test
    @DisplayName("Los items deben tener los datos correctos")
    public void testDatosItemsCorrectos() {
        pedido.agregarItem("Arroz Premium", 50, 3500.0);
        
        PedidoProveedor.ItemPedido item = pedido.getItems().get(0);
        
        assertEquals("Arroz Premium", item.getProducto());
        assertEquals(50, item.getCantidad());
        assertEquals(3500.0, item.getPrecioUnitario(), 0.001);
        assertEquals(175000.0, item.getSubtotal(), 0.001);
    }

    @Test
    @DisplayName("El subtotal del item debe calcularse correctamente")
    public void testSubtotalItem() {
        pedido.agregarItem("Producto", 15, 2500.0);
        
        PedidoProveedor.ItemPedido item = pedido.getItems().get(0);
        assertEquals(37500.0, item.getSubtotal(), 0.001);
    }

    @Test
    @DisplayName("La lista de items debe ser inmutable desde afuera")
    public void testListaItemsInmutable() {
        pedido.agregarItem("Arroz", 50, 3000.0);
        pedido.agregarItem("Frijol", 30, 4000.0);
        
        var items = pedido.getItems();
        assertEquals(2, items.size());
        
        // Intentar modificar la lista retornada no debe afectar al pedido
        items.clear();
        
        assertEquals(2, pedido.getItems().size());
    }

    // ============ TESTS DE OBSERVACIONES Y FECHAS ============

    @Test
    @DisplayName("Debe actualizar observaciones")
    public void testActualizarObservaciones() {
        pedido.setObservaciones("Pedido urgente");
        assertEquals("Pedido urgente", pedido.getObservaciones());
    }

    @Test
    @DisplayName("Debe actualizar fecha de entrega estimada")
    public void testActualizarFechaEntregaEstimada() {
        Date nuevaFecha = obtenerFechaFutura(14);
        pedido.setFechaEntregaEstimada(nuevaFecha);
        assertEquals(nuevaFecha, pedido.getFechaEntregaEstimada());
    }

    // ============ TESTS DE toString ============

    @Test
    @DisplayName("El toString debe mostrar información del pedido")
    public void testToString() {
        pedido.agregarItem("Arroz", 50, 3000.0);
        String resultado = pedido.toString();
        
        assertTrue(resultado.contains("Distribuidora Norte"));
        assertTrue(resultado.contains("PENDIENTE"));
        assertTrue(resultado.contains("150000"));
    }

    // ============ TESTS INTEGRALES ============

    @Test
    @DisplayName("Debe mantener consistencia en flujo completo de pedido")
    public void testFlujoCompletoPedido() {
        // Crear pedido
        assertEquals(PedidoProveedor.EstadoPedido.PENDIENTE, pedido.getEstado());
        assertEquals(0.0, pedido.getTotalPedido(), 0.001);
        
        // Agregar items
        pedido.agregarItem("Arroz", 50, 3000.0);
        pedido.agregarItem("Frijol", 30, 4000.0);
        assertEquals(270000.0, pedido.getTotalPedido(), 0.001);
        
        // Confirmar
        pedido.setEstado(PedidoProveedor.EstadoPedido.CONFIRMADO);
        assertTrue(pedido.estaPendiente());
        
        // Marcar en camino
        pedido.setEstado(PedidoProveedor.EstadoPedido.EN_CAMINO);
        assertFalse(pedido.estaPendiente());
        
        // Entregar
        pedido.marcarComoEntregado();
        assertTrue(pedido.estaCompletado());
        assertNotNull(pedido.getFechaEntregaReal());
    }

    // ============ MÉTODO AUXILIAR ============

    private Date obtenerFechaFutura(int dias) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, dias);
        return cal.getTime();
    }
}