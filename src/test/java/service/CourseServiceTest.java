package service;

import dto.CourseDTO;
import entity.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repo.CourseRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addCourse_shouldSaveCourseAndReturnDTO() {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setName("Math");
        Course savedCourse = new Course();
        savedCourse.setId(1L);
        savedCourse.setName("Math");
        when(courseRepository.save(any(Course.class))).thenReturn(savedCourse);
        CourseDTO result = courseService.addCourse(courseDTO);
        assertNotNull(result);
        assertEquals(savedCourse.getId(), result.getId());
        assertEquals(savedCourse.getName(), result.getName());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void getCourses_shouldReturnListOfCoursesDTO() {
        List<Course> courses = new ArrayList<>();
        Course course1 = new Course();
        course1.setId(1L);
        course1.setName("Math");
        Course course2 = new Course();
        course2.setId(2L);
        course2.setName("Science");
        courses.add(course1);
        courses.add(course2);
        when(courseRepository.findAll()).thenReturn(courses);
        List<CourseDTO> result = courseService.getCourses();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(course1.getId(), result.get(0).getId());
        assertEquals(course1.getName(), result.get(0).getName());
        assertEquals(course2.getId(), result.get(1).getId());
        assertEquals(course2.getName(), result.get(1).getName());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void getCourseById_existingCourseId_shouldReturnCourse() {
        Long courseId = 1L;
        Course course = new Course();
        course.setId(courseId);
        course.setName("Math");
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        Course result = courseService.getCourseById(courseId);
        assertNotNull(result);
        assertEquals(courseId, result.getId());
        assertEquals(course.getName(), result.getName());
        verify(courseRepository, times(1)).findById(courseId);
    }

    @Test
    void getCourseById_nonExistingCourseId_shouldThrowException() {
        Long courseId = 1L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> courseService.getCourseById(courseId));
        verify(courseRepository, times(1)).findById(courseId);
    }

    @Test
    void updateCourse_existingCourseId_shouldUpdateAndReturnDTO() {
        Long courseId = 1L;
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setName("Physics");

        Course existingCourse = new Course();
        existingCourse.setId(courseId);
        existingCourse.setName("Math");

        Course updatedCourse = new Course();
        updatedCourse.setId(courseId);
        updatedCourse.setName("Physics");

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(existingCourse));
        when(courseRepository.save(any(Course.class))).thenReturn(updatedCourse);

        CourseDTO result = courseService.updateCourse(courseId, courseDTO);

        assertNotNull(result);
        assertEquals(courseId, result.getId());
        assertEquals(courseDTO.getName(), result.getName());
        verify(courseRepository, times(1)).findById(courseId);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void updateCourse_nonExistingCourseId_shouldThrowException() {
        Long courseId = 1L;
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setName("Physics");

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> courseService.updateCourse(courseId, courseDTO));
        verify(courseRepository, times(1)).findById(courseId);
    }

    @Test
    void deleteCourse_existingCourseId_shouldDeleteCourse() {
        Long courseId = 1L;
        Course existingCourse = new Course();
        existingCourse.setId(courseId);
        existingCourse.setName("Math");

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(existingCourse));

        courseService.deleteCourse(courseId);

        verify(courseRepository, times(1)).findById(courseId);
        verify(courseRepository, times(1)).delete(existingCourse);
    }

    @Test
    void deleteCourse_nonExistingCourseId_shouldThrowException() {
        Long courseId = 1L;

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> courseService.deleteCourse(courseId));
        verify(courseRepository, times(1)).findById(courseId);
    }
}
