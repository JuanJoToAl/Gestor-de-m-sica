import java.util.Objects;

public class Artista {
    private String nombre;
    private String genero;

    public Artista(String nombre, String genero) {
        this.nombre = nombre;
        this.genero = genero;
    }

    public String getNombre() {
        return nombre;
    }

    public String getGenero() {
        return genero;
    }

    // Métodos obligatorios para que el Artista funcione como llave (Key) en el Grafo
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artista artista = (Artista) o;
        return Objects.equals(nombre, artista.nombre); 
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
    
    // Para que al imprimir el artista, se lea su nombre y no su espacio en memoria
    @Override
    public String toString() {
        return nombre;
    }
}
