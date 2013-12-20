/*
 * Syncany, www.syncany.org
 * Copyright (C) 2011-2013 Philipp C. Heckel <philipp.heckel@gmail.com> 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.syncany.cli;

import static java.util.Arrays.asList;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import org.syncany.operations.GenlinkOperation.GenlinkOperationResult;

public class GenlinkCommand extends AbstractInitCommand {
	@Override
	public CommandScope getRequiredCommandScope() {	
		return CommandScope.INITIALIZED_LOCALDIR;
	}
	
	@Override
	public int execute(String[] operationArgs) throws Exception {
		GenlinkCommandOptions commandOptions = parseGenlinkOptions(operationArgs);
		GenlinkOperationResult operationResult = client.genLink();		
		printResults(operationResult, commandOptions);
		
		return 0;		
	}
	
	private GenlinkCommandOptions parseGenlinkOptions(String[] operationArgs) {
		GenlinkCommandOptions commandOptions = new GenlinkCommandOptions();

		OptionParser parser = new OptionParser();			
		OptionSpec<Void> optionShort = parser.acceptsAll(asList("s", "short"));
		
		OptionSet options = parser.parse(operationArgs);

		// --short
		commandOptions.setShortOutput(options.has(optionShort));
		
		return commandOptions;
	}

	private void printResults(GenlinkOperationResult operationResult, GenlinkCommandOptions commandOptions) {
		if (commandOptions.isShortOutput()) {
			out.println(operationResult.getShareLink());
		}
		else {
			out.println();
			out.println("Repository created, and local folder initialized. To share the same repository");
			out.println("with others, you can share this link:");
			out.println();		
			out.println("   "+operationResult.getShareLink());
			out.println();
			
			if (operationResult.isShareLinkEncrypted()) {
				out.println("This link is encrypted with the given password, so you can safely share it.");
				out.println("using unsecure communication (chat, e-mail, etc.)");
				out.println();
				out.println("WARNING: The link contains the details of your repo connection which typically");
				out.println("         consist of usernames/password of the connection (e.g. FTP user/pass).");
			}
			else {
				out.println("WARNING: This link is NOT ENCRYPTED and might contain connection credentials");
				out.println("         Do NOT share this link unless you know what you are doing!");
				out.println();
				out.println("         The link contains the details of your repo connection which typically");
				out.println("         consist of usernames/password of the connection (e.g. FTP user/pass).");
			}

			out.println();
		}			
	}
	
	private class GenlinkCommandOptions {
		private boolean shortOutput = false;

		public boolean isShortOutput() {
			return shortOutput;
		}

		public void setShortOutput(boolean shortOutput) {
			this.shortOutput = shortOutput;
		}				
	}
}