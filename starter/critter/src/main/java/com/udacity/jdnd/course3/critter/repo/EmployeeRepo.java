package com.udacity.jdnd.course3.critter.repo;

import com.udacity.jdnd.course3.critter.entity.user.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {
    public List<Employee> getEmployeesByDaysAvailable(DayOfWeek dayOfWeek);
}
