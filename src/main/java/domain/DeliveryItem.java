package domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Class that represents a delivery item in the system.
 * @author Wilson Ventura
 * @since 2023
 */
public class DeliveryItem {

	private UUID itemID;
	private String itemName;

	/**
	 * Contructor creates a <code>DeliveryItem</code> with given name and initializes its ID.
	 * @param Item name.
	 */
	public DeliveryItem(String deliveryName) {
		itemID = UUID.randomUUID();
		itemName = deliveryName;
	}

	public void setItemName(String name) {
		itemName = name;
	}

	public String getItemName() {
		return itemName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(itemID, itemName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		DeliveryItem other = (DeliveryItem) obj;
		return Objects.equals(itemID, other.itemID)
				&& Objects.equals(itemName, other.itemName);
	}

}
