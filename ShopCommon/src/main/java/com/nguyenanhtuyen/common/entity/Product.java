package com.nguyenanhtuyen.common.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(unique = true, length = 256, nullable = false)
	private String name;
	
	@Column(unique = true, length = 256, nullable = false)
	private String alias;
	
	@Column(name = "short_description", length = 512, nullable = false)
	private String shortDescription;
	
	@Column(name = "full_description", length = 4096, nullable = false)
	private String fullDescription;
	
	@Column(name = "created_time")
	private Date createdTime;
	
	@Column(name = "updated_time")
	private Date updatedTime;
	
	private boolean enabled;
	
	@Column(name = "in_stock")
	private boolean inStock;
	
	private float cost;
	
	private float price;
	
	@Column(name = "discount_percent")
	private float discountPercent;
	
	private float length;
	
	private float width;
	
	private float height;
	
	private float weight;
	
	@Column(name = "main_image", nullable = false)
	private String mainImage;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
	
	@ManyToOne
	@JoinColumn(name = "brand_id")
	private Brand brand;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private Set<ProductImage> images = new HashSet<>();
	
	public void addExtraImage(String imageName) {
		this.images.add(new ProductImage(imageName, this));
	}
	
	@Transient
	public String getMainImagePath() {
		if(this.id == null) {
			return "/images/image-thumbnail.png";
		}
		return "/product-images/product-" + this.id + "/" + this.mainImage;
	}
	
}
