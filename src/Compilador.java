import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Compilador {

    private AnalizadorLexico analizadorLexico;
    private Parser parser;
    private ConversorTercetoAssembler conversor;

    public Compilador() {
    }

    public void compilar(String archivo)
            throws FileNotFoundException {

        analizadorLexico = new AnalizadorLexico();

        parser = new Parser();

        analizadorLexico.leerNuevoArchivo(archivo);

        parser.setAnalizadorLexico(
                analizadorLexico
        );

        parser.yyparse();

        conversor =
                new ConversorTercetoAssembler(
                        parser.getTercetos(),
                        analizadorLexico.getTablaSimbolo()
                );
    }


    // ========================================
    // TOKENS
    // ========================================

    public String obtenerTokens() {

        return capturarSalida(() -> {
            analizadorLexico.imprimirTokens();
        });
    }


    // ========================================
    // TABLA DE SÍMBOLOS
    // ========================================

    public String obtenerTablaSimbolos() {

        return capturarSalida(() -> {

            analizadorLexico
                    .getTablaSimbolo()
                    .imprimirTabla();

        });
    }


    // ========================================
    // ERRORES
    // ========================================

    public String obtenerErrores() {

        return capturarSalida(() -> {

            analizadorLexico
                    .imprimirWarningsLexicos();

            parser
                    .imprimirWarningsSemanticos();

            analizadorLexico
                    .imprimirErroresLexicos();

            parser
                    .imprimirErroresSintacticos();

            parser
                    .imprimirErroresSemanticos();

        });
    }


    // ========================================
    // TERCETOS
    // ========================================

    public String obtenerTercetos() {

        if (hayErrores()) {

            return """
                    No se pueden mostrar los tercetos.

                    Existen errores en el código fuente.
                    Revise la pestaña ERRORES.
                    """;
        }

        return capturarSalida(() -> {
            parser.imprimirTercetos();
        });
    }


    // ========================================
    // ASSEMBLER
    // ========================================

    public String obtenerAssembler() {

        if (hayErrores()) {

            return """
                    No se puede generar código Assembler.

                    Existen errores en el código fuente.
                    Revise la pestaña ERRORES.
                    """;
        }

        if (conversor == null) {

            return "No se ha realizado una compilación.";

        }

        return conversor.getConversionAssembler();
    }


    // ========================================
    // COMPROBAR ERRORES
    // ========================================

    public boolean hayErrores() {

        if (analizadorLexico == null ||
                parser == null) {

            return true;
        }

        return analizadorLexico.hayError()
                || parser.hayError();
    }


    // ========================================
    // CAPTURAR SYSTEM.OUT
    // ========================================

    private String capturarSalida(Runnable accion) {

        PrintStream salidaOriginal =
                System.out;

        ByteArrayOutputStream buffer =
                new ByteArrayOutputStream();

        PrintStream salidaTemporal =
                new PrintStream(buffer);

        try {

            System.setOut(
                    salidaTemporal
            );

            accion.run();

        } finally {

            System.setOut(
                    salidaOriginal
            );

            salidaTemporal.close();
        }

        return buffer.toString();
    }


    // ========================================
    // GETTERS
    // ========================================

    public AnalizadorLexico getAnalizadorLexico() {

        return analizadorLexico;
    }


    public Parser getParser() {

        return parser;
    }


    public ConversorTercetoAssembler getConversor() {

        return conversor;
    }
}