package fr.systemx.mic.servlet;

import com.artelys.mic.control.Control;
import com.artelys.mic.project.Project;
import com.artelys.mic.project.ProjectManager;
import com.artelys.mic.uc.ControlDistribution;
import com.artelys.mic.util.TDMException;
import com.wordnik.swagger.jaxrs.config.BeanConfig;
import fr.systemx.mic.resource.utils.ZoningUtil;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.swing.*;
import java.io.File;
import java.util.Properties;

public class InitializationServlet extends HttpServlet {
	
	private static final transient Logger LOG = Logger.getLogger(InitializationServlet.class);
	private final static String PROP_DATA_PATH = "data.path";

	@Override
	public void init(ServletConfig conf) {
		try {
			BasicConfigurator.configure();

			// Chargement des properties
			LOG.info("Chargement des properties ...");
			Properties properties = new Properties();

			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			properties.load(classLoader.getResourceAsStream("mic.properties"));

			String dataPath = System.getProperty(PROP_DATA_PATH, "");
			if(dataPath.equals("")) dataPath = properties.getProperty("data.path");
			System.out.println("Répertoire de données de distribution ampl : "+ ControlDistribution.dir.getAbsolutePath());
			System.out.println("Données utilisées: "+dataPath);
			//JFrame frame = new JFrame();
			//Control control = new Control(frame);
			new Control();
			ProjectManager pm = new ProjectManager();
			File f = new File(dataPath);
			try {
				Project p = pm.openProject(f);
			} catch (TDMException e) {
				e.printStackTrace();
			}

			BeanConfig beanConfig = new BeanConfig();
			beanConfig.setVersion("1.0.2");
			beanConfig.setBasePath("http://localhost:8080/rest");
			beanConfig.setResourcePackage("fr.systemx.mic.resource");
			beanConfig.setScan(true);

		} catch (Exception e) {
			LOG.error(e);
    		e.printStackTrace();
		}
	}

}
