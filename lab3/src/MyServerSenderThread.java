import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

//made in MyServerthread.java


public class MyServerSenderThread implements Runnable {

    //private ObjectOutputStream[] outputStreamList = null;
    private MSocket[] client_mSocket = null;
    private BlockingQueue eventQueue = null;
    public LamportClock myLamportClock = null;
    
    
    public MyServerSenderThread(MSocket[] client_mSocket, BlockingQueue eventQueue, LamportClock myLamportClock){
		this.client_mSocket = client_mSocket;
		this.eventQueue = eventQueue;
		this.myLamportClock =  myLamportClock;
		
    }

    public void run() {
        MPacket toBroadcast = null;
        
        while(true){
            try{	//dequeue packet, add global sequence number, and broadcast
                	//Take packet from queue to broadcast to all clients
            	/*if(eventQueue.isEmpty()){
            		System.out.println("MyServerSenderThread: Event queue is empty");
            	}
            	else{
            		System.out.println("MyServerSenderThread: Event queue is not empty");
            	}*/
            	System.out.println("MyServerSenderThread: Going to take from event queue");
            	toBroadcast = (MPacket)eventQueue.take();
                // Send only head packet of queue, need vector clock mechanism somewhere around here
                System.out.println("MyServerSenderThread: Taken from eventqueue. Now broadcast by writing to sockets");
                myLamportClock.value = myLamportClock.value + 1;
                System.out.println("MyServerSenderThread: Sending with lamport clock value " + myLamportClock.value);
                toBroadcast.lamportClock = myLamportClock.value;
                
                //Send it to all clients
                for(MSocket mSocket: client_mSocket){
                	System.out.println("MyServerSenderThread: Writing to sockets");
                    mSocket.writeObject(toBroadcast);
                }
            }catch(InterruptedException e){
                System.out.println("Throwing Interrupt");
                Thread.currentThread().interrupt();    
            }
            
        }
    }
    
}
