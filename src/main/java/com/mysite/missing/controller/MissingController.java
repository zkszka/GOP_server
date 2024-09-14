package com.mysite.missing.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mysite.missing.entity.Missing;
import com.mysite.missing.service.MissingService;

@RestController
@RequestMapping("/api/missing")
public class MissingController {

    private final MissingService missingService;

    @Autowired
    public MissingController(MissingService missingService) {
        this.missingService = missingService;
    }

    @PostMapping
    public ResponseEntity<?> registerMissing(
            @RequestParam("petName") String petName,
            @RequestParam("species") String species,
            @RequestParam("description") String description,
            @RequestParam("contactInfo") String contactInfo,
            @RequestParam(value = "photo", required = false) MultipartFile photo) {

        Missing missing = new Missing();
        missing.setPetName(petName);
        missing.setSpecies(species);
        missing.setDescription(description);
        missing.setContactInfo(contactInfo);

        if (photo != null) {
            try {
                missing.setPhoto(photo.getBytes());
            } catch (IOException e) {
                return new ResponseEntity<>("사진 업로드 실패", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        Missing savedMissing = missingService.saveLostPet(missing);
        return new ResponseEntity<>(savedMissing, HttpStatus.CREATED);
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<Missing>> getAllMissing() {
        List<Missing> allMissing = missingService.getAllMissing();
        return new ResponseEntity<>(allMissing, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMissing(@PathVariable Long id) {
        try {
            missingService.deleteMissing(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("삭제 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
