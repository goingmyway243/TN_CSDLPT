/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import dao.BranchDao;
import java.util.List;
import model.Branch;

/**
 *
 * @author vivau
 */
public class BranchService {
    
    private BranchDao branchDao;

    public BranchService() {
        branchDao = new BranchDao();
    }
    
    public List<Branch> getAllBranchs() {
        //
        return branchDao.getAllBranchs();
    }
    
    public void addBranch(Branch branch) {
        //
        branchDao.addBranch(branch);
    }
    
    public void deleteBranch(String macs) {
        //
        branchDao.deleteBranch(macs);
    }
    
    public Branch getBranchById(String macs) {
        return branchDao.getBranchById(macs);
    }
    
    public void updateBranch(Branch branch) {
        branchDao.updateBranch(branch);
    }
}
