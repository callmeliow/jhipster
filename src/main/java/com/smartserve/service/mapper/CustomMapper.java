package com.smartserve.service.mapper;

import com.smartserve.domain.Custom;
import com.smartserve.service.dto.CustomDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Custom} and its DTO {@link CustomDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomMapper extends EntityMapper<CustomDTO, Custom> {}
