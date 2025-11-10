
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para la clase Cliente
 * Principalmente para Issue #2: Acumular compras al saldo
 */
public class ClienteTest {

    private Cliente cliente;

    @BeforeEach
    public void setUp() {
        cliente = new Cliente(1, "Juan Pérez", "1234567890", "3001234567");
    }

    @Test
    @DisplayName("Issue #2: Debe crear un cliente con saldo inicial en cero")
    public void testCrearClienteConSaldoCero() {
        assertEquals(0.0, cliente.getSaldoDeuda(), 0.001);
        assertFalse(cliente.tieneDeuda());
    }

    @Test
    @DisplayName("Issue #2: Debe acumular una compra correctamente al saldo")
    public void testAgregarCompraSencilla() {
        cliente.agregarCompra(50000.0);
        assertEquals(50000.0, cliente.getSaldoDeuda(), 0.001);
        assertTrue(cliente.tieneDeuda());
    }

    @Test
    @DisplayName("Issue #2: Debe acumular múltiples compras al saldo")
    public void testAcumularMultiplesCompras() {
        cliente.agregarCompra(10000.0);
        cliente.agregarCompra(20000.0);
        cliente.agregarCompra(15000.0);

        assertEquals(45000.0, cliente.getSaldoDeuda(), 0.001);
        assertEquals(3, cliente.getHistorialCompras().size());
    }

    @Test
    @DisplayName("Issue #2: Debe lanzar excepción al agregar compra con monto cero")
    public void testAgregarCompraMontoCero() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cliente.agregarCompra(0.0);
        });
        assertEquals("El monto debe ser mayor a cero", exception.getMessage());
    }

    @Test
    @DisplayName("Issue #2: Debe lanzar excepción al agregar compra con monto negativo")
    public void testAgregarCompraMontoNegativo() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cliente.agregarCompra(-5000.0);
        });
        assertEquals("El monto debe ser mayor a cero", exception.getMessage());
    }

    @Test
    @DisplayName("Issue #2: El historial de compras debe registrar cada compra")
    public void testHistorialComprasSeRegistra() {
        cliente.agregarCompra(10000.0);
        cliente.agregarCompra(20000.0);

        assertEquals(2, cliente.getHistorialCompras().size());
        assertEquals(10000.0, cliente.getHistorialCompras().get(0).getMonto(), 0.001);
        assertEquals(20000.0, cliente.getHistorialCompras().get(1).getMonto(), 0.001);
    }

    @Test
    @DisplayName("Issue #7: Debe saldar la deuda correctamente")
    public void testSaldarDeuda() {
        cliente.agregarCompra(30000.0);
        assertTrue(cliente.tieneDeuda());

        cliente.saldarDeuda();

        assertEquals(0.0, cliente.getSaldoDeuda(), 0.001);
        assertFalse(cliente.tieneDeuda());
    }

    @Test
    @DisplayName("Debe registrar un pago parcial correctamente")
    public void testRegistrarPagoParcial() {
        cliente.agregarCompra(50000.0);
        double saldoRestante = cliente.registrarPago(20000.0);

        assertEquals(30000.0, saldoRestante, 0.001);
        assertEquals(30000.0, cliente.getSaldoDeuda(), 0.001);
        assertTrue(cliente.tieneDeuda());
    }

    @Test
    @DisplayName("Debe registrar un pago total correctamente")
    public void testRegistrarPagoTotal() {
        cliente.agregarCompra(50000.0);
        double saldoRestante = cliente.registrarPago(50000.0);

        assertEquals(0.0, saldoRestante, 0.001);
        assertFalse(cliente.tieneDeuda());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el pago es mayor a la deuda")
    public void testRegistrarPagoMayorADeuda() {
        cliente.agregarCompra(30000.0);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cliente.registrarPago(40000.0);
        });
        assertEquals("El pago no puede ser mayor a la deuda actual", exception.getMessage());
    }

    @Test
    @DisplayName("Debe mantener los datos del cliente correctos")
    public void testDatosCliente() {
        assertEquals(1, cliente.getId());
        assertEquals("Juan Pérez", cliente.getNombre());
        assertEquals("1234567890", cliente.getCedula());
        assertEquals("3001234567", cliente.getTelefono());
        assertNotNull(cliente.getFechaRegistro());
    }

    @Test
    @DisplayName("El toString debe mostrar información del cliente")
    public void testToString() {
        String resultado = cliente.toString();
        assertTrue(resultado.contains("Juan Pérez"));
        assertTrue(resultado.contains("1234567890"));
    }
}