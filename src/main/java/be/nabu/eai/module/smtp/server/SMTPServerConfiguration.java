package be.nabu.eai.module.smtp.server;

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
@XmlType(propOrder = { "forward", "passwordAuthenticationService", "port", "serverName", "keystore", "keyAlias", "sslServerMode", "truststore", "poolSize", "ioPoolSize" })
public class SMTPServerConfiguration {
	
	private Integer port;
	private KeyStoreArtifact keystore, truststore;
	private boolean forward;
	private DefinedService passwordAuthenticationService;
	private String keyAlias, serverName;
	private Integer poolSize, ioPoolSize;
	private SSLServerMode sslServerMode;
	
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
	
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
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
	
}
