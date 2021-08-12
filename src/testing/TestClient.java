package testing;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TestClient {
	
	public static void main(String[] args) throws IOException {
		Socket s = new Socket("localhost", 4999);

		InputStreamReader in = new InputStreamReader(s.getInputStream());
		BufferedReader bf = new BufferedReader(in);
		while (true) {
			System.out.println(bf.readLine());
		}
	}
	
}
