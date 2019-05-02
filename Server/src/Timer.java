public class Timer implements Runnable {
    boolean timeComplete = false;
    int startingSecond = 60*1;

    public void run(){
        System.out.println("Countdowm from "+startingSecond+ " to "+ 0);
        timeComplete = false;

        runTimmer();

        timeComplete = true;
        System.out.println("Time is up!");
    }
    public void runTimmer(){
        int counter = startingSecond;
        int mins;
        int seconds;
        while(counter>0){
            mins = counter/60;
            seconds = counter - (mins * 60);
            System.out.println("Remaining Min:"+mins+" sec:"+seconds);
            try{
                counter--;
                Thread.sleep(1000L);
            }
            catch(Exception E){

            }
        }
    }
}
