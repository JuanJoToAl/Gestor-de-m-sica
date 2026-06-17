package gestor_de_musica;

// Estructura de Pila Dinámica para el Historial de Canciones Escuchadas
public class HistorialMusica {
    private NodoPila tope; // Última canción escuchada

    public HistorialMusica() {
        this.tope = null;
    }

    // Push: Añadir canción al historial al terminar de reproducirla
    public void escucharCancion(Cancion c) {
        NodoPila nuevo = new NodoPila(c);
        nuevo.abajo = tope;
        tope = nuevo;
    }

    // Pop: Obtener la última canción escuchada (para un botón "Volver a escuchar")
    public Cancion obtenerUltimaEscuchada() {
        if (tope == null) return null;
        Cancion c = tope.cancion;
        tope = tope.abajo; // Se remueve del tope
        return c;
    }
    
    public boolean estaVacio() {
        return tope == null;
    }
}