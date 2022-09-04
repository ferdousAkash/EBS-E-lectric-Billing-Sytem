public class Registration implements CustomerDetails{
	private String name,holding,road,city,occupation, emailid, phone, meterid,userid,password;
	@Override
	public synchronized void setCustomerName(String name) {
		this.name=name;
	}

	@Override
	public synchronized void setHoldingNumber(String address) {
		holding=address;
	}

	@Override
	public synchronized void setRoadNumber(String address) {
		road=address;
	}

	@Override
	public synchronized void setCity(String address) {
		city=address;
	}

	@Override
	public synchronized void setOccupation(String occupation) {
		this.occupation=occupation;
	}

	@Override
	public synchronized void setPhoneNumber(String phone) {
		this.phone=phone;
	}

	@Override
	public synchronized void setEmailID(String emailid) {
		this.emailid=emailid;
	}

	@Override
	public synchronized void setMeterID(String meterid) {
		this.meterid=meterid;		
	}

	@Override
	public synchronized String getCustomerName() {
		return name;
	}

	@Override
	public String getAddress() {
		return "House: "+holding+" Road: "+road+" City: "+city;
	}

	@Override
	public String getPhoneNUmber() {
		return phone;
	}

	@Override
	public String getOccupation() {
		return occupation;
	}

	@Override
	public String getEmailID() {
		return emailid;
	}

	@Override
	public String getMeterID() {
		return meterid;
	}

	@Override
	public synchronized void setUserID(String userid) {
		this.userid=userid;
		}

	@Override
	public synchronized void setPassword(String password) {
		this.password=password;
	}

	@Override
	public String getUserID() {
		return userid;
	}

	@Override
	public String getPassword() {
		return password;
	}
	public boolean checkString(String str) {
	    char ch;
	    boolean capitalFlag = false;
	    boolean lowerCaseFlag = false;
	    boolean numberFlag = false;
	    for(int i=0;i < str.length();i++) {
	        ch = str.charAt(i);
	        if( Character.isDigit(ch)) {
	            numberFlag = true;
	        }
	        else if (Character.isUpperCase(ch)) {
	            capitalFlag = true;
	        }
	        else if (Character.isLowerCase(ch)) {
	            lowerCaseFlag = true;
	        }
	        if(numberFlag && capitalFlag && lowerCaseFlag)
	            return true;
	    }
	    return false;
	}
}
