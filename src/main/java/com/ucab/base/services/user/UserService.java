package com.ucab.base.services.user;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ucab.base.commons.exceptions.CustomAlreadyExistsException;
import com.ucab.base.commons.exceptions.CustomBaseException;
import com.ucab.base.commons.exceptions.CustomDatabaseOperationException;
import com.ucab.base.commons.exceptions.CustomInvalidActionException;
import com.ucab.base.commons.exceptions.CustomInvalidAttributeException;
import com.ucab.base.commons.exceptions.CustomMissingAttributeException;
import com.ucab.base.commons.exceptions.CustomRegistryNotFoundException;
import com.ucab.base.commons.validators.DataValidator;
import com.ucab.base.data.models.security.User;
import com.ucab.base.data.repositories.UserRepository;
import com.ucab.base.data.specifications.UserSpecifications;

@Service("userService")
public class UserService implements IUserService {

	final static Logger log = Logger.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private Environment environment;

	@Override
	public Page<User> listAllUsers(Pageable pageable, String keyword) throws CustomDatabaseOperationException {

		log.debug("Starting list all users");

		Page<User> result = null;

		try {

			log.debug("Trying to call findAll method of the repository");

			if (keyword != null && !keyword.isEmpty()) {

				result = userRepository.findAll(UserSpecifications.recordContains(keyword), pageable);
			} else {

				result = userRepository.findAll(pageable);
			}

		} catch (Exception e) {

			log.error(String.format("Error while trying to find users pageable: %s \n %s", pageable.toString(),
					e.getMessage()), e);
			throw new CustomDatabaseOperationException(
					String.format("Error while trying to find Users %s" + e.getMessage()));
		}

		log.debug(String.format("Finishing list all users with %d results", result.getSize()));

		return result;
	}

	@Override
	public User findByEmail(String email) throws CustomBaseException {

		if (email == null) {

			log.info("The given email address is empty.");
			throw new CustomMissingAttributeException("Null Attribute Email Address");
		}

		User user = null;

		try {

			user = userRepository.findByEmail(email);

		} catch (Exception e) {
			log.error("Error while trying to find user by email", e);
			throw new CustomDatabaseOperationException(e.getMessage());
		}

		return user;
	}

	private boolean isUsernameTaken(String username) throws CustomBaseException {
		boolean response;
		if (findByEmail(username) == null) {
			response = false;
		} else {
			response = true;
		}
		return response;
	}

	@Override
	public User createUser(User user) throws CustomBaseException {
		DataValidator.IsEmailFormat(user.getUsername());
		if (isUsernameTaken(user.getUsername())) {
			throw new CustomAlreadyExistsException("User with email: " + user.getUsername() + " Already Exist");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setExpires(1800000);
		User response = saveUser(user);
		//TODO: Send Creation Email
		return response;
	}

	public User updateCurrentUserInformation(User user) throws CustomBaseException {

		if (user == null) {
			log.info("The given user is empty.");
			throw new CustomMissingAttributeException("Null Attribute User");
		}


		User queriedUser = this.findByID(user.getId());

		queriedUser.setName(user.getName());
		queriedUser.setExpires(sessionTimeoutValidation(user.getExpires()));

		return this.saveUser(queriedUser);
	}

	@Override
	public User updateUserInformation(User user) throws CustomBaseException {

		if (user == null) {
			log.info("The given user is empty.");
			throw new CustomMissingAttributeException("Null Attribute User");
		}


		User queriedUser = this.findByID(user.getId());

		queriedUser.setName(user.getName());
		queriedUser.setExpires(sessionTimeoutValidation(user.getExpires()));

		if (user.getRole() != null) {

			queriedUser.setRole(user.getRole());
		}

		return this.saveUser(queriedUser);
	}

	public User findByID(Long userID)
			throws CustomDatabaseOperationException, CustomRegistryNotFoundException, CustomMissingAttributeException {
		User user;

		try {
			user = userRepository.findById(userID);
		} catch (Exception e) {
			log.error("Error while trying to find user", e);
			throw new CustomDatabaseOperationException(e.getMessage());
		}
		if (user == null) {
			log.info("Could not find user with ID: " + userID.toString());
			throw new CustomRegistryNotFoundException("User With Recid: " + userID.toString() + " Not Found");
		}

		return user;
	}

	public boolean passwordMatches(User user, String password) throws CustomBaseException {

		return passwordEncoder.matches(password, user.getPassword());
	}

	public boolean changePassword(User user, String password) throws CustomBaseException {
		DataValidator.IsValidPasswordFormat(password);

		user.setPassword(passwordEncoder.encode(password));

		saveUser(user);

		return true;
	}

	@Override
	public boolean updatePassword(Long userID, String oldPassword, String newPassword) throws CustomBaseException {

		User user = findByID(userID);

		if (!this.passwordMatches(user, oldPassword)) {

			log.info("The given password does not match with user's current");
			throw new CustomInvalidActionException("The given password does not match with user's current");
		}

		return this.changePassword(user, newPassword);
	}

	@Override
	public User saveUser(User user) throws CustomDatabaseOperationException {

		try {
			user = userRepository.save(user);
			user = userRepository.findById(user.getId());
		} catch (Exception e) {
			log.error("Error while trying to save user: " + e.getMessage());
			throw new CustomDatabaseOperationException("Error while trying save user :" + e.getMessage());
		}

		return user;
	}

	private long sessionTimeoutValidation(long timeout) throws CustomBaseException {

		if ((timeout >= 5) && (timeout <= 60)) {
			timeout = timeout * 60 * 1000; // segs times milisecs
		} else {
			throw new CustomInvalidAttributeException("Session time must be at least 5 minutes and at most 60 minutes");
		}

		return timeout;
	}

	@Override
	public boolean changeSessionTimeout(Long userID, long timeout) throws CustomBaseException {
		User user = findByID(userID);
		timeout = sessionTimeoutValidation(timeout);
		user.setExpires(timeout);
		saveUser(user);
		return true;
	}

	@Override
	public boolean validateEmail(String emailAddress) throws CustomBaseException {
		return DataValidator.IsEmailFormat(emailAddress);
	}

	private User findByResetToken(String resetToken) throws CustomBaseException {

		if (resetToken == null) {

			log.info("The given reset token is empty.");
			throw new CustomMissingAttributeException("Null Attribute Reset Token");
		}

		User user = null;

		try {

			user = userRepository.findByResetToken(resetToken);

		} catch (Exception e) {

			log.error("Error while trying to find user", e);
			throw new CustomDatabaseOperationException(e.getMessage());
		}

		if (user == null) {

			log.info("Could not find user with reset token: " + resetToken);
			throw new CustomRegistryNotFoundException(String.format("User With Reset Token %s Not Found", resetToken));
		}

		return user;
	}

	private boolean isResetPasswordRequestExpired(User user) {

		Long createdTime = user.getResetTokenCreatedAt().toEpochMilli();
		Long expiryTime = Long.parseLong(environment.getProperty("reset_token.expiry_time"));// milliseconds
		Long currentTime = System.currentTimeMillis();

		if (currentTime > (createdTime + expiryTime)) {

			return true;
		}

		return false;
	}

	@Override
	public User forgotPassword(String email) throws CustomBaseException {

		User user = this.findByEmail(email);

		if (user == null) {

			log.info("Could not find user with email: " + email);
			throw new CustomRegistryNotFoundException(String.format("User With Email %s Not Found", email));

		}

		String resetToken = generateToken();

		user.setResetToken(resetToken);
		user.setResetTokenCreatedAt(Instant.now());

		user = this.saveUser(user);
		
		//TODO: Send Email

		return user;
	}
	
	private String generateToken() {
		byte[] tokenBytes = new byte[32];

		SecureRandom secureRandom = new SecureRandom();

		secureRandom.nextBytes(tokenBytes);

		String token = new String(Base64.encode(tokenBytes), StandardCharsets.UTF_8);

		return token;
	}

	@Override
	public User findByResetPasswordRequest(String resetToken, Long userID) throws CustomBaseException {

		User user = this.findByResetToken(resetToken);

		if (userID == null) {

			log.info("The given user ID address is empty.");
			throw new CustomMissingAttributeException("Null Attribute User ID");
		}

		if (!userID.equals(user.getId())) {

			log.info("User ID attribute does not match with the User ID of the reset password request");
			throw new CustomInvalidAttributeException(
					"User ID Attribute Not Match With Reset Password Request User ID");
		}

		if (this.isResetPasswordRequestExpired(user)) {

			log.info(String.format("Reset password request with reset token %s expired", resetToken));
			throw new CustomInvalidActionException("Reset Password Request Expired");
		}

		return user;
	}

	@Override
	public boolean resetPassword(Long userID, String resetToken, String newPassword) throws CustomBaseException {

		User user = this.findByResetPasswordRequest(resetToken, userID);

		if (user == null) {

			return false;
		}

		user.setResetToken(null);
		user.setResetTokenCreatedAt(null);

		return this.changePassword(user, newPassword);

	}

	@Override
	public boolean deactivate(Long id) throws CustomBaseException {
		User user;
		user = findByID(id);
		if (!user.isActive()) {
			throw new CustomInvalidActionException("User is already deactivated");
		}
		user.setActive(false);
		try {
			user = userRepository.save(user);
		} catch (Exception e) {
			log.error("Error while trying to save User \n Error: " + e.getMessage());

			throw new CustomDatabaseOperationException("Error while trying to save User \n Error: " + e.getMessage());
		}

		return user.isActive();
	}

	@Override
	public boolean activate(Long id) throws CustomBaseException {
		User user;
		user = findByID(id);
		if (user.isActive()) {
			throw new CustomInvalidActionException("User is already activated");
		}
		user.setActive(true);
		try {
			user = userRepository.save(user);
		} catch (Exception e) {
			log.error("Error while trying to save User \n Error: " + e.getMessage());

			throw new CustomDatabaseOperationException("Error while trying to save User \n Error: " + e.getMessage());
		}

		return user.isActive();
	}
}
