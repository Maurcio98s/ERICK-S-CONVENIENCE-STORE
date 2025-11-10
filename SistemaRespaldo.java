package ERICK-S-CONVENIENCE-STORE;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Sistema de respaldo y guardado automático
 * Issue #27: Hacer respaldos de la información
 * Issue #25: Guardado automático
 */
public class SistemaRespaldo {
    private String directorioRespaldos;
    private Timer temporizadorAutoguardado;
    private boolean autoguardadoActivo;
    private long intervaloAutoguardado; // en milisegundos

    public SistemaRespaldo() {
        this.directorioRespaldos = "respaldos/";
        this.autoguardadoActivo = false;
        this.intervaloAutoguardado = 300000; // 5 minutos por defecto
        crearDirectorioRespaldos();
    }

    public SistemaRespaldo(String directorioRespaldos) {
        this.directorioRespaldos = directorioRespaldos;
        if (!this.directorioRespaldos.endsWith("/")) {
            this.directorioRespaldos += "/";
        }
        this.autoguardadoActivo = false;
        this.intervaloAutoguardado = 300000;
        crearDirectorioRespaldos();
    }

    /**
     * Crea el directorio de respaldos si no existe
     */
    private void crearDirectorioRespaldos() {
        File directorio = new File(directorioRespaldos);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
    }

    // ============ ISSUE #27: HACER RESPALDOS DE LA INFORMACIÓN ============

    /**
     * Guarda los datos de clientes en un archivo
     */
    public boolean guardarClientes(GestorClientes gestorClientes) {
        if (gestorClientes == null) {
            return false;
        }

        String nombreArchivo = generarNombreArchivo("clientes");
        try (PrintWriter writer = new PrintWriter(new FileWriter(directorioRespaldos + nombreArchivo))) {
            writer.println("=== RESPALDO DE CLIENTES ===");
            writer.println("Fecha: " + new Date());
            writer.println("Total clientes: " + gestorClientes.cantidadClientes());
            writer.println("================================");
            writer.println();

            for (Cliente cliente : gestorClientes.obtenerTodosLosClientes()) {
                writer.println("ID: " + cliente.getId());
                writer.println("Nombre: " + cliente.getNombre());
                writer.println("Cedula: " + cliente.getCedula());
                writer.println("Telefono: " + cliente.getTelefono());
                writer.println("Saldo Deuda: " + cliente.getSaldoDeuda());
                writer.println("Tiene Deuda: " + cliente.tieneDeuda());
                writer.println("Fecha Registro: " + cliente.getFechaRegistro());
                writer.println("---");
            }

            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar clientes: " + e.getMessage());
            return false;
        }
    }

    /**
     * Guarda los datos de pedidos en un archivo
     */
    public boolean guardarPedidos(GestorPedidos gestorPedidos) {
        if (gestorPedidos == null) {
            return false;
        }

        String nombreArchivo = generarNombreArchivo("pedidos");
        try (PrintWriter writer = new PrintWriter(new FileWriter(directorioRespaldos + nombreArchivo))) {
            writer.println("=== RESPALDO DE PEDIDOS ===");
            writer.println("Fecha: " + new Date());
            writer.println("Total pedidos: " + gestorPedidos.cantidadPedidos());
            writer.println("================================");
            writer.println();

            for (PedidoProveedor pedido : gestorPedidos.obtenerHistorialPedidos()) {
                writer.println("ID Pedido: " + pedido.getId());
                writer.println("Proveedor: " + pedido.getProveedor().getNombre());
                writer.println("Empresa: " + pedido.getProveedor().getEmpresa());
                writer.println("Fecha Pedido: " + pedido.getFechaPedido());
                writer.println("Fecha Entrega Estimada: " + pedido.getFechaEntregaEstimada());
                writer.println("Estado: " + pedido.getEstado());
                writer.println("Total: $" + pedido.getTotalPedido());
                writer.println("Cantidad Items: " + pedido.getItems().size());
                
                writer.println("Items:");
                for (PedidoProveedor.ItemPedido item : pedido.getItems()) {
                    writer.println("  - " + item.getProducto() + 
                                 " | Cant: " + item.getCantidad() + 
                                 " | Precio: $" + item.getPrecioUnitario() + 
                                 " | Subtotal: $" + item.getSubtotal());
                }
                writer.println("---");
            }

            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar pedidos: " + e.getMessage());
            return false;
        }
    }

    /**
     * Guarda todos los datos del sistema (respaldo completo)
     */
    public boolean guardarRespaldoCompleto(GestorClientes gestorClientes, GestorPedidos gestorPedidos) {
        boolean clientesOk = guardarClientes(gestorClientes);
        boolean pedidosOk = guardarPedidos(gestorPedidos);
        
        if (clientesOk && pedidosOk) {
            guardarResumenRespaldo(gestorClientes, gestorPedidos);
            return true;
        }
        return false;
    }

    /**
     * Guarda un resumen del respaldo
     */
    private void guardarResumenRespaldo(GestorClientes gestorClientes, GestorPedidos gestorPedidos) {
        String nombreArchivo = generarNombreArchivo("resumen");
        try (PrintWriter writer = new PrintWriter(new FileWriter(directorioRespaldos + nombreArchivo))) {
            writer.println("=== RESUMEN DE RESPALDO ===");
            writer.println("Fecha y hora: " + new Date());
            writer.println("================================");
            writer.println();
            
            writer.println("CLIENTES:");
            writer.println("Total clientes: " + gestorClientes.cantidadClientes());
            writer.println("Clientes con deuda: " + gestorClientes.obtenerClientesConDeuda().size());
            writer.println("Total deudas: $" + gestorClientes.calcularTotalDeudas());
            writer.println();
            
            writer.println("PEDIDOS:");
            GestorPedidos.EstadisticasPedidos stats = gestorPedidos.obtenerEstadisticas();
            writer.println("Total pedidos: " + stats.getTotalPedidos());
            writer.println("Pedidos pendientes: " + stats.getPedidosPendientes());
            writer.println("Pedidos completados: " + stats.getPedidosCompletados());
            writer.println("Pedidos cancelados: " + stats.getPedidosCancelados());
            writer.println("Total gastado: $" + stats.getTotalGastado());
            writer.println();
            
            writer.println("Total proveedores: " + gestorPedidos.cantidadProveedores());
            
        } catch (IOException e) {
            System.err.println("Error al guardar resumen: " + e.getMessage());
        }
    }

    /**
     * Genera un nombre de archivo con timestamp
     */
    private String generarNombreArchivo(String tipo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return tipo + "_" + sdf.format(new Date()) + ".txt";
    }

    // ============ ISSUE #25: GUARDADO AUTOMÁTICO ============

    /**
     * Inicia el guardado automático
     */
    public void iniciarAutoguardado(GestorClientes gestorClientes, GestorPedidos gestorPedidos) {
        if (autoguardadoActivo) {
            throw new IllegalStateException("El autoguardado ya está activo");
        }

        temporizadorAutoguardado = new Timer("AutoguardadoTimer", true);
        temporizadorAutoguardado.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("[Autoguardado] Guardando datos... " + new Date());
                boolean exito = guardarRespaldoCompleto(gestorClientes, gestorPedidos);
                if (exito) {
                    System.out.println("[Autoguardado] Datos guardados exitosamente");
                } else {
                    System.err.println("[Autoguardado] Error al guardar datos");
                }
            }
        }, intervaloAutoguardado, intervaloAutoguardado);

        autoguardadoActivo = true;
        System.out.println("[Autoguardado] Iniciado. Intervalo: " + (intervaloAutoguardado / 1000) + " segundos");
    }

    /**
     * Detiene el guardado automático
     */
    public void detenerAutoguardado() {
        if (temporizadorAutoguardado != null) {
            temporizadorAutoguardado.cancel();
            temporizadorAutoguardado = null;
            autoguardadoActivo = false;
            System.out.println("[Autoguardado] Detenido");
        }
    }

    /**
     * Configura el intervalo de autoguardado en segundos
     */
    public void configurarIntervaloAutoguardado(int segundos) {
        if (segundos < 10) {
            throw new IllegalArgumentException("El intervalo debe ser al menos 10 segundos");
        }
        this.intervaloAutoguardado = segundos * 1000L;
        
        if (autoguardadoActivo) {
            System.out.println("[Autoguardado] Intervalo actualizado. Reiniciando...");
        }
    }

    /**
     * Verifica si el autoguardado está activo
     */
    public boolean estaAutoguardadoActivo() {
        return autoguardadoActivo;
    }

    /**
     * Obtiene el intervalo de autoguardado en segundos
     */
    public int obtenerIntervaloAutoguardado() {
        return (int) (intervaloAutoguardado / 1000);
    }

    // ============ MÉTODOS AUXILIARES ============

    /**
     * Lista todos los archivos de respaldo
     */
    public String[] listarRespaldos() {
        File directorio = new File(directorioRespaldos);
        return directorio.list((dir, name) -> name.endsWith(".txt"));
    }

    /**
     * Obtiene la ruta del directorio de respaldos
     */
    public String getDirectorioRespaldos() {
        return directorioRespaldos;
    }

    /**
     * Elimina respaldos antiguos (mayores a X días)
     */
    public int limpiarRespaldosAntiguos(int diasAntiguedad) {
        File directorio = new File(directorioRespaldos);
        File[] archivos = directorio.listFiles((dir, name) -> name.endsWith(".txt"));
        
        if (archivos == null) {
            return 0;
        }

        long tiempoLimite = System.currentTimeMillis() - (diasAntiguedad * 24L * 60 * 60 * 1000);
        int eliminados = 0;

        for (File archivo : archivos) {
            if (archivo.lastModified() < tiempoLimite) {
                if (archivo.delete()) {
                    eliminados++;
                }
            }
        }

        return eliminados;
    }

    /**
     * Verifica si existe el directorio de respaldos
     */
    public boolean existeDirectorioRespaldos() {
        File directorio = new File(directorioRespaldos);
        return directorio.exists() && directorio.isDirectory();
    }
}