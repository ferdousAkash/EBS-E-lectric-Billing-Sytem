import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class PayService extends BillDetails {
	final double perUnitPrice = 1.5;
	PayService() {
		super.finePerDay= 10;
	}

	void setBillType(String type) {
		super.billType = type;
		if(type.matches("Flat")) super.taxPercentage = .10;
		else if(type.matches("House")) super.taxPercentage = .20;
		else if(type.matches("Shop")) super.taxPercentage = .30;
	}

	void setUnits(String units) {
		super.units = Double.parseDouble(units);
	}

	void setLastdate(String lastDateString) {
		try {
			super.lastDateOfPayment = new SimpleDateFormat("dd/MM/yyyy").parse(lastDateString);
		} catch (Exception e) {
		}
	}

	void setPaymentDate(String paymentDateString) {
		try {
			super.paymentDate = new SimpleDateFormat("dd/MM/yyyy").parse(paymentDateString);
		} catch (Exception e) {
		}
	}
	@Override
	boolean isLatePayment() {
		return (super.paymentDate.compareTo(super.lastDateOfPayment) > 0);
	}

	@Override
	double grossPayment() {
		return (perUnitPrice * super.units);
	}
	@Override
	double fineAmmount() {
		if(!isLatePayment()) return 0;
		else {
			long diffInMillies = Math.abs(super.paymentDate.getTime() - super.lastDateOfPayment.getTime());
			long differenceInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
			return differenceInDays * super.finePerDay;
		}
	}

	@Override
	double taxAmmount() {
		return grossPayment() * super.taxPercentage;
	}


	@Override
	double totalPayment() {
		return (grossPayment() + fineAmmount() + taxAmmount());
	}

	
}
