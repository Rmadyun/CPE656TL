package com.traintrax.navigation.database.rest.service;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.restlet.Component;
import org.restlet.data.Protocol;

/**
 * Class responsible for launching the Restful Service of the Train Navigation
 * Database
 * 
 * @author Corey Sanders
 *
 */
public class Service {


	/**
	 * Main function invoked when the service launches.
	 * 
	 * @param args
	 * @throws Exception
	 *             throws an exception if starting the web service fails.
	 */
	public static void main(String[] args) throws Exception {

		// Setup expected command line arguments
		Options commandlineOptions = new Options();

		// Options
		Option hostPortOption = Option.builder("s").required(false).argName("http port").hasArg().longOpt("service-port")
				.build();
		Option dbUsernameOption = Option.builder("u").required(false).argName("database username").hasArg().longOpt("db-user")
				.build();
		Option dbPasswordOption = Option.builder("p").required(false).argName("database password").hasArg().longOpt("db-pwd")
				.build();
		Option dbNameOption = Option.builder("n").required(false).argName("database name").hasArg().longOpt("db-name")
				.build();
		Option dbHostnameOption = Option.builder("a").required(false).argName("database host").hasArg().longOpt("db-host")
				.build();
		Option dbPortOption = Option.builder("d").required(false).argName("database port").hasArg().longOpt("db-port")
				.build();

		Option helpOption = Option.builder("h").required(false).longOpt("help").build();

		commandlineOptions.addOption(hostPortOption);
		commandlineOptions.addOption(dbUsernameOption);
		commandlineOptions.addOption(dbPasswordOption);
		commandlineOptions.addOption(helpOption);

		DefaultParser defaultParser = new DefaultParser();
		Boolean showHelp = false;
		String portValue = "";
		String dbUser = "";
		String dbPassword = "";
		String dbName = "";
		String dbHost = "";
		String dbPortValue = "";
		//Retrieve the current database configuration
		TrainNavigationDatabaseConfiguration dbConfiguration = new TrainNavigationDatabaseConfiguration(); 


		try {
			CommandLine parsedCommandLine = defaultParser.parse(commandlineOptions, args);

			if (parsedCommandLine.hasOption(helpOption.getOpt())) {
				showHelp = true;
			}

			portValue = parsedCommandLine.getOptionValue(hostPortOption.getOpt(), Integer.toString(dbConfiguration.getHostPort()));
			dbUser = parsedCommandLine.getOptionValue(dbUsernameOption.getOpt(), dbConfiguration.getDbUsername());
			dbPassword = parsedCommandLine.getOptionValue(dbPasswordOption.getOpt(), dbConfiguration.getDbPassword());
			dbName = parsedCommandLine.getOptionValue(dbNameOption.getOpt(), dbConfiguration.getDbName());
			dbHost = parsedCommandLine.getOptionValue(dbHostnameOption.getOpt(), dbConfiguration.getDbHost());
			dbPortValue = parsedCommandLine.getOptionValue(dbPortOption.getOpt(), Integer.toString(dbConfiguration.getDbPort()));

		} catch (MissingOptionException exception) {
			System.out.println(exception.getMessage());
			showHelp = true;
		}

		if (showHelp) {
			// automatically generate the help statement
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("TrainNavigationDatabase.RestService", commandlineOptions);
			return;
		}

		//Populate the DB configuration
		dbConfiguration.setHostPort(Integer.parseInt(portValue));
		dbConfiguration.setDbUsername(dbUser);
		dbConfiguration.setDbPassword(dbPassword);
		dbConfiguration.setDbName(dbName);
		dbConfiguration.setDbHost(dbHost);
		dbConfiguration.setDbPort(Integer.parseInt(dbPortValue));
		
		//Initialize the database for use
		TrainDatabaseRepositoryFactory.getInstance().initialize(dbConfiguration);

		System.out.printf("Launching Service with HTTP port: %d\n", dbConfiguration.getHostPort());

		// Initializes the Service
		// Create a new Restlet component and add a HTTP server connector to it
		Component component = new Component();
		component.getServers().add(Protocol.HTTP, dbConfiguration.getHostPort());

		// Then attach it to the local host
		component.getDefaultHost().attach("/TrackPoints", TrackPointResource.class);
		component.getDefaultHost().attach("/TrackSwitches", TrackSwitchResource.class);
		component.getDefaultHost().attach("/AdjacentPoints", AdjacentPointResource.class);
		component.getDefaultHost().attach("/TrackBlocks", TrackBlockResource.class);

		// Now, let's start the component!
		// Note that the HTTP server connector is also automatically started.
		component.start();
	}

}
