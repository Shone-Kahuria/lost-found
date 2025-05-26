package com.strathmore.lostandfound.controller;

import com.strathmore.lostandfound.model.LostFoundItem;
import com.strathmore.lostandfound.service.LostFoundItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "http://localhost:3000")
public class LostFoundItemController {

    @Autowired
    private LostFoundItemService service;

    @GetMapping
    public Page<LostFoundItem> getAllItems(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return service.getAllItems(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LostFoundItem> getItemById(@PathVariable Long id) {
        Optional<LostFoundItem> item = service.getItemById(id);
        return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public LostFoundItem createItem(@RequestBody LostFoundItem item) {
        return service.saveItem(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LostFoundItem> updateItem(@PathVariable Long id, @RequestBody LostFoundItem itemDetails) {
        Optional<LostFoundItem> optionalItem = service.getItemById(id);
        if (!optionalItem.isPresent()) {
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

        LostFoundItem updatedItem = service.saveItem(item);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        Optional<LostFoundItem> optionalItem = service.getItemById(id);
        if (!optionalItem.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public List<LostFoundItem> getItemsByStatus(@PathVariable String status) {
        return service.getItemsByStatus(status);
    }

    @GetMapping("/search")
    public Page<LostFoundItem> searchItems(@RequestParam String keyword,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return service.searchItems(keyword, pageable);
    }
}
