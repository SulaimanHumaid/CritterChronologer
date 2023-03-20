package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.pet.Pet;
import com.udacity.jdnd.course3.critter.entity.schedule.Schedule;
import com.udacity.jdnd.course3.critter.entity.schedule.ScheduleDTO;
import com.udacity.jdnd.course3.critter.entity.user.Employee;
import com.udacity.jdnd.course3.critter.repo.ScheduleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleService {
    @Autowired
    private ScheduleRepo scheduleRepo;
    @Autowired
    private PetService petService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CustomerService customerService;

    public ScheduleDTO saveSchedule(ScheduleDTO scheduleDTO) {
        List<Employee> employees = new ArrayList<>();
        List<Pet> pets = new ArrayList<>();
        if (scheduleDTO.getEmployeeIds() != null)
            employees = scheduleDTO.getEmployeeIds().stream().map(s -> employeeService.getEmployeeById(s)).collect(Collectors.toList());
        if (scheduleDTO.getPetIds() != null)
            pets = scheduleDTO.getPetIds().stream().map(s -> petService.getPetById(s)).collect(Collectors.toList());
        scheduleDTO.setId(scheduleRepo.save(new Schedule(scheduleDTO.getId(), employees, pets, scheduleDTO.getDate(), scheduleDTO.getActivities())).getId());
        return scheduleDTO;
    }

    public List<ScheduleDTO> getScheduleByPetId(long id) {
        List<Schedule> schedules = scheduleRepo.getSchedulesByPets(petService.getPetById(id));
        return ScheduleToScheduleDto(schedules);
    }

    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = scheduleRepo.findAll();
        return ScheduleToScheduleDto(schedules);
    }

    public List<ScheduleDTO> getScheduleForEmployee(long employeeId) {
        List<Schedule> schedules = scheduleRepo.getSchedulesByEmployees(employeeService.getEmployeeById(employeeId));
        return ScheduleToScheduleDto(schedules);
    }

    public List<ScheduleDTO> getScheduleForCustomer(Long customerId) {
        List<ScheduleDTO> schedules = new ArrayList<>();
        customerService.getCustomerById(customerId).getPets().stream().forEach(pet -> schedules.addAll(getScheduleByPetId(pet.getId())));
        return schedules;
    }

    private List<ScheduleDTO> ScheduleToScheduleDto(List<Schedule> schedules) {
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for (Schedule schedule : schedules) {
            List<Long> petsId = new ArrayList<>();
            List<Long> employeesId = new ArrayList<>();
            if (schedule.getPets() != null && schedule.getEmployees() != null) {
                employeesId = schedule.getEmployees().stream().map(s -> s.getId()).collect(Collectors.toList());
                petsId = schedule.getPets().stream().map(s -> s.getId()).collect(Collectors.toList());
            }
            scheduleDTOS.add(new ScheduleDTO(schedule.getId(), employeesId, petsId, schedule.getDate(), schedule.getActivities()));
        }
        return scheduleDTOS;
    }
}
