package be.nabu.eai.module.smtp.server.api;

import java.util.List;

import javax.jws.WebParam;
import javax.validation.constraints.NotNull;

import be.nabu.utils.mime.api.Part;

public interface MessageSubscriber {
	public void handle(@WebParam(name = "smtpServerId") @NotNull String smtpServerId, @WebParam(name = "messageId") @NotNull String messageId, @WebParam(name = "from") @NotNull String from, @WebParam(name = "to") @NotNull List<String> to, @NotNull @WebParam(name = "part") Part part);
}
