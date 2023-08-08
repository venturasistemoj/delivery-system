package system;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import domain.DeliveryItem;
import domain.Location;
import domain.Truck;

/**
 * Delivery system business class that inserts items, locations, and trucks in the system
 * and associates items with locations and locations with trucks.
 * @author Wilson Ventura
 * @since 2023
 */
public class DeliverySystem {

	private Scanner scan;

	/**
	 * <bold>System</bold> data structures <bold>business rules (BR)</bold>:
	 * BR1: <code>List</code> deliveryItems -  max 60 items;
	 * BR2: <code>Queue</code> deliveryPoints - max 15 delivery adresses;
	 * BR3: <code>Set</code> truckFleet - max 3 trucks.
	 */
	private List<DeliveryItem> deliveryItems;
	private Queue<Location> deliveryPoints;
	private Set<Truck> truckFleet;

	/**
	 * Constructor initializes Scanner, deliveryPoints <code>LinkedList</code>, deliveryItems <code>ArrayList</code>,
	 * and truckFleet <code>LinkedHashSet</code> implementations.
	 */
	public DeliverySystem() {
		deliveryItems = new ArrayList<>();
		deliveryPoints = new LinkedList<>();
		truckFleet = new LinkedHashSet<>();
		scan = new Scanner(System.in);
	}

	// Access methods
	public Queue<Location> getDeliveryPoints() {
		return deliveryPoints;
	}

	public void setDeliveryPoints(Queue<Location> deliveryPoints) {
		this.deliveryPoints = deliveryPoints;
	}

	public List<DeliveryItem> getDeliveryItems() {
		return deliveryItems;
	}

	public void setDeliveryItems(List<DeliveryItem> deliveryItems) {
		this.deliveryItems = deliveryItems;
	}

	public Set<Truck> getTruckFleet() {
		return truckFleet;
	}

	public void setTruckFleet(Set<Truck> truckFleet) {
		this.truckFleet = truckFleet;
	}

	/**
	 * <code>Enum</code> to handle the results of system operations.
	 */
	public enum ResultEnum {
		SUCCESS,
		LOCATION_FULL,
		LOCATION_NOT_FOUND,
		LOCATION_EXISTS,
		ITEM_NOT_FOUND,
		EMPTY_LIST,
		TRUCK_NOT_FOUND,
		ERROR
	}

	/**
	 * Case 1: Creates a <code>DeliveryItem</code> and inserts it into the <bold>system</bold> deliveryItems list.
	 * The data structure is an ordered <code>ArrayList</code> with duplicate delivery items allowed.
	 *
	 * @return true if the delivery item was entered without violating BR1, false otherwise.
	 */
	public boolean insertDeliveryItem() {
		try {
			System.out.println("Enter item name: ");
			// Java 10 Local-Variable Type Inference
			var deliveryItem = new DeliveryItem(scan.nextLine());

			if (getDeliveryItems().size() < 60) {
				getDeliveryItems().add(deliveryItem);
				return true;
			} else
				return false;
		} catch (NoSuchElementException | IllegalStateException e) {
			System.err.println("Invalid input. Please try again.");
			return false;
		}
	}

	/**
	 * Case 2: Creates a <code>Location</code> and enqueues it into the <bold>system</bold> deliveryPoints queue.
	 * The data structure is a FIFO Queue <code>LinkedList</code> with delivery order policy at locations.
	 *
	 * @return SUCCESS if the location was entered without violating BR2 and is not duplicate,
	 * LOCATION_FULL if violates BR1, LOCATION_EXISTS if is duplicate.
	 */
	public ResultEnum insertDeliveryPoint() {
		try {
			System.out.println("Enter delivery address: ");
			var address = new Location(scan.nextLine());

			if(getDeliveryPoints().size() < 15) {
				if ( ! getDeliveryPoints().contains(address)) {
					getDeliveryPoints().add(address);
					return ResultEnum.SUCCESS;
				} else
					return ResultEnum.LOCATION_EXISTS;
			} else
				return ResultEnum.LOCATION_FULL;

		} catch (NoSuchElementException | IllegalStateException e) {
			e.printStackTrace();
			return ResultEnum.ERROR;
		}
	}

	/**
	 * Case 3: Creates a <code>Truck</code> and inserts it into the <bold>system</bold> truckFleet set.
	 * The data structure is a <code>LinkedHashSet</code> sorted by input order with duplicate trucks not allowed.
	 *
	 * @return true if the truck was entered without violating BR3, false otherwise.
	 */
	public boolean insertTruck() {
		try {
			System.out.println("Enter truck license plate: ");
			var truck = new Truck(scan.nextLine());

			if (getTruckFleet().size() < 3) {
				getTruckFleet().add(truck);
				return true;
			} else
				return false;
		} catch (NoSuchElementException | IllegalStateException e) {
			System.err.println("Invalid input. Please try again.");
			return false;
		}
	}

	/**
	 * Case 4: Associates a registered delivery item with a registered location.
	 *
	 * @return SUCCESS if the item was inserted into the <bold>business</bold> locationItems list without violating
	 * the data structure policy; ITEM_NOT_FOUND if the item was not registered; LOCATION_NOT_FOUND if the location
	 * was not registered; LOCATION_FULL if the location locationItems list is full.
	 */
	public ResultEnum associateItemToLocation() {
		try {
			// search item in system deliveryItems
			System.out.println("Enter item name: ");
			String itemName = scan.nextLine();
			Optional<DeliveryItem> foundItem = getDeliveryItems().stream()
					.filter(item -> item.getItemName().equals(itemName))
					.findFirst();

			// search location in system deliveryPoints
			System.out.println("Enter delivery address: ");
			Optional<Location> foundLocation = searchLocation(scan.nextLine());

			/**
			 * Inserts the item into the business locationItems and removes it from the system deliveryItems.
			 * If there are duplicate items in the deliveryItems list, only the first one will be
			 * removed in order to not remove duplicate items from other locations.
			 */
			if (foundItem.isPresent() && foundLocation.isPresent()) {
				if (foundLocation.get().getLocationItems().size() < 4) {
					foundLocation.get().getLocationItems().add(foundItem.get());
					getDeliveryItems().remove(foundItem.get());
					return ResultEnum.SUCCESS;
				} else
					return ResultEnum.LOCATION_FULL;
			} else if ( ! foundItem.isPresent())
				return ResultEnum.ITEM_NOT_FOUND;
			else
				return ResultEnum.LOCATION_NOT_FOUND;

		} catch (NoSuchElementException | IllegalStateException e) {
			e.printStackTrace();
			return ResultEnum.ERROR;
		}
	}

	/**
	 * Case 5: Associates a registered location with a registered truck.
	 *
	 * @return SUCCESS if the location was queued into <bold>business</bold> truckPoints queue without violate
	 * the data structure policy and if the location locationItems list is not empty; EMPTY_LIST if the location
	 * locationItems list is empty; LOCATION_FULL if the truck truckPoints queue is full.
	 * LOCATION_NOT_FOUND if the location was not registered; TRUCK_NOT_FOUND if the truck was not registered;
	 */
	public ResultEnum associateLocationToTruck() {
		try {
			// search location in system deliveryPoints
			System.out.println("Enter delivery address: ");
			Optional<Location> foundLocation =  searchLocation(scan.nextLine());

			// search truck in system truckFleet
			System.out.println("Enter truck license plate: ");
			String truckPlate = scan.nextLine();
			Optional<Truck> foundTruck = getTruckFleet().stream()
					.filter(truck -> truck.getPlate().equals(truckPlate))
					.findAny();

			// Inserts the location into the business truckPoints and removes it from the system deliveryPoints.
			if (foundLocation.isPresent() && foundTruck.isPresent()) {
				if (foundTruck.get().getTruckPoints().size() < 5
						&& ! foundLocation.get().getLocationItems().isEmpty()) {
					foundTruck.get().getTruckPoints().add(foundLocation.get());
					getDeliveryPoints().remove(foundLocation.get());
					return ResultEnum.SUCCESS;
				} else if( foundLocation.get().getLocationItems().isEmpty() )
					return ResultEnum.EMPTY_LIST;
				else
					return ResultEnum.LOCATION_FULL;
			} else if ( ! foundLocation.isPresent())
				return ResultEnum.LOCATION_NOT_FOUND;
			else
				return ResultEnum.TRUCK_NOT_FOUND;
		} catch (NoSuchElementException | IllegalStateException e) {
			e.printStackTrace();
			return ResultEnum.ERROR;
		}
	}

	// helper method to avoid code repetition
	private Optional<Location> searchLocation(String locationName) {
		Optional<Location> foundLocation = getDeliveryPoints().stream()
				.filter(location -> location.getLocation().equals(locationName))
				.findFirst();
		return foundLocation;
	}

}
