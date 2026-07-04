package com.jefferson.medical_appointment_api.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String fullName;

  @Column(nullable = false, unique = true, length = 30)
  private String document;

  @Column(nullable = false, length = 20)
  private String phone;

  @Column(nullable = false, length = 150)
  private String email;

  @Column
  private LocalDate birthDate;
}
