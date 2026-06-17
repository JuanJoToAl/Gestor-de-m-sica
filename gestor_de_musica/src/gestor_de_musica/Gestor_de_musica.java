package gestor_de_musica;

import java.util.List;
import java.util.Scanner;

public class Gestor_de_musica {

    public static void main(String[] args) {
        // 1. INSTANCIAR TODOS LOS MÓDULOS DEL SISTEMA
        CatalogoMusical catalogo = new CatalogoMusical();
        ReproductorLogico reproductor = new ReproductorLogico();
        RankingServicio ranking = new RankingServicio();
        HistorialMusica historial = new HistorialMusica();
        RecomendadorConexiones recomendador = new RecomendadorConexiones();

        // 2. PRECARGA AUTOMÁTICA DE DATOS
        Artista art1 = new Artista("Linkin Park", "Rock");
        Artista art2 = new Artista("Evanescence", "Rock");
        Artista art3 = new Artista("Michael Jackson", "Pop");
        Artista art4 = new Artista("Bruno Mars", "Pop");

        recomendador.conectarArtistas(art1, art2);
        recomendador.conectarArtistas(art3, art4);

        Cancion c1 = new Cancion("1", "In the End", "Linkin Park", "Rock");
        Cancion c2 = new Cancion("2", "Bring Me to Life", "Evanescence", "Rock");
        Cancion c3 = new Cancion("3", "Billie Jean", "Michael Jackson", "Pop");
        Cancion c4 = new Cancion("4", "Uptown Funk", "Bruno Mars", "Pop");

        catalogo.insertarCancion(c1);
        catalogo.insertarCancion(c2);
        catalogo.insertarCancion(c3);
        catalogo.insertarCancion(c4);

        reproductor.agregarAPlaylist(c1);
        reproductor.agregarAPlaylist(c2);
        reproductor.agregarAPlaylist(c3);
        reproductor.agregarAPlaylist(c4);

        // 3. MENÚ INTERACTIVO POR CONSOLA
        Scanner teclado = new Scanner(System.in);
        int opcion = 0;

        System.out.println("\n=================================================");
        System.out.println("   ¡BIENVENIDO AL GESTOR DE MÚSICA DE LA NACHO!  ");
        System.out.println("=================================================");

        do {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Ver Catálogo Completo (Tabla Hash)");
            System.out.println("2. Subir/Registrar nueva canción manualmente");
            System.out.println("3. Agregar canción a la Fila de Espera (Cola FIFO)");
            System.out.println("4. Reproducir Siguiente Canción (Avanzar / Coherencia de módulos)");
            System.out.println("5. Ver Historial de reproducción (Pila LIFO)");
            System.out.println("6. Ver Top 5 canciones más escuchadas (Heap/Árbol)");
            System.out.println("7. Ver recomendaciones de artistas similares (Grafo)");
            System.out.println("8. Salir");
            System.out.print("Seleccione una opción: ");
            
            try {
                opcion = Integer.parseInt(teclado.nextLine());
            } catch (NumberFormatException e) {
                opcion = 0; 
            }

            switch (opcion) {
                case 1:
                    catalogo.mostrarCatalogo();
                    presionarEnterParaContinuar(teclado);
                    break;

                case 2:
                    System.out.println("\n--- REGISTRAR NUEVA CANCIÓN ---");
                    System.out.print("Ingrese ID único: ");
                    String id = teclado.nextLine();
                    System.out.print("Ingrese Título: ");
                    String titulo = teclado.nextLine();
                    System.out.print("Ingrese Artista: ");
                    String artNombre = teclado.nextLine();
                    System.out.print("Ingrese Género: ");
                    String genero = teclado.nextLine();

                    Cancion nueva = new Cancion(id, titulo, artNombre, genero);
                    catalogo.insertarCancion(nueva);
                    reproductor.agregarAPlaylist(nueva); 
                    System.out.println("¡Canción subida con éxito al catálogo y a la playlist!");
                    presionarEnterParaContinuar(teclado);
                    break;

                case 3:
                    System.out.println("\n--- AGREGAR A LA FILA DE ESPERA ---");
                    System.out.print("Escriba el TÍTULO de la canción que quiere encolar: ");
                    String titBuscar = teclado.nextLine();
                    Cancion encontrada = catalogo.buscarCancion(titBuscar);
                    
                    if (encontrada != null) {
                        reproductor.agregarAColaEspera(encontrada);
                        System.out.println("\"" + encontrada.getTitulo() + "\" ha sido agregada a la fila de espera.");
                    } else {
                        System.out.println("Canción no encontrada en el catálogo.");
                    }
                    presionarEnterParaContinuar(teclado);
                    break;

                case 4:
                    System.out.println("\n--- REPRODUCIDOR DE MÚSICA ---");
                    Cancion sonando = reproductor.reproducirSiguiente();
                    
                    if (sonando != null) {
                        System.out.println("🎶 Escuchando ahora: " + sonando.getTitulo() + " - " + sonando.getArtista());
                        ranking.actualizarReproduccion(sonando);
                        historial.escucharCancion(sonando);
                    } else {
                        System.out.println("No hay más canciones en la playlist ni en la cola de espera.");
                    }
                    presionarEnterParaContinuar(teclado);
                    break;

                case 5:
                    System.out.println("\n--- HISTORIAL DE REPRODUCCIÓN (Pila LIFO) ---");
                    if (historial.estaVacio()) {
                        System.out.println("El historial está vacío. ¡Pon a sonar algunas canciones primero!");
                    } else {
                        System.out.println("Sacando la última canción escuchada de la Pila...");
                        Cancion ultima = historial.obtenerUltimaEscuchada();
                        System.out.println("⏪ Última escuchada extraída: " + ultima.getTitulo() + " [" + ultima.getGenero() + "]");
                    }
                    presionarEnterParaContinuar(teclado);
                    break;

                case 6:
                    System.out.println("\n--- TOP 5 POPULARIDAD (Max-Heap) ---");
                    List<Cancion> top = ranking.obtenerTop5();
                    if (top.isEmpty()) {
                        System.out.println("No hay datos de reproducciones aún.");
                    } else {
                        for (int i = 0; i < top.size(); i++) {
                            System.out.println((i + 1) + ". " + top.get(i));
                        }
                    }
                    presionarEnterParaContinuar(teclado);
                    break;

                case 7:
                    System.out.println("\n--- RECOMENDADOR (Grafo de Artistas) ---");
                    System.out.println("Artistas cargados en el grafo: Linkin Park, Evanescence, Michael Jackson, Bruno Mars.");
                    System.out.print("Escriba el nombre exacto del artista para ver similares: ");
                    String buscarArtista = teclado.nextLine();
                    
                    Artista temporal = null;
                    if (buscarArtista.equalsIgnoreCase("Linkin Park")) temporal = art1;
                    if (buscarArtista.equalsIgnoreCase("Evanescence")) temporal = art2;
                    if (buscarArtista.equalsIgnoreCase("Michael Jackson")) temporal = art3;
                    if (buscarArtista.equalsIgnoreCase("Bruno Mars")) temporal = art4;

                    if (temporal != null) {
                        List<Artista> similares = recomendador.recomendarSimilares(temporal);
                        System.out.println("Artistas similares/relacionados en el Grafo:");
                        for (Artista a : similares) {
                            System.out.println(" ➜ " + a.getNombre() + " (" + a.getGenero() + ")");
                        }
                    } else {
                        System.out.println("Artista no encontrado en el grafo de prueba.");
                    }
                    presionarEnterParaContinuar(teclado);
                    break;

                case 8:
                    System.out.println("Cerrando el Gestor de Música. ¡Hasta luego!");
                    break;

                default:
                    System.out.println("Opción inválida. Intente de nuevo.");
                    presionarEnterParaContinuar(teclado);
            }
        } while (opcion != 8);

        teclado.close();
    }

    private static void presionarEnterParaContinuar(Scanner scanner) {
        System.out.println("\n[ Presione ENTER para continuar... ]");
        scanner.nextLine();
    }
}