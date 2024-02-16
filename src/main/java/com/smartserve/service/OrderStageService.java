package com.smartserve.service;

import com.smartserve.domain.OrderStage;
import com.smartserve.repository.OrderStageRepository;
import com.smartserve.service.dto.OrderStageDTO;
import com.smartserve.service.mapper.OrderStageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.smartserve.domain.OrderStage}.
 */
@Service
@Transactional
public class OrderStageService {

    private final Logger log = LoggerFactory.getLogger(OrderStageService.class);

    private final OrderStageRepository orderStageRepository;

    private final OrderStageMapper orderStageMapper;

    public OrderStageService(OrderStageRepository orderStageRepository, OrderStageMapper orderStageMapper) {
        this.orderStageRepository = orderStageRepository;
        this.orderStageMapper = orderStageMapper;
    }

    /**
     * Save a orderStage.
     *
     * @param orderStageDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderStageDTO save(OrderStageDTO orderStageDTO) {
        log.debug("Request to save OrderStage : {}", orderStageDTO);
        OrderStage orderStage = orderStageMapper.toEntity(orderStageDTO);
        orderStage = orderStageRepository.save(orderStage);
        return orderStageMapper.toDto(orderStage);
    }

    /**
     * Update a orderStage.
     *
     * @param orderStageDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderStageDTO update(OrderStageDTO orderStageDTO) {
        log.debug("Request to update OrderStage : {}", orderStageDTO);
        OrderStage orderStage = orderStageMapper.toEntity(orderStageDTO);
        orderStage = orderStageRepository.save(orderStage);
        return orderStageMapper.toDto(orderStage);
    }

    /**
     * Partially update a orderStage.
     *
     * @param orderStageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderStageDTO> partialUpdate(OrderStageDTO orderStageDTO) {
        log.debug("Request to partially update OrderStage : {}", orderStageDTO);

        return orderStageRepository
            .findById(orderStageDTO.getId())
            .map(existingOrderStage -> {
                orderStageMapper.partialUpdate(existingOrderStage, orderStageDTO);

                return existingOrderStage;
            })
            .map(orderStageRepository::save)
            .map(orderStageMapper::toDto);
    }

    /**
     * Get all the orderStages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderStageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrderStages");
        return orderStageRepository.findAll(pageable).map(orderStageMapper::toDto);
    }

    /**
     * Get one orderStage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderStageDTO> findOne(Long id) {
        log.debug("Request to get OrderStage : {}", id);
        return orderStageRepository.findById(id).map(orderStageMapper::toDto);
    }

    /**
     * Delete the orderStage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderStage : {}", id);
        orderStageRepository.deleteById(id);
    }
}
