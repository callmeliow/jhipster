import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Food from './food';
import OrderStage from './order-stage';
import OrderItem from './order-item';
import OrderItemCustom from './order-item-custom';
import Custom from './custom';
import Order from './order';
import OrderFood from './order-food';
import OrderCustom from './order-custom';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="food/*" element={<Food />} />
        <Route path="order-stage/*" element={<OrderStage />} />
        <Route path="order-item/*" element={<OrderItem />} />
        <Route path="order-item-custom/*" element={<OrderItemCustom />} />
        <Route path="custom/*" element={<Custom />} />
        <Route path="order/*" element={<Order />} />
        <Route path="order-food/*" element={<OrderFood />} />
        <Route path="order-custom/*" element={<OrderCustom />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
