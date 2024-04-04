package org.example.catalogservice.controller;

import org.example.catalogservice.jpa.CatalogEntity;
import org.example.catalogservice.service.CatalogService;
import org.example.catalogservice.util.CatalogMapper;
import org.example.catalogservice.vo.ResponseCatalog;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/catalog-service")
public class CatalogController {
    Environment env;
    CatalogService catalogService;

    public CatalogController(Environment env, CatalogService catalogService) {
        this.env = env;
        this.catalogService = catalogService;
    }

    @GetMapping("/health-check")
    public String status() {
        return String.format("It's Working in catalog-Service on PORT %s",
                env.getProperty("local.server.port"));
    }

    // 전체 상품 조회
    @GetMapping("/catalogs")
    public ResponseEntity<List<ResponseCatalog>> getUsers(){
        Iterable<CatalogEntity> userList = catalogService.getAllCatalogs();

        List<ResponseCatalog> result = new ArrayList<>();
        userList.forEach(v->{
            result.add(CatalogMapper.INSTANCE.entityToDto(v));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
