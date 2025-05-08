package com.example.demo.mapping;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * @param <E>  entity
 * @param <RQ> request
 * @param <RS> response
 * @author Asadbek
 */
public interface EntityMapping<E, RQ, RS> {

    E toEntity(RQ req);

    RS toDto(E entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(RQ req, @MappingTarget E entity);

    List<E> toEntity(List<RQ> dtoList);

    List<RS> toDto(List<E> entityList);
}
