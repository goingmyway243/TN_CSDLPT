/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import dao.TranscriptDao;
import java.util.List;
import model.Transcript;

/**
 *
 * @author vivau
 */
public class TranscriptService {
    
    private TranscriptDao transciptDao;

    public TranscriptService() {
        transciptDao = new TranscriptDao();
    }
    
    public List<Transcript> getAllTranscripts() {
        //
        return transciptDao.getAllTranscripts();
    }
    
    public void addTranscript(Transcript transcipt) {
        transciptDao.addTranscript(transcipt);
    }
    
    public void deleteTranscript(String masv, String mamh, int lan) {
        transciptDao.deleteTranscript(masv, mamh, lan);
    }
    
    public Transcript getTranscriptById(String masv, String mamh, int lan) {
        return transciptDao.getTranscriptById(masv, mamh, lan);
    }
    
    public void updateTranscript(Transcript transcipt) {
        transciptDao.updateTranscript(transcipt);
    }
    
}
