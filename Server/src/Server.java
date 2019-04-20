import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
//Server will be the listening thread of the network.
// When ever a ServerClient is available, Server will ensure to connect and save connection
public class Server extends Thread{
    ServerSocket ss;//empty socket
    ArrayList<ServerClient> myClient = new ArrayList<>(); //ServerClient that stores connection


    //method to initialize the ServerClient
    // Initializes the thread
    public boolean init(int port){
        try{
            ss = new ServerSocket(port);
            System.out.println("Server has been initialized: "+ss.getLocalPort() );
            start();
            return true;
        }
        catch(Exception E){
            E.printStackTrace();
            return false;
        }
    }

    //Thread that listens for ServerClients
    @Override
    public void run(){
        while(true){
            Socket s = null;
            try{
                s = ss.accept();
                System.out.println("New client connected: "+s);
                DataInputStream dis = new DataInputStream(s.getInputStream() );
                DataOutputStream dos = new DataOutputStream(s.getOutputStream() );

                ServerClient temp = new ServerClient(s, dis, dos);
                Thread t = temp;
                myClient.add(temp );

                t.start();
            }
            catch(Exception E){
                E.printStackTrace();
            }
        }

    }

}
