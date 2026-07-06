package gestor_de_musica;

public class Cancion {
    private String id;
    private String titulo;
    private String artista;
    private String genero;
    private int reproducciones;
    private String rutaArchivo; // Atributo para almacenar la ubicación del MP3

    // Constructor 1: El original (Evita que se rompa la precarga o código actual en el Main)
    public Cancion(String id, String titulo, String artista, String genero) {
        this.id = id;
        this.titulo = titulo;
        this.artista = artista;
        this.genero = genero;
        this.reproducciones = 0; // Inicia en cero automáticamente
        this.rutaArchivo = "";   // Inicializa vacío por defecto para evitar valores nulos
    }

    // Constructor 2: Sobrecargado (Útil para cuando decidas pasarle la ruta directamente)
    public Cancion(String id, String titulo, String artista, String genero, String rutaArchivo) {
        this.id = id;
        this.titulo = titulo;
        this.artista = artista;
        this.genero = genero;
        this.reproducciones = 0;
        this.rutaArchivo = rutaArchivo;
    }

    // === GETTERS Y SETTERS COMPLETO ===
    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getArtista() { return artista; }
    public String getGenero() { return genero; }
    public int getReproducciones() { return reproducciones; }

    // >>> NUEVOS MÉTODOS PARA EL FUNCIONAMIENTO DEL REPRODUCTOR FÍSICO <<<
    public String getRutaArchivo() { 
        return rutaArchivo; 
    }

    public void setRutaArchivo(String rutaArchivo) { 
        this.rutaArchivo = rutaArchivo; 
    }

    /**
     * Requerido por el Módulo de RankingServicio para actualizar el Heap
     */
    public void incrementarReproducciones() {
        this.reproducciones++;
    }

    @Override
    public String toString() {
        return titulo + " - " + artista + " [" + genero + "] (" + reproducciones + " repros)";
    }
}