package usine;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class AgentClient extends Agent {
	private List<String> prduit;
	private boolean PurchaseSend;

	@Override
	protected void setup() {
		PurchaseSend = false;
		prduit = new ArrayList<>();
		prduit.add("Table");
		prduit.add("Chaise");
		prduit.add("Buffet");
		prduit.add("Lit");
		prduit.add("Chevet");
		prduit.add("Armoire");
		prduit.add("Banquette");
		prduit.add("Fauteuil");
		prduit.add("Etagère");
		// Envoi du demande
		addBehaviour(new TickerBehaviour(this, 10000) {
			@Override
			protected void onTick() {
				if (!PurchaseSend) {
					// recherche a service commercial
					DFAgentDescription dfd = new DFAgentDescription();
					ServiceDescription sd = new ServiceDescription();
					sd.setType("Service-Commercial");
					dfd.addServices(sd);
					try {
						DFAgentDescription[] result = DFService.search(this.getAgent(), dfd);
						//envoi la commande
						String demande = getRandomDemande();
						ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
						msg.addReceiver(new AID(result[0].getName().getLocalName(), AID.ISLOCALNAME));
						msg.setLanguage("Prolog");
						msg.setContent(demande);
						send(msg);
						System.out.println(this.getAgent().getLocalName() + ": envoi demande d'achat :" + demande);
						PurchaseSend = true;
					} catch (FIPAException e) {
						e.printStackTrace();
					}

				}
			}
		});
		// Reçoit la demande
		addBehaviour(new CyclicBehaviour(this) {

			@Override
			public void action() {
				//recoit la commande
				if (PurchaseSend) {
					ACLMessage msg = receive();
					if (msg != null) {
						System.out.println(this.getAgent().getLocalName() + " : demande reçu !! ");
						System.out.println("******************************************************");
						PurchaseSend = false;
					}
				}
			}
		});
	}

	public String getRandomDemande() {
		int nbprd = (int) (Math.random() * 4) + 1;
		JSONObject json = new JSONObject();
		for (int i = 0; i < nbprd; i++) {
			json.put(prduit.get((int) (Math.random() * 9)), new Integer(((int) (Math.random() * 9) + 1)));
		}
		return json.toString();
	}

}
