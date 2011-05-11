package lt.reserv.jetty;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;

import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Used to launch embeded jetty server, for example in Eclipse.
 * 
 */
public class JettyLauncher {
	
	private static final Logger log = LoggerFactory.getLogger(JettyLauncher.class);

	/**
	 * Main function, starts the jetty server.
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		String context = null;
		String webAppDir = null;
		Integer port = null;
		String jettyEnv = null;
		
		for(int i=0; i < args.length;i++){
			if("-context".equals(args[i])){
				context=args[i+1];
			} else if("-webapp".equals(args[i])){
				webAppDir=args[i+1];
			} else if("-port".equals(args[i])){
				port=Integer.valueOf(args[i+1]);
			} else if ("-jettyEnv".equalsIgnoreCase(args[i]) ) {
				jettyEnv = args[i+1];
			}
		}
		
		if(context == null || webAppDir == null || port == null){
			System.err.println("JettyLauncher requires following parameters:");
			System.err.println("-context \t web application context");
			System.err.println("-webapp \t path pointing to web appplication root");
			System.err.println("-port   \t server port");
			System.exit(-1);
		}

		log.info("Starting server context: " +context + ", webapp: " + webAppDir + ", port: "+port);

		Server server = new Server();
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(port);
		server.addConnector(connector);
		

		WebAppContext web = new WebAppContext();
		web.setContextPath(context);
		web.setResourceBase(webAppDir);

		web.setParentLoaderPriority(true);
		server.setHandler(web);

		MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
		server.getContainer().addEventListener(mBeanContainer);
		mBeanContainer.start();

		try {
			server.start();
			server.join();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
