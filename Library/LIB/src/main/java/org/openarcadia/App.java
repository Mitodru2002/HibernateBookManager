package org.openarcadia;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class App {
    private static SessionFactory factory;

    static {
        factory = new Configuration()
                .configure("hibernate.cfg.xml")  // or just .configure()
                .addAnnotatedClass(Book.class)
                .buildSessionFactory();
    }

    public static void main(String[] args) {
        try {
            addBook("SQL");
            addBook("Java");

            List<String> books = getAllBookNames();
            System.out.println("Books in DB: " + books);
        } finally {
            factory.close();
        }
    }

    public static void addBook(String name) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            Book book = new Book();
            book.setBookName(name);
            session.save(book);
            session.getTransaction().commit();
            System.out.println("Book '" + name + "' saved successfully.");
        }
    }

    public static List<String> getAllBookNames() {
        try (Session session = factory.openSession()) {
            return session.createQuery(
                    "SELECT b.bookName FROM Book b", String.class
            ).getResultList();
        }
    }
}
