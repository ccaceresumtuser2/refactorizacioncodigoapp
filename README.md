# Refactorización de código en Java

Tres ejemplos de refactorización con **Programación Orientada a Objetos**, de menor a mayor dificultad.
Cada ejemplo tiene dos versiones del mismo programa:

- **`sinsol`**: código *sin* refactorizar, con problemas de diseño (*code smells*) a propósito.
- **`consol`**: el mismo comportamiento, ya refactorizado.

Las pruebas unitarias (JUnit 5) comprueban que las dos versiones den **los mismos resultados**:
refactorizar es mejorar la estructura del código **sin cambiar lo que hace**.

📖 La teoría (code smells, técnicas, SOLID, patrones y proceso) está en [teoria.md](teoria.md).

## Requisitos

- Java 21
- Maven 3.9 o superior (o NetBeans / IntelliJ / VS Code con soporte para Maven)

## Cómo ejecutarlo

```bash
# Ejecutar todas las pruebas
mvn test

# Ejecutar la demostración (clase principal)
mvn compile exec:java
```

En **NetBeans**: clic derecho en el proyecto → *Test* (Alt+F6) para las pruebas, o *Run* (F6) para la demostración.

## Estructura

```
src/main/java/com/udc/reut/cod/app/refactorizacioncodigoapp/
├── Refactorizacioncodigoapp.java      Demostración de las versiones refactorizadas
├── basico/
│   ├── sinsol/                        Factura
│   └── consol/                        Factura, LineaFactura, TipoCliente
├── intermedio/
│   ├── sinsol/                        Nomina
│   └── consol/                        Empleado, EmpleadoPlanta, EmpleadoPorHoras, Vendedor, Nomina
└── avanzado/
    ├── sinsol/                        ProcesadorPedidos
    └── consol/
        ├── modelo/                    Pedido (+ Builder), ItemPedido, Producto, Cliente, Recibo
        ├── cupon/                     Cupon, SinCupon, CuponPorcentaje, CuponEnvioGratis, CatalogoCupones
        ├── envio/                     MetodoEnvio, EnvioEstandar, EnvioExpress, EnvioInternacional
        ├── pago/                      MetodoPago, PagoTarjeta, PagoEfectivo
        ├── inventario/                Inventario, InventarioEnMemoria
        ├── notificacion/              Notificador, NotificadorEmail, NotificadorSms
        ├── excepciones/               PedidoInvalidoException, StockInsuficienteException
        └── servicio/                  ProcesadorPedidos

src/test/java/...                      Misma estructura, una clase de prueba por versión
diagramas/diagrama-clases.drawio       Diagrama de clases UML, una pestaña por paquete
```

---

## Ejemplo 1 · Básico: Factura

Calcula el total de una factura: suma de productos, descuento según el tipo de cliente e IVA (19 %).

| Problema en `sinsol` | Refactorización en `consol` |
|---|---|
| Nombres sin significado: `calc`, `p`, `c`, `t`, `s`, `d`, `r` | **Renombrar** variables y métodos |
| Números mágicos: `0.1`, `0.05`, `0.19` | **Reemplazar número mágico por constante** |
| Un método hace todo el cálculo | **Extraer método**: `calcularSubtotal`, `calcularDescuento`, `calcularIva`, `calcularTotal` |
| Arreglos paralelos `precios[]` y `cantidades[]` | **Reemplazar arreglos por objeto**: `LineaFactura` |
| Tipo de cliente como `String` | **Reemplazar código de tipo por enum**: `TipoCliente` conoce su porcentaje de descuento |

```java
// Antes
double total = new Factura().calc(new double[]{10000, 5000}, new int[]{2, 3}, "VIP");

// Después
Factura factura = new Factura(TipoCliente.VIP);
factura.agregarLinea(10_000, 2);
factura.agregarLinea(5_000, 3);
double total = factura.calcularTotal();   // 37485.0
```

## Ejemplo 2 · Intermedio: Nómina

Calcula el pago de cada empleado según su tipo:

- **De planta**: salario base, más auxilio de transporte (162.000) si gana menos de 2.000.000.
- **Por horas**: 15.000 por hora, con un recargo del 25 % en las horas por encima de 160.
- **Vendedor**: salario base más el 3 % de comisión sobre las ventas.

| Problema en `sinsol` | Refactorización en `consol` |
|---|---|
| `if/else` sobre un `String` por cada tipo de empleado | **Reemplazar condicional por polimorfismo**: clase abstracta `Empleado` con subclases |
| Agregar un tipo nuevo obliga a modificar `Nomina` | **Principio Abierto/Cerrado (OCP)**: se agrega una subclase y `Nomina` no cambia |
| Lista larga de parámetros que no aplican a todos (`horas`, `ventas`) | Cada subclase recibe solo los datos que necesita |
| Arreglos paralelos en `calcularTotalNomina` | `Nomina` contiene una `List<Empleado>` |

```java
Nomina nomina = new Nomina();
nomina.agregarEmpleado(new EmpleadoPlanta("Ana", 1_500_000));
nomina.agregarEmpleado(new EmpleadoPorHoras("Eva", 170));
nomina.agregarEmpleado(new Vendedor("Sol", 1_000_000, 10_000_000));
double total = nomina.calcularTotal();   // 5549500.0
```

## Ejemplo 3 · Avanzado: Procesamiento de pedidos

Un pedido de tienda en línea pasa por varios pasos: validación, subtotal, cupón de descuento, costo de envío,
recargo por medio de pago, actualización del inventario y aviso al cliente.

**Reglas de negocio**

| Concepto | Regla |
|---|---|
| Cupón `DESC10` | 10 % de descuento |
| Cupón `DESC20` | 20 % de descuento si el subtotal es de 100.000 o más |
| Cupón `ENVIOGRATIS` | El envío cuesta 0 |
| Envío `ESTANDAR` | 10.000; gratis si el monto con descuento es de 200.000 o más |
| Envío `EXPRESS` / `INTERNACIONAL` | 25.000 / 50.000 |
| Pago `TARJETA` / `EFECTIVO` | Recargo del 3 % sobre (monto + envío) / sin recargo |

| Problema en `sinsol` | Refactorización en `consol` |
|---|---|
| **Método Dios**: un solo método con 7 responsabilidades | **SRP**: `ProcesadorPedidos` solo coordina; cada responsabilidad tiene su clase |
| Cadenas de `if/else` para cupón, envío y pago | **Patrón Strategy**: interfaces `Cupon`, `MetodoEnvio`, `MetodoPago` |
| `if (cupon != null ...)` | **Patrón Null Object**: `SinCupon` |
| Convertir el texto `"DESC10"` en una regla | **Factory / Registro**: `CatalogoCupones` |
| Método con 7 parámetros sueltos | **Patrón Builder**: `Pedido.para(cliente).agregar(...).construir()` |
| Datos `static` globales (`precios`, `stock`) | **DIP + inyección de dependencias**: interfaz `Inventario` |
| Avisos escritos directamente con `System.out` | Interfaz `Notificador` (Observer simplificado) |
| Excepciones genéricas de Java | Excepciones del negocio: `PedidoInvalidoException`, `StockInsuficienteException` |
| Datos primitivos sueltos | Records inmutables: `Producto`, `ItemPedido`, `Cliente`, `Recibo` |
| **Bug**: si un producto aparece dos veces, el stock queda negativo | Las cantidades se agrupan por producto antes de validar el stock |

```java
InventarioEnMemoria inventario = new InventarioEnMemoria();
inventario.registrar(laptop, 5);
inventario.registrar(mouse, 10);
ProcesadorPedidos procesador = new ProcesadorPedidos(inventario,
        List.of(new NotificadorEmail(), new NotificadorSms()));

Pedido pedido = Pedido.para(new Cliente("Ana", "ana@mail.com", "3001234567"))
        .agregar(laptop, 1)
        .agregar(mouse, 2)
        .conCupon(CatalogoCupones.porDefecto().buscar("DESC10"))
        .enviarPor(new EnvioEstandar())
        .pagarCon(new PagoTarjeta())
        .construir();

Recibo recibo = procesador.procesar(pedido);   // total = 186430.0
```

---

## Pruebas

| Paquete | Qué demuestran |
|---|---|
| `*/sinsol` | **Pruebas de caracterización**: registran cómo se comporta hoy el código heredado, *antes* de tocarlo. En el ejemplo avanzado se nota lo difícil que es probarlo: hay que reiniciar los datos `static` y capturar `System.out`. |
| `*/consol` | Los mismos casos dan los mismos resultados. Además prueban la extensibilidad (un empleado o un cupón nuevo sin modificar el código existente), el encapsulamiento y los avisos usando un objeto falso (*fake*) en lugar de la consola. |

En total son 44 pruebas. El único cambio de comportamiento a propósito es la corrección del bug del stock negativo:
`sinsol` lo **documenta** y `consol` lo **corrige**.

## Diagrama de clases

[`diagramas/diagrama-clases.drawio`](diagramas/diagrama-clases.drawio) tiene **una pestaña por paquete**.
Se abre en [app.diagrams.net](https://app.diagrams.net) (*File → Open from → Device*) o con la extensión
*Draw.io Integration* de VS Code.

Colores: rojo = sin refactorizar · verde = refactorizado · amarillo = interfaz · morado = record · azul = enum.

## Ejercicios propuestos

1. **Básico**: agregar el tipo de cliente `EMPLEADO` con 15 % de descuento. ¿Cuántas líneas cambian en cada versión?
2. **Intermedio**: agregar un `EmpleadoTemporal` que cobra un valor fijo por contrato, sin modificar `Nomina`.
3. **Avanzado**: agregar un pago `PSE` con un recargo fijo de 2.500 y un cupón `2X1`. Escribir primero las pruebas.
4. **Avanzado**: cambiar `double` por `BigDecimal` para manejar el dinero sin errores de redondeo.
