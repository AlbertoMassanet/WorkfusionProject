# WorkfusionProject
### A RPA Workfusion example project 

Este es un proyecto RPA de ejemplo utilizando el ecosistema de WorkFusion como base de desarrollo. Esta dividido en tres procesos que han de ejecutarse de forma secuencial: performerBotTask1, performerBotTask2... Hace uso de base de datos, Secrets Vault y de la enorme API que posee WorkFusion para automatizar procesos.


                
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

Una vez realizada la importación, deeremos modificar el archivo de contantes en com.workfusion.ultis.Constant: el Alias del Secrets Vault, la ruta del [ejecutable](https://acme-test.uipath.com/download-client) y la ruta del archivo Excel de output.

También tendremos que realizar algunos cambios en Studio. Para poder usar Secrets Vault en modo local, Studio nos brinda la posibilidad de hacerlo usando un par clave/valor interno. para ello pulsaremos en el menú **Window** y seguidamente **Preferences**. Una vez la se nos abra la ventana, desplegaremos la opción _WorkFusion Studio_ y seleccionaremos _Secrets Vault_ y nos aparecerá una tabla con 3 campos: Alias, Key y Value. En el Alias usaremos vaultworkfusion o el que hayamos elegido en com.workfusion.ultis.Constant

3. ### Tablas para Liquibase.

La definición de las tablas está dentro del archivo _tables.xml_ que se encuentra dentro de la carpeta _*-package_. Esta es su ruta: _-package/src/main/resources/datastore/migrations/versioned/_. Esta es la estructura del archivo _tables.xml_:

```xml
<?xml version="1.1" encoding="UTF-8" standalone="no"?>
<databaseChangeLog xmlns="http://www.liquibase.org/xml/ns/dbchangelog" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog ../dbchangelog-3.6.xsd">

    <changeSet author="ODF2 archetype" id="uc_wf_v0_0001">
        <createTable tableName="uc_uc_wf_transaction_v0">
            <column defaultValueComputed="NEWID()" name="uuid" type="NVARCHAR(36)">
                <constraints nullable="false" unique="true"/>
            </column>
            <column name="variation_id" type="BIGINT"/>
            <column name="parent_uuid" type="NVARCHAR(36)"/>
            <column name="start_time" type="datetime2"/>
            <column name="end_time" type="datetime2"/>
            <column name="status" type="NVARCHAR(36)"/>
            <column name="error_status" type="NVARCHAR(36)"/>
            <column name="start_bp_uuid" type="NVARCHAR(36)"/>
            <column name="is_stp" type="int"/>
            <column name="split_status" type="NVARCHAR(36)"/>
        </createTable>
    </changeSet>

    <changeSet author="ODF2 archetype" id="uc_wf_v0_0002">
        <createTable tableName="uc_uc_wf_stage_v0">
            <column defaultValueComputed="NEWID()" name="uuid" type="NVARCHAR(36)">
                <constraints nullable="false" unique="true"/>
            </column>
            <column name="name" type="NVARCHAR(max)"/>
            <column name="description" type="NVARCHAR(max)"/>
            <column name="stage_order" type="INT"/>
        </createTable>
    </changeSet>

    ...

    <changeSet author="ODF2 archetype" id="uc_wf_v0_0005">
        <createTable tableName="uc_uc_wf_ocr_cache_v0">
            <column name="cache_key" type="NVARCHAR(256)">
                <constraints nullable="false" unique="true"/>
            </column>
            <column name="document_xml_link" type="NVARCHAR(2048)"/>
            <column name="pages" type="INT"/>
            <column name="meta_info_pages" type="NVARCHAR(max)"/>
            <column name="meta_info_xml_url" type="NVARCHAR(2048)"/>
            <column name="creation_time" type="NVARCHAR(256)"/>
        </createTable>
    </changeSet>

</databaseChangeLog>
```
Contiene todas las tablas que el proyecto necesitará para su correcta ejecución. Se puede ver una serie de pautas:

+ Todas las tablas estan numeradas: Si observamos el changeSet de cada tabla vemos que su id termina en una numeración (0001, 0002, etc).
+ Los tipos definidos son los correspondientes a MS SQL Server.
+ El nombre de las tablas sigue unas pautas. Por ejemplo, _uc_uc_wfProject_ocr_cache_v1_0_ cuyo nombre real es _ocr_cache_ debe contener los caracteres correspondientes para que Liquibase lo cree correctamente.

Teniendo en cuenta estas pautas, compondremos nuestras tablas al final del archivo XML pero antes de su cierre (_</databaseChangeLog>_). Por ejemplo:

```xml

<?xml version="1.1" encoding="UTF-8" standalone="no"?>
<databaseChangeLog xmlns="http://www.liquibase.org/xml/ns/dbchangelog" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog ../dbchangelog-3.6.xsd">

    <changeSet author="ODF2 archetype" id="uc_wf_v0_0001">
        <createTable tableName="uc_uc_wf_transaction_v0">
            <column defaultValueComputed="NEWID()" name="uuid" type="NVARCHAR(36)">
                <constraints nullable="false" unique="true"/>
            </column>
            <column name="variation_id" type="BIGINT"/>
            <column name="parent_uuid" type="NVARCHAR(36)"/>
            <column name="start_time" type="datetime2"/>
            <column name="end_time" type="datetime2"/>
            <column name="status" type="NVARCHAR(36)"/>
            <column name="error_status" type="NVARCHAR(36)"/>
            <column name="start_bp_uuid" type="NVARCHAR(36)"/>
            <column name="is_stp" type="int"/>
            <column name="split_status" type="NVARCHAR(36)"/>
        </createTable>
    </changeSet>

    <changeSet author="ODF2 archetype" id="uc_wf_v0_0002">
        <createTable tableName="uc_uc_wf_stage_v0">
            <column defaultValueComputed="NEWID()" name="uuid" type="NVARCHAR(36)">
                <constraints nullable="false" unique="true"/>
            </column>
            <column name="name" type="NVARCHAR(max)"/>
            <column name="description" type="NVARCHAR(max)"/>
            <column name="stage_order" type="INT"/>
        </createTable>
    </changeSet>

    <changeSet author="ODF2 archetype" id="uc_wf_v0_0003">
        <createTable tableName="uc_uc_wf_transaction_stage_log_v0">
            <column defaultValueComputed="NEWID()" name="uuid" type="NVARCHAR(36)">
                <constraints nullable="false" unique="true"/>
            </column>
            <column name="stage_id" type="NVARCHAR(36)">
                <constraints foreignKeyName="fk_uc_uc_acmeProject_transaction_stage_log_v1_0_uc_uc_acmeProject_stage_v1_0" nullable="false" referencedColumnNames="uuid" referencedTableName="uc_uc_acmeProject_stage_v1_0"/>
            </column>
            <column name="transaction_uuid" type="NVARCHAR(36)">
                <constraints foreignKeyName="uc_uc_acmeProject_transaction_stage_log_v1_0_uc_uc_acmeProject_transaction_v1_0" nullable="false" referencedColumnNames="uuid" referencedTableName="uc_uc_acmeProject_transaction_v1_0"/>
            </column>
            <column name="bp_uuid" type="NVARCHAR(36)"/>
            <column name="timestamp" type="DATE"/>
            <column name="type" type="NVARCHAR(36)"/>
        </createTable>
    </changeSet>

    <changeSet author="ODF2 archetype" id="uc_wf_v0_0004">
        <createTable tableName="uc_uc_wf_config_v0">
            <column defaultValueComputed="NEWID()" name="uuid" type="NVARCHAR(36)">
                <constraints nullable="false" unique="true"/>
            </column>
            <column name="variation_id" type="BIGINT"/>
            <column name="name" type="NVARCHAR(450)">
                <constraints nullable="false"/>
            </column>
            <column name="value" type="NVARCHAR(max)"/>
        </createTable>
    </changeSet>

    <changeSet author="ODF2 archetype" id="uc_wf_v0_0005">
        <createTable tableName="uc_uc_wf_ocr_cache_v0">
            <column name="cache_key" type="NVARCHAR(256)">
                <constraints nullable="false" unique="true"/>
            </column>
            <column name="document_xml_link" type="NVARCHAR(2048)"/>
            <column name="pages" type="INT"/>
            <column name="meta_info_pages" type="NVARCHAR(max)"/>
            <column name="meta_info_xml_url" type="NVARCHAR(2048)"/>
            <column name="creation_time" type="NVARCHAR(256)"/>
        </createTable>
    </changeSet>
    
    
    <!-- ADDED -->
    
    <changeSet author="ODF2 archetype" id="uc_wf_v0_0006">
        <createTable tableName="uc_uc_wf_clients_queue_v0">
            <column name="id_client" type="NVARCHAR(56)"/>
            <column name="wiid" type="NVARCHAR(56)"/>
            <column name="line_num" type="INT"/>
            <column name="page_num" type="INT"/>
        </createTable>
    </changeSet>
    
    <changeSet author="ODF2 archetype" id="uc_wf_v0_0007">
        <createTable tableName="uc_uc_wf_accounts_queue_v0">
            <column name="id_client" type="NVARCHAR(56)"/>
            <column name="id_account" type="NVARCHAR(56)"/>
            <column name="amount" type="NVARCHAR(56)"/>
            <column name="status" type="NVARCHAR(56)"/>
            <column name="is_Updated" type="BIT"/>
        </createTable>
    </changeSet>    

</databaseChangeLog>

```
Como puede verse, las dos últimas tablas son las que vamos a usar en nuestro proyecto. Les hemos añadido la numeración (0006 y 0007), hemos modificado el nombre manteniendo la estructura base y hemos usado el tipado de MS SQL Server.

4. ### Compilación y Ejecución del proyecto.

Una vez realizados los cambios solo nos queda compilar para que Studio nos genere los bots para realizar las pruebas en local y subirlo al Control Tower de WorkFusion. Vamos a ello.

`NOTA: A de tenerse en cuenta que las pruebas las vamos a realizar en modo local (sin usar Control Tower) por lo que, como se puede ver en los archivos de repositorios de ambas tablas, haremos uso de Jdbc como conexión. Para realizar las pruebas desde Control Tower, comentar el retorno de Jdbc y descomentar la linea de super(connectionSource, Client.class). `

Para compliar nos posicionamos sobre la carpeta raiz y ejecutamos:

  mvn clean install -DskipTests

Una vez que se ha realizado la compilación con éxito, lo subiremos al Control Tower con:

    mvn bundle:import -DskipTests

o, en caso de tener una maquina virtual remota:

    mvn bundle:import -DskipTests -Premote

`NOTA: Una vez realizada correctamente la ejecucion de Liquibase a traves de bundle:import debemos acceder a la base de datos de Workfusion usando alguna aplicacion de terceros como dbeaver para ver el nombre final que Liquibase ha dado a nuestras tablas. Copiamos sus nombres y modificamos las entidades de las tablas en Java tanto en @DatabaseTable como la variable estatica con el nombre correspondiente).`

Una vez finalizado con éxito tendremos listo nuestro proyecto para ejecutarse correctamente. Usaremos los archivos generados en la compilación que estarán en _C:\Users\<usuario>\workfusion-workspace\workfusionProject\workfusionProject-bcb\target\classes\configs\main\_ o en la ruta desde la que hemos instalado el proyecto _*\workfusionProject-bcb\target\classes\configs\main\_. Estos archivos son: performer-bot-task1.xml, performer-bot-task2.xml y performer-bot-task3.xml. Los copiamos a una carpeta visible por el Studio y los vamos ejecutando de forma secuencial pulsando sobre ellos con el botón derecho del ratón para que aparezca el menu contextual y seleccionamos _Run as..._ y luego **Bot Task** Y empezará a hacerse la magia :)

Si se quiere ejecutar desde el Control Tower, deberemos realizar las conexiones pertinentes entre los procesos en el flujo de trabajo del _Business Process_ y modificar los repositorios de ambas tablas tal y como se comentó anteriormente.
  
