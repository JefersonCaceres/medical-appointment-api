package com.jefferson.medical_appointment_api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medical_doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalDoctor {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String fullName;

  @Column(nullable = false, length = 100)
  private String specialty;

  @Column(length = 20)
  private String phone;

  @Column(length = 150)
  private String email;
}
