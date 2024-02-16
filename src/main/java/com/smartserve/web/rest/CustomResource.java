package com.smartserve.web.rest;

import com.smartserve.repository.CustomRepository;
import com.smartserve.service.CustomService;
import com.smartserve.service.dto.CustomDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.smartserve.domain.Custom}.
 */
@RestController
@RequestMapping("/api/customs")
public class CustomResource {

    private final Logger log = LoggerFactory.getLogger(CustomResource.class);

    private static final String ENTITY_NAME = "custom";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomService customService;

    private final CustomRepository customRepository;

    public CustomResource(CustomService customService, CustomRepository customRepository) {
        this.customService = customService;
        this.customRepository = customRepository;
    }

    /**
     * {@code POST  /customs} : Create a new custom.
     *
     * @param customDTO the customDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customDTO, or with status {@code 400 (Bad Request)} if the custom has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustomDTO> createCustom(@Valid @RequestBody CustomDTO customDTO) throws URISyntaxException {
        log.debug("REST request to save Custom : {}", customDTO);
        if (customDTO.getId() != null) {
            throw new BadRequestAlertException("A new custom cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomDTO result = customService.save(customDTO);
        return ResponseEntity
            .created(new URI("/api/customs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /customs/:id} : Updates an existing custom.
     *
     * @param id the id of the customDTO to save.
     * @param customDTO the customDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customDTO,
     * or with status {@code 400 (Bad Request)} if the customDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomDTO> updateCustom(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomDTO customDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Custom : {}, {}", id, customDTO);
        if (customDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CustomDTO result = customService.update(customDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /customs/:id} : Partial updates given fields of an existing custom, field will ignore if it is null
     *
     * @param id the id of the customDTO to save.
     * @param customDTO the customDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customDTO,
     * or with status {@code 400 (Bad Request)} if the customDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomDTO> partialUpdateCustom(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomDTO customDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Custom partially : {}, {}", id, customDTO);
        if (customDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomDTO> result = customService.partialUpdate(customDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /customs} : get all the customs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CustomDTO>> getAllCustoms(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Customs");
        Page<CustomDTO> page = customService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customs/:id} : get the "id" custom.
     *
     * @param id the id of the customDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomDTO> getCustom(@PathVariable("id") Long id) {
        log.debug("REST request to get Custom : {}", id);
        Optional<CustomDTO> customDTO = customService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customDTO);
    }

    /**
     * {@code DELETE  /customs/:id} : delete the "id" custom.
     *
     * @param id the id of the customDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustom(@PathVariable("id") Long id) {
        log.debug("REST request to delete Custom : {}", id);
        customService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
