package com.iDoctor.iDoctor.security;

import com.iDoctor.iDoctor.EnvironmentAccess;
import com.iDoctor.iDoctor.model.Doctor;
import com.iDoctor.iDoctor.repository.DoctorRepository;
import com.iDoctor.iDoctor.services.ChatGPTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private EnvironmentAccess environmentAccess;

    @Autowired
    ChatGPTService chatGPTService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Doctor doctor = doctorRepository.findByEmail(email);
        if (doctor == null) {
            throw new UsernameNotFoundException("Doctor not found");
        }
        System.out.println(doctor.getDoctorId());
        System.out.println(doctor.getApiKey());

        environmentAccess.setDoctorId(doctor.getDoctorId());
        environmentAccess.setApiKey(doctor.getApiKey());

        chatGPTService.updateApiKey(doctor.getApiKey());

        return   User.builder()
                .username(doctor.getEmail())
                .password(doctor.getPassword()) // Here, the password is taken as is (plain text)
                .authorities("USER") // specify the user's role here
                .build();
    }
}
