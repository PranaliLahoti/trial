package com.example.controller;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping({ "/employeedashboard"})

public class EmployeeController {

	private  List<ParcelDatabase> ParcelDatabases = createList();

	@GetMapping(produces =  "application/json") 
	public List<ParcelDatabase> firstPage() {
		return ParcelDatabases;
	}
	
	private static List<ParcelDatabase> createList() {
        List<ParcelDatabase> ParcelDatabases=new ArrayList<ParcelDatabase>();
        
		
		try (Connection conn = DriverManager.getConnection(
			"jdbc:postgresql://127.0.0.1:5432/dashboard", "postgres", "admin");
         PreparedStatement preparedStatement = conn.prepareStatement("Select *from public.\"Parcel_Details\" where \"EmpID\" = ? and \"Status\"=? "))
          {
            preparedStatement.setString(1,"123");
            preparedStatement.setString(2,"Received");
     // preparedStatement.setInt(1,123);
			System.out.println("here1");
		ResultSet resultSet = preparedStatement.executeQuery();
			System.out.println("here2");
		while (resultSet.next()) {

			String id = resultSet.getString("EmpID");
			String parcel=resultSet.getString("ParcelID");
			String cname=resultSet.getString("Company_name");
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
			obj.setr_time(r_time);
			obj.setd_time(d_time);
			obj.setstatus(status);
			
			//obj.setSalary(parcelid);
			// Timestamp -> LocalDateTime
			
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
		System.out.println("************************heresddtfy"+user.getParcelID());
		try{
			Connection conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/dashboard", "postgres", "admin");
			PreparedStatement preparedStatement=conn.prepareStatement("select \"EmpID\",\"EmpEmail\",\"EmpName\" from public.\"Employee_Database\" where \"EmpID\" =  (select \"EmpID\" from public.\"Parcel_Details\" where \"ParcelID\"=?)");
			preparedStatement.setString(1,user.getParcelID());
			ResultSet resultSet = preparedStatement.executeQuery();
			String mail="";
			String empname="";
			String id="";
			while (resultSet.next()) {

				id = resultSet.getString("EmpID");
			 	mail=resultSet.getString("EmpEmail");
			 	empname=resultSet.getString("EmpName");
			}

			preparedStatement=conn.prepareStatement("select \"Company_name\",\"RackNo\" from public.\"Parcel_Details\" where \"ParcelID\" =?");
			preparedStatement.setString(1,user.getParcelID());
			resultSet = preparedStatement.executeQuery();
			String company_name="";
			String rack_no="";
			while (resultSet.next()) {

				company_name = resultSet.getString("Company_name");
				rack_no=resultSet.getString("RackNo");
			}
			
	//	sendEmail(id,mail,empname,company_name,rack_no);
		
		}catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        return user;
        
    }

}
