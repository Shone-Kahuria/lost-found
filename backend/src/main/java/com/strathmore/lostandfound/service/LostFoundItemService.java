package com.strathmore.lostandfound.service;

import com.strathmore.lostandfound.model.LostFoundItem;
import com.strathmore.lostandfound.repository.LostFoundItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LostFoundItemService {

    private static final Logger logger = LoggerFactory.getLogger(LostFoundItemService.class);

    @Autowired
    private LostFoundItemRepository repository;

    public List<LostFoundItem> getAllItems() {
        logger.info("Fetching all lost and found items");
        return repository.findAll();
    }

    public Page<LostFoundItem> getAllItems(Pageable pageable) {
        logger.info("Fetching all lost and found items with pagination");
        return repository.findAll(pageable);
    }

    public Optional<LostFoundItem> getItemById(Long id) {
        logger.info("Fetching item by id: {}", id);
        return repository.findById(id);
    }

    public LostFoundItem saveItem(LostFoundItem item) {
        validateItem(item);
        logger.info("Saving item: {}", item.getTitle());
        return repository.save(item);
    }

    public void deleteItem(Long id) {
        if (!repository.existsById(id)) {
            logger.error("Attempted to delete non-existent item with id: {}", id);
            throw new IllegalArgumentException("Item with id " + id + " does not exist.");
        }
        logger.info("Deleting item with id: {}", id);
        repository.deleteById(id);
    }

    public List<LostFoundItem> getItemsByStatus(String status) {
        logger.info("Fetching items by status: {}", status);
        return repository.findByStatus(status);
    }

    public List<LostFoundItem> searchItems(String keyword) {
        logger.info("Searching items with keyword: {}", keyword);
        return repository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
    }

    public Page<LostFoundItem> searchItems(String keyword, Pageable pageable) {
        logger.info("Searching items with keyword: {} and pagination", keyword);
        return repository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword, pageable);
    }

    private void validateItem(LostFoundItem item) {
        if (item.getTitle() == null || item.getTitle().trim().isEmpty()) {
            logger.error("Validation failed: Title cannot be empty");
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (item.getDescription() == null || item.getDescription().trim().isEmpty()) {
            logger.error("Validation failed: Description cannot be empty");
            throw new IllegalArgumentException("Description cannot be empty");
        }
        if (item.getCategory() == null || item.getCategory().trim().isEmpty()) {
            logger.error("Validation failed: Category cannot be empty");
            throw new IllegalArgumentException("Category cannot be empty");
        }
        if (item.getStatus() == null || item.getStatus().trim().isEmpty()) {
            logger.error("Validation failed: Status cannot be empty");
            throw new IllegalArgumentException("Status cannot be empty");
        }
        // Add more validations as needed
    }
}
