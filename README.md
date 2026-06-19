# Gestor de música
Un gestor de música hecho por estudiantes de la nacho

## Diagrama UML

```mermaid
classDiagram
    class Gestor_de_musica {
        +main(args: String[]) void
    }

    class Artista {
        -nombre: String
        -genero: String
        +Artista(nombre: String, genero: String)
        +getNombre() String
        +getGenero() String
        +setNombre(nombre: String) void
        +setGenero(genero: String) void
    }

    class Cancion {
        -titulo: String
        -artista: Artista
        +Cancion(titulo: String, artista: Artista)
        +getTitulo() String
        +getArtista() Artista
        +setTitulo(titulo: String) void
        +setArtista(artista: Artista) void
    }

    class CatalogoMusical {
        +CatalogoMusical()
        +agregarCancion(cancion: Cancion) void
        +mostrarCatalogo() void
    }

    class ReproductorLogico {
        -cabeza: NodoDoble
        -cola: NodoDoble
        -actual: NodoDoble
        -frente: NodoSimple
        -fin: NodoSimple
        +ReproductorLogico()
        +agregarAPlaylist(cancion: Cancion) void
        +agregarAColaEspera(cancion: Cancion) void
        +reproducirSiguiente() Cancion
        +reproducirAnterior() Cancion
        +getCancionActual() Cancion
    }

    class HistorialMusica {
        -top: NodoPila
        +HistorialMusica()
        +registrarReproduccion(cancion: Cancion) void
        +mostrarHistorial() void
    }

    class RankingServicio {
        +RankingServicio()
        +registrarReproduccion(cancion: Cancion) void
        +obtenerTopCanciones() void
    }

    class RecomendadorConexiones {
        +RecomendadorConexiones()
        +obtenerRecomendaciones(cancion: Cancion) void
    }

    class NodoSimple {
        +cancion: Cancion
        +siguiente: NodoSimple
        +NodoSimple(cancion: Cancion)
    }

    class NodoDoble {
        +cancion: Cancion
        +siguiente: NodoDoble
        +anterior: NodoDoble
        +NodoDoble(cancion: Cancion)
    }

    class NodoPila {
        +cancion: Cancion
        +siguiente: NodoPila
        +NodoPila(cancion: Cancion)
    }

    %% --- RELACIONES DEL SISTEMA ---
    Gestor_de_musica ..> CatalogoMusical : instanciar / usa
    Gestor_de_musica ..> ReproductorLogico : instanciar / usa
    Gestor_de_musica ..> RankingServicio : instanciar / usa
    Gestor_de_musica ..> HistorialMusica : instanciar / usa
    Gestor_de_musica ..> RecomendadorConexiones : instanciar / usa
    
    Cancion o--> Artista : agregación (tiene un)
    CatalogoMusical "1" *--> "*" Cancion : composición (almacena)
    
    ReproductorLogico "1" *--> "*" NodoDoble : composición (Estructura Playlist)
    ReproductorLogico "1" *--> "*" NodoSimple : composición (Estructura ColaEspera)
    HistorialMusica "1" *--> "*" NodoPila : composición (Estructura Stack/Pila)
    
    NodoSimple --> Cancion : referencia / contiene
    NodoDoble --> Cancion : referencia / contiene
    NodoPila --> Cancion : referencia / contiene
```

---

## Desglose Técnico de las Estructuras de Datos

Este proyecto emplea un diseño modular donde cada estructura de datos fue seleccionada para resolver un problema algorítmico específico del dominio musical. A continuación, se explican las estructuras identificadas en el código:

### 1. Tabla Hash (CatalogoMusical)
* **Clase Asociada:** `CatalogoMusical`
* **Componente de Java:** `java.util.Map<String, Cancion>` instanciado a través de un `HashMap`.
* **Justificación de Elección:** Cuando un usuario introduce el nombre de una canción para añadirla a la fila de espera o buscar sus detalles, realizar una búsqueda lineal en una lista desorganizada degradaría el rendimiento. La Tabla Hash calcula el código hash del título de la canción, permitiendo indexar y recuperar el objeto en un **tiempo constante promedio de O(1)**.
* **Estrategia:** Las claves del mapa se almacenan siempre aplicando `.toLowerCase()`. Esto elimina la sensibilidad a las mayúsculas y minúsculas, garantizando que variaciones en la escritura apunten al mismo registro.

```mermaid
graph LR
    subgraph Tabla Hash - CatalogoMusical
        K1["Clave: 'cliffs of dover'"] --> |Función Hash| H1[Índice de Memoria] --> V1["Objeto: Cancion (ID: 01, Título: Cliffs of Dover)"]
        K2["Clave: 'comfortably numb'"] --> |Función Hash| H2[Índice de Memoria] --> V2["Objeto: Cancion (ID: 02, Título: Comfortably Numb)"]
    end
```

### 2. Estructuras Lineales Dinámicas (ReproductorLogico e HistorialMusica)
El sistema implementa tres tipos de estructuras lineales clásicas adaptadas a punteros explícitos mediante nodos internos:

#### A. Lista Doblemente Enlazada (Playlist Principal)
* **Ubicación:** `ReproductorLogico` (punteros `cabeza`, `cola`, `actual`) utilizando nodos `NodoDoble`.
* **Justificación:** Representa el orden secuencial por defecto de la biblioteca. Al contener enlaces hacia adelante (`siguiente`) y hacia atrás (`anterior`), faculta al reproductor a navegar de forma bidireccional sin tener que recalcular las posiciones desde la raíz de la lista. Costo algorítmico de **O(1)** en la transición de pistas.

```mermaid
graph LR
    subgraph Lista Doblemente Enlazada - Playlist
        Nodo1["[Anterior: null] <-- NodoDoble (Canción A) --> [Siguiente]"]
        Nodo2["[Anterior] <-- NodoDoble (Canción B) --> [Siguiente]"]
        Nodo3["[Anterior] <-- NodoDoble (Canción C) --> [Siguiente: null]"]
        Nodo1 <==> Nodo2
        Nodo2 <==> Nodo3
        
        style Nodo2 fill:#2a4d69,stroke:#fff,stroke-width:2px,color:#fff
    end
    Anclaje[Puntero Actual] -.-> Nodo2
```

#### B. Cola FIFO - First In, First Out (Fila de Espera)
* **Ubicación:** `ReproductorLogico` (punteros `frente`, `fin`) utilizando nodos `NodoSimple`.
* **Justificación:** Modela la opción de encolar canciones. Sigue el principio estricto de que la primera canción seleccionada de forma manual debe ser la primera en sonar, anteponiéndose al flujo de la playlist general. La inserción al final y extracción por el frente se procesan en **O(1)**.

```mermaid
graph LR
    subgraph Cola FIFO - Fila de Espera
        Frente["frente"] --> N1["NodoSimple (Canción X)"]
        N1 --> N2["NodoSimple (Canción Y)"]
        N2 --> Fin["fin"] --> N3["NodoSimple (Canción Z) --> null"]
    end
    
    Direccion[-- Flujo de Salida para Reproducción --] --> Frente
```

#### C. Pila Dinámica LIFO - Last In, First Out (Historial)
* **Ubicación:** `HistorialMusica` (puntero `tope`) utilizando nodos `NodoPila`.
* **Justificación:** Almacena el rastro cronológico de las canciones reproducidas. Se realiza una operación **Push** en el `tope` de la pila en **O(1)**. Para mostrar el historial sin destruir la estructura original, se utiliza un puntero auxiliar que desciende nodo por nodo a través de las referencias `.abajo`.

```mermaid
graph TD
    subgraph Pila LIFO - Historial
        Tope["tope"] --> T["NodoPila (Última Canción Escuchada)"]
        T --> N_Abajo1["NodoPila (Penúltima Canción Escuchada)"]
        N_Abajo1 --> N_Abajo2["NodoPila (Canción Antigua)"]
        N_Abajo2 --> N_Null["null"]
    end
```

### 3. Estructura No Lineal: Montículo de Máximos / Max-Heap (RankingServicio)
* **Clase Asociada:** `RankingServicio`
* **Componente de Java:** `java.util.PriorityQueue<Cancion>` configurado con comparador inverso por reproducciones.
* **Justificación de Elección:** El sistema requiere extraer ágilmente las 5 canciones con mayor popularidad. Un **Max-Heap** garantiza que el elemento raíz del árbol binario semiordenado contenga siempre el valor máximo absoluto.
* **Manejo de Mutaciones:** Cuando una canción es reproducida, su contador aumenta. Para no corromper el árbol, el sistema remueve la canción, incrementa el contador y la reinserta, desencadenando la flotación lógica de la estructura de forma segura en **O(log n)**.

```mermaid
graph TD
    subgraph Estructura del Max-Heap - Jerarquía de Popularidad
        Raiz["Raíz: Canción Pop (15 reproducciones)"] --> HijoI1["Hijo Izq: Canción Rock (12 repros)"]
        Raiz --> HijoD1["Hijo Der: Canción Jazz (8 repros)"]
        HijoI1 --> SubHijo1["Canción Indie (5 repros)"]
        HijoI1 --> SubHijo2["Canción Metal (3 repros)"]
    end
```

### 4. Grafo No Dirigido por Lista de Adyacencia (RecomendadorConexiones)
* **Clase Asociada:** `RecomendadorConexiones`
* **Componente de Java:** `Map<Artista, List<Artista>> grafoArtistas` implementado con un `HashMap`.
* **Justificación de Elección:** Modela redes relacionales de afinidad artística. Al no existir una jerarquía lineal aplicable a las similitudes musicales, un Grafo es idóneo.
* **Construcción:** Se crea una arista bidireccional entre nodos (Artistas) si comparten el mismo género musical. Esto permite simular redes de recomendación con búsquedas de vecinos directos en relación con las conexiones del nodo visitado.

```mermaid
graph LR
    subgraph Grafo No Dirigido - Red Relacional de Artistas
        A1((Queen)) --- A2((Led Zeppelin))
        A1 --- A3((AC/DC))
        A4((Miles Davis)) --- A5((John Coltrane))
        
        note1[Aristas por Género: 'Rock'] -.-> A1
        note2[Aristas por Género: 'Jazz'] -.-> A4
    end
    style A1 fill:#c94c4c,stroke:#333,stroke-width:1px,color:#fff
    style A2 fill:#c94c4c,stroke:#333,stroke-width:1px,color:#fff
    style A3 fill:#c94c4c,stroke:#333,stroke-width:1px,color:#fff
    style A4 fill:#37718e,stroke:#333,stroke-width:1px,color:#fff
    style A5 fill:#37718e,stroke:#333,stroke-width:1px,color:#fff
```

---

## ¿Qué ocurre bajo el capó en el Menú Principal?

Esta sección describe detalladamente el comportamiento algorítmico interno del sistema ante la interacción del usuario en la consola. Sirve como bitácora técnica para justificar las operaciones en memoria.

### Opción 1: Ver Catálogo Completo (Tabla Hash)
* **Operación Interna:** El programa accede a la instancia única de `CatalogoMusical`. Internamente invoca un iterador sobre el `entrySet()` o el `values()` del `HashMap`.
* **Mecánica Algorítmica:** Aunque la búsqueda de un elemento individual toma un tiempo constante O(1), listar el catálogo entero requiere recorrer todos los baldes ocupados de la tabla hash, lo cual se ejecuta en un tiempo lineal de **O(N)**, donde N es la cantidad total de canciones registradas.

### Opción 2: Subir/Registrar nueva canción manualmente
* **Operación Interna:** Solicita por consola el título de la canción, el nombre del artista y su género. 
* **Mecánica Algorítmica:** Instancia un nuevo objeto `Artista` y un objeto `Cancion`. Posteriormente ejecuta la operación `mapa.put(titulo.toLowerCase(), nuevaCancion)`. La Tabla Hash calcula el valor hash del string de entrada y ubica el nodo en la dirección de memoria correspondiente en un tiempo de **O(1)**. Adicionalmente, el objeto `Artista` se evalúa en el módulo del Grafo para verificar si debe conectarse con nodos preexistentes del mismo género.

### Opción 3: Agregar canción a la Fila de Espera (Cola FIFO)
* **Operación Interna:** El usuario digita el título de la canción que desea encolar.
* **Mecánica Algorítmica:** Primero se realiza una comprobación de existencia mediante `mapa.containsKey(titulo)`. Si la canción existe (acceso O(1)), se extrae la referencia del objeto y se invoca el método del reproductor para añadir a la cola. El programa crea un `NodoSimple` y modifica los punteros de la estructura FIFO: `fin.siguiente = nuevoNodo; fin = nuevoNodo;`. No se duplica la información de la canción en memoria, solo se enlaza su referencia lógica.

### Opción 4: Reproducir Siguiente Canción (Avanzar / Coherencia de módulos)
* **Operación Interna:** Esta es la función central de control y el punto más crítico en la integración del sistema. Activa cambios concurrentes en múltiples estructuras:
    1. **Estructura Fila de Espera (Cola FIFO):** El reproductor evalúa si el puntero `frente` es diferente de nulo. Si contiene nodos, extrae la canción aplicando un desencolado clásico (`frente = frente.siguiente`) y la reproduce.
    2. **Estructura Playlist (Lista Doble):** Si la cola de espera se encuentra vacía, el reproductor consulta el puntero `actual` de la lista doble y avanza a la siguiente posición mediante `actual = actual.siguiente`.
    3. **Estructura Historial (Pila LIFO):** Una vez seleccionada la canción por cualquiera de los dos mecanismos anteriores, se realiza una operación de inserción (Push) en el tope de la pila del historial, actualizando la referencia `top`.
    4. **Estructura Ranking (Max-Heap):** Se incrementa en 1 el contador interno de reproducciones del objeto `Cancion`. Debido a que las colas de prioridad nativas de Java no reordenan dinámicamente un elemento si cambian sus propiedades internas de ordenamiento, el módulo de ranking realiza un procedimiento seguro de remoción y reinserción: `priorityQueue.remove(cancion); priorityQueue.add(cancion);`, forzando el reacomodo del árbol binario en un tiempo de **O(log n)**.

### Opción 5: Ver Historial de reproducción (Pila LIFO)
* **Operación Interna:** Muestra las pistas que han sonado en orden cronológico inverso (desde la más reciente hasta la más antigua).
* **Mecánica Algorítmica:** Para evitar vaciar la pila mediante operaciones consecutivas de Pop (lo que destruiría los datos), el sistema implementa un recorrido de consulta pura. Se inicializa un puntero temporal `auxiliar = top` y se ejecuta un ciclo estructurado que imprime el contenido del nodo y se desplaza mediante `auxiliar = auxiliar.siguiente` hasta encontrar un valor nulo. El costo del recorrido completo es lineal **O(K)**, donde K es el número de canciones escuchadas.

### Opción 6: Ver Top 5 canciones más escuchadas (Heap/Árbol)
* **Operación Interna:** Despliega en pantalla los 5 registros con mayor índice de uso.
* **Mecánica Algorítmica:** El Max-Heap garantiza que la raíz siempre es el elemento con más reproducciones. Para obtener el top 5 sin alterar de manera permanente la cola de prioridad original, el sistema clona de manera superficial la estructura o extrae los 5 valores máximos consecutivamente guardándolos en un contenedor temporal, para posteriormente reinsertarlos. Cada extracción del valor máximo cuesta **O(log n)** debido al proceso de reorganización (down-heapify) que realiza el árbol.

### Opción 7: Ver recomendaciones de artistas similares (Grafo)
* **Operación Interna:** Utiliza la información del artista de la canción en reproducción o solicita el nombre de un artista por consola.
* **Mecánica Algorítmica:** El sistema busca el objeto `Artista` como clave dentro del `HashMap` que representa la lista de adyacencia del grafo. Al recuperarlo en **O(1)**, se obtiene acceso directo a la lista enlazada de artistas vecinos (aquellos que comparten su mismo género). El algoritmo itera sobre este conjunto acotado de nodos adyacentes para imprimirlos en consola, simulando un recorrido de Anchura (BFS) limitado exclusivamente a un nivel de profundidad.

### Opción 8: Detener Música (Apagar el reproductor)
* **Operación Interna:** Suspende los flujos automáticos de reproducción en consola.
* **Mecánica Algorítmica:** Cambia una bandera booleana de estado en la aplicación principal (ej. `estaReproduciendo = false`). Desde el punto de vista de las estructuras de datos, detiene los avances automáticos del puntero `actual` en la lista doblemente enlazada y congela temporalmente las mutaciones sobre la pila del historial y el heap de estadísticas hasta que se reanude la actividad.

---

## Dinámica de Coherencia de Módulos

La principal virtud del proyecto es la interconexión sistémica. Cuando el usuario decide reproducir una canción, se desata una reacción en cadena que demuestra el uso simultáneo de las estructuras:

```mermaid
sequenceDiagram
    autonumber
    Actor Usuario
    participant Main as Gestor_de_musica (Main)
    participant Rep as ReproductorLogico (Lineales)
    participant Rank as RankingServicio (Max-Heap)
    participant Hist as HistorialMusica (Pila LIFO)
    
    Usuario->>Main: Selecciona Opción 4 (Reproducir Siguiente)
    Main->>Rep: Invocación de reproducirSiguiente()
    Note over Rep: Revisa Cola FIFO primero.<br>Si está vacía, avanza en la Lista Doble.
    Rep->>Main: Retorna Objeto Cancion seleccionado
    Main->>Rank: Invocación de registrarReproduccion(cancion)
    Note over Rank: Actualiza contador y reordena el Max-Heap (O(log n))
    Main->>Hist: Invocación de registrarReproduccion(cancion)
    Note over Hist: Push en el tope de la Pila LIFO (O(1))
```
