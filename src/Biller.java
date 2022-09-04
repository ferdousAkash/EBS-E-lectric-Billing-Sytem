import java.io.*;
import java.util.*;

public class Biller extends PayService implements Runnable {
	int userIndex;
	boolean AdminVerified = false, userFound = false;
	boolean adminUse;
	Scanner inp = new Scanner(System.in);
	String tempFileName = "temp.txt";
	File tempFile = new File(tempFileName);

	public Biller(boolean adminUse) {
		this.adminUse = adminUse;
		if (adminUse)
			new Thread(this, "Bill generation");
		else
			new Thread(this, "Bill Details");
	}

	void processBillInformation() {
		String content, search;

		try {
			System.out.println("Enter Username: ");
			search = inp.nextLine();
			for (int b = 0; b < Test.highestUser; b++) {
				File file = new File("Customer" + b + ".txt");
				if (!file.exists())
					break;
				BufferedReader read = new BufferedReader(new FileReader(file));
				content = read.readLine();
				if (!content.split(" ")[1].equals(search)) {
					continue;
				} else
					userFound = true;
				this.userIndex = b;
				if (adminUse) {
					read.close();
					return;
				}
				System.out.println("Enter Password: ");
				search = inp.nextLine();
				content = read.readLine();
				if (!content.split(" ")[1].matches(search)) {
					userFound = false;
					break;
				}
				try {
					System.out.println("\nCustomer Information:\n");
				} catch (Exception d) {
					d.printStackTrace();
				}
				for (int i = 0; i <= 6; i++) {
					if (content.contains("Username") == true || content.contains("Password") == true) {
						content = read.readLine();
					} else {
						System.out.println(content);
						content = read.readLine();
					}
				}
				System.out.println("");
				break;
			}
		} catch (Exception c) {
			c.printStackTrace();
		}
	}

	void showBillInformation() {
		try {
			String fileName = "Customer" + this.userIndex + "Bill.txt";
			File file = new File(fileName);
			if (!file.exists())
				return;
			System.out.println("\nBill History:\n");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String content;
			while ((content = reader.readLine()) != null) {

				super.setUnits(content.split(",")[1]);
				super.setBillType(content.split(",")[2]);
				super.setLastdate(content.split(",")[3]);
				if (content.split(",")[5].equals("paymentDate"))
					super.setPaymentDate(content.split(",")[3]);
				else
					super.setPaymentDate(content.split(",")[5]);
				String billInfo = "Bill No              :  " + content.split(",")[0];
				billInfo = billInfo + "\n" + "Bill Status          :  " + content.split(",")[4];
				billInfo = billInfo + "\n" + "Units Used           :  " + content.split(",")[1];
				billInfo = billInfo + "\n" + "Type                 :  " + content.split(",")[2];
				billInfo = billInfo + "\n" + "Last Date of payment :  " + content.split(",")[3];
				if (!content.split(",")[5].equals("paymentDate"))
				{
				    billInfo = billInfo + "\n" + "Date of payment      :  " + content.split(",")[5];
					billInfo = billInfo + "\n" + "Fine                 :  " + String.format("%.2f", super.fineAmmount());
				}
				billInfo = billInfo + "\n" + "Gross Ammount        :  " + String.format("%.2f", super.grossPayment());
				billInfo = billInfo + "\n" + "Tax Ammount          :  " + String.format("%.2f", super.taxAmmount());
				billInfo = billInfo + "\n" + "Total Ammount        :  " + String.format("%.2f", super.totalPayment());
				System.out.println(billInfo + "\n\n");
			}
			reader.close();
		} catch (Exception e) {
		}
	}

	void verifyAdminStatus() {
		String content, password = "";
		try {
			File adminFile = new File("Admin.txt");
			BufferedReader reader = new BufferedReader(new FileReader(adminFile));
			while ((content = reader.readLine()) != null)
				password = content;
			reader.close();
		} catch (Exception e) {
		}
		System.out.println("Need admin verification to generate Bill");
		System.out.println("Enter admin password: ");
		String triedPassword = inp.nextLine();
		this.AdminVerified = password.contains(triedPassword);
		if (AdminVerified)
			System.out.println("Successfully logged in as admin.");
		else
			System.out.println("Wrong password.");
	}

	void GenerateBill() {
		try {
			String fileName = "Customer" + this.userIndex + "Bill.txt";
			File file = new File(fileName);

			if (file.exists()) {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				FileWriter writer = new FileWriter(tempFile);
				String content;
				int index = 1;
				while ((content = reader.readLine()) != null) {
					writer.write(content + "\n");
					index++;
				}
				String serialNo = index + ",";
				System.out.println("Enter units used: ");
				String unitsUsed = (inp.nextLine()) + ",";
				System.out.println("Enter Bill type: Flat || House || Shop");
				String billType = inp.nextLine();
				while (!"FlatHouseShop".contains(billType)) {
					System.out.println("Entered input is not a valid type.");
					System.out.println("Enter Bill type: Flat || House || Shop or Exit");
					billType = inp.nextLine();
					if (billType.equals("Exit")) {
						reader.close();
						writer.close();
						tempFile.delete();
						return;
					}
				}
				billType += ",";
				System.out.println("Enter last date of Payment: dd/mm/yyyy");
				String lastDate = inp.nextLine() + ",Due,paymentDate\n";
				String billInfo = serialNo + unitsUsed + billType + lastDate;
				writer.write(billInfo);
				writer.close();
				reader.close();
				reader = new BufferedReader(new FileReader(tempFile));
				file.delete();
				writer = new FileWriter(file);
				while ((content = reader.readLine()) != null) {
					writer.write(content + "\n");
				}
				reader.close();
				writer.close();
				tempFile.delete();
			} else {
				String serialNo = "1,";
				System.out.println("Enter units used: ");
				String unitsUsed = inp.nextLine() + ",";
				System.out.println("Enter Bill type: Flat || House || Shop");
				String billType = inp.nextLine() + ",";
				System.out.println("Enter last date of Payment: dd/mm/yyyy");
				String lastDate = inp.nextLine() + ",Due,paymentDate\n";
				String billInfo = serialNo + unitsUsed + billType + lastDate;
				FileWriter writer = new FileWriter(fileName);
				writer.write(billInfo);
				writer.close();
			}

		} catch (Exception e) {
		}
	}

	@Override
	public void run() {
		processBillInformation();
		if (!adminUse) {
			if (userFound)
				showBillInformation();
			else
				System.out.println("Wrong Username or Password");
			return;
		}
		if (!userFound) {
			System.out.println("Wrong Username or Password");
			return;
		}
		System.out.println("\nCreate Bill || back");
		String operation = inp.nextLine();
		if (operation.contains("back"))
			return;
		if (!operation.contains("Create Bill"))
			return;
		verifyAdminStatus();
		if (!this.AdminVerified)
			return;
		GenerateBill();

	}

}
