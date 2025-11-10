
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase que gestiona las operaciones con clientes
 * Issue #5: Buscar un cliente
 * Issue #7: Marcar deuda como saldada
 */
public class GestorClientes {
    private List<Cliente> clientes;
    private int siguienteId;

    public GestorClientes() {
        this.clientes = new ArrayList<>();
        this.siguienteId = 1;
    }

    /**
     * Agrega un nuevo cliente al sistema
     */
    public Cliente agregarCliente(String nombre, String cedula, String telefono) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula no puede estar vacía");
        }

        // Verificar si ya existe un cliente con esa cédula
        if (buscarClientePorCedula(cedula) != null) {
            throw new IllegalArgumentException("Ya existe un cliente con esa cédula");
        }

        Cliente nuevoCliente = new Cliente(siguienteId++, nombre, cedula, telefono);
        clientes.add(nuevoCliente);
        return nuevoCliente;
    }

    /**
     * Issue #5: Busca un cliente por su ID
     */
    public Cliente buscarClientePorId(int id) {
        return clientes.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Issue #5: Busca un cliente por su nombre (búsqueda exacta)
     */
    public Cliente buscarClientePorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return null;
        }

        return clientes.stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(nombre.trim()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Issue #5: Busca un cliente por su cédula
     */
    public Cliente buscarClientePorCedula(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            return null;
        }

        return clientes.stream()
                .filter(c -> c.getCedula().equals(cedula.trim()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Issue #5: Búsqueda parcial por nombre (contiene)
     */
    public List<Cliente> buscarClientesPorNombreParcial(String nombreParcial) {
        if (nombreParcial == null || nombreParcial.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return clientes.stream()
                .filter(c -> c.getNombre().toLowerCase()
                        .contains(nombreParcial.toLowerCase().trim()))
                .collect(Collectors.toList());
    }

    /**
     * Issue #7: Marca la deuda de un cliente como saldada
     */
    public boolean marcarDeudaComoPagada(int idCliente) {
        Cliente cliente = buscarClientePorId(idCliente);
        if (cliente == null) {
            return false;
        }
        cliente.saldarDeuda();
        return true;
    }

    /**
     * Issue #7: Marca la deuda como saldada usando el objeto Cliente
     */
    public void saldarDeuda(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }
        cliente.saldarDeuda();
    }

    /**
     * Registra un pago parcial de un cliente
     */
    public boolean registrarPago(int idCliente, double monto) {
        Cliente cliente = buscarClientePorId(idCliente);
        if (cliente == null) {
            return false;
        }
        try {
            cliente.registrarPago(monto);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Obtiene todos los clientes con deuda
     */
    public List<Cliente> obtenerClientesConDeuda() {
        return clientes.stream()
                .filter(Cliente::tieneDeuda)
                .collect(Collectors.toList());
    }

    /**
     * Calcula el total de deudas de todos los clientes
     */
    public double calcularTotalDeudas() {
        return clientes.stream()
                .mapToDouble(Cliente::getSaldoDeuda)
                .sum();
    }

    /**
     * Obtiene la lista completa de clientes
     */
    public List<Cliente> obtenerTodosLosClientes() {
        return new ArrayList<>(clientes);
    }

    /**
     * Obtiene la cantidad total de clientes registrados
     */
    public int cantidadClientes() {
        return clientes.size();
    }

    /**
     * Elimina un cliente del sistema
     */
    public boolean eliminarCliente(int idCliente) {
        return clientes.removeIf(c -> c.getId() == idCliente);
    }

    /**
     * Limpia todos los clientes (útil para testing)
     */
    public void limpiarClientes() {
        clientes.clear();
        siguienteId = 1;
    }
}