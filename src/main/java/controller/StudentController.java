package controller;

import dto.CourseDTO;
import dto.StudentDTO;
import entity.Course;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    StudentController(final StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/register")
    public ResponseEntity<StudentDTO> registerStudent(@RequestBody StudentDTO studentDTO) {
        StudentDTO registeredStudent = studentService.registerStudent(studentDTO);
        return new ResponseEntity<>(registeredStudent, HttpStatus.CREATED);
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        List<CourseDTO> courses = studentService.getAllCourses();
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @PostMapping("/{studentId}/courses/{courseId}")
    public ResponseEntity<Void> allocateCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        studentService.allocateCourse(studentId, courseId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{studentId}/courses")
    public ResponseEntity<List<Course>> getStudentCourses(@PathVariable Long studentId) {
        List<Course> courses = studentService.getStudentCourses(studentId);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @PutMapping("/{studentId}/courses")
    public ResponseEntity<Void> updateStudentCourses(@PathVariable Long studentId, @RequestBody List<Long> courseIds) {
        studentService.updateStudentCourses(studentId, courseIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long studentId) {
        studentService.deleteStudent(studentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
