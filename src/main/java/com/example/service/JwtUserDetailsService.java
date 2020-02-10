package com.example.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class JwtUserDetailsService implements UserDetailsService {
	
@Autowired
private PasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		final String Sql = "Select *from public.\"User_Credentials\" where username = '" + username+"';";
		System.out.println(Sql);
		
		Connection conn;
		try{
		conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/dashboard", "postgres", "admin");
		final PreparedStatement preparedStatement = conn.prepareStatement(Sql);
		System.out.println("I am here");
		final ResultSet resultSet = preparedStatement.executeQuery();	
		if (resultSet.next() == true) {	
			System.out.println("I am here1");
				final String password = resultSet.getString("password");
				final String EncodedPass = bCryptPasswordEncoder.encode(password);
				System.out.println("SQL encoded:"+EncodedPass);
				System.out.println("SQL username:"+username);
				System.out.println("SQL password:"+password);
				return new User(username, EncodedPass,
					new ArrayList<>());
			
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}catch(SQLException s){
		s.printStackTrace();
	}
	return null;
	}
}
