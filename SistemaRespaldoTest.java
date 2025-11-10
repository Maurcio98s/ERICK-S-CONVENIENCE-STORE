package ERICK-S-CONVENIENCE-STORE;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.util.Date;

/**
 * Tests para SistemaRespaldo
 * Issue #27: Hacer respaldos de la información
 * Issue #25: Guardado automático
 */
public class SistemaRespaldoTest {
    
    private SistemaRespaldo sistemaRespaldo;
    private GestorClientes gestorClientes;
    private GestorPedidos gestorPedidos;
    private String directorioTest = "respaldos_test/";

    @BeforeEach
    public void setUp() {
        sistemaRespaldo = new SistemaRespaldo(directorioTest);
        gestorClientes = new GestorClientes();
        gestorPedidos = new GestorPedidos();
        
        // Datos de prueba
        Cliente cliente1 = gestorClientes.agregarCliente("Pedro Gómez", "1234567890", "3001234567");
        Cliente cliente2 = gestorClientes.agregarCliente("Ana López", "9876543210", "3109876543");
        cliente1.agregarCompra(50000.0);
        cliente2.agregarCompra(30000.0);
        
        Proveedor proveedor = gestorPedidos.agregarProveedor("Distribuidora Norte", "Norte SA", "3201234567", "norte@test.com");
        PedidoProveedor pedido = gestorPedidos.crearPedido(proveedor.getId(), new Date());
        gestorPedidos.agregarItemAPedido(pedido.getId(), "Arroz", 50, 3000.0);
    }

    @AfterEach
    public void tearDown() {
        // Limpiar directorio de pruebas
        File directorio = new File(directorioTest);
        if (directorio.exists()) {
            File[] archivos = directorio.listFiles();
            if (archivos != null) {
                for (File archivo : archivos) {
                    archivo.delete();
                }
            }
            directorio.delete();
        }
        
        // Detener autoguardado si está activo
        if (sistemaRespaldo.estaAutoguardadoActivo()) {
            sistemaRespaldo.detenerAutoguardado();
        }
    }

    // ============ TESTS ISSUE #27: HACER RESPALDOS ============

    @Test
    @DisplayName("Issue #27: Debe crear el directorio de respaldos automáticamente")
    public void testCrearDirectorioRespaldos() {
        assertTrue(sistemaRespaldo.existeDirectorioRespaldos());
        File directorio = new File(directorioTest);
        assertTrue(directorio.exists());
        assertTrue(directorio.isDirectory());
    }

    @Test
    @DisplayName("Issue #27: Debe guardar respaldo de clientes correctamente")
    public void testGuardarClientes() {
        boolean resultado = sistemaRespaldo.guardarClientes(gestorClientes);
        
        assertTrue(resultado);
        String[] archivos = sistemaRespaldo.listarRespaldos();
        assertNotNull(archivos);
        assertTrue(archivos.length > 0);
        
        // Verificar que existe un archivo de clientes
        boolean tieneArchivoClientes = false;
        for (String archivo : archivos) {
            if (archivo.contains("clientes")) {
                tieneArchivoClientes = true;
                break;
            }
        }
        assertTrue(tieneArchivoClientes);
    }

    @Test
    @DisplayName("Issue #27: Debe guardar respaldo de pedidos correctamente")
    public void testGuardarPedidos() {
        boolean resultado = sistemaRespaldo.guardarPedidos(gestorPedidos);
        
        assertTrue(resultado);
        String[] archivos = sistemaRespaldo.listarRespaldos();
        assertNotNull(archivos);
        assertTrue(archivos.length > 0);
        
        // Verificar que existe un archivo de pedidos
        boolean tieneArchivoPedidos = false;
        for (String archivo : archivos) {
            if (archivo.contains("pedidos")) {
                tieneArchivoPedidos = true;
                break;
            }
        }
        assertTrue(tieneArchivoPedidos);
    }

    @Test
    @DisplayName("Issue #27: Debe guardar respaldo completo")
    public void testGuardarRespaldoCompleto() {
        boolean resultado = sistemaRespaldo.guardarRespaldoCompleto(gestorClientes, gestorPedidos);
        
        assertTrue(resultado);
        String[] archivos = sistemaRespaldo.listarRespaldos();
        assertNotNull(archivos);
        assertTrue(archivos.length >= 3); // clientes, pedidos y resumen
        
        // Verificar que existen los 3 tipos de archivos
        boolean tieneClientes = false;
        boolean tienePedidos = false;
        boolean tieneResumen = false;
        
        for (String archivo : archivos) {
            if (archivo.contains("clientes")) tieneClientes = true;
            if (archivo.contains("pedidos")) tienePedidos = true;
            if (archivo.contains("resumen")) tieneResumen = true;
        }
        
        assertTrue(tieneClientes);
        assertTrue(tienePedidos);
        assertTrue(tieneResumen);
    }

    @Test
    @DisplayName("Issue #27: Debe manejar gestor nulo sin generar error")
    public void testGuardarConGestorNulo() {
        boolean resultado = sistemaRespaldo.guardarClientes(null);
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Issue #27: Debe listar todos los respaldos")
    public void testListarRespaldos() {
        sistemaRespaldo.guardarClientes(gestorClientes);
        sistemaRespaldo.guardarPedidos(gestorPedidos);
        
        String[] archivos = sistemaRespaldo.listarRespaldos();
        
        assertNotNull(archivos);
        assertTrue(archivos.length >= 2);
    }

    @Test
    @DisplayName("Issue #27: Debe obtener el directorio de respaldos correcto")
    public void testObtenerDirectorioRespaldos() {
        String directorio = sistemaRespaldo.getDirectorioRespaldos();
        assertEquals(directorioTest, directorio);
    }

    // ============ TESTS ISSUE #25: GUARDADO AUTOMÁTICO ============

    @Test
    @DisplayName("Issue #25: Debe iniciar autoguardado correctamente")
    public void testIniciarAutoguardado() {
        assertFalse(sistemaRespaldo.estaAutoguardadoActivo());
        
        sistemaRespaldo.configurarIntervaloAutoguardado(1); // 1 segundo para test
        sistemaRespaldo.iniciarAutoguardado(gestorClientes, gestorPedidos);
        
        assertTrue(sistemaRespaldo.estaAutoguardadoActivo());
    }

    @Test
    @DisplayName("Issue #25: No debe permitir iniciar autoguardado si ya está activo")
    public void testNoIniciarAutoguardadoSiYaActivo() {
        sistemaRespaldo.configurarIntervaloAutoguardado(1);
        sistemaRespaldo.iniciarAutoguardado(gestorClientes, gestorPedidos);
        
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            sistemaRespaldo.iniciarAutoguardado(gestorClientes, gestorPedidos);
        });
        assertEquals("El autoguardado ya está activo", exception.getMessage());
    }

    @Test
    @DisplayName("Issue #25: Debe detener autoguardado correctamente")
    public void testDetenerAutoguardado() {
        sistemaRespaldo.configurarIntervaloAutoguardado(1);
        sistemaRespaldo.iniciarAutoguardado(gestorClientes, gestorPedidos);
        assertTrue(sistemaRespaldo.estaAutoguardadoActivo());
        
        sistemaRespaldo.detenerAutoguardado();
        
        assertFalse(sistemaRespaldo.estaAutoguardadoActivo());
    }

    @Test
    @DisplayName("Issue #25: Debe configurar intervalo de autoguardado")
    public void testConfigurarIntervaloAutoguardado() {
        sistemaRespaldo.configurarIntervaloAutoguardado(60);
        assertEquals(60, sistemaRespaldo.obtenerIntervaloAutoguardado());
        
        sistemaRespaldo.configurarIntervaloAutoguardado(120);
        assertEquals(120, sistemaRespaldo.obtenerIntervaloAutoguardado());
    }

    @Test
    @DisplayName("Issue #25: No debe permitir intervalo menor a 10 segundos")
    public void testIntervaloMinimoAutoguardado() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            sistemaRespaldo.configurarIntervaloAutoguardado(5);
        });
        assertEquals("El intervalo debe ser al menos 10 segundos", exception.getMessage());
    }

    @Test
    @DisplayName("Issue #25: El autoguardado debe crear archivos periódicamente")
    public void testAutoguardadoCreaArchivos() throws InterruptedException {
        sistemaRespaldo.configurarIntervaloAutoguardado(2); // 2 segundos
        sistemaRespaldo.iniciarAutoguardado(gestorClientes, gestorPedidos);
        
        // Esperar un poco más del intervalo
        Thread.sleep(2500);
        
        String[] archivos = sistemaRespaldo.listarRespaldos();
        assertNotNull(archivos);
        assertTrue(archivos.length >= 3); // Al menos un respaldo completo
        
        sistemaRespaldo.detenerAutoguardado();
    }

    @Test
    @DisplayName("Issue #25: Debe mantener el estado correcto del autoguardado")
    public void testEstadoAutoguardado() {
        assertFalse(sistemaRespaldo.estaAutoguardadoActivo());
        
        sistemaRespaldo.configurarIntervaloAutoguardado(1);
        sistemaRespaldo.iniciarAutoguardado(gestorClientes, gestorPedidos);
        assertTrue(sistemaRespaldo.estaAutoguardadoActivo());
        
        sistemaRespaldo.detenerAutoguardado();
        assertFalse(sistemaRespaldo.estaAutoguardadoActivo());
    }

    // ============ TESTS DE FUNCIONALIDADES ADICIONALES ============

    @Test
    @DisplayName("Debe limpiar respaldos antiguos")
    public void testLimpiarRespaldosAntiguos() {
        // Crear algunos respaldos
        sistemaRespaldo.guardarClientes(gestorClientes);
        sistemaRespaldo.guardarPedidos(gestorPedidos);
        
        // Intentar limpiar respaldos de más de 30 días (no debería eliminar los recientes)
        int eliminados = sistemaRespaldo.limpiarRespaldosAntiguos(30);
        
        assertEquals(0, eliminados);
        
        String[] archivos = sistemaRespaldo.listarRespaldos();
        assertTrue(archivos.length >= 2);
    }

    @Test
    @DisplayName("Debe verificar existencia del directorio de respaldos")
    public void testVerificarExistenciaDirectorio() {
        assertTrue(sistemaRespaldo.existeDirectorioRespaldos());
        
        File directorio = new File(directorioTest);
        directorio.delete();
        
        assertFalse(sistemaRespaldo.existeDirectorioRespaldos());
    }

    @Test
    @DisplayName("Debe crear múltiples respaldos sin sobrescribir")
    public void testMultiplesRespaldosSinSobrescribir() throws InterruptedException {
        sistemaRespaldo.guardarClientes(gestorClientes);
        Thread.sleep(1100); // Esperar para que cambie el timestamp
        sistemaRespaldo.guardarClientes(gestorClientes);
        Thread.sleep(1100);
        sistemaRespaldo.guardarClientes(gestorClientes);
        
        String[] archivos = sistemaRespaldo.listarRespaldos();
        
        // Contar archivos de clientes
        int contadorClientes = 0;
        for (String archivo : archivos) {
            if (archivo.contains("clientes")) {
                contadorClientes++;
            }
        }
        
        assertTrue(contadorClientes >= 3);
    }

    @Test
    @DisplayName("Los nombres de archivo deben incluir timestamp")
    public void testNombresArchivoConTimestamp() {
        sistemaRespaldo.guardarClientes(gestorClientes);
        
        String[] archivos = sistemaRespaldo.listarRespaldos();
        assertTrue(archivos.length > 0);
        
        // Verificar que el nombre contiene números (fecha y hora)
        String primerArchivo = archivos[0];
        assertTrue(primerArchivo.matches(".\\d+."));
    }
}