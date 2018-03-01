package blog;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;


@SuppressWarnings("serial")



public class CronServlet extends HttpServlet {
	private static final Logger _logger = Logger.getLogger(CronServlet.class.getName());
	
	private static int index;
	private static boolean hasStarted = false; 
	
public void doGet(HttpServletRequest req, HttpServletResponse resp)
throws IOException {
	
	
	
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Query query = new Query("Subscribers");
    Query q2 = new Query("BlogPost").addSort("date", Query.SortDirection.ASCENDING);
    List<Entity> posts = datastore.prepare(q2).asList(FetchOptions.Builder.withLimit(10000));
    int size = posts.size();
    
    if(!CronServlet.hasStarted) {
		CronServlet.hasStarted = true;
		CronServlet.index = 0;
	}
	else { //hasStarted
		if(CronServlet.index == size) {
			return;
		}
	}
    
    List<Entity> subs = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(10000));
    
    String strBody = "";
	for(int x = CronServlet.index; x < size; x++) {
		Entity b = posts.get(x);
		strBody = strBody + "Title : " +  b.getProperty("title");
		strBody = strBody + "\n";
		strBody = strBody + "Body : " + b.getProperty("content") + "\n";
		strBody = strBody + "on " + b.getProperty("date") + "\n\n";
	}
    
	_logger.info(strBody);
	
    for(Entity a : subs) {
	
	String strCallResult = "";
	resp.setContentType("text/plain");
	try {
		//Extract out the To, Subject and Body of the Email to be sent
		
		User subscriber = (User) a.getProperty("user");
		String strTo = subscriber.getEmail();
		String strSubject = "The Basic Blog Updates!";
		
		
		
		//Do validations here. Only basic ones i.e. cannot be null/empty
		//Currently only checking the To Email field
		if (strTo == null) throw new Exception("To field cannot be empty.");
		
		//Trim the stuff
		strTo = strTo.trim();
		if (strTo.length() == 0) throw new Exception("To field cannot be empty.");
		
		//Call the GAEJ Email Service
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("daily@homework3-blog-196421.appspotmail.com"));
		msg.addRecipient(Message.RecipientType.TO,
		
		new InternetAddress(strTo));

		msg.setSubject(strSubject);
		msg.setText(strBody);
		Transport.send(msg);

		strCallResult = "Success: " + "Email has been delivered.";
		resp.getWriter().println(strCallResult);
	}
	catch (Exception ex) {
		strCallResult = "Fail: " + ex.getMessage();
		resp.getWriter().println(strCallResult);
	}
	
    }
    
    CronServlet.index = posts.size();
}

@Override
public void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		doGet(req, resp);
	}
}