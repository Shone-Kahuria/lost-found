package com.strathmore.lostandfound.controller;

import com.strathmore.lostandfound.model.LostFoundItem;
import com.strathmore.lostandfound.service.LostFoundItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "http://localhost:3000")
@Validated
public class LostFoundItemController {

    private static final Logger logger = LoggerFactory.getLogger(LostFoundItemController.class);

    @Autowired
    private LostFoundItemService service;

    @GetMapping
    public Page<LostFoundItem> getAllItems(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        logger.info("Received request to get all items, page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return service.getAllItems(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LostFoundItem> getItemById(@PathVariable Long id) {
        logger.info("Received request to get item by id: {}", id);
        Optional<LostFoundItem> item = service.getItemById(id);
        return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createItem(@Valid @RequestBody LostFoundItem item) {
        logger.info("Received request to create item: {}", item.getTitle());
        try {
            LostFoundItem savedItem = service.saveItem(item);
            return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.error("Validation error while creating item: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateItem(@PathVariable Long id, @Valid @RequestBody LostFoundItem itemDetails) {
        logger.info("Received request to update item with id: {}", id);
        Optional<LostFoundItem> optionalItem = service.getItemById(id);
        if (!optionalItem.isPresent()) {
            logger.warn("Item with id {} not found for update", id);
            return ResponseEntity.notFound().build();
        }
        LostFoundItem item = optionalItem.get();
        item.setTitle(itemDetails.getTitle());
        item.setDescription(itemDetails.getDescription());
        item.setCategory(itemDetails.getCategory());
        item.setLocation(itemDetails.getLocation());
        item.setDateLostFound(itemDetails.getDateLostFound());
        item.setContactInfo(itemDetails.getContactInfo());
        item.setStatus(itemDetails.getStatus());

        try {
            LostFoundItem updatedItem = service.saveItem(item);
            return ResponseEntity.ok(updatedItem);
        } catch (IllegalArgumentException e) {
            logger.error("Validation error while updating item: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdateItem(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        logger.info("Received request to partially update item with id: {}", id);
        Optional<LostFoundItem> optionalItem = service.getItemById(id);
        if (!optionalItem.isPresent()) {
            logger.warn("Item with id {} not found for partial update", id);
            return ResponseEntity.notFound().build();
        }
        LostFoundItem item = optionalItem.get();

        updates.forEach((key, value) -> {
            switch (key) {
                case "title":
                    item.setTitle((String) value);
                    break;
                case "description":
                    item.setDescription((String) value);
                    break;
                case "category":
                    item.setCategory((String) value);
                    break;
                case "location":
                    item.setLocation((String) value);
                    break;
                case "dateLostFound":
                    item.setDateLostFound(LocalDate.parse((String) value));
                    break;
                case "contactInfo":
                    item.setContactInfo((String) value);
                    break;
                case "status":
                    item.setStatus((String) value);
                    break;
                default:
                    logger.warn("Unknown field '{}' in partial update", key);
            }
        });

        try {
            LostFoundItem updatedItem = service.saveItem(item);
            return ResponseEntity.ok(updatedItem);
        } catch (IllegalArgumentException e) {
            logger.error("Validation error while partially updating item: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        logger.info("Received request to delete item with id: {}", id);
        Optional<LostFoundItem> optionalItem = service.getItemById(id);
        if (!optionalItem.isPresent()) {
            logger.warn("Item with id {} not found for deletion", id);
            return ResponseEntity.notFound().build();
        }
        service.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public List<LostFoundItem> getItemsByStatus(@PathVariable String status) {
        logger.info("Received request to get items by status: {}", status);
        return service.getItemsByStatus(status);
    }

    @GetMapping("/search")
    public Page<LostFoundItem> searchItems(@RequestParam String keyword,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        logger.info("Received request to search items with keyword: {}, page: {}, size: {}", keyword, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return service.searchItems(keyword, pageable);
    }
}
