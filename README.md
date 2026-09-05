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


### Necesidad 4 — Reglas de transición según el estado


### Reflexión — otros tres patrones (opcional)
[Una o dos frases por cada uno de los tres patrones planteados en la
Parte 2 sobre dónde encajarían en ComprasUDES.]



## Herramientas utilizadas
- Java 17, Spring Boot 3.2, Apache Maven, JUnit 5
- VS Code o IntelliJ IDEA, Git, GitHub

## Conclusiones
[Párrafo de 3-5 oraciones con los aprendizajes más relevantes de
ambas partes, incluyendo qué hizo difícil o fácil decidir entre
patrones cercanos.]

