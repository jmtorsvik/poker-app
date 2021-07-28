package testing;

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class client {

	public static void main(String[] args) throws IOException {
		System.out.println("Client");
		Socket s = new Socket("localhost", 4999);

		InputStreamReader in = new InputStreamReader(s.getInputStream());
		BufferedReader bf = new BufferedReader(in);
		System.out.println(bf.readLine());
		
		PrintWriter pr = new PrintWriter(s.getOutputStream());
		Scanner sc = new Scanner(System.in);
		pr.println(sc.nextLine());
		pr.flush();
		
		System.out.println("cient shutdown");
	}
	
}
