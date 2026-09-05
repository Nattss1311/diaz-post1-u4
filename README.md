# Post-contenido — Unidad 4: Patrones de Comportamiento en ComprasUDES

## Descripción
Repositorio del post-contenido de la Unidad 4 de Patrones de Diseño
de Software. Un único proyecto Spring Boot (compras-comportamiento)
que resuelve cuatro necesidades reales del backend de ComprasUDES,
el sistema interno de solicitudes de compra corporativas: aprobación
por niveles jerárquicos, ejecución reversible de solicitudes
aprobadas, notificaciones ante cambios de estado y reglas de
transición según el estado actual de la solicitud.


## Cómo ejecutar
```
$ mvn clean package
$ mvn spring-boot:run
$ mvn test
```

## Decisiones de diseño

### Necesidad 1 — Aprobación por niveles jerárquicos
### **1. ¿Cuál es el síntoma de diseño exacto que describe el enunciado?**
El problema describe una petición (`Solicitud`) que debe ser evaluada de forma **secuencial** por múltiples manejadores/niveles independientes (*Supervisor*, *Gerente de Área*, *Director Financiero* y condicionalmente *Revisor de Cumplimiento Normativo*). Cada nivel decide con sus propios criterios si procesa (aprueba/rechaza) la solicitud o si la pasa al siguiente nivel en la secuencia. Además, se requiere que la estructura de la secuencia sea **reconfigurable** (agregar, quitar o reordenar niveles como el Revisor de Cumplimiento) sin modificar el cliente/controlador que dispara la solicitud ni los otros niveles existentes.

---

### **2. ¿Qué patrón(es) de los vistos en la guía de la unidad podrían encajar y por qué?**
* <ins>**Chain of Responsibility (Cadena de Responsabilidad):**</ins> Encaja perfectamente porque **desacopla al emisor de la petición de sus receptores** al encadenar los objetos receptores. Cada nivel de aprobación en la cadena decide si resuelve la solicitud o si la pasa al siguiente manejador de la cadena.
* <ins>**Command (Comando):**</ins> Podría parecer una opción lejana si se piensa en encapsular la acción de "aprobar" como un objeto independiente para ser ejecutado.

---

### **3. ¿Cuál de ellos se descarta y con qué argumento técnico, no solo intuitivo?**
Se descarta **Command**.

* **Argumento técnico:** **Command** encierra una operación discreta como un objeto con un receptor explícito para poder ejecutarla, encolarla o deshacerla posteriormente (modela una *acción reversible o ejecutable*). No resuelve de forma natural el encaminamiento dinámico de una petición a través de múltiples verificadores en orden secuencial donde cualquiera puede interrumpir o delegar el flujo.
* En cambio, **Chain of Responsibility** modela explícitamente la **propagación secuencial** de una petición a través de una cadena de manejadores hasta que uno de ellos la maneja o la cadena finaliza.

### Necesidad 2 — Ejecución reversible de solicitudes
### **1. ¿Cuál es el síntoma de diseño exacto que describe el enunciado?**
El problema describe la necesidad de ejecutar operaciones discretas (*Reservar Presupuesto*, *Generar Orden de Compra*) sobre una solicitud aprobada, donde cada operación debe ser **ejecutable y reversible** de forma independiente, manteniendo un **historial ordenado y consultable** de las operaciones realizadas para auditoría posterior.

---

### **2. ¿Qué patrón(es) de los vistos en la guía de la unidad podrían encajar y por qué?**
* <ins>**Command (Comando):**</ins> Encaja de forma natural porque **encapsula cada petición u operación dentro de un objeto independiente**. Este objeto almacena los parámetros y el estado necesarios para ejecutar la acción (`ejecutar()`) y revertirla (`deshacer()`), permitiendo además encolar o almacenar el historial de comandos ejecutados.
* <ins>**Chain of Responsibility (Cadena de Responsabilidad):**</ins> Podría evaluarse si se piensa erróneamente en ejecutar los pasos de forma secuencial.

---

### **3. ¿Cuál de ellos se descarta y con qué argumento técnico, no solo intuitivo?**
Se descarta **Chain of Responsibility**.

* **Argumento técnico:** **Chain of Responsibility** está diseñado para encaminar y evaluar una petición a través de múltiples receptores hasta que uno decida manejarla o delegarla. **No ofrece un mecanismo nativo para revertir acciones individuales** (no posee estado de deshacer) ni mantiene un historial de comandos auditables ejecutados con anterioridad.
* En cambio, **Command** modela operaciones como objetos autónomos que conocen exactamente cómo revertir su efecto (`deshacer()`) y permite mantener una pila o lista cronológica del historial.

---

### **Punto de Comparación: Necesidad 1 vs. Necesidad 2**
* **Por qué la solución de la Necesidad 1 (Chain) no resuelve la Necesidad 2:** En la Necesidad 2 no hay evaluadores juzgando condiciones para decidir si delegan o resuelven una petición; hay acciones concretas que deben ejecutarse y revertirse de forma independiente conservando su estado en un historial.
* **Por qué la solución de la Necesidad 2 (Command) no resuelve la Necesidad 1:** En la Necesidad 1 no se busca ejecutar/deshacer operaciones ni guardar un historial de acciones, sino propagar dinámicamente una solicitud por una cadena jerárquica hasta encontrar el nivel con la autoridad suficiente para resolverla.

### Necesidad 3 — Notificaciones ante cambio de estado


### **1. ¿Cuál es el síntoma de diseño exacto que describe el enunciado?**
El problema describe la necesidad de disparar múltiples reacciones automáticas e independientes (*Enviar correo al solicitante*, *Actualizar tablero de contabilidad*, *Registrar en log de auditoría*) inmediatamente después de que ocurre el evento de cambio de estado de una `Solicitud`. El código emisor que modifica el estado no debe conocer estas reacciones directamente, y el sistema debe permitir agregar o quitar nuevas reacciones en el futuro sin alterar la lógica central.

---

### **2. ¿Qué patrón(es) de los vistos en la guía de la unidad podrían encajar y por qué?**
* <ins>**Observer (Observador):**</ins> Encaja de forma natural porque **define una dependencia de uno a muchos entre objetos**. Cuando el sujeto (`Solicitud` / Gestor de eventos) cambia de estado, notifica automáticamente a todos sus suscriptores registrados para que ejecuten sus acciones de forma totalmente desacoplada.
* <ins>**State (Estado):**</ins> Podría evaluarse erróneamente por la presencia del concepto "cambio de estado" en el enunciado.

---

### **3. ¿Cuál de ellos se descarta y con qué argumento técnico, no solo intuitivo?**
Se descarta **State**.

* **Argumento técnico:** **State** está diseñado para alterar el *comportamiento interno* de un objeto cuando su estado cambia (encapsulando qué operaciones están permitidas o cómo se comporta el propio objeto en cada fase). En esta necesidad, el comportamiento propio de la `Solicitud` no se modifica; simplemente ocurre un evento y **módulos externos totalmente ajenos** (correo, contabilidad, auditoría) deben enterarse y reaccionar pasivamente. Por ello, **Observer** es el patrón adecuado al desacoplar al emisor de los receptores del evento.

---

### **Punto de Comparación: Necesidad 3 vs. Necesidad 4**
* **Por qué la solución de la Necesidad 3 (Observer) no resuelve la Necesidad 4:** En la Necesidad 3 los módulos son receptores externos que reaccionan de manera pasiva ante la notificación de un evento que ya sucedió. No controlan las reglas de negocio internas ni restringen las operaciones válidas de la solicitud.
* **Por qué la solución de la Necesidad 4 (State) no resuelve la Necesidad 3:** En la Necesidad 4 se busca modificar la conducta del objeto `Solicitud` según su fase actual (por ejemplo, permitir o bloquear cancelaciones y ediciones). El patrón State encapsula la lógica interna de transición y comportamiento del objeto, pero no provee un mecanismo desacoplado para notificar a observadores o subsistemas externos.

### Necesidad 4 — Reglas de transición según el estado

### **1. ¿Cuál es el síntoma de diseño exacto que describe el enunciado?**
El problema describe reglas de negocio y transiciones válidas para cada operación (*aprobar*, *rechazar*, *ejecutar*, *cancelar*) dispersas en múltiples estructuras condicionales (`if/else` o `switch`), evaluando constantemente `getEstado()`. Esta estructura viola el principio de Abierto/Cerrado, ya que agregar un nuevo estado (como `EN_ESPERA_PROVEEDOR`) obliga a modificar varios métodos y clases existentes para adaptar la lógica condicional.

---

### **2. ¿Qué patrón(es) de los vistos en la guía de la unidad podrían encajar y por qué?**
* <ins>**State (Estado):**</ins> Encaja de forma natural porque **permite a un objeto alterar su comportamiento cuando su estado interno cambia**. Cada estado se convierte en una clase independiente que encapsula las reglas y transiciones válidas para cada operación.
* <ins>**Strategy (Estrategia):**</ins> Podría evaluarse porque también intercambia algoritmos o comportamientos a través de una interfaz común.

---

### **3. ¿Cuál de ellos se descarta y con qué argumento técnico, no solo intuitivo?**
Se descarta **Strategy**.

* **Argumento técnico:** En **Strategy**, es el cliente externo quien elige, configura e inyecta explícitamente el algoritmo deseado para resolver un problema particular en un momento dado. Las estrategias son comportamientos independientes entre sí y no tienen noción de ciclo de vida ni de transiciones automáticas. En cambio, en la **Necesidad 4**, el cliente no inyecta comportamientos: es el propio objeto `Solicitud` el que cambia de comportamiento según su fase actual de historia, y las clases de estado conocen y provocan la transición hacia el siguiente estado como resultado de ejecutar una operación válida.

---

### **Punto de Comparación: Strategy vs. State (Decisión 4)**
* **Por qué Strategy no resuelve esta necesidad:** Strategy modela comportamientos intercambiables seleccionados externamente por el cliente. No gestiona transiciones entre estados ni conserva la historia del ciclo de vida del objeto context.
* **Por qué State es el patrón adecuado:** State encapsula el comportamiento variable según el estado interno del objeto. Las transiciones de un estado a otro ocurren de manera encapsulada y natural dentro de las propias clases de estado.

---

## Reflexión — Otros tres patrones de comportamiento

1. **Recorrido secuencial de solicitudes (Iterator):**
   * **Patrón:** <ins>Iterator (Iterador)</ins>.
   * **Justificación:** Permite recorrer secuencialmente una colección de solicitudes de un centro de costo sin exponer su estructura interna o representación subyacente (lista, mapa, etc.).

2. **Esqueleto de impresión de comprobantes (Template Method):**
   * **Patrón:** <ins>Template Method (Método Plantilla)</ins>.
   * **Justificación:** Define el esqueleto del algoritmo de impresión en una clase base (encabezado, cuerpo, pie), dejando que las subclases redefinan la forma de llenar el cuerpo sin alterar la estructura general.

3. **Guardar y restaurar instantáneas de una solicitud (Memento):**
   * **Patrón:** <ins>Memento (Recuerdo)</ins>.
   * **Justificación:** Permite capturar y externalizar el estado interno de una `Solicitud` en un momento dado para poder restaurarlo posteriormente sin violar su encapsulamiento. A diferencia del patrón **Command** (usado en la Necesidad 2, que guarda las operaciones inversas ejecutable/deshacer), Memento guarda la "foto" estática e íntegra del estado del objeto.


## Herramientas utilizadas
- Java 17, Spring Boot 3.2, Apache Maven, JUnit 5
- VS Code o IntelliJ IDEA, Git, GitHub

## Conclusiones

El desarrollo de esta actividad permitió evidenciar cómo los patrones de diseño del GoF facilitan la creación de sistemas extensibles y desacoplados ante requerimientos cambiantes en la gestión de compras. La implementación de Chain of Responsibility y Command en la primera parte simplificó el manejo de flujos de aprobación y acciones reversibles, mientras que State y Observer en la segunda parte garantizaron la correcta encapsulación de reglas de negocio y la notificación reactiva a múltiples clientes. La principal dificultad radicó en distinguir las fronteras entre patrones cercanos, como diferenciar entre un estado interno que cambia el comportamiento del objeto (State) frente a una estrategia intercambiable por el cliente (Strategy), o cuándo usar una cadena jerárquica frente a un patrón de comandos encapsulados. En conclusión, la aplicación combinada de estos patrones de comportamiento mejora la mantenibilidad del software al evitar condicionales anidados y reducir acoplamiento directo entre módulos.

