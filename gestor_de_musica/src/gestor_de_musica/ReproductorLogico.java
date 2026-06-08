package gestor_de_musica;

/**
 * MÓDULO DEL INTEGRANTE 2: REPRODUCTOR LÓGICO
 * * Cumple con los criterios del programa:
 * - Encapsulamiento: Atributos privados y métodos públicos.
 * - Eficiencia: Operaciones de inserción y recorrido en O(1).
 * - Modularidad: Cero impresión en consola (System.out), solo maneja lógica de datos.
 */
public class ReproductorLogico {

    // ESTRUCTURA 1: Lista doblemente enlazada para la playlist principal
    private NodoDoble cabeza;
    private NodoDoble cola;
    private NodoDoble actual;

    // Indice que representa la posicion de la cancion actual en la playlist
    private int indiceActual;

    // ESTRUCTURA 2: Cola FIFO para las canciones en espera de reproduccion
    private NodoSimple frente;
    private NodoSimple fin;

    public ReproductorLogico() {
        this.cabeza = null;
        this.cola = null;
        this.actual = null;
        this.indiceActual = -1; // Comienza en -1 indicando playlist vacia
        this.frente = null;
        this.fin = null;
    }

    // Agrega una cancion al final de la playlist principal
    public void agregarAPlaylist(Cancion cancion) {
        NodoDoble nuevoNodo = new NodoDoble(cancion);
        if (cabeza == null) {
            cabeza = nuevoNodo;
            cola = nuevoNodo;
            actual = nuevoNodo;
            indiceActual = 0; // Primera cancion agregada tiene indice 0
        } else {
            cola.siguiente = nuevoNodo;
            nuevoNodo.anterior = cola;
            cola = nuevoNodo;
        }
    }

    // Agrega una cancion a la cola de espera de reproduccion (FIFO)
    public void agregarACola(Cancion cancion) {
        NodoSimple nuevoNodo = new NodoSimple(cancion);
        if (frente == null) {
            frente = nuevoNodo;
            fin = nuevoNodo;
        } else {
            fin.siguiente = nuevoNodo;
            fin = nuevoNodo;
        }
    }

    // Determina la siguiente cancion, priorizando la cola de espera
    public Cancion reproducirSiguiente() {
        // Valida primero si hay elementos en la cola de espera
        if (frente != null) {
            Cancion cancionSiguiente = frente.cancion;
            frente = frente.siguiente; // Desencola la cancion
            if (frente == null) {
                fin = null; // Resetea fin si queda vacia
            }
            return cancionSiguiente; // No se altera el indiceActual de la playlist
        }

        // Si la cola esta vacia, avanza en la lista doblemente enlazada
        if (actual != null && actual.siguiente != null) {
            actual = actual.siguiente;
            indiceActual++; // Incrementa el indice de la playlist
            return actual.cancion;
        }

        return null; // Retorna null si no hay mas canciones
    }

    // Retrocede a la cancion anterior en la playlist
    public Cancion reproducirAnterior() {
        // Valida si se puede retroceder en la lista doblemente enlazada
        if (actual != null && actual.anterior != null) {
            actual = actual.anterior;
            indiceActual--; // Decrementa el indice de la playlist
            return actual.cancion;
        }
        return null; // Retorna null si ya esta al inicio de la playlist
    }

    // Devuelve la cancion actual de la playlist
    public Cancion getCancionActual() {
        if (actual != null) {
            return actual.cancion;
        }
        return null;
    }

    // Devuelve el indice actual de la reproduccion en la playlist
    public int getIndiceActual() {
        return this.indiceActual;
    }
}
