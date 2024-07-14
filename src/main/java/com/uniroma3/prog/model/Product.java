package com.uniroma3.prog.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

<<<<<<< HEAD
import jakarta.persistence.*;
=======
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
>>>>>>> 1679799afc19388a83f21569d3440de085c2c3d1

@Entity
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
<<<<<<< HEAD
	@Enumerated(EnumType.STRING)
	private Category category;
=======
	private String marca;
	private String category;
>>>>>>> 1679799afc19388a83f21569d3440de085c2c3d1
	private String description;
	@OneToOne
	private Image image;
	@OneToMany(mappedBy = "prodotto")
	private List<Review> review = new ArrayList<>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
<<<<<<< HEAD
	
	public Category getCategory() {
=======
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public String getCategory() {
>>>>>>> 1679799afc19388a83f21569d3440de085c2c3d1
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public List<Review> getReview() {
		return review;
	}
	public void setReview(List<Review> review) {
		this.review = review;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		return Objects.equals(id, other.id);
	}
	
	
	

}
