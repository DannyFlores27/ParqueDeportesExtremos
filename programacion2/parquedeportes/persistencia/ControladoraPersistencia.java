
package programacion2.parquedeportes.persistencia;

import programacion2.parquedeportes.logica.Boleto;
import programacion2.parquedeportes.logica.Deporte;
import programacion2.parquedeportes.logica.Factura;

public class ControladoraPersistencia {
    BoletoJpaController boletoJpa = new BoletoJpaController();
    DeporteJpaController deporteJpa = new DeporteJpaController();
    FacturaJpaController facturaJpa = new FacturaJpaController();
    ParqueJpaController parqueJpa = new ParqueJpaController();

    public void guardar(Deporte sport, Factura factura, Boleto boleto) {

        //Crear en la base de datos el deporte
        deporteJpa.create(sport);
        
        //Crear en la BD la factura
        facturaJpa.create(factura);
        
        //Crear en la BD el boleto
        boletoJpa.create(boleto);

    }
}
