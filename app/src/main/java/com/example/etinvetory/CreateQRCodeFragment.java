package com.example.etinvetory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.print.PrintHelper;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreateQRCodeFragment extends Fragment {
    private ImageView imgViewQRCode;
    private Button btnGenerateQRCode, btnPrint;
    EditText etId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_create_qrcode,container,false);
        imgViewQRCode = view.findViewById(R.id.imgViewQrCode);
        btnGenerateQRCode = view.findViewById(R.id.btnGenerate);
        btnPrint = view.findViewById(R.id.btnPrint);
        etId = view.findViewById(R.id.etId);

        btnGenerateQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etId.getText().toString()))
                {
                    Toast toast = Toast.makeText(
                            //getActivity(),"Custom Toast From Fragment",Toast.LENGTH_LONG
                            getActivity().getApplicationContext(), "Please, Type Product ID!!", Toast.LENGTH_LONG
                    );
                    // Set the Toast display position layout center
                    toast.setGravity(Gravity.CENTER,0,0);
                    // Finally, show the toast
                    toast.show();
                } else
                {
                    generateQRCode();
                    btnPrint.setVisibility(View.VISIBLE);
                    btnGenerateQRCode.setVisibility(View.INVISIBLE);
                }
            }
        });




        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPhotoPrint();
                etId.setText("");
                btnGenerateQRCode.setVisibility(View.VISIBLE);
                btnPrint.setVisibility(View.INVISIBLE);
                imgViewQRCode.setVisibility(View.INVISIBLE);
            }
        });

        return view;
    }


    private void generateQRCode()
    {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

    int QrWidthAndHeight = 40;
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(etId.getText().toString().trim(), BarcodeFormat.QR_CODE, QrWidthAndHeight,QrWidthAndHeight);
            Bitmap bitMap = Bitmap.createBitmap(QrWidthAndHeight,QrWidthAndHeight, Bitmap.Config.RGB_565);


            for (int x = 0; x<QrWidthAndHeight; x++)
            {
                for (int y =0; y<QrWidthAndHeight; y++)
                {
                    bitMap.setPixel(x,y, bitMatrix.get(x,y)? Color.BLACK : Color.WHITE);
                }
            }
            imgViewQRCode.setImageBitmap(bitMap);
            imgViewQRCode.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    private void doPhotoPrint() {
        BitmapDrawable drawable = (BitmapDrawable) imgViewQRCode.getDrawable();
        Bitmap bitmapConverted = drawable.getBitmap();


        // create a Print job from bitmap.
        PrintHelper photoPrinter = new PrintHelper(getActivity());
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        photoPrinter.printBitmap(etId.getText().toString() + " - print", bitmapConverted);
    }




}
