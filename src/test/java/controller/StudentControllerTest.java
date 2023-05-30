package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dto.CourseDTO;
import dto.StudentDTO;
import entity.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import service.StudentService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class StudentControllerTest {

    @Mock
    private StudentService studentService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        StudentController studentController = new StudentController(studentService);
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    void registerStudent_shouldReturnCreatedStatusAndRegisteredStudent() throws Exception {
        StudentDTO studentDTO = new StudentDTO();
        StudentDTO registeredStudent = new StudentDTO();
        when(studentService.registerStudent(any(StudentDTO.class))).thenReturn(registeredStudent);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/students/register").contentType(MediaType.APPLICATION_JSON).content(asJsonString(studentDTO))).andExpect(MockMvcResultMatchers.status().isCreated()).andExpect(MockMvcResultMatchers.jsonPath("$.id").value(registeredStudent.getId())).andReturn();

        verify(studentService, times(1)).registerStudent(any(StudentDTO.class));
    }

    @Test
    void getAllCourses_shouldReturnOkStatusAndListOfCourses() throws Exception {
        List<CourseDTO> courses = List.of(new CourseDTO());
        when(studentService.getAllCourses()).thenReturn(courses);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/students/courses")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(courses.get(0).getName())).andReturn();

        verify(studentService, times(1)).getAllCourses();
    }

    @Test
    void allocateCourse_shouldReturnCreatedStatus() throws Exception {
        Long studentId = 1L;
        Long courseId = 1L;
        doNothing().when(studentService).allocateCourse(eq(studentId), eq(courseId));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/students/{studentId}/courses/{courseId}", studentId, courseId)).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

        verify(studentService, times(1)).allocateCourse(eq(studentId), eq(courseId));
    }

    @Test
    void getStudentCourses_shouldReturnOkStatusAndListOfCourses() throws Exception {
        Long studentId = 1L;
        List<Course> courses = List.of(new Course(/* initialize course */));
        when(studentService.getStudentCourses(eq(studentId))).thenReturn(courses);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/students/{studentId}/courses", studentId)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(courses.get(0).getName())).andReturn();

        verify(studentService, times(1)).getStudentCourses(eq(studentId));
    }

    @Test
    void updateStudentCourses_shouldReturnOkStatus() throws Exception {
        Long studentId = 1L;
        List<Long> courseIds = Arrays.asList(1L, 2L);
        doNothing().when(studentService).updateStudentCourses(eq(studentId), eq(courseIds));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/students/{studentId}/courses", studentId).contentType(MediaType.APPLICATION_JSON).content(asJsonString(courseIds))).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        verify(studentService, times(1)).updateStudentCourses(eq(studentId), eq(courseIds));
    }

    @Test
    void deleteStudent_shouldReturnNoContentStatus() throws Exception {
        Long studentId = 1L;
        doNothing().when(studentService).deleteStudent(eq(studentId));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/students/{studentId}", studentId)).andExpect(MockMvcResultMatchers.status().isNoContent()).andReturn();

        verify(studentService, times(1)).deleteStudent(eq(studentId));
    }

    private static String asJsonString(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}



