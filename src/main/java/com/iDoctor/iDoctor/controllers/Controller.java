package com.iDoctor.iDoctor.controllers;


import com.iDoctor.iDoctor.EnvironmentAccess;
import com.iDoctor.iDoctor.model.Doctor;
import com.iDoctor.iDoctor.model.Patient;
import com.iDoctor.iDoctor.repository.DoctorRepository;
import com.iDoctor.iDoctor.security.CustomUserDetailsService;
import com.iDoctor.iDoctor.services.ChatGPTService;
import com.iDoctor.iDoctor.services.DoctorServiceImpl;
import com.iDoctor.iDoctor.services.PatientServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Controller
public class Controller {

    /*Add an ininbinder ... to convert trim input strings.
      Remove leading and trailing whitespaces.
      Resolve issue for our validation
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        binder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    DoctorServiceImpl doctorService;
    PatientServiceImpl patientService;
    @Autowired
    public Controller(DoctorServiceImpl doctorService, PatientServiceImpl patientService) {
        this.doctorService = doctorService;
        this.patientService = patientService;
    }
    @GetMapping("/")
    public String homePage(){
        return "index";
    }

    @GetMapping("/index")
    public String indexPage(){return "index";}
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/signup")
    public String showRegistrationForm(Model model) {
        model.addAttribute("doctor", new Doctor());
        return "signup";
    }

    @PostMapping("/signup")
    public String registerDoctor(@Valid @ModelAttribute("doctor") Doctor doctor, BindingResult result, Model model) {
        if (!doctor.getPassword().equals(doctor.getPasswordConfirm())) {
            result.rejectValue("passwordConfirm", "error.doctor", "Ο κωδικός δεν ταιριάζει");
            return "signup"; // Return back to the registration form view
        }
        if (result.hasErrors()) {
            return "signup";
        }
        try {
            doctorService.registerDoctor(doctor);

            return "redirect:/login";
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "signup";
        }
    }



    @GetMapping("/patientRegistration")
    public String newPatient(Model model){
        model.addAttribute("patient", new Patient());
        return "patientRegistration";
    }


    @Autowired
    private EnvironmentAccess environmentAccess;


    @PostMapping("/patientRegistration")
    public String registerPatient(@Valid Patient patient, BindingResult result,Model model) {

        if (result.hasErrors()) {
            return "patientRegistration";
        }
        try {

            patientService.registerPatient(patient, environmentAccess.getDoctorId());
            return "redirect:/dashboard";
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "patientRegistration";
        }
    }



    @GetMapping("/dashboard")
    public String dashboard(Model model){
        model.addAttribute("patient",new Patient());
        model.addAttribute("doctorId",environmentAccess.getDoctorId());
        return "dashboard";
    }

    @PostMapping("/dashboard")
    public String dashboardFilled(@ModelAttribute("patient") Patient patient, Model model){
        System.out.println(patient.getSocialSecurityNumber());
        try{
            Patient pat = patientService.getPatientInfo(patient.getSocialSecurityNumber());
            model.addAttribute("patient", pat);
            System.out.println(pat.getFullName());
            return "dashboard";
        }catch (IllegalStateException e){
            model.addAttribute("error", e.getMessage());
            return "dashboard";
        }
    }

    @PostMapping("/dashboard/update")
    public String updatePatient( @Valid Patient patient, BindingResult result, Model model){
        System.out.println(patient.getId());

        patientService.updatePatient(patient,environmentAccess.getDoctorId());
        model.addAttribute("patient", patient);
        return "dashboard";

    }
    @GetMapping("/patientList")
    public String showPatientList(Model model){

        //get the patients from db
        List<Patient> patients = patientService.findPatientsByDoctorId(environmentAccess.getDoctorId());
        for(Patient patient : patients){
            System.out.println(patient.toString());
        }
        //add to the spring model
        model.addAttribute("patients", patients);
        return "patientList";}

    @PostMapping("/patientList/delete/{patientId}")
    public String deletePatient(@PathVariable Long patientId, Model model){
        patientService.deletePatient(patientId);
        return "redirect:/patientList";
    }
    @Autowired
    ChatGPTService chatGPTService;
    @PostMapping("/send-message")
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, String> payload) throws Exception {
        String message = payload.get("message");

        System.out.println("Received message: " + message);
        String messageResponse= chatGPTService.sendQuery(message);
        System.out.println(messageResponse);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("receivedMessage", message);
        response.put("reply", messageResponse);
        // Respond back to the client
        return ResponseEntity.ok(response);

    }


}
