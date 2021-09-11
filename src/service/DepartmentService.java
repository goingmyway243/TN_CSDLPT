/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import dao.DepartmentDao;
import java.util.List;
import model.Department;

/**
 *
 * @author vivau
 */
public class DepartmentService {
    
    private DepartmentDao departmentDao;

    public DepartmentService() {
        departmentDao = new DepartmentDao();
    }
    
    public List<Department> getAllDepartments() {
        //
        return departmentDao.getAllDepartments();
    }
    
    public void addDepartment(Department department) {
        departmentDao.addDepartment(department);
    }
    
    public void deleteDepartment(String makh) {
        departmentDao.deleteDepartment(makh);
    }
    
    public Department getDepartmentById(String makh) {
        return departmentDao.getDepartmentById(makh);
    }
    
    public void updateDepartment(Department department) {
        departmentDao.updateDepartment(department);
    }
    
}
