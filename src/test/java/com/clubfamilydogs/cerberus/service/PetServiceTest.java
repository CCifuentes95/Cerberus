package com.clubfamilydogs.cerberus.service;

import com.clubfamilydogs.cerberus.dto.PetDto;
import com.clubfamilydogs.cerberus.mapper.PetMapper;
import com.clubfamilydogs.cerberus.persistance.PetRepository;
import com.clubfamilydogs.cerberus.persistance.models.Pet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    PetRepository repository;

    @InjectMocks
    PetService service;

    @Captor
    ArgumentCaptor<Pet> petCaptor;

    @BeforeEach
    void setUp() {
        service = new PetService(Mappers.getMapper(PetMapper.class), repository);
    }

    @Test
    void findAll() {
        //Given
        List<Pet> pets = List.of(
                Pet.builder().id(1L).name("Apolo").build(),
                Pet.builder().id(2L).name("Tommy").build()
        );

        //When
        when(repository.findAll()).thenReturn(pets);

        var result = service.findAll();

        //Then
        assertThat(result).isNotEmpty().hasSize(2);

        verify(repository, times(1)).findAll();

    }

    @Test
    @DisplayName("Find by Id and get result")
    void findById_whenIdExists_returnPet() {
        //Given
        Long id = 1L;
        var expected = Optional.of(Pet.builder().build());

        //When
        when(repository.findById(id)).thenReturn(expected);

        var result = service.findById(id);

        //Then
        assertThat(result).isPresent();
        verify(repository, times(1)).findById(anyLong());

    }

    @Test
    @DisplayName("Find by Id and get empty")
    void findById_whenIdExists_returnEmpty() {
        //Given
        Long id = 2L;

        //When
        when(repository.findById(id)).thenReturn(Optional.empty());

        var result = service.findById(id);

        //Then
        assertThat(result).isEmpty();
        verify(repository, times(1)).findById(anyLong());

    }

    @Test
    void save() {
        //Given
        PetDto dto = PetDto.builder().name("Apolo").lastName("Cifuentes").build();
        Pet saved = Pet.builder().id(1L).name("Apolo").lastName("Cifuentes").build();
        PetDto expected = PetDto.builder().id(1L).name("Apolo").lastName("Cifuentes").build();

        //When
        when(repository.saveAndFlush(petCaptor.capture())).thenReturn(saved);

        var result = service.save(dto);

        //Then
        assertThat(result).isNotNull().isEqualTo(expected);
        verify(repository, atLeastOnce()).saveAndFlush(petCaptor.getValue());
    }

    @Test
    @DisplayName("Succeed updating a Pet")
    void update_whenCorrectIdAndPayload_shouldSucceed() {
        //Given
        Long id = 1L;
        Optional<Pet> pet = Optional.of(Pet.builder().id(id).name("Apolito").lastName("Cifuentes").build());
        PetDto dto = PetDto.builder().id(id).name("Apolo").lastName("Cifuentes").build();
        PetDto expected = PetDto.builder().id(1L).name("Apolo").lastName("Cifuentes").build();
        Pet saved = Pet.builder().id(1L).name("Apolo").lastName("Cifuentes").build();

        //When
        when(repository.findById(any())).thenReturn(pet);
        when(repository.save(petCaptor.capture())).thenReturn(saved);


        var result = service.update(id, dto);

        //Then
        assertThat(result).isNotNull().isEqualTo(expected);
        verify(repository, atLeastOnce()).save(petCaptor.getValue());
    }

    @Test
    @DisplayName("Fail updating a Pet when no Id")
    void update_whenNoId_shouldFail() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> service.update(null, null))
                .withMessage("Id is required");
    }

    @Test
    @DisplayName("Fail updating a Pet when no payload")
    void update_whenNoPayload_shouldFail() {
        //When
        when(repository.findById(any())).thenReturn(Optional.empty());

        //Then
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> service.update(1L, null))
                .withMessage("Not Found");
    }

    @Test
    void deleteById() {
        //Given
        Long id = 1L;

        //When
        service.deleteById(id);

        //Then
        verify(repository, atMostOnce()).deleteById(id);
        verifyNoMoreInteractions(repository);
    }
}