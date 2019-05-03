public class Timer extends Thread {
    String updateString = "test";
    boolean timeComplete = false;
    int startingSecond = 10;//60*1;

    int counter = startingSecond;
    int mins;
    int seconds;

    Timer(){
        mins = counter / 60;
        seconds = counter - (mins * 60);
        updateString = "Rem min:" + mins + " sec:" + seconds;
    }
    public boolean timeUp(){
        return this.timeComplete;
    }
    public String getString(){
        return this.updateString;
    }
    public void run(){
        counter = startingSecond;
        //int mins;
        //int seconds;

        System.out.println("Countdowm from "+startingSecond+ " to "+ 0);
        while(counter>0) {
            mins = counter / 60;
            seconds = counter - (mins * 60);

            //System.out.println("Remaining Min:" + mins + " sec:" + seconds);
            updateString = "Rem min:" + mins + " sec:" + seconds;
            try {
                counter--;
                Thread.sleep(1000L);
            } catch (Exception E) {
            }
        }
        System.out.println("Time is up!");
    }
}
