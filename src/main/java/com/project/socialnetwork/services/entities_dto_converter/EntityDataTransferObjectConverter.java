package com.project.socialnetwork.services.entities_dto_converter;

public interface EntityDataTransferObjectConverter<T,D> {

    D convertToDTO(T entity);

    T convertToEntity(D dto);

}
