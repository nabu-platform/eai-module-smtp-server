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
import java.util.List;

import be.nabu.eai.developer.MainController;
import be.nabu.eai.developer.managers.base.BaseJAXBComplexGUIManager;
import be.nabu.eai.repository.resources.RepositoryEntry;
import be.nabu.libs.property.api.Property;
import be.nabu.libs.property.api.Value;

public class SMTPServerGUIManager extends BaseJAXBComplexGUIManager<SMTPServerConfiguration, SMTPServerArtifact>{

	public SMTPServerGUIManager() {
		super("SMTP Server", SMTPServerArtifact.class, new SMTPServerManager(), SMTPServerConfiguration.class);
	}

	@Override
	protected List<Property<?>> getCreateProperties() {
		return null;
	}

	@Override
	protected SMTPServerArtifact newInstance(MainController controller, RepositoryEntry entry, Value<?>... values) throws IOException {
		return new SMTPServerArtifact(entry.getId(), entry.getContainer(), entry.getRepository());
	}

	@Override
	public String getCategory() {
		return "Protocols";
	}
}
