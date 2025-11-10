# ERICK-S-CONVENIENCE-STORE

from datetime import datetime


def editar_cantidad(producto, nueva_cantidad):
    """
    Modifica la cantidad de un producto dentro de un pedido.
    """
    if nueva_cantidad <= 0:
        raise ValueError("La cantidad debe ser mayor a 0")

    producto.cantidad = nueva_cantidad
    return f"Cantidad actualizada a {nueva_cantidad} para {producto.nombre}"


def actualizar_estado(pedido, nuevo_estado):
    """
    Actualiza el estado del pedido.
    Estados válidos: pendiente, enviado, recibido, cancelado
    """
    if nuevo_estado not in pedido.ESTADOS_VALIDOS:
        raise ValueError(f"Estado inválido. Debe ser uno de: {pedido.ESTADOS_VALIDOS}")

    pedido.estado = nuevo_estado
    pedido.fecha_modificacion = datetime.now()
    return f"Estado del pedido actualizado a: {nuevo_estado}"


def agregar_producto(pedido, producto):
    """
    Agrega un producto al pedido.
    """
    pedido.productos.append(producto)
    pedido.fecha_modificacion = datetime.now()
    return f"Producto '{producto.nombre}' agregado correctamente."


def eliminar_producto(pedido, nombre_producto):
    """
    Elimina un producto del pedido buscando por nombre.
    """
    for p in pedido.productos:
        if p.nombre.lower() == nombre_producto.lower():
            pedido.productos.remove(p)
            pedido.fecha_modificacion = datetime.now()
            return f"Producto '{nombre_producto}' eliminado correctamente."

    raise ValueError(f"No se encontró el producto: {nombre_producto}")


def mostrar_resumen_pedido(pedido):
    """
    Retorna un resumen completo del pedido para verificar cambios.
    """
    resumen = f"""
--- Resumen del Pedido ---
Proveedor: {pedido.proveedor}
Fecha creación: {pedido.fecha_creacion.strftime("%Y-%m-%d %H:%M")}
Estado: {pedido.estado}

Productos:
"""

    for p in pedido.productos:
        resumen += f"   - {p.nombre} x {p.cantidad} (Subtotal: {p.subtotal:.2f})\n"

    resumen += f"\nTotal del pedido: {pedido.total():.2f}\n"
    return resumen
