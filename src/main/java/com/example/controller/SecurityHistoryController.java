package com.example.controller;



import java.util.ArrayList;
import java.util.List;

import java.sql.*;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.ParcelDatabase;



@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping({ "/securityhistory"})

public class SecurityHistoryController  {

	
	
	
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
			
			//obj.setSalary(parcelid);
			// Timestamp -> LocalDateTime
			
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

	

}
