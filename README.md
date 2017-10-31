*******************
*** INTEGRANTES ***
*******************

Felipe Avaria			2923547-3
Andrés Cifuentes	201004652-4


*********************
*** INSTRUCCIONES ***
*********************

* La tarea se encuentra en 3 Carpetas, cada una representando sea a Cliente, Servidor Central, o Servidor de Distrito.
* Para ejecutar alguno de los 3 programas, tiene que cambiar de carpeta a alguna de los programas, y en el terminal, correr el comando "make".
* Este compilara, y ejecuta el archivo Java dentro de la carpeta


*********************************
*** INSTRUCCIONES ESPECIFICAS ***
*********************************

** CLIENTE ** (carpeta /cliente)

* Al ingresar, se tiene que ingresar la ip de la máquina central, su puerto, y nombre de distrito a conectarse (Sensibilidad de mayusculas y minusculas)
* En caso de fallar conexion, se debe reiniciar el programa. Un caso tipico de falla, es cuando el Distrito a conectarse, todavia no se encuentra en la lista de distritos disponibles.
* Al conectarse, se observa un menu, con las opciones que se pueden realizar como cliente.


** SERVIDOR DISTRITO ** (carpeta /serv_distrito)

* Al ingresar, ingrese los datos que se solicitan. Estos tienen que ver con la conexión de multicast, y para peticiones
* También, ingrese los datos del servidor central, para solicitud de ID
* Luego, se accedera a un menu, que permite realizar las operaciones de distrito.


** SERVIDOR CENTRAL ** (carpeta /serv_central)

* Al correr aplicación, ingrese puerto para recibir las peticiones.
* Luego, se observara un menú, donde estan las acciones de servidor central.
* Agregar distritos es algo importante, ya que permite que los clientes puedan conectarse.
* Cuando un cliente se conecta, le aparecera una notificación en consola. Tiene que ingresar a la opcion del menu de "aceptar peticiones de clientes" para poder autorizar la conexión de clientes.

**************************
*** OTROS / IMPORTANTE ***
**************************

* En los 3 programas, se tiene una nomenclatura para el tipo de Titan, y el estado de un titan (libre, capturado, o asesinado). El significado de los numeros que se muestran en el programa son los siguientes:

** Tipo de Titan **
1: Normal
2: Excentrico
3: Cambiante

** Estado de un Titan **
0: Libre
1: Capturado
2: Asesinado


