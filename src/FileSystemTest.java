import manager.FileSystem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class FileSystemTest {

    private FileSystem fs;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        fs = new FileSystem();
        // Redirigir System.out para capturar la salida de la consola (para ls, pwd)
        System.setOut(new PrintStream(outContent));
    }

    // Restaurar System.out después de cada test
    // @AfterEach // Si necesitas esto, descomenta y usa el import org.junit.jupiter.api.AfterEach
    // void restoreStreams() {
    //     System.setOut(originalOut);
    // }


    @Test
    @DisplayName("1. cd: Debe cambiar el directorio a un subdirectorio existente")
    void testCdToExistingSubdirectory() {
        fs.mkdir("testDir"); // [cite: 13]
        assertTrue(fs.cd("testDir")); // [cite: 5, 6]
        assertEquals("/testDir", fs.getDirectorioActual().getRutaCompleta()); // [cite: 16]
    }

    @Test
    @DisplayName("2. cd: Debe subir al directorio padre con '..'")
    void testCdToParentDirectory() {
        fs.mkdir("a"); // [cite: 13]
        fs.cd("a"); // [cite: 5, 6]
        assertTrue(fs.cd("..")); // [cite: 5, 7]
        assertEquals("/", fs.getDirectorioActual().getRutaCompleta()); // [cite: 16]
    }

    @Test
    @DisplayName("3. cd: Debe mantenerse en la raíz al intentar subir desde la raíz")
    void testCdFromRootToParent() {
        assertFalse(fs.cd("..")); // [cite: 5, 7] // No debería poder subir
        assertEquals("/", fs.getDirectorioActual().getRutaCompleta()); // [cite: 16]
    }

    @Test
    @DisplayName("4. cd: No debe cambiar a un directorio inexistente")
    void testCdToNonExistentDirectory() {
        assertFalse(fs.cd("nonExistent")); // [cite: 5, 6]
        assertEquals("/", fs.getDirectorioActual().getRutaCompleta()); // Sigue en la raíz [cite: 16]
        assertTrue(outContent.toString().contains("Directorio 'nonExistent' no encontrado."));
    }

    @Test
    @DisplayName("5. touch: Debe crear un archivo en el directorio actual")
    void testTouchFileInCurrentDirectory() {
        assertTrue(fs.touch("myFile.txt")); // [cite: 8, 10, 11]
        assertNotNull(fs.getDirectorioActual().getHijo("myFile.txt"));
        assertTrue(fs.getDirectorioActual().getHijo("myFile.txt") instanceof Archivo);
    }

    @Test
    @DisplayName("6. touch: No debe crear un archivo si ya existe")
    void testTouchExistingFile() {
        fs.touch("existing.txt"); // [cite: 8, 10, 11]
        assertFalse(fs.touch("existing.txt")); // [cite: 8, 10, 11]
        assertTrue(outContent.toString().contains("Ya existe un archivo o directorio con el nombre 'existing.txt' en este directorio."));
    }

    @Test
    @DisplayName("7. mkdir: Debe crear un nuevo directorio")
    void testMkdirNewDirectory() {
        assertTrue(fs.mkdir("newDir")); // [cite: 13, 15]
        assertNotNull(fs.getDirectorioActual().getHijo("newDir"));
        assertTrue(fs.getDirectorioActual().getHijo("newDir") instanceof Directorio);
    }

    @Test
    @DisplayName("8. mkdir: No debe crear un directorio si ya existe")
    void testMkdirExistingDirectory() {
        fs.mkdir("existingDir"); // [cite: 13, 15]
        assertFalse(fs.mkdir("existingDir")); // [cite: 13, 15]
        assertTrue(outContent.toString().contains("Ya existe un archivo o directorio con el nombre 'existingDir' en este directorio."));
    }

    @Test
    @DisplayName("9. ls: Debe listar los contenidos del directorio actual")
    void testLsContents() {
        fs.mkdir("dir1"); // [cite: 13]
        fs.touch("file1.txt"); // [cite: 8]
        fs.ls(); // [cite: 12]
        String output = outContent.toString();
        assertTrue(output.contains("dir1/\n"));
        assertTrue(output.contains("file1.txt\n"));
    }

    @Test
    @DisplayName("10. ls: Debe indicar cuando el directorio está vacío")
    void testLsEmptyDirectory() {
        fs.ls(); // [cite: 12]
        assertTrue(outContent.toString().contains("El directorio está vacío."));
    }

    @Test
    @DisplayName("11. pwd: Debe imprimir la ruta completa del directorio actual")
    void testPwd() {
        fs.mkdir("dir1"); // [cite: 13]
        fs.cd("dir1"); // [cite: 5, 6]
        fs.pwd(); // [cite: 14, 16]
        assertTrue(outContent.toString().contains("/dir1"));
    }

    @Test
    @DisplayName("12. rm: Debe eliminar un archivo")
    void testRmFile() {
        fs.touch("tempFile.txt"); // [cite: 8]
        assertTrue(fs.rm("tempFile.txt")); // [cite: 37, 38]
        assertNull(fs.getDirectorioActual().getHijo("tempFile.txt"));
    }

    @Test
    @DisplayName("13. rm: Debe eliminar un directorio vacío")
    void testRmEmptyDirectory() {
        fs.mkdir("emptyDir"); // [cite: 13]
        assertTrue(fs.rm("emptyDir")); // [cite: 37, 38]
        assertNull(fs.getDirectorioActual().getHijo("emptyDir"));
    }

    @Test
    @DisplayName("14. rm: No debe eliminar un directorio no vacío")
    void testRmNonEmptyDirectory() {
        fs.mkdir("nonEmptyDir"); // [cite: 13]
        fs.cd("nonEmptyDir"); // [cite: 5, 6]
        fs.touch("inside.txt"); // [cite: 8]
        fs.cd(".."); // [cite: 5, 7]
        assertFalse(fs.rm("nonEmptyDir")); // [cite: 37, 38]
        assertNotNull(fs.getDirectorioActual().getHijo("nonEmptyDir")); // El directorio sigue ahí
        assertTrue(outContent.toString().contains("El directorio 'nonEmptyDir' no está vacío y no puede ser eliminado."));
    }

    @Test
    @DisplayName("15. rm: No debe eliminar un elemento inexistente")
    void testRmNonExistentItem() {
        assertFalse(fs.rm("noSuchThing")); // [cite: 37, 38]
        assertTrue(outContent.toString().contains("'noSuchThing' no encontrado."));
    }

    // --- Tests para BONUS: Rutas Completas ---

    @Test
    @DisplayName("16. touch con ruta completa: Debe crear archivo en ruta absoluta")
    void testTouchWithAbsolutePath() {
        fs.mkdir("a"); // [cite: 13]
        fs.mkdir("a/b"); // [cite: 13]
        assertTrue(fs.touch("/a/b/file.txt")); // [cite: 8, 10, 11, 39, 41]
        fs.cd("a/b"); // [cite: 5, 6]
        assertNotNull(fs.getDirectorioActual().getHijo("file.txt"));
    }

    @Test
    @DisplayName("17. touch con ruta relativa: Debe crear archivo en ruta relativa")
    void testTouchWithRelativePath() {
        fs.mkdir("dir1"); // [cite: 13]
        fs.cd("dir1"); // [cite: 5, 6]
        fs.mkdir("dir2"); // [cite: 13]
        assertTrue(fs.touch("dir2/file.txt")); // [cite: 8, 10, 11, 39]
        fs.cd("dir2"); // [cite: 5, 6]
        assertNotNull(fs.getDirectorioActual().getHijo("file.txt"));
    }

    @Test
    @DisplayName("18. cd con ruta absoluta: Debe cambiar el directorio a una ruta absoluta")
    void testCdWithAbsolutePath() {
        fs.mkdir("level1"); // [cite: 13]
        fs.mkdir("level1/level2"); // [cite: 13]
        assertTrue(fs.cd("/level1/level2")); // [cite: 5, 6, 39]
        assertEquals("/level1/level2", fs.getDirectorioActual().getRutaCompleta()); // [cite: 16]
    }

    @Test
    @DisplayName("19. cd con ruta compleja: Debe manejar rutas con '.' y '..'")
    void testCdWithComplexPath() {
        fs.mkdir("parent"); // [cite: 13]
        fs.mkdir("parent/child1"); // [cite: 13]
        fs.mkdir("parent/child2"); // [cite: 13]
        fs.cd("parent/child1"); // [cite: 5, 6]
        assertTrue(fs.cd("../child2")); // [cite: 5, 7, 39]
        assertEquals("/parent/child2", fs.getDirectorioActual().getRutaCompleta()); // [cite: 16]
    }

    @Test
    @DisplayName("20. mkdir con ruta completa: Debe crear directorio en ruta absoluta")
    void testMkdirWithAbsolutePath() {
        fs.mkdir("a"); // [cite: 13]
        assertTrue(fs.mkdir("/a/newDir")); // [cite: 13, 15, 39, 44]
        fs.cd("a"); // [cite: 5, 6]
        assertNotNull(fs.getDirectorioActual().getHijo("newDir"));
    }

    @Test
    @DisplayName("21. rm con ruta completa: Debe eliminar archivo en ruta absoluta")
    void testRmFileWithAbsolutePath() {
        fs.mkdir("data"); // [cite: 13]
        fs.touch("data/report.txt"); // [cite: 8]
        assertTrue(fs.rm("/data/report.txt")); // [cite: 37, 38, 39]
        fs.cd("data"); // [cite: 5, 6]
        assertNull(fs.getDirectorioActual().getHijo("report.txt"));
    }

    @Test
    @DisplayName("22. rm con ruta compleja: Debe eliminar directorio vacío con ruta relativa compleja")
    void testRmEmptyDirWithComplexRelativePath() {
        fs.mkdir("proj"); // [cite: 13]
        fs.cd("proj"); // [cite: 5, 6]
        fs.mkdir("temp"); // [cite: 13]
        fs.mkdir("temp/cache"); // [cite: 13]
        assertTrue(fs.rm("temp/cache")); // [cite: 37, 38, 39]
        assertNull(fs.getDirectorioActual().getHijo("temp").getHijo("cache"));
    }
}