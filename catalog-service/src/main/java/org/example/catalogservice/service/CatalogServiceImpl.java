package org.example.catalogservice.service;

import org.example.catalogservice.jpa.CatalogEntity;
import org.example.catalogservice.jpa.CatalogRepository;
import org.springframework.stereotype.Service;

@Service
public class CatalogServiceImpl implements CatalogService{

    private CatalogRepository catalogRepository;

    public CatalogServiceImpl(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    @Override
    public Iterable<CatalogEntity> getAllCatalogs() {
        return catalogRepository.findAll();
    }
}
