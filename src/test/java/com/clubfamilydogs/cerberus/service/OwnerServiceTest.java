package com.clubfamilydogs.cerberus.service;

import com.clubfamilydogs.cerberus.dto.OwnerDto;
import com.clubfamilydogs.cerberus.mapper.OwnerMapper;
import com.clubfamilydogs.cerberus.mapper.OwnerMapperImpl;
import com.clubfamilydogs.cerberus.mapper.PetMapper;
import com.clubfamilydogs.cerberus.persistance.OwnerRepository;
import com.clubfamilydogs.cerberus.persistance.models.Owner;
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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerServiceTest {

    @Mock
    OwnerRepository repository;

    @InjectMocks
    OwnerService service;

    @Captor
    ArgumentCaptor<Owner> captor;

    OwnerMapper mapper = new OwnerMapperImpl(Mappers.getMapper(PetMapper.class));


    @BeforeEach
    void setUp() {
        service = new OwnerService(mapper, repository);
    }


    @Test
    void findAll() {
        //Given
        List<Owner> Owners = List.of(
                Owner.builder().id(1L).name("Apolo").build(),
                Owner.builder().id(2L).name("Tommy").build()
        );

        //When
        when(repository.findAll()).thenReturn(Owners);

        var result = service.findAll();

        //Then
        assertThat(result).isNotEmpty().hasSize(2);

        verify(repository, times(1)).findAll();

    }

    @Test
    @DisplayName("Find by Id and get result")
    void findById_whenIdExists_returnOwner() {
        //Given
        Long id = 1L;
        var expected = Optional.of(Owner.builder().build());

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
        OwnerDto dto = OwnerDto.builder().name("Apolo").lastName("Cifuentes").build();
        Owner saved = Owner.builder().id(1L).name("Apolo").lastName("Cifuentes").build();
        OwnerDto expected = OwnerDto.builder().id(1L).name("Apolo").lastName("Cifuentes").build();

        //When
        when(repository.saveAndFlush(captor.capture())).thenReturn(saved);

        var result = service.save(dto);

        //Then
        assertThat(result).isNotNull().isEqualTo(expected);
        verify(repository, atLeastOnce()).saveAndFlush(captor.getValue());
    }

    @Test
    @DisplayName("Succeed updating an Owner")
    void update_whenCorrectIdAndPayload_shouldSucceed() {
        //Given
        Long id = 1L;
        Optional<Owner> owner = Optional.of(Owner.builder().id(id).name("Apolito").lastName("Cifuentes").pets(new HashSet<>()).build());
        OwnerDto dto = OwnerDto.builder().id(id).name("Apolo").lastName("Cifuentes").pets(new HashSet<>()).build();
        OwnerDto expected = OwnerDto.builder().id(1L).name("Apolo").lastName("Cifuentes").pets(new HashSet<>()).build();
        Owner saved = Owner.builder().id(1L).name("Apolo").lastName("Cifuentes").pets(new HashSet<>()).build();

        //When
        when(repository.findById(any())).thenReturn(owner);
        when(repository.saveAndFlush(captor.capture())).thenReturn(saved);


        var result = service.update(id, dto);

        //Then
        assertThat(result).isNotNull().isEqualTo(expected);
        verify(repository, atLeastOnce()).saveAndFlush(captor.getValue());
    }

    @Test
    @DisplayName("Fail updating a Owner when no Id")
    void update_whenNoId_shouldFail() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> service.update(null, null))
                .withMessage("Id is required");
    }

    @Test
    @DisplayName("Fail updating a Owner when no payload")
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