package com.project.socialnetwork.services.entity_data_transfer_object_converters;

import org.modelmapper.ModelMapper;

public abstract class EntityDataTransferObjectConverter<T,D>{
    protected final ModelMapper modelMapper;

    public EntityDataTransferObjectConverter(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    public abstract D convertToDTO(T entity);

    public abstract T convertToEntity(D dto);
}
