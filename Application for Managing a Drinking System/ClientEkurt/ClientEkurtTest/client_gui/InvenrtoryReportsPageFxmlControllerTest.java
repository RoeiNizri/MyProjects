package client_gui;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class InvenrtoryReportsPageFxmlControllerTest {
	private InvenrtoryReportsPageFxmlController controller = new InvenrtoryReportsPageFxmlController();

	/**
	 * Test description:Checking if when we insert a machine name the matching report on product table.
	 * input: Action 
	 * expected result(output): 28 products matching to product table size, and the first product is coca cola.
	 */
	@Test
	public void testShowReport() {
		// arrange
		controller.getMachineComboBox().setValue("Braude");
		// act
		controller.getShowReportBtn().fire(); // fire is running onAction of button
		// assert
		assertEquals(28, controller.getProductsTable().getItems().size());
		assertEquals("Coca Cola", controller.getProductsTable().getItems().get(0).getProductName());
	}

	/**
	 * Test description:Checking if when we insert a machine name the matching report on product table.
	 * input: Action 
	 * expected result(output): The Xsl file in the matching path.
	 */
	@Test
	public void testExportReport() {
		// arrange
		controller.getMachineComboBox().setValue("Braude");
		// act
		controller.getExportReportBtn().fire();
		// assert
		// assert that the export was successful
	}
}