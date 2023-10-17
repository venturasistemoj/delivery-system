package tests;

import static org.mockito.Mockito.when;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import domain.DeliveryItem;
import domain.Location;
import domain.Truck;
import system.DeliveryControlSystem;
import system.DeliverySystem;

public class DeliveryControlTest {

	@Mock
	private DeliverySystem deliverySystem;

	@InjectMocks
	private DeliveryControlSystem controlSystem;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testPerformDeliveries() {

		// truck CAM1 123 with 5 locations and 5 items
		Location l1 = new Location("Street 1");
		l1.getLocationItems().add(new DeliveryItem("item 1"));
		Location l2 = new Location("Street 2");
		l2.getLocationItems().add(new DeliveryItem("item 2"));
		Location l3 = new Location("Street 3");
		l3.getLocationItems().add(new DeliveryItem("item 3"));
		Location l4 = new Location("Street 4");
		l4.getLocationItems().add(new DeliveryItem("item 4"));
		Location l5 = new Location("Street 5");
		l5.getLocationItems().add(new DeliveryItem("item 5"));
		Truck t1 = new Truck("TRUCK1 123");
		t1.getTruckPoints().add(l1);
		t1.getTruckPoints().add(l2);
		t1.getTruckPoints().add(l3);
		t1.getTruckPoints().add(l4);
		t1.getTruckPoints().add(l5);

		// truck CAM2 456 with 2 locations and 8 items
		Location l6 = new Location("Street 6");
		l6.getLocationItems().add(new DeliveryItem("item 6"));
		l6.getLocationItems().add(new DeliveryItem("item 7"));
		l6.getLocationItems().add(new DeliveryItem("item 8"));
		l6.getLocationItems().add(new DeliveryItem("item 9"));
		Location l7 = new Location("Street 7");
		l7.getLocationItems().add(new DeliveryItem("item 10"));
		l7.getLocationItems().add(new DeliveryItem("item 11"));
		l7.getLocationItems().add(new DeliveryItem("item 12"));
		l7.getLocationItems().add(new DeliveryItem("item 13"));
		Truck t2 = new Truck("TRUCK2 456");
		t2.getTruckPoints().add(l6);
		t2.getTruckPoints().add(l7);

		// truck CAM2 456 with 3 locations and 6 items
		Location l8 = new Location("Street 8");
		l8.getLocationItems().add(new DeliveryItem("item 14"));
		l8.getLocationItems().add(new DeliveryItem("item 15"));
		Location l9 = new Location("Street 9");
		l9.getLocationItems().add(new DeliveryItem("item 16"));
		l9.getLocationItems().add(new DeliveryItem("item 17"));
		Location l10 = new Location("Street 10");
		l10.getLocationItems().add(new DeliveryItem("item 18"));
		l10.getLocationItems().add(new DeliveryItem("item 19"));
		Truck t3 = new Truck("TRUCK3 789");
		t3.getTruckPoints().add(l8);
		t3.getTruckPoints().add(l9);
		t3.getTruckPoints().add(l10);

		/**
		 * LinkedHashSet guarantees the trucks's delivery order.
		 */
		Set<Truck> truckFleet = new LinkedHashSet<>();
		truckFleet.add(t1);
		truckFleet.add(t2);
		truckFleet.add(t3);

		when(deliverySystem.getTruckFleet()).thenReturn(truckFleet);

		// system locations without trucks associated
		Location l11 = new Location("Street 11");
		l11.getLocationItems().add(new DeliveryItem("item 20"));
		Location l12 = new Location("Street 12");
		l12.getLocationItems().add(new DeliveryItem("item 21"));
		l12.getLocationItems().add(new DeliveryItem("item 22"));
		l12.getLocationItems().add(new DeliveryItem("item 23"));
		Location l13 = new Location("Street 13");
		l13.getLocationItems().add(new DeliveryItem("item 24"));
		l13.getLocationItems().add(new DeliveryItem("item 25"));

		Queue<Location> deliveryPoints = new LinkedList<>();
		deliveryPoints.add(l11);
		deliveryPoints.add(l12);
		deliveryPoints.add(l13);

		when(deliverySystem.getDeliveryPoints()).thenReturn(deliveryPoints);

		// tests
		controlSystem.performDeliveries();
	}

}
