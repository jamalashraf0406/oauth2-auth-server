package com.yutube.oauth2.entity;

import com.yutube.oauth2.enums.RoleName;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name="roles")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Enumerated(EnumType.STRING)
    private RoleName name;
}
