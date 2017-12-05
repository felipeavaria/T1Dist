# Tarea 2 Sistemas Distribuidos

Integrantes:
Felipe Avaria			2923547-3
Andrés Cifuentes	201004652-4

## Instrucciones

El comando "make" es el que controla la mayoria del codigo:

make : compila los archivos
make rmi: corre el comando rmiregister, este debe ser utilizado en la misma carpeta de los archivos
make servidor: corre el servidor
make cliente: corre el cliente
make clean: limpia los archivos .class


## Importante sobre el Algoritmo

- Parámetros de entrada, el programa sigue lo planteado en el enunciado, donde luego de ejecutar el "make", ya mencionado y luego correr el "make rmi", en una consola distinta, se debe correr como:

        java Semaforo <id> <n> <initialDelay> <bearer>

  donde:
    <id> corresponde al número identificador del proceso desde 0 en adelante (importante : estos no se deben repetir).
    <n>  corresponde al número de procesos totales de la ejecución (importante : debe ser coherente con los procesos que se correrán y deben ser todos iguales ).
    <initialDelay> valor de retraso mínimo para entrar en sección critica, en [microsegundos].
    <bearer> solo uno de los procesos debe llevar este valor como "true", sera quien inicie con el token.

## Consideraciones
- Cada proceso estará en sección critica durante 1 segundo.
- initialDelay es considerado como el tiempo mínimo que debe esperar un procesos para iniciar.
- Para optar al Bonus se tendrá que ingresar dos valores de initialDelay separados por una coma, esto se realizara de la siguiente manera.

        java Semaforo <id> <n> <initialDelay1>,<initialDelay2> <bearer>





## Ejemplo

consola 1: correr "make", es espera la compliación y luego "make rmi", se espera 1 segundo aprox. y esta corriendo.
consola 2: correr "java Semaforo 0 3 3000,2000 false"
consola 3: correr "java Semaforo 1 3 4000 true"
consola 4: correr "java Semaforo 2 3 9000 false"

Donde el procesos 0 corre su primera SC. primero, luego lo hace 1 y vuelve a 0, finalmente lo recibe el proceso 2. 
