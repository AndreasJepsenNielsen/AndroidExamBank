package com.example.bankapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.bankapp.Model.AccountModel;
import com.example.bankapp.Model.CustomerModel;
import com.example.bankapp.R;
import org.apache.commons.lang3.RandomStringUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Properties;

public class NemIdActivity extends AppCompatActivity {

    static String nemId;
    Button verifyButton;
    EditText inputCode;
    CustomerModel userDetails;
    AccountModel account;
    ArrayList<AccountModel> accounts;
    String choice;
    private static final int TEN_MINUTES = 10 * 60 * 1000;
    private long timeStamp10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nem_id);
        choice = getIntent().getStringExtra(getString(R.string.clicked));
        userDetails = getIntent().getParcelableExtra(getString(R.string.intentUser));
        account = getIntent().getParcelableExtra(getString(R.string.intentAccount));
        accounts = getIntent().getParcelableArrayListExtra(getString(R.string.intentAccounts));
        /***
         * Strict is here so that we dont get the NetWorkOnMAinThreadException, another way around this is using AsyncTask
         */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        init();
        sendMail();

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (System.currentTimeMillis() >= timeStamp10) {
                    Toast.makeText(NemIdActivity.this, getString(R.string.codeSessionRunOut), Toast.LENGTH_LONG).show();
                    sendMail();
                } else {
                    if (inputCode.getText().toString().equals(nemId)) {
                        if (choice.equals(getString(R.string.transfer))) {
                            Intent transferIntent = new Intent(NemIdActivity.this, TransferMoneyOthersActivity.class);
                            transferIntent.putExtra(getString(R.string.intentUser), userDetails);
                            transferIntent.putExtra(getString(R.string.intentAccount), account);
                            startActivity(transferIntent);
                            finish();
                        } else {
                            Intent transferIntent = new Intent(NemIdActivity.this, PayBillsActivity.class);
                            transferIntent.putExtra(getString(R.string.intentUser), userDetails);
                            transferIntent.putExtra(getString(R.string.intentAccount), account);
                            transferIntent.putExtra(getString(R.string.intentAccounts), accounts);
                            startActivity(transferIntent);
                            finish();
                        }
                    } else {
                        Toast.makeText(NemIdActivity.this, getString(R.string.nemIdIncorrect), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    /**
     * sends a mail to the user with the Nemid code
     */
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
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {

            Message message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(userDetails.getEmail()));
            message.setSubject(getString(R.string.nemIdCode));
            message.setText(getString(R.string.nemIdCodeExpires)
                    + "\n\n" + nemId);
            Transport.send(message);
        } catch (MessagingException e) {

            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     * generate a random number with 6 digits
     */
    public String generateNemId() {
        String generatedString = RandomStringUtils.random(6, "0123456789");
        timeStamp10 = System.currentTimeMillis() + TEN_MINUTES;
        return generatedString;
    }

    private void init() {
        verifyButton = findViewById(R.id.verifyButton);
        inputCode = findViewById(R.id.inputCode);
    }
}
