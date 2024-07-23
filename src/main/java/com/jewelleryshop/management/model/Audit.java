package com.jewelleryshop.management.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Audit {

    @CreatedDate
    @Field("created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Field("last_modified_date")
    private LocalDateTime lastModifiedDate;
}