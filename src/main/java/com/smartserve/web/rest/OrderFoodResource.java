package com.smartserve.web.rest;

import com.smartserve.repository.OrderFoodRepository;
import com.smartserve.service.OrderFoodService;
import com.smartserve.service.dto.OrderFoodDTO;
import com.smartserve.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.smartserve.domain.OrderFood}.
 */
@RestController
@RequestMapping("/api/order-foods")
public class OrderFoodResource {

    private final Logger log = LoggerFactory.getLogger(OrderFoodResource.class);

    private static final String ENTITY_NAME = "orderFood";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderFoodService orderFoodService;

    private final OrderFoodRepository orderFoodRepository;

    public OrderFoodResource(OrderFoodService orderFoodService, OrderFoodRepository orderFoodRepository) {
        this.orderFoodService = orderFoodService;
        this.orderFoodRepository = orderFoodRepository;
    }

    /**
     * {@code POST  /order-foods} : Create a new orderFood.
     *
     * @param orderFoodDTO the orderFoodDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderFoodDTO, or with status {@code 400 (Bad Request)} if the orderFood has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderFoodDTO> createOrderFood(@Valid @RequestBody OrderFoodDTO orderFoodDTO) throws URISyntaxException {
        log.debug("REST request to save OrderFood : {}", orderFoodDTO);
        if (orderFoodDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderFood cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderFoodDTO result = orderFoodService.save(orderFoodDTO);
        return ResponseEntity
            .created(new URI("/api/order-foods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /order-foods/:id} : Updates an existing orderFood.
     *
     * @param id the id of the orderFoodDTO to save.
     * @param orderFoodDTO the orderFoodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderFoodDTO,
     * or with status {@code 400 (Bad Request)} if the orderFoodDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderFoodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderFoodDTO> updateOrderFood(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderFoodDTO orderFoodDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OrderFood : {}, {}", id, orderFoodDTO);
        if (orderFoodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderFoodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderFoodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrderFoodDTO result = orderFoodService.update(orderFoodDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderFoodDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /order-foods/:id} : Partial updates given fields of an existing orderFood, field will ignore if it is null
     *
     * @param id the id of the orderFoodDTO to save.
     * @param orderFoodDTO the orderFoodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderFoodDTO,
     * or with status {@code 400 (Bad Request)} if the orderFoodDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orderFoodDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderFoodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderFoodDTO> partialUpdateOrderFood(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderFoodDTO orderFoodDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrderFood partially : {}, {}", id, orderFoodDTO);
        if (orderFoodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderFoodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderFoodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderFoodDTO> result = orderFoodService.partialUpdate(orderFoodDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderFoodDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /order-foods} : get all the orderFoods.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderFoods in body.
     */
    @GetMapping("")
    public List<OrderFoodDTO> getAllOrderFoods() {
        log.debug("REST request to get all OrderFoods");
        return orderFoodService.findAll();
    }

    /**
     * {@code GET  /order-foods/:id} : get the "id" orderFood.
     *
     * @param id the id of the orderFoodDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderFoodDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderFoodDTO> getOrderFood(@PathVariable("id") Long id) {
        log.debug("REST request to get OrderFood : {}", id);
        Optional<OrderFoodDTO> orderFoodDTO = orderFoodService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderFoodDTO);
    }

    /**
     * {@code DELETE  /order-foods/:id} : delete the "id" orderFood.
     *
     * @param id the id of the orderFoodDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderFood(@PathVariable("id") Long id) {
        log.debug("REST request to delete OrderFood : {}", id);
        orderFoodService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
