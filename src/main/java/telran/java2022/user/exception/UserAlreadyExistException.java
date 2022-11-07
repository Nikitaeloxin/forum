package telran.java2022.user.exception;

public class UserAlreadyExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1340414946097454680L;
	
	public UserAlreadyExistException(String login) {
		super("User with login = " + login + " already exist");
	}

}
