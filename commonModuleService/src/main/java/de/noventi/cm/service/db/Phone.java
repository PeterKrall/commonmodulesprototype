package de.noventi.cm.service.db;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Phone {

  @Id
  private String id;

  @Enumerated(EnumType.STRING)
  private PhoneType type;

  private String number;
}
