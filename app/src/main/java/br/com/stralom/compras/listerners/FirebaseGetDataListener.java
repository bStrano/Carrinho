package br.com.stralom.compras.listerners;


import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public interface FirebaseGetDataListener<T> {
    void handleListData(List<T> objects);
    void onHandleListDataFailed();
    void getObject();
}
