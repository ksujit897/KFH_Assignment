package dto;


import lombok.Data;

@Data
public class StudentDTO {
    private Long id;
    private String fullNameEnglish;
    private String fullNameArabic;
    private String email;
    private String telephoneNumber;
    private String address;
}
