package be.nabu.eai.module.smtp.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import be.nabu.eai.developer.api.InterfaceLister;
import be.nabu.eai.developer.util.InterfaceDescriptionImpl;

public class SMTPServerInterfaceLister implements InterfaceLister {

	private static Collection<InterfaceDescription> descriptions = null;
	
	@Override
	public Collection<InterfaceDescription> getInterfaces() {
		if (descriptions == null) {
			synchronized(SMTPServerInterfaceLister.class) {
				if (descriptions == null) {
					List<InterfaceDescription> descriptions = new ArrayList<InterfaceDescription>();
					descriptions.add(new InterfaceDescriptionImpl("SMTP Server", "Message Subscriber", "be.nabu.eai.module.smtp.server.api.MessageSubscriber.handle"));
					SMTPServerInterfaceLister.descriptions = descriptions;
				}
			}
		}
		return descriptions;
	}

}
