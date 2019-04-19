package sample;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread{
    ServerSocket ss;
    ArrayList<ServerClient> myClient = new ArrayList<>();

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
