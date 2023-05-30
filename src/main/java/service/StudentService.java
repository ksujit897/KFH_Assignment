package service;

import dto.CourseDTO;
import dto.StudentDTO;
import entity.Course;
import entity.Student;
import entity.StudentCourse;
import org.springframework.stereotype.Service;
import repo.CourseRepository;
import repo.StudentCourseRepository;
import repo.StudentRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final CourseService courseService;
    private final StudentCourseRepository studentCourseRepository;

    private final CourseRepository courseRepository;

    StudentService(final StudentRepository studentRepository,
                   final CourseService courseService,
                   final StudentCourseRepository studentCourseRepository,
                   final CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseService = courseService;
        this.studentCourseRepository = studentCourseRepository;

        this.courseRepository = courseRepository;
    }

    public StudentDTO registerStudent(StudentDTO studentDTO) {
        Student student = new Student();
        student.setFullNameEnglish(studentDTO.getFullNameEnglish());
        student.setFullNameArabic(studentDTO.getFullNameArabic());
        student.setEmail(studentDTO.getEmail());
        student.setTelephoneNumber(studentDTO.getTelephoneNumber());
        student.setAddress(studentDTO.getAddress());
        Student savedStudent = studentRepository.save(student);
        return convertToDTO(savedStudent);
    }

    public List<CourseDTO> getAllCourses() {
        return courseService.getAllCourses();
    }

    public void allocateCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + studentId));
        Course course = courseService.getCourseById(courseId);
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setStudent(student);
        studentCourse.setCourse(course);
        studentCourseRepository.save(studentCourse);
    }

    public List<Course> getStudentCourses(Long studentId) {
        List<StudentCourse> studentCourses = studentCourseRepository.findByStudentId(studentId);
        List<Course> courses = new ArrayList<>();
        for (StudentCourse studentCourse : studentCourses) {
            courses.add(studentCourse.getCourse());
        }
        return courses;
    }

    public void updateStudentCourses(Long studentId, List<Long> courseIds) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + studentId));
        studentCourseRepository.deleteByStudent(student);

        for (Long courseId : courseIds) {
            Course course = courseService.getCourseById(courseId);
            StudentCourse studentCourse = new StudentCourse();
            studentCourse.setStudent(student);
            studentCourse.setCourse(course);
            studentCourseRepository.save(studentCourse);
        }
    }

    public void deleteStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + studentId));
        studentCourseRepository.deleteByStudent(student);
        studentRepository.deleteById(studentId);
    }

    private StudentDTO convertToDTO(Student student) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(student.getId());
        studentDTO.setFullNameEnglish(student.getFullNameEnglish());
        studentDTO.setFullNameArabic(student.getFullNameArabic());
        studentDTO.setEmail(student.getEmail());
        studentDTO.setTelephoneNumber(student.getTelephoneNumber());
        studentDTO.setAddress(student.getAddress());
        return studentDTO;
    }
}

