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
    
    // MÉTODO CORREGIDO: Recorre los nodos uno a uno sin destruirlos
    public void mostrarHistorialCompleto() {
        if (this.estaVacio()) { 
            System.out.println("El historial está vacío. ¡Pon a sonar algunas canciones primero!");
            return;
        }

        System.out.println("Mostrando últimas reproducciones (de la más reciente a la más antigua):\n");
        
        // Creamos un nodo auxiliar para no perder ni modificar la referencia del "tope" real
        NodoPila actual = tope; 
        
        // El bucle continuará bajando por la pila hasta que no queden más nodos (abajo sea null)
        while (actual != null) {
            Cancion c = actual.cancion;
            System.out.println(c.getTitulo() + " - " + c.getArtista() + " [" + c.getGenero() + "]");
            
            // Avanzamos al nodo que está abajo en la pila
            actual = actual.abajo; 
        }
    }
}