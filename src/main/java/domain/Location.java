package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class that represents a delivery location in the system.
 * @author Wilson Ventura
 * @since 2023
 */
public class Location {

	private String address;

	/**
	 * Delivery items <code>List</code> <bold>business</bold> data structure (MAX 4).
	 */
	private List<DeliveryItem> locationItems;

	/**
	 * Contructor creates a location with given address, initializes its ID,
	 * and <code>ArrayList</code> implementation.
	 * @param Location address.
	 */
	public Location(String address) {
		this.address = address;
		locationItems = new ArrayList<>();
	}

	public String getLocation() {
		return address;
	}

	public List<DeliveryItem> getLocationItems() {
		return locationItems;
	}

	@Override
	public int hashCode() {
		return Objects.hash(address);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		return Objects.equals(address, other.address);
	}

}
