package com.smartserve.service;

import com.smartserve.domain.OrderFood;
import com.smartserve.repository.OrderFoodRepository;
import com.smartserve.service.dto.OrderFoodDTO;
import com.smartserve.service.mapper.OrderFoodMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.smartserve.domain.OrderFood}.
 */
@Service
@Transactional
public class OrderFoodService {

    private final Logger log = LoggerFactory.getLogger(OrderFoodService.class);

    private final OrderFoodRepository orderFoodRepository;

    private final OrderFoodMapper orderFoodMapper;

    public OrderFoodService(OrderFoodRepository orderFoodRepository, OrderFoodMapper orderFoodMapper) {
        this.orderFoodRepository = orderFoodRepository;
        this.orderFoodMapper = orderFoodMapper;
    }

    /**
     * Save a orderFood.
     *
     * @param orderFoodDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderFoodDTO save(OrderFoodDTO orderFoodDTO) {
        log.debug("Request to save OrderFood : {}", orderFoodDTO);
        OrderFood orderFood = orderFoodMapper.toEntity(orderFoodDTO);
        orderFood = orderFoodRepository.save(orderFood);
        return orderFoodMapper.toDto(orderFood);
    }

    /**
     * Update a orderFood.
     *
     * @param orderFoodDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderFoodDTO update(OrderFoodDTO orderFoodDTO) {
        log.debug("Request to update OrderFood : {}", orderFoodDTO);
        OrderFood orderFood = orderFoodMapper.toEntity(orderFoodDTO);
        orderFood = orderFoodRepository.save(orderFood);
        return orderFoodMapper.toDto(orderFood);
    }

    /**
     * Partially update a orderFood.
     *
     * @param orderFoodDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderFoodDTO> partialUpdate(OrderFoodDTO orderFoodDTO) {
        log.debug("Request to partially update OrderFood : {}", orderFoodDTO);

        return orderFoodRepository
            .findById(orderFoodDTO.getId())
            .map(existingOrderFood -> {
                orderFoodMapper.partialUpdate(existingOrderFood, orderFoodDTO);

                return existingOrderFood;
            })
            .map(orderFoodRepository::save)
            .map(orderFoodMapper::toDto);
    }

    /**
     * Get all the orderFoods.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrderFoodDTO> findAll() {
        log.debug("Request to get all OrderFoods");
        return orderFoodRepository.findAll().stream().map(orderFoodMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one orderFood by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderFoodDTO> findOne(Long id) {
        log.debug("Request to get OrderFood : {}", id);
        return orderFoodRepository.findById(id).map(orderFoodMapper::toDto);
    }

    /**
     * Delete the orderFood by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderFood : {}", id);
        orderFoodRepository.deleteById(id);
    }
}
