package com.tpro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tpro.domain.Student;
import com.tpro.dto.StudentDTO;
import com.tpro.exception.ResourceNotFoundException;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

	boolean existsByEmail(String email);

	List<Student> findByLastName(String lastName);

	//JPQL ile yazalım
	@Query("SELECT s FROM Student s WHERE s.grade=pGrade")
	List<Student> findAllEqualsGrade(@Param("pGrade") Integer grade);
	
		// native query (SQL)
		@Query(value="SELECT * from Student s WHERE s.grade=:pGrade", nativeQuery=true)
		Optional<List<Student>> findAllEqualsGradeWithSQL(@Param("pGrade")Integer grade)throws ResourceNotFoundException;

		// JPQL
		@Query("SELECT new com.tpro.dto.StudentDTO(s) FROM Student s WHERE s.id=:id" )
		Optional<StudentDTO> findStudentDTOById(@Param("id") Long id) throws ResourceNotFoundException;
	
	
	

}
