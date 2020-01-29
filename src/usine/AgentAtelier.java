package usine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.util.leap.HashMap;
import jade.util.leap.Serializable;

public class AgentAtelier extends Agent {
	private Stock stock;
	private List<ProduitCommande> Commandes;

	@Override
	protected void setup() {
		// initial demandes
		Commandes = new ArrayList<>();
		// initialisation stock initial
		List<ProduitStock> st = new ArrayList<>();
		st.add((ProduitStock) getArguments()[0]);
		st.add((ProduitStock) getArguments()[1]);
		st.add((ProduitStock) getArguments()[2]);
		stock = new Stock(st);
		// enregistrer dans la carte jaune
		addBehaviour(new OneShotBehaviour() {
			@Override
			public void action() {
				DFAgentDescription dfd = new DFAgentDescription();
				for (ProduitStock produitStock : stock.getStock()) {
					ServiceDescription sd = new ServiceDescription();
					sd.setType("Fabriquer-" + produitStock.getNameProduit());
					sd.setName(getLocalName() + "-Fabriquer-" + produitStock.getNameProduit());
					dfd.addServices(sd);
				}
				try {
					DFService.register(this.getAgent(), dfd);
				} catch (FIPAException e) {
					e.printStackTrace();
				}
			}
		});

		addBehaviour(new CyclicBehaviour() {

			@Override
			public void action() {
				ACLMessage msg = receive();
				if (msg != null) {
					if (msg.getSender().getLocalName().equals("Commercial")) {//recoit commande du matiere premiere
						String[] cmd = msg.getContent().split(":");
						Commandes.add(new ProduitCommande(Integer.parseInt(cmd[0]), cmd[1], Integer.parseInt(cmd[2])));
					} else {
						HashMap hash = null;
						try {
							hash = (HashMap) msg.getContentObject();
						} catch (UnreadableException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						for (ProduitStock produitStock : stock.getStock()) {
							if(produitStock.getNameProduit().equals(hash.get("nameP"))){
								produitStock.setQuantite(produitStock.getQteInitial());
							}
						}
					}
				}

			}
		});
		// traiter Commandes
		addBehaviour(new CyclicBehaviour() {

			@Override
			public void action() {
				for (ProduitCommande produitCommande : Commandes) {
					if (!produitCommande.getIsPreparer()) {
						if (!stock.isRepture(produitCommande)) {//commande preparer envoi la commande au service commercial
							System.out.println(this.getAgent().getLocalName() + "  " + stock.toString());
							stock.livrerCommande(produitCommande);
							DFAgentDescription dfd = new DFAgentDescription();
							ServiceDescription sd = new ServiceDescription();
							sd.setType("Service-Commercial");
							dfd.addServices(sd);
							try {
								DFAgentDescription[] result = DFService.search(this.getAgent(), dfd);
								ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
								msg.addReceiver(new AID(result[0].getName().getLocalName(), AID.ISLOCALNAME));
								msg.setLanguage("Prolog");
								msg.setContent(produitCommande.getIdCommande() + ":" + produitCommande.getNameProduit()
										+ ":" + produitCommande.getQuantite());
								send(msg);
							} catch (FIPAException e) {
								e.printStackTrace();
							}
							produitCommande.setIsPreparer(true);
						} else {
							if (!produitCommande.getIsTraiter()) {//envoi commande du matiere premiere manque du Produit
								HashMap msgH = new HashMap();
								msgH.put("TypeBois", stock.getStock().get(0).getTypeBois());
								msgH.put("Qte", stock.qteBois(produitCommande));
								msgH.put("nameP", produitCommande.getNameProduit());
								msgH.put("nameAtelier", this.getAgent().getLocalName());
								System.out.println(
										stock.getStock().get(0).getTypeBois() + " " + stock.qteBois(produitCommande));
								DFAgentDescription dfd = new DFAgentDescription();
								ServiceDescription sd = new ServiceDescription();
								sd.setType("Service-Approvisionnement");
								dfd.addServices(sd);
								try {
									DFAgentDescription[] result = DFService.search(this.getAgent(), dfd);
									ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
									msg.addReceiver(new AID(result[0].getName().getLocalName(), AID.ISLOCALNAME));
									msg.setLanguage("Prolog");
									try {
										msg.setContentObject((Serializable) msgH);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									send(msg);
								} catch (FIPAException e) {
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
