package usine;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class AgentCommercial extends Agent {
	private static final long serialVersionUID = 1L;
	List<Commande> commandes;
	int id;

	@Override
	protected void setup() {
		commandes = new ArrayList<>();
		id = 1;
		// enregistrer dans la Pages Jaunes
		addBehaviour(new OneShotBehaviour() {
			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				DFAgentDescription dfd = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType("Service-Commercial");
				sd.setName(getLocalName() + "Service-Commercial");
				dfd.addServices(sd);
				try {
					DFService.register(this.getAgent(), dfd);
				} catch (FIPAException e) {
					e.printStackTrace();
				}
			}
		});

		// enregistrer les demande de produit
		addBehaviour(new CyclicBehaviour(this) {
			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				ACLMessage msg = receive();
				if (msg != null) {
					// enregistrer les demandes des differents clients
					if (msg.getSender().getLocalName().equals("client")) {
						JSONParser parser = new JSONParser();
						Commande commande = new Commande(id, msg.getSender().getLocalName());
						try {
							JSONObject obj = (JSONObject) parser.parse(msg.getContent());
							// ranger la demande dans la Queue
							Set<String> key = obj.keySet();
							for (String object : key) {
								Long a = (Long) obj.get(object);
								commande.setCommandes(new ProduitCommande(object, a.intValue()));
							}
							commandes.add(commande);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						id++;
						System.out.println(this.getAgent().getLocalName() + 
								" : recoit commande du :" + msg.getSender().getLocalName() + " pour " + commande.a());
						// recoit les produit des differents demandes
					} else {
						//les commande du differents atelier
						String[] cmd = msg.getContent().split(":");
						for (ProduitCommande produitcommande : commandes.get(Integer.parseInt(cmd[0])-1).getCommandes()) {
							if(produitcommande.getNameProduit().equals(cmd[1])){
								produitcommande.setIsPreparer(true);
							}
						}
						System.out.println(this.getAgent().getLocalName() + " : commande N" + cmd[0] + " " + cmd[1] + " preparer");
					}
				}
			}
		});
		// traiter les demandes de produit
		addBehaviour(new CyclicBehaviour(this) {
			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				for (Commande commande : commandes) {
					if (!commande.getIsLivrer()) {
						if (commande.isPreparer()) {//la livraison de la commande : commande preparer par les atelier
							System.out.println(commande.getAdressClient() + ": Commande N" + commande.getIdCommande()
							+ " Livrer !!");
							ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
							msg.addReceiver(new AID(commande.getAdressClient(), AID.ISLOCALNAME));
							msg.setLanguage("Prolog");
							msg.setContent("Commande Livrer !!");
							send(msg);
							commande.setIsLivrer(true);
						} else if (!commande.isTraiter()) {//traiter les commande en envoi des commande au differents ateliers
							for (ProduitCommande produitCommande : commande.getCommandes()) {
								String msg = commande.getIdCommande() + ":" + produitCommande.getNameProduit() + ":" + produitCommande.getQuantite();
								DFAgentDescription dfd = new DFAgentDescription();
								ServiceDescription sd = new ServiceDescription();
								sd.setType("Fabriquer-"+produitCommande.getNameProduit());
								dfd.addServices(sd);
								try {
								   DFAgentDescription[] result = DFService.search(this.getAgent(), dfd);
								   System.out.println(result[0].getName().getLocalName() + " : Commande de prepare " + msg);
								   ACLMessage msg1 = new ACLMessage(ACLMessage.INFORM);
								   msg1.addReceiver(new AID(result[0].getName().getLocalName(), AID.ISLOCALNAME));
								   msg1.setLanguage("Prolog");
								   msg1.setContent(msg);
								   send(msg1);
								}
								catch (FIPAException e) {
								   e.printStackTrace();
								}
								produitCommande.setIsTraiter(true);
							}
						}
					}
				}
			}
		});

	}

}
