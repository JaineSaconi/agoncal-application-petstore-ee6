package org.agoncal.application.petstore.service;

import org.agoncal.application.petstore.domain.Category;
import org.agoncal.application.petstore.domain.Item;
import org.agoncal.application.petstore.domain.Product;
import org.agoncal.application.petstore.exception.ValidationException;
import org.agoncal.application.petstore.util.Loggable;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;
import lombok.NonNull;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */

@Stateless
@Loggable
public class CatalogService implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================

    @Inject
    private EntityManager em;

    // ======================================
    // =              Public Methods        =
    // ======================================

    public Category findCategory(@NonNull Long categoryId) {

        return em.find(Category.class, categoryId);
    }

    public Category findCategory(@NonNull String categoryName) {

        TypedQuery<Category> typedQuery = em.createNamedQuery(Category.FIND_BY_NAME, Category.class);
        typedQuery.setParameter("pname", categoryName);
        return typedQuery.getSingleResult();
    }

    public List<Category> findAllCategories() {
        TypedQuery<Category> typedQuery = em.createNamedQuery(Category.FIND_ALL, Category.class);
        return typedQuery.getResultList();
    }

    public Category createCategory(@NonNull Category category) {

        em.persist(category);
        return category;
    }

    public Category updateCategory(@NonNull Category category) {

        return em.merge(category);
    }

    public void removeCategory(@NonNull Category category) {
    
        em.remove(em.merge(category));
    }

    public void removeCategory(@NonNull Long categoryId) {

        removeCategory(findCategory(categoryId));
    }

    public List<Product> findProducts(@NonNull String categoryName) {

        TypedQuery<Product> typedQuery = em.createNamedQuery(Product.FIND_BY_CATEGORY_NAME, Product.class);
        typedQuery.setParameter("pname", categoryName);
        return typedQuery.getResultList();
    }

    public Product findProduct(@NonNull Long productId) {

        Product product = em.find(Product.class, productId);
        if (product != null) {
            product.getItems(); // TODO check lazy loading
        }
        return product;
    }

    public List<Product> findAllProducts() {
        TypedQuery<Product> typedQuery = em.createNamedQuery(Product.FIND_ALL, Product.class);
        return typedQuery.getResultList();
    }

    public Product createProduct(@NonNull Product product) {

        if (product.getCategory() != null && product.getCategory().getId() == null)
            em.persist(product.getCategory());

        em.persist(product);
        return product;
    }

    public Product updateProduct(@NonNull Product product) {

        return em.merge(product);
    }

    public void removeProduct(@NonNull Product product) {

        em.remove(em.merge(product));
    }

    public void removeProduct(@NonNull Long productId) {

        removeProduct(findProduct(productId));
    }

    public List<Item> findItems(@NonNull Long productId) {

        TypedQuery<Item> typedQuery = em.createNamedQuery(Item.FIND_BY_PRODUCT_ID, Item.class);
        typedQuery.setParameter("productId", productId);
        return typedQuery.getResultList();
    }

    public Item findItem(@NonNull final Long itemId) {
        return em.find(Item.class, itemId);
    }

    public List<Item> searchItems(String keyword) {
        if (keyword == null)
            keyword = "";

        TypedQuery<Item> typedQuery = em.createNamedQuery(Item.SEARCH, Item.class);
        typedQuery.setParameter("keyword", "%" + keyword.toUpperCase() + "%");
        return typedQuery.getResultList();
    }

    public List<Item> findAllItems() {
        TypedQuery<Item> typedQuery = em.createNamedQuery(Item.FIND_ALL, Item.class);
        return typedQuery.getResultList();
    }

    public Item createItem(@NonNull Item item) {
        if (item.getProduct() != null && item.getProduct().getId() == null) {
            em.persist(item.getProduct());
            if (item.getProduct().getCategory() != null && item.getProduct().getCategory().getId() == null)
                em.persist(item.getProduct().getCategory());
        }

        em.persist(item);
        return item;
    }

    public Item updateItem(@NonNull Item item) {
        return em.merge(item);
    }

    public void removeItem(@NonNull Item item) {

        em.remove(em.merge(item));
    }

    public void removeItem(@NonNull Long itemId) {

        removeItem(findItem(itemId));
    }
}
