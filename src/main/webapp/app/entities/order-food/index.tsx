import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import OrderFood from './order-food';
import OrderFoodDetail from './order-food-detail';
import OrderFoodUpdate from './order-food-update';
import OrderFoodDeleteDialog from './order-food-delete-dialog';

const OrderFoodRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<OrderFood />} />
    <Route path="new" element={<OrderFoodUpdate />} />
    <Route path=":id">
      <Route index element={<OrderFoodDetail />} />
      <Route path="edit" element={<OrderFoodUpdate />} />
      <Route path="delete" element={<OrderFoodDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OrderFoodRoutes;
