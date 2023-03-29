package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CredentialController {
    @Autowired
    private CredentialService credentialService;
    @Autowired
    private UserService userService;
    private String credMessage="";

//    public CredentialController(CredentialService credentialService, UserService userService) {
//        this.credentialService = credentialService;
//        this.userService = userService;
//    }

    @PostMapping("/savecredential")
    public String saveCredential(Authentication authentication, CredentialForm credentialForm, RedirectAttributes redirectAttributes) {
        try {

            Integer userId = userService.getUser(authentication.getName()).getUserId();
            credentialForm.setUserId(userId);

            if (credentialForm.getCredentialId() == null) {
                credentialService.addNewCredential(credentialForm);
                credMessage = "Credential added Successfully.";
            } else {
                credentialService.updateSelectedCredential(credentialForm);
                credMessage = "Credential updated Successfully.";
            }
        }catch (Exception ex){
            ex.printStackTrace();
            credMessage = "Save was not successful." + ex.getMessage();
        }
        //model.addAttribute("credentials", credentialService.getMyCredentials(userId));
        redirectAttributes.addAttribute("credMessage", credMessage);

        return "redirect:/home";
    }

    @GetMapping("/deletecredential/{credentialId}")
    public String deleteCredential(@PathVariable("credentialId") Integer credentialId, Authentication authentication, RedirectAttributes redirectAttributes) {
        Integer userId =0;
        try {
            userId = userService.getUser(authentication.getName()).getUserId();
            credentialService.deleteCredential(userId, credentialId);
            credMessage="Credential deleted Successfully.";
        }catch (Exception ex){
            ex.printStackTrace();
            credMessage="Delete was not successful." + ex.getMessage();
        }
        redirectAttributes.addAttribute("credMessage", credMessage);
            //model.addAttribute("credentials", credentialService.getMyCredentials(userId));
        return "redirect:/home";
    }

    @GetMapping("/getallcredentials")
    public String getAllCredentials(Authentication authentication, Model model) {
        int userId = userService.getUser(authentication.getName()).getUserId();
        model.addAttribute("credentials", credentialService.getMyCredentials(userId));
        return "redirect:/home";
    }
}
