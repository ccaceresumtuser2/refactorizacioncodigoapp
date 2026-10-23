# Teoría de la refactorización de código

> *"Cualquier tonto puede escribir código que una computadora entienda. Los buenos programadores escriben código que los humanos pueden entender."*
> — Martin Fowler, *Refactoring* (1999)

## Contenido

1. [¿Qué es refactorizar?](#1-qué-es-refactorizar)
2. [¿Por qué refactorizar?](#2-por-qué-refactorizar)
3. [¿Cuándo refactorizar y cuándo no?](#3-cuándo-refactorizar-y-cuándo-no)
4. [La red de seguridad: pruebas automatizadas](#4-la-red-de-seguridad-pruebas-automatizadas)
5. [Code smells (malos olores)](#5-code-smells-malos-olores)
6. [Catálogo de técnicas de refactorización](#6-catálogo-de-técnicas-de-refactorización)
7. [Principios de diseño orientado a objetos](#7-principios-de-diseño-orientado-a-objetos)
8. [Patrones de diseño que surgen al refactorizar](#8-patrones-de-diseño-que-surgen-al-refactorizar)
9. [Proceso paso a paso](#9-proceso-paso-a-paso)
10. [Deuda técnica](#10-deuda-técnica)
11. [Herramientas](#11-herramientas)
12. [Relación con los ejemplos del proyecto](#12-relación-con-los-ejemplos-del-proyecto)
13. [Bibliografía](#13-bibliografía)

---

## 1. ¿Qué es refactorizar?

Martin Fowler da dos definiciones:

- **Refactorización (sustantivo):** un cambio en la estructura interna del software para que sea más fácil de entender y más barato de modificar, **sin cambiar su comportamiento observable**.
- **Refactorizar (verbo):** reestructurar el software aplicando una serie de refactorizaciones, **sin cambiar su comportamiento observable**.

Esto implica tres cosas:

| Característica | Significado |
|---|---|
| **Conserva el comportamiento** | Con las mismas entradas, el programa da las mismas salidas. El usuario no nota la diferencia. |
| **Mejora la estructura interna** | El código queda más legible, más cohesivo, menos acoplado y más fácil de probar. |
| **Avanza en pasos pequeños** | Cada paso es tan pequeño que es difícil equivocarse, y después de cada paso el programa sigue funcionando. |

### Lo que NO es refactorizar

| Actividad | Por qué no es refactorizar |
|---|---|
| Agregar una funcionalidad | Cambia el comportamiento. |
| Corregir un bug | Cambia el comportamiento, aunque sea para bien. |
| Optimizar el rendimiento | El objetivo es la velocidad, no la claridad; a veces el código queda *menos* legible. |
| Reescribir todo desde cero | No se hace en pasos pequeños y seguros. |

Kent Beck lo resume con la metáfora de los **dos sombreros**: cuando programas, llevas puesto el sombrero de *agregar funcionalidad* o el de *refactorizar*, pero **nunca los dos a la vez**. Puedes cambiar de sombrero muchas veces en una hora, pero siempre debes saber cuál llevas.

---

## 2. ¿Por qué refactorizar?

1. **Mejora el diseño.** Sin mantenimiento, el código se degrada con cada cambio (*entropía del software*). Refactorizar lo devuelve a un buen estado.
2. **Hace el código más fácil de entender.** El código se lee muchas más veces de las que se escribe. Un nombre claro ahorra horas a quien lo lea después, y esa persona puedes ser tú dentro de seis meses.
3. **Ayuda a encontrar errores.** Al reorganizar el código entiendes mejor lo que hace, y los bugs quedan a la vista. En el ejemplo avanzado de este proyecto, la refactorización reveló un bug de stock negativo.
4. **Permite programar más rápido.** Parece contradictorio, pero un diseño limpio permite agregar funcionalidades rápido durante toda la vida del proyecto. Un código desordenado se vuelve cada vez más lento de cambiar.

```
 Esfuerzo por
 funcionalidad
     │                         ╱ sin refactorizar
     │                      ╱
     │                  ╱
     │             ╱
     │        ╱ ─────────────────── con refactorización continua
     │   ╱
     └──────────────────────────────▶ tiempo
```

---

## 3. ¿Cuándo refactorizar y cuándo no?

### Cuándo sí

| Momento | Descripción |
|---|---|
| **Regla de tres** | La primera vez haces algo. La segunda vez lo duplicas con algo de incomodidad. **La tercera vez, refactorizas.** |
| **Refactorización preparatoria** | Antes de agregar una funcionalidad: *"primero hago que el cambio sea fácil, y después hago el cambio fácil"* (Kent Beck). |
| **Refactorización para comprender** | Mientras lees código ajeno: renombras y extraes métodos para que el código cuente lo que entendiste. |
| **Regla del Boy Scout** | *"Deja el campamento más limpio de como lo encontraste."* Haz pequeñas mejoras cada vez que tocas un archivo. |
| **Durante la revisión de código** | La persona que revisa propone mejoras y se aplican en conjunto. |
| **En el ciclo TDD** | Rojo → Verde → **Refactorizar**. |

### Cuándo no

- **No hay pruebas** y no es posible escribirlas primero. Refactorizar a ciegas es arriesgado.
- **El código funciona y nunca se va a modificar.** Si nadie necesita entenderlo, no hay beneficio.
- **Es más barato reescribirlo.** Si el código está tan mal que no funciona, conviene reescribirlo.
- **Hay una fecha de entrega inminente.** Es mejor dejar la deuda registrada y pagarla después (ver [deuda técnica](#10-deuda-técnica)).

---

## 4. La red de seguridad: pruebas automatizadas

> *"Si quieres refactorizar, la condición previa esencial es tener pruebas sólidas."* — Martin Fowler

Refactorizar es cambiar código sin cambiar su comportamiento. **La única forma práctica de saber que el comportamiento no cambió son las pruebas automatizadas.**

### Pruebas de caracterización

Cuando el código heredado (*legacy*) no tiene pruebas, Michael Feathers propone escribir **pruebas de caracterización**: pruebas que registran lo que el código **hace hoy**, aunque no sea lo correcto.

1. Escribir una prueba con un valor esperado cualquiera.
2. Ejecutarla y ver que falla. El mensaje de error muestra el valor real.
3. Poner ese valor real como esperado. Ahora la prueba describe el comportamiento actual.
4. Repetir con otros casos hasta cubrir el código que se va a refactorizar.

En este proyecto, las pruebas de los paquetes `*/sinsol` son pruebas de caracterización. Incluso hay una que **documenta un bug** (`bugStockNegativoConProductoRepetido`): el código tiene ese comportamiento, así que la prueba lo registra.

### El código difícil de probar es una señal

Si una clase es difícil de probar, casi siempre es porque está mal diseñada:

| Dificultad al probar | Problema de diseño |
|---|---|
| Hay que reiniciar variables `static` antes de cada prueba | Estado global mutable |
| Hay que capturar `System.out` para verificar algo | Acoplamiento a un detalle de infraestructura |
| Hay que pasar muchos parámetros que no importan | Lista larga de parámetros |
| No se puede probar una parte sin ejecutar todo | Método con demasiadas responsabilidades |

---

## 5. Code smells (malos olores)

Un ***code smell*** es un síntoma superficial que **suele** indicar un problema más profundo de diseño. No es un error, porque el código funciona, pero sí una señal de que conviene refactorizar. El término viene de Kent Beck y Martin Fowler.

La clasificación más usada agrupa los olores en cinco familias:

### 5.1 Infladores (*Bloaters*)

Código que creció tanto que es difícil de manejar.

| Olor | Síntoma | Refactorización típica |
|---|---|---|
| **Método largo** | Un método de decenas de líneas que hace varias cosas | Extraer método |
| **Clase grande / Clase Dios** | Una clase con demasiados atributos y responsabilidades | Extraer clase |
| **Obsesión por primitivos** | `String` para tipos, `double` para dinero, arreglos para registros | Reemplazar primitivo por objeto, enum o record |
| **Lista larga de parámetros** | Más de 3 o 4 parámetros | Introducir objeto parámetro, Builder |
| **Grupos de datos** | Los mismos datos siempre viajan juntos (`email`, `telefono`) | Extraer clase (`Cliente`) |

### 5.2 Abusadores de la orientación a objetos

Uso incorrecto o incompleto de la orientación a objetos.

| Olor | Síntoma | Refactorización típica |
|---|---|---|
| **Sentencias switch / if-else por tipo** | Cadenas de condiciones que preguntan "¿qué tipo es?" | Reemplazar condicional por polimorfismo |
| **Campo temporal** | Atributos que solo tienen valor en algunos casos | Extraer clase, Null Object |
| **Herencia rechazada** | Una subclase ignora o anula lo que hereda | Reemplazar herencia por delegación |
| **Clases alternativas con interfaces distintas** | Dos clases hacen lo mismo con métodos de nombre diferente | Renombrar, extraer interfaz |

### 5.3 Impedimentos al cambio (*Change Preventers*)

Un cambio pequeño obliga a modificar muchos lugares.

| Olor | Síntoma | Refactorización típica |
|---|---|---|
| **Cambio divergente** | Una clase cambia por muchas razones distintas | Extraer clase (aplicar SRP) |
| **Cirugía de escopeta** | Un cambio obliga a tocar muchas clases | Mover método, mover campo |
| **Jerarquías paralelas** | Crear una subclase obliga a crear otra en otra jerarquía | Mover método, unificar jerarquías |

### 5.4 Prescindibles (*Dispensables*)

Algo que sobra y cuya eliminación deja el código más limpio.

| Olor | Síntoma | Refactorización típica |
|---|---|---|
| **Código duplicado** | El mismo fragmento aparece en varios lugares | Extraer método, subir método a la superclase |
| **Comentarios que explican código confuso** | El comentario compensa un código poco claro | Renombrar, extraer método con nombre expresivo |
| **Código muerto** | Variables, métodos o clases que nadie usa | Eliminar |
| **Generalidad especulativa** | Abstracciones "por si acaso" que nadie usa | Colapsar jerarquía, eliminar parámetro |
| **Clase perezosa** | Una clase que casi no hace nada | Integrar clase |
| **Números mágicos** | `0.19`, `160`, `200000` sin explicación | Reemplazar número mágico por constante |

### 5.5 Acopladores (*Couplers*)

Acoplamiento excesivo entre clases.

| Olor | Síntoma | Refactorización típica |
|---|---|---|
| **Envidia de características** | Un método usa más los datos de otra clase que los propios | Mover método |
| **Intimidad inapropiada** | Una clase accede a los detalles internos de otra | Mover método, encapsular campo |
| **Cadenas de mensajes** | `a.getB().getC().getD().hacer()` | Ocultar delegado (Ley de Demeter) |
| **Intermediario** | Una clase solo reenvía llamadas a otra | Eliminar intermediario |
| **Estado global** | Variables `static` públicas modificables desde cualquier parte | Inyección de dependencias |

---

## 6. Catálogo de técnicas de refactorización

Cada técnica tiene un **nombre**, una **motivación** y una **mecánica** (los pasos seguros para aplicarla). Estas son las más usadas.

### 6.1 Renombrar (variable, método, clase)

El nombre debe revelar la intención.

```java
// Antes
double calc(double[] p, int[] c, String t)

// Después
double calcularTotal()
```

### 6.2 Extraer método

Convertir un fragmento de código en un método cuyo nombre explique **qué** hace.

```java
// Antes
double r = s - d;
r = r + r * 0.19;

// Después
public double calcularIva() {
    return (calcularSubtotal() - calcularDescuento()) * TASA_IVA;
}
```

**Regla práctica:** si sientes la necesidad de escribir un comentario para explicar un bloque de código, extráelo a un método con ese comentario como nombre.

### 6.3 Reemplazar número mágico por constante

```java
// Antes
if (horas > 160) pago += (horas - 160) * 15000 * 0.25;

// Después
private static final int HORAS_ORDINARIAS = 160;
private static final double TARIFA_HORA = 15_000;
private static final double RECARGO_HORA_EXTRA = 0.25;
```

### 6.4 Reemplazar código de tipo por enum o clase

```java
// Antes
if (t.equals("VIP")) d = s * 0.1;

// Después
public enum TipoCliente {
    REGULAR(0.0), FRECUENTE(0.05), VIP(0.10);
    ...
}
```

El compilador ahora impide escribir `"VPI"` por error.

### 6.5 Reemplazar condicional por polimorfismo

Es la refactorización más representativa de la orientación a objetos.

```java
// Antes
if (tipo.equals("PLANTA"))        { ... }
else if (tipo.equals("HORAS"))    { ... }
else if (tipo.equals("VENDEDOR")) { ... }

// Después
public abstract class Empleado {
    public abstract double calcularPago();
}
public class EmpleadoPlanta extends Empleado { ... }
public class EmpleadoPorHoras extends Empleado { ... }
public class Vendedor extends Empleado { ... }
```

### 6.6 Introducir objeto parámetro / Reemplazar arreglos paralelos por objeto

```java
// Antes
procesar(String email, String telefono, String[] codigos, int[] cantidades, ...)

// Después
record Cliente(String nombre, String email, String telefono) { }
record ItemPedido(Producto producto, int cantidad) { }
```

### 6.7 Extraer clase

Cuando una clase tiene más de una responsabilidad, una parte se mueve a una clase nueva. El método de 7 responsabilidades del ejemplo avanzado se repartió entre `Pedido`, `Inventario`, `Notificador`, `Cupon`, `MetodoEnvio` y `MetodoPago`.

### 6.8 Extraer interfaz

Definir un contrato para que el cliente dependa de la abstracción y no de la implementación:

```java
public interface Inventario {
    Producto buscarProducto(String codigo);
    int consultarStock(String codigo);
    void descontar(String codigo, int cantidad);
}
```

### 6.9 Encapsular campo / colección

```java
// Antes: cualquiera podía modificar la lista
public List<Empleado> empleados;

// Después
private final List<Empleado> empleados = new ArrayList<>();
public List<Empleado> getEmpleados() {
    return Collections.unmodifiableList(empleados);
}
```

### 6.10 Mover método

Si un método usa más los datos de otra clase que los propios, se mueve a esa clase (aplica el principio del *Experto en información*). Por ejemplo, `calcularSubtotal()` está en `ItemPedido`, porque es la clase que tiene el precio y la cantidad.

### 6.11 Reemplazar excepción genérica por excepción de dominio

```java
// Antes
throw new IllegalStateException("Stock insuficiente para " + codigo);

// Después
throw new StockInsuficienteException(codigo, solicitado, disponible);
```

---

## 7. Principios de diseño orientado a objetos

Refactorizar es el camino; los principios indican hacia dónde ir.

### 7.1 Los cuatro pilares de la POO

| Pilar | Idea | En el proyecto |
|---|---|---|
| **Encapsulamiento** | Ocultar el estado interno y exponer solo comportamiento | `Factura` controla su lista de `LineaFactura` |
| **Abstracción** | Mostrar lo esencial y ocultar el detalle | Interfaces `Cupon`, `MetodoEnvio`, `Inventario` |
| **Herencia** | Reutilizar y especializar comportamiento | `EmpleadoPlanta extends Empleado` |
| **Polimorfismo** | Un mismo mensaje con distintas respuestas | `empleado.calcularPago()` |

### 7.2 Principios SOLID (Robert C. Martin)

| Principio | Enunciado | Olor que lo viola |
|---|---|---|
| **S** – Responsabilidad Única | Una clase debe tener **una sola razón para cambiar**. | Clase Dios, método largo |
| **O** – Abierto/Cerrado | Abierto a la extensión, **cerrado a la modificación**. | `if/else` por tipo |
| **L** – Sustitución de Liskov | Una subclase debe poder usarse en lugar de su superclase sin romper nada. | Herencia rechazada |
| **I** – Segregación de Interfaces | Ningún cliente debe depender de métodos que no usa. | Interfaces "gordas" |
| **D** – Inversión de Dependencias | Depender de **abstracciones**, no de implementaciones concretas. | `static`, `new` de dependencias dentro de la clase |

### 7.3 Otros principios

- **DRY** (*Don't Repeat Yourself*): cada conocimiento debe tener una única representación en el sistema.
- **KISS** (*Keep It Simple*): la solución más simple que funcione.
- **YAGNI** (*You Aren't Gonna Need It*): no programar lo que todavía no se necesita.
- **Ley de Demeter**: habla solo con tus vecinos inmediatos (evita `a.getB().getC()`).
- **Composición sobre herencia**: preferir *tiene un* a *es un* cuando la herencia no es natural.
- **Alta cohesión y bajo acoplamiento**: cada clase hace una cosa bien y depende poco de las demás.
- **Patrones GRASP**: *Experto en información*, *Creador*, *Controlador*, *Bajo acoplamiento*, *Alta cohesión*.

---

## 8. Patrones de diseño que surgen al refactorizar

Joshua Kerievsky (*Refactoring to Patterns*) muestra que los patrones de diseño no tienen que aplicarse desde el inicio: **se llega a ellos refactorizando** cuando el código lo pide.

| Patrón | Problema que resuelve | Refactorización que lleva a él | En el proyecto |
|---|---|---|---|
| **Strategy** | Una familia de algoritmos intercambiables | Reemplazar lógica condicional por Strategy | `Cupon`, `MetodoEnvio`, `MetodoPago` |
| **Null Object** | Validar `null` en todas partes | Introducir Null Object | `SinCupon` |
| **Factory / Registro** | Crear objetos a partir de un código o texto | Reemplazar constructores por método de fábrica | `CatalogoCupones` |
| **Builder** | Construir objetos con muchos parámetros | Encapsular composición con Builder | `Pedido.Builder` |
| **Observer** | Avisar a varios interesados sin acoplarse a ellos | Reemplazar notificaciones fijas por Observer | `Notificador` |
| **Template Method** | Pasos comunes con variaciones en las subclases | Formar método plantilla | (se puede aplicar a `Empleado`) |

> ⚠️ **Advertencia:** aplicar patrones sin necesidad produce *sobreingeniería* (el olor de *generalidad especulativa*). Un patrón se justifica cuando elimina un problema real.

---

## 9. Proceso paso a paso

### Ciclo básico

```
   ┌─────────────────────────────────────────────┐
   │ 1. ¿Hay pruebas? Si no, escribirlas primero │
   └───────────────────┬─────────────────────────┘
                       ▼
   ┌─────────────────────────────────────────────┐
   │ 2. Ejecutar pruebas → todas en VERDE        │◀──────┐
   └───────────────────┬─────────────────────────┘       │
                       ▼                                 │
   ┌─────────────────────────────────────────────┐       │
   │ 3. Identificar un code smell                │       │
   └───────────────────┬─────────────────────────┘       │
                       ▼                                 │
   ┌─────────────────────────────────────────────┐       │
   │ 4. Aplicar UNA refactorización pequeña      │       │
   └───────────────────┬─────────────────────────┘       │
                       ▼                                 │
   ┌─────────────────────────────────────────────┐       │
   │ 5. Compilar y ejecutar pruebas              │       │
   │    • Verde → commit y volver al paso 3 ─────┼───────┘
   │    • Rojo  → deshacer el último cambio      │
   └─────────────────────────────────────────────┘
```

### Reglas de oro

1. **Pasos pequeños.** Si una prueba falla, el error está en el último cambio, que fue pequeño.
2. **Ejecutar las pruebas después de cada paso.** Hay que tener retroalimentación constante.
3. **Hacer commit con frecuencia.** Así siempre se puede volver a un estado estable.
4. **No mezclar refactorización con nueva funcionalidad** (los dos sombreros).
5. **Si una prueba falla, deshacer en lugar de depurar.** Es más rápido volver atrás y dar un paso más pequeño.

### Refactorización y TDD

En el **Desarrollo Guiado por Pruebas** (TDD), refactorizar es la tercera fase del ciclo:

| Fase | Acción |
|---|---|
| 🔴 **Rojo** | Escribir una prueba que falla |
| 🟢 **Verde** | Escribir el código mínimo para que pase |
| 🔵 **Refactorizar** | Limpiar el código manteniendo las pruebas en verde |

---

## 10. Deuda técnica

Ward Cunningham comparó el código desordenado con una **deuda financiera**: tomar atajos permite entregar más rápido hoy, pero se pagan **intereses** (cada cambio futuro cuesta más) hasta que se salda la deuda refactorizando.

### Cuadrante de la deuda técnica (Martin Fowler)

|  | **Imprudente** | **Prudente** |
|---|---|---|
| **Deliberada** | *"No hay tiempo para diseñar."* | *"Entregamos ahora y pagamos la deuda después."* |
| **Inadvertida** | *"¿Qué es una arquitectura en capas?"* | *"Ahora sabemos cómo debimos haberlo hecho."* |

La refactorización es la forma de **pagar la deuda técnica**. Lo ideal es pagarla de forma continua, con un poco en cada tarea, en lugar de acumularla.

---

## 11. Herramientas

### Refactorizaciones automáticas del IDE

Los IDE aplican las refactorizaciones más comunes de forma segura y actualizan todas las referencias:

| Refactorización | NetBeans | IntelliJ IDEA | VS Code (Java) |
|---|---|---|---|
| Renombrar | Ctrl+R | Shift+F6 | F2 |
| Extraer método | Alt+Shift+M | Ctrl+Alt+M | Ctrl+Shift+R |
| Introducir variable | Alt+Shift+V | Ctrl+Alt+V | Ctrl+Shift+R |
| Introducir constante | Alt+Shift+C | Ctrl+Alt+C | Ctrl+Shift+R |
| Mover clase | Menú *Refactor → Move* | F6 | Arrastrar en el explorador |
| Extraer interfaz | Menú *Refactor → Extract Interface* | *Refactor → Extract Interface* | Acción rápida (Ctrl+.) |

### Análisis estático y métricas

- **SonarQube / SonarLint**: detecta *code smells*, duplicación y complejidad.
- **PMD**, **Checkstyle**, **SpotBugs**: reglas de estilo y errores comunes.
- **JaCoCo**: cobertura de pruebas, para saber si la red de seguridad es suficiente.

### Métricas útiles

| Métrica | Qué mide | Valor deseable |
|---|---|---|
| **Complejidad ciclomática** | Número de caminos independientes en un método | ≤ 10 por método |
| **Líneas por método** | Tamaño del método | Pocas; que quepa en una pantalla |
| **Acoplamiento (CBO)** | Cuántas clases usa una clase | Bajo |
| **Cohesión (LCOM)** | Cuánto se relacionan los métodos con los atributos | Alta cohesión |
| **Cobertura de pruebas** | Porcentaje del código ejecutado por las pruebas | Alta en el código a refactorizar |

---

## 12. Relación con los ejemplos del proyecto

| Ejemplo | Olores principales (`sinsol`) | Técnicas aplicadas (`consol`) | Principios y patrones |
|---|---|---|---|
| **1 · Básico** `basico` | Nombres pobres, números mágicos, método largo, arreglos paralelos, obsesión por primitivos | Renombrar, extraer método, constantes, reemplazar código de tipo por enum, extraer clase (`LineaFactura`) | Encapsulamiento, legibilidad |
| **2 · Intermedio** `intermedio` | `if/else` por tipo, lista larga de parámetros, arreglos paralelos | Reemplazar condicional por polimorfismo, encapsular colección | Herencia, polimorfismo, OCP, LSP |
| **3 · Avanzado** `avanzado` | Método Dios, estado global, `if/else` múltiples, acoplamiento a `System.out`, excepciones genéricas | Extraer clase, extraer interfaz, introducir objeto parámetro, mover método, inyección de dependencias | SOLID completo, Strategy, Null Object, Factory, Builder, Observer |

Para estudiar cada ejemplo se recomienda:

1. Leer la clase de `sinsol` e identificar los olores **antes** de ver la solución.
2. Leer la prueba de caracterización de `sinsol`.
3. Comparar con `consol` y su prueba: los resultados esperados son los mismos.
4. Revisar el diagrama de clases (`diagramas/diagrama-clases.drawio`) para ver la estructura.
5. Resolver los ejercicios propuestos en el [README](README.md).

---

## 13. Bibliografía

- Fowler, M. (2018). *Refactoring: Improving the Design of Existing Code* (2.ª ed.). Addison-Wesley.
- Martin, R. C. (2008). *Clean Code: A Handbook of Agile Software Craftsmanship*. Prentice Hall.
- Feathers, M. (2004). *Working Effectively with Legacy Code*. Prentice Hall.
- Kerievsky, J. (2004). *Refactoring to Patterns*. Addison-Wesley.
- Gamma, E., Helm, R., Johnson, R. y Vlissides, J. (1994). *Design Patterns: Elements of Reusable Object-Oriented Software*. Addison-Wesley.
- Beck, K. (2002). *Test-Driven Development: By Example*. Addison-Wesley.
- Larman, C. (2004). *Applying UML and Patterns* (3.ª ed.). Prentice Hall. (Patrones GRASP)
- Catálogo en línea de refactorizaciones: <https://refactoring.com/catalog/>
- Catálogo de *code smells* y técnicas: <https://refactoring.guru/es/refactoring>
