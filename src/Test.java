import java.io.*;
import java.util.*;

public class Test {
	static String adminPassword = "admin";
	static int highestUser = 10;
	static void generateAdminFile(String password) {
		try {
			File file = new File("Admin.txt");
			if (!file.exists()) {
				FileWriter writer = new FileWriter(file);
				writer.write(password);
				writer.close();
			} else {
				file.delete();
				FileWriter writer = new FileWriter(file);
				writer.write(password);
				writer.close();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void main(String[] args) {
		generateAdminFile(adminPassword);
		Scanner inp = new Scanner(System.in);
		Registration registration = new Registration();
		Register register = new Register(registration);
		Thread t1 = new Thread(register);
		Thread t2;

		while (true) {
			System.out.println("Take a token");
			System.out.println("Generate Bill/Register/Billing Details/Payment/Exit");
			String choice = inp.nextLine();
			if (choice.matches("Register")) {
				t1.start();
				try {
					t1.join();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (choice.matches("Billing Details")) {
				t2 = new Thread(new Biller(false));
				t2.start();
				try {
					t2.join();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (choice.matches("Generate Bill")) {
				t2 = new Thread(new Biller(true));
				t2.start();
				try {
					t2.join();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(choice.matches("Payment"))
			{
				t2 = new Thread(new Payment());
				t2.start();
				try {
					t2.join();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if (choice.matches("Exit")) {
				inp.close();
				break;
			}
			else 
			{
				inp.close();
			}
		}
	}

}
