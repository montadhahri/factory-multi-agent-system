package usine;

import java.io.IOException;

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

public class AgentFourniseure extends Agent {
	private String TypeBois;
	@Override
	protected void setup() {
		TypeBois = (String) getArguments()[0];
		// recevoir demande de planche pour l'un des trois ateliers et la
		// reponse
		
		addBehaviour(new OneShotBehaviour() {
			@Override
			public void action() {
				DFAgentDescription dfd = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType("Fournisseur-" + TypeBois);
				sd.setName(getLocalName()+"-Fournisseur-"+TypeBois);
				dfd.addServices(sd);
				try {
				   DFService.register(this.getAgent(), dfd);
				}
				catch (FIPAException e) {
				   e.printStackTrace();
				}

				
			}
		});
		
		addBehaviour(new CyclicBehaviour(this) {
			@Override
			public void action() {
				ACLMessage msg = receive();
				if (msg != null) {
					if(msg.getPerformative() == ACLMessage.REQUEST){
						try {
							System.out.println(this.getAgent().getLocalName() + " REQUEST from " + msg.getSender().getLocalName() + (HashMap)msg.getContentObject());
						} catch (UnreadableException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						ACLMessage msg1 = new ACLMessage(ACLMessage.PROPOSE);
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
					}else if(msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL){
						try {
							System.out.println(this.getAgent().getLocalName() + "ACCEPT_PROPOSAL from " + msg.getSender().getLocalName() + (HashMap)msg.getContentObject());
						} catch (UnreadableException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						ACLMessage msg1 = new ACLMessage(ACLMessage.INFORM);
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
					}
				}
			}
		});
	}
}
