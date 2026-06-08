package gestor_de_musica;

// Clase de pruebas unitarias para el modulo ReproductorLogico
public class ReproductorLogicoTest {

    public static void main(String[] args) {
        System.out.println("Iniciando pruebas unitarias de ReproductorLogico...");

        try {
            testPlaylistVacia();
            testFlujoSecuencialPlaylist();
            testFilaDeEsperaPrioritaria();
            testFlujoMixto();

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

    // Valida el comportamiento correcto cuando no hay canciones cargadas
    private static void testPlaylistVacia() {
        ReproductorLogico reproductor = new ReproductorLogico();
        
        // Verifica que la playlist vacia tenga indice -1 y cancion nula
        assertEquals(-1, reproductor.getIndiceActual(), "El indice inicial de playlist vacia debe ser -1");
        assertNull(reproductor.getCancionActual(), "La cancion actual de playlist vacia debe ser null");
        assertNull(reproductor.reproducirSiguiente(), "Siguiente en playlist vacia debe retornar null");
        assertNull(reproductor.reproducirAnterior(), "Anterior en playlist vacia debe retornar null");
    }

    // Valida la navegacion secuencial hacia adelante y hacia atras en la playlist
    private static void testFlujoSecuencialPlaylist() {
        ReproductorLogico reproductor = new ReproductorLogico();
        Cancion c1 = new Cancion("1", "Song 1", "Artist 1", "Rock");
        Cancion c2 = new Cancion("2", "Song 2", "Artist 2", "Pop");
        Cancion c3 = new Cancion("3", "Song 3", "Artist 3", "Indie");

        reproductor.agregarAPlaylist(c1);
        reproductor.agregarAPlaylist(c2);
        reproductor.agregarAPlaylist(c3);

        // Verifica la cancion inicial al cargar canciones
        assertEquals(0, reproductor.getIndiceActual(), "El indice inicial de reproduccion debe ser 0");
        assertEquals(c1.getTitulo(), reproductor.getCancionActual().getTitulo(), "La cancion actual debe ser la primera agregada");

        // Simula avanzar pistas secuencialmente
        Cancion sig1 = reproductor.reproducirSiguiente();
        assertEquals(1, reproductor.getIndiceActual(), "El indice debe avanzar a 1");
        assertEquals(c2.getTitulo(), sig1.getTitulo(), "Debe sonar la segunda cancion");

        Cancion sig2 = reproductor.reproducirSiguiente();
        assertEquals(2, reproductor.getIndiceActual(), "El indice debe avanzar a 2");
        assertEquals(c3.getTitulo(), sig2.getTitulo(), "Debe sonar la tercera cancion");

        // Valida fin de playlist
        assertNull(reproductor.reproducirSiguiente(), "No hay mas canciones, debe retornar null al intentar avanzar");
        assertEquals(2, reproductor.getIndiceActual(), "El indice debe permanecer en 2 tras intentar avanzar al final");

        // Simula retroceder pistas secuencialmente
        Cancion ant1 = reproductor.reproducirAnterior();
        assertEquals(1, reproductor.getIndiceActual(), "El indice debe retroceder a 1");
        assertEquals(c2.getTitulo(), ant1.getTitulo(), "Debe sonar la segunda cancion al retroceder");

        Cancion ant2 = reproductor.reproducirAnterior();
        assertEquals(0, reproductor.getIndiceActual(), "El indice debe retroceder a 0");
        assertEquals(c1.getTitulo(), ant2.getTitulo(), "Debe sonar la primera cancion al retroceder");

        // Valida inicio de playlist
        assertNull(reproductor.reproducirAnterior(), "Debe retornar null al intentar retroceder antes de la primera cancion");
        assertEquals(0, reproductor.getIndiceActual(), "El indice debe permanecer en 0 tras intentar retroceder al inicio");
    }

    // Valida que la fila de espera tenga prioridad y que no altere el estado de la playlist principal
    private static void testFilaDeEsperaPrioritaria() {
        ReproductorLogico reproductor = new ReproductorLogico();
        Cancion c1 = new Cancion("1", "Song 1", "Artist 1", "Rock");
        Cancion c2 = new Cancion("2", "Song 2", "Artist 2", "Pop");
        Cancion c3 = new Cancion("3", "Song 3", "Artist 3", "Indie");

        reproductor.agregarAPlaylist(c1);
        reproductor.agregarAPlaylist(c2);
        reproductor.agregarAPlaylist(c3);

        Cancion cx = new Cancion("10", "Queue Song X", "Artist X", "Metal");
        Cancion cy = new Cancion("11", "Queue Song Y", "Artist Y", "Jazz");

        // Agrega canciones a la cola de espera ("A continuacion")
        reproductor.agregarACola(cx);
        reproductor.agregarACola(cy);

        // Al avanzar, debe priorizar la cola de espera sin alterar la playlist actual
        Cancion sigQueue1 = reproductor.reproducirSiguiente();
        assertEquals(cx.getTitulo(), sigQueue1.getTitulo(), "Debe sonar la primera cancion de la cola de espera");
        assertEquals(0, reproductor.getIndiceActual(), "El indice actual en playlist debe permanecer en 0");

        Cancion sigQueue2 = reproductor.reproducirSiguiente();
        assertEquals(cy.getTitulo(), sigQueue2.getTitulo(), "Debe sonar la segunda cancion de la cola de espera");
        assertEquals(0, reproductor.getIndiceActual(), "El indice actual en playlist debe permanecer en 0");

        // Una vez vacia la cola de espera, avanzar debe continuar con el orden de la playlist
        Cancion sigPlaylist = reproductor.reproducirSiguiente();
        assertEquals(c2.getTitulo(), sigPlaylist.getTitulo(), "Debe sonar la siguiente cancion en la playlist principal");
        assertEquals(1, reproductor.getIndiceActual(), "El indice actual debe avanzar a 1");
    }

    // Valida la interaccion mixta entre playlist y fila de espera al avanzar y retroceder
    private static void testFlujoMixto() {
        ReproductorLogico reproductor = new ReproductorLogico();
        Cancion c1 = new Cancion("1", "Song 1", "Artist 1", "Rock");
        Cancion c2 = new Cancion("2", "Song 2", "Artist 2", "Pop");
        Cancion c3 = new Cancion("3", "Song 3", "Artist 3", "Indie");

        reproductor.agregarAPlaylist(c1);
        reproductor.agregarAPlaylist(c2);
        reproductor.agregarAPlaylist(c3);

        // Avanzar a la segunda cancion
        reproductor.reproducirSiguiente();
        assertEquals(1, reproductor.getIndiceActual(), "Indice debe estar en 1");

        // Encolar cancion de espera
        Cancion cz = new Cancion("12", "Queue Song Z", "Artist Z", "Blues");
        reproductor.agregarACola(cz);

        // Siguiente debe sonar la de la cola de espera
        Cancion res1 = reproductor.reproducirSiguiente();
        assertEquals(cz.getTitulo(), res1.getTitulo(), "Debe reproducir la cancion de la cola");
        assertEquals(1, reproductor.getIndiceActual(), "El indice de playlist no debe cambiar tras reproducir de la cola");

        // Retroceder debe regresar a la primera cancion de la playlist
        Cancion res2 = reproductor.reproducirAnterior();
        assertEquals(c1.getTitulo(), res2.getTitulo(), "Retroceder debe ir a la cancion 1 de la playlist principal");
        assertEquals(0, reproductor.getIndiceActual(), "El indice de playlist debe retroceder a 0");
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

    private static void assertNull(Object obj, String msg) {
        if (obj != null) {
            throw new AssertionError(msg + " | Se esperaba null pero se obtuvo un objeto");
        }
    }
}
