package depositoAutomatizado;

import depositoAutomatizado.robots.Robot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Deposito {
    private List<Compartimiento> compartimientos;
    private List<Pedido> pedidos;
    private List<Recorrido> recorridosPreestablecidos;
    private List<Robot> robots;
    private Transportista transportista;
    private Mantenimiento mantenimiento;

    public Deposito(Transportista transportista){
        this.compartimientos = new ArrayList<>();
        this.pedidos = new ArrayList<>();
        this.recorridosPreestablecidos = new ArrayList<>();
        this.robots = new ArrayList<>();
        this.transportista = transportista;
    }

    public void agregarCompartimientos(Compartimiento ... compartimientos){
        Collections.addAll(this.compartimientos, compartimientos);
    }

    public void agregarPedido(Pedido pedido){
        this.pedidos.add(pedido);
    }

    public void agregarRobots(Robot ... robots){
        Collections.addAll(this.robots, robots);
    }

    public void agregarRecorridos(Recorrido ... recorridos){
        Collections.addAll(this.recorridosPreestablecidos, recorridos);
    }

    public void atenderPedido(){
        Pedido pedido = this.pedidos.get(0);
        List<Mercaderia> mercaderias = pedido.getMercaderias();
        List<Compartimiento> compartimientos = mercaderias.stream().map(m -> m.getCompartimiento()).collect(Collectors.toList());
        List<Recorrido> recorridos = compartimientos.stream().map(c -> c.getRecorrido()).collect(Collectors.toList());

        // robots libres
        List<Robot> robotsLibres = this.robots.stream().filter(r -> r.getEstado().equals("Libre")).collect(Collectors.toList());

        // this.robotsLibres.parallelStream().forEach(r -> r.buscarMercaderia());
        // r -> r.buscarMercaderia(recorridos.get(0)), recorridos.remove(0), iterar
        // observer que notifica cuando robot esta libre, y le asigno un recorrido pendiente

        if(verificarMercaderias(pedido)){
            // avisar transportista
            this.transportista.notificarPaqueteCompleto(pedido);
            this.pedidos.remove(0);
        }
    }

    public Compartimiento getCompartimientoDeMercaderia(Mercaderia mercaderia){
        return this.compartimientos
                .stream()
                .filter(c -> c.getMercaderia() == mercaderia)
                .collect(Collectors.toList())
                .get(0);
    }

    public boolean verificarMercaderias(Pedido pedido){
        PuntoConsolidacion puntoConsolidacion = pedido.getPuntoConsolidacion();
        return pedido.getMercaderias().equals(puntoConsolidacion.getMercaderias());
    }
}
