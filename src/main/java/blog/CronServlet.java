package blog;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class CronServlet extends HttpServlet {
	
	private static final Logger _logger = Logger.getLogger(CronServlet.class.getName());
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
			try {
			_logger.info("Cron Job has been executed");
			//Put your logic here
			//BEGIN
			//END
			}
			catch (Exception ex) {
			//Log any exceptions in your Cron Job
			}
	}
}
