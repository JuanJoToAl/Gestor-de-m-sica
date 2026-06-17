# Gestor de música
Un gestor de música hecho por estudiantes de la nacho

# Diagrama UML
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

# 🎵 SoundWave - Distribución de Roles y Tareas del Proyecto

## 📋 Tareas Iniciales (Todo el Equipo)
- [ ] Crear el repositorio en GitHub y definir la estructura de carpetas.
- [ ] Implementar la entidad básica `Cancion.java`.
- [ ] Implementar la entidad básica `Artista.java`.
- [ ] Diseñar el esqueleto de la clase controladora central `SoundWaveApp.java` (Main).

---

## 👤 Integrante 1: Módulo de Búsqueda e Inventario (`CatalogoMusical`)
**Responsabilidad:** Implementar el almacenamiento masivo del catálogo mediante una **Tabla Hash** para lograr búsquedas instantáneas $O(1)$.

- [ ] Crear el archivo `CatalogoMusical.java`.
- [ ] Definir e inicializar la estructura hash interna (ej. `Map<String, Cancion>`).
- [ ] Desarrollar el método `insertarCancion(Cancion c)`.
- [ ] Desarrollar el método `buscarCancion(String titulo)`.
- [ ] Poblar el catálogo con un set de datos inicial (mínimo 20 canciones de prueba).
- [ ] Realizar pruebas unitarias aisladas del módulo.

---

## 👤 Integrante 2: Módulo del Reproductor (`ReproductorLogico`)
**Responsabilidad:** Desarrollar el flujo secuencial y la fila de espera combinando una **Lista Doble Enlazada** (recorrido) y una **Cola** (fila de reproducción).

- [ ] Crear el archivo `ReproductorLogico.java`.
- [ ] Definir la estructura de lista (playlist base) y la estructura FIFO (`Queue`) para la fila de espera.
- [ ] Implementar la variable `indiceActual` para controlar el estado de reproducción.
- [ ] Desarrollar el método `agregarACola(Cancion c)`.
- [ ] Desarrollar el método `reproducirSiguiente()` (debe validar primero si hay elementos en la cola).
- [ ] Desarrollar el método `reproducirAnterior()`.
- [ ] Realizar pruebas unitarias simulando adelantar y retroceder pistas.

---

## 👤 Integrante 3: Módulo de Estadísticas (`RankingServicio`)
**Responsabilidad:** Gestionar el ordenamiento dinámico de popularidad en tiempo real utilizando un **Heap / Montículo** de máximos.

- [ ] Crear el archivo `RankingServicio.java`.
- [ ] Definir la estructura del montículo (`PriorityQueue` configurada con un comparador inverso basado en reproducciones).
- [ ] Desarrollar el método `actualizarReproduccion(Cancion c)` (incrementa contador, remueve versión previa del heap y reinserta).
- [ ] Desarrollar el método `obtenerTop5()` para extraer la cima de manera ordenada sin romper el Heap original.
- [ ] Realizar pruebas unitarias simulando la reproducción consecutiva de una misma canción para validar que suba en el top.

---

## 👤 Integrante 4: Módulo de Relaciones (`RecomendadorConexiones`)
**Responsabilidad:** Modelar la red de recomendaciones usando un **Grafo No Dirigido** basado en listas de adyacencia simples.

- [ ] Crear el archivo `RecomendadorConexiones.java`.
- [ ] Definir la estructura del grafo utilizando un mapa de adyacencia (ej. `Map<String, List<String>>`).
- [ ] Desarrollar el método `conectarArtistas(String a1, String a2)` para crear relaciones bidireccionales de similitud.
- [ ] Desarrollar el método `recomendarSimilares(String artista)` (recorrido BFS limitado a 1 nivel de vecindad).
- [ ] Poblar el grafo inicial conectando a los artistas de las canciones de prueba.
- [ ] Realizar pruebas unitarias ingresando un artista y verificando que liste correctamente a sus similares.

---

## 🚀 Integración y Entrega Final (Todo el Equipo)
- [ ] Acoplar los 4 módulos de servicio dentro de la clase principal `SoundWaveApp.java`.
- [ ] Vincular el menú de la consola para que invoque los métodos de cada integrante.
- [ ] Validar que la reproducción de una canción (Módulo 2) dispare la actualización en el ranking (Módulo 3).
- [ ] Probar el sistema completo ante entradas inválidas o nulas.
- [ ] Redactar el reporte final (incluyendo la declaración de uso de IA según el reglamento del curso).
