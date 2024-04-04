package org.example.catalogservice.util;

import org.example.catalogservice.jpa.CatalogEntity;
import org.example.catalogservice.vo.ResponseCatalog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CatalogMapper {
    CatalogMapper INSTANCE = Mappers.getMapper(CatalogMapper.class);

    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "productName", source = "productName")
    @Mapping(target = "unitPrice", source = "unitPrice")
    @Mapping(target = "stock", source="stock")
    @Mapping(target = "createdAt", source="createdAt")
    ResponseCatalog entityToDto(CatalogEntity catalogEntity);
}
