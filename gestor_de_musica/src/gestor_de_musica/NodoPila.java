package gestor_de_musica;

// Nodo para la estructura de la Pila
public class NodoPila {
    public Cancion cancion;
    public NodoPila abajo;

    public NodoPila(Cancion cancion) {
        this.cancion = cancion;
        this.abajo = null;
    }
}