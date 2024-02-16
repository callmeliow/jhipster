import food from 'app/entities/food/food.reducer';
import orderStage from 'app/entities/order-stage/order-stage.reducer';
import orderItem from 'app/entities/order-item/order-item.reducer';
import orderItemCustom from 'app/entities/order-item-custom/order-item-custom.reducer';
import custom from 'app/entities/custom/custom.reducer';
import order from 'app/entities/order/order.reducer';
import orderFood from 'app/entities/order-food/order-food.reducer';
import orderCustom from 'app/entities/order-custom/order-custom.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  food,
  orderStage,
  orderItem,
  orderItemCustom,
  custom,
  order,
  orderFood,
  orderCustom,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
