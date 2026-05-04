package intercambioobjetos;

import java.io.Serializable;

public class ObjetoSerializable implements Serializable {

    int id;
    String mensaje;

    public ObjetoSerializable(int id, String mensaje) {
        this.id = id;
        this.mensaje = mensaje;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getMensaje() {
        return mensaje;
    }

}
