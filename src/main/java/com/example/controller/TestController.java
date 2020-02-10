package com.example.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.sql.*;
import java.text.SimpleDateFormat;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.example.model.ParcelDatabase;



@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping({ "/securitydashboard"})

public class TestController implements CommandLineRunner{

	@Autowired
    private JavaMailSender javaMailSender;
	
	@Override
    public void run(String... args) {
		
	}
	
	
    
	void sendEmailCreate(String mail,String ParcelID, String cname,String r_time,String empname,String otp){
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(mail);
        msg.setSubject("Parcel Received");
        msg.setText("Hello "+empname+"!! \n Your Parcel of "+cname+" has been received by security guard.\n Parcel ID: "+ParcelID+" at time "+r_time+"\n Your OTP is "+otp);

        javaMailSender.send(msg);


	}

	private static char[] generateOTP() {
		String numbers = "1234567890";
		Random random = new Random();
		char[] otp = new char[4];
  
		for(int i = 0; i< 4 ; i++) {
		   otp[i] = numbers.charAt(random.nextInt(numbers.length()));
		}
		
		return otp;
	 }

	private  List<ParcelDatabase> ParcelDatabases = createList();

	@GetMapping(produces =  "application/json") 
	public List<ParcelDatabase> firstPage() {
		return ParcelDatabases;
	}
	
	private static List<ParcelDatabase> createList() {
		List<ParcelDatabase> ParcelDatabases=new ArrayList<ParcelDatabase>();
		
		try (Connection conn = DriverManager.getConnection(
			"jdbc:postgresql://127.0.0.1:5432/dashboard", "postgres", "admin");
			
		 PreparedStatement preparedStatement = conn.prepareStatement("Select *from public.\"Parcel_Details\" where \"Status\"= ? ")) {
			preparedStatement.setString(1,"Received");
			System.out.println("here1");
		ResultSet resultSet = preparedStatement.executeQuery();
			System.out.println("here2");
		while (resultSet.next()) {

			String id = resultSet.getString("EmpID");
			String parcel=resultSet.getString("ParcelID");
			String cname=resultSet.getString("Company_name");
			String rack=resultSet.getString("RackNo");
			String note=resultSet.getString("Note");
			String r_time=resultSet.getString("Receive_Time");
			String d_time=resultSet.getString("Deliver_Time");
			String status=resultSet.getString("Status");
			String nodays=resultSet.getString("No_Days");
			System.out.println(nodays);
			
			
			//int parcelid = resultSet.getInt("ParcelID");
			System.out.println(id);
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
			obj.setno_days(nodays);
			//obj.setSalary(parcelid);
			// Timestamp -> LocalDateTime
			
			String c_time;
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
			Date date = new Date();  
			System.out.println(formatter.format(date));  
			c_time=formatter.format(date);
			Date d1=null;
			Date d2=null;
			d1=formatter.parse(c_time);
			d2=formatter.parse(r_time);
			long diff=d1.getTime()-d2.getTime();
			long diffDays = diff / (24 * 60 * 60 * 1000);
			obj.settime_diff(Long.toString(diffDays));

			ParcelDatabases.add(obj);
			System.out.println("here4");
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
			Connection conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/dashboard", "postgres", "admin");
			System.out.println("Receiver id:"+user.getrecId());

			if(user.getrecId()==null)
			{
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
		Date date = new Date();  
		System.out.println(formatter.format(date));  
		user.setr_time(formatter.format(date));
		user.setstatus("Received");
		String phone=user.getephone();
		PreparedStatement preparedStatement=conn.prepareStatement("select \"EmpID\",\"EmpEmail\",\"EmpName\" from public.\"Employee_Database\" where \"EmpPhone\" = ?");
		preparedStatement.setString(1,phone);
		ResultSet resultSet = preparedStatement.executeQuery();
			System.out.println("here2");
			String mail="";
			String empname="";
		while (resultSet.next()) {

			String id = resultSet.getString("EmpID");
			 mail=resultSet.getString("EmpEmail");
			 empname=resultSet.getString("EmpName");
			user.setEmpId(id);
			user.setemail(mail);
			
			//int parcelid = resultSet.getInt("ParcelID");
			System.out.println("here3");
			
		}
		user.settime_diff("0");
		ParcelDatabases.add(user);
		char[] a=generateOTP();
		String o = new String(a);
		System.out.println(o);
	    preparedStatement = conn.prepareStatement("insert into public.\"Parcel_Details\" (\"ParcelID\",\"Note\",\"EmpID\",\"Company_name\",\"Status\",\"Receive_Time\",\"RackNo\",\"otp\") values(?,?,?,?,?,?,?,?)");
		preparedStatement.setString(1, user.getParcelID());
			preparedStatement.setString(2, user.getnote());
			preparedStatement.setString(3, user.getEmpId());
			preparedStatement.setString(4, user.getcname());
			preparedStatement.setString(5,user.getstatus());
			preparedStatement.setString(6,user.getr_time());
			preparedStatement.setString(7,user.getrack());
			preparedStatement.setString(8,o);
			preparedStatement.executeUpdate();

			sendEmailCreate(mail,user.getParcelID(),user.getcname(),user.getr_time(),empname,o);
			
	}
	else
	{
		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^In test controller");
		System.out.println("*********************"+user.getrecId());
		System.out.println("*********************"+user.getParcelID());
		System.out.println("++++++++++++++++++++++++"+user.getotp());
	//	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  

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
		ParcelDatabase e = new ParcelDatabase();
       
		Iterator <ParcelDatabase>it = ParcelDatabases.iterator();
      
		while(it.hasNext())
		{   
			e=it.next();
			if(e.getParcelID().equals(user.getParcelID()))
			{
				ParcelDatabases.remove(e);
				break;
			}

		}
	}
		
		
	//}else
/*	{
		System.out.print("OTP did not match.Try Again");
	}	*/
		
	}
			
			}catch (SQLException e) {
				System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}

			
		
		return user;
	}
	 
	@Bean
	public JavaMailSender getJavaMailSender2() {
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
