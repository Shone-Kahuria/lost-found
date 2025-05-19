package com.strathmore.lostandfound.service;

import com.strathmore.lostandfound.model.LostFoundItem;
import com.strathmore.lostandfound.repository.LostFoundItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Optional<LostFoundItem> getItemById(Long id) {
        return repository.findById(id);
    }

    public LostFoundItem saveItem(LostFoundItem item) {
        return repository.save(item);
    }

    public void deleteItem(Long id) {
        repository.deleteById(id);
    }

    public List<LostFoundItem> getItemsByStatus(String status) {
        return repository.findByStatus(status);
    }

    public List<LostFoundItem> searchItems(String keyword) {
        return repository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
    }
}
