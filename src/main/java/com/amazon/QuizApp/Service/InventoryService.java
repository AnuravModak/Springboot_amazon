package com.example.inventory;

import com.example.repository.InventoryRepository;
import com.example.model.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public boolean reserveInventory(Long productId) {

        Inventory inventory = inventoryRepository.findById(productId).orElse(null);

        if (inventory == null) {
            return false;
        }

        if (inventory.getAvailableQuantity() > 0) {
            inventory.setAvailableQuantity(inventory.getAvailableQuantity() - 1);
            return true;
        }

        return false;
    }
}