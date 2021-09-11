/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import dao.RegisterDao;
import java.util.List;
import model.Register;

/**
 *
 * @author vivau
 */
public class RegisterService {
    
    private RegisterDao registerDao;

    public RegisterService() {
        registerDao = new RegisterDao();
    }
    
    public List<Register> getAllRegisters() {
        //
        return registerDao.getAllRegisters();
    }
    
    public void addRegister(Register register) {
        registerDao.addRegister(register);
    }
    
    public void deleteRegister(String mamh, String malop, int lan) {
        registerDao.deleteRegister(mamh, malop, lan);
    }
    
    public Register getRegisterById(String mamh, String malop, int lan) {
        return registerDao.getRegisterById(mamh, malop, lan);
    }
    
    public void updateRegister(Register register) {
        registerDao.updateRegister(register);
    }
}
