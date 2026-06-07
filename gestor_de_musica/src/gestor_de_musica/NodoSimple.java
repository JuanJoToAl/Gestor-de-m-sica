
package gestor_de_musica;

public class NodoSimple {
    public Cancion cancion;
    public NodoSimple siguiente;

    public NodoSimple(Cancion cancion) {
        this.cancion = cancion;
        this.siguiente = null;
    }
}