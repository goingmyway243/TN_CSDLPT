/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import dao.ClassroomDao;
import java.util.List;
import model.Classroom;

/**
 *
 * @author vivau
 */
public class ClassroomService {

    private ClassroomDao classroomDao;

    public ClassroomService() {
        classroomDao = new ClassroomDao();
    }

    public List<Classroom> getAllClassrooms() {
        //
        return classroomDao.getAllClassrooms();
    }

    public void addClassroom(Classroom classroom) {
        classroomDao.addClassroom(classroom);
    }

    public void deleteClassroom(String malop) {
        classroomDao.deleteClassroom(malop);
    }

    public Classroom getClassroomById(String malop) {
        return classroomDao.getClassroomById(malop);
    }

    public void updateClassroom(Classroom classroom) {
        classroomDao.updateClassroom(classroom);
    }

}
