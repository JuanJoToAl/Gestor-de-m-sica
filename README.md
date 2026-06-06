# Gestor de música
Un gestor de música hecho por estudiantes de la nacho

# Diagrama UML
```mermaid
classDiagram
    class SoundWaveApp {
        - CatalogoMusical catalogo
        - ReproductorLogico reproductor
        - RankingServicio ranking
        - RecomendadorConexiones recomendador
        + main(String[] args)
        + mostrarMenu()
    }

    class Cancion {
        - String id
        - String titulo
        - String artista
        - String genero
        - int reproducciones
        + Cancion(String id, String titulo, String artista, String genero)
        + incrementarReproducciones()
        + getId() String
        + getTitulo() String
        + getArtista() String
        + getReproducciones() int
    }

    class Artista {
        - String nombre
        - String genero
        + Artista(String nombre, String genero)
        + getNombre() String
        + getGenero() String
    }

    class CatalogoMusical {
        - Map~String, Cancion~ tablaHash
        + insertarCancion(Cancion c)
        + buscarCancion(String titulo) Cancion
    }

    class ReproductorLogico {
        - List~Cancion~ playlistDoble
        - Queue~Cancion~ filaEspera
        - int indiceActual
        + agregarACola(Cancion c)
        + reproducirSiguiente() Cancion
        + reproducirAnterior() Cancion
    }

    class RankingServicio {
        - PriorityQueue~Cancion~ maxHeap
        + actualizarReproduccion(Cancion c)
        + obtenerTop5() List~Cancion~
    }

    class RecomendadorConexiones {
        - Map~String, List~String~~ grafoArtistas
        + conectarArtistas(String a1, String a2)
        + recomendarSimilares(String artista) List~String~
    }

    SoundWaveApp --> CatalogoMusical : coordina
    SoundWaveApp --> ReproductorLogico : coordina
    SoundWaveApp --> RankingServicio : coordina
    SoundWaveApp --> RecomendadorConexiones : coordina
    CatalogoMusical --> Cancion : almacena
    ReproductorLogico --> Cancion : agenda
    RankingServicio --> Cancion : prioriza
    RecomendadorConexiones --> Artista : relaciona
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
