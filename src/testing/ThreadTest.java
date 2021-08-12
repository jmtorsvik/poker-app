package testing;
import java.util.concurrent.TimeUnit;

public class ThreadTest {

	public static void count(String counter) {
		for (int i = 0; i < 10; i++) {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				System.out.println("Error while sleeping.");
			}
			System.out.println(counter + " " + (i + 1));
		}
	}
	
	public static void main(String[] args) {
		Runnable r = () -> {
			count("Thread-count");
		};
		
		new Thread(r).start();
		
		count("Main-count");
	}
	
}
