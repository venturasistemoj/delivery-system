package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import domain.DeliveryItem;
import domain.Location;
import domain.Truck;
import system.DeliverySystem;
import system.DeliverySystem.ResultEnum;

public class DeliverySystemTest {

	@Mock
	private Scanner scan;

	@InjectMocks
	private DeliverySystem deliverySystem;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testCreateDeliveryItem() {

		// mock behavior config
		when(scan.nextLine()).thenReturn("Item tests");

		// tests
		boolean result = deliverySystem.insertDeliveryItem();

		//verification
		assertTrue(result);
		assertTrue(deliverySystem.getDeliveryItems().size() < 60);
	}

	@Test
	public void testCreateLocation() {

		when(scan.nextLine()).thenReturn("Street tests");

		ResultEnum result = deliverySystem.insertDeliveryPoint();

		assertEquals(ResultEnum.SUCCESS, result);
		assertTrue(deliverySystem.getDeliveryPoints().size() < 15);
	}

	@Test
	public void testCreateTruck() {

		when(scan.nextLine()).thenReturn("Plate tests");

		boolean result = deliverySystem.insertTruck();

		assertTrue(result);
		assertTrue(deliverySystem.getTruckFleet().size() < 3);
	}

	@Test
	public void testAssociateItemToLocation() {

		/* Creating a spy of the actual DeliverySystem instance. A spy is a partially fake object that allows us to
		 * keep the original behavior of methods, but also override some behavior. Here we are using spy(deliverySystem)
		 * which will allow us to configure the behavior of the getDeliveryItems() and getDeliveryPoints() methods
		 * specifically for this tests.
		 */
		DeliverySystem deliverySystemSpy = spy(deliverySystem);

		// Mocking behavior
		when(scan.nextLine()).thenReturn("Item tests").thenReturn("Street tests");

		// Configuring objects involved
		DeliveryItem item = new DeliveryItem("Item tests");
		Location location = new Location("Street tests");

		when(deliverySystemSpy.getDeliveryItems()).thenReturn(new ArrayList<>(Collections.singletonList(item)));
		when(deliverySystemSpy.getDeliveryPoints()).thenReturn(new LinkedList<>(Collections.singletonList(location)));

		// Test
		ResultEnum result = deliverySystemSpy.associateItemToLocation();

		// Verification
		assertEquals(ResultEnum.SUCCESS, result);
		verify(deliverySystemSpy, times(2)).getDeliveryItems();
		verify(deliverySystemSpy, times(1)).getDeliveryPoints();
	}

	@Test
	public void testAssociateLocationToTruck() {

		DeliverySystem deliverySystemSpy = spy(deliverySystem);
		when(scan.nextLine()).thenReturn("Street tests").thenReturn("Truck Plate");
		Location location = new Location("Street tests");
		location.getLocationItems().add(new DeliveryItem("Item tests"));
		Truck truck = new Truck("Truck Plate");

		when(deliverySystemSpy.getDeliveryPoints()).thenReturn((new LinkedList<>(Collections.singletonList(location))));
		when(deliverySystemSpy.getTruckFleet()).thenReturn((new HashSet<>(Collections.singletonList(truck))));

		ResultEnum result = deliverySystemSpy.associateLocationToTruck();

		assertEquals(ResultEnum.SUCCESS, result);
		verify(deliverySystemSpy, times(2)).getDeliveryPoints();
		verify(deliverySystemSpy, times(1)).getTruckFleet();
	}

}
