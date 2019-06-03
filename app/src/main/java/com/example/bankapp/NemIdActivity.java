package com.example.bankapp;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bankapp.Model.AccountModel;
import com.example.bankapp.Model.CustomerModel;


import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;


import org.apache.commons.lang3.RandomStringUtils;

public class NemIdActivity extends AppCompatActivity{

    static String nemId;
    Button verifyButton;
    EditText inputCode;
    CustomerModel userDetails;
    AccountModel account;
    String choice;
    private static final int TEN_MINUTES = 10 * 60 * 1000;
    private long timeStamp10;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nem_id);
        choice = getIntent().getStringExtra(getString(R.string.clicked));
        userDetails = getIntent().getParcelableExtra(getString(R.string.intentUser));
        account = getIntent().getParcelableExtra(getString(R.string.intentAccounts));
        verifyButton = findViewById(R.id.verifyButton);
        inputCode = findViewById(R.id.inputCode);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        sendMail();


        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(System.currentTimeMillis() >= timeStamp10){
                    Toast.makeText(NemIdActivity.this, "This code session has run out, sending you a new code", Toast.LENGTH_LONG).show();
                    sendMail();
                }else{
                    if(inputCode.getText().toString().equals(nemId)){
                        System.out.println("Rigtig kode");
                        if(choice.equals(getString(R.string.transfer))){
                            Intent transferIntent = new Intent(NemIdActivity.this, TransferMoneyOthersActivity.class);
                            transferIntent.putExtra(getString(R.string.intentUser), userDetails);
                            transferIntent.putExtra(getString(R.string.intentAccounts), account);
                            startActivity(transferIntent);
                        }else{
                            System.out.println("WRong");
                        }
                    }else{
                        Toast.makeText(NemIdActivity.this, "NemId is incorrect", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

    }








    private void sendMail() {

        nemId = generateNemId();

        final String username = "postitpython@gmail.com";
        final String password = "postit1234";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(userDetails.getEmail()));
            message.setSubject("NemId Code");
            message.setText("Here is your nemId Code it expires in 10 minutes: "
                    + "\n\n" + nemId );

            Transport.send(message);

            System.out.println("Done");

        }

        catch (MessagingException e)
        {
            // throw new RuntimeException(e);
            System.out.println("Username or Password are incorrect ... exiting !");
        }
    }



    public String generateNemId() {
        String generatedString = RandomStringUtils.random(6,"0123456789");

        System.out.println(generatedString);

        timeStamp10 = System.currentTimeMillis() + TEN_MINUTES;

        System.out.println(System.currentTimeMillis());
        System.out.println(timeStamp10);

        return generatedString;
    }






}
