package dto;

import lombok.Data;

@Data
public class StudentCourseDTO {
    private Long id;
    private StudentDTO student;
    private CourseDTO course;
}
