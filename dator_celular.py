# ERICK-S-CONVENIENCE-STORE


from datetime import datetime


def validar_datos_brutos(datos):
   
    if "proveedor" not in datos or not datos["proveedor"]:
        raise ValueError("Falta el proveedor.")

    if "productos" not in datos or not isinstance(datos["productos"], list):
        raise ValueError("Debe incluir al menos un producto.")

    for p in datos["productos"]:
        if "nombre" not in p or "cantidad" not in p or "precio" not in p:
            raise ValueError("Cada producto debe tener nombre, cantidad y precio.")

    return True


def convertir_datos_a_objetos(datos, ClaseProducto, ClasePedido):
 

    # Crear objetos de productos
    productos_obj = []
    for p in datos["productos"]:
        producto = ClaseProducto(
            nombre=p["nombre"],
            cantidad=int(p["cantidad"]),
            precio_unitario=float(p["precio"])
        )
        productos_obj.append(producto)

    # Crear el pedido completo
    pedido = ClasePedido(
        proveedor=datos["proveedor"],
        productos=productos_obj,
        fecha_entrega=datos.get("fecha_entrega"),
    )

    return pedido


def procesar_entrada_desde_celular(datos_crudos, ClaseProducto, ClasePedido):


    # Paso 1: Validación
    validar_datos_brutos(datos_crudos)

    # Paso 2: Conversión
    pedido = convertir_datos_a_objetos(datos_crudos, ClaseProducto, ClasePedido)

    return pedido


def generar_respuesta_movil(exito=True, mensaje="", pedido_id=None):
   
    return {
        "exito": exito,
        "mensaje": mensaje,
        "pedido_id": pedido_id,
        "timestamp": datetime.now().isoformat(),
    }
