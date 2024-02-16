import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import OrderStage from './order-stage';
import OrderStageDetail from './order-stage-detail';
import OrderStageUpdate from './order-stage-update';
import OrderStageDeleteDialog from './order-stage-delete-dialog';

const OrderStageRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<OrderStage />} />
    <Route path="new" element={<OrderStageUpdate />} />
    <Route path=":id">
      <Route index element={<OrderStageDetail />} />
      <Route path="edit" element={<OrderStageUpdate />} />
      <Route path="delete" element={<OrderStageDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OrderStageRoutes;
