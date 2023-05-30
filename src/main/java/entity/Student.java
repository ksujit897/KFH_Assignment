package entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullNameEnglish;
    private String fullNameArabic;
    private String email;
    private String telephoneNumber;
    private String address;
}
