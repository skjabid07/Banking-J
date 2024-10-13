package BankingSystemsApp;

import java.security.PrivateKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class User {

	private Connection connection;
	private Scanner scanner;

	public User(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}

	public void register() {
		scanner.nextLine();
		System.out.print("Enter Full Name: ");
		String fullname = scanner.nextLine();
		System.out.print("Enter Email: ");
		String email = scanner.nextLine();
		System.out.print("Enter Password: ");
		String password = scanner.nextLine();

		if (!user_exit(email)) {
			System.out.println("User Already Exit");
		}

		String sql = "insert into user( full_name,email,password) values (?,?,?)";

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, fullname);
			preparedStatement.setString(2, email);
			preparedStatement.setString(3, password);
			int affectedrows = preparedStatement.executeUpdate();

			if (affectedrows > 0) {
				System.out.println("Successfully Inserted");

			} else {
				System.out.println("Failed");
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public String login() {
		scanner.nextLine();
		System.out.print("Enter Email: ");
		String email = scanner.nextLine();
		System.out.print("Enter Password: ");
		String password = scanner.nextLine();

		String sql = "select * from user where email=? and password=?";

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, password);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return email;

			} else {
				return null;
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	public boolean user_exit(String email) {
		String sql = "select * from user where email=?";

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, email);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return true;

			} else {
				return false;
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;

	}

}
