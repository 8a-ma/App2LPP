package App2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Date;
import java.util.Calendar;
import java.util.Iterator;
import java.util.InputMismatchException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

//Clase Persona
class Persona {
  protected String rut;
  protected String nombre;
  protected String edad;
  

  public Persona(String rut, String nombre, String edad) {
    setrut(rut);
    setnombre(nombre);
    setedad(edad);
  }

  // GETTERS
  public String getrut() {
    return this.rut;
  }

  public String getnombre() {
    return this.nombre;
  }

  public String getedad() {
    return this.edad;
  }

  // SETTERS
  public void setrut(String rut) {
    this.rut = rut;

  }

  public void setnombre(String nombre) {
    this.nombre = nombre;
  }

  public void setedad(String edad) {
    this.edad = edad;
  }

  // METODOS
  public boolean validar_edad(){
    int edad = Integer.parseInt(this.edad);
    try{
      if(edad <= 0){
        return false;
      }
    } catch (Exception e){
      return false;
    }
    
    return true;
  }
  public boolean formato_rut() {
    int largo = this.rut.length();
    int verificador;
    int suma = 0;
    int multiplicador = 2;
    //Si no tiene - retorna 0
    if (this.rut.charAt(this.rut.length() - 2) != '-') {
      return false;
    }
    //Si tiene como ultimo numero es k se transforma a numero
    if (this.rut.charAt(this.rut.length() - 1) == 'k' || this.rut.charAt(this.rut.length() - 1) == 'K') {
      verificador = 10;
    } else if (this.rut.charAt(this.rut.length() - 1) >= '0' && this.rut.charAt(this.rut.length() - 1) <= '9') {
      verificador = Integer.parseInt(this.rut.substring(this.rut.length() - 1));
    } else {
      return false;
    }

    for (int i = largo - 3; i >= 0; i--) {
      if (this.rut.charAt(i) >= '0' && this.rut.charAt(i) <= '9') {
        suma += (this.rut.charAt(i) - '0') * multiplicador;
        multiplicador++;

        if (multiplicador == 8) {
          multiplicador = 2;
        }
      } else {
        return false;
      }
    }

    int resto = suma % 11;
    int resultado = 11 - resto;

    if (resultado == verificador) {
      return true;
    } else {
      return false;
    }
  }
}

//Clase Sede
class Sede {
  protected String code_sede;
  protected String ubicacion;

  public Sede(String code_sede, String ubicacion) {
    setcode_sede(code_sede);
    setubicacion(ubicacion);
  }

  // GETTERS
  public String getcode_sede() {
    return this.code_sede;
  }

  public String getubicacion() {
    return this.ubicacion;
  }

  // SETTERS
  public void setcode_sede(String code_sede) {
    this.code_sede = code_sede;
  }

  public void setubicacion(String ubicacion) {
    this.ubicacion = ubicacion;
  }
}

//Clase Plan
class Plan {
  protected String code_plan;
  protected String descripcion;

  public Plan(String code_plan, String descripcion) {
    this.code_plan = code_plan;
    this.descripcion = descripcion;
  }

  // GETTERS
  public String getcode_plan() {
    return this.code_plan;
  }

  public String getdescripcion() {
    return this.descripcion;
  }

  // SETTERS
  public void setcode_plan(String code_plan) {
    this.code_plan = code_plan;
  }

  public void setdescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

}

//Clase Cliente HEREDA Persona
class Cliente extends Persona{
  protected Plan plan;
  protected String desde;
  protected String hasta;
  protected Sede sede;

  public Cliente(String rut, String nombre, String edad, String code_plan, String descripcion, String desde, String hasta, String code_sede, String ubicacion){
    super(rut, nombre, edad);

    setdesde(desde);
    sethasta(hasta);
    setsede(code_sede, ubicacion);
    setplan(code_plan, descripcion);
  }

  //GETTER
  public Plan getplan(){
    return this.plan;
  }
  public Sede getsede(){
    return this.sede;
  }
  public String getdesde(){
    return this.desde;
  }
  public String gethasta(){
    return this.hasta;
  }
  //SETTER
  public void setplan(String code_plan, String descripcion){
    Plan plan = new Plan(code_plan, descripcion);
    this.plan = plan;
  }
  public void setsede(String code_Sede, String ubicacion){
    Sede sede = new Sede(code_Sede, ubicacion);
    this.sede = sede;
  }
  public void setdesde(String desde){
    this.desde = desde;
  }
  public void sethasta(String hasta){
    this.hasta = hasta;
  }

  //Metodos
  //Verifica si el rut esta en el formato correcto
  public boolean formato_rut(){
    if (this.rut.charAt(this.rut.length() - 2) != '-') {
      return false;
    }
    else {
      return true;
    }
  }
  //Valida que la fecha desde sea menor a la fecha hasta
  public boolean validar_fecha(String desde, String hasta) {
    try {
      Date fecha1 = new SimpleDateFormat("yyyy-MM-dd").parse(desde);
      Date fecha2 = new SimpleDateFormat("yyyy-MM-dd").parse(hasta);
      if (fecha1.compareTo(fecha2) < 0) {
        return true;
      }
    } catch (ParseException e) {
      return false;
    }
    return false;
  }
  // Valida que la fecha siga el formato correcto
  public boolean validar_fecha(String fecha) {
    try {
      Date fecha1 = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
      return true;
    } catch (ParseException e) {
      return false;
    }
  }
}

//Clase Data
abstract class Data{
  String path;
  List<Cliente> data = new ArrayList<>();
  List<Sede> sedes = new ArrayList<>();
  List<String> codes_planes = new ArrayList<>();
  List<String> descripcion_planes = new ArrayList<>();

  public Data(String path){

    setpath(path);
    try{

      setData();
      initplanes();
      initsedes();

      elminarClientesSinrut();
      eliminarClienteSinPlan();
      eliminarClienteSinSede();
      eliminarCliente4Fecha();
      eliminarCliente4FechaHasta();

      rellenarClienteSinDesde();
      rellenarClienteSinCodeSede();

    } catch (Exception e){
      System.out.println("Archivo no encontrado");
    }
  }
  //SETTER
  public void setpath(String path){
    this.path = path;
  }
  //GETTER
  public List<Cliente> getdata(){
    return this.data;
  }
  //EXTRACCION DE DATOS Y LIMPIEZA
  //Se lee el csv y se almacenan los clientes
  public void setData()throws Exception {
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(this.path));
      String fila;
      while ((fila = reader.readLine()) != null) {
        String[] datos = fila.split(",");
        // Impone como "null" a cada casilla que este vacia, nulla o que el largo de la
        // casilla sea menor a 1
        for (int i = 0; i < datos.length; i++) {
          if (datos[i] == null || datos[i].isEmpty() || datos[i].length() < 1) {
            datos[i] = "null";
          }
        }
        // Crea los nulos para las ultimas casillas que no tengan datos
        if (datos.length < 9) {
          for (int j = datos.length; j < 9; j++) {
            List<String> lista = new ArrayList<>(Arrays.asList(datos));
            lista.add(j, "null");
            datos = lista.toArray(new String[0]);
          }
        }

        String rut = datos[0];
        String nombre = datos[1];
        String edad = datos[2];
        String code_plan = datos[3];
        String descripcion = datos[4];
        String desde = datos[5];
        String hasta = datos[6];
        String code_sede = datos[7];
        String ubicacion = datos[8];
        Cliente cliente = new Cliente(
            rut,
            nombre,
            edad,
            code_plan,
            descripcion,
            desde,
            hasta,
            code_sede,
            ubicacion);
        this.data.add(cliente);
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        reader.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }
  //Se extraen los code_planes y descripciones
  public void initplanes() {
    List<String> codes = new ArrayList<>();
    List<String> descripciones = new ArrayList<>();

    // Se guardan todos los code y descripciones del data en arreglos diferentes
    for (int i = 1; i < this.data.size(); i++) {
      codes.add(this.data.get(i).getplan().getcode_plan());
      descripciones.add(this.data.get(i).getplan().getdescripcion());
    }

    List<String> codes_unicos = new ArrayList<>();
    List<String> descripciones_unicos = new ArrayList<>();

    // Se eliminan los duplicados de codigos
    for (String code : codes) {
      if (!codes_unicos.contains(code)) {
        codes_unicos.add(code);
      }
    }
    // Se eliminan los duplicados de descripciones
    for (String descripcion : descripciones) {
      if (!descripciones_unicos.contains(descripcion)) {
        descripciones_unicos.add(descripcion);
      }
    }
    // Se elimina el "null"
    String nulo = new String("null");
    if (codes_unicos.contains(nulo)) {
      codes_unicos.remove(nulo);
    }
    // Se elimina el "null"
    if (descripciones_unicos.contains(nulo)) {
      descripciones_unicos.remove(nulo);
    }

    // Se almacenan en los atributos
    for (String code : codes_unicos) {
      this.codes_planes.add(code);
    }
    for (String descripcion : descripciones_unicos) {
      this.descripcion_planes.add(descripcion);
    }
  }
  //Se extraen las sedes
  public void initsedes() {
    List<Sede> sedes = new ArrayList<>();
    // Se obtienen las sedes
    for (int i = 1; i < this.data.size(); i++) {
      sedes.add(this.data.get(i).getsede());
    }
    // Eliminar duplicados
    List<Sede> sedes_unicos = new ArrayList<>();
    sedes_unicos.add(sedes.get(0));

    // Elimina los code iguales y ubicaciones "null"
    for (int i = 0; i < sedes.size(); i++) {
      boolean encontrado = false;
      for (Sede sede : sedes_unicos) {

        if (sedes.get(i).getcode_sede().equals(sede.getcode_sede())
            || sedes.get(i).getubicacion().equals(sede.getubicacion())) {
          encontrado = true;
          break;
        }
      }
      if (!encontrado) {
        sedes_unicos.add(sedes.get(i));
      }
    }

    // Se agrega las ubicaciones faltantes

    sedes_unicos.get(0).setubicacion("La Cisterna");
    sedes_unicos.get(1).setcode_sede("MAC");

    for (Sede sede : sedes_unicos) {
      this.sedes.add(sede);
    }
  }
  //LIMPIEZA
  // Elimina los clientes invalidos por rut o nulo 
  public void elminarClientesSinrut() {
    Iterator<Cliente> iterator = this.data.iterator();
    while (iterator.hasNext()) {
      Cliente cliente = iterator.next();
      Persona persona = new Persona(cliente.getrut(), "", "");
      if (!persona.formato_rut() || !cliente.formato_rut() || cliente.getrut().equals("null")) {
        iterator.remove();
      }
    }
  }
  // Elimina los clientes con planes nulos
  public void eliminarClienteSinPlan() {
    String nulo = new String("null");
    Iterator<Cliente> it = this.data.iterator();
    while (it.hasNext()) {
      Cliente cliente = it.next();
      if (cliente.getplan().getcode_plan().equals(nulo) || cliente.getplan().getdescripcion().equals(nulo)) {
        it.remove();
      }
    }
  }
  //Elimina los clientes con code_sedes y ubicacion nulas
  public void eliminarClienteSinSede(){
    String nulo = new String("null");
    Iterator<Cliente> it = this.data.iterator();
    while (it.hasNext()) {
      Cliente cliente = it.next();
      if (cliente.getsede().getcode_sede().equals(nulo) && cliente.getsede().getubicacion().equals(nulo)) {
        it.remove();
      }
    }
  }
  //Elimina los clientes con fechas invalidas
  public void eliminarCliente4Fecha() {
    Iterator<Cliente> it = this.data.iterator();
    while (it.hasNext()) {
      Cliente cliente = it.next();
      if (!cliente.validar_fecha(cliente.getdesde(), cliente.gethasta())) {
        it.remove();
      }
    }
  }
  // Elimina los clientes que tengan fechas hasta nulas
  public void eliminarCliente4FechaHasta() {
    String nulo = new String("null");
    Iterator<Cliente> it = this.data.iterator();
    while (it.hasNext()) {
      Cliente cliente = it.next();
      if (cliente.gethasta().equals(nulo)) {
        it.remove();
      }
    }
  }
  
  //RELLENAR
  // Rellena los cliente que tengan fecha desde nula
  public void rellenarClienteSinDesde() {
    String nulo = new String("null");
    for (Cliente cliente : this.data) {
      if (cliente.getdesde().equals(nulo) && !cliente.gethasta().equals(nulo)) {
        try {
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
          Date hasta = sdf.parse(cliente.gethasta());
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(hasta);
          calendar.add(Calendar.MONTH, -3);
          Date desdeFecha = calendar.getTime();
          String desde = sdf.format(desdeFecha);
          cliente.setdesde(desde);
        } catch (ParseException e) {

        }
      }
    }
  }
  // Rellear los clientes que tengan sede vacia
  public void rellenarClienteSinCodeSede() {
    String nulo = new String("null");
    for (Cliente cliente : this.data) {
      if(cliente.getsede().getcode_sede().equals(nulo) || cliente.getsede().getubicacion().equals(nulo)){
        for(Sede sede : this.sedes){
          if(cliente.getsede().getcode_sede().equals(sede.getcode_sede()) || cliente.getsede().getubicacion().equals(sede.getubicacion())){
            cliente.setsede(sede.getcode_sede(), sede.getubicacion());
          }
        }
      }
    }
  }
  
  //METODOS
  //Ordenar por alfabeto por BumbbleSort
  public void ordenarAlfabeticamente(){
    int n = this.data.size()-1;
    Cliente temp;

    for(int i = 1; i < n; i++){
      for(int j = 1; j < (n-i); j++){
        if(this.data.get(j-1).getnombre().compareTo(this.data.get(j).getnombre()) > 0){
          temp = this.data.get(j-1);
          this.data.set(j-1, this.data.get(j));
          this.data.set(j, temp);
        }
      }
    }
  }
  
  //Guardar en un archivo
  public void guardar(String path) {
    try {
      FileWriter file = new FileWriter(path);
      BufferedWriter bw = new BufferedWriter(file);
      for (Cliente cliente : this.data) {
        bw.write(
          cliente.getrut() + "," +
          cliente.getnombre() + "," +
          cliente.getedad() + "," +
          cliente.getplan().getcode_plan() + "," +
          cliente.getplan().getdescripcion() + "," +
          cliente.getdesde() + "," +
          cliente.gethasta() + "," +
          cliente.getsede().getcode_sede() + "," +
          cliente.getsede().getubicacion() + "\n");
        
      }
      bw.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //Busqueda de nombres
  public String busquedaNombre(){
    int i = 0;
    List<String> ruts = new ArrayList<>();
    List<String> nombres = new ArrayList<>();
    System.out.print("Ingrese un nombre: ");
    Scanner resp = new Scanner(System.in);
    String nombre = resp.nextLine();
    
    //Encuentra las primeras 5 coincidencias
    for(Cliente cliente : this.data){
      if(cliente.getnombre().regionMatches(true, 0, nombre, 0, nombre.length()) && i < 5){
        i++;
        ruts.add(cliente.getrut());
        nombres.add(cliente.getnombre());
      }
    }

    //Muestra los primeros 5 clientes encontrados
    int index = 1;
    for(String name : nombres){
      if(i >= 0 && i < 5){
        System.out.println(index+ ". " + name);
      }
      else{
        break;
      }
      index++;
    }

    //Se pide el nombre realmente buscado
    Scanner respname = new Scanner(System.in);
    try {
      System.out.print("Ingrese una opcion: ");
      int respint = respname.nextInt();
      System.out.println();
      System.out.println();
      if(respint <= 5 && respint >= 1){
        respint--;
        return ruts.get(respint);
      }
      else{
        System.out.println("Opcion invalida");
      }
    } catch (Exception e) {
      System.out.println("Opcion invalida");
      return "";
    }
    return "";
  }
}
//Clase Menu ABSTRACCION Data
class Menu extends Data{
  public Menu(String path){
    super(path);
  }
  //MOSTRAR
  //Metodo para mostrar todos los clientes
  public void mostrarClientes(){
    for(Cliente cliente : this.data){
      System.out.print(
        cliente.getrut() + " " +
        cliente.getnombre() + " " +
        cliente.getplan().getcode_plan() + " " +
        cliente.getplan().getdescripcion() + " " +
        cliente.getdesde() + " " +
        cliente.gethasta() + " " +
        cliente.getsede().getcode_sede() + " " +
        cliente.getsede().getubicacion());
      System.out.println();
    }
    System.out.println();
  }
  //Metodo para mostrar todas las sedes
  public void mostrarSedes(){
    int i = 1;
    for(Sede sede : this.sedes){
      System.out.println(i+". " + sede.getcode_sede() + ", " + sede.getubicacion());
      i++;
    }
    System.out.println();
  }
  //Metodo para mostrar todos los codigos de planes
  public void mostrarCodePlanes(){
    int i = 1;
    for(String code : this.codes_planes){
      System.out.println(i + ". " + code);
      i++;
    }
    System.out.println();
  }
  //Metodo para mostrar todos las descripciones
  public void mostrarDescripciones(){
    int i = 1;
    for(String descripcion : this.descripcion_planes){
      System.out.println(i + ". " + descripcion);
      i++;
    }
    System.out.println();
  }
  //Muestrar un cliente por su rut
  public void mostrarCliente(String rut){
    boolean activacion = true;
    for (Cliente cliente : this.data) {
      if (cliente.getrut().equals(rut)) {
        System.out.print(
            "Rut: " + cliente.getrut() + "\n" +
            "Nombre: " + cliente.getnombre() + "\n" +
            "Code plan: " + cliente.getplan().getcode_plan() + "\n" +
            "Descripcion: " + cliente.getplan().getdescripcion() + "\n" +
            "Desde: " + cliente.getdesde() + "\n" +
            "Hasta: " + cliente.gethasta() + "\n" +
            "Code sede: " + cliente.getsede().getcode_sede() + "\n" +
            "Ubicacion: " + cliente.getsede().getubicacion());
        activacion = false;
        break;
      }
    }
    if(activacion){
      System.out.println("Rut no encontrado o formato incorrecto");
    }
    System.out.println();
  }

  //AGREGAR
  //Agrega un nuevo cliente
  public void agregarCliente() {
    String rut = "";
    String nombre = "";
    String edad = "";
    String code_plan = "";
    String descripcion = "";
    String desde = "";
    String hasta = "";
    String code_sede = "";
    String ubicacion = "";

    Persona persona = new Persona(rut, nombre, edad);

    // RUT
    while (true) {
      Scanner resp = new Scanner(System.in);
      System.out.print("Rut (1111111-1): ");
      rut = resp.nextLine();
      persona.setrut(rut);
      if(!persona.formato_rut()) {
        System.out.println("Rut invalido");
        System.out.println();
      } else {
        persona.setrut(rut);
        System.out.println();
        break;
      }
    }

    // NOMBRE
    Scanner resp1 = new Scanner(System.in);
    System.out.print("Nombre: ");
    nombre = resp1.nextLine();
    if (nombre.isEmpty()) {
      persona.setnombre("null");
    }
    persona.setnombre(nombre);
    System.out.println();

    // EDAD
    
    while (true) {
      Scanner resp2 = new Scanner(System.in);
      int edadint = 1;
      System.out.print("Edad: ");
      edad = resp2.nextLine();
      try {
        edadint = Integer.parseInt(edad);
        if (edadint <= 0) {
          System.out.println("Edad invalida");
          System.out.println();
        } else {
          persona.setedad(edad);
          System.out.println();
          break;
        }
      } catch (Exception e) {
        System.out.println("Formto illegal");
        System.out.println();
      }
    }

    // Plan
    // Codigo
    while (true) {
      Scanner resp3 = new Scanner(System.in);
      mostrarCodePlanes();
      System.out.println();
      System.out.print("Codigo plan: ");
      code_plan = resp3.nextLine();
      if (!this.codes_planes.contains(code_plan)) {
        System.out.println("Codigo invalido");
        System.out.println();
      } else {
        System.out.println();
        break;
      }
    }

    // Descripcion
    int opcion = 0;
    while (true) {
      mostrarDescripciones();
      Scanner resp4 = new Scanner(System.in);
      System.out.print("Numero de la descripcion: ");
      opcion = resp4.nextInt();
      opcion--;
      try {
        if (opcion >= this.descripcion_planes.size() || opcion < 0) {
          System.out.println("Opcion no valida");
          System.out.println();
        } else {
          descripcion = this.descripcion_planes.get(opcion);
          System.out.println();
          break;
        }
      } catch (Exception e) {
        System.out.println("Opcion illegal");
        System.out.println();
      }
    }

    // Sedes
    boolean codigo_valido = false;
    while (!codigo_valido) {
      mostrarSedes();
      System.out.print("Codigo sede: ");
      Scanner respIvaluable = new Scanner(System.in);
      String code_infutable = new String(respIvaluable.nextLine());
      
      // Revisa si la sede existe
      for (Sede sede : this.sedes) {
        if (sede.getcode_sede().equals(code_infutable)) {
          code_sede = sede.getcode_sede();
          ubicacion = sede.getubicacion();
          codigo_valido = true;
          System.out.println();
          break;
        }
      }
      if (!codigo_valido) {
        System.out.println("Codigo invalido");
        System.out.println();
      }
    }

    // Fechas
    while (true) {
      Scanner resp6 = new Scanner(System.in);
      Scanner resp7 = new Scanner(System.in);
      System.out.print("Fecha desde (yyyy-MM-dd): ");
      desde = resp6.nextLine();
      System.out.print("Fecha hasta (yyyy-MM-dd): ");
      hasta = resp7.nextLine();
      try {
        Date fecha1 = new SimpleDateFormat("yyyy-MM-dd").parse(desde);
        Date fecha2 = new SimpleDateFormat("yyyy-MM-dd").parse(hasta);
        if (fecha1.compareTo(fecha2) <= 0) {
          break;
        } else {
          System.out.println("Fecha desde es mayor a la fecha hasta");
          System.out.println("Fechas invalidas");
          System.out.println();
        }
        continue;
      } catch (ParseException e) {
        System.out.println("Formato fecha invalida");
        System.out.println();
      }
    }

    Cliente cliente = new Cliente(persona.getrut(), persona.getnombre(), persona.getedad(), code_plan, descripcion,
        desde, hasta, code_sede, ubicacion);

    this.data.add(cliente);
    System.out.println("Nuevo cliente agregado");
    System.out.println();
  }
  //Agrega un nuevo codigo de plan
  public void agregarcode() {
    mostrarCodePlanes();
    System.out.print("Ingrese un nuevo codigo: ");
    Scanner resp = new Scanner(System.in);
    String codeObject = new String(resp.nextLine());
    for (String code1 : this.codes_planes) {
      if (code1.equals(codeObject)) {
        System.out.println("Codigo ya ingresado");
        System.out.println();
        return;
      }
    }
    this.codes_planes.add(codeObject);
    System.out.println("Codigo plan agregada");
    System.out.println();
  }
  //Agrega una nueva descripcion de plan
  public void agregardescripcion() {
    mostrarDescripciones();
    Scanner resp = new Scanner(System.in);
    System.out.print("Nueva descripcion: ");
    String descObject = new String(resp.nextLine());

    for (String desc : this.descripcion_planes) {
      if (desc.equals(descObject)) {
        System.out.println("Descripcion ya ingresada");
        System.out.println();
        return;
      }
    }
    this.descripcion_planes.add(descObject);
    System.out.println("Descripcion agregada");
    System.out.println();
  }
  //Agrega una nueva sede (code y ubicacion)
  public void agregarsede() {
    mostrarSedes();
    Scanner resp1 = new Scanner(System.in);
    Scanner resp2 = new Scanner(System.in);
    System.out.print("Nuevo codigo: ");
    String code_sede = new String(resp1.nextLine());
    System.out.println();
    System.out.print("Nueva ubicacion: ");
    String ubicacion = new String(resp2.nextLine());

    for(Sede sede1 : this.sedes){
      if(sede1.getcode_sede().equals(code_sede)){
        System.out.println("Sede ya ingresada");
        System.out.println();
        return;
      }
    }
    Sede sede = new Sede(code_sede, ubicacion);
    this.sedes.add(sede);
    System.out.println("Sede agregada");
    System.out.println();
  }
  
  //ELIMINAR
  //Elimina un cliente ingresando un rut
  public void eliminarCliente() {
    Scanner respRut1 = new Scanner(System.in);

    System.out.println();
    System.out.print("Ingrese un rut: ");
    String rut = respRut1.nextLine();
    System.out.println();

    boolean activacion = false;

    for(Cliente client : this.data){
      if(client.getrut().equals(rut)){
        activacion = true;
      }
    }

    if(activacion){
      Iterator<Cliente> it = this.data.iterator();
      while (it.hasNext()) {
        Cliente cliente = it.next();
        if (cliente.getrut().equals(rut)) {
          it.remove();
          System.out.println("Cliente eliminado");
        }
      }
    }
    else{
      System.out.println("Cliente no encontrado o formato incorrecto");
    }
    
  }
  //Elimina un cliente por nombre
  public void eliminarCliente(String rut){
    Iterator<Cliente> it = this.data.iterator();
    while(it.hasNext()){
      Cliente cliente = it.next();
      if(cliente.getrut().equals(rut)){
        it.remove();
        System.out.println("Cliente eliminado");
        return;
      }
    }
    System.out.println("Cliente no encontrado");    
  }
  //Elimina un codigo de plan
  public void eliminarcodeplan() {
    mostrarCodePlanes();
    Scanner code = new Scanner(System.in);
    System.out.print("Codigo: ");
    String codeObject = new String(code.nextLine());

    Iterator<String> it = this.codes_planes.iterator();
    while(it.hasNext()){
      String cod = it.next();
      boolean client = false;
      for(Cliente cliente : this.data){
        if(cliente.getplan().getcode_plan().equals(codeObject)){
          client = true;
          break;
        }
        else{
          client = false;
        }
      }
      if(cod.equals(codeObject) && !client){
        it.remove();
        System.out.println("Codigo eliminado");
        return;
      }
    }
    System.out.println("Codigo no encontrado o con Clientes asociados");
  }
  //Elimina una descripcion de plan
  public void eliminardescripcion() {
    mostrarDescripciones();
    Scanner respDescPlan = new Scanner(System.in);
    System.out.print("Numero de la descripcion: ");
    try {
      int descplan = respDescPlan.nextInt();
      descplan--;

      boolean client = false;
      for(Cliente cliente : this.data){
        if(cliente.getplan().getdescripcion().equals(this.descripcion_planes.get(descplan))){
          client = true;
          break;
        }
        else{
          client = false;
        }
      }
      if(descplan >= 0 || descplan < this.descripcion_planes.size() && !client){
        this.descripcion_planes.remove(descplan);
        System.out.println("Descripcion eliminada");
        return;
      }
      else{
        System.out.println("Descripcion no encontrada o con Clientes asociads");
        return;
      }
      
    } catch (Exception e) {
      System.out.println("Opcion invalida");
      return;
    }
    
  }
  //Elimina una sede
  public void eliminarsede(){
    mostrarSedes();
    Scanner resp = new Scanner(System.in);
    System.out.print("Codigo: ");
    String code_sede = new String(resp.nextLine());

    Iterator<Sede> it = this.sedes.iterator();
    while(it.hasNext()){
      Sede sede = it.next();
      boolean client = false;
      for(Cliente cliente : this.data){
        if(cliente.getsede().getcode_sede().equals(code_sede)){
          client = true;
          break;
        }
        else{
          client = false;
        }
      }
      if(sede.getcode_sede().equals(code_sede) && !client){
        it.remove();
        System.out.println("Sede eliminada");
        System.out.println();
        return;
      }
    }

    System.out.println("Sede no encontrada o tiene clientes asociados");
  }

  //EDITAR
  public void editarCliente(){
    String cliente_ref = "";
    while(true){
      Scanner resp1 = new Scanner(System.in);
      System.out.println("0. Salir");
      System.out.println("1. Rut");
      System.out.println("2. Nombre");
      System.out.print("Opcion: ");
      String opcion = new String(resp1.nextLine());
      if("0".equals(opcion)){
        break;
      }
      else if("1".equals(opcion)){
        Scanner resp2 = new Scanner(System.in);
        System.out.print("Rut :");
        cliente_ref = resp2.nextLine();
        break;
      }
      else if("2".equals(opcion)){
        cliente_ref = busquedaNombre();
        break;
      }
      else{
        System.out.println("Opcion no valida");
        break;
      }
    }
    //Se busca el cliente que se querie cambiar
    int indexCliente = 0;
    boolean existencia = false;
    for(Cliente cliente : this.data){
      if(cliente.getrut().equals(cliente_ref)){
        existencia = true;
        break;
      }
      indexCliente++;
    }
    if(!existencia){
      System.out.println("Cliente no encontrado");
      return;
    }
    

    System.out.println();
    System.out.println("0. Salir");
    System.out.println("1. Rut");
    System.out.println("2. Nombre");
    System.out.println("3. Edad");
    System.out.println("4. Codigo plan");
    System.out.println("5. Descripcion plan");
    System.out.println("6. Fecha desde");
    System.out.println("7. Fecha hasta");
    System.out.println("8. Sede");
    
    System.out.println();
    Scanner scan = new Scanner(System.in);
    try {
      System.out.print("Opcion: ");
      int resp = scan.nextInt();
      System.out.println();

      switch (resp) {
        case 0:
          break;
        case 1: //RUT
          Scanner scanrut = new Scanner(System.in);
          System.out.print("Ingrese un nuevo rut (11111-1): ");
          String newrut = new String(scanrut.nextLine());
          Persona temppersona = new Persona(newrut, "", "");
          if(!temppersona.formato_rut()){
            System.out.println("Rut en formato incorrecto o erroneo");
            System.out.println();
            break;
          }
          else{
            this.data.get(indexCliente).setrut(newrut);
            System.out.println("Rut cambiado");
            System.out.println();
          }
          break;
        case 2: //NOMBRE
          Scanner scanname = new Scanner(System.in);
          System.out.print("Ingrese un nuevo nombre: ");
          String newname = new String(scanname.nextLine());
          if(newname.isBlank()){
            this.data.get(indexCliente).setnombre("null");
          }
          else{
            this.data.get(indexCliente).setnombre(newname);
          }
          System.out.println("Nombre cambiado");
          System.out.println();
          break;
        case 3: //EDAD
          Scanner scanedad = new Scanner(System.in);
          System.out.print("Ingrese una nueva edad: ");
          String newedad = new String(scanedad.nextLine());
          Persona tempersona1 = new Persona("", "", newedad);
          if(!tempersona1.validar_edad()){
            System.out.println("Edad invalida");
            System.out.println();
          }
          else{
            this.data.get(indexCliente).setedad(newedad);
            System.out.println("Edad cambiada");
            System.out.println();
          }
          break;
        case 4: //CODIGO PLAN
          mostrarCodePlanes();
          Scanner scancodeplan = new Scanner(System.in);
          System.out.print("Ingrese una nuevo codigo: ");
          String newcodeplan = new String(scancodeplan.nextLine());
          if(this.codes_planes.contains(newcodeplan)){
            //Se establece una nuevas fechas
            String fecha1;
            String fecha2;
            Scanner date1 = new Scanner(System.in);
            Scanner date2 = new Scanner(System.in);

            System.out.print("Porfavor ingresar la fecha desde (yyyy-MM-dd): ");
            fecha1 = date1.nextLine();
            System.out.print("Porfavor ingresar la fecha hasta (yyyy-MM-dd): ");
            fecha2 = date2.nextLine();

            if (!this.data.get(indexCliente).validar_fecha(fecha1)) {
              System.out.println("Fecha " + fecha1 + " Invalida");
            } else if (!this.data.get(indexCliente).validar_fecha(fecha2)) {
              System.out.println("Fecha " + fecha2 + " Invalida");
            }

            if (!this.data.get(indexCliente).validar_fecha(fecha1, fecha2)) {
              System.out.println("Fechas invalidas");
            } else {
              this.data.get(indexCliente).setdesde(fecha1);
              this.data.get(indexCliente).sethasta(fecha2);
            }
            this.data.get(indexCliente).setplan(newcodeplan, this.data.get(indexCliente).getplan().getdescripcion());
            System.out.println("Codigo plan cambiado");
            System.out.println();
            break;
          }
          else{
            System.out.println("Codigo plan no encontrado");
            System.out.println();
          }
          break;
        case 5: //DESCRIPCION
          mostrarDescripciones();
          Scanner scandescr = new Scanner(System.in);
          System.out.print("Ingrese un numero de opcion: ");
          try {
            int descropcion = Integer.parseInt(scandescr.nextLine());
            descropcion--;
            if(descropcion >= 0 && descropcion < this.descripcion_planes.size()){
              //Se establece una nuevas fechas
              String fecha1;
              String fecha2;
              Scanner date1 = new Scanner(System.in);
              Scanner date2 = new Scanner(System.in);

              System.out.print("Porfavor ingresar la fecha desde (yyyy-MM-dd): ");
              fecha1 = date1.nextLine();
              System.out.print("Porfavor ingresar la fecha hasta (yyyy-MM-dd): ");
              fecha2 = date2.nextLine();

              if (!this.data.get(indexCliente).validar_fecha(fecha1)) {
                System.out.println("Fecha " + fecha1 + " Invalida");
              } else if (!this.data.get(indexCliente).validar_fecha(fecha2)) {
                System.out.println("Fecha " + fecha2 + " Invalida");
              }

              if (!this.data.get(indexCliente).validar_fecha(fecha1, fecha2)) {
                System.out.println("Fechas invalidas");
              } else {
                this.data.get(indexCliente).setdesde(fecha1);
                this.data.get(indexCliente).sethasta(fecha2);
              }
              this.data.get(indexCliente).setplan(this.data.get(indexCliente).getplan().getcode_plan(), this.descripcion_planes.get(descropcion));
              System.out.println("Descripcion cambiada");
              System.out.println();
            }
          } catch (Exception e) {
            System.out.println("Opcion invalida");
            break;
          }
          break;
        case 6: //FECHA DESDE
          Scanner scandesde = new Scanner(System.in);
          System.out.print("Nueva fecha desde (yyyy-MM-dd): ");
          String desde = scandesde.nextLine();
          if(this.data.get(indexCliente).validar_fecha(desde, this.data.get(indexCliente).gethasta())){
            this.data.get(indexCliente).setdesde(desde);
            System.out.println("Fecha desde cambiada");
            System.out.println();
          }
          else{
            System.out.println("Fecha desde invalida");
            System.out.println();
          }
          break;
        case 7: //FECHA HASTA
          Scanner scanhasta = new Scanner(System.in);
          System.out.print("Nueva fecha hasta (yyyy-MM-dd): ");
          String hasta = scanhasta.nextLine();
          if(this.data.get(indexCliente).validar_fecha(this.data.get(indexCliente).getdesde(), hasta)){
            this.data.get(indexCliente).sethasta(hasta);
            System.out.println("Fecha hasta cambiada");
            System.out.println();
          }
          else{
            System.out.println("Fecha hasta invalida");
            System.out.println();
          }
          break;
        case 8: //SEDE
          mostrarSedes();
          Scanner scansede = new Scanner(System.in);
          System.out.print("Ingrese el codigo de una sede: ");
          String code_Sede = new String(scansede.nextLine());
          for(Sede sede : this.sedes){
            if(sede.code_sede.equals(code_Sede)){
              this.data.get(indexCliente).setsede(code_Sede, this.data.get(indexCliente).getsede().getubicacion());
              System.out.println("Sede cambiada");
              System.out.println();
              return;
            }
          }
          System.out.println("Sede no encontrada");
          break;
        default:
          System.out.println("Opcion invalida");
          break;
    }
    } catch (Exception e) {
      System.out.println("Opcion invalida");
      return;
    }
    

  }

  //ORQUESTADORES
  //Da dos opciones de eliminacion de cliente nombre y rut
  public void orchestatorEliminarCliente(){
    Scanner resp = new Scanner(System.in);
    System.out.println("0. Salir");
    System.out.println("1. Rut");
    System.out.println("2. Nombre");
    System.out.print("Eliga una opcion: ");
    String opcion = new String(resp.nextLine());
    System.out.println();

    if("0".equals(opcion)){
      return;
    }
    else if("1".equals(opcion)){
      eliminarCliente();
      return;
    }
    else if("2".equals(opcion)){
      eliminarCliente(busquedaNombre());
      return;
    }
    else{
      System.out.println("Opcion invalida");
      System.out.println();
      return;
    }
  }

  //Da dos opciones para mostrar un cliente nombre y rut
  public void orchestatorMostrarCliente(){
    Scanner resp = new Scanner(System.in);
    System.out.println("0. Salir");
    System.out.println("1. Rut");
    System.out.println("2. Nombre");
    System.out.print("Eliga una opcion: ");
    String opcion = new String(resp.nextLine());
    System.out.println();

    if("0".equals(opcion)){
      return;
    }
    else if("1".equals(opcion)){
      Scanner resprut = new Scanner(System.in);
      System.out.print("Ingrese un rut: ");
      String rut = resprut.nextLine();
      mostrarCliente(rut);
      return;
    }
    else if("2".equals(opcion)){
      mostrarCliente(busquedaNombre());
      return;
    }
    else{
      System.out.println("Opcion invalida");
      System.out.println();
    }
  }

  //Da dos opciones para agregar en plan codigo y descripcion
  public void orchestatorAgregarPlan(){
    Scanner resp = new Scanner(System.in);
    System.out.println("0. Salir");
    System.out.println("1. Codigo");
    System.out.println("2. Descripcion");
    System.out.print("Eliga una opcion: ");
    String opcion = new String(resp.nextLine());
    System.out.println();

    if("0".equals(opcion)){
      return;
    }
    else if("1".equals(opcion)){
      agregarcode();
      return;
    }
    else if("2".equals(opcion)){
      agregardescripcion();
      return;
    }
    else{
      System.out.println("Opcion no valida");
      return;
    }
  }
  
  //Da dos opciones para eliminar plan codigo y descripcion
  public void orchestatorEliminarPlan(){
    Scanner resp = new Scanner(System.in);
    System.out.println("0. Salir");
    System.out.println("1. Codigo");
    System.out.println("2. Descripcion");
    System.out.print("Eliga una opcion: ");
    String opcion = new String(resp.nextLine());
    System.out.println();

    if("0".equals(opcion)){
      return;
    }
    else if("1".equals(opcion)){
      eliminarcodeplan();
      return;
    }
    else if("2".equals(opcion)){
      eliminardescripcion();
      return;
    }
    else{
      System.out.println("Opcion no valida");
      System.out.println();
      return;
    }
  }

  //Da dos opciones para mostrar plan codigo y descripcion
  public void orchestatorMostrarPlan(){
    Scanner resp = new Scanner(System.in);

    System.out.println("0. Salir");
    System.out.println("1. Codigo");
    System.out.println("2. Descripcion");
    System.out.print("Eliga una opcion: ");

    String opcion = new String(resp.nextLine());
    System.out.println();

    if("0".equals(opcion)){
      return;
    }
    else if("1".equals(opcion)){
      mostrarCodePlanes();
      return;
    }
    else if("2".equals(opcion)){
      mostrarDescripciones();
      return;
    }
    else{
      System.out.println("Opcion invalida");
      System.out.println();
      return;
    }
  }
}

//Clase App crea un objeto de MENU
public class App2 {

 public void app(){
  Menu menu = new Menu("BigMuscle.csv");

  Scanner resp = new Scanner(System.in);
  int num;
  do {
    menu.guardar("BigMuscle.bak");
    System.out.println("==================================");
    System.out.println("=============Menu=================");
    System.out.println("Numero de clientes: " + menu.getdata().size());
    System.out.println();
    System.out.println("01. Agregar Cliente");
    System.out.println("02. Eliminar Cliente");
    System.out.println("03. Editar Cliente");
    System.out.println("04. Visualizar Cliente");
    System.out.println("05. Agregar Sede");
    System.out.println("06. Eliminar Sede");
    System.out.println("07. Ver Sedes");
    System.out.println("08. Agregar Plan");
    System.out.println("09. Elimnar Plan");
    System.out.println("10. Ver Plan");
    System.out.println("11. Ver plantilla clientes");
    System.out.println("12. Ordenar Alfabeticamente");
    System.out.println("0. Salir");
    System.out.println();
    System.out.print("Ingrese una opcion: ");
    num = 11123;
    try{
      num = resp.nextInt();
      System.out.println("==================================");
      System.out.println("==================================");
      System.out.println();
    } catch (Exception e){
      System.out.println("Opcion invalida");
      System.out.println("El programa se cierra\n\nporque lo enojaste");
    }
    switch (num) {
      case 0: // Salir
        System.out.println();
        System.out.println("Adios ;D");
        System.out.println();
        menu.guardar("BigMuscle.csv");
        break;
      case 1: // Agregar Cliente
        menu.agregarCliente();
        break;
      case 2: // Eliminar Cliente
        menu.orchestatorEliminarCliente();
        break;
      case 3: // Editar Cliente
        menu.editarCliente();
        break;
      case 4: // Visualizar Cliente
        menu.orchestatorMostrarCliente();
        break;
      case 5: // Agregar Sede
        menu.agregarsede();
        break;
      case 6: // Eliminar Sede
        menu.eliminarsede();
        break;
      case 7: // Ver Sedes
        menu.mostrarSedes();
        break;
      case 8: // Agregar Plan
        menu.orchestatorAgregarPlan();
        break;
      case 9: // Eliminar Plan
        menu.orchestatorEliminarPlan();
        break;
      case 10: // Ver Plan
        menu.orchestatorMostrarPlan();
        break;
      case 11: // Ver plantilla clientes
        menu.mostrarClientes();
        break;
      case 12: // Ordenar alfabeticamente
        menu.ordenarAlfabeticamente();
        break;
      default:
        System.out.println("Opcion invalida");
      break;
     }
    } while (num != 0);
  } 
}

