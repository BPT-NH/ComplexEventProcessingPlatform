package sushi.esper.examples;

import java.util.Arrays;
import java.util.List;

import sushi.esper.SushiStreamProcessingAdapter;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.eventhandling.Broker;
import sushi.excel.importer.ExcelImporter;
import sushi.notification.SushiNotificationPriorityEnum;
import sushi.notification.SushiNotificationRuleForEvent;
import sushi.notification.SushiNotificationRuleForQuery;
import sushi.query.SushiQuery;
import sushi.query.SushiQueryTypeEnum;
import sushi.transformation.TransformationManager;
import sushi.transformation.TransformationRule;
import sushi.user.SushiUser;

/**
 * This will create event types and notifications from the demo video (from final presentation 02.07.2013)
 * It will not load events into the platform.
 * Take care: It will result in an error if a query with the same name is already stored, e.g. because this class was run twice.
 */
public class ContainerTransportShowcase {
	
	public static void main(String[] args) {
		ContainerTransportShowcase showCase = new ContainerTransportShowcase();
		showCase.prepareEventtypes();
		showCase.prepareNotifications();
	}
	
	public void prepareNotifications() {
		SushiUser user = new SushiUser("Planner", "1234", "bp2013w1@gmail.com");
		user.save();
		SushiNotificationRuleForEvent not1 = new SushiNotificationRuleForEvent(SushiEventType.findByTypeName("TruckReady"), user, SushiNotificationPriorityEnum.LOW);
		not1.save();
		
		SushiQuery query = new SushiQuery("OrdersWithRunLongerThan2Days", "SELECT A.ContainerID, ((B.Timestamp.getTime() - A.Timestamp.getTime()) / (1000 * 60 * 60 *24) ) AS run FROM PATTERN [every A=ReadyForDischarge -> every B=ContainerDelivered(B.ContainerID = A.ContainerID AND ((B.Timestamp.getTime() - A.Timestamp.getTime()) > (1000*60*60*24*2)))]", SushiQueryTypeEnum.LIVE);
		query.save();
		query.addToEsper();
		
		SushiNotificationRuleForQuery not2 = new SushiNotificationRuleForQuery(query, user, SushiNotificationPriorityEnum.HIGH);
		not2.save();
		
	}

	public void prepareEventtypes() {
		
		System.out.println();
		System.out.println("=============================");
		System.out.println("=============================");
		System.out.println();
		
		SushiAttribute containerArrivalAttributes1 = new SushiAttribute("ContainerID", SushiAttributeTypeEnum.INTEGER);
		SushiAttribute containerArrivalAttributes2 = new SushiAttribute("Pier", SushiAttributeTypeEnum.STRING);
		SushiAttribute containerArrivalAttributes3 = new SushiAttribute("Refrigeration_Status", SushiAttributeTypeEnum.STRING);
		List<SushiAttribute> attributes1 = Arrays.asList(containerArrivalAttributes1, containerArrivalAttributes2, containerArrivalAttributes3);
		SushiEventType containerArrival = new SushiEventType("ContainerArrival", attributes1, "Timestamp");
		Broker.send(containerArrival);

		SushiAttribute readyForDischargeAttributes1 = new SushiAttribute("ContainerID", SushiAttributeTypeEnum.INTEGER);
		SushiAttribute readyForDischargeAttributes2 = new SushiAttribute("Pier", SushiAttributeTypeEnum.STRING);
		SushiAttribute readyForDischargeAttributes3 = new SushiAttribute("Refrigeration_Status", SushiAttributeTypeEnum.STRING);
		SushiAttribute readyForDischargeAttributes4 = new SushiAttribute("ShipID", SushiAttributeTypeEnum.STRING);
		List<SushiAttribute> attributes2 = Arrays.asList(readyForDischargeAttributes1, readyForDischargeAttributes2, readyForDischargeAttributes3, readyForDischargeAttributes4);
		SushiEventType readyForDischarge = new SushiEventType("ReadyForDischarge", attributes2, "Timestamp");
		Broker.send(readyForDischarge);
		
		SushiAttribute containerDischargedAttributes1 = new SushiAttribute("ContainerID", SushiAttributeTypeEnum.INTEGER);
		List<SushiAttribute> attributes3 = Arrays.asList(containerDischargedAttributes1);
		SushiEventType containerDischarged = new SushiEventType("ContainerDischarged", attributes3, "Timestamp");
		Broker.send(containerDischarged);
		
		SushiAttribute storeChilledAttributes1 = new SushiAttribute("ContainerID", SushiAttributeTypeEnum.INTEGER);
		SushiAttribute storeChilledAttributes2 = new SushiAttribute("Warehouse", SushiAttributeTypeEnum.STRING);
		List<SushiAttribute> attributes4 = Arrays.asList(storeChilledAttributes1, storeChilledAttributes2);
		SushiEventType storeChilled = new SushiEventType("StoreChilled", attributes4, "Timestamp");
		Broker.send(storeChilled);
		
		SushiAttribute storeUnChilledAttributes1 = new SushiAttribute("ContainerID", SushiAttributeTypeEnum.INTEGER);
		SushiAttribute storeUnChilledAttributes2 = new SushiAttribute("Warehouse", SushiAttributeTypeEnum.STRING);
		List<SushiAttribute> attributes5 = Arrays.asList(storeUnChilledAttributes1, storeUnChilledAttributes2);
		SushiEventType storeUnChilled = new SushiEventType("StoreUnchilled", attributes5, "Timestamp");
		Broker.send(storeUnChilled);
		
		SushiAttribute truckReadyAttributes1 = new SushiAttribute("ContainerID", SushiAttributeTypeEnum.INTEGER);
		SushiAttribute truckReadyAttributes2 = new SushiAttribute("TruckID", SushiAttributeTypeEnum.STRING);
		List<SushiAttribute> attributes6 = Arrays.asList(truckReadyAttributes1, truckReadyAttributes2);
		SushiEventType truckReady = new SushiEventType("TruckReady", attributes6, "Timestamp");
		Broker.send(truckReady);
		
		SushiAttribute containerDeliveryAttributes1 = new SushiAttribute("ContainerID", SushiAttributeTypeEnum.INTEGER);
		SushiAttribute containerDeliveryAttributes2 = new SushiAttribute("TruckID", SushiAttributeTypeEnum.STRING);
		SushiAttribute containerDeliveryAttributes3 = new SushiAttribute("Price", SushiAttributeTypeEnum.INTEGER);
		List<SushiAttribute> attributes7 = Arrays.asList(containerDeliveryAttributes1, containerDeliveryAttributes2, containerDeliveryAttributes3);
		SushiEventType containerDelivery = new SushiEventType("ContainerDelivered", attributes7, "Timestamp");
		Broker.send(containerDelivery);
	}
		
}
