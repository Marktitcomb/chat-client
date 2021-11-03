import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

/**

*

* @author MTITCOMB

*/

public class ClientServer

{
  private Consumer<Serializable> onRecieveCallback;
  private ConnectionThread connection = new ConnectionThread();
  private String userName;
  private int port;
  private String IP;

  ClientServer(String IP, int port, String userName, Consumer<Serializable> onRecieveCallback){
    this.onRecieveCallback = onRecieveCallback;
    connection.setDaemon(true);
    this.port = port;
    this.IP = IP;
    this.userName = userName;
  }

  public void sendMessage(Serializable data)throws Exception{
    connection.out.writeObject(data);
  }

  public void send(Serializable data)throws Exception{
    connection.out.writeObject(data);
  }

  public void startConnection() throws Exception{
    connection.start();
  }

  public void closeConnection() throws Exception{
    connection.socket.close();
  }
 

  private class ConnectionThread extends Thread{
    private Socket socket;
    private ObjectOutputStream out;

    @Override
    public void run(){
      try(      
          Socket socket = new Socket("localhost", 4555);
          ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
          ObjectInputStream in = new ObjectInputStream(socket.getInputStream()))
      {
        this.socket = socket;
        this.out = out;
        socket.setTcpNoDelay(true);
        out.writeObject(userName);

        while(true){
          Serializable data = (Serializable) in.readObject();
          onRecieveCallback.accept(data);
        }
      }catch(Exception e){

          onRecieveCallback.accept("Connection Closed");
      }
    }
  }
}

 