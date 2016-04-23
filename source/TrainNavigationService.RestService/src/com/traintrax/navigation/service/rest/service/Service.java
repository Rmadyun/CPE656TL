package com.traintrax.navigation.service.rest.service;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.restlet.Component;
import org.restlet.data.Protocol;

/**
 * Class responsible for launching the Restful Service
 * of the Train Navigation Service
 * @author Corey Sanders
 *
 */
public class Service {

	/**
	 * Main function invoked when the service launches.
	 * @param args
	 * @throws Exception throws an exception if starting the web service fails.
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
		Option mduPortOption = Option.builder("m").required(false).argName("mdu serial port").hasArg().longOpt("mdu-port")
				.build();
		Option locoNetPortOption = Option.builder("t").required(false).argName("pr3 serial port").hasArg().longOpt("pr3-port")
				.build();

		Option helpOption = Option.builder("h").required(false).longOpt("help").build();

		commandlineOptions.addOption(hostPortOption);
		commandlineOptions.addOption(dbUsernameOption);
		commandlineOptions.addOption(dbPasswordOption);
		commandlineOptions.addOption(dbNameOption);
		commandlineOptions.addOption(dbHostnameOption);
		commandlineOptions.addOption(dbPortOption);
		commandlineOptions.addOption(mduPortOption);
		commandlineOptions.addOption(locoNetPortOption);
		commandlineOptions.addOption(helpOption);

		DefaultParser defaultParser = new DefaultParser();
		Boolean showHelp = false;
		String portValue = "";
		String dbUser = "";
		String dbPassword = "";
		String dbName = "";
		String dbHost = "";
		String dbPortValue = "";
		String mduPort = "";
		String pr3Port = "";
		//Retrieve the current service configuration
		TrainNavigationServiceConfiguration serviceConfiguration = new TrainNavigationServiceConfiguration(); 


		try {
			CommandLine parsedCommandLine = defaultParser.parse(commandlineOptions, args);

			if (parsedCommandLine.hasOption(helpOption.getOpt())) {
				showHelp = true;
			}

			portValue = parsedCommandLine.getOptionValue(hostPortOption.getOpt(), Integer.toString(serviceConfiguration.getHostPort()));
			dbUser = parsedCommandLine.getOptionValue(dbUsernameOption.getOpt(), serviceConfiguration.getDbUsername());
			dbPassword = parsedCommandLine.getOptionValue(dbPasswordOption.getOpt(), serviceConfiguration.getDbPassword());
			dbName = parsedCommandLine.getOptionValue(dbNameOption.getOpt(), serviceConfiguration.getDbName());
			dbHost = parsedCommandLine.getOptionValue(dbHostnameOption.getOpt(), serviceConfiguration.getDbHost());
			dbPortValue = parsedCommandLine.getOptionValue(dbPortOption.getOpt(), Integer.toString(serviceConfiguration.getDbPort()));
			mduPort = parsedCommandLine.getOptionValue(dbHostnameOption.getOpt(), serviceConfiguration.getMduSerialPort());
			pr3Port = parsedCommandLine.getOptionValue(dbHostnameOption.getOpt(), serviceConfiguration.getPr3SerialPort());

		} catch (MissingOptionException exception) {
			System.out.println(exception.getMessage());
			showHelp = true;
		}

		if (showHelp) {
			// automatically generate the help statement
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("TrainNavigationService.RestService", commandlineOptions);
			return;
		}

		//Populate the service configuration
		serviceConfiguration.setHostPort(Integer.parseInt(portValue));
		serviceConfiguration.setDbUsername(dbUser);
		serviceConfiguration.setDbPassword(dbPassword);
		serviceConfiguration.setDbName(dbName);
		serviceConfiguration.setDbHost(dbHost);
		serviceConfiguration.setDbPort(Integer.parseInt(dbPortValue));
		serviceConfiguration.setMduSerialPort(mduPort);
		serviceConfiguration.setPr3SerialPort(pr3Port);
		
		//Initialize the service for use
		TrainNavigationServiceSingleton.initialize(serviceConfiguration);

		
		//Initializes the Service
		 // Create a new Restlet component and add a HTTP server connector to it
	    Component component = new Component();
	    component.getServers().add(Protocol.HTTP, 8183);

	    // Then attach it to the local host
	    component.getDefaultHost().attach("/TrainPosition", TrainPositionResource.class);
	    component.getDefaultHost().attach("/TrainIdentifiers", TrainIdentifierResource.class);
	    component.getDefaultHost().attach("/TrackSwitchState", TrackSwitchStateResource.class);

	    // Now, let's start the component!
	    // Note that the HTTP server connector is also automatically started.
	    component.start();
	}

}
