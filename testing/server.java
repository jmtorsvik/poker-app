package testing;

import java.net.*;
import java.util.LinkedList;
import java.util.List;
import java.io.*;

public class server {

	public static void main(String[] args) throws IOException {
		System.out.println("Server");
	
		ServerSocket ss = new ServerSocket(4999);
		
		List<BufferedReader> readers = new LinkedList<>();
		List<PrintWriter> writers = new LinkedList<>();
		
		int n = 2;
		for (int i = 0; i < n; i++) {
			Socket s = ss.accept();
			
			InputStreamReader in = new InputStreamReader(s.getInputStream());
			readers.add(new BufferedReader(in));
			
			writers.add(new PrintWriter(s.getOutputStream()));
		}
		
		for (int i = 0; i < n; i++) {
			PrintWriter pr = writers.get(i);
			pr.println("Hello client!");
			pr.flush();
			
			System.out.println(readers.get(i).readLine());
		}
		
		System.out.println("server shutdown");
		
	}
	
}
