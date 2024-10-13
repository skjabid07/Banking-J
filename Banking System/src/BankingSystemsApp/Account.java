package BankingSystemsApp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Account {
	private Connection connection;
	private Scanner scanner;

	public Account(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}

	public long openAccount(String email) {
		if (!account_exit(email)) {
			String sql = "insert into accounts (account_number,full_name,email,balance,security_pin) values (?,?,?,?,?)";
			scanner.nextLine();
			System.out.print("Enter Full Name: ");
			String fullname = scanner.nextLine();
			System.out.print("Enter Balance: ");
			double balance = scanner.nextDouble();
			scanner.nextLine();
			System.out.println("Enter Security PIN: ");
			String securityPin = scanner.nextLine();

			try {
				long account_number = generateAccountNumber();
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setLong(1, account_number);
				preparedStatement.setString(2, fullname);
				preparedStatement.setString(3, email);
				preparedStatement.setDouble(4, balance);
				preparedStatement.setString(5, securityPin);
				int affectedrow = preparedStatement.executeUpdate();

				if (affectedrow > 0) {
					return account_number;

				} else {
					throw new RuntimeException("Account Creation Failed");
				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		}
		throw new RuntimeException("Account Already Exit");

	}

	public long getAccountNumber(String email) {

		String sql = "select account_number from accounts where email= ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, email);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getLong("account_number");
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		throw new RuntimeException("Account doestn't exist");

	}

	private long generateAccountNumber() {
		String sql = "select account_number from accounts order by account_number desc limit 1";
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				long account_number = resultSet.getLong("account_number");
				return account_number + 1;
			} else {
				return 100100;
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return 100100;

	}

	public boolean account_exit(String email) {
		String sql = "select account_number from accounts where email=?";

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
