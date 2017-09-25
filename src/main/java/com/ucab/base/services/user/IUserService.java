package com.ucab.base.services.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.ucab.base.commons.exceptions.CustomBaseException;
import com.ucab.base.commons.exceptions.CustomDatabaseOperationException;
import com.ucab.base.data.models.security.User;

public interface IUserService {
	
	/**
	 * Returns all registered users by username
	 * @param pageable, keyword
	 * @return
	 * @throws CustomDatabaseOperationException
	 */
	@Transactional(readOnly = true)
	Page<User> listAllUsers(Pageable pageable, String keyword) throws CustomDatabaseOperationException;
    
	 /**
	 * Returns the user with the given ID.
	 * @param userID
	 * @return Queried user.
	 * @throws CustomBaseException
	 */
	User findByID(Long userID) throws CustomBaseException;
    
    /**
	 * Returns the user with the given email address.
	 * @param email
	 * @return Queried user.
	 * @throws CustomBaseException
	 */
    @Transactional
    User findByEmail(String email) throws CustomBaseException;
    
    @Transactional
    User createUser(User user) throws CustomBaseException;

    /**
  	 *Updates the name and session expiration time of the current user.
  	 * @param user
  	 * @return The updated user.
  	 * @throws CustomBaseException
  	 */
    User updateCurrentUserInformation(User user) throws CustomBaseException;
    
    /**
  	 *Updates  the name, session expiration time and role of the user.
  	 * @param user
  	 * @return The updated user.
  	 * @throws CustomBaseException
  	 */
    User updateUserInformation(User user) throws CustomBaseException;
    
    boolean changeSessionTimeout(Long userID, long timeout) throws CustomBaseException;
    
    /**
     * Verifies if the given password matches with the user current password.
     * @param user
     * @param password
     * @return true if the password matches false if don't.
     * @throws CustomBaseException if a database connection or operation fails.
     */
    boolean passwordMatches(User user, String password) throws CustomBaseException;
    
    /**
     * Updates the password of the user.
     * @param user
     * @param password
     * @return 
     * @throws CustomBaseException
     */
    boolean changePassword(User user, String password) throws CustomBaseException;
    
    /**
     * Updates the password of the user if the old password exists and the format of the new password is valid.
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @return true if the request to update password is valid false if don't.
     * @throws CustomBaseException
     */
    boolean updatePassword(Long userID, String oldPassword, String newPassword) throws CustomBaseException;
    
    boolean validateEmail(String emailAddress) throws CustomBaseException;

	@Transactional
	User saveUser(User user) throws CustomDatabaseOperationException;
	
	/**
     * Creates a reset password request by sending a email to the user with a reset password link.
     * @param emailAddress
     * @return the user who request the reset password operation if user does not exist return null.
     * @throws CustomBaseException
     */
	User forgotPassword(String emailAddress) throws CustomBaseException;
	
	/**
     * Validates if the reset password request is valid to reset the password of the user.
     * @param resetToken
     * @param userID
     * @return returns the user of the reset password request if the request is valid null if don't.
     * @throws CustomBaseException
     */
	User findByResetPasswordRequest(String resetToken, Long userID) throws CustomBaseException;
	
	/**
     * Reset the password of the user if the request is valid.
     * @param userID
     * @param resetToken
     * @param newPassword
     * @return True if the operation was executed successfully false if don't.
     * @throws CustomBaseException
     */
	boolean resetPassword(Long userID, String resetToken, String newPassword) throws CustomBaseException;
	
	/**
	 * Switches the client status from active to deactivate and returns the client active status.
	 * <p>
	 * @param recID.
	 * @return boolean.
	 * @throws CustomBaseException
	 */
	@Transactional
	boolean deactivate(Long id) throws CustomBaseException;
	
	/**
	 * Switches the user status from active to deactivate and returns the user active status.
	 * <p>
	 * @param recID
	 * @return boolean.
	 * @throws CustomBaseException
	 */
	
	boolean activate(Long id) throws CustomBaseException;
}