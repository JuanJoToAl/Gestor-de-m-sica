
package gestor_de_musica;

public class Cancion {
    private String id;
    private String titulo;
    private String artista;
    private String genero;
    private int reproducciones;

    public Cancion(String id, String titulo, String artista, String genero) {
        this.id = id;
        this.titulo = titulo;
        this.artista = artista;
        this.genero = genero;
        this.reproducciones = 0;
    }

    public String getTitulo() { return titulo; }
    public String getArtista() { return artista; }
    // Otros getters y setters se pueden poner aquí :v
}