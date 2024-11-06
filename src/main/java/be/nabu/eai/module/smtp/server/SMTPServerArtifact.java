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

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509KeyManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.nabu.eai.authentication.api.PasswordAuthenticator;
import be.nabu.eai.module.smtp.server.api.MessageSubscriber;
import be.nabu.eai.repository.RepositoryThreadFactory;
import be.nabu.eai.repository.api.Repository;
import be.nabu.eai.repository.artifacts.jaxb.JAXBArtifact;
import be.nabu.eai.repository.util.CombinedAuthenticator;
import be.nabu.eai.repository.util.SystemPrincipal;
import be.nabu.libs.artifacts.api.StartableArtifact;
import be.nabu.libs.artifacts.api.StoppableArtifact;
import be.nabu.libs.authentication.api.Authenticator;
import be.nabu.libs.events.api.EventHandler;
import be.nabu.libs.events.impl.EventDispatcherImpl;
import be.nabu.libs.nio.api.NIOServer;
import be.nabu.libs.nio.impl.NIOServerImpl;
import be.nabu.libs.resources.api.ResourceContainer;
import be.nabu.libs.services.pojo.POJOUtils;
import be.nabu.libs.smtp.server.SMTPPipelineFactory;
import be.nabu.libs.smtp.server.forwarder.MailForwarder;
import be.nabu.utils.io.SSLServerMode;
import be.nabu.utils.mime.api.Header;
import be.nabu.utils.mime.api.Part;
import be.nabu.utils.mime.impl.MimeUtils;
import be.nabu.utils.security.AliasKeyManager;
import be.nabu.utils.security.KeyStoreHandler;
import be.nabu.utils.security.SSLContextType;

public class SMTPServerArtifact extends JAXBArtifact<SMTPServerConfiguration> implements StartableArtifact, StoppableArtifact {

	private NIOServer server;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final String SMTP_IO_POOL_SIZE = "be.nabu.eai.smtp.ioPoolSize";
	private static final String SMTP_PROCESS_POOL_SIZE = "be.nabu.eai.smtp.processPoolSize";
	
	public SMTPServerArtifact(String id, ResourceContainer<?> directory, Repository repository) {
		super(id, directory, repository, "smtp-server.xml", SMTPServerConfiguration.class);
	}

	@Override
	public void stop() throws IOException {
		if (server != null) {
			server.stop();
			server = null;
		}
	}

	@Override
	public void start() throws IOException {
		try {
			Integer port = getConfig().getPort();
			SSLContext context = null;
			if (getConfig().getKeystore() != null && getConfig().getKeyAlias() != null) {
				KeyStoreHandler keyStoreHandler = new KeyStoreHandler(getConfig().getKeystore().getKeyStore().getKeyStore());
				KeyManager[] keyManagers = keyStoreHandler.getKeyManagers();
				for (int i = 0; i < keyManagers.length; i++) {
					if (keyManagers[i] instanceof X509KeyManager) {
						keyManagers[i] = new AliasKeyManager((X509KeyManager) keyManagers[i], getConfig().getKeyAlias());
					}
				}
				context = SSLContext.getInstance(SSLContextType.TLS.toString());
				context.init(keyManagers, keyStoreHandler.getTrustManagers(), new SecureRandom());
				if (port == null) {
					port = 587;
				}
			}
			else if (port == null) {
				port = 25;
			}
			Integer ioPoolSize = getConfig().getIoPoolSize() == null ? new Integer(System.getProperty(SMTP_IO_POOL_SIZE, "2")) : getConfig().getIoPoolSize();
			Integer processPoolSize = getConfig().getPoolSize() == null ? new Integer(System.getProperty(SMTP_PROCESS_POOL_SIZE, "5")) : getConfig().getPoolSize();
			
			SSLServerMode sslServerMode = getConfig().getSslServerMode() == null ? SSLServerMode.NO_CLIENT_CERTIFICATES : getConfig().getSslServerMode();
			NIOServerImpl server = new NIOServerImpl(
				context, 
				sslServerMode,
				port, 
				ioPoolSize, 
				processPoolSize, 
				new SMTPPipelineFactory(
					getConfig().getHost(),
					// TODO: proper mail validator
					null, 
					getAuthenticator(), 
					context, 
					sslServerMode
				), 
				new EventDispatcherImpl(), 
				new RepositoryThreadFactory(getRepository())
			);
			
			if (getConfig().isForward()) {
				List<String> names = new ArrayList<String>();
				names.add(getConfig().getHost());
				if (getConfig().getAliases() != null) {
					names.addAll(getConfig().getAliases());
				}
				server.getDispatcher().subscribe(Part.class, new MailForwarder(null, null, names.toArray(new String[names.size()])));
			}
			
			server.getDispatcher().subscribe(Part.class, new EventHandler<Part, String>() {
				@Override
				public String handle(Part event) {
					if (getConfig().getSubscriptions() != null) {
						Header originalFrom = MimeUtils.getHeader("X-Original-From", event.getHeaders());
						Header [] originalTo = MimeUtils.getHeaders("X-Original-From", event.getHeaders());
						List<String> to = new ArrayList<String>();
						if (originalTo != null) {
							for (Header header : originalTo) {
								to.add(header.getValue());
							}
						}
						Header messageId = MimeUtils.getHeader("X-Generated-Id", event.getHeaders());
						for (MessageSubscription subscription : getConfig().getSubscriptions()) {
							if (subscription.getFromMatch() == null || originalFrom == null || originalFrom.getValue().matches(subscription.getFromMatch())) {
								if (subscription.getMessageSubscriber() != null) {
									MessageSubscriber subscriber = POJOUtils.newProxy(MessageSubscriber.class, subscription.getMessageSubscriber(), getRepository(), SystemPrincipal.ROOT);
									subscriber.handle(getId(), messageId == null ? null : messageId.getValue(), originalFrom == null ? null : originalFrom.getValue(), to, event);
								}
								if (!subscription.isContinue()) {
									break;
								}
							}
						}
					}
					return null;
				}
			});
			
			this.server = server;
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						server.start();
					}
					catch (Exception e) {
						logger.error("Could not start server", e);
					}
				}
			});
			thread.start();
		}
		catch (Exception e) {
			logger.error("Could not start smtp server: " + getId(), e);
			throw new RuntimeException(e);
		}
	}
	
	public Authenticator getAuthenticator() {
		if (getConfig().getPasswordAuthenticationService() != null) {
			PasswordAuthenticator passwordAuthenticator = POJOUtils.newProxy(PasswordAuthenticator.class, getConfig().getPasswordAuthenticationService(), getRepository(), SystemPrincipal.ROOT);
			return new CombinedAuthenticator(passwordAuthenticator, null);
		}
		return null;
	}

	@Override
	public boolean isStarted() {
		return server != null;
	}

}
