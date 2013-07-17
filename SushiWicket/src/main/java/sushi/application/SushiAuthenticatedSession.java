package sushi.application;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

import sushi.user.SushiUser;
import sushi.user.UserProvider;

/**
 * The session class for an authenticated session for the web application.
 * @author micha
 */
public class SushiAuthenticatedSession extends AuthenticatedWebSession {
	
	private static final long serialVersionUID = 1L;
	
	private SushiUser user;

    /**
     * Constructor for an authenticated session for the web application.
     * @param request
     */
    protected SushiAuthenticatedSession(Request request){
        super(request);
    }

    /**
     * Checks the given username and password, returning a User object if if the username and
     * password identify a valid user.
     * 
     * @param username
     *            The username
     * @param password
     *            The password
     * @return True if the user was authenticated
     */
    @Override
    public final boolean authenticate(final String username, final String password){
        this.user = UserProvider.findUser(username, password);
        return user != null;
    }

    public SushiUser getUser(){
        return user;
    }

    /**
     * Sets a new user.
     * @param user
     * 
     */
    public void setUser(final SushiUser user){
        this.user = user;
    }

    /**
     * @see org.apache.wicket.authentication.AuthenticatedWebSession#getRoles()
     */
    @Override
    public Roles getRoles(){
        return null;
    }
}

