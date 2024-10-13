package BankingSystemsApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class BankingSystemApp {
	private static final String url = "jdbc:mysql://localhost:3306/banking_system";
	private static final String username = "root";
	private static final String password = "1234";

	public static void main(String args[]) throws Exception {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver Loaded");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			System.out.println("Connection Established");
			Scanner scanner = new Scanner(System.in);
			User user = new User(connection, scanner);
			Account account = new Account(connection, scanner);
			AccountManager accountManager = new AccountManager(connection, scanner);

			String email;
			long account_number;

			while (true) {
				System.out.println("Welcome to banking system");
				System.out.println("1. Register");
				System.out.println("2. Login");
				System.out.println("3. Exit");
				System.out.println();
				System.out.print("Enter your choice: ");
				int choice = scanner.nextInt();
				switch (choice) {
				case 1: {
					user.register();
					break;

				}
				case 2: {
					email = user.login();
					if (email != null) {
						System.out.println();
						System.out.println("User successfully logged in");
						if (!account.account_exit(email)) {
							System.out.println();
							System.out.println("1. Open a new account");
							System.out.println("2. Exit");

							if (scanner.nextInt() == 1) {
								account_number = account.openAccount(email);
								System.out.println("New account opened successfully");
								System.out.println("Account Number is: " + account_number);
							} else {
								break;
							}

						}
						account_number = account.getAccountNumber(email);
						int choice2 = 0;
						while (choice2 != 5) {
							System.out.println();
							System.out.println("1. Debit Money");
							System.out.println("2. Credit Money");
							System.out.println("3. Transfer Money");
							System.out.println("4. Check Balance");
							System.out.println("5. Logout");
							System.out.print("Enter you choice: ");
							choice2 = scanner.nextInt();
							switch (choice2) {
							case 1: {
								accountManager.debit_money(account_number);
								break;

							}
							case 2: {
								accountManager.credit_money(account_number);
								break;

							}
							case 3: {
								accountManager.transfer_money(account_number);
								break;

							}
							case 4: {
								accountManager.getMoney(account_number);
								break;

							}
							case 5: {
								break;

							}
							default:
								System.out.println("Enter Valid Choice");
								break;
							}

						}

					} else {
						System.out.println("Incorrect email and password");
					}
				}
				case 3: {
					System.out.println("Thank you for using banking system");
					System.out.println("Exiting System");
					return;

				}
				default:
					System.out.println("Enter Valid Choice");
				}

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
}