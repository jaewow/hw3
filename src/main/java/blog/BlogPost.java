package blog;

import java.util.Date;

import com.google.appengine.api.users.User;


public class BlogPost implements Comparable<BlogPost>{
    
    User user;
    String title;
    String content;
    Date date;
    
    public BlogPost(User user, String content, String guestbookName) {
        this.user = user;
        this.content = content;
        date = new Date();
    }
    public User getUser() {
        return user;
    }
    public String getContent() {
        return content;
    }
    
    @Override
    public int compareTo(BlogPost other) {
        if (date.after(other.date)) {
            return 1;
        } else if (date.before(other.date)) {
            return -1;
        }
        return 0;
     }
}
