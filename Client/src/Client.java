import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread {
    String ipAddress;
    int port;

    //Scanner scn = new Scanner(System.in );
    InetAddress ip;
    Socket S;
    DataInputStream dis;
    DataOutputStream dos;

    String txData = "";//output data
    String rxData = "";//input data

    //read string
    public String readData(){
	String temp = this.rxData;
	this.rxData = "";
        return temp;
    }

    //send string
    public void sendData(String data){
        this.txData = data;
    }

    //inin port
    //FIXME implement boolean that return true if connection is successful
    public void init(String ipAddress,int port){
        this.ipAddress = ipAddress;
        this.port = port;
    }

    //Thread for client
    public void run(){
        try{
            ip = InetAddress.getByName(this.ipAddress);
            S = new Socket(ip, port);
            dis = new DataInputStream(S.getInputStream());
            dos = new DataOutputStream(S.getOutputStream());

            while(true){
                if(!txData.isEmpty() ){
                    dos.writeUTF(txData);
                    txData = "";
                }
                if(dis.available()>0){
                    rxData = dis.readUTF();
                    //System.out.println("client: "+rxData);
                }
                //if "Exit" is string, close connection
                if(rxData.equals("Exit")){
                    S.close();
                    break;
                }
            }
            //scn.close();
            dis.close();
            dos.close();
        }
        catch(Exception E){
            E.printStackTrace();
        }
    }
}
