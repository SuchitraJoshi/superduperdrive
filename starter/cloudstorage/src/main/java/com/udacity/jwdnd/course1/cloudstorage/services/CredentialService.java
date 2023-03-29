package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.CredentialForm;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {

    private CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;


    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }
    @PostConstruct
    public void postConstruct() {
        System.out.println("Creating CredentialService bean");
    }

    public CredentialForm addNewCredential(CredentialForm credentialForm) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credentialForm.getCredentialPassword(), encodedKey);
        credentialForm.setCredentialId(credentialMapper.insertCredential(new Credential(credentialForm.getCredentialId(),credentialForm.getUserId(),
                credentialForm.getCredentialUrl(),credentialForm.getCredentialUsername(),encodedKey,encryptedPassword)));
        return credentialForm;
    }
    public void updateSelectedCredential(CredentialForm credentialForm) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credentialForm.getCredentialPassword(), encodedKey);
        int rowsUpdated = credentialMapper.updateCredential(new Credential(credentialForm.getCredentialId(),credentialForm.getUserId(),
                credentialForm.getCredentialUrl(),credentialForm.getCredentialUsername(),encodedKey,encryptedPassword));
    }

    public CredentialForm getSelectedCredential(int userId, int credentialId){
        Credential credential= credentialMapper.getCredentialById(userId,credentialId);
        String decryptedPassword = encryptionService.decryptValue(credential.getEncryptedPassword(), credential.getEncryptionKey());
        return new CredentialForm(credential.getCredentialId(),credential.getUserId(),
                credential.getCredentialUrl(),credential.getCredentialUsername(),decryptedPassword);
    }
    public List<Credential> getMyCredentials(int userId) {
        return credentialMapper.getMyCredentials(userId);
    }

    public int deleteCredential(int userId, int credentialId){
        return credentialMapper.deleteCredential(userId, credentialId);
    }
}
