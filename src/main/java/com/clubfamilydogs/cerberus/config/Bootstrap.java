package com.clubfamilydogs.cerberus.config;

import com.clubfamilydogs.cerberus.persistance.OwnerRepository;
import com.clubfamilydogs.cerberus.persistance.models.Owner;
import com.clubfamilydogs.cerberus.persistance.models.Pet;
import com.clubfamilydogs.cerberus.persistance.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class Bootstrap implements CommandLineRunner {

    private final PetRepository petRepository;
    private final com.clubfamilydogs.cerberus.persistance.OwnerRepository ownerRepository;

    @Override
    public void run(String... args){
        if (petRepository.count() == 0){
            loadObjects();
        }
    }

    private void loadObjects() {

        Owner owner = Owner.builder()
                .name("Camilo")
                .lastName("Cifuentes")
                .email("ccifuentes@gmail.com")
                .build();
        owner.addPet(Pet.builder().name("Apolo").lastName("Cifuentes").build());

        Owner owner1 = Owner.builder()
                .name("Geral")
                .lastName("Nuñez")
                .email("gnunez@gmail.com")
                .build();
        owner1.addPet(Pet.builder().name("Tommy").lastName("Nuñez").build());



        ownerRepository.save(owner);
        ownerRepository.save(owner1);
    }
}
