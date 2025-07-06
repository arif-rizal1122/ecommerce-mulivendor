package com.ecommerce.ecommerce_multivendor.service.product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ecommerce.ecommerce_multivendor.dto.request.CreateProductRequest;
import com.ecommerce.ecommerce_multivendor.exception.ProductException;
import com.ecommerce.ecommerce_multivendor.model.Category;
import com.ecommerce.ecommerce_multivendor.model.Product;
import com.ecommerce.ecommerce_multivendor.model.Seller;
import com.ecommerce.ecommerce_multivendor.repository.CategoryRepository;
import com.ecommerce.ecommerce_multivendor.repository.ProductRepository;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Product createProduct(CreateProductRequest request, Seller seller) {
        
        Category category1 = categoryRepository.findByCategoryId(request.getCategory());
        if (category1==null) {
            Category category = new Category();
            category.setCategoryId(request.getCategory());
            category.setLevel(1);
            categoryRepository.save(category);
        }
        Category category2 = categoryRepository.findByCategoryId(request.getCategory2());
        if (category2==null) {
            Category category = new Category();
            category.setCategoryId(request.getCategory2());
            category.setLevel(2);
            category.setParentCategory(category1);
            categoryRepository.save(category);
        }
        Category category3 = categoryRepository.findByCategoryId(request.getCategory3());
         if (category3==null) {
            Category category = new Category();
            category.setCategoryId(request.getCategory3());
            category.setLevel(3);
            category.setParentCategory(category2);
            categoryRepository.save(category);
        }

        int discountPercentage = calculateDiscountPercentage(request.getMrpPrice(), request.getSellingPrice());

        Product product = new Product();
        product.setSeller(seller);
        product.setCategory(category3);
        product.setDescription(request.getDescription());
        product.setCreatedAt(LocalDateTime.now());
        product.setTitle(request.getTitle());
        product.setColor(request.getColor());
        product.setSellingPrice(request.getSellingPrice());
        product.setImages(request.getImages());
        product.setMrpPrice(request.getMrpPrice());
        product.setSizes(request.getSizes());
        product.setDiscountPercent(discountPercentage);

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long productId) throws ProductException {
        Product product = findProductById(productId);
        productRepository.delete(product);
    }

    @Override
    public Product findProductById(Long productId) throws ProductException {
        return productRepository.findById(productId)
           .orElseThrow(() -> new ProductException("product not found with id .." + productId));
    }


    @Override
    public Page<Product> getAllProduct(String category, String brand, String colors, String sizes,Integer minPrize, Integer maxPrize,
            Integer minDiscount, String sort, String stock, Integer pageNumber) {
        
        Specification<Product> spec = (root, query, criteriaBuilder)->{
            List<Predicate> predicates = new ArrayList<>();
            if (category!=null) {
                Join<Product, Category> categoryJoin=root.join("category");
                predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"), category));
            }
            if (colors!=null && !colors.isEmpty()) {
                System.out.println("color "+colors);
                predicates.add(criteriaBuilder.equal(root.get("color"), colors));
            }
            if (sizes!=null && !sizes.isEmpty()) {
                System.out.println("size "+sizes);
                predicates.add(criteriaBuilder.equal(root.get("size"), sizes));
            }
            if (minPrize != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrize"), minPrize));
            }
            if (maxPrize != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"), maxPrize));
            }
            if (minDiscount != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discountPercent"), minDiscount));
            }
            if (stock != null) {
                predicates.add(criteriaBuilder.equal(root.get("stock"), stock));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Pageable pageable;
        if (sort!=null) {
            switch (sort) {
                case "price_high":
                    pageable = PageRequest.of(pageNumber!=null? pageNumber:0, 10, Sort.by("sellingPrice").ascending());
                    break;
                case "price_low":
                    pageable = PageRequest.of(pageNumber!=null? pageNumber:0, 10, Sort.by("sellingPrice").descending());
                    break;
                default:
                    pageable = PageRequest.of(pageNumber!=null? pageNumber:0, 10, Sort.unsorted());
                    break;    
            }
            } else {
                 pageable = PageRequest.of(pageNumber!=null ? pageNumber:0, 10, Sort.unsorted());
            }
            return productRepository.findAll(spec, pageable);
    }



    @Override
    public List<Product> getProductBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }

    @Override
    public List<Product> searchProducts(String query) {   
        return searchProducts(query);
    }

    @Override
    public Product updateProduct(Long productId, Product product) throws ProductException {
        findProductById(productId);
        product.setId(productId);

        return productRepository.save(product);
    }
    

    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice){
        if (mrpPrice<=0) {
            throw new IllegalArgumentException("actual price must be greater than 0");
        }
        double discount = mrpPrice - sellingPrice;
        double discountPercentage = (discount/mrpPrice)*100;
        return (int)discountPercentage;
    }

}
