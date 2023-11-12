
package programacion2.parquedeportes.logica;

import programacion2.parquedeportes.persistencia.ControladoraPersistencia;

public class Controladora {
    
    ControladoraPersistencia controlPersis = new ControladoraPersistencia();
    
    public Deporte seleccionDeporte(String nombre){
        
        Deporte canotaje = new Deporte(6, "Canotaje", 100, 100.00);
        Deporte parapente = new Deporte(7, "Parapente", 100, 50.00);
        Deporte escalar = new Deporte(8, "Escalar", 50, 100.00);
        Deporte jumping = new Deporte(9, "Jumping", 50, 75.00);
        Deporte cuatrimoto = new Deporte(10, "Cuatrimoto", 25,100.00);
        Deporte gotcha = new Deporte(11, "Gotcha", 100, 125.00);
        Deporte deporte = new Deporte(12, "Gotcha", 100, 125.00);
        
        if (nombre.equalsIgnoreCase(nombre)){
            deporte = switch (nombre) {
                case "Canotaje" -> canotaje;
                case "Parapente" -> parapente;
                case "Escalar" -> escalar;
                case "Jumping" -> jumping;
                case "Cuatrimoto" -> cuatrimoto;
                case "Gotcha" -> gotcha;
                case "canotaje" -> canotaje;
                case "parapente" -> parapente;
                case "escalar" -> escalar;
                case "jumping" -> jumping;
                case "cuatrimoto" -> cuatrimoto;
                case "gotcha" -> gotcha;
                default -> gotcha;
            };
        }
        return deporte;
    }

    public void terminarBoleto(String nit, String nombre, String deporte, int cantidad) {
        
        Deporte sport = new Deporte();
        sport.setNombre(deporte);
        sport.setCupo(cantidad);
        sport.setPrecio(100.00);
        
        Fecha fechaHoy = new Fecha();
        
        Factura factura = new Factura();
        factura.setFecha(fechaHoy.fechaActual());
        factura.setNit(nit);
        factura.setNombre(nombre);
        
        Boleto boleto = new Boleto();
        boleto.setCantidad(cantidad);
        boleto.setFactura(factura);
        boleto.setDeporte(sport);
        
        controlPersis.guardar(sport, factura, boleto);
        
    }
    
}
