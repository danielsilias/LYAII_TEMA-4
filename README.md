# Compilador con Interfaz Gráfica JavaFX

Proyecto de compilador desarrollado en Java y adaptado a una interfaz gráfica utilizando JavaFX.

El sistema permite cargar o escribir código fuente y realizar las principales etapas del proceso de compilación, mostrando los resultados de cada análisis mediante una interfaz gráfica.

## Características principales

El compilador permite realizar las siguientes operaciones:

- Abrir archivos de código fuente `.txt`.
- Crear y editar código fuente.
- Guardar y guardar como archivos `.txt`.
- Realizar análisis léxico.
- Realizar análisis sintáctico.
- Realizar análisis semántico.
- Mostrar los tokens detectados.
- Mostrar la tabla de símbolos.
- Mostrar warnings y errores encontrados.
- Generar código intermedio mediante tercetos.
- Generar código Assembler.
- Exportar el código generado a un archivo `.asm`.

## Etapas del compilador

### 1. Análisis Léxico

El analizador léxico procesa el código fuente e identifica los diferentes tokens que forman el programa.

La interfaz permite visualizar información como:

- Número de línea.
- Número de token.
- Referencia.
- Lexema.

También se muestra la cantidad total de tokens detectados.

### 2. Análisis Sintáctico

El análisis sintáctico verifica que las instrucciones del programa cumplan con la estructura definida por la gramática del compilador.

Los errores sintácticos encontrados pueden consultarse desde la pestaña `Errores`.

### 3. Análisis Semántico

El análisis semántico verifica aspectos relacionados con el significado de las instrucciones y los elementos declarados en el programa.

La interfaz permite consultar la tabla de símbolos con información como:

- Referencia.
- Token.
- Lexema.
- Uso.
- Tipo.

### 4. Código intermedio

Después de realizar correctamente los análisis anteriores, el compilador genera una representación intermedia mediante tercetos.

Estos resultados pueden visualizarse desde la pestaña `Tercetos`.

### 5. Generación de Assembler

Cuando el código fuente no contiene errores, el compilador genera código Assembler.

El resultado puede visualizarse desde la pestaña `Assembler`.

También es posible exportarlo utilizando el botón:

`EXPORTAR .ASM`

El usuario puede seleccionar la ubicación donde desea guardar el archivo `Assembler.asm`.

## Interfaz gráfica

La interfaz fue desarrollada utilizando JavaFX.

La ventana principal está dividida en dos secciones:

### Código fuente

Permite escribir, modificar o visualizar el programa que será analizado.

### Resultados

Los resultados del compilador se organizan en las siguientes pestañas:

- Tokens
- Símbolos
- Errores
- Tercetos
- Assembler

La interfaz también muestra indicadores del estado de cada etapa:

- Léxico
- Sintáctico
- Semántico
- Tercetos
- Assembler

Cuando una compilación finaliza correctamente, los indicadores muestran el estado satisfactorio de cada etapa.

## Botones principales

### Nuevo

Limpia el editor para comenzar un nuevo código fuente.

### Abrir

Permite seleccionar y cargar un archivo `.txt`.

### Guardar

Guarda los cambios realizados sobre el archivo abierto.

### Guardar como

Permite guardar el código fuente en un nuevo archivo `.txt`.

### Limpiar

Limpia los resultados generados por los análisis.

### 1 LÉXICO

Ejecuta el proceso de análisis y muestra los resultados correspondientes a los tokens.

### 2 SINTÁCTICO

Ejecuta el proceso de análisis y muestra la información relacionada con los errores del código.

### 3 SEMÁNTICO

Ejecuta el proceso de análisis y muestra la tabla de símbolos y el estado del análisis.

### COMPILAR TODO

Ejecuta el proceso completo de compilación y genera:

1. Tokens.
2. Tabla de símbolos.
3. Errores.
4. Tercetos.
5. Código Assembler.

### EXPORTAR .ASM

Permite guardar el código Assembler generado en un archivo con extensión `.asm`.

Esta opción está disponible cuando la compilación se realiza correctamente.

## Tecnologías utilizadas

- Java
- JavaFX
- Maven
- IntelliJ IDEA
- CSS
- Analizador léxico
- Parser
- Generación de código intermedio
- Generación de código Assembler

## Estructura general del proyecto

```text
Compilador/
│
├── pom.xml
├── README.md
├── casos de prueba/
│
└── src/
    ├── AnalizadorLexico.java
    ├── Parser.java
    ├── TablaSimbolo.java
    ├── Terceto.java
    ├── ConversorTercetoAssembler.java
    ├── Compilador.java
    ├── MainFX.java
    │
    └── main/
        └── resources/
            └── compilador.css