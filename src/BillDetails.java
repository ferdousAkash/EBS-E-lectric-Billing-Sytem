import java.util.Date;

public abstract class BillDetails {
	double units;
	String billType;
	Date lastDateOfPayment;
	//
	double taxPercentage;
	double finePerDay;
	Date paymentDate;
	//
	abstract double grossPayment();
	abstract double taxAmmount();
	abstract double fineAmmount();
	abstract double totalPayment();
	abstract boolean isLatePayment();
}
