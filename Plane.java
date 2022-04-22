import java.util.concurrent.BlockingQueue;

public class Plane implements Runnable {
	private String flightStatus;
	private String cleaningRefuelingStatus;
	private int flightNum;
	private BlockingQueue<Runway> runways = null; //initialization
	private BlockingQueue<Gate> gates = null;
	private Runway runway; //for getting
	private Gate gate;
	private Boolean random;
	
	
	//Constructor . getting values from main
	public Plane (int flightNum, Boolean random, BlockingQueue<Runway> runways, BlockingQueue<Gate> gates) {
		this.flightNum = flightNum;
		this.runways = runways;
		this.gates = gates;
		//checking whether true or false in random which was generated in main
		if (random == false) {
			flightStatus = "landing";
		}else if (random == true) {
			flightStatus = "taking off";
		}
		//printing after creation of a plane
		System.out.println(java.time.LocalTime.now() +" Aircraft "+flightNum+" has reached the airport and is requesting to " + flightStatus +"."); 
		random = !random; //switching random value for cleaning and refueling
		if (random == true) {
			cleaningRefuelingStatus = "Required";
		}else {
			cleaningRefuelingStatus = "not";
		}
		 
	}


	public void run() {
		try {
			runway = runways.take();
			//System.out.println(java.time.LocalTime.now() + runway.getName() + " is being utilized by aircraft " + flightNum + ".");
			System.out.println(java.time.LocalTime.now() + " The aircraft " + flightNum + " will be " + flightStatus + " on " + runway.getName()+".");
			if (flightStatus == "landing") {
				Thread.sleep(5000); // 5 seconds
				System.out.println(java.time.LocalTime.now() + " Aircraft " + flightNum + " has be landed and " + runway.getName() + " has been freed."); //printing after status ended..
				//runways.add(runway); // giving back runway or freeing it for other planes
				//need to assign gate -------
				try { //assigning gate to the flights that the landing status
					gate = gates.take();
					runways.add(runway); // giving back runway or freeing it for other planes
					//System.out.println(java.time.LocalTime.now() + " The Gate " + gate.getName() + " is being utilized by " + flightNum + "");
					System.out.println(java.time.LocalTime.now() + " The Passengers of aircraft " + flightNum + " are disembarking " + "at the "  + gate.getName() + ".");
					if (cleaningRefuelingStatus == "Required") {
						System.out.println(java.time.LocalTime.now() + " Aircraft " + flightNum + " is getting refueled and cleaned" + " at the "  + gate.getName() + ".");
						Thread.sleep(5000); // Putting thread to sleep to mimic cleaning and refueling - 5 seconds
					}
					System.out.println(java.time.LocalTime.now() + " The Passengers of have embarked on Aircraft " + flightNum + " at the "  + gate.getName() + " and requesting take-off.");
					//System.out.println(java.time.LocalTime.now() +" This aircraft in gate -> " + gate.getName() + "is requesting runway!"); // try to take runway
					try { //taking off after getting gate
						runway = runways.take();
						gates.add(gate); // free up the gate
						//System.out.println(java.time.LocalTime.now() + " The Runway " + runway.getName() + " is being utilized by " + flightNum + ".");
						Thread.sleep(3000); // take off time - 3 seconds
						System.out.println(java.time.LocalTime.now() + " Aircraft " + flightNum + " has taken off at " + runway.getName() + " and freed it.");
						runways.add(runway);
					}catch(InterruptedException e) {
						e.printStackTrace();
					}
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
		
			} else if (flightStatus == "taking off") {
				Thread.sleep(3000);
				System.out.println(java.time.LocalTime.now() + " Aircraft " + flightNum + " has taken off at " + runway.getName() + " and freed it."); //printing after status ended..
				runways.add(runway); // giving back runway or freeing it for other planes
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}	
	}
}
