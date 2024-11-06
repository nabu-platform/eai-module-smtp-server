/*
* Copyright (C) 2016 Alexander Verbruggen
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program. If not, see <https://www.gnu.org/licenses/>.
*/

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
