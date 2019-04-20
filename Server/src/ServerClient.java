import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ServerClient extends Thread {
    String txData = "";//receiving string
    String []rxData = {""};//sending string



    final DataInputStream dis;//input stream
    final DataOutputStream dos;//output stream
    final Socket s;//socket connection

    //initializer of the socket with input/output stream
    public ServerClient(Socket mySocket, DataInputStream Din, DataOutputStream Dout){
        this.s = mySocket;
        this.dis = Din;
        this.dos = Dout;
    }

    //SendString method
    public void sendData(String data){
        this.txData = data;
    }

    //ReadString method
    public String[] readData(){
        String []temp = this.rxData;
        this.rxData = new String[]{""};
        return temp;
    }

    //Method that listens for incomming/outgoing string
    @Override
    public void run(){
        while(true){
            try{
                //if there is a string, send it
                if( !txData.isEmpty() ){
                    dos.writeUTF(this.txData);
                    this.txData = "";
                }
                //if there is incomming data, save it
                if(dis.available()>0){
                    String temp = dis.readUTF();
                    this.rxData = temp.split(":");
                }
            }
            catch(Exception E){
                E.printStackTrace();
            }
        }
    }
}
