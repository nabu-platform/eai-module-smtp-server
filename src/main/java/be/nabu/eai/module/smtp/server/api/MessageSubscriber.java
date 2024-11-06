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

package be.nabu.eai.module.smtp.server.api;

import java.util.List;

import javax.jws.WebParam;
import javax.validation.constraints.NotNull;

import be.nabu.utils.mime.api.Part;

public interface MessageSubscriber {
	public void handle(@WebParam(name = "smtpServerId") @NotNull String smtpServerId, @WebParam(name = "messageId") @NotNull String messageId, @WebParam(name = "from") @NotNull String from, @WebParam(name = "to") @NotNull List<String> to, @NotNull @WebParam(name = "part") Part part);
}
