<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.FetchOptions" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>

 <head>
   <link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
 </head>
 

  <body>

<h1> The Basic Blog </h1>
<img src = "book_PNG2115.png" alt = "Image of Book" style="width:150px;height:100px;">

<%

    UserService userService = UserServiceFactory.getUserService();

    User user = userService.getCurrentUser();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
    boolean isSubscribed = false;
    
    if (user != null) {

      pageContext.setAttribute("user", user);
	
      Query subquery = new Query("Subscribers");
      
      List<Entity> subs = datastore.prepare(subquery).asList(FetchOptions.Builder.withLimit(10000));
      
      for(Entity a : subs) {
      	if(a.getProperty("user").equals(user)){
			isSubscribed = true;
      	}
      }
      
      
      
	%>

	<p>Hello, ${fn:escapeXml(user.nickname)}! (You can

	<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.)</p>
	

	<%
	
	if(!isSubscribed){
		%>
  		
			<form action="/subscribe" method = "post">
		<div><input type="submit" value="Subscribe!"></div>
		</form>
		
		
		<%
  	}
  

    } else {

	%>
	
	<p>Hello!

	<a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>

	to post on the blog!</p>

	<%

    }

	%>
	
	<form action="/allPosts.jsp">
    <div><input type="submit" value="View All Posts"></div>
    </form>

 	<hr>

	<%




    // Run an ancestor query to ensure we see the most up-to-date

    // view of the Greetings belonging to the selected Guestbook.

    Query query = new Query("BlogPost").addSort("date", Query.SortDirection.DESCENDING);

    List<Entity> posts = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
    
    int index = posts.size();

    if (posts.isEmpty()) {

    %>
        
		<p>Blog is currently empty!</p>
		
    <%

    } else {

        %>


        
        <%

        for (int x = 0; x < index; x++) {
        	
        	Entity post = posts.get(x);

            pageContext.setAttribute("post_content",

                                     post.getProperty("content"));

            if (post.getProperty("user") == null) {

                %>

					

                <%

            } else {

                pageContext.setAttribute("post_user",

                                         post.getProperty("user"));
                pageContext.setAttribute("post_date", post.getProperty("date"));
                
                pageContext.setAttribute("post_title", post.getProperty("title"));

                %>

                <p><b>${fn:escapeXml(post_user.nickname)}</b> wrote on ${fn:escapeXml(post_date)}:</p>

				<h3 style="font-family:Courier">${fn:escapeXml(post_title)}</h3>

				<blockquote>${fn:escapeXml(post_content)}</blockquote>
				
                <%

            }

            %>

            
            <%

        }

    }

%>

 <%	if(user != null){ %>
 

    <form action="/blog" method="post">

	  <div><textarea name="title" rows="1" cols="60" placeholder = "Insert title here..."></textarea></div>	
		
      <div><textarea name="content" rows="5" cols="60" placeholder = "Insert blog post here..."></textarea></div>

      <div><input type="submit" value="Post to blog" /></div>

      <!--  <input type="hidden" name="guestbookName" value="${fn:escapeXml(guestbookName)}"/> -->

    </form>
    
    <% } %>

 

  </body>

</html>