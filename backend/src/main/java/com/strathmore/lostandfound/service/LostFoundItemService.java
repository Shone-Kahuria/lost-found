package com.strathmore.lostandfound.service;

import com.strathmore.lostandfound.model.LostFoundItem;
import com.strathmore.lostandfound.repository.LostFoundItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LostFoundItemService {

    @Autowired
    private LostFoundItemRepository repository;

    public List<LostFoundItem> getAllItems() {
        return repository.findAll();
    }

    public Page<LostFoundItem> getAllItems(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<LostFoundItem> getItemById(Long id) {
        return repository.findById(id);
    }

    public LostFoundItem saveItem(LostFoundItem item) {
        validateItem(item);
        return repository.save(item);
    }

    public void deleteItem(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Item with id " + id + " does not exist.");
        }
        repository.deleteById(id);
    }

    public List<LostFoundItem> getItemsByStatus(String status) {
        return repository.findByStatus(status);
    }

    public List<LostFoundItem> searchItems(String keyword) {
        return repository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
    }

    public Page<LostFoundItem> searchItems(String keyword, Pageable pageable) {
        return repository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword, pageable);
    }

    private void validateItem(LostFoundItem item) {
        if (item.getTitle() == null || item.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (item.getDescription() == null || item.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        if (item.getCategory() == null || item.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty");
        }
        if (item.getStatus() == null || item.getStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be empty");
        }
        // Add more validations as needed
    }
}
