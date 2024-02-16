import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/food">
        <Translate contentKey="global.menu.entities.food" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/order-stage">
        <Translate contentKey="global.menu.entities.orderStage" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/order-item">
        <Translate contentKey="global.menu.entities.orderItem" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/order-item-custom">
        <Translate contentKey="global.menu.entities.orderItemCustom" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/custom">
        <Translate contentKey="global.menu.entities.custom" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/order">
        <Translate contentKey="global.menu.entities.order" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/order-food">
        <Translate contentKey="global.menu.entities.orderFood" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/order-custom">
        <Translate contentKey="global.menu.entities.orderCustom" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
