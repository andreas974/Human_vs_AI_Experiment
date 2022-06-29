package edu.kit.exp.impl;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Arrays;

import edu.kit.exp.client.ClientMain;
import edu.kit.exp.server.ServerMain;

public class run {
	/**
	 * This method initiates a command line dialog which provides options for
	 * starting a server or clients.
	 * 
	 * @param args
	 *            A String field which contains a parameter that can either
	 *            start a server("s") or a client ("c").
	 * @throws IOException
	 */
	public static void main(String[] args) throws Exception {

		char serverClientChoice = 0;
		int instanceAmount = 1;

		if (args.length == 0) {
			System.out.println(
					"Use command line parameters to skip this window. Use \"-s\" to start a server or \"-c\" to start a client.");

			RunServerClientDialog startDialog = new RunServerClientDialog();
			startDialog.setVisible(true);
		} else {
			String input = args[0].toLowerCase();
			if (input.equals("-s")) {
				serverClientChoice = 's';
			} else if (input.equals("-c")) {
				serverClientChoice = 'c';
			} else if (input.length() > 2 && input.substring(0, 2).equals("-c")) {
				serverClientChoice = 'c';
				try {
					instanceAmount = Integer.valueOf(input.substring(2));
				} catch (Exception e) {
				}
			} else {
				System.out.println(
						"No expected command line parameters. Use \"-s\" to start a server or \"-c\" to start a client.");
				System.exit(1);
			}

			// process command line parameters
			switch (serverClientChoice) {
			case 's':
			case 'S':
				System.out.println("Starting server...");
				ServerMain.main(args);
				break;

			case 'c':
			case 'C':
				System.out.println("Starting client...");
				if (instanceAmount > 1) {
					StringBuilder cmd = new StringBuilder();
					cmd.append("\"" + System.getProperty("java.home") + File.separator + "bin" + File.separator
							+ "java\" ");
					cmd.append("-cp ").append(ManagementFactory.getRuntimeMXBean().getClassPath()).append(" ");
					cmd.append(run.class.getName()).append(" ");
					cmd.append("-c ").append(" ");
					cmd.append("-autologin").append(" ");
					cmd.append("-clientid client_");
					try {
						for (int i = 0; i < instanceAmount - 1; i++) {
							Runtime.getRuntime().exec(cmd.toString() + String.format("%03d", i + 1));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					args = Arrays.copyOf(args, args.length + 3);
					args[args.length - 3] = "-autologin";
					args[args.length - 2] = "-clientid";
					args[args.length - 1] = "client_" + String.format("%03d", instanceAmount);
				}
				ClientMain.main(args);
				break;

			default:
				System.out.println("No expected command line parameters.");
				System.exit(1);
				break;
			}
		}
	}
}
