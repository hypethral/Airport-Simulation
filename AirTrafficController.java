//Author: Arslan Khurram / TP058344
//Program: Airport Simulation using threads
//Date: 24th Feb 2022
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class AirTrafficController {
	public static void main(String[] args) {
		
		//Used to generate Status of Aircraft, Whether it will be landing or taking off
		Random random = new Random();
		
		//Functions the same way as arrayblockingqueue but unlike arrayblockingqueue it can be optionally-bounded.
		BlockingQueue<Runway> runways = new LinkedBlockingQueue<Runway>(2);
		BlockingQueue<Gate> gates = new LinkedBlockingQueue<Gate>(2);
		
		
		//These thread pools will used for runway and gate, respectively.
		//After plane has successfully landed then it give the aircraft to the gate.
		//After Gate does his thing it will give it back the runway for take off.
		ExecutorService runwayThreadPool = Executors.newFixedThreadPool(2);
		ExecutorService gateThreadPool = Executors.newFixedThreadPool(2);
		
		//creating gates and runways objects
		for(int i = 1; i <= 2; i++) {
			runways.add(new Runway(i));
			gates.add(new Gate(i));
		}
		
		//contruct 10 aircrafts according to the requirements and submitting them to the thread pool
		for (int i = 1; i <= 10; i++) {
			try { 
				Thread.sleep(random.nextInt(5001)); //In assignment Question
				runwayThreadPool.submit(new Plane(i, random.nextBoolean(), runways, gates));
				
			}catch (InterruptedException e) {
				e.printStackTrace(); //error handling method
				}	
		}
		gateThreadPool.shutdown(); 
		runwayThreadPool.shutdown();
		
		try {
			if (runwayThreadPool.awaitTermination(500, TimeUnit.SECONDS)) {
				System.out.println("Simulation has ended! by Arslan Khurram");
			}
		} catch (InterruptedException e) {}	
	}
}
