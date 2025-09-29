import java.util.Scanner;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

// ============= CLASES BASE =============
class Node {
    Object dato;
    Node siguiente;
    int prioridad;
    
    public Node(Object dato) {
        this.dato = dato;
        this.siguiente = null;
        this.prioridad = 0;
    }
    
    public Node(Object dato, int prioridad) {
        this.dato = dato;
        this.siguiente = null;
        this.prioridad = prioridad;
    }
}

// ============= CLASE PARA PEDIDOS =============
class Pedido {
    private int numeroPedido;
    private String horaLlegada;
    private String detallesPlatillos;
    private int prioridad;
    private String cliente;
    
    public Pedido(int numeroPedido, String horaLlegada, String detallesPlatillos, int prioridad, String cliente) {
        this.numeroPedido = numeroPedido;
        this.horaLlegada = horaLlegada;
        this.detallesPlatillos = detallesPlatillos;
        this.prioridad = prioridad;
        this.cliente = cliente;
    }
    
    // Getters
    public int getNumeroPedido() { return numeroPedido; }
    public String getHoraLlegada() { return horaLlegada; }
    public String getDetallesPlatillos() { return detallesPlatillos; }
    public int getPrioridad() { return prioridad; }
    public String getCliente() { return cliente; }
    
    @Override
    public String toString() {
        return String.format("Pedido #%d - Cliente: %s - Hora: %s - Prioridad: %d\n  Platillos: %s", 
                           numeroPedido, cliente, horaLlegada, prioridad, detallesPlatillos);
    }
}

// ============= NODO DEL ARBOL BINARIO =============
class NodoArbol {
    Pedido pedido;
    NodoArbol izquierdo;
    NodoArbol derecho;
    
    public NodoArbol(Pedido pedido) {
        this.pedido = pedido;
        this.izquierdo = null;
        this.derecho = null;
    }
}

// ============= ARBOL BINARIO DE BUSQUEDA PARA PEDIDOS =============
class ArbolPedidos {
    private NodoArbol raiz;
    private int totalPedidos;
    
    public ArbolPedidos() {
        raiz = null;
        totalPedidos = 0;
    }
    
    // INSERT - Insertar pedido en el árbol
    public void insertarPedido(Pedido pedido) {
        raiz = insertarRecursivo(raiz, pedido);
        totalPedidos++;
        System.out.println("Pedido agregado al sistema: " + pedido.getNumeroPedido());
    }
    
    private NodoArbol insertarRecursivo(NodoArbol nodo, Pedido pedido) {
        // Caso base: si el nodo es null, crear nuevo nodo
        if (nodo == null) {
            return new NodoArbol(pedido);
        }
        
        // Insertar según el número de pedido (criterio de ordenamiento)
        if (pedido.getNumeroPedido() < nodo.pedido.getNumeroPedido()) {
            nodo.izquierdo = insertarRecursivo(nodo.izquierdo, pedido);
        } else if (pedido.getNumeroPedido() > nodo.pedido.getNumeroPedido()) {
            nodo.derecho = insertarRecursivo(nodo.derecho, pedido);
        }
        // Si el número es igual, no insertar (evitar duplicados)
        
        return nodo;
    }
    
    // SEARCH - Buscar pedido por número
    public Pedido buscarPedido(int numeroPedido) {
        return buscarRecursivo(raiz, numeroPedido);
    }
    
    private Pedido buscarRecursivo(NodoArbol nodo, int numeroPedido) {
        // Caso base: nodo vacío o pedido encontrado
        if (nodo == null) {
            return null;
        }
        
        if (numeroPedido == nodo.pedido.getNumeroPedido()) {
            return nodo.pedido;
        }
        
        // Buscar en subárbol izquierdo o derecho
        if (numeroPedido < nodo.pedido.getNumeroPedido()) {
            return buscarRecursivo(nodo.izquierdo, numeroPedido);
        } else {
            return buscarRecursivo(nodo.derecho, numeroPedido);
        }
    }
    
    // DELETE - Eliminar pedido del árbol
    public boolean eliminarPedido(int numeroPedido) {
        int pedidosAntes = totalPedidos;
        raiz = eliminarRecursivo(raiz, numeroPedido);
        
        if (totalPedidos < pedidosAntes) {
            System.out.println("Pedido #" + numeroPedido + " eliminado del sistema");
            return true;
        } else {
            System.out.println("Pedido #" + numeroPedido + " no encontrado");
            return false;
        }
    }
    
    private NodoArbol eliminarRecursivo(NodoArbol nodo, int numeroPedido) {
        // Caso base
        if (nodo == null) {
            return null;
        }
        
        // Buscar el nodo a eliminar
        if (numeroPedido < nodo.pedido.getNumeroPedido()) {
            nodo.izquierdo = eliminarRecursivo(nodo.izquierdo, numeroPedido);
        } else if (numeroPedido > nodo.pedido.getNumeroPedido()) {
            nodo.derecho = eliminarRecursivo(nodo.derecho, numeroPedido);
        } else {
            // Nodo encontrado - eliminar
            totalPedidos--;
            
            // Caso 1: Nodo sin hijos
            if (nodo.izquierdo == null && nodo.derecho == null) {
                return null;
            }
            
            // Caso 2: Nodo con un hijo
            if (nodo.izquierdo == null) {
                return nodo.derecho;
            }
            if (nodo.derecho == null) {
                return nodo.izquierdo;
            }
            
            // Caso 3: Nodo con dos hijos
            // Encontrar el sucesor inOrden (mínimo en subárbol derecho)
            NodoArbol sucesor = encontrarMinimo(nodo.derecho);
            nodo.pedido = sucesor.pedido;
            nodo.derecho = eliminarRecursivo(nodo.derecho, sucesor.pedido.getNumeroPedido());
        }
        
        return nodo;
    }
    
    private NodoArbol encontrarMinimo(NodoArbol nodo) {
        while (nodo.izquierdo != null) {
            nodo = nodo.izquierdo;
        }
        return nodo;
    }
    
    // Recorridos del árbol
    
    // In-Orden: Obtener pedidos en orden cronológico (ordenado por número)
    public void recorridoInOrden() {
        System.out.println("=== PEDIDOS EN ORDEN CRONOLOGICO (IN-Orden) ===");
        if (raiz == null) {
            System.out.println("No hay pedidos en el sistema");
            return;
        }
        inOrdenRecursivo(raiz);
        System.out.println("Total de pedidos: " + totalPedidos);
    }
    
    private void inOrdenRecursivo(NodoArbol nodo) {
        if (nodo != null) {
            inOrdenRecursivo(nodo.izquierdo);
            System.out.println(nodo.pedido);
            System.out.println("---");
            inOrdenRecursivo(nodo.derecho);
        }
    }
    
    // Pre-Orden: Ver secuencia de inserción de pedidos
    public void recorridoPreOrden() {
        System.out.println("=== SECUENCIA DE INSERCION DE PEDIDOS (PRE-Orden) ===");
        if (raiz == null) {
            System.out.println("No hay pedidos en el sistema");
            return;
        }
        preOrdenRecursivo(raiz);
    }
    
    private void preOrdenRecursivo(NodoArbol nodo) {
        if (nodo != null) {
            System.out.println(nodo.pedido);
            System.out.println("---");
            preOrdenRecursivo(nodo.izquierdo);
            preOrdenRecursivo(nodo.derecho);
        }
    }
    
    // Post-Orden: Para generar reportes al finalizar turno
    public void recorridoPostOrden() {
        System.out.println("=== REPORTE DE FIN DE TURNO (POST-Orden) ===");
        if (raiz == null) {
            System.out.println("No hay pedidos en el sistema");
            return;
        }
        postOrdenRecursivo(raiz);
        System.out.println("RESUMEN DEL TURNO:");
        System.out.println("Total de pedidos procesados: " + totalPedidos);
    }
    
    private void postOrdenRecursivo(NodoArbol nodo) {
        if (nodo != null) {
            postOrdenRecursivo(nodo.izquierdo);
            postOrdenRecursivo(nodo.derecho);
            System.out.println(nodo.pedido);
            System.out.println("---");
        }
    }
    
    // Métodos utilitarios
    public boolean estaVacio() {
        return raiz == null;
    }
    
    public int getTotalPedidos() {
        return totalPedidos;
    }
    
    // Encontrar pedido con mayor prioridad (recorrido completo)
    public Pedido encontrarMayorPrioridad() {
        if (raiz == null) return null;
        return encontrarMayorPrioridadRecursivo(raiz, null);
    }
    
    private Pedido encontrarMayorPrioridadRecursivo(NodoArbol nodo, Pedido maxPrioridad) {
        if (nodo == null) return maxPrioridad;
        
        // Comparar prioridad actual
        if (maxPrioridad == null || nodo.pedido.getPrioridad() < maxPrioridad.getPrioridad()) {
            maxPrioridad = nodo.pedido;
        }
        
        // Recursivamente buscar en ambos subárboles
        maxPrioridad = encontrarMayorPrioridadRecursivo(nodo.izquierdo, maxPrioridad);
        maxPrioridad = encontrarMayorPrioridadRecursivo(nodo.derecho, maxPrioridad);
        
        return maxPrioridad;
    }
    
    // Mostrar estructura del árbol (para depuración)
    public void mostrarEstructura() {
        System.out.println("=== ESTRUCTURA DEL ARBOL DE PEDIDOS ===");
        if (raiz == null) {
            System.out.println("Arbol vacio");
            return;
        }
        mostrarEstructuraRecursiva(raiz, "", true);
    }
    
    private void mostrarEstructuraRecursiva(NodoArbol nodo, String prefijo, boolean esUltimo) {
        if (nodo != null) {
            System.out.println(prefijo + (esUltimo ? "└── " : "├── ") + 
                             "Pedido #" + nodo.pedido.getNumeroPedido() + 
                             " (Prioridad: " + nodo.pedido.getPrioridad() + ")");
            
            String nuevoPrefijo = prefijo + (esUltimo ? "    " : "│   ");
            
            if (nodo.izquierdo != null || nodo.derecho != null) {
                if (nodo.derecho != null) {
                    mostrarEstructuraRecursiva(nodo.derecho, nuevoPrefijo, nodo.izquierdo == null);
                }
                if (nodo.izquierdo != null) {
                    mostrarEstructuraRecursiva(nodo.izquierdo, nuevoPrefijo, true);
                }
            }
        }
    }
}


// Clase Tarea con estructura jerárquica para recursividad
class Tarea {
    private String nombre;
    private String descripcion;
    private int tiempoEstimado; // en minutos
    private int prioridad; // 1=alta, 2=media, 3=baja
    private Date fechaLimite;
    private String empleadoAsignado;
    private List<Tarea> subtareas;
    private String id;
    
    public Tarea(String id, String nombre, String descripcion, int tiempoEstimado, int prioridad) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tiempoEstimado = tiempoEstimado;
        this.prioridad = prioridad;
        this.subtareas = new ArrayList<>();
        this.fechaLimite = new Date();
        this.empleadoAsignado = "";
    }
    
    // Getters y setters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public int getTiempoEstimado() { return tiempoEstimado; }
    public int getPrioridad() { return prioridad; }
    public Date getFechaLimite() { return fechaLimite; }
    public String getEmpleadoAsignado() { return empleadoAsignado; }
    public List<Tarea> getSubtareas() { return subtareas; }
    
    public void setEmpleadoAsignado(String empleado) { this.empleadoAsignado = empleado; }
    public void setFechaLimite(Date fecha) { this.fechaLimite = fecha; }
    
    public void agregarSubtarea(Tarea subtarea) {
        subtareas.add(subtarea);
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s (Tiempo: %d min, Prioridad: %d)", 
                           id, nombre, tiempoEstimado, prioridad);
    }
}

// Clase Empleado para HashMap
class Empleado {
    private String id;
    private String nombre;
    private String puesto;
    private List<String> tareasAsignadas;
    
    public Empleado(String id, String nombre, String puesto) {
        this.id = id;
        this.nombre = nombre;
        this.puesto = puesto;
        this.tareasAsignadas = new ArrayList<>();
    }
    
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getPuesto() { return puesto; }
    public List<String> getTareasAsignadas() { return tareasAsignadas; }
    
    public void asignarTarea(String tareaId) {
        tareasAsignadas.add(tareaId);
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s (%s)", id, nombre, puesto);
    }
}

// Nodo para grafo de dependencias
class NodoGrafo {
    String tareaId;
    List<NodoGrafo> dependencias;
    boolean visitado;
    boolean enProceso;
    
    public NodoGrafo(String tareaId) {
        this.tareaId = tareaId;
        this.dependencias = new ArrayList<>();
        this.visitado = false;
        this.enProceso = false;
    }
}

// ============= GESTOR DE TAREAS CON ALGORITMOS AVANZADOS =============
class GestorTareasAvanzado {
    private HashMap<String, Tarea> tablaTareas;
    private HashMap<String, Empleado> tablaEmpleados;
    private HashMap<String, NodoGrafo> grafoTareas;
    private List<Tarea> tareasPrincipales;
    
    public GestorTareasAvanzado() {
        tablaTareas = new HashMap<>();
        tablaEmpleados = new HashMap<>();
        grafoTareas = new HashMap<>();
        tareasPrincipales = new ArrayList<>();
        inicializarDatos();
    }
    
    private void inicializarDatos() {
        // Crear empleados
        agregarEmpleado(new Empleado("EMP001", "Juan Carlos", "Taquero Principal"));
        agregarEmpleado(new Empleado("EMP002", "María Elena", "Ayudante de Cocina"));
        agregarEmpleado(new Empleado("EMP003", "Pedro López", "Cajero"));
        agregarEmpleado(new Empleado("EMP004", "Ana Sofía", "Limpieza"));
        
        // Crear tareas principales con subtareas
        crearTareasIniciales();
    }
    
    private void crearTareasIniciales() {
        // Tarea principal: Preparar Tacos de Carnitas
        Tarea tacosCarnitas = new Tarea("T001", "Preparar Tacos de Carnitas", 
                                       "Proceso completo de preparación", 0, 1);
        
        // Subtareas
        Tarea cocinarCarne = new Tarea("T001.1", "Cocinar carnitas", 
                                      "Cocinar la carne de cerdo", 45, 1);
        Tarea prepararTortillas = new Tarea("T001.2", "Calentar tortillas", 
                                           "Calentar tortillas en comal", 10, 2);
        Tarea cortarCebolla = new Tarea("T001.3", "Cortar cebolla y cilantro", 
                                       "Preparar guarniciones", 8, 2);
        Tarea prepararSalsa = new Tarea("T001.4", "Preparar salsa verde", 
                                       "Hacer salsa fresca", 15, 2);
        
        tacosCarnitas.agregarSubtarea(cocinarCarne);
        tacosCarnitas.agregarSubtarea(prepararTortillas);
        tacosCarnitas.agregarSubtarea(cortarCebolla);
        tacosCarnitas.agregarSubtarea(prepararSalsa);
        
        // Agregar subtarea a subtarea (recursividad profunda)
        Tarea lavarCarne = new Tarea("T001.1.1", "Lavar y sazonar carne", 
                                    "Preparación inicial", 10, 1);
        cocinarCarne.agregarSubtarea(lavarCarne);
        
        // Agregar todas las tareas
        tablaTareas.put(tacosCarnitas.getId(), tacosCarnitas);
        tablaTareas.put(cocinarCarne.getId(), cocinarCarne);
        tablaTareas.put(prepararTortillas.getId(), prepararTortillas);
        tablaTareas.put(cortarCebolla.getId(), cortarCebolla);
        tablaTareas.put(prepararSalsa.getId(), prepararSalsa);
        tablaTareas.put(lavarCarne.getId(), lavarCarne);
        
        tareasPrincipales.add(tacosCarnitas);
        
        // Crear dependencias en el grafo
        crearDependenciasGrafo();
    }
    
    private void crearDependenciasGrafo() {
        // Crear nodos del grafo
        for (String tareaId : tablaTareas.keySet()) {
            grafoTareas.put(tareaId, new NodoGrafo(tareaId));
        }
        
        // Establecer dependencias (T001.1.1 debe ejecutarse antes que T001.1)
        grafoTareas.get("T001.1").dependencias.add(grafoTareas.get("T001.1.1"));
        // T001 depende de todas sus subtareas
        grafoTareas.get("T001").dependencias.add(grafoTareas.get("T001.1"));
        grafoTareas.get("T001").dependencias.add(grafoTareas.get("T001.2"));
        grafoTareas.get("T001").dependencias.add(grafoTareas.get("T001.3"));
        grafoTareas.get("T001").dependencias.add(grafoTareas.get("T001.4"));
    }
    
    public void agregarEmpleado(Empleado empleado) {
        tablaEmpleados.put(empleado.getId(), empleado);
    }
    
    // ============= MÉTODOS RECURSIVOS =============
    
    // Calcular tiempo total de una tarea incluyendo subtareas (RECURSIVIDAD)
    public int calcularTiempoTotal(Tarea tarea) {
        int tiempoTotal = tarea.getTiempoEstimado();
        
        // Caso base: si no hay subtareas, retorna el tiempo de la tarea
        if (tarea.getSubtareas().isEmpty()) {
            return tiempoTotal;
        }
        
        // Caso recursivo: suma el tiempo de todas las subtareas
        for (Tarea subtarea : tarea.getSubtareas()) {
            tiempoTotal += calcularTiempoTotal(subtarea);
        }
        
        return tiempoTotal;
    }
    
    // Contar total de tareas y subtareas (RECURSIVIDAD)
    public int contarTareas(Tarea tarea) {
        int contador = 1; // Contar la tarea actual
        
        // Recursivamente contar subtareas
        for (Tarea subtarea : tarea.getSubtareas()) {
            contador += contarTareas(subtarea);
        }
        
        return contador;
    }
    
    // Mostrar estructura jerárquica (RECURSIVIDAD)
    public void mostrarEstructuraJerarquica(Tarea tarea, String indentacion) {
        System.out.println(indentacion + "├── " + tarea);
        
        for (Tarea subtarea : tarea.getSubtareas()) {
            mostrarEstructuraJerarquica(subtarea, indentacion + "│   ");
        }
    }
    
    // ============= ALGORITMOS DIVIDE Y VENCERÁS =============
    
    // Merge Sort para ordenar tareas por prioridad
    public List<Tarea> mergeSort(List<Tarea> tareas, String criterio) {
        if (tareas.size() <= 1) {
            return tareas;
        }
        
        int medio = tareas.size() / 2;
        List<Tarea> izquierda = new ArrayList<>(tareas.subList(0, medio));
        List<Tarea> derecha = new ArrayList<>(tareas.subList(medio, tareas.size()));
        
        izquierda = mergeSort(izquierda, criterio);
        derecha = mergeSort(derecha, criterio);
        
        return merge(izquierda, derecha, criterio);
    }
    
    private List<Tarea> merge(List<Tarea> izq, List<Tarea> der, String criterio) {
        List<Tarea> resultado = new ArrayList<>();
        int i = 0, j = 0;
        
        while (i < izq.size() && j < der.size()) {
            boolean condicion = false;
            
            switch (criterio) {
                case "prioridad":
                    condicion = izq.get(i).getPrioridad() <= der.get(j).getPrioridad();
                    break;
                case "tiempo":
                    condicion = izq.get(i).getTiempoEstimado() <= der.get(j).getTiempoEstimado();
                    break;
            }
            
            if (condicion) {
                resultado.add(izq.get(i++));
            } else {
                resultado.add(der.get(j++));
            }
        }
        
        while (i < izq.size()) resultado.add(izq.get(i++));
        while (j < der.size()) resultado.add(der.get(j++));
        
        return resultado;
    }
    
    // Quick Sort para ordenamiento alternativo
    public void quickSort(List<Tarea> tareas, int inicio, int fin, String criterio) {
        if (inicio < fin) {
            int pivote = particionar(tareas, inicio, fin, criterio);
            quickSort(tareas, inicio, pivote - 1, criterio);
            quickSort(tareas, pivote + 1, fin, criterio);
        }
    }
    
    private int particionar(List<Tarea> tareas, int inicio, int fin, String criterio) {
        Tarea pivote = tareas.get(fin);
        int i = inicio - 1;
        
        for (int j = inicio; j < fin; j++) {
            boolean condicion = false;
            
            switch (criterio) {
                case "prioridad":
                    condicion = tareas.get(j).getPrioridad() <= pivote.getPrioridad();
                    break;
                case "tiempo":
                    condicion = tareas.get(j).getTiempoEstimado() <= pivote.getTiempoEstimado();
                    break;
            }
            
            if (condicion) {
                i++;
                Collections.swap(tareas, i, j);
            }
        }
        
        Collections.swap(tareas, i + 1, fin);
        return i + 1;
    }
    
    // Balancear carga de trabajo entre empleados (DIVIDE Y VENCERÁS)
    public void balancearCargaTrabajo(List<Tarea> tareas) {
        List<Empleado> empleados = new ArrayList<>(tablaEmpleados.values());
        balancearRecursivo(tareas, empleados, 0);
    }
    
    private void balancearRecursivo(List<Tarea> tareas, List<Empleado> empleados, int indiceEmpleado) {
        if (tareas.isEmpty()) return;
        
        if (tareas.size() == 1) {
            // Asignar única tarea al empleado actual
            Empleado empleado = empleados.get(indiceEmpleado % empleados.size());
            tareas.get(0).setEmpleadoAsignado(empleado.getNombre());
            empleado.asignarTarea(tareas.get(0).getId());
            return;
        }
        
        // Dividir tareas por la mitad
        int medio = tareas.size() / 2;
        List<Tarea> primera = tareas.subList(0, medio);
        List<Tarea> segunda = tareas.subList(medio, tareas.size());
        
        // Asignar recursivamente a diferentes empleados
        balancearRecursivo(primera, empleados, indiceEmpleado);
        balancearRecursivo(segunda, empleados, (indiceEmpleado + 1) % empleados.size());
    }
    
    // ============= MÉTODOS DE BÚSQUEDA =============
    
    // Búsqueda binaria (requiere lista ordenada)
    public Tarea busquedaBinaria(List<Tarea> tareas, int tiempoBuscado) {
        int inicio = 0;
        int fin = tareas.size() - 1;
        
        while (inicio <= fin) {
            int medio = (inicio + fin) / 2;
            int tiempoMedio = tareas.get(medio).getTiempoEstimado();
            
            if (tiempoMedio == tiempoBuscado) {
                return tareas.get(medio);
            } else if (tiempoMedio < tiempoBuscado) {
                inicio = medio + 1;
            } else {
                fin = medio - 1;
            }
        }
        
        return null;
    }
    
    // Búsqueda secuencial
    public List<Tarea> busquedaSecuencial(String criterio, String valor) {
        List<Tarea> resultados = new ArrayList<>();
        
        for (Tarea tarea : tablaTareas.values()) {
            switch (criterio) {
                case "nombre":
                    if (tarea.getNombre().toLowerCase().contains(valor.toLowerCase())) {
                        resultados.add(tarea);
                    }
                    break;
                case "empleado":
                    if (tarea.getEmpleadoAsignado().toLowerCase().contains(valor.toLowerCase())) {
                        resultados.add(tarea);
                    }
                    break;
            }
        }
        
        return resultados;
    }
    
    // ============= ALGORITMOS DE GRAFOS =============
    
    // DFS para recorrer dependencias
    public void dfsRecorrido(String tareaId, Set<String> visitados) {
        if (visitados.contains(tareaId)) return;
        
        visitados.add(tareaId);
        System.out.println("Visitando tarea: " + tablaTareas.get(tareaId).getNombre());
        
        NodoGrafo nodo = grafoTareas.get(tareaId);
        if (nodo != null) {
            for (NodoGrafo dependencia : nodo.dependencias) {
                dfsRecorrido(dependencia.tareaId, visitados);
            }
        }
    }
    
    // Orden topológico para secuencia correcta de ejecución
    public List<String> ordenTopologico() {
        List<String> resultado = new ArrayList<>();
        Set<String> visitadosPermanente = new HashSet<>();
        Set<String> visitadosTemporales = new HashSet<>();
        
        for (String tareaId : grafoTareas.keySet()) {
            if (!visitadosPermanente.contains(tareaId)) {
                if (dfsTopologico(tareaId, visitadosPermanente, visitadosTemporales, resultado)) {
                    System.out.println("¡CICLO DETECTADO! No es posible generar orden topológico.");
                    return new ArrayList<>();
                }
            }
        }
        
        Collections.reverse(resultado);
        return resultado;
    }
    
    private boolean dfsTopologico(String tareaId, Set<String> visitadosPerm, 
                                 Set<String> visitadosTemp, List<String> resultado) {
        if (visitadosTemp.contains(tareaId)) return true; // Ciclo detectado
        if (visitadosPerm.contains(tareaId)) return false;
        
        visitadosTemp.add(tareaId);
        
        NodoGrafo nodo = grafoTareas.get(tareaId);
        if (nodo != null) {
            for (NodoGrafo dependencia : nodo.dependencias) {
                if (dfsTopologico(dependencia.tareaId, visitadosPerm, visitadosTemp, resultado)) {
                    return true;
                }
            }
        }
        
        visitadosTemp.remove(tareaId);
        visitadosPerm.add(tareaId);
        resultado.add(tareaId);
        
        return false;
    }
    
    // ============= MÉTODOS PÚBLICOS PARA EL MENÚ =============
    
    public void mostrarCalculosRecursivos() {
        System.out.println("=== CÁLCULOS RECURSIVOS ===");
        for (Tarea tarea : tareasPrincipales) {
            int tiempoTotal = calcularTiempoTotal(tarea);
            int totalTareas = contarTareas(tarea);
            
            System.out.println("\nTarea: " + tarea.getNombre());
            System.out.println("Tiempo total estimado: " + tiempoTotal + " minutos");
            System.out.println("Total de tareas/subtareas: " + totalTareas);
            System.out.println("\nEstructura jerárquica:");
            mostrarEstructuraJerarquica(tarea, "");
        }
    }
    
    public void mostrarOrdenamiento() {
        List<Tarea> todasTareas = new ArrayList<>(tablaTareas.values());
        
        System.out.println("=== ORDENAMIENTO DE TAREAS ===");
        System.out.println("\n--- Ordenamiento por Prioridad (Merge Sort) ---");
        List<Tarea> ordenadoPorPrioridad = mergeSort(new ArrayList<>(todasTareas), "prioridad");
        ordenadoPorPrioridad.forEach(System.out::println);
        
        System.out.println("\n--- Ordenamiento por Tiempo (Quick Sort) ---");
        List<Tarea> ordenadoPorTiempo = new ArrayList<>(todasTareas);
        quickSort(ordenadoPorTiempo, 0, ordenadoPorTiempo.size() - 1, "tiempo");
        ordenadoPorTiempo.forEach(System.out::println);
    }
    
    public void mostrarBalanceoTrabajo() {
        System.out.println("=== BALANCEO DE CARGA DE TRABAJO ===");
        List<Tarea> tareas = new ArrayList<>(tablaTareas.values());
        balancearCargaTrabajo(tareas);
        
        System.out.println("\nAsignación de tareas por empleado:");
        for (Empleado empleado : tablaEmpleados.values()) {
            System.out.println("\n" + empleado);
            System.out.println("Tareas asignadas:");
            for (String tareaId : empleado.getTareasAsignadas()) {
                Tarea tarea = tablaTareas.get(tareaId);
                if (tarea != null) {
                    System.out.println("  - " + tarea);
                }
            }
        }
    }
    
    public void mostrarBusquedas(Scanner scanner) {
        System.out.println("=== BÚSQUEDAS ===");
        System.out.println("1. Búsqueda binaria por tiempo (requiere ordenamiento)");
        System.out.println("2. Búsqueda secuencial por nombre");
        System.out.println("3. Búsqueda secuencial por empleado");
        System.out.print("Seleccione tipo de búsqueda: ");
        
        int opcion = scanner.nextInt();
        scanner.nextLine();
        
        switch (opcion) {
            case 1:
                System.out.print("Ingrese tiempo a buscar (minutos): ");
                int tiempo = scanner.nextInt();
                scanner.nextLine();
                
                List<Tarea> tareasOrdenadas = new ArrayList<>(tablaTareas.values());
                quickSort(tareasOrdenadas, 0, tareasOrdenadas.size() - 1, "tiempo");
                
                Tarea encontrada = busquedaBinaria(tareasOrdenadas, tiempo);
                if (encontrada != null) {
                    System.out.println("Tarea encontrada: " + encontrada);
                } else {
                    System.out.println("No se encontró tarea con ese tiempo exacto.");
                }
                break;
                
            case 2:
                System.out.print("Ingrese nombre o parte del nombre: ");
                String nombre = scanner.nextLine();
                List<Tarea> resultadosNombre = busquedaSecuencial("nombre", nombre);
                
                if (resultadosNombre.isEmpty()) {
                    System.out.println("No se encontraron tareas con ese nombre.");
                } else {
                    System.out.println("Tareas encontradas:");
                    resultadosNombre.forEach(System.out::println);
                }
                break;
                
            case 3:
                System.out.print("Ingrese nombre del empleado: ");
                String empleado = scanner.nextLine();
                List<Tarea> resultadosEmpleado = busquedaSecuencial("empleado", empleado);
                
                if (resultadosEmpleado.isEmpty()) {
                    System.out.println("No se encontraron tareas para ese empleado.");
                } else {
                    System.out.println("Tareas del empleado:");
                    resultadosEmpleado.forEach(System.out::println);
                }
                break;
        }
    }
    
    public void mostrarGrafoDependencias() {
        System.out.println("=== ANÁLISIS DE DEPENDENCIAS (GRAFOS) ===");
        
        System.out.println("\n--- Recorrido DFS ---");
        Set<String> visitados = new HashSet<>();
        dfsRecorrido("T001", visitados);
        
        System.out.println("\n--- Orden Topológico (Orden de Ejecución) ---");
        List<String> orden = ordenTopologico();
        
        if (!orden.isEmpty()) {
            System.out.println("Secuencia correcta de ejecución:");
            for (int i = 0; i < orden.size(); i++) {
                Tarea tarea = tablaTareas.get(orden.get(i));
                System.out.println((i + 1) + ". " + tarea.getNombre() + " (" + orden.get(i) + ")");
            }
        }
    }
    
    public void mostrarHashMaps() {
        System.out.println("=== ACCESO RÁPIDO CON HASHMAPS ===");
        
        System.out.println("\n--- Todas las Tareas ---");
        for (Tarea tarea : tablaTareas.values()) {
            System.out.println(tarea + " | Empleado: " + 
                             (tarea.getEmpleadoAsignado().isEmpty() ? "Sin asignar" : tarea.getEmpleadoAsignado()));
        }
        
        System.out.println("\n--- Todos los Empleados ---");
        for (Empleado empleado : tablaEmpleados.values()) {
            System.out.println(empleado + " | Tareas: " + empleado.getTareasAsignadas().size());
        }
    }
    
    // Acceso directo O(1) usando HashMap
    public Tarea obtenerTareaPorId(String id) {
        return tablaTareas.get(id);
    }
    
    public Empleado obtenerEmpleadoPorId(String id) {
        return tablaEmpleados.get(id);
    }
}

// ============= CLASES ORIGINALES (mantenidas) =============
class LinkedList {
    private Node cabeza;
    private int tamaño;
    
    public LinkedList() {
        cabeza = null;
        tamaño = 0;
    }
    
    public void insertarAlInicio(Object dato) {
        Node nuevo = new Node(dato);
        nuevo.siguiente = cabeza;
        cabeza = nuevo;
        tamaño++;
    }
    
    public void insertarAlFinal(Object dato) {
        Node nuevo = new Node(dato);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Node actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
        tamaño++;
    }
    
    public Object eliminarCabeza() {
        if (cabeza == null) return null;
        Object dato = cabeza.dato;
        cabeza = cabeza.siguiente;
        tamaño--;
        return dato;
    }
    
    public Object eliminarUltimo() {
        if (cabeza == null) return null;
        if (cabeza.siguiente == null) {
            Object dato = cabeza.dato;
            cabeza = null;
            tamaño--;
            return dato;
        }
        Node actual = cabeza;
        while (actual.siguiente.siguiente != null) {
            actual = actual.siguiente;
        }
        Object dato = actual.siguiente.dato;
        actual.siguiente = null;
        tamaño--;
        return dato;
    }
    
    public Object obtenerCabeza() {
        return (cabeza != null) ? cabeza.dato : null;
    }
    
    public Object obtenerUltimo() {
        if (cabeza == null) return null;
        Node actual = cabeza;
        while (actual.siguiente != null) {
            actual = actual.siguiente;
        }
        return actual.dato;
    }
    
    public Object obtenerPorPosicion(int posicion) {
        if (posicion < 1 || posicion > tamaño) {
            return null;
        }
        
        Node actual = cabeza;
        for (int i = 1; i < posicion && actual != null; i++) {
            actual = actual.siguiente;
        }
        
        return (actual != null) ? actual.dato : null;
    }
    
    public boolean estaVacia() {
        return cabeza == null;
    }
    
    public void mostrar() {
        Node actual = cabeza;
        int contador = 1;
        while (actual != null) {
            System.out.println(contador + ". " + actual.dato);
            actual = actual.siguiente;
            contador++;
        }
        if (contador == 1) System.out.println("Lista vacía");
    }
    
    public boolean eliminarDato(Object dato) {
        if (cabeza == null) return false;
        if (cabeza.dato.equals(dato)) {
            cabeza = cabeza.siguiente;
            tamaño--;
            return true;
        }
        Node actual = cabeza;
        while (actual.siguiente != null) {
            if (actual.siguiente.dato.equals(dato)) {
                actual.siguiente = actual.siguiente.siguiente;
                tamaño--;
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }
    
    public int getTamaño() {
        return tamaño;
    }
    
    public Node getCabeza() {
        return cabeza;
    }
}

class Pila {
    private LinkedList lista;
    
    public Pila() {
        lista = new LinkedList();
    }
    
    public void push(Object dato) {
        lista.insertarAlInicio(dato);
        System.out.println("Elemento agregado al tope de la pila: " + dato);
    }
    
    public Object pop() {
        if (estaVacia()) {
            System.out.println("Error: La pila esta vacia");
            return null;
        }
        Object elemento = lista.eliminarCabeza();
        System.out.println("Elemento removido del tope: " + elemento);
        return elemento;
    }
    
    public Object peek() {
        if (estaVacia()) {
            System.out.println("Error: La pila esta vacia");
            return null;
        }
        return lista.obtenerCabeza();
    }
    
    public boolean estaVacia() {
        return lista.estaVacia();
    }
    
    public void mostrar() {
        System.out.println("=== CONTENIDO DE LA PILA ===");
        lista.mostrar();
    }
}

class Cola {
    private LinkedList lista;
    
    public Cola() {
        lista = new LinkedList();
    }
    
    public void enqueue(Object dato) {
        lista.insertarAlFinal(dato);
        System.out.println("Cliente agregado a la cola: " + dato);
    }
    
    public Object dequeue() {
        if (estaVacia()) {
            System.out.println("Error: No hay clientes en la cola");
            return null;
        }
        Object elemento = lista.eliminarCabeza();
        System.out.println("Cliente atendido: " + elemento);
        return elemento;
    }
    
    public Object front() {
        if (estaVacia()) {
            System.out.println("Error: No hay clientes en la cola");
            return null;
        }
        return lista.obtenerCabeza();
    }
    
    public boolean estaVacia() {
        return lista.estaVacia();
    }
    
    public void mostrar() {
        System.out.println("=== COLA DE CLIENTES ===");
        lista.mostrar();
    }
}

class ColaPrioridad {
    private Node cabeza;
    private int tamaño;
    
    public ColaPrioridad() {
        cabeza = null;
        tamaño = 0;
    }
    
    public void enqueue(Object dato, int prioridad) {
        Node nuevo = new Node(dato, prioridad);
        
        if (cabeza == null || prioridad < cabeza.prioridad) {
            nuevo.siguiente = cabeza;
            cabeza = nuevo;
        } else {
            Node actual = cabeza;
            while (actual.siguiente != null && actual.siguiente.prioridad <= prioridad) {
                actual = actual.siguiente;
            }
            nuevo.siguiente = actual.siguiente;
            actual.siguiente = nuevo;
        }
        tamaño++;
        System.out.println("Ingrediente agregado con prioridad " + prioridad + ": " + dato);
    }
    
    public Object dequeue() {
        if (estaVacia()) {
            System.out.println("Error: No hay ingredientes en la cola de prioridad");
            return null;
        }
        Object elemento = cabeza.dato;
        int prioridad = cabeza.prioridad;
        cabeza = cabeza.siguiente;
        tamaño--;
        System.out.println("Ingrediente procesado: " + elemento + " (Prioridad: " + prioridad + ")");
        return elemento;
    }
    
    public Object peek() {
        if (estaVacia()) {
            System.out.println("Error: No hay ingredientes en la cola de prioridad");
            return null;
        }
        return cabeza.dato + " (Prioridad: " + cabeza.prioridad + ")";
    }
    
    public boolean estaVacia() {
        return cabeza == null;
    }
    
    public void mostrar() {
        System.out.println("=== COLA DE INGREDIENTES POR PRIORIDAD ===");
        System.out.println("(Prioridad 1: 1-7 dias, Prioridad 2: 8-30 dias, Prioridad 3: 1-12 meses)");
        
        if (cabeza == null) {
            System.out.println("Lista vacia");
            return;
        }
        
        Node actual = cabeza;
        int contador = 1;
        while (actual != null) {
            System.out.println(contador + ". " + actual.dato + " (Prioridad: " + actual.prioridad + ")");
            actual = actual.siguiente;
            contador++;
        }
    }
}

class ElementoMenu {
    private String nombre;
    private String descripcion;
    private double precio;
    
    public ElementoMenu(String nombre, String descripcion, double precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }
    
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public double getPrecio() { return precio; }
    
    @Override
    public String toString() {
        return nombre + " - $" + precio;
    }
    
    public String detalles() {
        return "=== " + nombre + " ===" +
               "\nDescripción: " + descripcion +
               "\nPrecio: $" + precio;
    }
}

class ListaMenu {
    private LinkedList lista;
    
    public ListaMenu() {
        lista = new LinkedList();
        inicializarMenu();
    }
    
    private void inicializarMenu() {
        agregar(new ElementoMenu("Taco de Carnitas", 
                "Delicioso taco de carnitas con cebolla, cilantro y salsa verde", 25.0));
        agregar(new ElementoMenu("Taco de Pollo", 
                "Taco de pollo a la plancha con pico de gallo y salsa roja", 20.0));
        agregar(new ElementoMenu("Taco de Pastor", 
                "Taco al pastor con piña, cebolla, cilantro y salsa", 22.0));
        agregar(new ElementoMenu("Quesadilla", 
                "Quesadilla de queso Oaxaca con opción de agregar carne", 30.0));
        agregar(new ElementoMenu("Agua de Horchata", 
                "Refrescante agua de horchata casera", 15.0));
    }
    
    public void agregar(ElementoMenu elemento) {
        lista.insertarAlFinal(elemento);
    }
    
    public ElementoMenu buscar(int posicion) {
        if (posicion < 1 || posicion > lista.getTamaño()) {
            return null;
        }
        
        Object elemento = lista.obtenerPorPosicion(posicion);
        return (ElementoMenu) elemento;
    }
    
    public boolean eliminar(String nombre) {
        return lista.eliminarDato(nombre);
    }
    
    public void mostrarMenu() {
        System.out.println("=== MENÚ DE LA TAQUERÍA ===");
        lista.mostrar();
    }
    
    public void mostrarDetalles(int opcion) {
        ElementoMenu elemento = buscar(opcion);
        if (elemento != null) {
            System.out.println(elemento.detalles());
        } else {
            System.out.println("Error: Opcion no valida");
        }
    }
    
    public boolean estaVacia() {
        return lista.estaVacia();
    }
}

// ============= CLASE PRINCIPAL EXTENDIDA =============
public class TaqueriaFinal {
    private static Scanner scanner = new Scanner(System.in);
    
    // Estructuras originales del avance del proyecto
    private Cola colaClientes;
    private Pila pilaPlatos;
    private ColaPrioridad colaIngredientes;
    private ListaMenu menu;
    
    // Para las funcionalidades avanzadas del proyecto final
    private GestorTareasAvanzado gestorTareas;
    private ArbolPedidos arbolPedidos;
    
    public TaqueriaFinal() {
        colaClientes = new Cola();
        pilaPlatos = new Pila();
        colaIngredientes = new ColaPrioridad();
        menu = new ListaMenu();
        gestorTareas = new GestorTareasAvanzado();
        arbolPedidos = new ArbolPedidos();
        
        // Inicializar con algunos pedidos de ejemplo
        inicializarPedidosEjemplo();
    }
    
    // ============= INICIALIZACION DE DATOS DE EJEMPLO =============
    
    private void inicializarPedidosEjemplo() {
        // Crear algunos pedidos de ejemplo
        arbolPedidos.insertarPedido(new Pedido(105, "14:30", "2 Tacos de Carnitas, 1 Agua de Horchata", 2, "María García"));
        arbolPedidos.insertarPedido(new Pedido(103, "14:15", "1 Quesadilla de Pollo", 1, "Juan Pérez"));
        arbolPedidos.insertarPedido(new Pedido(108, "14:45", "3 Tacos al Pastor, 1 Refresco", 3, "Carlos López"));
        arbolPedidos.insertarPedido(new Pedido(102, "14:10", "1 Taco de Pollo", 1, "Ana Rodríguez"));
        arbolPedidos.insertarPedido(new Pedido(107, "14:40", "2 Quesadillas, 2 Aguas de Horchata", 2, "Luis Martínez"));
    }
    
    // ============= METODOS ORIGINALES (mantenidos) =============
    
    private void agregarCliente() {
        System.out.print("Ingrese el nombre del cliente: ");
        String nombreCliente = scanner.nextLine();
        colaClientes.enqueue("Cliente: " + nombreCliente);
        System.out.println("Cliente agregado a la fila de espera.");
    }
    
    private void atenderCliente() {
        if (colaClientes.estaVacia()) {
            System.out.println("No hay clientes esperando.");
            return;
        }
        
        Object cliente = colaClientes.front();
        System.out.println("Atendiendo a: " + cliente);
        System.out.print("Presione Enter para completar el servicio...");
        scanner.nextLine();
        colaClientes.dequeue();
    }
    
    private void verProximoCliente() {
        Object siguiente = colaClientes.front();
        if (siguiente != null) {
            System.out.println("Próximo cliente a atender: " + siguiente);
        }
    }
    
    private void mostrarColaClientes() {
        colaClientes.mostrar();
    }
    
    private void apilarPlatoSucio() {
        System.out.print("Ingrese el número del plato sucio: ");
        String numeroPlato = scanner.nextLine();
        pilaPlatos.push("Plato #" + numeroPlato);
        System.out.println("Plato apilado en la zona de lavado.");
    }
    
    private void lavarPlato() {
        if (pilaPlatos.estaVacia()) {
            System.out.println("No hay platos para lavar.");
            return;
        }
        
        Object plato = pilaPlatos.peek();
        System.out.println("Lavando " + plato + "...");
        System.out.print("Presione Enter para terminar de lavar...");
        scanner.nextLine();
        pilaPlatos.pop();
        System.out.println("Plato limpio y listo para usar!");
    }
    
    private void verTopePlatos() {
        Object tope = pilaPlatos.peek();
        if (tope != null) {
            System.out.println("Próximo plato a lavar: " + tope);
        }
    }
    
    private void mostrarPilaPlatos() {
        pilaPlatos.mostrar();
    }
    
    private void agregarIngrediente() {
        System.out.print("Ingrese el nombre del ingrediente: ");
        String ingrediente = scanner.nextLine();
        
        System.out.println("Seleccione el nivel de prioridad:");
        System.out.println("1. Alta prioridad (1-7 días para caducar)");
        System.out.println("2. Prioridad media (8-30 días para caducar)");
        System.out.println("3. Prioridad baja (1-12 meses para caducar)");
        System.out.print("Opción: ");
        
        int prioridad = scanner.nextInt();
        scanner.nextLine();
        
        if (prioridad >= 1 && prioridad <= 3) {
            colaIngredientes.enqueue(ingrediente, prioridad);
        } else {
            System.out.println("Error: Prioridad debe ser 1, 2 o 3");
        }
    }
    
    private void procesarIngrediente() {
        if (colaIngredientes.estaVacia()) {
            System.out.println("No hay ingredientes para procesar.");
            return;
        }
        
        Object ingrediente = colaIngredientes.peek();
        System.out.println("Procesando ingrediente: " + ingrediente);
        System.out.print("Presione Enter para confirmar uso...");
        scanner.nextLine();
        colaIngredientes.dequeue();
        System.out.println("Ingrediente utilizado correctamente.");
    }
    
    private void verProximoIngrediente() {
        Object siguiente = colaIngredientes.peek();
        if (siguiente != null) {
            System.out.println("Próximo ingrediente a usar: " + siguiente);
        }
    }
    
    private void mostrarColaIngredientes() {
        colaIngredientes.mostrar();
    }
    
    private void mostrarMenu() {
        menu.mostrarMenu();
    }
    
    private void verDetallesMenu() {
        System.out.println("=== MENÚ DETALLADO ===");
        menu.mostrarMenu();
        System.out.print("Seleccione un número para ver detalles: ");
        
        try {
            int opcion = scanner.nextInt();
            scanner.nextLine();
            menu.mostrarDetalles(opcion);
        } catch (Exception e) {
            System.out.println("Error: Ingrese un numero valido");
            scanner.nextLine();
        }
    }
    
    private void mostrarEstadoGeneral() {
        System.out.println("\n==================================================");
        System.out.println("           ESTADO GENERAL DE LA TAQUERIA");
        System.out.println("==================================================");
        
        System.out.println("CLIENTES:");
        if (colaClientes.estaVacia()) {
            System.out.println("   No hay clientes esperando");
        } else {
            Object siguiente = colaClientes.front();
            System.out.println("   Proximo a atender: " + siguiente);
        }
        
        System.out.println("\nPLATOS:");
        if (pilaPlatos.estaVacia()) {
            System.out.println("   No hay platos sucios");
        } else {
            Object tope = pilaPlatos.peek();
            System.out.println("   Proximo a lavar: " + tope);
        }
        
        System.out.println("\nINGREDIENTES:");
        if (colaIngredientes.estaVacia()) {
            System.out.println("   No hay ingredientes registrados");
        } else {
            Object prioritario = colaIngredientes.peek();
            System.out.println("   Proximo a usar: " + prioritario);
        }
        
        System.out.println("==================================================");
    }
    
    // ============= MENÚ PRINCIPAL EXTENDIDO =============
    
    public void mostrarMenuPrincipal() {
        int opcion;
        
        do {
            System.out.println("\n============================================");
            System.out.println("   SISTEMA DE GESTION AVANZADO - TAQUERIA");
            System.out.println("============================================");
            System.out.println("  1. Gestion de Clientes (Cola-FIFO)");
            System.out.println("  2. Gestion de Platos (Pila-LIFO)");
            System.out.println("  3. Gestion de Ingredientes (Cola Prioridad)");
            System.out.println("  4. Arbol Binario de Pedidos");
            System.out.println("  5. Menu de la Taqueria");
            System.out.println("  6. Analisis Recursivo de Tareas");
            System.out.println("  7. Divide y Venceras - Ordenamiento");
            System.out.println("  8. Balanceo de Carga de Trabajo");
            System.out.println("  9. Acceso Rapido con HashMap");
            System.out.println("  10. Busquedas Avanzadas");
            System.out.println("  11. Analisis de Dependencias (Grafos)");
            System.out.println("  12. Estado General del Sistema");
            System.out.println("  13. Demostracion Completa de Algoritmos");
            System.out.println("  14. Salir del sistema");
            System.out.println("============================================");
            System.out.print("Seleccione una opcion: ");
            
            try {
                opcion = scanner.nextInt();
                scanner.nextLine();
                
                switch (opcion) {
                    case 1: menuGestionClientes(); break;
                    case 2: menuGestionPlatos(); break;
                    case 3: menuGestionIngredientes(); break;
                    case 4: menuArbolPedidos(); break;
                    case 5: menuGestionMenu(); break;
                    case 6: gestorTareas.mostrarCalculosRecursivos(); break;
                    case 7: gestorTareas.mostrarOrdenamiento(); break;
                    case 8: gestorTareas.mostrarBalanceoTrabajo(); break;
                    case 9: gestorTareas.mostrarHashMaps(); break;
                    case 10: gestorTareas.mostrarBusquedas(scanner); break;
                    case 11: gestorTareas.mostrarGrafoDependencias(); break;
                    case 12: mostrarEstadoGeneral(); break;
                    case 13: demostracionCompleta(); break;
                    case 14: 
                        System.out.println("Gracias por usar el Sistema de Gestion Avanzado!");
                        break;
                    default: 
                        System.out.println("Opcion invalida. Seleccione un numero del 1 al 14.");
                        break;
                }
                
                if (opcion != 14) {
                    System.out.print("\nPresione Enter para continuar...");
                    scanner.nextLine();
                }
                
            } catch (Exception e) {
                System.out.println("Error: Por favor ingrese un numero valido.");
                scanner.nextLine();
                opcion = 0;
            }
            
        } while (opcion != 14);
    }
    
    // ============= MENU PARA ARBOL BINARIO DE PEDIDOS =============
    
    private void menuArbolPedidos() {
        int opcion;
        do {
            System.out.println("\n=== GESTION DE PEDIDOS (Arbol Binario) ===");
            System.out.println("1. Agregar nuevo pedido");
            System.out.println("2. Buscar pedido por numero");
            System.out.println("3. Eliminar pedido entregado");
            System.out.println("4. Ver pedidos en orden cronologico (In-Orden)");
            System.out.println("5. Ver secuencia de insercion (Pre-Orden)");
            System.out.println("6. Generar reporte de fin de turno (Post-Orden)");
            System.out.println("7. Encontrar pedido con mayor prioridad");
            System.out.println("8. Mostrar estructura del arbol");
            System.out.println("9. Volver al menu principal");
            System.out.print("Opcion: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1: agregarNuevoPedido(); break;
                case 2: buscarPedidoPorNumero(); break;
                case 3: eliminarPedidoEntregado(); break;
                case 4: arbolPedidos.recorridoInOrden(); break;
                case 5: arbolPedidos.recorridoPreOrden(); break;
                case 6: arbolPedidos.recorridoPostOrden(); break;
                case 7: mostrarPedidoMayorPrioridad(); break;
                case 8: arbolPedidos.mostrarEstructura(); break;
                case 9: break;
                default: System.out.println("Opcion invalida");
            }
            
            if (opcion != 9 && opcion >= 1 && opcion <= 8) {
                System.out.print("Presione Enter para continuar...");
                scanner.nextLine();
            }
        } while (opcion != 9);
    }
    
    private void agregarNuevoPedido() {
        try {
            System.out.print("Ingrese numero de pedido: ");
            int numero = scanner.nextInt();
            scanner.nextLine();
            
            System.out.print("Ingrese hora de llegada (HH:MM): ");
            String hora = scanner.nextLine();
            
            System.out.print("Ingrese detalles de los platillos: ");
            String detalles = scanner.nextLine();
            
            System.out.print("Ingrese prioridad (1=Alta, 2=Media, 3=Baja): ");
            int prioridad = scanner.nextInt();
            scanner.nextLine();
            
            System.out.print("Ingrese nombre del cliente: ");
            String cliente = scanner.nextLine();
            
            if (prioridad >= 1 && prioridad <= 3) {
                Pedido nuevoPedido = new Pedido(numero, hora, detalles, prioridad, cliente);
                arbolPedidos.insertarPedido(nuevoPedido);
                System.out.println("Pedido agregado exitosamente al sistema.");
            } else {
                System.out.println("Error: La prioridad debe ser 1, 2 o 3");
            }
            
        } catch (Exception e) {
            System.out.println("Error: Ingrese datos validos");
            scanner.nextLine();
        }
    }
    
    private void buscarPedidoPorNumero() {
        try {
            System.out.print("Ingrese numero de pedido a buscar: ");
            int numero = scanner.nextInt();
            scanner.nextLine();
            
            Pedido pedidoEncontrado = arbolPedidos.buscarPedido(numero);
            
            if (pedidoEncontrado != null) {
                System.out.println("=== PEDIDO ENCONTRADO ===");
                System.out.println(pedidoEncontrado);
            } else {
                System.out.println("Pedido #" + numero + " no encontrado en el sistema");
            }
            
        } catch (Exception e) {
            System.out.println("Error: Ingrese un numero valido");
            scanner.nextLine();
        }
    }
    
    private void eliminarPedidoEntregado() {
        try {
            System.out.print("Ingrese numero de pedido entregado: ");
            int numero = scanner.nextInt();
            scanner.nextLine();
            
            boolean eliminado = arbolPedidos.eliminarPedido(numero);
            
            if (eliminado) {
                System.out.println("Pedido entregado y eliminado del sistema correctamente.");
            }
            
        } catch (Exception e) {
            System.out.println("Error: Ingrese un numero valido");
            scanner.nextLine();
        }
    }
    
    private void mostrarPedidoMayorPrioridad() {
        Pedido pedidoPrioritario = arbolPedidos.encontrarMayorPrioridad();
        
        if (pedidoPrioritario != null) {
            System.out.println("=== PEDIDO CON MAYOR PRIORIDAD ===");
            System.out.println(pedidoPrioritario);
        } else {
            System.out.println("No hay pedidos en el sistema");
        }
    }
    
    // ============= SUBMENUS ORGANIZADOS =============
    
    private void menuGestionClientes() {
        int opcion;
        do {
            System.out.println("\n=== GESTIÓN DE CLIENTES (Cola - FIFO) ===");
            System.out.println("1. Agregar cliente a la fila");
            System.out.println("2. Atender próximo cliente");
            System.out.println("3. Ver próximo cliente");
            System.out.println("4. Mostrar cola completa de clientes");
            System.out.println("5. Volver al menú principal");
            System.out.print("Opción: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1: agregarCliente(); break;
                case 2: atenderCliente(); break;
                case 3: verProximoCliente(); break;
                case 4: mostrarColaClientes(); break;
                case 5: break;
                default: System.out.println("Opción inválida");
            }
            
            if (opcion != 5 && opcion >= 1 && opcion <= 4) {
                System.out.print("Presione Enter para continuar...");
                scanner.nextLine();
            }
        } while (opcion != 5);
    }
    
    private void menuGestionPlatos() {
        int opcion;
        do {
            System.out.println("\n=== GESTIÓN DE PLATOS (Pila - LIFO) ===");
            System.out.println("1. Apilar plato sucio");
            System.out.println("2. Lavar plato (del tope)");
            System.out.println("3. Ver próximo plato a lavar");
            System.out.println("4. Mostrar pila completa de platos");
            System.out.println("5. Volver al menú principal");
            System.out.print("Opción: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1: apilarPlatoSucio(); break;
                case 2: lavarPlato(); break;
                case 3: verTopePlatos(); break;
                case 4: mostrarPilaPlatos(); break;
                case 5: break;
                default: System.out.println("Opción inválida");
            }
            
            if (opcion != 5 && opcion >= 1 && opcion <= 4) {
                System.out.print("Presione Enter para continuar...");
                scanner.nextLine();
            }
        } while (opcion != 5);
    }
    
    private void menuGestionIngredientes() {
        int opcion;
        do {
            System.out.println("\n=== GESTIÓN DE INGREDIENTES (Cola Prioridad) ===");
            System.out.println("1. Agregar ingrediente");
            System.out.println("2. Usar ingrediente prioritario");
            System.out.println("3. Ver próximo ingrediente a usar");
            System.out.println("4. Mostrar cola completa de ingredientes");
            System.out.println("5. Volver al menú principal");
            System.out.print("Opción: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1: agregarIngrediente(); break;
                case 2: procesarIngrediente(); break;
                case 3: verProximoIngrediente(); break;
                case 4: mostrarColaIngredientes(); break;
                case 5: break;
                default: System.out.println("Opción inválida");
            }
            
            if (opcion != 5 && opcion >= 1 && opcion <= 4) {
                System.out.print("Presione Enter para continuar...");
                scanner.nextLine();
            }
        } while (opcion != 5);
    }
    
    private void menuGestionMenu() {
        int opcion;
        do {
            System.out.println("\n=== MENU DE LA TAQUERIA ===");
            System.out.println("1. Mostrar menu completo");
            System.out.println("2. Ver detalles de platillo");
            System.out.println("3. Volver al menu principal");
            System.out.print("Opcion: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1: mostrarMenu(); break;
                case 2: verDetallesMenu(); break;
                case 3: break;
                default: System.out.println("Opcion invalida");
            }
            
            if (opcion != 3 && opcion >= 1 && opcion <= 2) {
                System.out.print("Presione Enter para continuar...");
                scanner.nextLine();
            }
        } while (opcion != 3);
    }
    
    private void demostracionCompleta() {
        System.out.println("\n======= DEMOSTRACION COMPLETA DE ALGORITMOS =======");
        
        System.out.println("\n1. RECURSIVIDAD:");
        gestorTareas.mostrarCalculosRecursivos();
        
        System.out.println("\n\n2. DIVIDE Y VENCERAS:");
        gestorTareas.mostrarOrdenamiento();
        
        System.out.println("\n\n3. BALANCEO DE TRABAJO:");
        gestorTareas.mostrarBalanceoTrabajo();
        
        System.out.println("\n\n4. HASHMAPS:");
        gestorTareas.mostrarHashMaps();
        
        System.out.println("\n\n5. ARBOL BINARIO DE PEDIDOS:");
        System.out.println("--- Recorrido In-Orden ---");
        arbolPedidos.recorridoInOrden();
        
        System.out.println("\n\n6. GRAFOS:");
        gestorTareas.mostrarGrafoDependencias();
        
        System.out.println("\nDemostracion completa finalizada!");
        System.out.println("Todos los algoritmos avanzados han sido ejecutados exitosamente.");
    }
    
    public static void main(String[] args) {
        System.out.println("============= Pedidos agregados de ejemplo: =============");
        TaqueriaFinal taqueria = new TaqueriaFinal();
        taqueria.mostrarMenuPrincipal();
        scanner.close();
    }
}