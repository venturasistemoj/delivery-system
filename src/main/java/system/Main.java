package system;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Delivery system application class that provides a user interface.
 * @author Wilson Ventura
 * @since 2023
 */
public class Main {

	public static void main(String[] args) {

		DeliverySystem deliverySystem = new DeliverySystem();
		DeliveryControlSystem controlSystem = new DeliveryControlSystem(deliverySystem);

		Scanner scanner = new Scanner(System.in);
		System.out.println("► Delivery Control System ◄");
		int option;

		do {
			System.out.println("[1] Insert delivery item;");
			System.out.println("[2] Insert delivery point;");
			System.out.println("[3] Insert truck;");
			System.out.println("[4] Associate item to delivery point;");
			System.out.println("[5] Associate delivery point to truck;");
			System.out.println("[6] Perform deliveries;");
			System.out.println("[0] Close.");
			System.out.println("Enter the desired option number: ");

			try {

				option = scanner.nextInt();
				scanner.nextLine(); // consumes the new line after the number

				switch (option) {
				case 1:
					if( deliverySystem.insertDeliveryItem() ) {
						System.out.println("Delivery item entered successfully.\n");
						break;
					} else { // Java 13 new string text blocks
						System.out.println("""
								Delivery items list complete!\n
								Need to associate item with address.\n
								""" );
						break;
					}
				case 2:  // Java 13 new switch expressions
					String out1 = switch (deliverySystem.insertDeliveryPoint()) {
					case SUCCESS -> "Delivery point entered successfully.\n";
					case LOCATION_FULL -> """
							Delivery point list complete!\n
							Need to associate delivery point with truck.\n
							""";
					case LOCATION_EXISTS -> "Delivery point already registered!\n";
					case ERROR -> "Invalid input. Please try again.\n";
					default -> "System error.\n";
					};
					System.out.println(out1);
					break;
				case 3:
					if( deliverySystem.insertTruck() ) {
						System.out.println("Truck entered successfully.\n");
						break;
					} else {
						System.out.println("Truck fleet complete!\n");
						break;
					}
				case 4:
					String out2 = switch (deliverySystem.associateItemToLocation()) {
					case SUCCESS -> "Item associated successfully.\n";
					case ITEM_NOT_FOUND -> "Item not found!\n";
					case LOCATION_NOT_FOUND -> "Location not found!\n";
					case LOCATION_FULL -> "Location items list complete!\n";
					case ERROR -> "Invalid input. Please try again.\n";
					default -> "System error.\n";
					};
					System.out.println(out2);
					break;
				case 5:
					String out3 = switch (deliverySystem.associateLocationToTruck()) {
					case SUCCESS -> "Location associated successfully.\n";
					case LOCATION_NOT_FOUND -> "Location not found!\n";
					case TRUCK_NOT_FOUND -> "Truck not found!\n";
					case EMPTY_LIST -> "Location delivery items list empty!\n";
					case LOCATION_FULL -> "Truck delivery points complete!\n";
					case ERROR -> "Invalid input. Please try again.\n";
					default -> "System error.\n";
					};
					System.out.println(out3);
					break;
				case 6:
					controlSystem.performDeliveries();
					break;
				case 0:
					System.err.println("Shut down system ...");
					break;
				default:
					break;
				}
			} catch (InputMismatchException e) {
				System.err.println("Invalid Input! Please enter a number option.");
				// Reset option to show menu again
				scanner.nextLine();
				option = -1;
			} catch (NoSuchElementException | IllegalStateException e) {
				System.err.println("Error while reading input. Please try again.");
				scanner.nextLine();
				option = -1;
			}

		} while (option != 0);

		scanner.close(); // close the scanner after loop

	} // main

}
