package gestor_de_musica;

import java.util.List;

// Clase de pruebas unitarias para el modulo RankingServicio
public class RankingServicioTest {

    public static void main(String[] args) {
        System.out.println("Iniciando pruebas unitarias de RankingServicio...");

        try {
            testRankingVacio();
            testReordenamientoPorReproducciones();
            testTop5NoRompeHeap();
            testTop5ConMenosDeCinco();

            System.out.println("Todas las pruebas pasaron exitosamente.");
        } catch (AssertionError e) {
            System.err.println("PRUEBA FALLIDA: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("ERROR INESPERADO DURANTE PRUEBAS: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    // Valida que obtenerTop5 retorna lista vacia cuando no hay canciones
    private static void testRankingVacio() {
        RankingServicio servicio = new RankingServicio();
        List<Cancion> top = servicio.obtenerTop5();
        assertEquals(0, top.size(), "El top debe estar vacio si no hay canciones registradas");
    }

    // Valida que al reproducir canciones repetidamente, estas suben en el ranking
    private static void testReordenamientoPorReproducciones() {
        RankingServicio servicio = new RankingServicio();
        Cancion c1 = new Cancion("1", "Cancion A", "Artista 1", "Rock");
        Cancion c2 = new Cancion("2", "Cancion B", "Artista 2", "Pop");
        Cancion c3 = new Cancion("3", "Cancion C", "Artista 3", "Indie");

        // Registra las canciones con distintas reproducciones
        servicio.actualizarReproduccion(c1); // c1 = 1 reproduccion
        servicio.actualizarReproduccion(c2); // c2 = 1 reproduccion
        servicio.actualizarReproduccion(c2); // c2 = 2 reproducciones
        servicio.actualizarReproduccion(c3); // c3 = 1 reproduccion
        servicio.actualizarReproduccion(c3); // c3 = 2 reproducciones
        servicio.actualizarReproduccion(c3); // c3 = 3 reproducciones

        // La cancion con mas reproducciones debe encabezar el top
        List<Cancion> top = servicio.obtenerTop5();
        assertEquals("Cancion C", top.get(0).getTitulo(), "La cancion con mas reproducciones debe ser la primera del top");
        assertEquals("Cancion B", top.get(1).getTitulo(), "La segunda cancion mas reproducida debe ser la segunda del top");
        assertEquals("Cancion A", top.get(2).getTitulo(), "La cancion menos reproducida debe ser la tercera del top");

        // Verifica los contadores de reproducciones exactos
        assertEquals(3, top.get(0).getReproducciones(), "Cancion C debe tener 3 reproducciones");
        assertEquals(2, top.get(1).getReproducciones(), "Cancion B debe tener 2 reproducciones");
        assertEquals(1, top.get(2).getReproducciones(), "Cancion A debe tener 1 reproduccion");
    }

    // Valida que llamar a obtenerTop5 repetidamente no destruye el heap original
    private static void testTop5NoRompeHeap() {
        RankingServicio servicio = new RankingServicio();
        Cancion c1 = new Cancion("1", "Cancion A", "Artista 1", "Rock");
        Cancion c2 = new Cancion("2", "Cancion B", "Artista 2", "Pop");

        servicio.actualizarReproduccion(c1);
        servicio.actualizarReproduccion(c2);
        servicio.actualizarReproduccion(c2); // c2 = 2 reproducciones, c1 = 1 reproduccion

        int tamanoAntesDeConsultas = servicio.getHeap().size();

        // Llamar a obtenerTop5 multiples veces no debe alterar el heap principal
        servicio.obtenerTop5();
        servicio.obtenerTop5();

        assertEquals(tamanoAntesDeConsultas, servicio.getHeap().size(), "El heap no debe cambiar de tamano tras llamar a obtenerTop5");

        // Los datos siguen siendo correctos despues de multiples consultas
        List<Cancion> top = servicio.obtenerTop5();
        assertEquals("Cancion B", top.get(0).getTitulo(), "Cancion B debe seguir siendo la primera del top");
    }

    // Valida que obtenerTop5 funciona correctamente cuando hay menos de 5 canciones
    private static void testTop5ConMenosDeCinco() {
        RankingServicio servicio = new RankingServicio();
        Cancion c1 = new Cancion("1", "Cancion X", "Artista 1", "Rock");
        Cancion c2 = new Cancion("2", "Cancion Y", "Artista 2", "Jazz");

        servicio.actualizarReproduccion(c1);
        servicio.actualizarReproduccion(c2);

        // Si solo hay 2 canciones, el top debe tener solo 2 elementos
        List<Cancion> top = servicio.obtenerTop5();
        assertEquals(2, top.size(), "El top debe tener solo el numero de canciones disponibles");
    }

    // Metodos auxiliares para aserciones
    private static void assertEquals(int expected, int actual, String msg) {
        if (expected != actual) {
            throw new AssertionError(msg + " | Esperado: " + expected + ", Obtenido: " + actual);
        }
    }

    private static void assertEquals(String expected, String actual, String msg) {
        if (expected == null && actual == null) return;
        if (expected == null || !expected.equals(actual)) {
            throw new AssertionError(msg + " | Esperado: " + expected + ", Obtenido: " + actual);
        }
    }
}
