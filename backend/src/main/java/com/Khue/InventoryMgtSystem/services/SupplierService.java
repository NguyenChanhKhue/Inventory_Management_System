package com.Khue.InventoryMgtSystem.services;

import com.Khue.InventoryMgtSystem.dto.Response;
import com.Khue.InventoryMgtSystem.dto.SupplierDTO;

public interface SupplierService {
    Response addSupplier(SupplierDTO supplierDTO);

    Response updateSupplier(Long id, SupplierDTO supplierDTO);

    Response getAllSupplier();

    Response getSupplierById(Long id);

    Response deleteSupplier(Long id);
}
