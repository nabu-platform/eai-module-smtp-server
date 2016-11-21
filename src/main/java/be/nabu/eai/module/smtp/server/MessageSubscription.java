package be.nabu.eai.module.smtp.server;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import be.nabu.eai.api.InterfaceFilter;
import be.nabu.eai.repository.jaxb.ArtifactXMLAdapter;
import be.nabu.libs.services.api.DefinedService;

public class MessageSubscription {
	private String fromMatch;
	private DefinedService messageSubscriber;
	private boolean isContinue;
	
	public String getFromMatch() {
		return fromMatch;
	}
	public void setFromMatch(String fromMatch) {
		this.fromMatch = fromMatch;
	}
	
	@XmlJavaTypeAdapter(value = ArtifactXMLAdapter.class)
	@InterfaceFilter(implement = "be.nabu.eai.module.smtp.server.api.MessageSubscriber.handle")
	public DefinedService getMessageSubscriber() {
		return messageSubscriber;
	}
	public void setMessageSubscriber(DefinedService messageSubscriber) {
		this.messageSubscriber = messageSubscriber;
	}
	public boolean isContinue() {
		return isContinue;
	}
	public void setContinue(boolean isContinue) {
		this.isContinue = isContinue;
	}
	
}
