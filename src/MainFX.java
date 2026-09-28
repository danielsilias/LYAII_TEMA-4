import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainFX extends Application {

    // =========================================================
    // COMPONENTES PRINCIPALES
    // =========================================================

    private TextArea editorCodigo;

    private TextArea areaTokens;
    private TextArea areaSimbolos;
    private TextArea areaErrores;
    private TextArea areaTercetos;
    private TextArea areaAssembler;

    private TabPane pestanas;

    private Tab tabTokens;
    private Tab tabSimbolos;
    private Tab tabErrores;
    private Tab tabTercetos;
    private Tab tabAssembler;

    private Label estado;
    private Label nombreArchivo;

    // Indicadores de fases
    private Label indicadorLexico;
    private Label indicadorSintactico;
    private Label indicadorSemantico;
    private Label indicadorTercetos;
    private Label indicadorAssembler;

    // Resumen
    private Label resumenTokens;
    private Label resumenErrores;

    private Button btnExportarAssembler;

    private File archivoActual;
    private Compilador compilador;

    private Stage ventanaPrincipal;


    // =========================================================
    // INICIO
    // =========================================================

    @Override
    public void start(Stage stage) {

        ventanaPrincipal = stage;

        // =====================================================
        // TÍTULO
        // =====================================================

        Label titulo = new Label("COMPILADOR");
        titulo.getStyleClass().add("titulo");

        Label subtitulo = new Label(
                "Analizador Léxico, Sintáctico y Semántico"
        );

        subtitulo.getStyleClass().add("subtitulo");


        // =====================================================
        // NOMBRE DEL ARCHIVO
        // =====================================================

        nombreArchivo = new Label("Sin archivo");
        nombreArchivo.getStyleClass().add("archivo-label");


        // =====================================================
        // BOTONES DE ARCHIVO
        // =====================================================

        Button btnNuevo = new Button("＋ Nuevo");
        Button btnAbrir = new Button("📂 Abrir");
        Button btnGuardar = new Button("💾 Guardar");
        Button btnGuardarComo = new Button("Guardar como");
        Button btnLimpiar = new Button("Limpiar");


        // =====================================================
        // BOTONES DE FASES
        // =====================================================

        Button btnLexico = new Button("1  LÉXICO");
        Button btnSintactico = new Button("2  SINTÁCTICO");
        Button btnSemantico = new Button("3  SEMÁNTICO");

        Button btnCompilar = new Button("▶ COMPILAR TODO");

        btnExportarAssembler =
                new Button("💾 EXPORTAR .ASM");


        // =====================================================
        // ESTILOS DE BOTONES
        // =====================================================

        btnLexico.getStyleClass().add("btn-fase");
        btnSintactico.getStyleClass().add("btn-fase");
        btnSemantico.getStyleClass().add("btn-fase");

        btnCompilar.getStyleClass().add("btn-compilar");

        btnExportarAssembler
                .getStyleClass()
                .add("btn-fase");


        // =====================================================
        // TAMAÑOS
        // =====================================================

        btnNuevo.setPrefHeight(36);
        btnAbrir.setPrefHeight(36);
        btnGuardar.setPrefHeight(36);
        btnGuardarComo.setPrefHeight(36);
        btnLimpiar.setPrefHeight(36);

        btnLexico.setPrefHeight(36);
        btnSintactico.setPrefHeight(36);
        btnSemantico.setPrefHeight(36);

        btnCompilar.setPrefHeight(36);
        btnExportarAssembler.setPrefHeight(36);

        btnLexico.setPrefWidth(115);
        btnSintactico.setPrefWidth(135);
        btnSemantico.setPrefWidth(130);

        btnCompilar.setPrefWidth(170);
        btnExportarAssembler.setPrefWidth(165);


        // Al iniciar no hay Assembler para exportar
        btnExportarAssembler.setDisable(true);


        // =====================================================
        // EVENTOS
        // =====================================================

        btnNuevo.setOnAction(e ->
                nuevoArchivo()
        );

        btnAbrir.setOnAction(e ->
                abrirArchivo()
        );

        btnGuardar.setOnAction(e ->
                guardarArchivo()
        );

        btnGuardarComo.setOnAction(e ->
                guardarComo()
        );

        btnLimpiar.setOnAction(e -> {

            limpiarResultados();

            cambiarEstado(
                    "● Resultados limpiados",
                    ""
            );
        });

        btnLexico.setOnAction(e ->
                ejecutarFaseLexica()
        );

        btnSintactico.setOnAction(e ->
                ejecutarFaseSintactica()
        );

        btnSemantico.setOnAction(e ->
                ejecutarFaseSemantica()
        );

        btnCompilar.setOnAction(e ->
                compilarCodigo()
        );

        btnExportarAssembler.setOnAction(e ->
                exportarAssembler()
        );


        // =====================================================
        // BARRA DE ARCHIVOS
        // =====================================================

        HBox barraArchivos = new HBox(
                10,
                btnNuevo,
                btnAbrir,
                btnGuardar,
                btnGuardarComo,
                btnLimpiar
        );

        barraArchivos.setAlignment(
                Pos.CENTER_LEFT
        );


        // =====================================================
        // BARRA DE COMPILACIÓN
        // =====================================================

        HBox barraCompilacion = new HBox(
                8,
                btnLexico,
                btnSintactico,
                btnSemantico,
                btnCompilar,
                btnExportarAssembler
        );

        barraCompilacion.setAlignment(
                Pos.CENTER_RIGHT
        );


        Region espacio = new Region();

        HBox.setHgrow(
                espacio,
                Priority.ALWAYS
        );


        HBox barraHerramientas = new HBox(
                10,
                barraArchivos,
                espacio,
                barraCompilacion
        );

        barraHerramientas.setAlignment(
                Pos.CENTER_LEFT
        );

        barraHerramientas.setPadding(
                new Insets(12, 0, 0, 0)
        );


        // =====================================================
        // INDICADORES
        // =====================================================

        indicadorLexico =
                crearIndicador("○ LÉXICO");

        indicadorSintactico =
                crearIndicador("○ SINTÁCTICO");

        indicadorSemantico =
                crearIndicador("○ SEMÁNTICO");

        indicadorTercetos =
                crearIndicador("○ TERCETOS");

        indicadorAssembler =
                crearIndicador("○ ASSEMBLER");


        HBox barraIndicadores = new HBox(
                12,
                indicadorLexico,
                indicadorSintactico,
                indicadorSemantico,
                indicadorTercetos,
                indicadorAssembler
        );

        barraIndicadores.setAlignment(
                Pos.CENTER_LEFT
        );

        barraIndicadores.setPadding(
                new Insets(10, 0, 0, 0)
        );


        // =====================================================
        // RESUMEN
        // =====================================================

        resumenTokens =
                new Label("Tokens: --");

        resumenErrores =
                new Label("Estado: Sin analizar");

        resumenTokens.setStyle(
                "-fx-text-fill: #94a3b8;" +
                        "-fx-font-weight: bold;"
        );

        resumenErrores.setStyle(
                "-fx-text-fill: #94a3b8;" +
                        "-fx-font-weight: bold;"
        );


        Region espacioResumen =
                new Region();

        HBox.setHgrow(
                espacioResumen,
                Priority.ALWAYS
        );


        HBox barraResumen = new HBox(
                10,
                resumenTokens,
                espacioResumen,
                resumenErrores
        );

        barraResumen.setAlignment(
                Pos.CENTER_LEFT
        );

        barraResumen.setPadding(
                new Insets(8, 4, 0, 4)
        );


        // =====================================================
        // CABECERA
        // =====================================================

        VBox cabecera = new VBox(
                4,
                titulo,
                subtitulo,
                barraHerramientas,
                barraIndicadores,
                barraResumen
        );

        cabecera.getStyleClass().add(
                "header"
        );


        // =====================================================
        // EDITOR
        // =====================================================

        Label tituloEditor =
                new Label("CÓDIGO FUENTE");

        tituloEditor
                .getStyleClass()
                .add("panel-titulo");


        editorCodigo = new TextArea();

        editorCodigo.setPromptText(
                "Escriba aquí el código fuente...\n\n" +
                        "También puede abrir un archivo .txt."
        );

        editorCodigo.setWrapText(false);

        editorCodigo
                .getStyleClass()
                .add("editor");


        VBox panelEditor = new VBox(
                7,
                tituloEditor,
                nombreArchivo,
                editorCodigo
        );

        panelEditor
                .getStyleClass()
                .add("panel");

        VBox.setVgrow(
                editorCodigo,
                Priority.ALWAYS
        );


        // =====================================================
        // RESULTADOS
        // =====================================================

        Label tituloResultados =
                new Label("RESULTADOS");

        tituloResultados
                .getStyleClass()
                .add("panel-titulo");


        areaTokens = crearAreaResultado(
                "Los tokens aparecerán aquí."
        );

        areaSimbolos = crearAreaResultado(
                "La tabla de símbolos aparecerá aquí."
        );

        areaErrores = crearAreaResultado(
                "Los errores aparecerán aquí."
        );

        areaTercetos = crearAreaResultado(
                "Los tercetos aparecerán aquí."
        );

        areaAssembler = crearAreaResultado(
                "El código Assembler aparecerá aquí."
        );


        // =====================================================
        // PESTAÑAS
        // =====================================================

        tabTokens = crearTab(
                "Tokens",
                areaTokens
        );

        tabSimbolos = crearTab(
                "Símbolos",
                areaSimbolos
        );

        tabErrores = crearTab(
                "Errores",
                areaErrores
        );

        tabTercetos = crearTab(
                "Tercetos",
                areaTercetos
        );

        tabAssembler = crearTab(
                "Assembler",
                areaAssembler
        );


        pestanas = new TabPane();

        pestanas.getTabs().addAll(
                tabTokens,
                tabSimbolos,
                tabErrores,
                tabTercetos,
                tabAssembler
        );


        VBox panelResultados = new VBox(
                8,
                tituloResultados,
                pestanas
        );

        panelResultados
                .getStyleClass()
                .add("panel");

        VBox.setVgrow(
                pestanas,
                Priority.ALWAYS
        );


        // =====================================================
        // DIVISIÓN CENTRAL
        // =====================================================

        SplitPane splitPane = new SplitPane(
                panelEditor,
                panelResultados
        );

        splitPane.setOrientation(
                Orientation.HORIZONTAL
        );

        splitPane.setDividerPositions(
                0.50
        );


        // =====================================================
        // ESTADO
        // =====================================================

        estado = new Label("● Listo");

        estado.getStyleClass().add(
                "estado"
        );

        estado.setMaxWidth(
                Double.MAX_VALUE
        );


        // =====================================================
        // LAYOUT
        // =====================================================

        BorderPane root =
                new BorderPane();

        root.setTop(cabecera);
        root.setCenter(splitPane);
        root.setBottom(estado);


        // =====================================================
        // ESCENA
        // =====================================================

        Scene scene = new Scene(
                root,
                1450,
                850
        );


        // =====================================================
        // CSS
        // =====================================================

        var css =
                getClass()
                        .getResource(
                                "/compilador.css"
                        );

        if (css != null) {

            scene.getStylesheets().add(
                    css.toExternalForm()
            );

        } else {

            System.err.println(
                    "ADVERTENCIA: No se encontró compilador.css"
            );
        }


        // =====================================================
        // VENTANA
        // =====================================================

        stage.setTitle(
                "Compilador - JavaFX"
        );

        stage.setScene(scene);

        stage.setMinWidth(1100);
        stage.setMinHeight(700);

        stage.show();
    }


    // =========================================================
    // CREAR INDICADOR
    // =========================================================

    private Label crearIndicador(
            String texto
    ) {

        Label label =
                new Label(texto);

        label.setStyle(
                "-fx-background-color: #18243a;" +
                        "-fx-text-fill: #94a3b8;" +
                        "-fx-padding: 7 14 7 14;" +
                        "-fx-background-radius: 7;" +
                        "-fx-border-radius: 7;" +
                        "-fx-border-color: #30415f;" +
                        "-fx-font-size: 11px;" +
                        "-fx-font-weight: bold;"
        );

        return label;
    }


    // =========================================================
    // INDICADOR CORRECTO
    // =========================================================

    private void indicadorCorrecto(
            Label indicador,
            String texto
    ) {

        indicador.setText(
                "✓ " + texto
        );

        indicador.setStyle(
                "-fx-background-color: #123524;" +
                        "-fx-text-fill: #4ade80;" +
                        "-fx-padding: 7 14 7 14;" +
                        "-fx-background-radius: 7;" +
                        "-fx-border-radius: 7;" +
                        "-fx-border-color: #22c55e;" +
                        "-fx-font-size: 11px;" +
                        "-fx-font-weight: bold;"
        );
    }


    // =========================================================
    // INDICADOR ERROR
    // =========================================================

    private void indicadorError(
            Label indicador,
            String texto
    ) {

        indicador.setText(
                "✕ " + texto
        );

        indicador.setStyle(
                "-fx-background-color: #3b1518;" +
                        "-fx-text-fill: #f87171;" +
                        "-fx-padding: 7 14 7 14;" +
                        "-fx-background-radius: 7;" +
                        "-fx-border-radius: 7;" +
                        "-fx-border-color: #ef4444;" +
                        "-fx-font-size: 11px;" +
                        "-fx-font-weight: bold;"
        );
    }


    // =========================================================
    // REINICIAR INDICADOR
    // =========================================================

    private void reiniciarIndicador(
            Label indicador,
            String texto
    ) {

        indicador.setText(
                "○ " + texto
        );

        indicador.setStyle(
                "-fx-background-color: #18243a;" +
                        "-fx-text-fill: #94a3b8;" +
                        "-fx-padding: 7 14 7 14;" +
                        "-fx-background-radius: 7;" +
                        "-fx-border-radius: 7;" +
                        "-fx-border-color: #30415f;" +
                        "-fx-font-size: 11px;" +
                        "-fx-font-weight: bold;"
        );
    }


    // =========================================================
    // CREAR PESTAÑA
    // =========================================================

    private Tab crearTab(
            String titulo,
            TextArea contenido
    ) {

        Tab tab = new Tab(
                titulo,
                contenido
        );

        tab.setClosable(false);

        return tab;
    }


    // =========================================================
    // CREAR ÁREA DE RESULTADO
    // =========================================================

    private TextArea crearAreaResultado(
            String mensaje
    ) {

        TextArea area =
                new TextArea();

        area.setEditable(false);

        area.setWrapText(false);

        area.setText(mensaje);

        area.getStyleClass().add(
                "resultado"
        );

        return area;
    }


    // =========================================================
    // NUEVO
    // =========================================================

    private void nuevoArchivo() {

        editorCodigo.clear();

        archivoActual = null;

        nombreArchivo.setText(
                "Nuevo archivo"
        );

        limpiarResultados();

        cambiarEstado(
                "● Nuevo archivo",
                ""
        );

        editorCodigo.requestFocus();
    }


    // =========================================================
    // ABRIR
    // =========================================================

    private void abrirArchivo() {

        FileChooser selector =
                crearSelectorArchivos();

        selector.setTitle(
                "Abrir código fuente"
        );

        File archivo =
                selector.showOpenDialog(
                        ventanaPrincipal
                );

        if (archivo == null) {
            return;
        }

        try {

            String contenido =
                    Files.readString(
                            archivo.toPath(),
                            StandardCharsets.UTF_8
                    );

            editorCodigo.setText(
                    contenido
            );

            archivoActual =
                    archivo;

            nombreArchivo.setText(
                    archivo.getName()
            );

            limpiarResultados();

            cambiarEstado(
                    "● Archivo abierto: "
                            + archivo.getName(),
                    ""
            );

        } catch (IOException e) {

            mostrarError(
                    "Error al abrir archivo",
                    e.getMessage()
            );
        }
    }


    // =========================================================
    // GUARDAR
    // =========================================================

    private void guardarArchivo() {

        if (archivoActual == null) {

            guardarComo();
            return;
        }

        try {

            Files.writeString(
                    archivoActual.toPath(),
                    editorCodigo.getText(),
                    StandardCharsets.UTF_8
            );

            nombreArchivo.setText(
                    archivoActual.getName()
            );

            cambiarEstado(
                    "✓ Archivo guardado: "
                            + archivoActual.getName(),
                    "correcto"
            );

        } catch (IOException e) {

            mostrarError(
                    "Error al guardar",
                    e.getMessage()
            );
        }
    }


    // =========================================================
    // GUARDAR COMO
    // =========================================================

    private void guardarComo() {

        FileChooser selector =
                crearSelectorArchivos();

        selector.setTitle(
                "Guardar código fuente"
        );

        File archivo =
                selector.showSaveDialog(
                        ventanaPrincipal
                );

        if (archivo == null) {
            return;
        }

        if (!archivo
                .getName()
                .toLowerCase()
                .endsWith(".txt")) {

            archivo = new File(
                    archivo.getAbsolutePath()
                            + ".txt"
            );
        }

        try {

            Files.writeString(
                    archivo.toPath(),
                    editorCodigo.getText(),
                    StandardCharsets.UTF_8
            );

            archivoActual =
                    archivo;

            nombreArchivo.setText(
                    archivo.getName()
            );

            cambiarEstado(
                    "✓ Archivo guardado: "
                            + archivo.getName(),
                    "correcto"
            );

        } catch (IOException e) {

            mostrarError(
                    "Error al guardar archivo",
                    e.getMessage()
            );
        }
    }


    // =========================================================
    // FASE LÉXICA
    // =========================================================

    private void ejecutarFaseLexica() {

        if (!prepararCompilacion()) {
            return;
        }

        pestanas
                .getSelectionModel()
                .select(tabTokens);

        indicadorCorrecto(
                indicadorLexico,
                "LÉXICO"
        );

        actualizarContadores();

        cambiarEstado(
                "✓ Análisis léxico completado",
                "correcto"
        );
    }


    // =========================================================
    // FASE SINTÁCTICA
    // =========================================================

    private void ejecutarFaseSintactica() {

        if (!prepararCompilacion()) {
            return;
        }

        pestanas
                .getSelectionModel()
                .select(tabErrores);

        indicadorCorrecto(
                indicadorLexico,
                "LÉXICO"
        );

        if (compilador.hayErrores()) {

            indicadorError(
                    indicadorSintactico,
                    "SINTÁCTICO"
            );

            cambiarEstado(
                    "✕ El análisis encontró errores",
                    "error"
            );

        } else {

            indicadorCorrecto(
                    indicadorSintactico,
                    "SINTÁCTICO"
            );

            cambiarEstado(
                    "✓ Análisis sintáctico completado",
                    "correcto"
            );
        }

        actualizarContadores();
    }


    // =========================================================
    // FASE SEMÁNTICA
    // =========================================================

    private void ejecutarFaseSemantica() {

        if (!prepararCompilacion()) {
            return;
        }

        pestanas
                .getSelectionModel()
                .select(tabSimbolos);

        indicadorCorrecto(
                indicadorLexico,
                "LÉXICO"
        );

        if (compilador.hayErrores()) {

            indicadorError(
                    indicadorSemantico,
                    "SEMÁNTICO"
            );

            cambiarEstado(
                    "✕ El análisis encontró errores",
                    "error"
            );

        } else {

            indicadorCorrecto(
                    indicadorSintactico,
                    "SINTÁCTICO"
            );

            indicadorCorrecto(
                    indicadorSemantico,
                    "SEMÁNTICO"
            );

            cambiarEstado(
                    "✓ Análisis semántico completado",
                    "correcto"
            );
        }

        actualizarContadores();
    }


    // =========================================================
    // PREPARAR ANÁLISIS
    // =========================================================

    private boolean prepararCompilacion() {

        String codigo =
                editorCodigo.getText();

        if (codigo == null ||
                codigo.isBlank()) {

            mostrarError(
                    "Código vacío",
                    "Escriba o abra un código fuente antes de analizar."
            );

            return false;
        }

        File archivoTemporal = null;

        try {

            cambiarEstado(
                    "● Analizando código...",
                    ""
            );

            archivoTemporal =
                    File.createTempFile(
                            "codigo_compilador_",
                            ".txt"
                    );

            Files.writeString(
                    archivoTemporal.toPath(),
                    codigo,
                    StandardCharsets.UTF_8
            );

            compilador =
                    new Compilador();

            compilador.compilar(
                    archivoTemporal
                            .getAbsolutePath()
            );


            String tokens =
                    compilador.obtenerTokens();

            areaTokens.setText(
                    textoResultado(
                            tokens,
                            "No se generaron tokens."
                    )
            );


            String simbolos =
                    compilador.obtenerTablaSimbolos();

            areaSimbolos.setText(
                    textoResultado(
                            simbolos,
                            "La tabla de símbolos está vacía."
                    )
            );


            String errores =
                    compilador.obtenerErrores();

            areaErrores.setText(
                    textoResultado(
                            errores,
                            "✓ No se encontraron errores."
                    )
            );


            String tercetos =
                    compilador.obtenerTercetos();

            areaTercetos.setText(
                    textoResultado(
                            tercetos,
                            "No se generaron tercetos."
                    )
            );


            String assembler =
                    compilador.obtenerAssembler();

            areaAssembler.setText(
                    textoResultado(
                            assembler,
                            "No se generó código Assembler."
                    )
            );


            actualizarContadores();

            btnExportarAssembler.setDisable(
                    compilador.hayErrores()
            );

            return true;

        } catch (Exception e) {

            cambiarEstado(
                    "✕ Error durante el análisis",
                    "error"
            );

            String mensaje =
                    e.getMessage();

            if (mensaje == null ||
                    mensaje.isBlank()) {

                mensaje =
                        e.toString();
            }

            mostrarError(
                    "Error durante el análisis",
                    mensaje
            );

            e.printStackTrace();

            return false;

        } finally {

            if (archivoTemporal != null) {

                try {

                    Files.deleteIfExists(
                            archivoTemporal.toPath()
                    );

                } catch (IOException ignored) {

                }
            }
        }
    }


    // =========================================================
    // COMPILAR TODO
    // =========================================================

    private void compilarCodigo() {

        cambiarEstado(
                "● Compilando todo...",
                ""
        );

        if (!prepararCompilacion()) {
            return;
        }

        indicadorCorrecto(
                indicadorLexico,
                "LÉXICO"
        );


        if (compilador.hayErrores()) {

            indicadorError(
                    indicadorSintactico,
                    "SINTÁCTICO"
            );

            indicadorError(
                    indicadorSemantico,
                    "SEMÁNTICO"
            );

            indicadorError(
                    indicadorTercetos,
                    "TERCETOS"
            );

            indicadorError(
                    indicadorAssembler,
                    "ASSEMBLER"
            );

            btnExportarAssembler.setDisable(
                    true
            );

            resumenErrores.setText(
                    "Estado: Se encontraron errores"
            );

            resumenErrores.setStyle(
                    "-fx-text-fill: #f87171;" +
                            "-fx-font-weight: bold;"
            );

            pestanas
                    .getSelectionModel()
                    .select(tabErrores);

            cambiarEstado(
                    "✕ Compilación terminada con errores",
                    "error"
            );

        } else {

            indicadorCorrecto(
                    indicadorSintactico,
                    "SINTÁCTICO"
            );

            indicadorCorrecto(
                    indicadorSemantico,
                    "SEMÁNTICO"
            );

            indicadorCorrecto(
                    indicadorTercetos,
                    "TERCETOS"
            );

            indicadorCorrecto(
                    indicadorAssembler,
                    "ASSEMBLER"
            );

            btnExportarAssembler.setDisable(
                    false
            );

            resumenErrores.setText(
                    "Estado: Sin errores"
            );

            resumenErrores.setStyle(
                    "-fx-text-fill: #4ade80;" +
                            "-fx-font-weight: bold;"
            );

            cambiarEstado(
                    "✓ Compilación correcta",
                    "correcto"
            );
        }

        actualizarContadores();
    }


    // =========================================================
    // CONTADOR DE TOKENS
    // =========================================================

    private void actualizarContadores() {

        if (areaTokens == null) {
            return;
        }

        String texto =
                areaTokens.getText();

        int cantidad =
                extraerCantidadTokens(texto);

        if (cantidad >= 0) {

            resumenTokens.setText(
                    "Tokens: " + cantidad
            );

        } else {

            resumenTokens.setText(
                    "Tokens: --"
            );
        }


        if (compilador != null) {

            if (compilador.hayErrores()) {

                resumenErrores.setText(
                        "Estado: Con errores"
                );

                resumenErrores.setStyle(
                        "-fx-text-fill: #f87171;" +
                                "-fx-font-weight: bold;"
                );

            } else {

                resumenErrores.setText(
                        "Estado: Sin errores"
                );

                resumenErrores.setStyle(
                        "-fx-text-fill: #4ade80;" +
                                "-fx-font-weight: bold;"
                );
            }
        }
    }


    // =========================================================
    // EXTRAER CANTIDAD DE TOKENS
    // =========================================================

    private int extraerCantidadTokens(
            String texto
    ) {

        if (texto == null) {
            return -1;
        }

        Pattern patron =
                Pattern.compile(
                        "(?i)se\\s+detectaron\\s+(\\d+)\\s+tokens"
                );

        Matcher matcher =
                patron.matcher(texto);

        if (matcher.find()) {

            try {

                return Integer.parseInt(
                        matcher.group(1)
                );

            } catch (NumberFormatException ignored) {

            }
        }

        return -1;
    }


    // =========================================================
    // EXPORTAR ASSEMBLER
    // =========================================================

    private void exportarAssembler() {

        if (compilador == null) {

            mostrarError(
                    "Sin compilación",
                    "Primero debe compilar el código."
            );

            return;
        }


        if (compilador.hayErrores()) {

            mostrarError(
                    "No se puede exportar",
                    "El código contiene errores. Corríjalos antes de exportar el Assembler."
            );

            return;
        }


        String assembler =
                compilador.obtenerAssembler();


        if (assembler == null ||
                assembler.isBlank()) {

            mostrarError(
                    "Assembler vacío",
                    "No existe código Assembler para exportar."
            );

            return;
        }


        FileChooser selector =
                new FileChooser();

        selector.setTitle(
                "Exportar código Assembler"
        );


        selector.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Archivo Assembler (*.asm)",
                        "*.asm"
                )
        );


        selector.setInitialFileName(
                "Assembler.asm"
        );


        File archivo =
                selector.showSaveDialog(
                        ventanaPrincipal
                );


        if (archivo == null) {
            return;
        }


        if (!archivo
                .getName()
                .toLowerCase()
                .endsWith(".asm")) {

            archivo = new File(
                    archivo.getAbsolutePath()
                            + ".asm"
            );
        }


        try {

            Files.writeString(
                    archivo.toPath(),
                    assembler,
                    StandardCharsets.UTF_8
            );


            cambiarEstado(
                    "✓ Assembler exportado: "
                            + archivo.getName(),
                    "correcto"
            );


            Alert alerta =
                    new Alert(
                            Alert.AlertType.INFORMATION
                    );

            alerta.setTitle(
                    "Compilador"
            );

            alerta.setHeaderText(
                    "Assembler exportado correctamente"
            );

            alerta.setContentText(
                    "El archivo fue guardado en:\n\n"
                            + archivo.getAbsolutePath()
            );

            alerta.showAndWait();


        } catch (IOException e) {

            mostrarError(
                    "Error al exportar Assembler",
                    e.getMessage()
            );
        }
    }


    // =========================================================
    // CAMBIAR ESTADO
    // =========================================================

    private void cambiarEstado(
            String mensaje,
            String tipo
    ) {

        estado.setText(
                mensaje
        );

        estado.getStyleClass().removeAll(
                "estado-correcto",
                "estado-error"
        );

        if ("correcto".equals(tipo)) {

            estado
                    .getStyleClass()
                    .add(
                            "estado-correcto"
                    );

        } else if ("error".equals(tipo)) {

            estado
                    .getStyleClass()
                    .add(
                            "estado-error"
                    );
        }
    }


    // =========================================================
    // SELECTOR DE ARCHIVOS
    // =========================================================

    private FileChooser crearSelectorArchivos() {

        FileChooser selector =
                new FileChooser();


        selector
                .getExtensionFilters()
                .add(

                        new FileChooser.ExtensionFilter(
                                "Código fuente (*.txt)",
                                "*.txt"
                        )
                );


        File carpeta =
                new File(
                        "casos de prueba"
                );


        if (carpeta.exists() &&
                carpeta.isDirectory()) {

            selector.setInitialDirectory(
                    carpeta
            );
        }


        return selector;
    }


    // =========================================================
    // TEXTO RESULTADO
    // =========================================================

    private String textoResultado(
            String texto,
            String mensajeVacio
    ) {

        if (texto == null ||
                texto.isBlank()) {

            return mensajeVacio;
        }

        return texto;
    }


    // =========================================================
    // LIMPIAR RESULTADOS
    // =========================================================

    private void limpiarResultados() {

        areaTokens.setText(
                "Los tokens aparecerán aquí."
        );

        areaSimbolos.setText(
                "La tabla de símbolos aparecerá aquí."
        );

        areaErrores.setText(
                "Los errores aparecerán aquí."
        );

        areaTercetos.setText(
                "Los tercetos aparecerán aquí."
        );

        areaAssembler.setText(
                "El código Assembler aparecerá aquí."
        );


        reiniciarIndicador(
                indicadorLexico,
                "LÉXICO"
        );

        reiniciarIndicador(
                indicadorSintactico,
                "SINTÁCTICO"
        );

        reiniciarIndicador(
                indicadorSemantico,
                "SEMÁNTICO"
        );

        reiniciarIndicador(
                indicadorTercetos,
                "TERCETOS"
        );

        reiniciarIndicador(
                indicadorAssembler,
                "ASSEMBLER"
        );


        resumenTokens.setText(
                "Tokens: --"
        );

        resumenErrores.setText(
                "Estado: Sin analizar"
        );

        resumenErrores.setStyle(
                "-fx-text-fill: #94a3b8;" +
                        "-fx-font-weight: bold;"
        );


        btnExportarAssembler.setDisable(
                true
        );


        compilador = null;
    }


    // =========================================================
    // MOSTRAR ERROR
    // =========================================================

    private void mostrarError(
            String titulo,
            String mensaje
    ) {

        Alert alerta =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alerta.setTitle(
                "Compilador"
        );

        alerta.setHeaderText(
                titulo
        );

        alerta.setContentText(
                mensaje
        );

        alerta.showAndWait();
    }


    // =========================================================
    // MAIN
    // =========================================================

    public static void main(
            String[] args
    ) {

        launch(args);
    }
}