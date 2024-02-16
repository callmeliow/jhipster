package com.smartserve.service;

import com.smartserve.domain.Custom;
import com.smartserve.repository.CustomRepository;
import com.smartserve.service.dto.CustomDTO;
import com.smartserve.service.mapper.CustomMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.smartserve.domain.Custom}.
 */
@Service
@Transactional
public class CustomService {

    private final Logger log = LoggerFactory.getLogger(CustomService.class);

    private final CustomRepository customRepository;

    private final CustomMapper customMapper;

    public CustomService(CustomRepository customRepository, CustomMapper customMapper) {
        this.customRepository = customRepository;
        this.customMapper = customMapper;
    }

    /**
     * Save a custom.
     *
     * @param customDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomDTO save(CustomDTO customDTO) {
        log.debug("Request to save Custom : {}", customDTO);
        Custom custom = customMapper.toEntity(customDTO);
        custom = customRepository.save(custom);
        return customMapper.toDto(custom);
    }

    /**
     * Update a custom.
     *
     * @param customDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomDTO update(CustomDTO customDTO) {
        log.debug("Request to update Custom : {}", customDTO);
        Custom custom = customMapper.toEntity(customDTO);
        custom = customRepository.save(custom);
        return customMapper.toDto(custom);
    }

    /**
     * Partially update a custom.
     *
     * @param customDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomDTO> partialUpdate(CustomDTO customDTO) {
        log.debug("Request to partially update Custom : {}", customDTO);

        return customRepository
            .findById(customDTO.getId())
            .map(existingCustom -> {
                customMapper.partialUpdate(existingCustom, customDTO);

                return existingCustom;
            })
            .map(customRepository::save)
            .map(customMapper::toDto);
    }

    /**
     * Get all the customs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Customs");
        return customRepository.findAll(pageable).map(customMapper::toDto);
    }

    /**
     * Get one custom by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomDTO> findOne(Long id) {
        log.debug("Request to get Custom : {}", id);
        return customRepository.findById(id).map(customMapper::toDto);
    }

    /**
     * Delete the custom by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Custom : {}", id);
        customRepository.deleteById(id);
    }
}
