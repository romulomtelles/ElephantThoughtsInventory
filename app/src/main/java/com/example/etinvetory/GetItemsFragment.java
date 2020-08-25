package com.example.etinvetory;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.etinvetory.Models.Product;
import com.example.etinvetory.Models.ProductListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;

public class GetItemsFragment extends Fragment {
    private Button btnGetScanItems, btnSaveItems;
    public static TextView tv1;
    public static View view;
    private final int CAMERA_RESULT = 101;
    private DatabaseReference mDatabase;
    String productCode;
    Product newProduct = new Product();
    ListView listInventory;
    ArrayList<Product> productsList = new ArrayList<Product>();
    ArrayList<Product> DBList = new ArrayList<Product>();
    FirebaseUser user;
    DatabaseReference dbInventoryOut;
    HashMap<String, Integer> result = new HashMap<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_get_items,container,false);
;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        btnGetScanItems = view.findViewById(R.id.btnGetScanItems);
        tv1 = view.findViewById(R.id.tv1);
        listInventory = view.findViewById(R.id.listIventory);
        btnSaveItems = view.findViewById(R.id.btnSaveItems);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        dbInventoryOut = FirebaseDatabase.getInstance().getReference();





        // made to save the product's code from qr scan
        tv1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                productCode = tv1.getText().toString();
                getDocumentFromFirebase(productCode);
                checkIfFirebaseWriteOrUpdate(productCode);
            }
        });



        btnGetScanItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MANAGE Camera's Permission
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    Log.d("Permission","Permission is Granted");
                    scanQrCode();

                }
                else{
                    requestCameraPermission();
                }



            }



        });

        btnSaveItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                // Create DB Reference
                DatabaseReference mDbRef = mDatabase.child("inventory");
                DatabaseReference dbRefInventoryOut = mDatabase.child("inventoryOut").child(user.getUid());

//                //Loop through items will be save on inventory.
                int i= 0;
                for(i=0; i<productsList.size(); i++)
                {

                    int subtractQuantity = DBList.get(i).getQuantity() - productsList.get(i).getQuantity();
                    mDbRef.child(String.valueOf(productsList.get(i).getId())).child("quantity").setValue(subtractQuantity);

                    for (String key : result.keySet()) {
                        if(key.equals(String.valueOf(productsList.get(i).getId())))
                        {
                            int totalOutInventoryQuantity = result.get(key) + productsList.get(i).getQuantity();
                            dbRefInventoryOut.child(String.valueOf(productsList.get(i).getId())).child("quantity").setValue(totalOutInventoryQuantity);
                            if(productsList.size()>(i+1))
                            {
                                i++;
                            }

                        }
                    }
                    dbRefInventoryOut.child(String.valueOf(productsList.get(i).getId())).child("quantity").setValue(productsList.get(i).getQuantity());

                }


                    // Move to Home Fragment through InventoryDashboard Activity (It was easiest way)
                startActivity(new Intent(view.getContext(),InventoryDashboard.class));

            }
        });






        return view;
    }

    private void checkIfFirebaseWriteOrUpdate(String productId) {

        DatabaseReference dbRefInventoryOut = dbInventoryOut.child("inventoryOut").child(user.getUid());


        dbRefInventoryOut.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

               for(DataSnapshot ds : snapshot.getChildren())
               {

                   result.put(ds.getKey(),ds.child("quantity").getValue(Integer.class));
               }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Permission needed")
                    .setMessage("Camera is necessary to Scan QR Code!")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[] {Manifest.permission.CAMERA}, CAMERA_RESULT);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {Manifest.permission.CAMERA}, CAMERA_RESULT);
        }


    }

    public void displayList(Product product)
    {
        // Set up temp number for quantity
        int newProductQuantity = 0;

        // After Scan one item, the Button to save Products will be VISIBLE.
        btnSaveItems.setVisibility(View.VISIBLE);

        if(productsList.size() == 0)
        {
            productsList.add(product);
        } else {
            // check if we have same product on the list.
            for(int i=0; i<productsList.size(); i++)
            {
                System.out.println(productsList.get(i));
                if(productsList.get(i).getId() == product.getId())
                {
                    newProductQuantity = productsList.get(i).getQuantity() + 1;
                    productsList.get(i).setQuantity(newProductQuantity);
                }
            }
            if(newProductQuantity == 0)
            {
                //Add the Person objects to an ArrayList
                productsList.add(product);
            }
        }



        // Set the customized Adapter for Product.
        ProductListAdapter adapter = new ProductListAdapter(view.getContext(), R.layout.custom_lv, productsList);
        listInventory.setAdapter(adapter);
    }

    private void scanQrCode()
    {
        startActivity(new Intent(getActivity(), ScanCameraActivity.class));

    }

    private void getDocumentFromFirebase(String id)
    {
        mDatabase.child("inventory").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get Product object according ID
                 newProduct = snapshot.getValue(Product.class);
                 // This list will keep original quantities from DB
                DBList.add(snapshot.getValue(Product.class));
                 //set the product as 1 to save indiviual products
                 newProduct.setQuantity(1);
              displayList(newProduct);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}

