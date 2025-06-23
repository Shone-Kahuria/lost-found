package com.strathmore.lostandfound.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "lost_found_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LostFoundItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String description;

    private String category;

    private String location;

    private LocalDate dateLostFound;

    private String contactInfo;

    private String status; // "lost" or "found"
}
