select max(employee_id), avg(position_id), min(store_id)
 from cp.`employee.json`
 where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and full_name in(select full_name from cp.`employee.json` where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`)))))))))
 and hire_date in (select hire_date from cp.`employee.json` where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and full_name in(select full_name from cp.`employee.json` where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))))
 and salary in( select salary from cp.`employee.json` where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and full_name in(select full_name from cp.`employee.json` where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`)))))))))
 and hire_date in (select hire_date from cp.`employee.json` where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and full_name in(select full_name from cp.`employee.json` where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`)))))))))))
 and education_level in (select education_level from cp.`employee.json`where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and full_name in(select full_name from cp.`employee.json` where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`)))))))))
 and hire_date in (select hire_date from cp.`employee.json` where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and full_name in(select full_name from cp.`employee.json` where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))))
 and salary in( select salary from cp.`employee.json` where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and full_name in(select full_name from cp.`employee.json` where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`)))))))))
 and hire_date in (select hire_date from cp.`employee.json` where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and full_name in(select full_name from cp.`employee.json` where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))))))
 and birth_date in (select birth_date from cp.`employee.json` where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and full_name in(select full_name from cp.`employee.json` where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`)))))))))
 and hire_date in (select hire_date from cp.`employee.json` where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and full_name in(select full_name from cp.`employee.json` where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))))
 and salary in( select salary from cp.`employee.json` where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and full_name in(select full_name from cp.`employee.json` where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`)))))))))
 and hire_date in (select hire_date from cp.`employee.json` where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and full_name in(select full_name from cp.`employee.json` where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`)))))))))))
 and education_level in (select education_level from cp.`employee.json`where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and full_name in(select full_name from cp.`employee.json` where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`)))))))))
 and hire_date in (select hire_date from cp.`employee.json` where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and full_name in(select full_name from cp.`employee.json` where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))))
 and salary in( select salary from cp.`employee.json` where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and full_name in(select full_name from cp.`employee.json` where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`)))))))))
 and hire_date in (select hire_date from cp.`employee.json` where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and full_name in(select full_name from cp.`employee.json` where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`)))))))))))))
 and position_title in (select position_title from cp.`employee.json` where
 employee_id in(select employee_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 and position_id in(select position_id from cp.`employee.json` where
 full_name in(select full_name from cp.`employee.json` where
 first_name in(select first_name from cp.`employee.json` where
 last_name in(select last_name from cp.`employee.json` where
 position_id in(select position_id from cp.`employee.json` where
 position_title in(select position_title from cp.`employee.json` where
 store_id in(select store_id from cp.`employee.json` where
 department_id in(select department_id from cp.`employee.json`))))))))
 )
