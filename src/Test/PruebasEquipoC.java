package Test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

import Objetos.archivos_adjuntos;
import Objetos.ejecuciones_mantenimiento;
import Objetos.gamas_mantenimiento;
import Objetos.herramientas;
import DAO.gamas_manteniminetoDAO;
import DAO.herramientasDAO;

/**
 * Clase de pruebas JUnit4 para el proyecto EquipoC.
 *
 * Cubre los siguientes apartados:
 *   1. Constructores y toString() de las clases de Objetos.
 *   2. Operaciones CRUD de los DAOs (requiere conexión a la BD).
 *
 * IMPORTANTE: los tests de DAO (sección 2) necesitan que la base de datos
 * esté accesible con los parámetros configurados en Comun/conexion.java.
 * Si la BD no está disponible, estos tests fallarán con un error de conexión.
 *
 * @author EquipoC – generado para JUnit 4
 */
public class PruebasEquipoC {

    // -----------------------------------------------------------------------
    // Objetos reutilizados entre tests
    // -----------------------------------------------------------------------

    private gamas_mantenimiento gama;
    private herramientas herramienta;
    private archivos_adjuntos archivo;
    private ejecuciones_mantenimiento ejecucion;

    private gamas_manteniminetoDAO gamaDAO;
    private herramientasDAO herramientaDAO;

    // -----------------------------------------------------------------------
    // Configuración previa a cada test (@Before)
    // -----------------------------------------------------------------------

    /**
     * Se ejecuta antes de cada @Test.
     * Inicializa objetos con datos de prueba conocidos.
     */
    @Before
    public void setUp() {

        // Objeto gama_mantenimiento de prueba
        gama = new gamas_mantenimiento(
                1,
                "Mantenimiento Preventivo Mensual",
                "preventivo",
                "mensual",
                "Revisión general de maquinaria cada mes"
        );

        // Objeto herramientas de prueba
        herramienta = new herramientas(
                10,
                "Llave inglesa",
                "manual",
                "HER-001",
                "disponible",
                "Almacén A",
                true
        );

        // Objeto archivos_adjuntos de prueba
        archivo = new archivos_adjuntos(
                5,
                "informe_abril.pdf",
                "orden",
                "C:/archivos/informe_abril.pdf",
                3
        );

        // Objeto ejecuciones_mantenimiento de prueba
        Date fecha = new Date();
        Time inicio = new Time(8, 0, 0);
        Time fin    = new Time(10, 30, 0);
        ejecucion = new ejecuciones_mantenimiento(
                100,
                42,
                fecha,
                inicio,
                fin,
                7,
                "Sin incidencias",
                "completado"
        );

        // DAOs (para los tests de integración con BD)
        gamaDAO = new gamas_manteniminetoDAO();
        herramientaDAO = new herramientasDAO();
    }

    /**
     * Se ejecuta después de cada @Test.
     * Libera referencias para evitar fugas de memoria entre tests.
     */
    @After
    public void tearDown() {
        gama          = null;
        herramienta   = null;
        archivo       = null;
        ejecucion     = null;
        gamaDAO       = null;
        herramientaDAO = null;
    }

    // =========================================================================
    // SECCIÓN 1 – Pruebas de las clases de Objetos (sin BD)
    // =========================================================================

    // --- gamas_mantenimiento -------------------------------------------------

    /**
     * Verifica que el constructor vacío crea un objeto no nulo.
     */
    @Test
    public void testGamaConstructorVacio() {
        gamas_mantenimiento g = new gamas_mantenimiento();
        assertNotNull("El constructor vacío no debe devolver null", g);
    }

    /**
     * Verifica que el constructor completo asigna correctamente los valores
     * usando el método toString() como prueba indirecta.
     */
    @Test
    public void testGamaToString() {
        String resultado = gama.toString();
        // toString() devuelve: "id - nombre - tipo_mantenimiento"
        assertEquals("1 - Mantenimiento Preventivo Mensual - preventivo", resultado);
    }

    /**
     * Comprueba que el toString() de una gama contiene el nombre.
     */
    @Test
    public void testGamaToStringContieneNombre() {
        assertTrue(gama.toString().contains("Mantenimiento Preventivo Mensual"));
    }

    // --- herramientas --------------------------------------------------------

    /**
     * Verifica que el constructor vacío crea un objeto no nulo.
     */
    @Test
    public void testHerramientaConstructorVacio() {
        herramientas h = new herramientas();
        assertNotNull("El constructor vacío no debe devolver null", h);
    }

    /**
     * Verifica el toString() de herramientas: "id - nombre - estado".
     */
    @Test
    public void testHerramientaToString() {
        String resultado = herramienta.toString();
        assertEquals("10 - Llave inglesa - disponible", resultado);
    }

    /**
     * Comprueba el getter getId_herramienta().
     */
    @Test
    public void testHerramientaGetId() {
        assertEquals(10, herramienta.getId_herramienta());
    }

    /**
     * Comprueba el getter getNombre().
     */
    @Test
    public void testHerramientaGetNombre() {
        assertEquals("Llave inglesa", herramienta.getNombre());
    }

    /**
     * Comprueba el getter getEstado().
     */
    @Test
    public void testHerramientaGetEstado() {
        assertEquals("disponible", herramienta.getEstado());
    }

    // --- archivos_adjuntos ---------------------------------------------------

    /**
     * Verifica que el constructor vacío crea un objeto no nulo.
     */
    @Test
    public void testArchivoConstructorVacio() {
        archivos_adjuntos a = new archivos_adjuntos();
        assertNotNull("El constructor vacío no debe devolver null", a);
    }

    /**
     * Verifica el toString() de archivos_adjuntos: "id - nombre - tipo_referencia".
     */
    @Test
    public void testArchivoToString() {
        String resultado = archivo.toString();
        assertEquals("5 - informe_abril.pdf - orden", resultado);
    }

    /**
     * Comprueba que el toString() de un archivo no es nulo ni vacío.
     */
    @Test
    public void testArchivoToStringNoNulo() {
        assertNotNull(archivo.toString());
        assertFalse(archivo.toString().isEmpty());
    }

    // --- ejecuciones_mantenimiento -------------------------------------------

    /**
     * Verifica que el constructor vacío crea un objeto no nulo.
     */
    @Test
    public void testEjecucionConstructorVacio() {
        ejecuciones_mantenimiento e = new ejecuciones_mantenimiento();
        assertNotNull("El constructor vacío no debe devolver null", e);
    }

    /**
     * Verifica el toString() de ejecuciones_mantenimiento.
     * Formato: "id_ejecucion - orden: id_orden - resultado"
     */
    @Test
    public void testEjecucionToString() {
        String resultado = ejecucion.toString();
        assertEquals("100 - orden: 42 - completado", resultado);
    }

    /**
     * Comprueba que el toString() contiene el resultado esperado.
     */
    @Test
    public void testEjecucionToStringContieneResultado() {
        assertTrue(ejecucion.toString().contains("completado"));
    }

    // =========================================================================
    // SECCIÓN 2 – Pruebas de integración con la base de datos (DAO)
    //
    // Estos tests requieren conexión activa a la BD.
    // Si la BD no está disponible, marcarán error de conexión.
    // =========================================================================

    // --- gamas_manteniminetoDAO ----------------------------------------------

    /**
     * Verifica que Recibir() devuelve una lista no nula.
     * Si la BD tiene registros, la lista tendrá elementos; si está vacía,
     * devolverá una lista vacía (nunca null).
     */
    @Test
    public void testGamaDAORecibirNoNull() {
        ArrayList<Object> lista = gamaDAO.Recibir();
        assertNotNull("Recibir() no debe devolver null", lista);
    }

    /**
     * Verifica que todos los elementos devueltos por Recibir() son instancias
     * de gamas_mantenimiento.
     */
    @Test
    public void testGamaDAORecibirTiposCorrecto() {
        ArrayList<Object> lista = gamaDAO.Recibir();
        for (Object obj : lista) {
            assertTrue(
                "Cada elemento debe ser gamas_mantenimiento",
                obj instanceof gamas_mantenimiento
            );
        }
    }

    /**
     * Verifica que Mostrar() devuelve true (indica que la operación fue exitosa).
     */
    @Test
    public void testGamaDAOMostrarRetornaTrue() {
        boolean resultado = gamaDAO.Mostrar();
        assertTrue("Mostrar() debe devolver true", resultado);
    }

    // --- herramientasDAO -----------------------------------------------------

    /**
     * Verifica que Recibir() de herramientasDAO devuelve una lista no nula.
     */
    @Test
    public void testHerramientaDAORecibirNoNull() {
        ArrayList<Object> lista = herramientaDAO.Recibir();
        assertNotNull("Recibir() no debe devolver null", lista);
    }

    /**
     * Verifica que todos los elementos devueltos son instancias de herramientas.
     */
    @Test
    public void testHerramientaDAORecibirTiposCorrecto() {
        ArrayList<Object> lista = herramientaDAO.Recibir();
        for (Object obj : lista) {
            assertTrue(
                "Cada elemento debe ser herramientas",
                obj instanceof herramientas
            );
        }
    }

    /**
     * Verifica que Mostrar() de herramientasDAO devuelve true.
     */
    @Test
    public void testHerramientaDAOMostrarRetornaTrue() {
        boolean resultado = herramientaDAO.Mostrar();
        assertTrue("Mostrar() debe devolver true", resultado);
    }
}
