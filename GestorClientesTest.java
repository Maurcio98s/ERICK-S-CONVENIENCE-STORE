package com.tienda.service;

import com.tienda.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Tests para la clase GestorClientes
 * Issue #5: Buscar un cliente
 * Issue #7: Marcar deuda como saldada
 */
public class GestorClientesTest {

    private GestorClientes gestor;

    @BeforeEach
    public void setUp() {
        gestor = new GestorClientes();
    }

    // ============ TESTS BÁSICOS DE GESTIÓN ============

    @Test
    @DisplayName("Debe crear un gestor vacío inicialmente")
    public void testGestorVacio() {
        assertEquals(0, gestor.cantidadClientes());
        assertTrue(gestor.obtenerTodosLosClientes().isEmpty());
    }

    @Test
    @DisplayName("Debe agregar un cliente correctamente")
    public void testAgregarCliente() {
        Cliente cliente = gestor.agregarCliente("María García", "9876543210", "3109876543");

        assertNotNull(cliente);
        assertEquals("María García", cliente.getNombre());
        assertEquals(1, gestor.cantidadClientes());
    }

    @Test
    @DisplayName("No debe permitir agregar cliente con nombre vacío")
    public void testAgregarClienteNombreVacio() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gestor.agregarCliente("", "1234567890", "3001234567");
        });
        assertEquals("El nombre no puede estar vacío", exception.getMessage());
    }

    @Test
    @DisplayName("No debe permitir agregar cliente con cédula duplicada")
    public void testAgregarClienteCedulaDuplicada() {
        gestor.agregarCliente("Carlos López", "1111111111", "3001111111");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gestor.agregarCliente("Pedro Martínez", "1111111111", "3002222222");
        });
        assertEquals("Ya existe un cliente con esa cédula", exception.getMessage());
    }

    // ============ TESTS ISSUE #5: BUSCAR CLIENTE ============

    @Test
    @DisplayName("Issue #5: Debe buscar cliente por ID correctamente")
    public void testBuscarClientePorId() {
        Cliente cliente1 = gestor.agregarCliente("Ana Rodríguez", "1010101010", "3101010101");
        Cliente cliente2 = gestor.agregarCliente("Luis Martínez", "2020202020", "3202020202");

        Cliente encontrado = gestor.buscarClientePorId(cliente1.getId());
        assertNotNull(encontrado);
        assertEquals("Ana Rodríguez", encontrado.getNombre());
    }

    @Test
    @DisplayName("Issue #5: Debe retornar null si el ID no existe")
    public void testBuscarClientePorIdNoExiste() {
        gestor.agregarCliente("Pedro Pérez", "3030303030", "3303030303");

        Cliente encontrado = gestor.buscarClientePorId(999);
        assertNull(encontrado);
    }

    @Test
    @DisplayName("Issue #5: Debe buscar cliente por nombre exacto")
    public void testBuscarClientePorNombre() {
        gestor.agregarCliente("Sofía González", "4040404040", "3404040404");
        gestor.agregarCliente("Miguel Torres", "5050505050", "3505050505");

        Cliente encontrado = gestor.buscarClientePorNombre("Sofía González");
        assertNotNull(encontrado);
        assertEquals("4040404040", encontrado.getCedula());
    }

    @Test
    @DisplayName("Issue #5: La búsqueda por nombre no debe ser case-sensitive")
    public void testBuscarClientePorNombreCaseInsensitive() {
        gestor.agregarCliente("Roberto Díaz", "6060606060", "3606060606");

        Cliente encontrado1 = gestor.buscarClientePorNombre("roberto díaz");
        Cliente encontrado2 = gestor.buscarClientePorNombre("ROBERTO DÍAZ");

        assertNotNull(encontrado1);
        assertNotNull(encontrado2);
        assertEquals(encontrado1.getId(), encontrado2.getId());
    }

    @Test
    @DisplayName("Issue #5: Debe retornar null si el nombre no existe")
    public void testBuscarClientePorNombreNoExiste() {
        gestor.agregarCliente("Laura Sánchez", "7070707070", "3707070707");

        Cliente encontrado = gestor.buscarClientePorNombre("Juan Inexistente");
        assertNull(encontrado);
    }

    @Test
    @DisplayName("Issue #5: Debe buscar cliente por cédula")
    public void testBuscarClientePorCedula() {
        gestor.agregarCliente("Diana Castro", "8080808080", "3808080808");

        Cliente encontrado = gestor.buscarClientePorCedula("8080808080");
        assertNotNull(encontrado);
        assertEquals("Diana Castro", encontrado.getNombre());
    }

    @Test
    @DisplayName("Issue #5: Debe buscar clientes por nombre parcial")
    public void testBuscarClientesPorNombreParcial() {
        gestor.agregarCliente("Carlos Alberto Pérez", "1111111111", "3001111111");
        gestor.agregarCliente("María Carolina López", "2222222222", "3002222222");
        gestor.agregarCliente("Juan Rodríguez", "3333333333", "3003333333");

        List<Cliente> encontrados = gestor.buscarClientesPorNombreParcial("carl");

        assertEquals(2, encontrados.size());
        assertTrue(encontrados.stream().anyMatch(c -> c.getNombre().contains("Carlos")));
        assertTrue(encontrados.stream().anyMatch(c -> c.getNombre().contains("Carolina")));
    }

    @Test
    @DisplayName("Issue #5: La búsqueda parcial debe retornar lista vacía si no hay coincidencias")
    public void testBuscarClientesPorNombreParcialSinResultados() {
        gestor.agregarCliente("Pedro Gómez", "4444444444", "3004444444");

        List<Cliente> encontrados = gestor.buscarClientesPorNombreParcial("xyz");
        assertTrue(encontrados.isEmpty());
    }

    // ============ TESTS ISSUE #7: MARCAR DEUDA COMO SALDADA ============

    @Test
    @DisplayName("Issue #7: Debe marcar deuda como pagada por ID")
    public void testMarcarDeudaComoPagadaPorId() {
        Cliente cliente = gestor.agregarCliente("Andrés Vargas", "5555555555", "3005555555");
        cliente.agregarCompra(100000.0);

        assertTrue(cliente.tieneDeuda());

        boolean resultado = gestor.marcarDeudaComoPagada(cliente.getId());

        assertTrue(resultado);
        assertFalse(cliente.tieneDeuda());
        assertEquals(0.0, cliente.getSaldoDeuda(), 0.001);
    }

    @Test
    @DisplayName("Issue #7: Debe retornar false si el cliente no existe al marcar como pagada")
    public void testMarcarDeudaComoPagadaClienteNoExiste() {
        boolean resultado = gestor.marcarDeudaComoPagada(999);
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Issue #7: Debe saldar deuda usando el objeto Cliente")
    public void testSaldarDeudaConObjeto() {
        Cliente cliente = gestor.agregarCliente("Carolina Ruiz", "6666666666", "3006666666");
        cliente.agregarCompra(75000.0);
        cliente.agregarCompra(25000.0);

        assertEquals(100000.0, cliente.getSaldoDeuda(), 0.001);

        gestor.saldarDeuda(cliente);

        assertEquals(0.0, cliente.getSaldoDeuda(), 0.001);
        assertFalse(cliente.tieneDeuda());
    }

    @Test
    @DisplayName("Issue #7: Debe lanzar excepción si se intenta saldar deuda de cliente nulo")
    public void testSaldarDeudaClienteNulo() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gestor.saldarDeuda(null);
        });
        assertEquals("El cliente no puede ser nulo", exception.getMessage());
    }

    @Test
    @DisplayName("Issue #7: Saldar deuda de cliente sin deuda debe dejarlo en cero")
    public void testSaldarDeudaClienteSinDeuda() {
        Cliente cliente = gestor.agregarCliente("Felipe Mora", "7777777777", "3007777777");

        assertEquals(0.0, cliente.getSaldoDeuda(), 0.001);

        gestor.saldarDeuda(cliente);

        assertEquals(0.0, cliente.getSaldoDeuda(), 0.001);
    }

    // ============ TESTS DE FUNCIONALIDADES ADICIONALES ============

    @Test
    @DisplayName("Debe registrar pago parcial correctamente")
    public void testRegistrarPagoParcial() {
        Cliente cliente = gestor.agregarCliente("Valentina Cruz", "8888888888", "3008888888");
        cliente.agregarCompra(150000.0);

        boolean resultado = gestor.registrarPago(cliente.getId(), 50000.0);

        assertTrue(resultado);
        assertEquals(100000.0, cliente.getSaldoDeuda(), 0.001);
    }

    @Test
    @DisplayName("Debe obtener lista de clientes con deuda")
    public void testObtenerClientesConDeuda() {
        Cliente cliente1 = gestor.agregarCliente("Jorge Pinto", "1234567891", "3001234567");
        Cliente cliente2 = gestor.agregarCliente("Sandra Ríos", "1234567892", "3001234568");
        Cliente cliente3 = gestor.agregarCliente("Camilo Ortiz", "1234567893", "3001234569");

        cliente1.agregarCompra(50000.0);
        cliente3.agregarCompra(30000.0);
        // cliente2 no tiene deuda

        List<Cliente> clientesConDeuda = gestor.obtenerClientesConDeuda();

        assertEquals(2, clientesConDeuda.size());
        assertTrue(clientesConDeuda.contains(cliente1));
        assertTrue(clientesConDeuda.contains(cliente3));
        assertFalse(clientesConDeuda.contains(cliente2));
    }

    @Test
    @DisplayName("Debe calcular total de deudas correctamente")
    public void testCalcularTotalDeudas() {
        Cliente cliente1 = gestor.agregarCliente("Alberto Ramos", "9999999991", "3009999991");
        Cliente cliente2 = gestor.agregarCliente("Beatriz Silva", "9999999992", "3009999992");
        Cliente cliente3 = gestor.agregarCliente("César Vega", "9999999993", "3009999993");

        cliente1.agregarCompra(40000.0);
        cliente2.agregarCompra(60000.0);
        cliente3.agregarCompra(50000.0);

        double totalDeudas = gestor.calcularTotalDeudas();

        assertEquals(150000.0, totalDeudas, 0.001);
    }

    @Test
    @DisplayName("Debe eliminar cliente correctamente")
    public void testEliminarCliente() {
        Cliente cliente = gestor.agregarCliente("Ricardo Blanco", "1010101011", "3101010101");
        assertEquals(1, gestor.cantidadClientes());

        boolean resultado = gestor.eliminarCliente(cliente.getId());

        assertTrue(resultado);
        assertEquals(0, gestor.cantidadClientes());
    }

    @Test
    @DisplayName("Debe limpiar todos los clientes")
    public void testLimpiarClientes() {
        gestor.agregarCliente("Cliente 1", "1111111111", "3001111111");
        gestor.agregarCliente("Cliente 2", "2222222222", "3002222222");
        gestor.agregarCliente("Cliente 3", "3333333333", "3003333333");

        assertEquals(3, gestor.cantidadClientes());

        gestor.limpiarClientes();

        assertEquals(0, gestor.cantidadClientes());
    }
}