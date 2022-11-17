package com.tpro.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.tpro.domain.Student;
import com.tpro.dto.StudentDTO;
import com.tpro.service.StudentService;


@RestController
@RequestMapping("/students")
public class StudentController {
	
	Logger logger = LoggerFactory.getLogger(StudentController.class);
	
	
	@Autowired
	private StudentService studentService;
	/*
	@GetMapping("/welcome") // localhost:8080/students/welcome
	public String welcome() {
		
		return "Welcome to Student Controller";
	}*/
	
	@GetMapping("/welcome") // localhost:8080/students/welcome
	public String welcome(HttpServletRequest request) {
		
		logger.warn("---------------Welcome{}", request.getServletPath());
		return "Welcome to Student Controller";
	}
	
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<Student>> getAll(){
		
		List<Student> studentsList =studentService.getAll();
	
		return ResponseEntity.ok(studentsList);
	}
	
	@PostMapping
	@PreAuthorize("hasRole('STUDENT')")
	public ResponseEntity<Map<String, String>> createStudent(@Valid @RequestBody Student student){
		
		studentService.createStudent(student);
		Map<String, String> map= new HashMap<>();
		map.put("message", "Student is created succesfully");
		map.put("statüs", "True");
		
		return new ResponseEntity<>(map,HttpStatus.CREATED);
	}
	
	// request param
	@GetMapping("/query")
	@PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
	public ResponseEntity<Student> getStudent(@RequestParam ("id") Long id){
		
		Student student = studentService.findStudent(id);
		return ResponseEntity.ok(student);
	}
	
	// pathvariable
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
	public ResponseEntity<Student> getStudentWithPath(@PathVariable ("id") Long id){
		
		Student student = studentService.findStudent(id);
		return ResponseEntity.ok(student);
	}
	
	// delete student
	// Delete Student
		@DeleteMapping("/{id}")
		public ResponseEntity<Map<String,String>> deleteStudent(@PathVariable("id") Long id) {
			studentService.deleteStudent(id);
			Map<String,String> map = new HashMap<>();
			map.put("message", "Student is deleted successfuly");
			map.put("status", "true");
			return new ResponseEntity<>(map,HttpStatus.OK);
			
		}
		
		//update student, dto kullanılacak
		@PutMapping("{id}")
		public ResponseEntity<Map<String, String>> updateStudent (@PathVariable Long id, @RequestBody StudentDTO studentDTO){
			studentService.updateStudent(id, studentDTO);
			Map<String,String> map = new HashMap<>();
			map.put("message", "Student is updated successfuly");
			map.put("status", "true");
			return new ResponseEntity<>(map,HttpStatus.OK);
		}
		
		//pageable
		@GetMapping("/page")
		public ResponseEntity<Page<Student>> getAllWithPage(@RequestParam("page") int page,
															@RequestParam("size") int size,
															@RequestParam("sort") String prop,
															@RequestParam("direction") Direction direction){
			
			Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
			Page<Student> studentPage= studentService.getAllWithPage1(pageable);
			
			return ResponseEntity.ok(studentPage);
			
		}
		
		// Get By LastName
		@GetMapping("/querylastname")  // students/querylastname...
		public ResponseEntity<List<Student>> getStudentByLastName(@RequestParam("lastName") String lastName){
			List<Student> list = studentService.findStudent(lastName);
			return ResponseEntity.ok(list);
			
		}
		
		// Get ALL students By grade ( JPQL )
				@GetMapping("/grade/{grade}")
				public ResponseEntity<List<Student>> getStudentsEqualsGrade(@PathVariable("grade") Integer grade){
					List<Student> list = studentService.findAllEqualsGrade(grade);
					return ResponseEntity.ok(list);
					
				}
				
				
				// DB den direk DTO olarak datamı alsam ?
				@GetMapping("/query/dto")
				public ResponseEntity<StudentDTO> getStudentDTO(@RequestParam("id") Long id){
					StudentDTO studentDTO = studentService.findStudentDTOById(id);
					return ResponseEntity.ok(studentDTO);
				}
				
				

}
