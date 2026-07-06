package gestor_de_musica;

/**
 * Entidad básica: Artista
 *
 * POJO compartido por todos los módulos.
 * El RecomendadorConexiones (Módulo 4) lo usa como nodo del grafo.
 */
public class Artista {

    private String nombre;
    private String genero;

    public Artista(String nombre, String genero) {
        this.nombre = nombre;
        this.genero = genero;
    }

    public String getNombre() { return nombre; }
    public String getGenero() { return genero; }

    @Override
    public String toString() {
        return nombre + " (" + genero + ")";
    }
}
