package com.udacity.jdnd.course3.critter.repo;

import com.udacity.jdnd.course3.critter.entity.pet.Pet;
import com.udacity.jdnd.course3.critter.entity.schedule.Schedule;
import com.udacity.jdnd.course3.critter.entity.user.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ScheduleRepo extends JpaRepository<Schedule, Long> {

    public List<Schedule> getSchedulesByPets(Pet pets);

    public List<Schedule> getSchedulesByEmployees(Employee employees);
}
