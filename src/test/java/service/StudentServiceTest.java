package service;

import dto.CourseDTO;
import dto.StudentDTO;
import entity.Course;
import entity.Student;
import entity.StudentCourse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repo.CourseRepository;
import repo.StudentCourseRepository;
import repo.StudentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class StudentServiceTest {

    private StudentService studentService;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseService courseService;

    @Mock
    private StudentCourseRepository studentCourseRepository;

    @Mock
    private CourseRepository courseRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        studentService = new StudentService(studentRepository, courseService, studentCourseRepository);
    }

    @Test
    public void testRegisterStudent() {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setFullNameEnglish("Sujit Kumar");
        studentDTO.setEmail("sujit@example.com");

        Student student = new Student();
        student.setFullNameEnglish("Sujit Kumar");
        student.setEmail("sujit.doe@example.com");

        when(studentRepository.save(any(Student.class))).thenReturn(student);
        StudentDTO result = studentService.registerStudent(studentDTO);
        assertNotNull(result);
        assertEquals("Sujit Kumar", result.getFullNameEnglish());
        assertEquals("sujit.doe@example.com", result.getEmail());
    }

    @Test
    public void testGetAllCourses() {
        List<CourseDTO> expectedCourses = new ArrayList<>();
        expectedCourses.add(createCourseDTO(1L, "Course 1"));
        expectedCourses.add(createCourseDTO(2L, "Course 2"));
        when(courseService.getCourses()).thenReturn(expectedCourses);
        List<CourseDTO> result = studentService.getAllCourses();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Course 1", result.get(0).getName());
        assertEquals(2L, result.get(1).getId());
        assertEquals("Course 2", result.get(1).getName());
    }

    private CourseDTO createCourseDTO(Long id, String name) {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(id);
        courseDTO.setName(name);
        return courseDTO;
    }


    @Test
    public void testAllocateCourse() {
        Long studentId = 1L;
        Long courseId = 2L;

        Student student = new Student();
        student.setId(studentId);

        Course course = new Course();
        course.setId(courseId);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseService.getCourseById(courseId)).thenReturn(course);
        assertDoesNotThrow(() -> studentService.allocateCourse(studentId, courseId));
        verify(studentCourseRepository, times(1)).save(any(StudentCourse.class));
    }

    @Test
    public void testUpdateStudentCourses() {
        Long studentId = 1L;
        List<Long> courseIds = new ArrayList<>();
        courseIds.add(1L);
        courseIds.add(2L);

        Student student = new Student();
        student.setId(studentId);

        Course course1 = new Course();
        course1.setId(1L);

        Course course2 = new Course();
        course2.setId(2L);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseService.getCourseById(1L)).thenReturn(course1);
        when(courseService.getCourseById(2L)).thenReturn(course2);
        assertDoesNotThrow(() -> studentService.updateStudentCourses(studentId, courseIds));

        verify(studentCourseRepository, times(1)).deleteByStudent(student);
        verify(studentCourseRepository, times(2)).save(any(StudentCourse.class));
    }

    @Test
    public void testDeleteStudent() {
        Long studentId = 1L;

        Student student = new Student();
        student.setId(studentId);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        assertDoesNotThrow(() -> studentService.deleteStudent(studentId));

        verify(studentCourseRepository, times(1)).deleteByStudent(student);
        verify(studentRepository, times(1)).deleteById(studentId);
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


