import java.io.*;
import java.util.*;

public class Register implements Runnable{
	Registration m;
	Scanner inp=new Scanner(System.in);
	Scanner inp1=new Scanner(System.in);
	public Register(Registration registration) {
		this.m=registration;
		new Thread(this,"Register");
	}
	
	@Override
	public void run() {
		
		String phone, emailid,userid,password,choice;
		try {
			for(int a=0; a<Test.highestUser;a++) {
				File file=new File("Customer"+a+".txt");
				if(file.exists()==true) {
					continue;
				}
				else if(file.createNewFile()==true) {
					System.out.println("New Registration form\n");
				}
				else {
					System.out.println("Failed to open Registration form");
					break;
				}
				FileWriter database=new FileWriter("Customer"+a+".txt");
				System.out.println("Enter name:");
				m.setCustomerName(inp.nextLine());
				System.out.println("Enter house number:");
				m.setHoldingNumber(inp.nextLine());
				System.out.println("Enter Road number:");
				m.setRoadNumber(inp.nextLine());
				System.out.println("Enter City:");
				m.setCity(inp.nextLine());
				System.out.println("Enter Occupation:");
				m.setOccupation(inp.nextLine());
				System.out.println("Enter Phone number:");
				for(int i=0;i<3;i++) {
					phone=inp.nextLine();
					if(phone.length()==11) {
						m.setPhoneNumber(phone);
						break;
					}
					else {
						System.out.println("Try again: (not 11 numbers)");
						continue;
					}
				}
				System.out.println("Enter EmailID:");
				for(int j=0;j<3;j++) {
					emailid=inp1.nextLine();
					if(emailid.contains("@")&&emailid.contains(".com")) {
						m.setEmailID(emailid);
						break;
					}
					else {
						System.out.println("Try again: ('@'or'.com' missing)");
						continue;
					}
				}
				System.out.println("Enter MeterID:");
				m.setMeterID(inp1.nextLine());
				System.out.println("Enter a Username:");
				for(int j=0;j<3;j++) {
					userid=inp1.nextLine();
					if(m.checkString(userid)==true){
						m.setUserID(userid);
						break;
					}
					else {
						System.out.println("Try again: (must have a capital, small and number)");
						continue;
					}
				}
				System.out.println("Enter a Password:");
				for(int j=0;j<3;j++) {
					password=inp1.nextLine();
					if(password.length()>=6){
						m.setPassword(password);
						break;
					}
					else {
						System.out.println("Try again: (must have 6 letters)");
						continue;
					}
				}
				database.write("Username: "+m.getUserID()+"\nPassword: "+m.getPassword());
				database.write("\nName: "+m.getCustomerName()+"\nAddress: "+m.getAddress()+"\nPhone: "+m.getPhoneNUmber()+"\nOccupation: "+m.getOccupation()+"\nEmail ID: "+m.getEmailID()+"\nMeter ID: "+m.getMeterID());
				System.out.println("Registration Complete\n");
				System.out.println("Next Registration: (Yes or No)");
				choice=inp.nextLine();
				if(choice.matches("Yes")||choice.matches("yes")||choice.matches("Y")||choice.matches("y")) {
					database.close();
					try {
						Thread.sleep(1000);
					}catch(Exception b) {
						b.printStackTrace();
					}
					continue;
					}
				else {
					database.close();
					try {
						Thread.sleep(1000);
					}catch(Exception b) {
						b.printStackTrace();
					}
					break;
				}
			}
		}catch(Exception a) {
			a.printStackTrace();
		}
	}

}
