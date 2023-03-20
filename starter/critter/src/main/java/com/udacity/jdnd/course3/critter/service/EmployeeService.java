package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.user.Employee;
import com.udacity.jdnd.course3.critter.entity.user.EmployeeDTO;
import com.udacity.jdnd.course3.critter.entity.user.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.entity.user.EmployeeSkill;
import com.udacity.jdnd.course3.critter.repo.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class EmployeeService {
    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private ScheduleService scheduleService;


    public List<EmployeeDTO> getAllEmployees() {
        return EmployeesToEmployeeDTOs(employeeRepo.findAll());
    }

    public EmployeeDTO SaveEmployee(EmployeeDTO employeeDTO) {
        List<EmployeeSkill> employeeSkills = new ArrayList<>(employeeDTO.getSkills());

        employeeDTO.setId(employeeRepo.save(
                        new Employee(
                                employeeDTO.getId(),
                                employeeDTO.getName(),
                                employeeSkills,
                                employeeDTO.getDaysAvailable()
                        )
                ).getId()
        );

        return employeeDTO;
    }

    public void setAvailability(Set<DayOfWeek> dayOfWeeks, long employeeId) {
        Employee employee = employeeRepo.getOne(employeeId);

        employee.setDaysAvailable(dayOfWeeks);
        employeeRepo.save(employee);
    }

    public Employee getEmployeeById(long id) {
        return employeeRepo.getOne(id);
    }

    public EmployeeDTO getEmployeeDtoById(long id) {
        Employee employee = employeeRepo.getOne(id);

        return new EmployeeDTO(
                employee.getId(),
                employee.getName(),
                new HashSet<>(employee.getSkills()),
                employee.getDaysAvailable()
        );
    }

    public List<EmployeeDTO> getAvailabileEmployee(EmployeeRequestDTO employeeRequestDTO) {

        List<Employee> employees = employeeRepo.getEmployeesByDaysAvailable(employeeRequestDTO.getDate().getDayOfWeek());
        List<Employee> avilableEmployee = new ArrayList<>();

        for (Employee e : employees) {
            if (employeeRequestDTO.getSkills().stream().allMatch(es -> e.getSkills().stream().anyMatch(er -> es == er)))
                avilableEmployee.add(e);
        }

        return EmployeesToEmployeeDTOs(avilableEmployee);
    }

    public List<EmployeeDTO> EmployeesToEmployeeDTOs(List<Employee> employees) {
        List<EmployeeDTO> employeeDTOS = new ArrayList<>();

        employees.stream().
                forEach(employee ->
                        employeeDTOS.
                                add(new EmployeeDTO(
                                        employee.getId(),
                                        employee.getName(),
                                        new HashSet<>(employee.getSkills()),
                                        employee.getDaysAvailable()))
                );
        return employeeDTOS;
    }
}
