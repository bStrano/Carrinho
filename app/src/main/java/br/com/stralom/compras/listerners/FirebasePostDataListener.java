package br.com.stralom.compras.listerners;


public interface FirebasePostDataListener<T> {
    void addOnSuccessListener(Object objects);
    void addOnFailureListener();
}
