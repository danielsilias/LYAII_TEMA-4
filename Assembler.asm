.386
.model flat, stdcall ; Indica el tamaño de programa
.STACK 200h ; Inicializa Stacken dir.indicada
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
include \masm32\include\masm32rt.inc
dll_dllcrt0 PROTO C
printf PROTO C :VARARG

.data
_funcion_actual DD ?
ERROR_EN_EJECUCION DB "ERROR EN EJECUCION" , 0 
Division_por_cero DB "Division por cero" , 0 
Overflow_en_suma_de_enteros DB "Overflow en suma de enteros" , 0 
Recursion_en_una_funcion DB "Recursion en una funcion" , 0 
PRINT DB "Impresion por pantalla" , 0 
aux_mem_2bytes DW ?
_aux_conversion DD ?
_aux_Programa_Ejemplo DQ ?
_aux2_Programa_Ejemplo DD ?
_f2_Programa_Ejemplo DD ?
_y_Programa_Ejemplo_f1 DD ?
_Z_Programa_Ejemplo_f1 DD ?
_constante1 DQ 1.5E+2
cadena4 DB "Entra al if" , 0 
cadena5 DB "Entra al while" , 0 
cadena6 DB "Entra al CATCH" , 0 
cadena7 DB "todo  perfecto " , 0 
@aux0 DD ?
@aux1 DD ?
@aux2 DQ ?
@aux3 DD ?
@aux4 DQ ?
@aux5 DD ?
@aux6 DD ?
@aux7 DD ?


.code
FNINIT

error_division_por_cero_ULONG:
CMP EAX, 0
JNE fin_funcion_division_por_cero_ULONG
invoke MessageBox, NULL, addr Division_por_cero , addr ERROR_EN_EJECUCION , MB_OK 
JMP fin_ejecucion
fin_funcion_division_por_cero_ULONG:
ret

error_division_por_cero_DOUBLE:
FTST
MOV EAX, 0
FSTSW aux_mem_2bytes
MOV AX , aux_mem_2bytes
SAHF
JNE fin_funcion_division_por_cero_DOUBLE
invoke MessageBox, NULL, addr Division_por_cero , addr ERROR_EN_EJECUCION , MB_OK 
JMP fin_ejecucion
fin_funcion_division_por_cero_DOUBLE:
ret

error_overflow_suma_entero:
JNC fin_overflow_suma_entero
invoke MessageBox, NULL, addr Overflow_en_suma_de_enteros , addr ERROR_EN_EJECUCION , MB_OK 
JMP fin_ejecucion
fin_overflow_suma_entero:
ret

error_recursion:
CMP EAX, _funcion_actual
JNE fin_recursion
invoke MessageBox, NULL, addr Recursion_en_una_funcion , addr ERROR_EN_EJECUCION , MB_OK 
JMP fin_ejecucion
fin_recursion:
ret

f1_Programa_Ejemplo:

MOV _y_Programa_Ejemplo_f1, EAX
MOV EAX, f1_Programa_Ejemplo
MOV _funcion_actual, EAX

MOV EAX, _y_Programa_Ejemplo_f1
MOV _Z_Programa_Ejemplo_f1, EAX

MOV EAX, _Z_Programa_Ejemplo_f1
CMP EAX, 0
MOV EAX, 0
MOV EBX, 0
SETS AH
SETZ BH
ADD AH, BH
SETZ AH
MOV @aux0, EAX


MOV EAX, START
MOV _funcion_actual, EAX

MOV EAX, _Z_Programa_Ejemplo_f1
ret

START:
MOV EAX, START
MOV _funcion_actual, EAX

FLD _constante1
FSTP _aux_Programa_Ejemplo

MOV EAX, 1
MOV _aux2_Programa_Ejemplo, EAX

MOV EAX, _aux2_Programa_Ejemplo ; 
ADD EAX, 1
CALL error_overflow_suma_entero
MOV @aux1, EAX

FILD @aux1
FSTP @aux2

FLD _aux_Programa_Ejemplo
FCOMP @aux2
MOV EAX, 0
FSTSW aux_mem_2bytes
MOV AX, aux_mem_2bytes
SAHF
MOV EBX, 0
SETC BH
CMP BH, 0
SETZ BH
MOV @aux3, EBX

MOV EAX, @aux3
SUB EAX, 0
JE label_18

invoke MessageBox, NULL, addr cadena4 , addr PRINT , MB_OK 
label_12:
FILD _aux2_Programa_Ejemplo
FSTP @aux4

FLD _aux_Programa_Ejemplo
FCOMP @aux4
MOV EAX, 0
FSTSW aux_mem_2bytes
MOV AX, aux_mem_2bytes
SAHF
MOV EBX, 0
MOV ECX, 0
SETC BH
SETZ CH
ADD BH, CH
SETZ BH
MOV @aux5, EBX

MOV EAX, @aux5
SUB EAX, 0
JE label_18

invoke MessageBox, NULL, addr cadena5 , addr PRINT , MB_OK 
JMP label_18

JMP label_12

label_18:
MOV EAX, f1_Programa_Ejemplo
CALL error_recursion
MOV EAX, _aux2_Programa_Ejemplo
CALL f1_Programa_Ejemplo
MOV @aux6, EAX

MOV EAX, @aux6
CALL error_division_por_cero_ULONG
MOV EAX, 1
MOV EDX, 0
DIV @aux6
MOV @aux7, EAX

MOV EAX, @aux7
MOV _aux2_Programa_Ejemplo, EAX

MOV EAX, @aux0
SUB EAX, 0
JNE label_24

invoke MessageBox, NULL, addr cadena6 , addr PRINT , MB_OK 
MOV EAX, 0
MOV _aux2_Programa_Ejemplo, EAX

label_24:
invoke MessageBox, NULL, addr cadena7 , addr PRINT , MB_OK 
fin_ejecucion:
FNINIT
invoke ExitProcess, 0
END START


