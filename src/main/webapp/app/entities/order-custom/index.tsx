import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import OrderCustom from './order-custom';
import OrderCustomDetail from './order-custom-detail';
import OrderCustomUpdate from './order-custom-update';
import OrderCustomDeleteDialog from './order-custom-delete-dialog';

const OrderCustomRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<OrderCustom />} />
    <Route path="new" element={<OrderCustomUpdate />} />
    <Route path=":id">
      <Route index element={<OrderCustomDetail />} />
      <Route path="edit" element={<OrderCustomUpdate />} />
      <Route path="delete" element={<OrderCustomDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OrderCustomRoutes;
