package com.mysite.missing.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "missing")
public class Missing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "missing_id") // 컬럼 이름을 'missing_id'로 지정
    private Long id;

    @Column(name = "missing_name", nullable = false) // 컬럼 이름을 'missing_name'으로 지정
    private String petName;

    @Column(name = "missing_species", nullable = false) // 컬럼 이름을 'missing_species'로 지정
    private String species;

    @Column(name = "missing_description", nullable = false) // 컬럼 이름을 'missing_description'으로 지정
    private String description;

    @Column(name = "missing_contact", nullable = false) // 컬럼 이름을 'missing_contact'로 지정
    private String contactInfo;

    @Lob
    @Column(name = "missing_photo") // 컬럼 이름을 'missing_photo'로 지정
    private byte[] photo;

    @Column(name = "missing_createdAt") // 컬럼 이름을 'missing_createdAt'으로 지정
    private LocalDateTime createdAt;

    public Missing() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
