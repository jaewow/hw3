package blog;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class SubscribeServlet extends HttpServlet {
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)

            throws IOException {

    UserService userService = UserServiceFactory.getUserService();

    User user = userService.getCurrentUser();

    Entity sub = new Entity("Subscribers");
    
    sub.setProperty("user", user);


    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Query query = new Query("Subscribers");
    
    List<Entity> subs = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(10000));
    
    for(Entity a : subs) {
    	if(a.getProperty("user").equals(user)){
    		resp.sendRedirect("/blog.jsp");
    		return;
    	}
    }
    
    datastore.put(sub);



    resp.sendRedirect("/blog.jsp");
	}
	
}