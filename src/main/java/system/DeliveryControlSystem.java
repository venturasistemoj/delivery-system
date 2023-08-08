package system;

import java.util.Comparator;

import domain.DeliveryItem;
import domain.Location;
import domain.Truck;

/**
 * Delivery system control class that handle the system delivery logic.
 * @author Wilson Ventura
 * @since 2023
 */
public class DeliveryControlSystem {

	private DeliverySystem deliverySystem;

	public DeliveryControlSystem(DeliverySystem ds) {
		deliverySystem = ds;
	}

	public DeliveryControlSystem() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Case 6: Dispatches trucks and makes deliveries.
	 * Gets a truck from the truckFleet system set, stacks delivery items on truck,
	 * performs deliveries, and empties the truck.
	 */
	public void performDeliveries() {
		if (deliverySystem.getTruckFleet().isEmpty())
			System.out.println("No truck on fleet! Need to add truck.\n");
		else {
			stackItemsOnTrucks();
			allocateRemainingPointsToTrucks();

			for (Truck truck : deliverySystem.getTruckFleet()) {
				System.out.printf("\nTruck %s route%n", truck.getPlate());
				int totalDeliveryPoints = truck.getTruckPoints().size();
				int totalItemsShipped = truck.getItemsShipped();

				while (!truck.getTruckPoints().isEmpty()) {
					Location deliveryPoint = truck.getTruckPoints().poll();
					System.out.printf("Visited the delivery point %s.\n", deliveryPoint.getLocation());
					System.out.println("The following items were delivered:");

					for (DeliveryItem item : deliveryPoint.getLocationItems())
						System.out.println(item.getItemName());
				}

				System.err.println("Total delivery points: " + totalDeliveryPoints);
				System.err.println("Total items shipped: " + totalItemsShipped);

				truck.getTruckBed().clear();
			}
		}
	}

	/**
	 * Stacks delivery items on trucks.
	 * Retrieves the HEAD of the truck's <code>Queue</code> and
	 * inserts the item at the END of the truck's <code>Deque</code>.
	 */
	private void stackItemsOnTrucks() {
		for (Truck truck : deliverySystem.getTruckFleet())
			if ( ! truck.getTruckPoints().isEmpty())
				for (Location deliveryPoint : truck.getTruckPoints())
					for (DeliveryItem item : deliveryPoint.getLocationItems()) {
						truck.getTruckBed().addLast(item);
						truck.setItemsShipped();
					}
	}

	/**
	 * Allocates delivery points from the system queue that have not been associated with a truck. Assigns to
	 * selectedTruck the truck with the shortest queue of locations. Checks if there are more trucks with the
	 * same number of points in the queue, compare its truck bed size, and assign the location to shortest.
	 * Stacks the location items on truck and update its shipped items.
	 */
	private void allocateRemainingPointsToTrucks() {
		while ( ! deliverySystem.getDeliveryPoints().isEmpty()) {
			Location currentPoint = deliverySystem.getDeliveryPoints().poll();

			final Truck[] selectedTruck = new Truck[1];

			deliverySystem.getTruckFleet().stream()
			.min(Comparator.comparingInt(truck -> truck.getTruckPoints().size()))
			.ifPresent(minTruck -> {
				selectedTruck[0] = minTruck;
			});

			if (selectedTruck[0] != null) {
				deliverySystem.getTruckFleet().stream()
				.filter(truck -> truck.getTruckPoints().size() == selectedTruck[0].getTruckPoints().size())
				.min(Comparator.comparingInt(truck -> truck.getTruckBed().size()))
				.ifPresent(truck -> selectedTruck[0] = truck);

				selectedTruck[0].getTruckPoints().add(currentPoint);
			}

			for (DeliveryItem item : currentPoint.getLocationItems()) {
				selectedTruck[0].getTruckBed().addLast(item);
				selectedTruck[0].setItemsShipped();
			}
		}
	}

}
