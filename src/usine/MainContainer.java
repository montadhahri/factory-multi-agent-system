package usine;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainContainer {
	Stage window;

	public static void main(String[] args) {
		try {

			Runtime runtime = Runtime.instance();
			Properties proprieties = new ExtendedProperties();
			proprieties.setProperty(Profile.GUI, "true");
			ProfileImpl profileImpl = new ProfileImpl(proprieties);
			AgentContainer mainContainer = runtime.createMainContainer(profileImpl);
			mainContainer.start();
			// commercial agent
			AgentController AgentCommercial = mainContainer.createNewAgent("Commercial", "usine.AgentCommercial", null);
			AgentCommercial.start();
			// atelier 1 agent
			AgentController AgentAtelier1 = mainContainer
					.createNewAgent("Atelier1", "usine.AgentAtelier",
							new Object[] { new ProduitStock("Table", 20, "Chêne", 5),
									new ProduitStock("Chaise", 120, "Chêne", 2),
									new ProduitStock("Buffet", 20, "Chêne", 6) });
			AgentAtelier1.start();
			// atelier 2 agent
			AgentController AgentAtelier2 = mainContainer.createNewAgent("Atelier2", "usine.AgentAtelier",
					new Object[] { new ProduitStock("Lit", 10, "Merisier", 7),
							new ProduitStock("Chevet", 20, "Merisier", 1),
							new ProduitStock("Armoire", 10, "Merisier", 8) });
			AgentAtelier2.start();
			// atelier 3 agent
			AgentController AgentAtelier3 = mainContainer.createNewAgent("Atelier3", "usine.AgentAtelier",
					new Object[] { new ProduitStock("Banquette", 30, "Noyer", 6),
							new ProduitStock("Fauteuil", 60, "Noyer", 3),
							new ProduitStock("Etagère", 30, "Noyer", 8) });
			AgentAtelier3.start();
			// approvisionnement agent
			AgentController AgentApprovisionnement = mainContainer.createNewAgent("Approvisionnement",
					"usine.AgentApprovisionnement", null);
			AgentApprovisionnement.start();
			// Client agent
			AgentController AgentClient = mainContainer.createNewAgent("client", "usine.AgentClient", null);
			AgentClient.start();
			// Fourniseure agent
			AgentController AgentFourniseure = mainContainer.createNewAgent("Fourniseure", "usine.AgentFourniseure",
					new Object[] { "Chêne" });
			AgentFourniseure.start();
			AgentController AgentFourniseure1 = mainContainer.createNewAgent("Fourniseure1", "usine.AgentFourniseure",
					new Object[] { "Chêne" });
			AgentFourniseure1.start();
			AgentController AgentFourniseure2 = mainContainer.createNewAgent("Fourniseure2", "usine.AgentFourniseure",
					new Object[] { "Chêne" });
			AgentFourniseure2.start();
			AgentController AgentFourniseure3 = mainContainer.createNewAgent("Fourniseure3", "usine.AgentFourniseure",
					new Object[] { "Chêne" });
			AgentFourniseure3.start();
			AgentController AgentFourniseure4 = mainContainer.createNewAgent("Fourniseure4", "usine.AgentFourniseure",
					new Object[] { "Merisier" });
			AgentFourniseure4.start();
			AgentController AgentFourniseure5 = mainContainer.createNewAgent("Fourniseure5", "usine.AgentFourniseure",
					new Object[] { "Merisier" });
			AgentFourniseure5.start();
			AgentController AgentFourniseure6 = mainContainer.createNewAgent("Fourniseure6", "usine.AgentFourniseure",
					new Object[] { "Noyer" });
			AgentFourniseure6.start();
			AgentController AgentFourniseure7 = mainContainer.createNewAgent("Fourniseure7", "usine.AgentFourniseure",
					new Object[] { "Noyer" });
			AgentFourniseure7.start();
			AgentController AgentFourniseure8 = mainContainer.createNewAgent("Fourniseure8", "usine.AgentFourniseure",
					new Object[] { "Noyer" });
			AgentFourniseure8.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
