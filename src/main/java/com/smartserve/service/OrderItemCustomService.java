package com.smartserve.service;

import com.smartserve.domain.OrderItemCustom;
import com.smartserve.repository.OrderItemCustomRepository;
import com.smartserve.service.dto.OrderItemCustomDTO;
import com.smartserve.service.mapper.OrderItemCustomMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.smartserve.domain.OrderItemCustom}.
 */
@Service
@Transactional
public class OrderItemCustomService {

    private final Logger log = LoggerFactory.getLogger(OrderItemCustomService.class);

    private final OrderItemCustomRepository orderItemCustomRepository;

    private final OrderItemCustomMapper orderItemCustomMapper;

    public OrderItemCustomService(OrderItemCustomRepository orderItemCustomRepository, OrderItemCustomMapper orderItemCustomMapper) {
        this.orderItemCustomRepository = orderItemCustomRepository;
        this.orderItemCustomMapper = orderItemCustomMapper;
    }

    /**
     * Save a orderItemCustom.
     *
     * @param orderItemCustomDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderItemCustomDTO save(OrderItemCustomDTO orderItemCustomDTO) {
        log.debug("Request to save OrderItemCustom : {}", orderItemCustomDTO);
        OrderItemCustom orderItemCustom = orderItemCustomMapper.toEntity(orderItemCustomDTO);
        orderItemCustom = orderItemCustomRepository.save(orderItemCustom);
        return orderItemCustomMapper.toDto(orderItemCustom);
    }

    /**
     * Update a orderItemCustom.
     *
     * @param orderItemCustomDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderItemCustomDTO update(OrderItemCustomDTO orderItemCustomDTO) {
        log.debug("Request to update OrderItemCustom : {}", orderItemCustomDTO);
        OrderItemCustom orderItemCustom = orderItemCustomMapper.toEntity(orderItemCustomDTO);
        orderItemCustom = orderItemCustomRepository.save(orderItemCustom);
        return orderItemCustomMapper.toDto(orderItemCustom);
    }

    /**
     * Partially update a orderItemCustom.
     *
     * @param orderItemCustomDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderItemCustomDTO> partialUpdate(OrderItemCustomDTO orderItemCustomDTO) {
        log.debug("Request to partially update OrderItemCustom : {}", orderItemCustomDTO);

        return orderItemCustomRepository
            .findById(orderItemCustomDTO.getId())
            .map(existingOrderItemCustom -> {
                orderItemCustomMapper.partialUpdate(existingOrderItemCustom, orderItemCustomDTO);

                return existingOrderItemCustom;
            })
            .map(orderItemCustomRepository::save)
            .map(orderItemCustomMapper::toDto);
    }

    /**
     * Get all the orderItemCustoms.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrderItemCustomDTO> findAll() {
        log.debug("Request to get all OrderItemCustoms");
        return orderItemCustomRepository
            .findAll()
            .stream()
            .map(orderItemCustomMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one orderItemCustom by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderItemCustomDTO> findOne(Long id) {
        log.debug("Request to get OrderItemCustom : {}", id);
        return orderItemCustomRepository.findById(id).map(orderItemCustomMapper::toDto);
    }

    /**
     * Delete the orderItemCustom by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderItemCustom : {}", id);
        orderItemCustomRepository.deleteById(id);
    }
}
