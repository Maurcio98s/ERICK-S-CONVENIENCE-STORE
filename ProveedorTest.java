package ERICK-S-CONVENIENCE-STORE;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para la clase Proveedor
 */
public class ProveedorTest {
    
    private Proveedor proveedor;

    @BeforeEach
    public void setUp() {
        proveedor = new Proveedor(1, "Juan Distribuidora", "Distribuidora del Norte", "3001234567", "juan@distribuidora.com");
    }

    @Test
    @DisplayName("Debe crear un proveedor correctamente")
    public void testCrearProveedor() {
        assertNotNull(proveedor);
        assertEquals(1, proveedor.getId());
        assertEquals("Juan Distribuidora", proveedor.getNombre());
        assertEquals("Distribuidora del Norte", proveedor.getEmpresa());
        assertEquals("3001234567", proveedor.getTelefono());
        assertEquals("juan@distribuidora.com", proveedor.getEmail());
        assertTrue(proveedor.isActivo());
    }

    @Test
    @DisplayName("Debe iniciar con lista de productos vacía")
    public void testListaProductosVacia() {
        assertEquals(0, proveedor.getProductosQueSupministra().size());
    }

    @Test
    @DisplayName("Debe agregar un producto correctamente")
    public void testAgregarProducto() {
        proveedor.agregarProducto("Arroz");
        
        assertEquals(1, proveedor.getProductosQueSupministra().size());
        assertTrue(proveedor.suministraProducto("Arroz"));
    }

    @Test
    @DisplayName("Debe agregar múltiples productos")
    public void testAgregarMultiplesProductos() {
        proveedor.agregarProducto("Arroz");
        proveedor.agregarProducto("Frijol");
        proveedor.agregarProducto("Azúcar");
        proveedor.agregarProducto("Aceite");
        
        assertEquals(4, proveedor.getProductosQueSupministra().size());
        assertTrue(proveedor.suministraProducto("Arroz"));
        assertTrue(proveedor.suministraProducto("Frijol"));
        assertTrue(proveedor.suministraProducto("Azúcar"));
        assertTrue(proveedor.suministraProducto("Aceite"));
    }

    @Test
    @DisplayName("No debe agregar producto vacío")
    public void testNoAgregarProductoVacio() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            proveedor.agregarProducto("");
        });
        assertEquals("El producto no puede estar vacío", exception.getMessage());
    }

    @Test
    @DisplayName("No debe agregar producto nulo")
    public void testNoAgregarProductoNulo() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            proveedor.agregarProducto(null);
        });
        assertEquals("El producto no puede estar vacío", exception.getMessage());
    }

    @Test
    @DisplayName("No debe agregar productos duplicados")
    public void testNoAgregarProductosDuplicados() {
        proveedor.agregarProducto("Arroz");
        proveedor.agregarProducto("Arroz");
        proveedor.agregarProducto("Arroz");
        
        assertEquals(1, proveedor.getProductosQueSupministra().size());
    }

    @Test
    @DisplayName("Debe verificar correctamente si suministra un producto")
    public void testSuministraProducto() {
        proveedor.agregarProducto("Arroz");
        proveedor.agregarProducto("Frijol");
        
        assertTrue(proveedor.suministraProducto("Arroz"));
        assertTrue(proveedor.suministraProducto("Frijol"));
        assertFalse(proveedor.suministraProducto("Azúcar"));
    }

    @Test
    @DisplayName("Debe cambiar el estado activo/inactivo")
    public void testCambiarEstadoActivo() {
        assertTrue(proveedor.isActivo());
        
        proveedor.setActivo(false);
        assertFalse(proveedor.isActivo());
        
        proveedor.setActivo(true);
        assertTrue(proveedor.isActivo());
    }

    @Test
    @DisplayName("Debe actualizar nombre del proveedor")
    public void testActualizarNombre() {
        proveedor.setNombre("Nuevo Nombre");
        assertEquals("Nuevo Nombre", proveedor.getNombre());
    }

    @Test
    @DisplayName("Debe actualizar empresa del proveedor")
    public void testActualizarEmpresa() {
        proveedor.setEmpresa("Nueva Empresa SA");
        assertEquals("Nueva Empresa SA", proveedor.getEmpresa());
    }

    @Test
    @DisplayName("Debe actualizar teléfono del proveedor")
    public void testActualizarTelefono() {
        proveedor.setTelefono("3109876543");
        assertEquals("3109876543", proveedor.getTelefono());
    }

    @Test
    @DisplayName("Debe actualizar email del proveedor")
    public void testActualizarEmail() {
        proveedor.setEmail("nuevo@email.com");
        assertEquals("nuevo@email.com", proveedor.getEmail());
    }

    @Test
    @DisplayName("La lista de productos debe ser inmutable desde afuera")
    public void testListaProductosInmutable() {
        proveedor.agregarProducto("Arroz");
        proveedor.agregarProducto("Frijol");
        
        var lista = proveedor.getProductosQueSupministra();
        assertEquals(2, lista.size());
        
        // Intentar modificar la lista retornada no debe afectar al proveedor
        lista.add("Azúcar");
        
        assertEquals(2, proveedor.getProductosQueSupministra().size());
        assertFalse(proveedor.suministraProducto("Azúcar"));
    }

    @Test
    @DisplayName("El toString debe mostrar información del proveedor")
    public void testToString() {
        String resultado = proveedor.toString();
        
        assertTrue(resultado.contains("Juan Distribuidora"));
        assertTrue(resultado.contains("Distribuidora del Norte"));
        assertTrue(resultado.contains("activo=true"));
    }

    @Test
    @DisplayName("Debe mantener consistencia después de múltiples operaciones")
    public void testConsistenciaMultiplesOperaciones() {
        proveedor.agregarProducto("Arroz");
        proveedor.agregarProducto("Frijol");
        proveedor.setNombre("Proveedor Actualizado");
        proveedor.agregarProducto("Azúcar");
        proveedor.setActivo(false);
        proveedor.agregarProducto("Aceite");
        
        assertEquals("Proveedor Actualizado", proveedor.getNombre());
        assertEquals(4, proveedor.getProductosQueSupministra().size());
        assertFalse(proveedor.isActivo());
    }
}