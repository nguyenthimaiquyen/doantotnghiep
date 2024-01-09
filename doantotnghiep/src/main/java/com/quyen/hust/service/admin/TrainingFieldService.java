package com.quyen.hust.service.admin;

import com.quyen.hust.entity.admin.TrainingField;
import com.quyen.hust.exception.TrainingFieldNotFoundException;
import com.quyen.hust.model.request.admin.TrainingFieldRequest;
import com.quyen.hust.model.response.admin.TrainingFieldResponse;
import com.quyen.hust.repository.admin.TrainingFieldJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TrainingFieldService {
    private final TrainingFieldJpaRepository trainingFieldJpaRepository;


    public List<TrainingFieldResponse> getAll() {
        return trainingFieldJpaRepository.findAll().stream().map(
                trainingField -> TrainingFieldResponse.builder()
                        .id(trainingField.getId())
                        .fieldName(trainingField.getFieldName())
                        .description(trainingField.getDescription())
                        .build()
        ).collect(Collectors.toList());
    }

    public TrainingFieldResponse getTrainingFieldDetails(Long id) throws TrainingFieldNotFoundException {
        return trainingFieldJpaRepository.findById(id).map(
            trainingField -> TrainingFieldResponse.builder()
                    .fieldName(trainingField.getFieldName())
                    .description(trainingField.getDescription())
                    .build()
        ).orElseThrow(() -> new TrainingFieldNotFoundException("Training field with id" + id + " could not be found!"));
    }

    @Transactional
    public void saveTrainingField(TrainingFieldRequest request) {
        TrainingField trainingField = TrainingField.builder()
                .fieldName(request.getFieldName())
                .description(request.getDescription())
                .build();
        if (!ObjectUtils.isEmpty(request.getId())) {
            //update a training field
            TrainingField tf = trainingFieldJpaRepository.findById(request.getId()).get();
            tf.setFieldName(request.getFieldName());
            tf.setDescription(request.getDescription());
            return;
        }
        //create a new training field
        trainingFieldJpaRepository.save(trainingField);
    }

    public void deleteTrainingField(Long id) {
        trainingFieldJpaRepository.deleteById(id);
    }


}
