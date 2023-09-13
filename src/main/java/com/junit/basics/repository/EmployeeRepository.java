package com.junit.basics.repository;

import com.junit.basics.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    public Optional<Employee> findByEmail(String email);

    //defined custom query using JPQL with Index params
    @Query("select e from Employee e where e.firstName=?1 and e.lastName=?2")
    Employee findByJPQL(String firstName, String lastName);

    //defined custom query using JPQL with Named params
    @Query("select e from Employee e where e.firstName=:firstName and e.lastName=:lastName")
    Employee findByJPQLNamedParams(@Param("firstName") String firstName, @Param("lastName") String lastName);

    //defined custom query using Native SQL with index params
	//in nativeQuery, we directly use the database table name and column not the Java entity
    @Query(value = "select * from employees e where e.first_name= ?1 and e.last_name= ?2", nativeQuery = true)
    Employee findByNativeSQL(String firstName, String lastName);

    //defined custom query using Native SQL with named params
    @Query(value="select * from employees e where e.first_name=:firstName and e.last_name=:lastName",
            nativeQuery = true)
    Employee findByNativeSqlNamed(String firstName, String lastName);
}
