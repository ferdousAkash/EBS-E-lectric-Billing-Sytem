import java.io.*;
import java.util.*;

public class Payment extends PayService implements Runnable {
    PayService payService;
    int userIndex, dueBills;
    double currentDue = 0;
    boolean userFound;
    String dueString, currentDate;
    Scanner inp = new Scanner(System.in);
    String tempFileName = "temp.txt";
    File tempFile = new File(tempFileName);

    void verifyUser() {
        String content, search;
        userFound = false;

        try {
            System.out.println("Enter Username: ");
            search = inp.nextLine();
            for (int b = 0; b < Test.highestUser; b++) {
                File file = new File("Customer" + b + ".txt");
                if (!file.exists())
                    break;
                BufferedReader read = new BufferedReader(new FileReader(file));
                content = read.readLine();
                if (!content.split(" ")[1].matches(search)) {
                    continue;
                } else {
                    System.out.println("Enter Password: ");
                    search = inp.nextLine();
                    content = read.readLine();
                    if (content.split(" ")[1].matches(search)) {
                        userFound = true;
                        userIndex = b;
                    }
                    read.close();
                    break;
                }

            }
        } catch (Exception c) {
            c.printStackTrace();
        }
    }

    void processBillInformation(boolean showHistory) {
        dueString = "";
        dueBills = 0;
        try {
            String fileName = "Customer" + this.userIndex + "Bill.txt";
            File file = new File(fileName);
            if (!file.exists())
                return;
            if (showHistory)
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
                if (showHistory)
                    System.out.println(billInfo + "\n\n");
                if (content.split(",")[4].matches("Due")) {
                    dueString += content.split(",")[0] + " | ";
                    dueBills++;
                }
            }
            reader.close();

            if (!showHistory) {
                System.out.println("You have " + dueBills + " due bills.");
                if (dueBills > 0) {
                    System.out.println("The due bills are: ");
                    System.out.println(dueString);
                    System.out.println("Enter the serial of payable bill.");
                }
            }
        } catch (Exception e) {
        }
    }

    void showSerializedBillInfo(String serial) {
        try {
            String fileName = "Customer" + this.userIndex + "Bill.txt";
            File file = new File(fileName);
            if (!file.exists())
                return;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String content;
            while ((content = reader.readLine()) != null) {
                if (!content.split(",")[0].equals(serial))
                    continue;
                super.setUnits(content.split(",")[1]);
                super.setBillType(content.split(",")[2]);
                super.setLastdate(content.split(",")[3]);
                super.setPaymentDate(currentDate);
                String billInfo = "Bill No              :  " + content.split(",")[0];
                billInfo = billInfo + "\n" + "Bill Status          :  " + content.split(",")[4];
                billInfo = billInfo + "\n" + "Units Used           :  " + content.split(",")[1];
                billInfo = billInfo + "\n" + "Type                 :  " + content.split(",")[2];
                billInfo = billInfo + "\n" + "Last Date of payment :  " + content.split(",")[3];
                billInfo = billInfo + "\n" + "Gross Ammount        :  " + String.format("%.2f", super.grossPayment());
                billInfo = billInfo + "\n" + "Tax Ammount          :  " + String.format("%.2f", super.taxAmmount());
                billInfo = billInfo + "\n" + "Fine Ammount         :  " + String.format("%.2f", super.fineAmmount());
                billInfo = billInfo + "\n" + "Total Ammount        :  " + String.format("%.2f", super.totalPayment());
                currentDue = super.totalPayment();
                System.out.println(billInfo + "\n\n");
                break;
            }
            reader.close();

        } catch (Exception e) {
        }
    }

    void paymentTransaction(String serial) {
        System.out.println(serial);
        String fileName = "Customer" + this.userIndex + "Bill.txt";
        File file = new File(fileName);
        if (!file.exists())
            return;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            FileWriter writer = new FileWriter(tempFile);
            String content;
            while ((content = reader.readLine()) != null) {
                if (!content.split(",")[0].equals(serial)) {
                    writer.write(content + "\n");
                } else {
                    String line = content.split(",")[0] + "," + content.split(",")[1];
                    line = line + "," + content.split(",")[2] + "," + content.split(",")[3] + ",Paid," + currentDate
                            + "\n";
                    writer.write(line);
                }
            }
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

        } catch (Exception e) {

        }
    }

    public Payment() {
        new Thread(this, "Payment");
    }

    @Override
    public void run() {
        verifyUser();
        if (!userFound) {
            System.out.println("Wrong Username or Password.");
            return;
        }
        System.out.println("Bill History || Pay || Back");
        String choice = inp.nextLine();
        if (choice.matches("Bill History")) {
            processBillInformation(true);

        } else if (choice.matches("Pay")) {
            processBillInformation(false);
            if (dueBills == 0) {
                return;
            }
            String serialNo = inp.nextLine();
            String checkString = serialNo + " |";
            while (!dueString.contains(checkString)) {
                System.out.println("Entered input is not a payable bill serial.");
                System.out.println("Enter serial: / Exit");
                serialNo = inp.nextLine();
                checkString = serialNo + " |";
                if (serialNo.equals("Exit"))
                    return;
            }
            System.out.println("Enter date: dd/mm/yyyy");
            currentDate = inp.nextLine();
            showSerializedBillInfo(serialNo);

            System.out.println("Enter required payment: ");
            double payedAmmount = inp.nextDouble();
            if (payedAmmount < currentDue) {
                System.out.println("Transaction Error. Insufficient balance.");
                return;
            } else {
                paymentTransaction(serialNo);
                if (payedAmmount > currentDue) {
                    System.out.println("Bill serial " + serialNo + ": payed succesfully");
                    System.out.println("Returned extra ammount: " + String.format("%.2f", (payedAmmount - currentDue)));
                    return;
                }
                System.out.println("Bill serial " + serialNo + ": payed succesfully");
            }

        }
    }

}
