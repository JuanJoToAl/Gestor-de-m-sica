

package gestor_de_musica;

/**
 * MÓDULO DEL INTEGRANTE 2: REPRODUCTOR LÓGICO
 * * Cumple con los criterios del programa:
 * - Encapsulamiento: Atributos privados y métodos públicos.
 * - Eficiencia: Operaciones de inserción y recorrido en O(1).
 * - Modularidad: Cero impresión en consola (System.out), solo maneja lógica de datos.
 */
public class ReproductorLogico {

    // === ESTRUCTURA 1: LISTA DOBLE ENLAZADA (Playlist Base) ===
    private NodoDoble cabeza;
    private NodoDoble cola;
    private NodoDoble actual; // Puntero clave: rastrea la canción que está sonando

    // === ESTRUCTURA 2: COLA / QUEUE (Fila de espera "A continuación") ===
    private NodoSimple frente;
    private NodoSimple fin;

    public ReproductorLogico() {
        this.cabeza = null;
        this.cola = null;
        this.actual = null;
        this.frente = null;
        this.fin = null;
    }

    /**
     * Añade una canción al final de la playlist principal (Lista Doble).
     * Eficiencia: O(1) gracias al puntero 'cola'.
     * @param cancion
     */
    public void agregarAPlaylist(Cancion cancion) {
        NodoDoble nuevoNodo = new NodoDoble(cancion);
        if (cabeza == null) {
            cabeza = nuevoNodo;
            cola = nuevoNodo;
            actual = nuevoNodo; // La primera canción agregada queda como actual
        } else {
            cola.siguiente = nuevoNodo;
            nuevoNodo.anterior = cola;
            cola = nuevoNodo;
        }
    }

    /**
     * Añade una canción a la fila de espera "A continuación" (Cola FIFO).
     * El usuario elige qué escuchar después sin alterar el orden de la playlist.
     * Eficiencia: O(1) gracias al puntero 'fin'.
     * @param cancion
     */
    public void agregarAColaEspera(Cancion cancion) {
        NodoSimple nuevoNodo = new NodoSimple(cancion);
        if (frente == null) {
            frente = nuevoNodo;
            fin = nuevoNodo;
        } else {
            fin.siguiente = nuevoNodo;
            fin = nuevoNodo;
        }
    }

    /**
     * Determina la siguiente canción a reproducir.
     * REGLA DE NEGOCIO: Prioriza la fila de espera (Cola). Si está vacía,
     * avanza al siguiente elemento de la playlist (Lista Doble).
     * @return Objeto Cancion que debe sonar, o null si ya no hay más música.
     */
    public Cancion reproducirSiguiente() {
        // 1. Verificamos si hay canciones prioritarias en la Cola de Espera (FIFO)
        if (frente != null) {
            Cancion cancionSiguiente = frente.cancion;
            frente = frente.siguiente; // Desencolar
            if (frente == null) {
                fin = null; // Si la cola quedó vacía, limpiamos el puntero final
            }
            return cancionSiguiente; 
            // NOTA: Al salir de la cola, no movemos el puntero 'actual' de la lista doble
            // para que cuando termine la cola, el usuario regrese a donde iba en su álbum.
        }

        // 2. Si la cola está vacía, avanzamos en la Lista Doble Enlazada
        if (actual != null && actual.siguiente != null) {
            actual = actual.siguiente;
            return actual.cancion;
        }

        return null; // Fin de la playlist y sin canciones en cola
    }

    /**
     * Retrocede a la canción anterior en la playlist.
     * Las canciones en la fila de espera no afectan al historial hacia atrás.
     * Eficiencia: O(1) gracias al puntero 'anterior' de la Lista Doble.
     * @return Objeto Cancion anterior, o null si ya está en la primera canción.
     */
    public Cancion reproducirAnterior() {
        if (actual != null && actual.anterior != null) {
            actual = actual.anterior;
            return actual.cancion;
        }
        return null; // Ya está al inicio de la lista
    }

    /**
     * Devuelve la canción que está seleccionada actualmente en la playlist.
     * @return 
     */
    public Cancion getCancionActual() {
        if (actual != null) {
            return actual.cancion;
        }
        return null;
    }
}

