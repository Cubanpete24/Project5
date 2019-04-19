import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ServerClient extends Thread {
    String txData = "";
    String rxData = "";

    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;

    public ServerClient(Socket mySocket, DataInputStream Din, DataOutputStream Dout){
        this.s = mySocket;
        this.dis = Din;
        this.dos = Dout;
    }

    public void sendData(String data){
        this.txData = data;
    }

    public String readData(){
        return this.rxData;
    }

    @Override
    public void run(){
        while(true){
            try{
                if( !txData.isEmpty() ){
                    dos.writeUTF(this.txData);
                    this.txData = "";
                }
                if(dis.available()>0){
                    this.rxData = dis.readUTF();
                }
            }
            catch(Exception E){
                E.printStackTrace();
            }
        }
    }
}
