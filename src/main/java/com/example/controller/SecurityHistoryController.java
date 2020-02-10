package com.example.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.ParcelDatabase;



@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping({ "/securityhistory"})

public class SecurityHistoryController implements CommandLineRunner {

	@Autowired
    private JavaMailSender javaMailSender;
	
	@Override
    public void run(String... args) {
		
	}
	void sendEmail(String s,String name,String id,String dtime,String recname) {

		SimpleMailMessage msg = new SimpleMailMessage();
		
        msg.setTo(s);
        msg.setSubject("Parcel Delivered");
        msg.setText("Hello "+name+"!! \n Your Parcel has been handed over to Employee id:"+id+" Name :"+recname+" at "+dtime);

        javaMailSender.send(msg);

	}
	
	
	private  List<ParcelDatabase> ParcelDatabases = createList();

	@GetMapping(produces =  "application/json") 
	public List<ParcelDatabase> firstPage() {
		return ParcelDatabases;
	}
	
	private static List<ParcelDatabase> createList() {
		List<ParcelDatabase> ParcelDatabases=new ArrayList<ParcelDatabase>();
		
		try (Connection conn = DriverManager.getConnection(
			"jdbc:postgresql://127.0.0.1:5433/dashboard", "postgres", "admin");
			
		 PreparedStatement preparedStatement = conn.prepareStatement("Select *from public.\"Parcel_Details\" where \"Status\"= ? ")) {
			preparedStatement.setString(1,"Delivered");
			ResultSet resultSet = preparedStatement.executeQuery();
			
		while (resultSet.next()) {

			String id = resultSet.getString("EmpID");
			String parcel=resultSet.getString("ParcelID");
			String cname=resultSet.getString("Company_name");
			String rack=resultSet.getString("RackNo");
			String note=resultSet.getString("Note");
			String r_time=resultSet.getString("Receive_Time");
			String d_time=resultSet.getString("Deliver_Time");
			String status=resultSet.getString("Status");
			System.out.println(id);
			
			
			//int parcelid = resultSet.getInt("ParcelID");
			System.out.println("here3");
			ParcelDatabase obj = new ParcelDatabase();
			obj.setEmpId(id);
			
			obj.setSalary(10);
			obj.setParcelID(parcel);
			obj.setcname(cname);
			obj.setrack(rack);
			obj.setnote(note);
			obj.setr_time(r_time);
			obj.setd_time(d_time);
			obj.setstatus(status);
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
			Date d1=null;
			Date d2=null;
			d1=formatter.parse(d_time);
			d2=formatter.parse(r_time);
			long diff=d1.getTime()-d2.getTime();
			long diffDays = diff / (24 * 60 * 60 * 1000);
			obj.settime_diff(Long.toString(diffDays));
			
			ParcelDatabases.add(obj);
			
		}
		//ParcelDatabases.forEach(x -> System.out.println(x));

	} catch (SQLException e) {
		System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
	} catch (Exception e) {
		e.printStackTrace();
	}
		return ParcelDatabases;
	}

	@PostMapping
	public ParcelDatabase create(@RequestBody ParcelDatabase user) {
	
		try {
			Connection conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5433/dashboard", "postgres", "admin");
			System.out.println("Receiver id:"+user.getrecId());

			
		System.out.println("Herein security history controller");
		System.out.println("*********************"+user.getrecId());
		System.out.println("*********************"+user.getParcelID());
		System.out.println("++++++++++++++++++++++++"+user.getotp());
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  

		PreparedStatement preparedStatement=conn.prepareStatement("select \"otp\" from public.\"Parcel_Details\" where \"ParcelID\" = ?");
		preparedStatement.setString(1, user.getParcelID());
		ResultSet resultSet = preparedStatement.executeQuery();
		String genotp="";
		while (resultSet.next()) {

		
			genotp=resultSet.getString("otp");
			
						
		}
		System.out.println(genotp);
		String ot=user.getotp();
		if(genotp.equals(ot))
		{
		Date date = new Date();  
		System.out.println(formatter.format(date));  
		user.setd_time(formatter.format(date));
		user.setstatus("Delivered");

		preparedStatement = conn.prepareStatement("update public.\"Parcel_Details\" set \"RecId\"= ?, \"Deliver_Time\"=?, \"Status\"=? where \"ParcelID\" = ? ");
		preparedStatement.setString(1, user.getrecId());
		preparedStatement.setString(2, user.getd_time());
		preparedStatement.setString(3, user.getstatus());
		preparedStatement.setString(4, user.getParcelID());
		preparedStatement.executeUpdate();


		preparedStatement=conn.prepareStatement("select \"EmpID\", \"Company_name\", \"RackNo\", \"Note\", \"Receive_Time\" from public.\"Parcel_Details\" where \"ParcelID\" = ?");
		preparedStatement.setString(1, user.getParcelID());
	    resultSet = preparedStatement.executeQuery();
		String employeeid="";
		String company_name="";
		String rackno="";
		String note="";
		String rtime="";
		while (resultSet.next()) {

		
			employeeid=resultSet.getString("EmpID");
			company_name=resultSet.getString("Company_name");
			rackno=resultSet.getString("RackNo");
			note=resultSet.getString("Note");
			rtime=resultSet.getString("Receive_Time");
			user.setEmpId(employeeid);
			user.setcname(company_name);
			user.setrack(rackno);
			user.setnote(note);
			user.setr_time(rtime);
						
		}
		
		System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+employeeid);
		preparedStatement=conn.prepareStatement("select \"EmpEmail\",\"EmpName\" from public.\"Employee_Database\" where \"EmpID\" = ?");
		preparedStatement.setString(1, employeeid);
		resultSet = preparedStatement.executeQuery();
		int i=0;
		String mail="";
		String empname="";
	while (resultSet.next()) {

		
		mail=resultSet.getString("EmpEmail");
		empname=resultSet.getString("EmpName");
		i=1;
		if(i==1)
		break;
		
	}
	preparedStatement=conn.prepareStatement("select \"EmpName\" from public.\"Employee_Database\" where \"EmpID\" = (select \"RecId\" from public.\"Parcel_Details\" where \"ParcelID\"=?)");
	preparedStatement.setString(1,user.getParcelID());	
	resultSet = preparedStatement.executeQuery();
		String recname="";
	while (resultSet.next()) {

		
		recname=resultSet.getString("EmpName");
		
	}
	System.out.println("ffffffffffffffffffffff"+mail);
	sendEmail(mail,empname,user.getrecId(),user.getd_time(),recname);
	System.out.println("**************Mail Sent**********");
	Date d1=null;
	Date d2=null;
	d1=formatter.parse(user.getd_time());
	d2=formatter.parse(user.getr_time());
	long diff=d1.getTime()-d2.getTime();
	long diffDays = diff / (24 * 60 * 60 * 1000);
	user.settime_diff(Long.toString(diffDays));
				
	ParcelDatabases.add(user);
		
		
	}else
	{
		
		System.out.print("OTP did not match.Try Again");
	}	
		
			
			}catch (SQLException e) {
				System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}

			
		
		return user;
	}
	
	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);
		  
		mailSender.setUsername("iinsight.akankshas@gmail.com");
		mailSender.setPassword("menmylife2");
		  
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");
		  
		return mailSender;
	}
	
	

}
