package com.smartserve.service;

import com.smartserve.domain.OrderCustom;
import com.smartserve.repository.OrderCustomRepository;
import com.smartserve.service.dto.OrderCustomDTO;
import com.smartserve.service.mapper.OrderCustomMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.smartserve.domain.OrderCustom}.
 */
@Service
@Transactional
public class OrderCustomService {

    private final Logger log = LoggerFactory.getLogger(OrderCustomService.class);

    private final OrderCustomRepository orderCustomRepository;

    private final OrderCustomMapper orderCustomMapper;

    public OrderCustomService(OrderCustomRepository orderCustomRepository, OrderCustomMapper orderCustomMapper) {
        this.orderCustomRepository = orderCustomRepository;
        this.orderCustomMapper = orderCustomMapper;
    }

    /**
     * Save a orderCustom.
     *
     * @param orderCustomDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderCustomDTO save(OrderCustomDTO orderCustomDTO) {
        log.debug("Request to save OrderCustom : {}", orderCustomDTO);
        OrderCustom orderCustom = orderCustomMapper.toEntity(orderCustomDTO);
        orderCustom = orderCustomRepository.save(orderCustom);
        return orderCustomMapper.toDto(orderCustom);
    }

    /**
     * Update a orderCustom.
     *
     * @param orderCustomDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderCustomDTO update(OrderCustomDTO orderCustomDTO) {
        log.debug("Request to update OrderCustom : {}", orderCustomDTO);
        OrderCustom orderCustom = orderCustomMapper.toEntity(orderCustomDTO);
        orderCustom = orderCustomRepository.save(orderCustom);
        return orderCustomMapper.toDto(orderCustom);
    }

    /**
     * Partially update a orderCustom.
     *
     * @param orderCustomDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderCustomDTO> partialUpdate(OrderCustomDTO orderCustomDTO) {
        log.debug("Request to partially update OrderCustom : {}", orderCustomDTO);

        return orderCustomRepository
            .findById(orderCustomDTO.getId())
            .map(existingOrderCustom -> {
                orderCustomMapper.partialUpdate(existingOrderCustom, orderCustomDTO);

                return existingOrderCustom;
            })
            .map(orderCustomRepository::save)
            .map(orderCustomMapper::toDto);
    }

    /**
     * Get all the orderCustoms.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrderCustomDTO> findAll() {
        log.debug("Request to get all OrderCustoms");
        return orderCustomRepository.findAll().stream().map(orderCustomMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one orderCustom by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderCustomDTO> findOne(Long id) {
        log.debug("Request to get OrderCustom : {}", id);
        return orderCustomRepository.findById(id).map(orderCustomMapper::toDto);
    }

    /**
     * Delete the orderCustom by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderCustom : {}", id);
        orderCustomRepository.deleteById(id);
    }
}
