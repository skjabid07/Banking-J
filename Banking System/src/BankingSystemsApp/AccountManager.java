package BankingSystemsApp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class AccountManager {
	private Connection connection;
	private Scanner scanner;

	public AccountManager(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}

	public void credit_money(long account_number) throws Exception {
		scanner.nextLine();
		System.out.print("Enter Amount: ");
		double amount = scanner.nextDouble();
		scanner.nextLine();
		System.out.print("Enter Security Pin: ");
		String securitypin = scanner.nextLine();
		connection.setAutoCommit(false);
		if (account_number != 0) {
			PreparedStatement preparedStatement = connection
					.prepareStatement("select * from accounts where account_number=? and security_pin=?");
			preparedStatement.setLong(1, account_number);
			preparedStatement.setString(2, securitypin);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				String credit_query = "update accounts set balance = balance + ? where account_number = ?";
				PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
				preparedStatement1.setDouble(1, amount);
				preparedStatement1.setLong(2, account_number);
				int affectedrows = preparedStatement1.executeUpdate();
				if (affectedrows > 0) {
					System.out.println("Amount Credited Successfully");
					connection.commit();
					connection.setAutoCommit(true);
				} else {
					System.out.println("Amount Credit Failed");
					connection.rollback();
					connection.setAutoCommit(true);
				}
			}

			else {
				System.out.println("Invalid Credential");
			}

		} else {
			System.out.println("Enter Valid Account Number");
		}
		connection.setAutoCommit(true);
	}

	public void debit_money(long account_number) throws Exception {
		scanner.nextLine();
		System.out.print("Enter Amount: ");
		double amount = scanner.nextDouble();
		scanner.nextLine();
		System.out.print("Enter Security Pin: ");
		String securitypin = scanner.nextLine();
		connection.setAutoCommit(false);
		if (account_number != 0) {
			PreparedStatement preparedStatement = connection
					.prepareStatement("select * from accounts where account_number=? and security_pin=?");
			preparedStatement.setLong(1, account_number);
			preparedStatement.setString(2, securitypin);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				double current_balance = resultSet.getDouble("balance");
				if (amount <= current_balance) {
					String debit_query = "update accounts set balance = balance - ? where account_number = ?";

					PreparedStatement preparedStatement1 = connection.prepareStatement(debit_query);
					preparedStatement1.setDouble(1, amount);
					preparedStatement1.setLong(2, account_number);
					int affectedrows = preparedStatement1.executeUpdate();
					if (affectedrows > 0) {
						System.out.println("Amount Debited Successfully");
						connection.commit();
						connection.setAutoCommit(true);
					} else {
						System.out.println("Amount Debit Failed");
						connection.rollback();
						connection.setAutoCommit(true);
					}
				} else {
					System.out.println("Insufficient Balance");
				}
			}

			else {
				System.out.println("Invalid Credential");
			}

		} else {
			System.out.println("Enter Valid Account Number");
		}
		connection.setAutoCommit(true);
	}

	public void getMoney(long account_number) throws Exception {
		scanner.nextLine();
		System.out.print("Enter Security PIN: ");
		String securitypin = scanner.nextLine();
		String get_amount = "select balance from accounts where account_number=? and security_pin=?";

		PreparedStatement preparedStatement = connection.prepareStatement(get_amount);
		preparedStatement.setLong(1, account_number);
		preparedStatement.setString(2, securitypin);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			double amount = resultSet.getDouble("balance");
			System.out.println("Current Balanace is :" + amount);

		} else {
			System.out.println("Incorrect PIN");
		}

	}

	public void transfer_money(long sender_account_number) throws Exception {
		scanner.nextLine();
		System.out.print("Enter Receiver Account Number: ");
		long receiver_account_number = scanner.nextLong();
		System.out.println("Enter Amount: ");
		double amount = scanner.nextDouble();
		scanner.nextLine();
		System.out.println("Enter Security PIN: ");
		String securitypin = scanner.nextLine();

		connection.setAutoCommit(false);
		if (sender_account_number != 0 && receiver_account_number != 0) {
			PreparedStatement preparedStatement = connection
					.prepareStatement("select * from accounts where account_number=? and security_pin=?");
			preparedStatement.setLong(1, sender_account_number);
			preparedStatement.setString(2, securitypin);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				double current_balance = resultSet.getDouble("balance");
				if (amount <= current_balance) {
					String debit_query = "update accounts set balance = balance - ? where account_number=?";
					String credit_query = "update accounts set balance = balance + ? where account_number=?";
					PreparedStatement preparedStatement2 = connection.prepareStatement(debit_query);
					PreparedStatement preparedStatement3 = connection.prepareStatement(credit_query);
					preparedStatement2.setDouble(1, amount);
					preparedStatement2.setLong(2, sender_account_number);
					preparedStatement3.setDouble(1, amount);
					preparedStatement3.setLong(2, receiver_account_number);

					int affectedrows = preparedStatement2.executeUpdate();
					int affecyedrows1 = preparedStatement3.executeUpdate();

					if (affectedrows > 0 && affecyedrows1 > 0) {
						System.out.println("Transaction successfull");
						connection.commit();
						connection.setAutoCommit(true);

					} else {
						System.out.println("Transaction Failed");
						connection.rollback();
						connection.setAutoCommit(true);
					}

				} else {
					System.out.println("Insufficient Balance");
				}

			}
		} else {
			System.out.println("Inavlid Account");
		}

	}

}
