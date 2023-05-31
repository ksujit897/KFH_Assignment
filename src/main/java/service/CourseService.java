package service;

import dto.CourseDTO;
import entity.Course;
import exception.ExceptionHandler;
import org.springframework.stereotype.Service;
import repo.CourseRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    private CourseService(final CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public CourseDTO addCourse(CourseDTO courseDTO) {
        Course course = new Course();
        course.setName(courseDTO.getName());
        Course savedCourse = courseRepository.save(course);
        return convertToDTO(savedCourse);
    }

    public List<CourseDTO> getCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> ExceptionHandler.handleEntityNotFoundException("Course", id));
    }

    public CourseDTO updateCourse(Long id, CourseDTO courseDTO) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> ExceptionHandler.handleEntityNotFoundException("Course", id));
        course.setName(courseDTO.getName());
        Course updatedCourse = courseRepository.save(course);
        return convertToDTO(updatedCourse);
    }

    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> ExceptionHandler.handleEntityNotFoundException("Course", id));
        courseRepository.delete(course);
    }

    private CourseDTO convertToDTO(Course course) {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(course.getId());
        courseDTO.setName(course.getName());
        return courseDTO;
    }
}

