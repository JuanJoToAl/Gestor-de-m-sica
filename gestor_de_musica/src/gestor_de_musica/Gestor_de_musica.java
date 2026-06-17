package gestor_de_musica;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Gestor_de_musica {

    // Mapa global para guardar los artistas leídos y usarlos en el recomendador (Grafo)
    private static Map<String, Artista> mapaArtistas = new HashMap<>();
    
    // Estructura para validar en tiempo real que los IDs sean totalmente únicos
    private static Set<String> idsExistentes = new HashSet<>();

    public static void main(String[] args) {
        // 1. INSTANCIAR TODOS LOS MÓDULOS DEL SISTEMA
        CatalogoMusical catalogo = new CatalogoMusical();
        ReproductorLogico reproductor = new ReproductorLogico();
        RankingServicio ranking = new RankingServicio();
        HistorialMusica historial = new HistorialMusica();
        RecomendadorConexiones recomendador = new RecomendadorConexiones();

        // 2. PRECARGA DINÁMICA DE DATOS DESDE ARCHIVO DE TEXTO
        cargarDatosDesdeArchivo(catalogo, reproductor, recomendador);

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
            System.out.println("8. Detener Música (Apagar el reproductor)");
            System.out.println("9. Salir");
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
                    
                    // VALIDACIÓN EN BUCLE DEL ID ÚNICO
                    String id = "";
                    while (true) {
                        System.out.print("Ingrese ID único: ");
                        id = teclado.nextLine().trim();
                        
                        if (id.isEmpty()) {
                            System.out.println("El ID no puede estar vacío. Intente de nuevo.");
                        } else if (idsExistentes.contains(id)) {
                            System.out.println("Error: El ID '" + id + "' ya está registrado por otra canción. Ingrese uno diferente.");
                        } else {
                            break; // El ID es válido y no está repetido, salimos del bucle
                        }
                    }
                    
                    System.out.print("Ingrese Título: ");
                    String titulo = teclado.nextLine();
                    System.out.print("Ingrese Artista: ");
                    String artNombre = teclado.nextLine();
                    System.out.print("Ingrese Género: ");
                    String genero = teclado.nextLine();
                    
                    // SOLICITUD DEL NOMBRE DEL ARCHIVO (CONCATENACIÓN AUTOMÁTICA)
                    System.out.print("Ingrese solo el nombre del archivo (ej: bohemian_rhapsody): ");
                    String nombreArchivo = teclado.nextLine().trim();

                    // Construimos la ruta completa de forma interna agregando "musica/" al inicio y ".mp3" al final
                    String ruta = "musica/" + nombreArchivo + ".mp3";

                    // Instanciar el objeto con el constructor de 5 parámetros incluyendo la ruta automatizada
                    Cancion nueva = new Cancion(id, titulo, artNombre, genero, ruta);
                    
                    // A. Insertar en las estructuras en memoria (Disponibilidad inmediata)
                    catalogo.insertarCancion(nueva);
                    reproductor.agregarAPlaylist(nueva); 
                    idsExistentes.add(id); // Registrar el ID en el Set para evitar que se repita en esta misma sesión

                    // Coherencia del módulo de recomendaciones (Grafo)
                    String claveArtista = artNombre.toLowerCase();
                    Artista artistaObj = mapaArtistas.getOrDefault(claveArtista, new Artista(artNombre, genero));
                    mapaArtistas.putIfAbsent(claveArtista, artistaObj);

                    for (Artista a : mapaArtistas.values()) {
                        if (!a.getNombre().equalsIgnoreCase(artNombre) && a.getGenero().equalsIgnoreCase(genero)) {
                            recomendador.conectarArtistas(artistaObj, a);
                        }
                    }

                    // B. Persistencia: Escribir de forma permanente en el archivo de texto
                    try (FileWriter fw = new FileWriter("canciones.txt", true);
                         PrintWriter pw = new PrintWriter(fw)) {
                        
                        // Guardamos con la estructura id|titulo|artista|genero|ruta
                        pw.println(id + "|" + titulo + "|" + artNombre + "|" + genero + "|" + ruta);
                        System.out.println("¡Canción subida con éxito al catálogo de la RAM!");
                        System.out.println("💾 ¡Guardado permanente completado en 'canciones.txt'!");
                        
                    } catch (IOException e) {
                        System.out.println("Canción lista en RAM, pero falló el guardado en disco: " + e.getMessage());
                    }

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
                    System.out.println("\n--- REPRODUCTOR DE MÚSICA ---");
                    Cancion sonando = reproductor.reproducirSiguiente();
                    
                    if (sonando != null) {
                        System.out.println("Escuchando ahora: " + sonando.getTitulo() + " - " + sonando.getArtista());
                        ranking.actualizarReproduccion(sonando);
                        historial.escucharCancion(sonando);
                    } else {
                        System.out.println("No hay más canciones en la playlist ni en la cola de espera.");
                    }
                    presionarEnterParaContinuar(teclado);
                    break;

                case 5:
                    System.out.println("\n--- HISTORIAL DE REPRODUCCIÓN (Pila LIFO) ---");
                    // CAMBIO CRÍTICO: Invocamos el método que recorre toda la pila sin destruirla
                    historial.mostrarHistorialCompleto();
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
                    System.out.print("Escriba el nombre exacto del artista para ver similares: ");
                    String buscarArtista = teclado.nextLine();
                    
                    Artista temporal = mapaArtistas.get(buscarArtista.toLowerCase());

                    if (temporal != null) {
                        List<Artista> similares = recomendador.recomendarSimilares(temporal);
                        System.out.println("Artistas similares/relacionados en el Grafo:");
                        if (similares.isEmpty()) {
                            System.out.println(" ➜ No hay conexiones para este artista.");
                        } else {
                            for (Artista a : similares) {
                                System.out.println(" ➜ " + a.getNombre() + " (" + a.getGenero() + ")");
                            }
                        }
                    } else {
                        System.out.println("Artista no encontrado en la base de datos.");
                    }
                    presionarEnterParaContinuar(teclado);
                    break;

                case 8:
                    System.out.println("\n--- DETENIENDO MÚSICA ---");
                    reproductor.detenerMusica(); 
                    System.out.println("⏹️ La reproducción ha sido detenida.");
                    presionarEnterParaContinuar(teclado);
                    break;

                case 9:
                    reproductor.detenerMusica(); 
                    System.out.println("Cerrando el Gestor de Música. ¡Hasta luego!");
                    break;

                default:
                    System.out.println("Opción inválida. Intente de nuevo.");
                    presionarEnterParaContinuar(teclado);
            }
        } while (opcion != 9);

        teclado.close();
    }

    // MÉTODO PARA LEER EL ARCHIVO DE TEXTO
    private static void cargarDatosDesdeArchivo(CatalogoMusical catalogo, ReproductorLogico reproductor, RecomendadorConexiones recomendador) {
        File archivo = new File("canciones.txt");
        
        if (!archivo.exists()) {
            System.out.println("No se encontró 'canciones.txt'. Inicia sin canciones base.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            System.out.println("Cargando base de datos musical...");
            
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue; 
                
                String[] datos = linea.split("\\|");
                
                if (datos.length >= 4) {
                    String id = datos[0].trim();
                    String titulo = datos[1].trim();
                    String nombreArtista = datos[2].trim();
                    String genero = datos[3].trim();
                    String ruta = (datos.length > 4) ? datos[4].trim() : "";

                    // Registrar el ID leído en el conjunto de IDs existentes
                    idsExistentes.add(id);

                    // 1. Manejo dinámico de Artistas y el Grafo
                    String claveArtista = nombreArtista.toLowerCase();
                    Artista artistaObj = mapaArtistas.getOrDefault(claveArtista, new Artista(nombreArtista, genero));
                    mapaArtistas.putIfAbsent(claveArtista, artistaObj);

                    // Conectar automáticamente a artistas si son del mismo género musical
                    for (Artista a : mapaArtistas.values()) {
                        if (!a.getNombre().equalsIgnoreCase(nombreArtista) && a.getGenero().equalsIgnoreCase(genero)) {
                            recomendador.conectarArtistas(artistaObj, a);
                        }
                    }

                    // 2. Creación e Inserción de la Canción
                    Cancion nueva = new Cancion(id, titulo, nombreArtista, genero, ruta);
                    catalogo.insertarCancion(nueva);
                    reproductor.agregarAPlaylist(nueva);
                }
            }
            System.out.println("¡Precarga finalizada con éxito!");
            
        } catch (Exception e) {
            System.out.println("Error al leer el archivo de canciones: " + e.getMessage());
        }
    }

    private static void presionarEnterParaContinuar(Scanner scanner) {
        System.out.println("\n[ Presione ENTER para continuar... ]");
        scanner.nextLine();
    }
}