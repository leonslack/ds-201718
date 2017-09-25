package com.ucab.base.data.models.security;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ucab.base.data.models.CustomBaseEntity;

@Entity
@Table(name = "[User]")
@Where(clause = "IsDeleted=0")
public class User extends CustomBaseEntity implements UserDetails {
	
	private static final long serialVersionUID = -4034914600895086320L;

	@Column(name = "Password", nullable = false)
    private String password;
	
    @Column(name = "Email")
    private String email;
    
    @Column(name = "Name")
    private String name;    
    
    @Column(name = "ResetPassword")
    private boolean resetPassword=true;

    @Column(name = "ExpiresIn")
	private long expires;

    @Column(name = "ResetToken")
	private String resetToken;
    
    //TODO OMAR
    @LastModifiedDate
	@Column(name = "ResetTokenCreatedAt", columnDefinition = "DATETIME")
	private Instant  resetTokenCreatedAt;  
    
    
	@OneToOne()
    @JoinColumn(name = "RoleID")	
	private Role role;
	
	
	public User(){
		super();
	}

	public User(String password, String email) {
	
		this.password = password;
		this.email = email;		
	}

	@Override
	@JsonIgnore
	public String getPassword() {
		return password;
	}

	
	
	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	public long getExpires() {
		return expires;
	}

	public void setExpires(long expires) {
		this.expires = expires;
	}	

	public String getResetToken() {
		return resetToken;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}

	public Instant getResetTokenCreatedAt() {
		return resetTokenCreatedAt;
	}

	public void setResetTokenCreatedAt(Instant resetTokenCreatedAt) {
		this.resetTokenCreatedAt = resetTokenCreatedAt;
	}

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if(role== null){
			return Collections.emptySet();
		}
		return role.getPermissions();
	}

	@Override
	public String getUsername() {
		return email;
	}
	
	public void setUsername(String username) {
		this.email = username;
	}
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	public boolean getResetPassword() {
		return resetPassword;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isResetPassword() {
		return resetPassword;
	}

	public void setResetPassword(boolean resetPassword) {
		this.resetPassword = resetPassword;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
	
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		
		return true;
	}

	@Override
	public String toString(){
		
		String user = "User :";
		user+= "\nRecID: ";
		if(getId()!= null)
		{
			user+= getId();
		}
		user+= "\nEmail: ";
		if(getEmail()!=null)
		{
			user+=this.email;
		}
		user+="\nName:";
		if(name!=null)
		{
			user+=this.name;
		}
		if(password!=null)
		{
			user+=this.password;
		}
		user+="\nRoleId:";
		if(role!=null)
		{
			user+=this.role.getId().toString();
		}
		user+="\nReset Password:";
		
		user+=this.resetPassword;
		
		//Reset token
		
		if(this.resetToken != null){
			
			user+="\nReset Token:";
		
			user+=this.resetToken;
		}
		
		if(this.resetTokenCreatedAt != null){
			
			user+="\nReset Token Created At:";
			
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			
			user+=dateFormat.format(this.resetTokenCreatedAt);
		}
		
		return user;
		
	}
}
