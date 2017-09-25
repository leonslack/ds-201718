package com.ucab.base.data.models;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.hibernate.annotations.Where;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.context.SecurityContextHolder;

//TODO DELETE AuditedTags FIX TO STRING METHOD
@MappedSuperclass
@Where(clause = "IsDeleted=0")
public abstract class CustomBaseEntity implements Serializable {

	private static final long serialVersionUID = 6881023937886972341L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "RecID", unique = true, nullable = false )	
	private Long id;
	public static final String _id = "id";
	
	@Column(name = "IsActive", nullable = false)
	private boolean isActive = true;
	public static final String _isActive = "isActive";
	
	@Column(name = "IsDeleted", nullable = false)
	private boolean isDeleted = false;

	@CreatedBy
	@Column(name = "CreatedBy", updatable=false)
	@NotAudited
	private String createdBy;
	public static final String _createdBy = "createdBy";
	
	@CreatedDate
	@Column(name = "CreatedAt", columnDefinition = "DATETIME", updatable=false)
	@NotAudited
	private Instant createdAt;
	public static final String _createdAt = "createdAt";
	
	
	@LastModifiedBy
	@Column(name = "UpdatedBy")
	private String updateBy;

	@LastModifiedDate
	@Column(name = "UpdatedAt", columnDefinition = "DATETIME")
	private Instant updatedAt;

	public CustomBaseEntity() {
		
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof CustomBaseEntity)) {
			return false;
		}
		if(id== null){
			return false;
		}
		CustomBaseEntity other = (CustomBaseEntity) obj;
		return getId().equals(other.getId());
	}

	public Long getId() {
		if(id!=null)
			return id;
		else
			return null;
	}

	public void setId(Long id) {
		if(id!=null)
		{
			this.id = id;
		}
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}

	@PrePersist
	void onCreate() {
		Instant currentDate = Instant.now();

		String currentUser;
		try{
			currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
		}catch(NullPointerException e){
			currentUser = "System";
		}

		this.createdAt = currentDate;
		this.createdBy = currentUser;

		this.updateBy = currentUser;
		this.updatedAt = currentDate;

	}

	@PreUpdate()
	void onUpdate() {

		Instant currentDate = Instant.now();
		
		String currentUser;
		try{
			currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
		}catch(NullPointerException e){
			currentUser = "System";
		}
		this.updateBy = currentUser;
		this.updatedAt = currentDate;

	}
	
	
	protected final String getBasetoString(){
		
		String entity = "\nRecID:";
		if(id!= null)
			entity += id.toString();
		
		entity+= "\nIs Active:";
		entity += isActive;
		
		entity+= "\nIs Deleted:";
		entity += isDeleted;
		
		entity+= "\nCreated By:";
		if(createdBy!= null){
			entity += createdBy;
		}
		
		entity+= "\nCreated At:";
		if(createdAt != null){
		//	entity += DateHelper.formatDate(createdAt);
		}
		
		entity+= "\nUpdated By:";
		if(updateBy!= null){
			entity += updateBy;
		}
		
		entity+= "\nUpdated At:";
		if(updatedAt!= null){
		//		entity += DateHelper.formatDate(updatedAt);
		}
		return entity;
		
	}

}