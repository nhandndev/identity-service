package com.nhan.identity_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class InvalidatedTokens {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "expiryTime")
    private Date expiryTime ;
}
