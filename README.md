# WorkfusionProject
### A RPA Workfusion example project 

Este es un proyecto RPA de ejemplo utilizando el ecosistema de WorkFusion como base de desarrollo. Esta dividido en tres procesos que han de ejecutarse de forma secuencial: performerBotTask1, performerBotTask2... Hace uso de base de datos, Vaults Secret y de la enorme API que posee WorkFusion para automatizar procesos.


                
1. ### Arquetipo.
     Utilizaremos maven usando el arquetipo simple con la version 10.2.6.10. Deberemos posicionarnos en la carpeta desde la cual vamos a desarrollar nuestro proyecto. Por ejemplo: _c:\users\user1\workfusion-proyects_
   
       mvn archetype:generate -DarchetypeGroupId="com.workfusion.odf2" -DarchetypeArtifactId="simple-archetype" -DarchetypeVersion="10.2.6.10" -DgroupId="com.workfusion" -DartifactId="WorkfusionProject" -Dversion="1.0" -Dpackage="com.workfusion" -Dusecase-code="uc-wf" -Dusecase-name="uc-wf" -Dcontrol-tower-url="http://localhost:15280" -DinteractiveMode=false

Varios detalles a tener en cuenta:

+ Tanto _-Dusecase-code_ como _-Dusecase-name_ deben estar correctamente definidos. Liquibase los usará para formar nuestras tablas en MS SQL Server.
+ Tener en cuenta el puerto a usar. Yo usé el 15280 que es donde apuntaba el Control Tower de WorkFusion.
+ Tanto la versión como el tipo de arquetipo deben estar correctamente definidos.

Una vez ejecutada la creacion de nuestro entorno de desarrollo usando maven nos aparecerá una estructura de 3 carpetas con el nombre de nuestro proyecto seguido de -bcb, -package y -test.

2. ### Importación del proyecto a Studio.

Desde el menú _File_ de nuestro Studio pulsaremos _Import_ y nos aparecerá una nueva ventana que nos permite seleccionar que tipo de importación vamos a hacer. Seleccionamos _Maven_ y dentro de el, _Existing Maven Projects_ y pulsamos **next**. Tras ello, nos aparecerá la selección de la carpeta donde esta nuestro proyecto (_Root Directory_). Pulsamos sobre **Browse...** y seleccionamos la carpeta raiz. Una vez hecho esto, en la caja de _Projects_ aparecerá nuestra carpeta seguida de su estructura (tal como se dijo en el paso anterior: -bcb, -package y -test) Seleccionamos las 4 opciones. y pulsamos **next**. El proceso demorará unos minutos y ya tendremos nuestro proyecto instalado para Studio.

3. ### Tablas para Liquibase.

4. ### Compilación y Ejecución del proyecto.
                

  
