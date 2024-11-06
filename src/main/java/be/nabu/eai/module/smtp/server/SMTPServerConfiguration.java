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

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import be.nabu.eai.api.EnvironmentSpecific;
import be.nabu.eai.api.InterfaceFilter;
import be.nabu.eai.module.keystore.KeyStoreArtifact;
import be.nabu.eai.repository.jaxb.ArtifactXMLAdapter;
import be.nabu.libs.services.api.DefinedService;
import be.nabu.utils.io.SSLServerMode;

@XmlRootElement(name = "smtpServer")
@XmlType(propOrder = { "forward", "passwordAuthenticationService", "port", "host", "aliases", "keystore", "keyAlias", "sslServerMode", "truststore", "poolSize", "ioPoolSize", "subscriptions" })
public class SMTPServerConfiguration {
	
	private Integer port;
	private KeyStoreArtifact keystore, truststore;
	private boolean forward;
	private DefinedService passwordAuthenticationService;
	private String keyAlias, host;
	private List<String> aliases;
	private Integer poolSize, ioPoolSize;
	private SSLServerMode sslServerMode;
	private List<MessageSubscription> subscriptions;
	
	@EnvironmentSpecific
	@XmlJavaTypeAdapter(value = ArtifactXMLAdapter.class)
	public KeyStoreArtifact getKeystore() {
		return keystore;
	}
	public void setKeystore(KeyStoreArtifact keystore) {
		this.keystore = keystore;
	}

	public boolean isForward() {
		return forward;
	}
	public void setForward(boolean forward) {
		this.forward = forward;
	}

	@XmlJavaTypeAdapter(value = ArtifactXMLAdapter.class)
	@InterfaceFilter(implement = "be.nabu.eai.authentication.api.PasswordAuthenticator.authenticate")
	public DefinedService getPasswordAuthenticationService() {
		return passwordAuthenticationService;
	}
	public void setPasswordAuthenticationService(DefinedService passwordAuthenticationService) {
		this.passwordAuthenticationService = passwordAuthenticationService;
	}
	
	@EnvironmentSpecific
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	
	@EnvironmentSpecific
	public String getKeyAlias() {
		return keyAlias;
	}
	public void setKeyAlias(String keyAlias) {
		this.keyAlias = keyAlias;
	}
	
	@NotNull
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	
	public List<String> getAliases() {
		return aliases;
	}
	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}
	
	@EnvironmentSpecific
	@XmlJavaTypeAdapter(value = ArtifactXMLAdapter.class)
	public KeyStoreArtifact getTruststore() {
		return truststore;
	}
	public void setTruststore(KeyStoreArtifact truststore) {
		this.truststore = truststore;
	}

	public Integer getPoolSize() {
		return poolSize;
	}
	public void setPoolSize(Integer poolSize) {
		this.poolSize = poolSize;
	}
	
	public Integer getIoPoolSize() {
		return ioPoolSize;
	}
	public void setIoPoolSize(Integer ioPoolSize) {
		this.ioPoolSize = ioPoolSize;
	}
	
	@EnvironmentSpecific
	public SSLServerMode getSslServerMode() {
		return sslServerMode;
	}
	public void setSslServerMode(SSLServerMode sslServerMode) {
		this.sslServerMode = sslServerMode;
	}
	
	public List<MessageSubscription> getSubscriptions() {
		return subscriptions;
	}
	public void setSubscriptions(List<MessageSubscription> subscriptions) {
		this.subscriptions = subscriptions;
	}
	
}
