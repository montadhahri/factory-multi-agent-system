package usine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.util.leap.HashMap;

public class AgentApprovisionnement extends Agent {
	private HashMap commandeBois;
	int i;
	private static final long serialVersionUID = 1L;
	@Override
	protected void setup() {
		commandeBois = new HashMap();
		i = 1;
		addBehaviour(new OneShotBehaviour() {
			private static final long serialVersionUID = 1L;
			@Override
			public void action() {
				DFAgentDescription dfd = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType("Service-Approvisionnement");
				sd.setName(getLocalName()+"-Service-Approvisionnement");
				dfd.addServices(sd);
				try {
				   DFService.register(this.getAgent(), dfd);
				}
				catch (FIPAException e) {
				   e.printStackTrace();
				}
			}
		});
		
		addBehaviour(new CyclicBehaviour() {
			@Override
			public void action() {
				ACLMessage msg = receive();
				if (msg != null) {
					if(msg.getPerformative() == ACLMessage.REQUEST){//recoit commande du matiere premier de la part des ateliers
						HashMap hash = null;
						try {
							hash = (HashMap) msg.getContentObject();
							hash.put("id", i);
							commandeBois.put(i, false);
							i++;
						} catch (UnreadableException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						DFAgentDescription dfd = new DFAgentDescription();
						ServiceDescription sd = new ServiceDescription();
						sd.setType("Fournisseur-" + hash.get("TypeBois"));
						dfd.addServices(sd);
						try {
						   DFAgentDescription[] result = DFService.search(this.getAgent(), dfd);
						   for (DFAgentDescription dfAgentDescription : result) {
						   ACLMessage msg1 = new ACLMessage(ACLMessage.REQUEST);
							msg1.addReceiver(new AID(dfAgentDescription.getName().getLocalName(), AID.ISLOCALNAME));
							msg1.setLanguage("Prolog");
							try {
								msg1.setContentObject(hash);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							send(msg1);
						   }
						}
						catch (FIPAException e) {
						   e.printStackTrace();
						}
					} else if(msg.getPerformative() == ACLMessage.PROPOSE){//recoit les differents proposition des fournisseurs et choisir la premier proposition
						HashMap hash = null;
						try {
							hash = (HashMap) msg.getContentObject();
						} catch (UnreadableException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						System.out.println(this.getAgent().getLocalName() + " : PROPOSE from " + msg.getSender().getLocalName());
						if(!(Boolean) commandeBois.get(hash.get("id"))){
							ACLMessage msg1 = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
							msg1.addReceiver(new AID(msg.getSender().getLocalName(), AID.ISLOCALNAME));
							msg1.setLanguage("Prolog");
							try {
								msg1.setContentObject(msg.getContentObject());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (UnreadableException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							send(msg1);
							commandeBois.put(hash.get("id"), true);
						}
					} else if(msg.getPerformative() == ACLMessage.INFORM){//recoit la matiere premier et l'envoi au atelier concerne
						System.out.println("recoit du matiere premiere" + msg.getSender().getLocalName());
						HashMap hash = null;
						try {
							hash = (HashMap) msg.getContentObject();
						} catch (UnreadableException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						System.out.println(hash);
						ACLMessage msg1 = new ACLMessage(ACLMessage.INFORM);
						msg1.addReceiver(new AID((String)hash.get("nameAtelier"), AID.ISLOCALNAME));
						msg1.setLanguage("Prolog");
						try {
							msg1.setContentObject(msg.getContentObject());
						} catch (IOException | UnreadableException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						send(msg1);
					}
				}
			}
		});

	}
	
		
}
