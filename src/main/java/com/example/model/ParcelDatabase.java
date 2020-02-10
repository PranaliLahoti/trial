package com.example.model;


public class ParcelDatabase {
	private String empId;
	private String parcelID;
	private double salary;
	private String cname;
	private String rack;
	private String note;
	private String r_time;
	private String d_time;
	private String status;
	private String ephone;
	private String email;
	private String recId;
	private String recname;
	private String recphone;
	private String no_days;
	public ParcelDatabase() {
	}
	public String getrecId()
	{
		return recId;
	}
	public void setrecId(String recId){
		this.recId=recId;
	}
	public String getno_days()
	{
		return no_days;
	}
	public void setno_days(String no_days){
		this.no_days=no_days;
	}
	public String getrecname()
	{
		return recname;
	}
	public void setrecname(String recname){
		this.recname=recname;
	}
	public String getrecphone()
	{
		return recphone;
	}
	public void setrecphone(String recphone){
		this.recphone=recphone;
	}
	public String getcname(){
		return cname;
	}
	public void setcname(String cname){
		this.cname=cname;
	}
	public String getephone(){
		return ephone;
	}
	public void setephone(String ephone){
		this.ephone=ephone;
	}
	public String getemail(){
		return email;
	}
	public void setemail(String email){
		this.email=email;
	}
	public String getnote(){
		return note;
	}
	public void setnote(String note){
		this.note=note;
	}

	public String getr_time(){
		return r_time;
	}
	public void setr_time(String r_time){
		this.r_time=r_time;
	}

	public String getd_time(){
		return d_time;
	}
	public void setd_time(String d_time){
		this.d_time=d_time;
	}

	public String getstatus(){
		return status;
	}
	public void setstatus(String status){
		this.status=status;
	}
	public String getrack(){
		return rack;
	}


	public void setrack(String rack){
		this.rack=rack;
	}

	public String getParcelID( )
	{
		return parcelID;
	}
	public void setParcelID(String parcelID)
	{
		this.parcelID=parcelID;
	}
	
	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((empId == null) ? 0 : empId.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((parcelID == null) ? 0 : parcelID.hashCode());
		result = prime * result + ((cname == null) ? 0 : cname.hashCode());
		result = prime * result + ((rack == null) ? 0 : rack.hashCode());
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		result = prime * result + ((r_time == null) ? 0 : r_time.hashCode());
		result = prime * result + ((d_time == null) ? 0 : d_time.hashCode());
		result = prime * result + ((ephone == null) ? 0 : ephone.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((recId == null) ? 0 : recId.hashCode());
		result = prime * result + ((recname == null) ? 0 : recname.hashCode());
		result = prime * result + ((recphone == null) ? 0 : recphone.hashCode());
	//	long temp;
	//	temp = Double.doubleToLongBits(salary);
	//	result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParcelDatabase other = (ParcelDatabase) obj;
		if (empId == null) {
			if (other.empId != null)
				return false;
		} else if (!empId.equals(other.empId))
			return false;

		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		
		if (parcelID == null) {
			if (other.parcelID != null)
					return false;
		} else if (!parcelID.equals(other.parcelID))
				return false;
		
		if (cname == null) {
			if (other.cname != null)
				return false;
		} else if (!cname.equals(other.cname))
			return false;

		if (rack == null) {
			if (other.rack != null)
				return false;
		} else if (!rack.equals(other.rack))
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (r_time == null) {
			if (other.r_time != null)
					return false;
		} else if (!r_time.equals(other.r_time))
			return false;
		if (d_time == null) {
			if (other.d_time != null)
					return false;
		} else if (!d_time.equals(other.d_time))
			return false;
		if (ephone == null) {
			if (other.ephone != null)
				return false;
		} else if (!ephone.equals(other.ephone))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
				return false;		
		if (recId == null) {
			if (other.recId != null)
				return false;
		} else if (!recId.equals(other.recId))
				return false;
		if (recname == null) {
			if (other.recname != null)
				return false;
		} else if (!recname.equals(other.recname))
				return false;
		if (recphone == null) {
			if (other.recphone != null)
				return false;
		} else if (!recphone.equals(other.recphone))
				return false;						
		if (Double.doubleToLongBits(salary) != Double.doubleToLongBits(other.salary))
			return false;
		return true;
	}

}

