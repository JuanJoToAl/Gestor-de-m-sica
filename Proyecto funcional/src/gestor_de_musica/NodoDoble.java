package gestor_de_musica;

public class NodoDoble {
    public Cancion cancion;
    public NodoDoble siguiente;
    public NodoDoble anterior;

    public NodoDoble(Cancion cancion) {
        this.cancion = cancion;
        this.siguiente = null;
        this.anterior = null;
    }
}
