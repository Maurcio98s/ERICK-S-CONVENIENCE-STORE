# ERICK-S-CONVENIENCE-STORE
# gestión_de_pedido_a_proveedores.py


from datetime import datetime


class ProductoProveedor:
    def __init__(self, nombre: str, cantidad: int, precio_unitario: float):
        self.nombre = nombre
        self.cantidad = cantidad
        self.precio_unitario = precio_unitario

    @property
    def subtotal(self):
        return self.cantidad * self.precio_unitario

    def __repr__(self):
        return f"{self.nombre} x {self.cantidad} = ${self.subtotal:.2f}"


class PedidoProveedor:
    ESTADOS_VALIDOS = ["pendiente", "enviado", "recibido", "cancelado"]

    def __init__(self, proveedor: str):
        self.proveedor = proveedor
        self.fecha_creacion = datetime.now()
        self.productos = []
        self.estado = "pendiente"

    def agregar_producto(self, nombre: str, cantidad: int, precio_unitario: float):
        producto = ProductoProveedor(nombre, cantidad, precio_unitario)
        self.productos.append(producto)

    def cambiar_estado(self, nuevo_estado: str):
        if nuevo_estado not in self.ESTADOS_VALIDOS:
            raise ValueError(f"Estado inválido. Debe ser uno de: {self.ESTADOS_VALIDOS}")
        self.estado = nuevo_estado

    @property
    def total(self):
        return sum(p.subtotal for p in self.productos)

    def resumen(self):
        print(f"--- Pedido a proveedor: {self.proveedor} ---")
        print(f"Fecha: {self.fecha_creacion.strftime('%Y-%m-%d %H:%M')}")
        print(f"Estado: {self.estado}")
        print("\nProductos:")
        for p in self.productos:
            print(f" - {p}")
        print(f"\nTOTAL: ${self.total:.2f}")

    def __repr__(self):
        return f"PedidoProveedor(proveedor='{self.proveedor}', estado='{self.estado}', total={self.total})"


