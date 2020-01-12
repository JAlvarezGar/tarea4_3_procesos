
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author Jesús Alvarez<jjalvarezgar@gmail.com>
 */
public class ServidorHilo extends Thread {

    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private int idSessio;

    public ServidorHilo(Socket socket) {
        this.socket = socket;
//        this.idSessio = id;
        try {
            dos = new DataOutputStream(this.socket.getOutputStream());
            dis = new DataInputStream(this.socket.getInputStream());
        } catch (IOException ex) {
            System.out.println("");
        }
    }

    public void desconnectar() {
        try {
            socket.close();
        } catch (IOException ex) {
            System.out.println("");
        }
    }

    @Override
    public void run() {
        try {

            // leo el nombre del archivo enviado desde el cliente
            String nombre = dis.readUTF();

            String datos = nombre;

            File archivo = new File(datos);

            if (archivo.exists()) {
                dos.writeUTF("Se recibe el archivo : " + archivo.toString());
                System.out.println("Se solicita el archivo : " + archivo.toString());
                FileReader fR = new FileReader(archivo);
                //creamos un buffer gestionar el archivo.
                BufferedReader bF = new BufferedReader(fR);
                String archivos;

                // lectura del archvio seleccionadolínea a línea 
                while ((archivos = bF.readLine()) != null) {
                    // enviamos la lectura a traves del flujo
                    dos.writeUTF(archivos);
                }
                dis.close();

                dos.close();
                fR.close();
                bF.close();
                desconnectar();

            } else {

                dos.writeUTF("Ese archivo no existe...");
                dis.close();

                dos.close();

                desconnectar();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error de conexión", "Mensaje", JOptionPane.ERROR_MESSAGE);
        }

    }
}
