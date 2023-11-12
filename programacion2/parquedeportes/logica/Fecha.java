
package programacion2.parquedeportes.logica;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Fecha {
    public String fechaActual() {
        LocalDate fecha = LocalDate.now(); // Obtiene la fecha actual del sistema

        // Formatear la fecha como String (mes-día-año)
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("MM-dd-yyyy"); // Define el formato deseado
        String fechaComoString = fecha.format(formato);

        return fechaComoString;
    }

}
