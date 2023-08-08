package domain;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/**
 * Class that represents a truck in the system.
 * @author Wilson Ventura
 * @since 2023
 */
public class Truck {

	private String licensePlate;

	/**
	 * Location <code>Queue</code> <bold>business</bold> data structure (MAX 5).
	 * FIFO delivery order policy at locations.
	 */
	private Queue<Location> truckPoints;

	/**
	 * Delivery items <code>Deque</code> <bold>business</bold> data structure (MAX 20).
	 * Items must be stacked in the truck bed in the reverse order of the
	 * <code>Queue</code> exit so that the first in the queue are delivered.
	 */
	private Deque<DeliveryItem> truckBed;

	private int itemsShipped;

	/**
	 * Constructor creates a truck with given license plate and initializes
	 * <code>LinkedList Queue</code> and <code>ArrayDeque Stack</code> implementations.
	 * @param Truck licence plate.
	 */
	public Truck(String plate) {
		licensePlate = plate;
		truckPoints = new LinkedList<>();
		truckBed = new ArrayDeque<>();
	}

	public String getPlate() {
		return licensePlate;
	}

	public Queue<Location> getTruckPoints() {
		return truckPoints;
	}

	public Deque<DeliveryItem> getTruckBed() {
		return truckBed;
	}

	public int getItemsShipped() {
		return itemsShipped;
	}

	public void setItemsShipped() {
		itemsShipped++;
	}

	@Override
	public int hashCode() {
		return Objects.hash(licensePlate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		Truck other = (Truck) obj;
		return Objects.equals(licensePlate, other.licensePlate);
	}

}
