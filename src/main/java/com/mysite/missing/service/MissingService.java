package com.mysite.missing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysite.missing.entity.Missing;
import com.mysite.missing.repository.MissingRepository;

@Service
public class MissingService {
    private final MissingRepository missingRepository;

    @Autowired
    public MissingService(MissingRepository missingRepository) {
        this.missingRepository = missingRepository;
    }

    public Missing saveLostPet(Missing missing) {
        return missingRepository.save(missing);
    }
    
    public List<Missing> getAllMissing() {
        return missingRepository.findAll();
    }
    
    public void deleteMissing(Long id) {
        missingRepository.deleteById(id);
    }
}
