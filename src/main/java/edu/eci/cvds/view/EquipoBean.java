package edu.eci.cvds.view;

import com.google.inject.Inject;
import edu.eci.cvds.entities.Equipo;
import edu.eci.cvds.entities.Novedad;
import edu.eci.cvds.exceptions.HistorialEquiposException;
import edu.eci.cvds.services.ServicesElemento;
import edu.eci.cvds.services.ServicesEquipo;
import edu.eci.cvds.services.ServicesHistorialDeEquipoFactory;
import edu.eci.cvds.services.ServicesNovedad;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("deprecation")
@ManagedBean(name = "equipoBean")
@SessionScoped

public class EquipoBean extends BasePageBean{

    private static final long serialVersionUID = 1L;
    private String tipoEstado;
    private List<Equipo> equipos;

    @Inject
    private ServicesEquipo servicesEquipo;

    private Map<String, Integer> equipoMap;
    private String selectedEquipo;


    public List<Equipo> consultarEquipos() throws HistorialEquiposException {
        equipos = servicesEquipo.consultarEquipos();
        return equipos;
    }

    public void registrarEquipo(String nombre, String activoS, int laboratorio, int torre, int pantalla, int mouse, int teclado, int responsable) throws HistorialEquiposException, ParseException {
        int id = this.consultarNextId() + 1;
        Equipo equipo;
        boolean activo = false;
        if (activoS.equalsIgnoreCase("Activo")){
            activo = true;
        }
        equipo = new Equipo(id, laboratorio, nombre, activo);
        servicesEquipo.addEquipo(equipo);
        /*Para asociar Elementos*/
        asociarElemento(id, torre, pantalla, mouse, teclado);
        /*Para Registrar Novedades*/
        generarNovedad(id, torre, pantalla, mouse, teclado, responsable, equipo);

    }

    private void asociarElemento(int id, int torre, int pantalla, int mouse, int teclado) throws HistorialEquiposException {
        /*Para asociar Elementos*/
        ServicesElemento servicesElemento = ServicesHistorialDeEquipoFactory.getInstance().getServicesElemento();
        servicesElemento.actualizarEquipoAsociado(torre, id);
        servicesElemento.actualizarEquipoAsociado(pantalla, id);
        servicesElemento.actualizarEquipoAsociado(mouse, id);
        servicesElemento.actualizarEquipoAsociado(teclado, id);
    }

    private void generarNovedad(int id, int torre, int pantalla, int mouse, int teclado, int responsable, Equipo equipo) throws HistorialEquiposException, ParseException {
        /*Para Registrar Novedades*/
        ServicesNovedad servicesNovedad = ServicesHistorialDeEquipoFactory.getInstance().getServicesNovedad();
        servicesNovedad.registrarNovedad(new Novedad(1, responsable, id, torre,
                new SimpleDateFormat("YYYY/MM/DD").parse("2020/09/28"), "Elemento Nuevo Registrado", "Se registro un nuevo elemento de tipo torre al equipo " + equipo.getNombre()));
        servicesNovedad.registrarNovedad(new Novedad(1, responsable, id, pantalla,
                new SimpleDateFormat("YYYY/MM/DD").parse("2020/09/28"), "Elemento Nuevo Registrado", "Se registro un nuevo elemento de tipo pantalla al equipo " + equipo.getNombre()));
        servicesNovedad.registrarNovedad(new Novedad(1, responsable, id, mouse,
                new SimpleDateFormat("YYYY/MM/DD").parse("2020/09/28"), "Elemento Nuevo Registrado", "Se registro un nuevo elemento de tipo mouse al equipo " + equipo.getNombre()));
        servicesNovedad.registrarNovedad(new Novedad(1, responsable, id, teclado,
                new SimpleDateFormat("YYYY/MM/DD").parse("2020/09/28"), "Elemento Nuevo Registrado", "Se registro un nuevo elemento de tipo teclado al equipo " + equipo.getNombre()));
    }

    public String getTipoEstado() {
        return tipoEstado;
    }

    public void setTipoEstado(String tipoEstado) {
        this.tipoEstado = tipoEstado;
    }

    private int consultarNextId() throws HistorialEquiposException {
        this.consultarEquipos();
        int maxInt = 0;
        for (Equipo equipo : equipos){
            if (equipo.getId() > maxInt){ maxInt = equipo.getId();}
        }
        return maxInt;
    }

    public Map<String, Integer> getEquipoMap() throws HistorialEquiposException {
        consultarEquipos();
        equipoMap = new LinkedHashMap<String,Integer>();
        for(Equipo equipo : equipos){
            equipoMap.put(equipo.getNombre(), equipo.getId());
        }
        return equipoMap;
    }

    public void setEquipoMap(Map<String, Integer> equipoMap) {
        this.equipoMap = equipoMap;
    }

    public String getSelectedEquipo() {
        return selectedEquipo;
    }

    public void setSelectedEquipo(String selectedEquipo) {
        this.selectedEquipo = selectedEquipo;
    }
}
