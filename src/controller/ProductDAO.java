package controller;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import model.Product;



public class ProductDAO {
	
	public List<Product> getAllProducts() {	
		List<Product> listOfProducts = new ArrayList<>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			Query<Product> query = session.createQuery("from Product");
			/* query.list() executes the Hibernate Query Language
			 * (FROM Book), gets all the Book rows from the database,
			 * turns each row into a Book object, using the Book
			 * constructor, adds each Book to a List and returns that
			 * List. */
			listOfProducts = query.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		
		return listOfProducts;
	}
	
	public void insertProduct(Product product) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction(); 	// starts the transaction
			/* session.save() will save the book object to the database
			 * and return the newly generated id */
			Integer id = (Integer)session.save(product);
			System.out.println("Product added, id : " + id);
			tx.commit(); 
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace(); 
		} finally {
			session.close();
		}
	}

	public void deleteProduct(int productId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			/* session.get() goes to the database with the bookId 
			 * argument, find the matching row for that bookId (primary key), 
			 * turns it into a Book object, using the constructor
			 * and setters from the Book class (listed as the first 
			 * argument) */
			Product product = session.get(Product.class, productId);
			/* Deletes the book from the database */
			session.delete(product);
			tx.commit();			
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	// 4 LOC: open session, get the book, close session, return the book
	public Product getProductById(int productId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Product product = session.get(Product.class, productId);
		session.close();
		return product;
	}
	
	public void updateProduct(Product updatedProduct) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.update(updatedProduct);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) 
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	protected List<Product> searchProduct(String searchType, String searchText) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		// Add on wildcard characters onto the searchText
		searchText = "%" + searchText + "%";
		String hql = "FROM Product WHERE " + searchType + " LIKE :text";
		Query<Product> query = session.createQuery(hql);
		query.setParameter("text", searchText);
	    List<Product> listOfProducts = query.list();
	    System.out.println("List from the DAO: " + listOfProducts);
		return listOfProducts;
	}
}


