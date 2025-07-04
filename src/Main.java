import manager.FileSystem;

public class Main {
    public static void main(String[] args) {
        FileSystem fs = new FileSystem();

        System.out.println("--- PRUEBAS DE LA APLICACIÓN DE CONSOLA ---");
        System.out.println("Comando: pwd");
        fs.pwd();

        System.out.println("\nComando: mkdir archivos");
        fs.mkdir("archivos");
        System.out.println("Contenido del directorio actual:");
        fs.ls();

        System.out.println("\nComando: cd archivos");
        fs.cd("archivos");
        System.out.println("Comando: pwd");
        fs.pwd();

        System.out.println("\nComando: touch hola.txt");
        fs.touch("hola.txt");
        System.out.println("Contenido del directorio actual:");
        fs.ls();

        System.out.println("\nComando: mkdir documentos");
        fs.mkdir("documentos");
        System.out.println("Contenido del directorio actual:");
        fs.ls();

        System.out.println("\nComando: cd documentos");
        fs.cd("documentos");
        System.out.println("Comando: pwd");
        fs.pwd();

        System.out.println("\nComando: cd ../..");
        fs.cd("../..");
        System.out.println("Comando: pwd");
        fs.pwd();

        System.out.println("\n--- PRUEBAS BONUS: RUTAS COMPLETAS ---");

        System.out.println("\nComando: touch /archivos/documentos/nuevo_archivo.txt");
        fs.touch("/archivos/documentos/nuevo_archivo.txt");
        System.out.println("Comando: cd archivos/documentos");
        fs.cd("archivos/documentos");
        System.out.println("Contenido del directorio actual:");
        fs.ls();

        System.out.println("\nComando: cd /");
        fs.cd("/");
        System.out.println("Comando: pwd");
        fs.pwd();

        System.out.println("\nComando: mkdir /archivos/documentos/2025");
        fs.mkdir("/archivos/documentos/2025");
        System.out.println("Comando: cd /archivos/documentos");
        fs.cd("/archivos/documentos");
        System.out.println("Contenido del directorio actual:");
        fs.ls();

        System.out.println("\nComando: rm /archivos/documentos/nuevo_archivo.txt");
        fs.rm("/archivos/documentos/nuevo_archivo.txt");
        System.out.println("Contenido del directorio actual:");
        fs.ls();

        System.out.println("\n--- PRUEBAS DE ERRORES / CASOS ESPECIALES ---");
        System.out.println("\nComando: Intentar eliminar un directorio no vacío (rm /archivos)");
        fs.cd("/");
        fs.rm("/archivos");
        System.out.println("Contenido del directorio actual (debería seguir mostrando 'archivos/'):");
        fs.ls();

        System.out.println("\nComando: Eliminar directorio vacío (rm 2025)");
        fs.cd("/archivos/documentos");
        fs.rm("2025");
        System.out.println("Contenido del directorio actual:");
        fs.ls();

        System.out.println("\nComando: cd .. (desde /archivos/documentos)");
        fs.cd("..");
        System.out.println("Contenido del directorio actual:");
        fs.ls();

        System.out.println("\nComando: touch existente.txt");
        fs.touch("existente.txt");
        System.out.println("Comando: touch existente.txt (intentar crear uno ya existente)");
        fs.touch("existente.txt");
        System.out.println("Contenido del directorio actual:");
        fs.ls();
    }
}