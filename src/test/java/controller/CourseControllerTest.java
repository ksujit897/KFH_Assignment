package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dto.CourseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import service.CourseService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CourseControllerTest {

    @Mock
    private CourseService courseService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        CourseController courseController = new CourseController(courseService);
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
    }

    @Test
    void addCourse_shouldReturnCreatedStatusAndAddedCourse() throws Exception {
        CourseDTO courseDTO = new CourseDTO();
        CourseDTO addedCourse = new CourseDTO();
        when(courseService.addCourse(any(CourseDTO.class))).thenReturn(addedCourse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/courses").contentType(MediaType.APPLICATION_JSON).content(asJsonString(courseDTO))).andExpect(MockMvcResultMatchers.status().isCreated()).andExpect(MockMvcResultMatchers.jsonPath("$.name").value(addedCourse.getName())).andReturn();

        verify(courseService, times(1)).addCourse(any(CourseDTO.class));
    }

    @Test
    void getCourses_shouldReturnOkStatusAndListOfCourses() throws Exception {
        List<CourseDTO> courses = Arrays.asList(new CourseDTO());
        when(courseService.getCourses()).thenReturn(courses);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(courses.get(0).getName())).andReturn();

        verify(courseService, times(1)).getCourses();
    }

    @Test
    void updateCourse_shouldReturnOkStatusAndUpdatedCourse() throws Exception {
        Long courseId = 1L;
        CourseDTO courseDTO = new CourseDTO();
        CourseDTO updatedCourse = new CourseDTO();
        when(courseService.updateCourse(eq(courseId), any(CourseDTO.class))).thenReturn(updatedCourse);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/courses/{id}", courseId).contentType(MediaType.APPLICATION_JSON).content(asJsonString(courseDTO))).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.name").value(updatedCourse.getName())).andReturn();

        verify(courseService, times(1)).updateCourse(eq(courseId), any(CourseDTO.class));
    }

    @Test
    void deleteCourse_shouldReturnNoContentStatus() throws Exception {
        Long courseId = 1L;
        doNothing().when(courseService).deleteCourse(eq(courseId));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/courses/{id}", courseId)).andExpect(MockMvcResultMatchers.status().isNoContent());
        verify(courseService, times(1)).deleteCourse(eq(courseId));
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
